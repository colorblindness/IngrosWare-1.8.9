package ingros.ware.client.mixin.impl;

import ingros.ware.client.mixin.accessors.IAxisAlignedBB;
import javafx.scene.chart.Axis;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AxisAlignedBB.class)
public class MixinAxisAlignedBB implements IAxisAlignedBB {

    @Shadow
    public double minX;

    @Shadow
    public double minY;

    @Shadow
    public double minZ;

    @Shadow
    public double maxX;

    @Shadow
    public double maxY;

    @Shadow
    public double maxZ;

    @Override
    public AxisAlignedBB offsetAndUpdate(double par1, double par3, double par5) {
        this.minX += par1;
        this.minY += par3;
        this.minZ += par5;
        this.maxX += par1;
        this.maxY += par3;
        this.maxZ += par5;
        return (AxisAlignedBB) (Object) this;
    }
}