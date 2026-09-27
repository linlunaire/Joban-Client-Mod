package com.jsblock.screen;

import mtr.screen.WidgetBetterCheckbox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

/** JCM checkbox label owns its check marker; the vanilla default label would overlap it. */
public final class WidgetCheckbox26 extends WidgetBetterCheckbox {
   public WidgetCheckbox26(int x, int y, int width, int height, Component message, OnClick onClick) {
      super(x, y, width, height, message, onClick);
   }

   @Override
   public void renderWidget(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
      extractDefaultSprite(graphics);
      graphics.text(Minecraft.getInstance().font, (selected() ? "[x] " : "[ ] ") + getMessage().getString(),
         getX() + 4, getY() + (height - 8) / 2, 0xFFFFFFFF);
   }
}
