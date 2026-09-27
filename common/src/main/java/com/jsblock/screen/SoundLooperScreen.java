package com.jsblock.screen;

import com.jsblock.block.SoundLooper;
import com.jsblock.packet.PacketClient;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.screen.WidgetBetterCheckbox;
import mtr.screen.WidgetBetterTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SoundLooperScreen extends ScreenMapper implements IGui {
   private final WidgetBetterTextField textBoxSoundId;
   private final WidgetIntegerTextField textBoxSoundVolume;
   private final WidgetIntegerTextField textBoxRepeatTick;
   private final WidgetIntegerTextField textBoxx1;
   private final WidgetIntegerTextField textBoxx2;
   private final WidgetIntegerTextField textBoxy1;
   private final WidgetIntegerTextField textBoxy2;
   private final WidgetIntegerTextField textBoxz1;
   private final WidgetIntegerTextField textBoxz2;
   private final WidgetBetterCheckbox checkBoxLimitRange;
   private final WidgetBetterCheckbox checkBoxNeedRedstone;
   private final Button buttonCategory;
   private final BlockPos pos;
   private int selectedCategory;
   private static final int VOLUME_SCALE = 100;
   private static final int TEXT_PADDING = 16;
   private static final int TEXT_FIELD_WIDTH = 100;
   private static final int POS_FIELD_WIDTH = 50;
   private static final int FINAL_TEXT_HEIGHT = 24;
   private static final int MAX_TEXT_LENGTH = 128;
   private static final int BUTTON_WIDTH = 60;
   private static final int BUTTON_HEIGHT = 18;
   private static final int DEFAULT_REPEAT_TICK = 20;
   private static final int DEFAULT_VOLUME = 100;
   private static final SoundSource[] SOURCE_LIST;

   public SoundLooperScreen(BlockPos pos) {
      super(Text.literal(""));
      this.pos = pos;
      this.buttonCategory = UtilitiesClient.newButton(Text.literal(""), (button) -> {
         ++this.selectedCategory;
         if (this.selectedCategory > SOURCE_LIST.length - 1) {
            this.selectedCategory = 0;
         }

         button.setMessage(Text.literal(SOURCE_LIST[this.selectedCategory].getName()));
      });
      this.textBoxSoundId = new WidgetBetterTextField("mtr:ticket_barrier", 128);
      this.textBoxRepeatTick = new WidgetIntegerTextField(20, true, 128);
      this.textBoxSoundVolume = new WidgetIntegerTextField(100, true, 128);
      this.textBoxx1 = new WidgetIntegerTextField(0, false, 128);
      this.textBoxx2 = new WidgetIntegerTextField(0, false, 128);
      this.textBoxy1 = new WidgetIntegerTextField(0, false, 128);
      this.textBoxy2 = new WidgetIntegerTextField(0, false, 128);
      this.textBoxz1 = new WidgetIntegerTextField(0, false, 128);
      this.textBoxz2 = new WidgetIntegerTextField(0, false, 128);
      this.checkBoxLimitRange = new WidgetBetterCheckbox(0, 0, 0, 20, Text.literal(""), (checked) -> {
      });
      this.checkBoxNeedRedstone = new WidgetBetterCheckbox(0, 0, 0, 20, Text.literal(""), (checked) -> {
      });
   }

   protected void init() {
      super.init();
      int i = 1;
      IDrawing.setPositionAndWidth(this.buttonCategory, this.width - 20 - 60, 24 * i++ + 20, 60);
      IDrawing.setPositionAndWidth(this.textBoxSoundId, this.width - 20 - 100, 24 * i++ + 20, 100);
      IDrawing.setPositionAndWidth(this.textBoxSoundVolume, this.width - 20 - 100, 24 * i++ + 20, 100);
      IDrawing.setPositionAndWidth(this.textBoxRepeatTick, this.width - 20 - 100, 24 * i++ + 20, 100);
      IDrawing.setPositionAndWidth(this.checkBoxLimitRange, this.width - 20 - 100, 24 * i++ + 20, 100);
      IDrawing.setPositionAndWidth(this.checkBoxNeedRedstone, this.width - 20 - 100, 24 * i++ + 20, 100);
      IDrawing.setPositionAndWidth(this.textBoxx1, this.width - 20 - 150, 24 * i + 20, 50);
      IDrawing.setPositionAndWidth(this.textBoxy1, this.width - 20 - 100, 24 * i + 20, 50);
      IDrawing.setPositionAndWidth(this.textBoxz1, this.width - 20 - 50, 24 * i++ + 20, 50);
      IDrawing.setPositionAndWidth(this.textBoxx2, this.width - 20 - 150, 24 * i + 20, 50);
      IDrawing.setPositionAndWidth(this.textBoxy2, this.width - 20 - 100, 24 * i + 20, 50);
      IDrawing.setPositionAndWidth(this.textBoxz2, this.width - 20 - 50, 24 * i + 20, 50);
      Level world = Minecraft.getInstance().level;
      if (world != null) {
         BlockEntity entity = world.getBlockEntity(this.pos);
         if (entity instanceof SoundLooper.TileEntitySoundLooper) {
            BlockPos pos1 = ((SoundLooper.TileEntitySoundLooper)entity).getPos1();
            BlockPos pos2 = ((SoundLooper.TileEntitySoundLooper)entity).getPos2();
            this.textBoxx1.setValue(String.valueOf(pos1.getX()));
            this.textBoxx2.setValue(String.valueOf(pos2.getX()));
            this.textBoxy1.setValue(String.valueOf(pos1.getY()));
            this.textBoxy2.setValue(String.valueOf(pos2.getY()));
            this.textBoxz1.setValue(String.valueOf(pos1.getZ()));
            this.textBoxz2.setValue(String.valueOf(pos2.getZ()));
            this.textBoxSoundId.setValue(((SoundLooper.TileEntitySoundLooper)entity).getSoundId());
            this.textBoxRepeatTick.setValue(String.valueOf(((SoundLooper.TileEntitySoundLooper)entity).getLoopInterval()));
            this.textBoxSoundVolume.setValue(String.valueOf(Math.round(((SoundLooper.TileEntitySoundLooper)entity).getSoundVolume() * 100.0F)));
            this.checkBoxNeedRedstone.setChecked(((SoundLooper.TileEntitySoundLooper)entity).getNeedRedstone());
            this.checkBoxLimitRange.setChecked(((SoundLooper.TileEntitySoundLooper)entity).getLimitRange());
            this.selectedCategory = ((SoundLooper.TileEntitySoundLooper)entity).getSoundCategory();
            this.buttonCategory.setMessage(Text.literal(SOURCE_LIST[this.selectedCategory].getName()));
         }
      }

      this.addDrawableChild(this.buttonCategory);
      this.addDrawableChild(this.textBoxSoundId);
      this.addDrawableChild(this.textBoxSoundVolume);
      this.addDrawableChild(this.textBoxRepeatTick);
      this.addDrawableChild(this.checkBoxLimitRange);
      this.addDrawableChild(this.checkBoxNeedRedstone);
      this.addDrawableChild(this.textBoxx1);
      this.addDrawableChild(this.textBoxy1);
      this.addDrawableChild(this.textBoxz1);
      this.addDrawableChild(this.textBoxx2);
      this.addDrawableChild(this.textBoxy2);
      this.addDrawableChild(this.textBoxz2);
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
      try {
         this.renderBackground(guiGraphics, mouseX, mouseY, 0.0F);
         guiGraphics.drawCenteredString(this.font, Text.translatable("gui.jsblock.looper", new Object[0]), this.width / 2, 16, -1);
         int i = 1;
         boolean limitedRange = this.checkBoxLimitRange.selected();
         guiGraphics.drawString(this.font, Text.translatable("gui.jsblock.looper.sound_source", new Object[0]), 20, 24 * i++ + 20, -1);
         guiGraphics.drawString(this.font, Text.translatable("gui.jsblock.looper.sound_id", new Object[0]), 20, 24 * i++ + 20, -1);
         guiGraphics.drawString(this.font, Text.translatable("gui.jsblock.looper.sound_vol", new Object[0]), 20, 24 * i++ + 20, -1);
         guiGraphics.drawString(this.font, Text.translatable("gui.jsblock.looper.repeat_tick", new Object[0]), 20, 24 * i++ + 20, -1);
         guiGraphics.drawString(this.font, Text.translatable("gui.jsblock.looper.limit_range", new Object[0]), 20, 24 * i++ + 20, -1);
         guiGraphics.drawString(this.font, Text.translatable("gui.jsblock.looper.need_redstone", new Object[0]), 20, 24 * i++ + 20, -1);
         if (limitedRange) {
            guiGraphics.drawString(this.font, Text.translatable("gui.jsblock.looper.pos1", new Object[0]), 20, 24 * i++ + 20, -1);
            guiGraphics.drawString(this.font, Text.translatable("gui.jsblock.looper.pos2", new Object[0]), 20, 24 * i++ + 20, -1);
         }

         this.textBoxx1.setVisible(limitedRange);
         this.textBoxy1.setVisible(limitedRange);
         this.textBoxz1.setVisible(limitedRange);
         this.textBoxx2.setVisible(limitedRange);
         this.textBoxy2.setVisible(limitedRange);
         this.textBoxz2.setVisible(limitedRange);
         super.render(guiGraphics, mouseX, mouseY, delta);
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public void onClose() {
      int repeatTick = this.textBoxRepeatTick.getIntegerValue(1);
      float volume = (float)this.textBoxSoundVolume.getIntegerValue(1) / 100.0F;
      BlockPos pos1 = new BlockPos(this.textBoxx1.getIntegerValue(), this.textBoxy1.getIntegerValue(), this.textBoxz1.getIntegerValue());
      BlockPos pos2 = new BlockPos(this.textBoxx2.getIntegerValue(), this.textBoxy2.getIntegerValue(), this.textBoxz2.getIntegerValue());
      PacketClient.sendSoundLooperC2S(this.pos, this.selectedCategory, this.textBoxSoundId.getValue(), repeatTick, volume, this.checkBoxNeedRedstone.selected(), this.checkBoxLimitRange.selected(), pos1, pos2);
      super.onClose();
   }

   public boolean isPauseScreen() {
      return false;
   }

   static {
      SOURCE_LIST = new SoundSource[]{SoundSource.MASTER, SoundSource.MUSIC, SoundSource.WEATHER, SoundSource.AMBIENT, SoundSource.PLAYERS, SoundSource.BLOCKS, SoundSource.VOICE};
   }
}
