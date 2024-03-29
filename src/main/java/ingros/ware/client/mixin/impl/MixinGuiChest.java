package ingros.ware.client.mixin.impl;

import ingros.ware.client.mixin.accessors.IGuiChest;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiChest.class)
public abstract class MixinGuiChest implements IGuiChest {

    @Override
    @Accessor
    public abstract IInventory getLowerChestInventory();
}
