package com.jsblock.screen;

import com.jsblock.block.BlockPIDSBaseHorizontal;
import com.jsblock.block.JobanPIDSBase;
import com.jsblock.client.JobanCustomResources;
import com.jsblock.packet.PacketClient;
import java.util.HashSet;
import java.util.Set;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.packet.IPacket;
import mtr.screen.PIDSConfigScreen;
import mtr.screen.WidgetBetterCheckbox;
import mtr.screen.WidgetBetterTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class JobanPIDSConfigScreen extends ScreenMapper implements IGui, IPacket {
   private final BlockPos pos1;
   private final BlockPos pos2;
   private final String[] messages;
   private final boolean[] hideArrival;
   private String presetID;
   private final WidgetBetterTextField[] textFieldMessages;
   private final WidgetBetterCheckbox[] buttonsHideArrival;
   private final WidgetSuggestionTextField presetIDTextField;
   private final Component messageText = Text.translatable("gui.mtr.pids_message", new Object[0]);
   private final Component hideArrivalText = Text.translatable("gui.mtr.hide_arrival", new Object[0]);
   private final Component presetText = Text.translatable("gui.jsblock.pids_preset", new Object[0]);
   private final WidgetBetterCheckbox selectAllCheckbox;
   private final Button filterButton;
   private final Set<Long> filterPlatformIds;
   private static final int MAX_MESSAGE_LENGTH = 2048;

   public JobanPIDSConfigScreen(BlockPos pos1, BlockPos pos2, int maxArrivals, String presetID) {
      super(Text.literal(""));
      this.pos1 = pos1;
      this.pos2 = pos2;
      this.presetID = presetID;
      this.messages = new String[maxArrivals];


      for(int i = 0; i < maxArrivals; ++i) {
         this.messages[i] = "";
      }

      this.hideArrival = new boolean[maxArrivals];
      this.textFieldMessages = new WidgetBetterTextField[maxArrivals];

      for(int i = 0; i < maxArrivals; ++i) {
         this.textFieldMessages[i] = new WidgetBetterTextField("", 2048);
      }

      this.selectAllCheckbox = new WidgetBetterCheckbox(0, 0, 0, 20, Text.translatable("gui.mtr.automatically_detect_nearby_platform", new Object[0]), (checked) -> {
      });
      this.presetIDTextField = new WidgetSuggestionTextField("None", JobanCustomResources.PIDSPresets.keySet(), 2048, true);
      this.buttonsHideArrival = new WidgetBetterCheckbox[maxArrivals];

      for(int i = 0; i < maxArrivals; ++i) {
         this.buttonsHideArrival[i] = new WidgetBetterCheckbox(0, 0, 0, 20, this.hideArrivalText, (checked) -> {
         });
      }

      Level world = Minecraft.getInstance().level;
      if (world != null) {
         BlockEntity entity = world.getBlockEntity(pos1);
         if (entity instanceof JobanPIDSBase.TileEntityBlockJobanPIDS) {
            this.filterPlatformIds = ((BlockPIDSBaseHorizontal.TileEntityBlockPIDSBaseHorizontal)entity).getPlatformIds();

            for(int i = 0; i < maxArrivals; ++i) {
               this.messages[i] = ((JobanPIDSBase.TileEntityBlockJobanPIDS)entity).getMessage(i);
               this.hideArrival[i] = ((JobanPIDSBase.TileEntityBlockJobanPIDS)entity).getHideArrival(i);
            }
         } else {
            this.filterPlatformIds = new HashSet();
         }
      } else {
         this.filterPlatformIds = new HashSet();
      }

      this.filterButton = PIDSConfigScreen.getPlatformFilterButton(pos1, this.selectAllCheckbox, this.filterPlatformIds, this);
   }

   protected void init() {
      super.init();
      int textWidth = this.font.width(this.hideArrivalText) + 20 + 12;

      for(int i = 0; i < this.textFieldMessages.length; ++i) {
         WidgetBetterTextField textFieldMessage = this.textFieldMessages[i];
         IDrawing.setPositionAndWidth(textFieldMessage, 22, 102 + 24 * i, this.width - 40 - 4 - textWidth);
         textFieldMessage.setValue(this.messages[i]);
         this.addDrawableChild(textFieldMessage);
         WidgetBetterCheckbox buttonHideArrival = this.buttonsHideArrival[i];
         IDrawing.setPositionAndWidth(buttonHideArrival, this.width - 20 - textWidth + 6, 102 + 24 * i, textWidth);
         buttonHideArrival.setChecked(this.hideArrival[i]);
         this.addDrawableChild(buttonHideArrival);
         IDrawing.setPositionAndWidth(this.selectAllCheckbox, 20, 20, 144);
         this.selectAllCheckbox.setChecked(this.filterPlatformIds.isEmpty());
         this.addDrawableChild(this.selectAllCheckbox);
         IDrawing.setPositionAndWidth(this.filterButton, 20, 60, 72);
         this.filterButton.setMessage(Text.translatable("selectWorld.edit", new Object[0]));
         this.addDrawableChild(this.filterButton);
      }

      IDrawing.setPositionAndWidth(this.presetIDTextField, this.width - 20 - textWidth + 6, 102 + 24 * this.textFieldMessages.length, textWidth);
      this.presetIDTextField.setValue(this.presetID);
      this.addDrawableChild(this.presetIDTextField);
   }

   public void tick() {
      WidgetBetterTextField[] var1 = this.textFieldMessages;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         WidgetBetterTextField textFieldMessage = var1[var3];

      }

   }

   public void onClose() {
      for(int i = 0; i < this.textFieldMessages.length; ++i) {
         this.messages[i] = this.textFieldMessages[i].getValue();
         this.hideArrival[i] = this.buttonsHideArrival[i].selected();
      }

      if (this.selectAllCheckbox.selected()) {
         this.filterPlatformIds.clear();
      }

      this.presetID = this.presetIDTextField.getValue();
      PacketClient.sendJobanPIDSConfigC2S(this.pos1, this.pos2, this.messages, this.hideArrival, this.filterPlatformIds, this.presetID);
      super.onClose();
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
      try {
         this.renderBackground(guiGraphics, mouseX, mouseY, 0.0F);
         super.render(guiGraphics, mouseX, mouseY, delta);
         guiGraphics.drawString(this.font, Text.translatable("gui.mtr.filtered_platforms", new Object[]{this.selectAllCheckbox.selected() ? 0 : this.filterPlatformIds.size()}), 20, 46, -1);
         guiGraphics.drawString(this.font, this.messageText, 20, 86, -1);
         guiGraphics.drawString(this.font, this.presetText, 20, 206, -1);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public boolean isPauseScreen() {
      return false;
   }
}
