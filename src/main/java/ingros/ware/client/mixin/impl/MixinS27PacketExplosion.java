package ingros.ware.client.mixin.impl;

import ingros.ware.client.mixin.accessors.IS27PacketExplosion;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(S27PacketExplosion.class)
public abstract class MixinS27PacketExplosion implements IS27PacketExplosion {

    @Override
    @Accessor
    public abstract void setPosY(double posY);

    @Override
    @Accessor
    public abstract void setPosX(double posX);

    @Override
    @Accessor
    public abstract void setPosZ(double posZ);
}
