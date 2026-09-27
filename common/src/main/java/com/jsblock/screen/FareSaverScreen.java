package com.jsblock.screen;

import com.jsblock.packet.PacketClient;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;

public class FareSaverScreen extends ScreenMapper implements IGui {
   private final WidgetIntegerTextField textBoxDiscount;
   private final BlockPos pos;
   private int discount;
   private static final int DEFAULT_DISCOUNT = 2;
   private static final int MAX_TEXT_LENGTH = 4;
   private static final int TEXT_PADDING = 16;
   private static final int TEXT_FIELD_WIDTH = 100;
   private static final int FINAL_TEXT_HEIGHT = 24;

   public FareSaverScreen(BlockPos pos, int discount) {
      super(Text.literal(""));
      this.pos = pos;
      this.discount = discount;
      this.textBoxDiscount = new WidgetIntegerTextField(2, true, 4);
   }

   protected void init() {
      super.init();
      int i = 1;
      IDrawing.setPositionAndWidth(this.textBoxDiscount, this.width - 20 - 100, 24 * i++ + 20, 100);
      this.textBoxDiscount.setValue(String.valueOf(this.discount));
      this.addDrawableChild(this.textBoxDiscount);
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
      try {
         this.renderBackground(guiGraphics, mouseX, mouseY, 0.0F);
         guiGraphics.drawCenteredString(this.font, Text.translatable("block.jsblock.faresaver_1", new Object[0]), this.width / 2, 16, -1);
         int i = 1;
         guiGraphics.drawString(this.font, Text.translatable("gui.jsblock.faresaver.discount", new Object[0]), 20, 24 * i++ + 20, -1);
         super.render(guiGraphics, mouseX, mouseY, delta);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public void onClose() {
      this.discount = this.textBoxDiscount.getIntegerValue(1);
      PacketClient.sendFaresaverConfigC2S(this.pos, this.discount);
      super.onClose();
   }

   public boolean isPauseScreen() {
      return false;
   }
}
