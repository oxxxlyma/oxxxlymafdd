package ru.oxxxlyma.particle;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {

    public static final SimpleParticleType CUSTOM_ATTACK = FabricParticleTypes.simple();

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier("oxxxlyma", "custom_attack"), CUSTOM_ATTACK);
        ParticleFactoryRegistry.getInstance().register(CUSTOM_ATTACK, Factory::new);
    }

    public static class Factory extends SpriteBillboardParticle {
        public Factory(SpriteProvider spriteProvider, ClientWorld world,
                       double x, double y, double z, double vx, double vy, double vz) {
            super(world, x, y, z);
            this.setSprite(spriteProvider.getSprite(world.random));
            this.maxAge = 25;
            this.scale = 0.25F;
            this.velocityX = vx;
            this.velocityY = vy;
            this.velocityZ = vz;
            this.red = (float) vx;
            this.green = (float) vy;
            this.blue = (float) vz;
        }

        public Factory(SpriteProvider spriteProvider) { super(null, 0, 0, 0); }

        @Override
        public void tick() {
            super.tick();
            this.scale *= 0.95F;
            this.alpha = 1.0F - (float) this.age / this.maxAge;
        }

        @Override
        public ParticleTextureSheet getType() {
            return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
        }
    }
}