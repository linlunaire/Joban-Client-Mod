package com.jsblock.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.components.AbstractWidget;

public class InlineComponentEntry {
   public int availableWidth;
   public ScreenAlignment horizontalAlignment;
   public ScreenAlignment verticalAlignment;
   public static final int MARGIN = 10;
   public List<AbstractWidget> widgetList = new ArrayList();

   public InlineComponentEntry(ScreenAlignment horizontalAlignment, ScreenAlignment verticalAlignment, AbstractWidget... widget) {
      this.widgetList.addAll(Arrays.asList(widget));
      this.horizontalAlignment = horizontalAlignment;
      this.verticalAlignment = verticalAlignment;
   }

   public void setAvailableWidth(int availableWidth) {
      this.availableWidth = availableWidth;
   }

   public int calculateWidth() {
      return this.availableWidth / this.widgetList.size() - 10;
   }
}
