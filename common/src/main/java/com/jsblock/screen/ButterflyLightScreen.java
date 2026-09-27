package com.jsblock.screen;

import com.jsblock.packet.PacketClient;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;

public class ButterflyLightScreen extends ScreenMapper implements IGui {
   private final WidgetIntegerTextField textBoxCountdown;
   private final BlockPos pos;
   private int countdown;
   private static final int DEFAULT_DISCOUNT = 2;
   private static final int MAX_TEXT_LENGTH = 4;
   private static final int TEXT_PADDING = 16;
   private static final int TEXT_FIELD_WIDTH = 100;
   private static final int FINAL_TEXT_HEIGHT = 24;

   public ButterflyLightScreen(BlockPos pos, int countdown) {
      super(Text.literal(""));
      this.pos = pos;
      this.countdown = countdown;
      this.textBoxCountdown = new WidgetIntegerTextField(2, true, 4);
   }

   protected void init() {
      super.init();
      int i = 1;
      IDrawing.setPositionAndWidth(this.textBoxCountdown, this.width - 20 - 100, 24 * i++ + 20, 100);
      this.textBoxCountdown.setValue(String.valueOf(this.countdown));
      this.addDrawableChild(this.textBoxCountdown);
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
      try {
         this.renderBackground(guiGraphics, mouseX, mouseY, 0.0F);
         guiGraphics.drawCenteredString(this.font, Text.translatable("block.jsblock.butterfly_light", new Object[0]), this.width / 2, 16, -1);
         int i = 1;
         guiGraphics.drawString(this.font, Text.translatable("gui.jsblock.butterfly.countdown", new Object[0]), 20, 24 * i++ + 20, -1);
         super.render(guiGraphics, mouseX, mouseY, delta);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public void onClose() {
      this.countdown = this.textBoxCountdown.getIntegerValue(1);
      PacketClient.sendButterflyConfigC2S(this.pos, this.countdown);
      super.onClose();
   }

   public boolean isPauseScreen() {
      return false;
   }
}
