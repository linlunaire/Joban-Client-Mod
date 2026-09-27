package com.jsblock;

import java.util.Objects;

public class Compatibilities {
   public static boolean lowerThanMin(String ogMinVersion, String ogCurVersion) {
      if (ogCurVersion != null && ogMinVersion != null) {
         String minVersion = ogMinVersion.split("-")[0];
         String curVersion = ogCurVersion.split("-")[0];
         if (Objects.equals(minVersion, curVersion)) {
            return false;
         } else {
            String[] splittedA = minVersion.split("\\.");
            String[] splittedB = curVersion.split("\\.");
            if (splittedA.length != splittedB.length) {
               return false;
            } else {
               for(int i = 0; i < splittedA.length; ++i) {
                  int min = Integer.parseInt(splittedA[i]);
                  int cur = Integer.parseInt(splittedB[i]);
                  if (min < cur) {
                     return false;
                  }

                  if (min > cur) {
                     return true;
                  }
               }

               return false;
            }
         }
      } else {
         return false;
      }
   }

   protected static void incompatible(String curVer, String minVer) {
      Joban.LOGGER.fatal("[Joban Client] This version of JCM is incompatible with MTR " + curVer + ".");
      Joban.LOGGER.fatal("[Joban Client] Please install MTR " + minVer + " and try again.");
      Joban.LOGGER.fatal("");
   }
}
