package com.jsblock.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.jsblock.data.ConfigGuiEntry;
import com.jsblock.data.InlineComponentEntry;
import com.jsblock.data.ScreenAlignment;
import com.jsblock.data.ScreenRoot;
import com.jsblock.data.TextLabel;
import com.jsblock.vermappings.render.RenderHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

public class ConfigScreenBase extends ScreenMapper implements IGui {
   private double elapsedTime;
   private final List<InlineComponentEntry> inlineComponentList;
   private final List<ConfigGuiEntry> configList;
   private final List<TextLabel> customText;
   private final ScreenRoot screenRoot;
   private boolean initalized;
   public static final int BUTTON_HEIGHT = 20;
   private static final int TRANSITION_DURATION = 20;
   private static final int TEXT_PADDING = 12;
   private static final int CONFIG_BUTTON_WIDTH = 60;
   private static final int TEXT_FIELD_WIDTH = 100;
   private static final int FINAL_TEXT_HEIGHT = 20;
   private static final int MAX_TEXT_LENGTH = 128;
   private static final ResourceLocation BACKGROUND = ResourceLocation.parse("jsblock:textures/gui/background/bg.png");
   private static final ResourceLocation STAR_BACKGROUND = ResourceLocation.parse("jsblock:textures/gui/background/stars.png");
   private static final ResourceLocation TERRAIN_BACKGROUND = ResourceLocation.parse("jsblock:textures/gui/background/terrain.png");

   public ConfigScreenBase(ScreenRoot root, TextLabel... customText) {
      super(Text.literal(""));
      this.screenRoot = root;
      this.inlineComponentList = new ArrayList();
      this.customText = new ArrayList();
      this.configList = new ArrayList();
      this.customText.addAll(Arrays.asList(customText));
   }

   public AbstractWidget registerConfigRowButton(MutableComponent description, Component defaultMessage, Consumer<AbstractWidget> onClick) {
      MutableComponent var10000 = Text.literal("");
      Objects.requireNonNull(onClick);
      Button button = UtilitiesClient.newButton(var10000, onClick::accept);
      button.setMessage(defaultMessage);
      this.configList.add(new ConfigGuiEntry(description, button, 60, 20));
      return button;
   }

   public void registerInlineRow(InlineComponentEntry... entries) {
      this.inlineComponentList.addAll(Arrays.asList(entries));
   }

