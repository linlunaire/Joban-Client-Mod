import groovy.json.JsonSlurper

File root = new File(args ? args[0] : '../..').canonicalFile
File target = new File(root, 'versions/26.2')
Map extensions = [:]
def shell = new GroovyShell(new Binding([rootProject: [ext: extensions]]))
shell.evaluate(new File(target, 'source-port.gradle'))
shell.evaluate(new File(target, 'loader-port.gradle'))
Closure convert = { String path ->
    def parts = path.split('/', 2)
    String source = new File(root, "${parts[0]}/src/main/java/${parts[1]}").getText('UTF-8')
    extensions.transformJcm26LoaderSource(extensions.transformJcm26CoreSource(source, path), path)
}
String joban = convert('common/com/jsblock/Joban.java')
assert joban.indexOf('registerServerS2CTypes') < joban.indexOf('registerBlockItem.accept')
assert joban.indexOf('registerServerS2CTypes') < joban.indexOf('registerPlayerJoinEvent')
String registrations = joban.substring(joban.indexOf('registerServerS2CTypes'), joban.indexOf('LOGGER.info'))
String[] s2c = ['PACKET_VERSION_CHECK', 'PACKET_OPEN_BUTTERFLY_CONFIG_SCREEN', 'PACKET_OPEN_FARESAVER_CONFIG_SCREEN', 'PACKET_OPEN_JOBAN_PIDS_CONFIG_SCREEN', 'PACKET_OPEN_RV_PIDS_CONFIG_SCREEN', 'PACKET_OPEN_SOUND_LOOPER_SCREEN', 'PACKET_OPEN_SUBSIDY_CONFIG_SCREEN']
s2c.each { assert registrations.count('IPacketJoban.' + it) == 1 }
assert joban.contains('Platform.getMod("mtr").getVersion()')
assert !joban.contains('return "3.3.0";')
assert joban.count('com.jsblock.integration.FareSaverIntegration.register();') == 1
assert joban.indexOf('FareSaverIntegration.register()') < joban.indexOf('registerBlockItem.accept')
String packet = convert('common/com/jsblock/packet/PacketClient.java')
assert !packet.contains('minecraft.screen') && !packet.contains('minecraft.execute(')
assert packet.count('minecraft.gui.screen()') == 5
String fabric = convert('fabric/com/jsblock/fabric/JobanFabric.java')
assert fabric.contains('new mtr.mappings.TooltipBlockItem') && fabric.contains('RegistrationContext.blockItemProperties(id(path), block.get())')
String forge = convert('neoforge/com/jsblock/mappings/ForgeUtilities.java')
assert !forge.contains('new ServerEntity(') && !forge.contains('RenderLevelStageEvent.Stage')
assert forge.contains('mtr.neoforge.mappings.ForgeUtilities.createAddEntityPacket(entity)')

def converter = new GroovyClassLoader(getClass().classLoader).parseClass(new File(target, 'JcmResourcePort.groovy'))
String initializer = new File(root, 'common/src/main/java/com/jsblock/Joban.java').getText('UTF-8')
Set ids = converter.registeredItems(initializer)
File resources = new File(root, 'common/src/main/resources')
int recipeCount = 0, lootCount = 0
new File(resources, 'data').eachFileRecurse { file ->
    if (!file.isFile() || !file.name.endsWith('.json')) return
    String relative = resources.toPath().relativize(file.toPath()).toString().replace('\\', '/')
    Map source = new JsonSlurper().parse(file) as Map
    Map migrated = converter.convert(relative, source)
    assert converter.convert(converter.targetPath(relative), migrated) == migrated
    if (relative.contains('/recipe/')) {
        recipeCount++
        def ingredients = migrated.type == 'minecraft:crafting_shaped' ? migrated.key.values() : migrated.ingredients
        assert ingredients.every { it instanceof String || it instanceof List }
        assert migrated.result.id instanceof String && !migrated.result.containsKey('item')
    }
    if (relative.contains('/loot_table/')) lootCount++
}
ids.each { id ->
    File model = new File(resources, "assets/jsblock/models/item/${id}.json")
    assert model.isFile(): "Missing model for jsblock:${id}"
    assert !(new JsonSlurper().parse(model) as Map).containsKey('overrides'): "Legacy model overrides need explicit migration: ${id}"
    assert converter.itemDefinition('jsblock', id).model.model == "jsblock:item/${id}"
}
assert converter.convert('data/example/recipes/test.json', [type:'minecraft:crafting_shapeless', ingredients:[[item:'minecraft:chain'], [tag:'minecraft:planks']], result:[item:'example:test']]) == [type:'minecraft:crafting_shapeless', ingredients:['minecraft:iron_chain','#minecraft:planks'], result:[id:'example:test']]
assert converter.targetPath('data/example/loot_tables/blocks/test.json') == 'data/example/loot_table/blocks/test.json'
assert converter.convert('data/example/loot_table/blocks/test.json', [condition:'minecraft:alternative']).condition == 'minecraft:any_of'
['bottom', 'top'].each { half ->
    String path = "assets/jsblock/models/block/psdapg/drlapg/apg_glass_end_${half}.json"
    Map original = new JsonSlurper().parse(new File(resources, path)) as Map
    String unchanged = groovy.json.JsonOutput.toJson(original)
    Map migrated = converter.convert(path, original)
    assert migrated.textures.particle == 'mtr:item/apg_glass': "Missing APG end particle: ${half}"
    assert converter.convert(path, migrated) == migrated
    migrated.textures.remove('particle')
    assert migrated == original: "Particle repair changed model geometry or face textures: ${half}"
    assert groovy.json.JsonOutput.toJson(original) == unchanged: 'Conversion mutated source model'
    String unrelatedPath = path.replace('/drlapg/', '/drlapg/old/')
    Map unrelated = new JsonSlurper().parse(new File(resources, unrelatedPath)) as Map
    assert converter.convert(unrelatedPath, unrelated) == unrelated: 'Unrelated old model was rewritten'
}
Map metadata = new JsonSlurper().parse(new File(target, 'fabric/src/main/resources/fabric.mod.json')) as Map
assert metadata.id == 'jsblock' && metadata.depends.minecraft == '26.2' && metadata.depends.java == '>=25'
assert metadata.depends.mtr == '>=' + '${minimumMtrVersion}'
println "PASS: seven early S2C types, packet/client source migration, loader item namespace/tooltips, ${ids.size()} registered item models, ${recipeCount} recipes, ${lootCount} loot tables, two targeted APG particle repairs (no game/codecs launched)"
