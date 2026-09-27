package com.jsblock.data

open class ScreenRoot(
    @JvmField var screenAlignment: ScreenAlignment?,
    @JvmField var widthFactor: Float,
    @JvmField var heightFactor: Float
) {
    @JvmField var width: Int = 0
    @JvmField var height: Int = 0
    @JvmField var startX: Int = 0

    open fun init(screenWidth: Float, screenHeight: Float) {
        width = (screenWidth / widthFactor).toInt()
        height = (screenHeight / heightFactor).toInt()
        startX = getXPadding(width.toFloat(), screenWidth)
    }

    companion object {
        @JvmStatic
        fun getXPadding(screenWidth: Float, totalWidth: Float): Int = (totalWidth - screenWidth).toInt() / 2
    }
}
