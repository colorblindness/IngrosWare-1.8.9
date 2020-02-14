package ingros.ware.client.mixin.impl;

import ingros.ware.client.Client;
import ingros.ware.client.events.ChatEvent;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {

    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    private void messageSend(String msg, boolean addToChat, final CallbackInfo callbackInfo) {
        if (addToChat) {
            ((GuiScreen)(Object)this).mc.ingameGUI.getChatGUI().addToSentMessages(msg);
            final ChatEvent event = new ChatEvent(msg);
            Client.INSTANCE.getBus().fireEvent(event);
            if (event.isCancelled())
            callbackInfo.cancel();
        }
    }
}
