package com.jsblock.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jsblock.Joban;
import com.jsblock.data.PIDSPreset;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.function.Consumer;
import mtr.mappings.Utilities;
import mtr.mappings.UtilitiesClient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class JobanCustomResources {
   private static final PIDSPreset defaultPreset1 = new PIDSPreset(ResourceLocation.fromNamespaceAndPath("jsblock", "textures/pids_screen/door_cls_apg.png"), true, true, false, new boolean[]{true, true, true, false}, (Integer)null, (String)null, (Int2IntArrayMap)null);
   private static final PIDSPreset defaultPreset2 = new PIDSPreset(ResourceLocation.fromNamespaceAndPath("jsblock", "textures/pids_screen/door_cls_psd.png"), true, true, false, new boolean[]{true, true, true, false}, (Integer)null, (String)null, (Int2IntArrayMap)null);
   private static final PIDSPreset defaultPreset3 = new PIDSPreset(ResourceLocation.fromNamespaceAndPath("jsblock", "textures/pids_screen/door_cls_train.png"), true, true, false, new boolean[]{true, true, true, false}, (Integer)null, (String)null, (Int2IntArrayMap)null);
   public static final String CUSTOM_RESOURCES_ID = "joban_custom_resources";
   public static final String customResourcePath = "jsblock:joban_custom_resources.json";
   public static HashMap<String, PIDSPreset> PIDSPresets = new HashMap();

   public static void reload(ResourceManager manager) {
      PIDSPresets.clear();
      PIDSPresets.put("door_cls_apg", defaultPreset1);
      PIDSPresets.put("door_cls_psd", defaultPreset2);
      PIDSPresets.put("door_cls_train", defaultPreset3);
      readResource(manager, "jsblock:joban_custom_resources.json", (jsonConfig) -> {
         try {
            if (!jsonConfig.has("pids_images") || !jsonConfig.get("pids_images").isJsonArray()) {
               Joban.LOGGER.warn("[JCM] Invalid joban_custom_resources.json!");
               Joban.LOGGER.warn("[JCM] \"pids_images\" must be an array");
               return;
            }

            jsonConfig.get("pids_images").getAsJsonArray().forEach((jsonElement) -> {
               String id = jsonElement.getAsJsonObject().get("id").getAsString();
               if (PIDSPresets.containsKey(id)) {
                  Joban.LOGGER.warn("[Joban Client] PIDS Preset ID: " + id + " already added.");
               } else {
                  PIDSPreset preset = PIDSPreset.fromJson(jsonElement);
                  PIDSPresets.put(id, preset);
               }
            });
         } catch (Exception var2) {
            var2.printStackTrace();
         }

      });
      Joban.LOGGER.info("[Joban Client] Loaded PIDS Preset: " + String.join(", ", PIDSPresets.keySet()));
   }

   private static void readResource(ResourceManager manager, String path, Consumer<JsonObject> callback) {
      try {
         UtilitiesClient.getResources(manager, ResourceLocation.parse(path)).forEach((resource) -> {
            try {
               InputStream stream = Utilities.getInputStream(resource);

               try {
                  callback.accept((new JsonParser()).parse(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject());
               } catch (Throwable var7) {
                  if (stream != null) {
                     try {
                        stream.close();
                     } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                     }
                  }

                  throw var7;
               }

               if (stream != null) {
                  stream.close();
               }
            } catch (Exception var8) {
               var8.printStackTrace();
            }

            try {
               Utilities.closeResource(resource);
            } catch (IOException var5) {
               var5.printStackTrace();
            }

         });
      } catch (Exception var4) {
      }

   }
}
