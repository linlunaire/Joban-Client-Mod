package com.jsblock.screen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import mtr.mappings.UtilitiesClient;
import mtr.screen.WidgetBetterTextField;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class WidgetSuggestionTextField extends WidgetBetterTextField {
   private final Collection<String> suggestionList;
   private List<String> matchedSuggestionList;
   private final int RED_COLOR = 16733525;
   private final int WHITE_COLOR = 16777215;
   private String currentSuggestion = "";

   public WidgetSuggestionTextField(String defaultSuggestion, Collection<String> suggestionList, int maxLength, boolean strict) {
      super(defaultSuggestion, maxLength);
      this.suggestionList = suggestionList;
      this.matchedSuggestionList = new ArrayList(suggestionList);
   }

   public void setResponder(Consumer<String> changedListener) {
      super.setResponder((text) -> {
         this.matchedSuggestionList = (List)(text.isEmpty() ? new ArrayList(this.suggestionList) : (List)this.suggestionList.stream().filter((str) -> {
            return str.startsWith(text);
         }).collect(Collectors.toList()));
         if (this.matchedSuggestionList.isEmpty()) {
            this.setTextColor(16733525);
         } else {
            this.setTextColor(16777215);
         }

         if (!text.isEmpty() && !this.matchedSuggestionList.isEmpty()) {
            this.setSuggestion(((String)this.matchedSuggestionList.get(0)).substring(text.length()));
            this.currentSuggestion = (String)this.matchedSuggestionList.get(0);
         }

         changedListener.accept(text);
      });
   }

   public boolean keyPressed(int i, int j, int k) {
      if (this.canConsumeInput() && !this.getValue().isEmpty() && (i == 257 || i == 335)) {
         this.setValue(this.currentSuggestion);
      }

      return super.keyPressed(i, j, k);
   }

   public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
      if (this.isFocused()) {
         Font font = Minecraft.getInstance().font;
         int i = 0;

         for(Iterator var7 = this.matchedSuggestionList.iterator(); var7.hasNext(); ++i) {
            String suggestion = (String)var7.next();
            int color = i == 0 ? ChatFormatting.YELLOW.getColor() : -1;
            int var10003 = UtilitiesClient.getWidgetX(this);
            Objects.requireNonNull(font);
            guiGraphics.drawString(font, suggestion, var10003, i * 9 + UtilitiesClient.getWidgetY(this) + this.height + 4, color, false);
         }
      }

   }
}
