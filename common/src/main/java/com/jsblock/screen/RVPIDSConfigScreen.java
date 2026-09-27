package com.jsblock.screen;

import com.jsblock.block.PIDSRVBase;
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

public class RVPIDSConfigScreen extends ScreenMapper implements IGui, IPacket {
   private final BlockPos pos1;
   private final BlockPos pos2;
   private final String[] messages;
   private final boolean[] hideArrival;
   private String presetID;
   private boolean hidePlatformNumber;
   private final WidgetBetterTextField[] textFieldMessages;
   private final WidgetBetterCheckbox[] buttonsHideArrival;
   private final WidgetBetterCheckbox buttonsHidePlatformNumbers;
   private final WidgetSuggestionTextField presetIDTextField;
   private final WidgetBetterCheckbox selectAllCheckbox;
   private final Button filterButton;
   private final Set<Long> filterPlatformIds;
   private final Component messageText = Text.translatable("gui.mtr.pids_message", new Object[0]);
   private final Component hideArrivalText = Text.translatable("gui.mtr.hide_arrival", new Object[0]);
   private final Component hidePlatformNumberText = Text.translatable("gui.jsblock.hide_platform_number", new Object[0]);
   private final Component presetText = Text.translatable("gui.jsblock.pids_preset", new Object[0]);
   private static final int MAX_MESSAGE_LENGTH = 2048;

   public RVPIDSConfigScreen(BlockPos pos1, BlockPos pos2, int maxArrivals, boolean hidePlatformNumber, String presetID) {
      super(Text.literal(""));
      this.pos1 = pos1;
      this.pos2 = pos2;
      this.hidePlatformNumber = hidePlatformNumber;
      this.presetID = presetID;
      this.messages = new String[maxArrivals];


      for(int i = 0; i < maxArrivals; ++i) {
         this.messages[i] = "";
      }

      this.hideArrival = new boolean[maxArrivals];
      this.filterPlatformIds = new HashSet();
      this.textFieldMessages = new WidgetBetterTextField[maxArrivals];

      for(int i = 0; i < maxArrivals; ++i) {
         this.textFieldMessages[i] = new WidgetBetterTextField("", 2048);
      }

      this.selectAllCheckbox = new WidgetBetterCheckbox(0, 0, 0, 20, Text.translatable("gui.mtr.automatically_detect_nearby_platform", new Object[0]), (checked) -> {
      });
      this.presetIDTextField = new WidgetSuggestionTextField("None", JobanCustomResources.PIDSPresets.keySet(), 2048, true);
      this.buttonsHideArrival = new WidgetBetterCheckbox[maxArrivals];
      this.buttonsHidePlatformNumbers = new WidgetBetterCheckbox(0, 0, 0, 20, Text.literal(""), (checked) -> {
      });

      for(int i = 0; i < maxArrivals; ++i) {
         this.buttonsHideArrival[i] = new WidgetBetterCheckbox(0, 0, 0, 20, this.hideArrivalText, (checked) -> {
         });
      }

      Level world = Minecraft.getInstance().level;
      if (world != null) {
         BlockEntity entity = world.getBlockEntity(pos1);
         if (entity instanceof PIDSRVBase.TileEntityBlockRVPIDS) {
            this.filterPlatformIds.addAll(((PIDSRVBase.TileEntityBlockRVPIDS)entity).getPlatformIds());

            for(int i = 0; i < maxArrivals; ++i) {
               this.messages[i] = ((PIDSRVBase.TileEntityBlockRVPIDS)entity).getMessage(i);
               this.hideArrival[i] = ((PIDSRVBase.TileEntityBlockRVPIDS)entity).getHideArrival(i);
            }
         }
      }

      this.filterButton = PIDSConfigScreen.getPlatformFilterButton(pos1, this.selectAllCheckbox, this.filterPlatformIds, this);
   }

   protected void init() {
      super.init();
      int textWidth = this.font.width(this.hideArrivalText) + 20 + 12;
      int startY = 20;
      IDrawing.setPositionAndWidth(this.selectAllCheckbox, 20, startY, 144);
      this.selectAllCheckbox.setChecked(this.filterPlatformIds.isEmpty());
      this.addDrawableChild(this.selectAllCheckbox);
      startY += 20;
      IDrawing.setPositionAndWidth(this.filterButton, 20, startY, 72);
      this.filterButton.setMessage(Text.translatable("selectWorld.edit", new Object[0]));
      this.addDrawableChild(this.filterButton);
      startY += 20;

      for(int i = 0; i < this.textFieldMessages.length; ++i) {
         WidgetBetterTextField textFieldMessage = this.textFieldMessages[i];
         IDrawing.setPositionAndWidth(textFieldMessage, 22, startY + 2 + 24, this.width - 40 - 4 - textWidth);
         textFieldMessage.setValue(this.messages[i]);
         this.addDrawableChild(textFieldMessage);
         WidgetBetterCheckbox buttonHideArrival = this.buttonsHideArrival[i];
         IDrawing.setPositionAndWidth(buttonHideArrival, this.width - 20 - textWidth + 6, startY + 2 + 24, textWidth);
         buttonHideArrival.setChecked(this.hideArrival[i]);
         this.addDrawableChild(buttonHideArrival);
         startY += 23;
      }

      IDrawing.setPositionAndWidth(this.buttonsHidePlatformNumbers, this.width - 20 - textWidth + 6, startY + 2 + 24, textWidth);
      this.buttonsHidePlatformNumbers.setChecked(this.hidePlatformNumber);
      this.addDrawableChild(this.buttonsHidePlatformNumbers);
      startY += 23;
      IDrawing.setPositionAndWidth(this.presetIDTextField, this.width - 20 - textWidth + 6, startY + 2 + 24, textWidth);
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

      this.hidePlatformNumber = this.buttonsHidePlatformNumbers.selected();
      this.presetID = this.presetIDTextField.getValue();
      PacketClient.sendRVPIDSConfigC2S(this.pos1, this.pos2, this.messages, this.hideArrival, this.filterPlatformIds, this.hidePlatformNumber, this.presetID);
      super.onClose();
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
      try {
         this.renderBackground(guiGraphics, mouseX, mouseY, 0.0F);
         guiGraphics.drawString(this.font, Text.translatable("gui.mtr.filtered_platforms", new Object[]{this.selectAllCheckbox.selected() ? 0 : this.filterPlatformIds.size()}), 20 + this.filterButton.getWidth() + 6, 46, -1);
         guiGraphics.drawString(this.font, this.messageText, 20, 66, -1);
         guiGraphics.drawString(this.font, this.hidePlatformNumberText, 20, 186, -1);
         guiGraphics.drawString(this.font, this.presetText, 20, 206, -1);
         super.render(guiGraphics, mouseX, mouseY, delta);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public boolean isPauseScreen() {
      return false;
   }
}
