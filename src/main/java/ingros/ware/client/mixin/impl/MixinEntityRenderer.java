package ingros.ware.client.mixin.impl;

import ingros.ware.client.Client;
import ingros.ware.client.events.Render2DEvent;
import ingros.ware.client.events.Render3DEvent;
import ingros.ware.client.mixin.accessors.IEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(EntityRenderer.class)
@Implements(@Interface(iface = IEntityRenderer.class, prefix = "ext$"))
public abstract class MixinEntityRenderer  {

    @Shadow
    private void setupCameraTransform(float partialTicks, int pass) {}

    @Intrinsic(displace = true)
    public void ext$setupCameraTransform(float partialTicks, int pass) {
        setupCameraTransform(partialTicks, pass);
    }

    @Shadow
    private void orientCamera(float partialTicks) {}

    @Intrinsic(displace = true)
    public void ext$cameraOrientation(float partialTicks) {
        orientCamera(partialTicks);
    }

    @Redirect(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiIngame.renderGameOverlay(F)V"))
    private void updateCameraAndRender$renderGameOverlay(GuiIngame guiIngame, float partialTicks) {
        guiIngame.renderGameOverlay(partialTicks);
        final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        Client.INSTANCE.getBus().fireEvent(new Render2DEvent(partialTicks,scaledresolution));
    }
    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE_STRING", target = "net/minecraft/profiler/Profiler.endStartSection(Ljava/lang/String;)V", args = {"ldc=hand"}))
    private void onStartHand(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        Client.INSTANCE.getBus().fireEvent(new Render3DEvent(partialTicks));
    }
}