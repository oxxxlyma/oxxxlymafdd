package ru.oxxxlyma;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import ru.oxxxlyma.particle.ModParticles;

public class OxlMod implements ClientModInitializer {
    public static Entity highlightedTarget = null;

    @Override
    public void onInitializeClient() {
        ModParticles.register();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.crosshairTarget == null) {
                highlightedTarget = null;
                return;
            }
            if (client.crosshairTarget.getType() == HitResult.Type.ENTITY) {
                EntityHitResult hit = (EntityHitResult) client.crosshairTarget;
                Entity entity = hit.getEntity();
                if (entity instanceof PlayerEntity) {
                    highlightedTarget = entity;
                    return;
                }
            }
            highlightedTarget = null;
        });
    }
}