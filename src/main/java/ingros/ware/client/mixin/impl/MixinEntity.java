package ingros.ware.client.mixin.impl;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public class MixinEntity {


    @Shadow
    public float rotationYaw;
    @Shadow
    public float rotationPitch;
    @Shadow
    public double posY;
    @Shadow
    public boolean onGround;

    @Shadow
    public void moveEntity(double p_moveEntity_1_, double p_moveEntity_3_, double p_moveEntity_5_) {}

    public float getRotationYaw() {
        return rotationYaw;
    }

    public float getRotationPitch() {
        return rotationPitch;
    }

    public double getPosY() {
        return posY;
    }

    public boolean isOnGround() {
        return onGround;
    }
}