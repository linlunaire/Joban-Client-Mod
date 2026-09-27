package com.jsblock.screen;

import com.jsblock.packet.PacketClient;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;

public class SubsidyMachineScreen extends ScreenMapper implements IGui {
   private final WidgetIntegerTextField textBoxPricePerClick;
   private final WidgetIntegerTextField textBoxTimeout;
   private final BlockPos pos;
   private int pricePerClick;
   private int timeout;
   private static final int TEXT_PADDING = 16;
   private static final int TEXT_FIELD_WIDTH = 100;
   private static final int FINAL_TEXT_HEIGHT = 24;
   private static final int MAX_TEXT_LENGTH = 6;

   public SubsidyMachineScreen(BlockPos pos, int pricePerClick, int timeout) {
      super(Text.literal(""));
      this.pos = pos;
      this.pricePerClick = pricePerClick;
      this.timeout = timeout;
      this.textBoxPricePerClick = new WidgetIntegerTextField(10, true, 6);
      this.textBoxTimeout = new WidgetIntegerTextField(0, true, 6);
   }

   protected void init() {
      super.init();
      int i = 1;
      IDrawing.setPositionAndWidth(this.textBoxPricePerClick, this.width - 20 - 100, 24 * i++ + 20, 100);
      IDrawing.setPositionAndWidth(this.textBoxTimeout, this.width - 20 - 100, 24 * i++ + 20, 100);
      this.textBoxPricePerClick.setValue(String.valueOf(this.pricePerClick));
      this.textBoxTimeout.setValue(String.valueOf(this.timeout));
      this.addDrawableChild(this.textBoxPricePerClick);
      this.addDrawableChild(this.textBoxTimeout);
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
      try {
         this.renderBackground(guiGraphics, mouseX, mouseY, 0.0F);
         guiGraphics.drawCenteredString(this.font, Text.translatable("block.jsblock.subsidy_machine_1", new Object[0]), this.width / 2, 16, -1);
         int i = 1;
         guiGraphics.drawString(this.font, Text.translatable("gui.jsblock.subsidyScreen.price", new Object[0]), 20, 24 * i++ + 20, -1);
         guiGraphics.drawString(this.font, Text.translatable("gui.jsblock.subsidyScreen.timeout", new Object[0]), 20, 24 * i++ + 20, -1);
         super.render(guiGraphics, mouseX, mouseY, delta);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public void onClose() {
      this.pricePerClick = this.textBoxPricePerClick.getIntegerValue(1);
      this.timeout = this.textBoxTimeout.getIntegerValue(0);
      PacketClient.sendSubsidyConfigC2S(this.pos, this.pricePerClick, this.timeout);
      super.onClose();
   }

   public boolean isPauseScreen() {
      return false;
   }
}
