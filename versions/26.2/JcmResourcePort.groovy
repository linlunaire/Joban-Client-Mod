import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/** Pure build-time conversions; never reads or modifies worlds or the original resources. */
class JcmResourcePort {
    // Match the neighboring APG glass base models; do not rewrite unrelated legacy assets.
    static final Map<String, String> MODEL_PARTICLES = [
        'assets/jsblock/models/block/psdapg/drlapg/apg_glass_end_bottom.json': 'mtr:item/apg_glass',
        'assets/jsblock/models/block/psdapg/drlapg/apg_glass_end_top.json': 'mtr:item/apg_glass'
    ].asImmutable()

    static Set<String> registeredItems(String initializer) {
        Set<String> result = new LinkedHashSet<>()
        (initializer =~ /register(?:BlockItem|Item)\.accept\("([^"]+)"/).each { result.add(it[1]) }
        if (result.isEmpty()) throw new IllegalArgumentException('No item registrations found in Joban.init')
        result
    }

    static String targetPath(String path) {
        path.replaceFirst(/^(data\/[^\/]+\/)recipes\//, '$1recipe/')
                .replaceFirst(/^(data\/[^\/]+\/)loot_tables\//, '$1loot_table/')
                .replaceFirst(/^(data\/[^\/]+\/tags\/)blocks\//, '$1block/')
                .replaceFirst(/^(data\/[^\/]+\/tags\/)items\//, '$1item/')
    }

    static Map convert(String path, Map original) {
        Map result = new JsonSlurper().parseText(JsonOutput.toJson(original)) as Map
        String target = targetPath(path)
        if (target ==~ /data\/[^\/]+\/recipe\/.+\.json/) {
            if (result.type == 'minecraft:crafting_shaped') {
                result.key = result.key.collectEntries { key, value -> [(key): ingredient(value)] }
            } else if (result.type == 'minecraft:crafting_shapeless') {
                result.ingredients = result.ingredients.collect { ingredient(it) }
            } else {
                throw new IllegalArgumentException("Unsupported recipe requires review: ${path}: ${result.type}")
            }
            if (!(result.result instanceof Map) || !(result.result.id ?: result.result.item)) throw new IllegalArgumentException("Invalid recipe result: ${path}")
            if (result.result.containsKey('item')) result.result.id = result.result.remove('item')
            result.result.id = itemId(result.result.id)
        } else if (target ==~ /data\/[^\/]+\/loot_table\/.+\.json/) {
            convertLoot(result)
        } else if (MODEL_PARTICLES.containsKey(target)) {
            if (!(result.textures instanceof Map)) throw new IllegalArgumentException("Missing model textures: ${path}")
            if (!result.textures.containsKey('particle')) result.textures.particle = MODEL_PARTICLES[target]
        }
        result
    }

    private static Object ingredient(Object value) {
        if (value instanceof String) return value.startsWith('#') ? value : itemId(value)
        if (value instanceof Map && value.size() == 1) {
            if (value.item instanceof String) return itemId(value.item)
            if (value.tag instanceof String) return '#' + value.tag
        }
        if (value instanceof List && !value.isEmpty()) {
            List converted = value.collect { ingredient(it) }
            if (converted.every { it instanceof String && !it.startsWith('#') }) return converted
        }
        throw new IllegalArgumentException("Unsupported ingredient requires review: ${value}")
    }

    private static String itemId(String id) { id == 'minecraft:chain' ? 'minecraft:iron_chain' : id }

    private static void convertLoot(Object value) {
        if (value instanceof Map) {
            if (value.condition == 'minecraft:alternative') value.condition = 'minecraft:any_of'
            value.values().each { convertLoot(it) }
        } else if (value instanceof List) value.each { convertLoot(it) }
    }

    static Map itemDefinition(String namespace, String path) {
        [model: [type: 'minecraft:model', model: "${namespace}:item/${path}"]]
    }
}
