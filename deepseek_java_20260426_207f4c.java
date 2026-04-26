package ru.oxxxlyma.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.oxxxlyma.OxlConfig;
import ru.oxxxlyma.particle.ModParticles;
import java.util.Random;

@Mixin(PlayerEntity.class)
public class AttackParticleMixin {

    @Inject(method = "onAttacking", at = @At("TAIL"))
    private void onAttack(Entity target, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player == null || target == null) return;

        Random random = new Random();
        Vec3d pos = target.getPos();

        for (int i = 0; i < 12; i++) {
            double offX = (random.nextDouble() - 0.5) * 1.2;
            double offY = random.nextDouble() * 2.0;
            double offZ = (random.nextDouble() - 0.5) * 1.2;

            MinecraftClient.getInstance().particleManager.addParticle(
                ModParticles.CUSTOM_ATTACK,
                pos.x + offX, pos.y + offY, pos.z + offZ,
                OxlConfig.PARTICLE_RED, OxlConfig.PARTICLE_GREEN, OxlConfig.PARTICLE_BLUE
            );
        }
    }
}