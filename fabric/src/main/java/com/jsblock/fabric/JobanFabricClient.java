package com.jsblock.fabric;

import com.jsblock.Joban;
import com.jsblock.JobanClient;
import com.jsblock.Particles;
import com.jsblock.client.JobanCustomResources;
import com.jsblock.particle.LightBlockParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

/** Client-only Fabric registrations. */
public final class JobanFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        JobanClient.init();
        ParticleFactoryRegistry.getInstance().register(Particles.LIGHT_BLOCK.get(), new LightBlockParticle.Provider());
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new CustomResourcesWrapper());
    }

    private static final class CustomResourcesWrapper implements SimpleSynchronousResourceReloadListener {
        @Override
        public ResourceLocation getFabricId() {
            return ResourceLocation.fromNamespaceAndPath(Joban.MOD_ID, "joban_custom_resources");
        }

        @Override
        public void onResourceManagerReload(ResourceManager resourceManager) {
            JobanCustomResources.reload(resourceManager);
        }
    }
}
