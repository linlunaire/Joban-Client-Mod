package com.jsblock.data

import java.util.ArrayList
import net.minecraft.client.gui.components.AbstractWidget

open class InlineComponentEntry(
    @JvmField var horizontalAlignment: ScreenAlignment?,
    @JvmField var verticalAlignment: ScreenAlignment?,
    vararg widget: AbstractWidget?
) {
    @JvmField var availableWidth: Int = 0
    @JvmField var widgetList: MutableList<AbstractWidget?>? = ArrayList(widget.asList())

    open fun setAvailableWidth(availableWidth: Int) {
        this.availableWidth = availableWidth
    }

    open fun calculateWidth(): Int = availableWidth / widgetList!!.size - MARGIN

    companion object {
        const val MARGIN: Int = 10
    }
}
