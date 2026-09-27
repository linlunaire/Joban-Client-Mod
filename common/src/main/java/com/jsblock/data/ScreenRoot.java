package com.jsblock.data;

public class ScreenRoot {
   public ScreenAlignment screenAlignment;
   public int width;
   public int height;
   public int startX;
   public float widthFactor;
   public float heightFactor;

   public ScreenRoot(ScreenAlignment screenAlignment, float widthFactor, float heightFactor) {
      this.screenAlignment = screenAlignment;
      this.widthFactor = widthFactor;
      this.heightFactor = heightFactor;
   }

   public void init(float screenWidth, float screenHeight) {
      this.width = (int)(screenWidth / this.widthFactor);
      this.height = (int)(screenHeight / this.heightFactor);
      this.startX = getXPadding((float)this.width, screenWidth);
   }

   public static int getXPadding(float screenWidth, float totalWidth) {
      return (int)(totalWidth - screenWidth) / 2;
   }
}
