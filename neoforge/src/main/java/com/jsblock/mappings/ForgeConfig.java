package com.jsblock.mappings;

import com.jsblock.screen.ConfigScreen;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ForgeConfig {
   public static void registerConfig(ModContainer modContainer) {
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, (IConfigScreenFactory) (container, screen) -> new ConfigScreen());
   }
}
