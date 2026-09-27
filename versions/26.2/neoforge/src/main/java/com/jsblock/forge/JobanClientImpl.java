package com.jsblock.forge;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

public final class JobanClientImpl {
    private static final Map<SimpleParticleType, ParticleProvider<SimpleParticleType>> PROVIDERS = new LinkedHashMap<>();

    public static void registerParticle(SimpleParticleType particle, ParticleProvider<SimpleParticleType> provider) {
        PROVIDERS.put(particle, provider);
    }

    public static void registerProviders(RegisterParticleProvidersEvent event) {
        PROVIDERS.forEach(event::registerSpecial);
    }
}
