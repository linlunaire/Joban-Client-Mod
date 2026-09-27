package com.jsblock.particle;

import com.jsblock.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/** Item-model particle with the original fixed size, no gravity and 80-tick lifetime. */
public final class LightBlockParticle extends SingleQuadParticle {
    private LightBlockParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        gravity = 0;
        lifetime = 80;
        hasPhysics = false;
    }

    @Override protected Layer getLayer() { return Layer.bySprite(sprite); }
    @Override public float getQuadSize(float partialTick) { return 0.5F; }

    public static final class Provider implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            ItemStackRenderState state = new ItemStackRenderState();
            Minecraft.getInstance().getItemModelResolver().updateForTopItem(state, new ItemStack(Blocks.LIGHT_BLOCK.get()),
                    ItemDisplayContext.NONE, level, null, 0);
            var material = state.pickParticleMaterial(random);
            return material == null ? null : new LightBlockParticle(level, x, y, z, material.sprite());
        }
    }
}
