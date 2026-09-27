package com.jsblock.forge;

import java.util.Optional;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;

public class JobanImpl {
   public static String getMTRVersion() {
      try {
         Optional<? extends ModContainer> mtr = ModList.get().getModContainerById("mtr");
         if (mtr.isPresent()) {
            String mtrVersion = ((ModContainer)mtr.get()).getModInfo().getVersion().toString();
            return mtrVersion.replace(mtrVersion.split("-")[0] + "-", "");
         } else {
            return null;
         }
      } catch (Exception var2) {
         return null;
      }
   }
}
