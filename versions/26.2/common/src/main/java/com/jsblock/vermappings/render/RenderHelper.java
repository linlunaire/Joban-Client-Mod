package com.jsblock.vermappings.render;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Extraction owns blending in 26.2; colour belongs to each queued draw. */
public final class RenderHelper {
   private RenderHelper() {
   }

   public static void fill(GuiGraphicsExtractor graphics, int x, int y, int width, int height, int argb) {
      graphics.fill(x, y, x + width, y + height, argb);
   }

   /** Matches the legacy Font alpha fallback for RGB-only PIDS and sign colours. */
   public static int legacyTextColor(int color) {
      return (color & 0xFC000000) == 0 ? color | 0xFF000000 : color;
   }
}
