package com.jsblock.data

import net.minecraft.network.chat.Component

open class TextLabel(
    @JvmField var text: Component?,
    @JvmField var scale: Float,
    @JvmField var horizontalAlignment: ScreenAlignment?,
    y: Int
) {
    @JvmField var y: Double = y.toDouble()
}
