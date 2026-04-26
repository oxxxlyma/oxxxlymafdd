package ru.oxxxlyma.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.oxxxlyma.OxlConfig;
import ru.oxxxlyma.OxlMod;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(MatrixStack matrices, float tickDelta, long limitTime,
                          boolean renderBlockOutline, Camera camera,
                          GameRenderer gameRenderer, LightmapTextureManager lightmap,
                          Matrix4f matrix4f, CallbackInfo ci) {

        Entity target = OxlMod.highlightedTarget;
        if (target == null) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || !target.isAlive()) return;

        Vec3d camPos = camera.getPos();
        Box box = target.getBoundingBox().offset(-camPos.x, -camPos.y, -camPos.z);

        float r = OxlConfig.RED;
        float g = OxlConfig.GREEN;
        float b = OxlConfig.BLUE;
        float a = OxlConfig.ALPHA;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
        RenderSystem.depthMask(false);
        RenderSystem.lineWidth(OxlConfig.LINE_WIDTH);
        RenderSystem.disableCull();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);

        double x1 = box.minX, y1 = box.minY, z1 = box.minZ;
        double x2 = box.maxX, y2 = box.maxY, z2 = box.maxZ;

        // Нижняя грань
        buffer.vertex((float)x1, (float)y1, (float)z1).color(r, g, b, a);
        buffer.vertex((float)x2, (float)y1, (float)z1).color(r, g, b, a);
        buffer.vertex((float)x2, (float)y1, (float)z1).color(r, g, b, a);
        buffer.vertex((float)x2, (float)y1, (float)z2).color(r, g, b, a);
        buffer.vertex((float)x2, (float)y1, (float)z2).color(r, g, b, a);
        buffer.vertex((float)x1, (float)y1, (float)z2).color(r, g, b, a);
        buffer.vertex((float)x1, (float)y1, (float)z2).color(r, g, b, a);
        buffer.vertex((float)x1, (float)y1, (float)z1).color(r, g, b, a);

        // Верхняя грань
        buffer.vertex((float)x1, (float)y2, (float)z1).color(r, g, b, a);
        buffer.vertex((float)x2, (float)y2, (float)z1).color(r, g, b, a);
        buffer.vertex((float)x2, (float)y2, (float)z1).color(r, g, b, a);
        buffer.vertex((float)x2, (float)y2, (float)z2).color(r, g, b, a);
        buffer.vertex((float)x2, (float)y2, (float)z2).color(r, g, b, a);
        buffer.vertex((float)x1, (float)y2, (float)z2).color(r, g, b, a);
        buffer.vertex((float)x1, (float)y2, (float)z2).color(r, g, b, a);
        buffer.vertex((float)x1, (float)y2, (float)z1).color(r, g, b, a);

        // Вертикальные рёбра
        buffer.vertex((float)x1, (float)y1, (float)z1).color(r, g, b, a);
        buffer.vertex((float)x1, (float)y2, (float)z1).color(r, g, b, a);
        buffer.vertex((float)x2, (float)y1, (float)z1).color(r, g, b, a);
        buffer.vertex((float)x2, (float)y2, (float)z1).color(r, g, b, a);
        buffer.vertex((float)x2, (float)y1, (float)z2).color(r, g, b, a);
        buffer.vertex((float)x2, (float)y2, (float)z2).color(r, g, b, a);
        buffer.vertex((float)x1, (float)y1, (float)z2).color(r, g, b, a);
        buffer.vertex((float)x1, (float)y2, (float)z2).color(r, g, b, a);

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.lineWidth(1.0F);
    }
}