   protected void init() {
      super.init();
      int i = 0;
      int startY = 0;

      TextLabel component;
      for(Iterator var3 = this.customText.iterator(); var3.hasNext(); startY += (int)(component.y * (double)component.scale)) {
         component = (TextLabel)var3.next();
      }

      this.screenRoot.init((float)this.width, (float)this.height);
      List<ConfigGuiEntry> originalList = new ArrayList(this.configList);
      this.configList.clear();
      Iterator var9 = originalList.iterator();

      while(var9.hasNext()) {
         ConfigGuiEntry entry = (ConfigGuiEntry)var9.next();
         entry.y = startY + 12 + 20 * i++ + 20;
         this.configList.add(entry);
         IDrawing.setPositionAndWidth(entry.widget, this.width - this.screenRoot.startX - 60, entry.y, entry.widgetWidth);
         this.addDrawableChild(entry.widget);
      }

      for(int k = 0; k < this.inlineComponentList.size(); ++k) {
         InlineComponentEntry entry = (InlineComponentEntry)this.inlineComponentList.get(k);
         entry.setAvailableWidth(this.screenRoot.width);

         for(int j = 0; j < entry.widgetList.size(); ++j) {
            AbstractWidget widget = (AbstractWidget)entry.widgetList.get(j);
            UtilitiesClient.setWidgetX(widget, entry.calculateWidth() * j);
            this.addDrawableChild(widget);
         }

         this.inlineComponentList.set(k, entry);
      }

      if (!this.initalized) {
         this.initalized = true;
      }

   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
      try {
         this.elapsedTime += (double)delta;
         int startY = 0;
         this.renderBG(guiGraphics);
         Iterator var6 = this.customText.iterator();

         int startInlineY;
         while(var6.hasNext()) {
            TextLabel component = (TextLabel)var6.next();
            int textWidth = (int)((float)this.font.width(component.text) * component.scale);
            int componentX = this.screenRoot.startX + ScreenAlignment.getX(component.horizontalAlignment, this.screenRoot.width, textWidth);
            startInlineY = (int)((double)startY + component.y);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate((float)componentX, (float)startInlineY, 0.0F);
            guiGraphics.pose().scale(component.scale, component.scale, component.scale);
            guiGraphics.drawString(this.font, component.text, 0, 0, -1);
            startY = (int)((float)startY + 12.0F * component.scale);
            guiGraphics.pose().popPose();
         }

         ConfigGuiEntry entry;
         for(var6 = this.configList.iterator(); var6.hasNext(); guiGraphics.drawString(this.font, entry.text, this.screenRoot.startX, entry.y + entry.widgetHeight / 4, -1)) {
            entry = (ConfigGuiEntry)var6.next();
            boolean mouseXInScreenRoot = RailwayData.isBetween((double)mouseX, (double)this.screenRoot.startX, (double)(this.screenRoot.startX + this.screenRoot.width));
            boolean mouseYInThisEntry = RailwayData.isBetween((double)mouseY, (double)entry.y, (double)(entry.y + entry.widgetHeight));
            if (mouseXInScreenRoot && mouseYInThisEntry) {
               RenderSystem.enableBlend();
               RenderSystem.defaultBlendFunc();
               RenderHelper.disableTexture();
               RenderHelper.setShaderColor(1.0F, 1.0F, 1.0F, 0.3F);
               guiGraphics.fill(this.screenRoot.startX, entry.y, this.screenRoot.width, entry.widgetHeight, -1);
               RenderHelper.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               RenderHelper.enableTexture();
            }
         }

         int inlineRow = 1;
         int bottomPadding = 20;

         for(Iterator var22 = this.inlineComponentList.iterator(); var22.hasNext(); ++inlineRow) {
            InlineComponentEntry inlineEntry = (InlineComponentEntry)var22.next();
            startInlineY = ScreenAlignment.getY(inlineEntry.verticalAlignment, this.height, 20) - 20 * this.inlineComponentList.size() - bottomPadding;
            int y = startInlineY + 20 * inlineRow;
            int totalWidth = inlineEntry.calculateWidth() * inlineEntry.widgetList.size() + 10 * inlineEntry.widgetList.size();
            int startX = ScreenAlignment.getX(inlineEntry.horizontalAlignment, this.width, totalWidth);

            for(int i = 0; i < inlineEntry.widgetList.size(); ++i) {
               AbstractWidget widget = (AbstractWidget)inlineEntry.widgetList.get(i);
               int buttonX = startX + inlineEntry.calculateWidth() * i + 10 * i;
               IDrawing.setPositionAndWidth(widget, buttonX, y, inlineEntry.calculateWidth());
            }
         }

         super.render(guiGraphics, mouseX, mouseY, delta);
      } catch (Exception var17) {
         var17.printStackTrace();
      }

   }

   public boolean keyPressed(int keyCode, int scanCode, int modifier) {
      if (keyCode == 256) {
         this.closeScreen(true);
         return true;
      } else {
         return super.keyPressed(keyCode, scanCode, modifier);
      }
   }

   private void renderBG(GuiGraphics guiGraphics) {
      guiGraphics.fill(0, 0, this.width, this.height, 0xFF000000);
   }

   public void closeScreen(boolean save) {
      if (save) {
         this.onSave();
      }

      super.onClose();
   }

   public void onSave() {
   }

   protected static Component getBooleanButtonText(boolean state) {
      return Text.translatable(state ? "options.mtr.on" : "options.mtr.off", new Object[0]);
   }

   private double easeOutAnimation(double x) {
      return x == 1.0 ? 1.0 : 1.0 - Math.pow(1.0 - x, 5.0);
   }
}
