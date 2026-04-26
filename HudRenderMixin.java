package ru.oxxxlyma.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.oxxxlyma.hud.TargetHUD;

@Mixin(InGameHud.class)
public class HudRenderMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void renderHUD(DrawContext context, float tickDelta, CallbackInfo ci) {
        TargetHUD.render(context);
    }
}