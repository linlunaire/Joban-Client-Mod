package com.jsblock.screen;

import mtr.screen.WidgetBetterTextField;
import mtr.screen.WidgetBetterTextField.TextFieldFilter;

public class WidgetIntegerTextField extends WidgetBetterTextField {
   private final int defaultValue;

   public WidgetIntegerTextField(int defaultValue, boolean positiveOnly, int maxLength) {
      super(positiveOnly ? TextFieldFilter.POSITIVE_INTEGER : TextFieldFilter.INTEGER, String.valueOf(defaultValue), maxLength);
      this.defaultValue = defaultValue;
   }

   public int getIntegerValue(int minValue) {
      try {
         return Math.max(minValue, Integer.parseInt(this.getValue()));
      } catch (NumberFormatException var3) {
         return this.defaultValue;
      }
   }

   public int getIntegerValue() {
      return this.getIntegerValue(Integer.MIN_VALUE);
   }
}
