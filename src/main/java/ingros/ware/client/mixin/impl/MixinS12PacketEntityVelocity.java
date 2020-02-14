package ingros.ware.client.mixin.impl;

import ingros.ware.client.mixin.accessors.IS12PacketEntityVelocity;
import ingros.ware.client.mixin.accessors.IS27PacketExplosion;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(S12PacketEntityVelocity.class)
public abstract class MixinS12PacketEntityVelocity implements IS12PacketEntityVelocity {

    @Override
    @Accessor
    public abstract void setMotionY(int motionY);

    @Override
    @Accessor
    public abstract void setMotionX(int motionX);

    @Override
    @Accessor
    public abstract void setMotionZ(int motionZ);
}
