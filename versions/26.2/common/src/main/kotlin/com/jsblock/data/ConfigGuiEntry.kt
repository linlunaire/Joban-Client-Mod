package com.jsblock.data

import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.network.chat.MutableComponent

open class ConfigGuiEntry(
    @JvmField var text: MutableComponent?,
    @JvmField var widget: AbstractWidget?,
    @JvmField var widgetWidth: Int,
    @JvmField var widgetHeight: Int
) {
    @JvmField var y: Int = 0
}
