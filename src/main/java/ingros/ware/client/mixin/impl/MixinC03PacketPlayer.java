package ingros.ware.client.mixin.impl;

import ingros.ware.client.mixin.accessors.IC03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(C03PacketPlayer.class)
public abstract class MixinC03PacketPlayer implements IC03PacketPlayer {

    @Override
    @Accessor
    public abstract void setOnGround(boolean onGround);

    @Override
    @Accessor
    public abstract void setY(double y);

    @Override
    @Accessor
    public abstract void setX(double x);

    @Override
    @Accessor
    public abstract void setZ(double z);

    @Override
    @Accessor
    public abstract double getY();

    @Override
    @Accessor
    public abstract double getX();

    @Override
    @Accessor
    public abstract double getZ();

    @Override
    @Accessor
    public abstract void setYaw(float yaw);

    @Override
    @Accessor
    public abstract float getYaw();

    @Override
    @Accessor
    public abstract void setPitch(float pitch);

    @Override
    @Accessor
    public abstract float getPitch();
}