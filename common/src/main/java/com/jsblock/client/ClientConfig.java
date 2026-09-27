package com.jsblock.client;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jsblock.Joban;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Collections;

public class ClientConfig {
   private static final String CONFIG_PATH = System.getProperty("user.dir") + "/config/jsclient.json";
   private static boolean renderDisabled = false;
   private static boolean bypassServerVersionCheck = false;
   private static boolean debugMode = false;

   public static void loadConfig() {
      if (!Files.exists(Paths.get(CONFIG_PATH), new LinkOption[0])) {
         Joban.LOGGER.warn("[Joban Client] Config file not found, generating one...");

         try {
            writeConfig();
         } catch (Exception var2) {
            var2.printStackTrace();
         }

      } else {
         Joban.LOGGER.info("[Joban Client] Reading Config...");

         try {
            JsonObject jsonConfig = (new JsonParser()).parse(String.join("", Files.readAllLines(Paths.get(CONFIG_PATH)))).getAsJsonObject();
            if (jsonConfig.has("renderDisabled")) {
               renderDisabled = jsonConfig.get("renderDisabled").getAsBoolean();
            }

            if (jsonConfig.has("bypassVersionCheck")) {
               bypassServerVersionCheck = jsonConfig.get("bypassVersionCheck").getAsBoolean();
            }

            if (jsonConfig.has("debugMode")) {
               debugMode = jsonConfig.get("debugMode").getAsBoolean();
            }
         } catch (Exception var4) {
            var4.printStackTrace();

            try {
               writeConfig();
            } catch (Exception var3) {
               var3.printStackTrace();
            }
         }

      }
   }

   public static void writeConfig() {
      Joban.LOGGER.info("[Joban Client] Writing Config...");
      JsonObject jsonConfig = new JsonObject();
      jsonConfig.addProperty("renderDisabled", renderDisabled);
      jsonConfig.addProperty("bypassVersionCheck", bypassServerVersionCheck);
      jsonConfig.addProperty("debugMode", debugMode);

      try {
         Files.write(Paths.get(CONFIG_PATH), Collections.singleton((new GsonBuilder()).setPrettyPrinting().create().toJson(jsonConfig)));
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static boolean getRenderDisabled() {
      return renderDisabled;
   }

   public static boolean getVersionCheckDisabled() {
      return bypassServerVersionCheck;
   }

   public static boolean getDebugModeEnabled() {
      return debugMode;
   }

   public static boolean setRenderDisabled(boolean disabled) {
      renderDisabled = disabled;
      return renderDisabled;
   }

   public static boolean setDebugMode(boolean enabled) {
      debugMode = enabled;
      return debugMode;
   }

   public static boolean setVersionCheckDisabled(boolean disabled) {
      bypassServerVersionCheck = disabled;
      return bypassServerVersionCheck;
   }
}
