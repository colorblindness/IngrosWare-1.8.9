package ingros.ware.client.mixin.impl;

import ingros.ware.client.Client;
import ingros.ware.client.events.BoundingBoxEvent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Block.class)
public abstract class MixinBlock {

    private BoundingBoxEvent bbEvent;

    @Shadow
    public abstract AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state);

    @Inject(method = "addCollisionBoxesToList(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;)V",
            at = @At("HEAD"))
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity, CallbackInfo info) {
        final Block block = (Block) (Object) (this);
        bbEvent = new BoundingBoxEvent(block, pos, mask);
        Client.INSTANCE.getBus().fireEvent(bbEvent);
    }

    @Redirect(method = "addCollisionBoxesToList(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getCollisionBoundingBox(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/AxisAlignedBB;"))
    private AxisAlignedBB getBB(IBlockState state, IBlockAccess world, BlockPos pos, CallbackInfo ci) {
        AxisAlignedBB bb = (bbEvent == null) ?
                getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, pos, state) :
                bbEvent.getBoundingBox();
        bbEvent = null;
        return bb;
    }

}