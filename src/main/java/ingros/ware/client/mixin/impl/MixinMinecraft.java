package ingros.ware.client.mixin.impl;

import ingros.ware.client.Client;
import ingros.ware.client.events.KeyPressedEvent;
import ingros.ware.client.events.TickEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(method = "runTick()V", at = @At(value = "INVOKE_ASSIGN", target = "Lorg/lwjgl/input/Keyboard;next()Z", shift = At.Shift.AFTER))
    private void onKeyPress(CallbackInfo ci) {
        if (Keyboard.getEventKeyState()) {
            Client.INSTANCE.getBus().fireEvent(new KeyPressedEvent(Keyboard.getEventKey()));
        }
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        Client.INSTANCE.getBus().fireEvent(new TickEvent());
    }

    @Inject(method = "startGame", at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;",
            shift = At.Shift.AFTER))
    public void startGame(CallbackInfo ci) {
        Client.INSTANCE.startupClient();
    }

    @Inject(method = "shutdownMinecraftApplet", at = @At("HEAD"))
    private void shutdown(CallbackInfo ci) {
        Client.INSTANCE.shutdownClient();
    }
}