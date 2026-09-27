package com.jsblock.data

enum class ScreenAlignment {
    HORZ_LEFT, HORZ_CENTER, HORZ_RIGHT, VERT_TOP, VERT_CENTER, VERT_BOTTOM;

    companion object {
        @JvmStatic
        fun getX(alignment: ScreenAlignment?, screenWidth: Int, componentWidth: Int): Int =
            when (alignment!!) {
                HORZ_RIGHT -> screenWidth - componentWidth
                HORZ_CENTER -> (screenWidth - componentWidth) / 2
                else -> 0
            }

        @JvmStatic
        fun getY(alignment: ScreenAlignment?, screenHeight: Int, componentHeight: Int): Int =
            when (alignment!!) {
                VERT_BOTTOM -> screenHeight - componentHeight
                VERT_CENTER -> screenHeight / 2 - componentHeight / 2
                else -> 0
            }
    }
}
