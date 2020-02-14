package ingros.ware.client.mixin.accessors;

import net.minecraft.util.AxisAlignedBB;

public interface IAxisAlignedBB {

    AxisAlignedBB offsetAndUpdate(double par1, double par3, double par5);
}