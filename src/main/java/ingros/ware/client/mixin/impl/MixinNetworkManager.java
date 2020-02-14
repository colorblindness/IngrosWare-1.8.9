package ingros.ware.client.mixin.impl;

import ingros.ware.client.Client;
import ingros.ware.client.events.PacketEvent;
import net.b0at.api.event.types.EventType;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {


    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
        final PacketEvent event = new PacketEvent(EventType.PRE, packet);
        Client.INSTANCE.getBus().fireEvent(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void onChannelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
        final PacketEvent event = new PacketEvent(EventType.POST, packet);
        Client.INSTANCE.getBus().fireEvent(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }
}