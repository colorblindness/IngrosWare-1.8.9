package ingros.ware.client.mixin.impl;

import ingros.ware.client.Client;
import ingros.ware.client.events.MotionEvent;
import ingros.ware.client.events.PushEvent;
import ingros.ware.client.events.UpdateEvent;
import ingros.ware.client.mixin.accessors.IEntityPlayerSP;
import net.b0at.api.event.types.EventType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP extends MixinEntityPlayer implements IEntityPlayerSP {
    @Shadow
    protected Minecraft mc;

    private UpdateEvent eventUpdate;

    @Override
    public void moveEntity(double x, double y, double z) {
        MotionEvent event = new MotionEvent(x, y, z);
        Client.INSTANCE.getBus().fireEvent(event);
        super.moveEntity(event.getX(), event.getY(), event.getZ());
    }

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    private void onPushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        final PushEvent eventPushOutOfBlocks = new PushEvent();
        Client.INSTANCE.getBus().fireEvent(eventPushOutOfBlocks);
        if (eventPushOutOfBlocks.isCancelled()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
    private void onUpdateWalkingPlayerHead(CallbackInfo ci) {
        eventUpdate = new UpdateEvent(getRotationYaw(),getRotationPitch(), getPosY(), isOnGround(), EventType.PRE);
        Client.INSTANCE.getBus().fireEvent(eventUpdate, EventType.PRE);
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/AxisAlignedBB;minY:D"))
    private double onUpdateWalkingPlayerMinY(AxisAlignedBB boundingBox) {
        return eventUpdate.getY();
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;onGround:Z"))
    private boolean onUpdateWalkingPlayerOnGround(EntityPlayerSP player) {
        return eventUpdate.isOnGround();
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationYaw:F"))
    private float onUpdateWalkingPlayerRotationYaw(EntityPlayerSP player) {
        return eventUpdate.getYaw();
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationPitch:F"))
    private float onUpdateWalkingPlayerRotationPitch(EntityPlayerSP player) {
        return eventUpdate.getPitch();
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"))
    private void onUpdateWalkingPlayerReturn(CallbackInfo ci) {
        Client.INSTANCE.getBus().fireEvent(new UpdateEvent(), EventType.POST);
    }

    @Override
    public boolean isMoving() {
        return mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown();
    }
}