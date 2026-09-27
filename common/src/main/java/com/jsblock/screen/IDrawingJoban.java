package com.jsblock.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import mtr.client.Config;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

public interface IDrawingJoban {
   static void drawStringWithFont(PoseStack matrices, Font textRenderer, MultiBufferSource.BufferSource immediate, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, float x, float y, float maxWidth, float maxHeight, float scale, boolean keepRatio, int textColor, boolean shadow, int light, String font) {
      drawStringWithFont(matrices, textRenderer, immediate, text, horizontalAlignment, verticalAlignment, horizontalAlignment, x, y, maxWidth, maxHeight, scale, keepRatio, textColor, shadow, light, font, false, (IDrawing.DrawingCallback)null);
   }

   static void drawStringWithFont(PoseStack matrices, Font textRenderer, MultiBufferSource.BufferSource immediate, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, IGui.HorizontalAlignment xAlignment, float x, float y, float maxWidth, float maxHeight, float scale, boolean keepRatio, int textColor, boolean shadow, int light, String font, boolean sameSize, IDrawing.DrawingCallback drawingCallback) {
      Style style;
      for(style = Config.useMTRFont() ? Style.EMPTY.withFont(ResourceLocation.parse(font)) : Style.EMPTY; text.contains("||"); text = text.replace("||", "|")) {
      }

      String[] stringSplit = text.split("\\|");
      List<Boolean> isCJKList = new ArrayList();
      List<FormattedCharSequence> orderedTexts = new ArrayList();
      int totalHeight = 0;
      int totalWidth = 0;
      String[] var25 = stringSplit;
      int var26 = stringSplit.length;

      boolean isCJK;
      for(int var27 = 0; var27 < var26; ++var27) {
         String stringSplitPart = var25[var27];
         isCJK = IGui.isCjk(stringSplitPart);
         isCJKList.add(isCJK);
         FormattedCharSequence orderedText = Text.literal(stringSplitPart).setStyle(style).getVisualOrderText();
         orderedTexts.add(orderedText);
         totalHeight += 10 * (isCJK ? 2 : 1);
         int width = textRenderer.width(orderedText) * (isCJK ? 2 : 1);
         if (width > totalWidth) {
            totalWidth = width;
         }
      }

      if (maxHeight >= 0.0F && (float)totalHeight / scale > maxHeight) {
         scale = (float)totalHeight / maxHeight;
      }

      matrices.pushPose();
      float totalWidthScaled;
      float scaleX;
      if (maxWidth >= 0.0F && (float)totalWidth > maxWidth * scale) {
         totalWidthScaled = maxWidth * scale;
         scaleX = (float)totalWidth / maxWidth;
      } else {
         totalWidthScaled = (float)totalWidth;
         scaleX = scale;
      }

      matrices.scale(1.0F / scaleX, 1.0F / (keepRatio ? scaleX : scale), 1.0F / scale);
      float offset = verticalAlignment.getOffset(y * scale, (float)totalHeight);

      for(int i = 0; i < orderedTexts.size(); ++i) {
         isCJK = (Boolean)isCJKList.get(i);
         int extraScale = isCJK && !sameSize ? 2 : 1;
         if (isCJK && !sameSize) {
            matrices.pushPose();
            matrices.scale((float)extraScale, (float)extraScale, 1.0F);
         }

         float xOffset = horizontalAlignment.getOffset(xAlignment.getOffset(x * scaleX, (float)totalWidth), (float)(textRenderer.width((FormattedCharSequence)orderedTexts.get(i)) * extraScale - totalWidth));
         float shade = light == 15728880 ? 1.0F : Math.min((float)LightTexture.block(light) / 16.0F * 0.1F + 0.7F, 1.0F);
         int a = textColor >> 24 & 255;
         int r = (int)((float)(textColor >> 16 & 255) * shade);
         int g = (int)((float)(textColor >> 8 & 255) * shade);
         int b = (int)((float)(textColor & 255) * shade);
         if (immediate != null) {
            UtilitiesClient.drawInBatch(textRenderer, (FormattedCharSequence)orderedTexts.get(i), xOffset / (float)extraScale, offset / (float)extraScale, (a << 24) + (r << 16) + (g << 8) + b, shadow, matrices.last().pose(), immediate, 0, light);
         }

         if (isCJK && !sameSize) {
            matrices.popPose();
         }

         offset += (float)(10 * extraScale);
      }

      matrices.popPose();
      if (drawingCallback != null) {
         float x1 = xAlignment.getOffset(x, totalWidthScaled / scale);
         float y1 = verticalAlignment.getOffset(y, (float)totalHeight / scale);
         drawingCallback.drawingCallback(x1, y1, x1 + totalWidthScaled / scale, y1 + (float)totalHeight / scale);
      }

   }

   static void renderTextWithOffset(PoseStack matrices, Font textRenderer, MultiBufferSource.BufferSource immediate, String text, float x, float y, float maxX, float maxY, int color, int light, IGui.HorizontalAlignment xAlignment, IGui.VerticalAlignment yAlignment, boolean keepRatio, String font) {
      float finalY;
      float finalX;
      if (Config.useMTRFont()) {
         finalY = y + 0.4F;
         finalX = x;
      } else {
         finalY = y + 0.4F;
         finalX = x + 0.1F;
      }

      drawStringWithFont(matrices, textRenderer, immediate, text, xAlignment, yAlignment, finalX, finalY, maxX, 5.0F, 1.0F, keepRatio, color, false, light, font);
      immediate.endBatch();
   }
}
