package ru.oxxxlyma.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import ru.oxxxlyma.OxlMod;

public class TargetHUD {

    private static float displayAlpha = 0.0F;

    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        boolean hasTarget = OxlMod.highlightedTarget instanceof PlayerEntity
                && OxlMod.highlightedTarget.isAlive();

        displayAlpha = hasTarget ? Math.min(1.0F, displayAlpha + 0.1F)
                                 : Math.max(0.0F, displayAlpha - 0.08F);
        if (displayAlpha <= 0.01F) return;

        PlayerEntity target = (PlayerEntity) OxlMod.highlightedTarget;
        if (target == null || client.player == null) return;

        int width = 160, height = 60;
        int x = client.getWindow().getScaledWidth() / 2 - width / 2;
        int y = 6;
        int alpha = (int) (displayAlpha * 180);

        // Тень
        context.fill(x + 2, y + 2, x + width + 2, y + height + 2, 0x40000000);
        // Фон
        context.fill(x, y, x + width, y + height, (alpha << 24) | 0x1A0030);
        // Рамка
        context.fill(x, y, x + width, y + 1, (alpha << 24) | 0xA020F0);
        context.fill(x, y + height - 1, x + width, y + height, (alpha << 24) | 0xA020F0);
        context.fill(x, y, x + 1, y + height, (alpha << 24) | 0xA020F0);
        context.fill(x + width - 1, y, x + width, y + height, (alpha << 24) | 0xA020F0);
        // Боковая линия
        context.fill(x + 2, y + 2, x + 4, y + height - 2, (alpha << 24) | 0x6000AA);

        // Иконка-заглушка
        context.fill(x + 9, y + 10, x + 23, y + 24, 0xFF6A0DAD);
        context.fill(x + 11, y + 12, x + 21, y + 22, 0xFF8B2FC9);
        context.fill(x + 14, y + 13, x + 18, y + 17, 0xFFD8A0FF);
        context.fill(x + 15, y + 15, x + 16, y + 16, 0xFF1A0030);
        context.fill(x + 17, y + 15, x + 18, y + 16, 0xFF1A0030);
        context.fill(x + 15, y + 18, x + 18, y + 19, 0xFF1A0030);

        // Имя
        context.drawText(client.textRenderer, "§d§l" + target.getName().getString(),
                x + 28, y + 7, 0xFFFFFF, true);
        // HP
        context.drawText(client.textRenderer,
                String.format("§5❤ §f%.0f/%.0f", target.getHealth(), target.getMaxHealth()),
                x + 28, y + 22, 0xFFFFFF, true);
        // Полоска HP
        int bx = x + 28, by = y + 37, bw = 120, bh = 5;
        float pct = target.getHealth() / target.getMaxHealth();
        context.fill(bx, by, bx + bw, by + bh, 0xFF1A1A1A);
        int col = interpolate(0xFFFF3333, 0xFFA020F0, pct);
        context.fill(bx, by, bx + (int)(bw * pct), by + bh, col);
        context.fill(bx, by, bx + (int)(bw * pct), by + 1, 0x44FFFFFF);

        // Дистанция
        double dist = client.player.getPos().distanceTo(target.getPos());
        context.drawText(client.textRenderer, String.format("§5📍 §f%.1f м", dist),
                x + width - 60, y + 7, 0xFFFFFF, true);
        // Броня
        if (target.getArmor() > 0)
            context.drawText(client.textRenderer, String.format("§5🛡 §f%d", target.getArmor()),
                    x + width - 60, y + 22, 0xFFFFFF, true);
    }

    private static int interpolate(int c1, int c2, float r) {
        return 0xFF000000
            | ((int)(((c1>>16)&0xFF)+(((c2>>16)&0xFF)-((c1>>16)&0xFF))*r) << 16)
            | ((int)(((c1>>8)&0xFF)+(((c2>>8)&0xFF)-((c1>>8)&0xFF))*r) << 8)
            | (int)((c1&0xFF)+((c2&0xFF)-(c1&0xFF))*r);
    }
}