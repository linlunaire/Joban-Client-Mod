package com.jsblock.screen;

import com.jsblock.Joban;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.util.Mth;

public class IncompatibleVersionScreen extends ScreenMapper implements IGui {
   private final Button ignoreButton;
   private final String minVersion;
   private final String currentVersion;
   public static final int BUTTON_HEIGHT = 20;
   private static final int TEXT_PADDING = 16;
   private static final int FINAL_TEXT_HEIGHT = 24;

   public IncompatibleVersionScreen(String minVersion, String currentVersion) {
      super(Text.literal(""));
      this.minVersion = minVersion;
      this.currentVersion = currentVersion;
      this.ignoreButton = UtilitiesClient.newButton(Text.literal(""), (btn) -> {
         this.onClose();
         UtilitiesClient.setScreen(this.minecraft, (ScreenMapper)null);
      });
      this.ignoreButton.setMessage(Text.translatable("gui.jsblock.ignore", new Object[0]));
   }

   protected void init() {
      super.init();
      int btnWidth = Mth.clamp((int)((double)this.width / 1.25), 0, 380);
      IDrawing.setPositionAndWidth(this.ignoreButton, (this.width - btnWidth) / 2, this.height - 20, btnWidth);
      this.addDrawableChild(this.ignoreButton);
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
      try {
         this.renderBackground(guiGraphics, mouseX, mouseY, 0.0F);
         guiGraphics.drawCenteredString(this.font, Text.translatable("gui.jsblock.incompatible_title", new Object[0]).withStyle(ChatFormatting.RED), this.width / 2, 16, -1);
         int i = 1;
         guiGraphics.drawCenteredString(this.font, Text.translatable("gui.jsblock.incompatible_1", new Object[]{Joban.getVersion(), this.currentVersion}), this.width / 2, 24 * i++ + 20, -1);
         guiGraphics.drawCenteredString(this.font, Text.translatable("gui.jsblock.incompatible_2", new Object[]{this.minVersion}), this.width / 2, 24 * i++ + 20, -1);
         guiGraphics.drawCenteredString(this.font, Text.translatable("gui.jsblock.incompatible_3", new Object[0]), this.width / 2, 24 * i++ + 20, -1);
         super.render(guiGraphics, mouseX, mouseY, delta);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public void onClose() {
      super.onClose();
   }

   public boolean isPauseScreen() {
      return false;
   }
}
