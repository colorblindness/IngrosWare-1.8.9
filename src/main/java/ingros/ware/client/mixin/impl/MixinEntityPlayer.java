package ingros.ware.client.mixin.impl;

import ingros.ware.client.Client;
import ingros.ware.client.events.InsideBlockRenderEvent;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer extends MixinEntity {

    @Inject(method = "isEntityInsideOpaqueBlock",at = @At("HEAD"),cancellable = true)
    private void onIsEntityInsideOpaqueBlock(CallbackInfoReturnable<Boolean> cir) {
        final InsideBlockRenderEvent insideBlockRenderEvent = new InsideBlockRenderEvent();
        Client.INSTANCE.getBus().fireEvent(insideBlockRenderEvent);
        if(insideBlockRenderEvent.isCancelled()) {
            cir.setReturnValue(false);
        }
    }
}