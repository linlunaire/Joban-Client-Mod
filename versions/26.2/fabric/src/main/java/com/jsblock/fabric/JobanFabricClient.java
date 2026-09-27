package com.jsblock.fabric;

import com.jsblock.Joban;
import com.jsblock.JobanClient;
import com.jsblock.Particles;
import com.jsblock.client.JobanCustomResources;
import com.jsblock.particle.LightBlockParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public final class JobanFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        JobanClient.init();
        ParticleProviderRegistry.getInstance().register(Particles.LIGHT_BLOCK.get(), new LightBlockParticle.Provider());
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(
                Identifier.fromNamespaceAndPath(Joban.MOD_ID, JobanCustomResources.CUSTOM_RESOURCES_ID),
                (ResourceManagerReloadListener) JobanCustomResources::reload);
    }
}
