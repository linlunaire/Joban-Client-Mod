package com.jsblock.data

import com.google.gson.JsonElement
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap
import net.minecraft.resources.Identifier

open class PIDSPreset(
    @JvmField var image: Identifier?,
    @JvmField var showWeather: Boolean,
    @JvmField var showClock: Boolean,
    @JvmField var customTextPushArrival: Boolean,
    @JvmField var visibility: BooleanArray?,
    @JvmField var color: Int?,
    @JvmField var font: String?,
    private var carLengthColorMap: Int2IntArrayMap?
) {
    open fun getCarColor(car: Int): Int? = carLengthColorMap?.let {
        if (it.containsKey(car - 1)) it.get(car - 1) else null
    }

    companion object {
        @JvmStatic
        fun fromJson(element: JsonElement?): PIDSPreset {
            val json = element!!.asJsonObject
            val showWeather = if (json.has("showWeather")) json.get("showWeather").asBoolean else true
            val showClock = if (json.has("showClock")) json.get("showClock").asBoolean else true
            val customTextPushArrival = json.has("customTextPushArrival") && json.get("customTextPushArrival").asBoolean
            val fonts = if (json.has("fonts")) json.get("fonts").asString else null
            val hexColor = if (json.has("color")) json.get("color").asString else null
            val carLengthColor = if (json.has("carLengthColor")) json.get("carLengthColor").asJsonArray else null
            val hiddenRowList = if (json.has("hideRow")) json.get("hideRow").asJsonArray else null
            val background = Identifier.parse(json.get("background").asString)
            val color = hexColor?.let { Integer.parseInt(it, 16) }
            val hiddenRows = hiddenRowList?.let { rows ->
                BooleanArray(4).also { values ->
                    for (i in 0 until minOf(rows.size(), values.size)) values[i] = rows.get(i).asBoolean
                }
            }
            val colors = carLengthColor?.let { rows ->
                Int2IntArrayMap().also { values ->
                    for (i in 0 until rows.size()) {
                        val value = rows.get(i)
                        if (!value.isJsonNull) values.put(i, Integer.parseInt(value.asString, 16))
                    }
                }
            }
            return PIDSPreset(background, showWeather, showClock, customTextPushArrival, hiddenRows, color, fonts, colors)
        }
    }
}
