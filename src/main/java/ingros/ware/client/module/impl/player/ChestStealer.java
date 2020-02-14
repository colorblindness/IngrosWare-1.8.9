package ingros.ware.client.module.impl.player;

import ingros.ware.client.events.UpdateEvent;
import ingros.ware.client.mixin.accessors.IGuiChest;
import ingros.ware.client.module.Module;
import ingros.ware.client.utils.TimerUtil;
import ingros.ware.client.utils.value.impl.NumberValue;
import net.b0at.api.event.Subscribe;
import net.b0at.api.event.types.EventType;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * made by oHare for eclipse
 *
 * @since 8/28/2019
 **/
public class ChestStealer extends Module {
    private boolean doneSteal;

    public ChestStealer() {
        super("ChestStealer", Category.PLAYER, new Color(255, 75, 170).getRGB());
        setRenderLabel("Chest Stealer");
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (getMc().thePlayer == null) return;
        if (event.getEventType() == EventType.PRE) {
            if (getMc().currentScreen instanceof GuiChest) {
                final GuiChest chest = (GuiChest) getMc().currentScreen;
                if (isChestEmpty(chest) || isInventoryFull()) {
                    getMc().thePlayer.closeScreen();
                    doneSteal = true;
                }
                for (int index = 0; index < ((IGuiChest) chest).getLowerChestInventory().getSizeInventory(); ++index) {
                    final ItemStack stack = ((IGuiChest) chest).getLowerChestInventory().getStackInSlot(index);
                    if ((stack != null)) {
                        getMc().playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, getMc().thePlayer);
                    }
                }
            } else doneSteal = true;
        }
    }

    private boolean isChestEmpty(final GuiChest chest) {
        for (int index = 0; index <= ((IGuiChest) chest).getLowerChestInventory().getSizeInventory(); ++index) {
            final ItemStack stack = ((IGuiChest) chest).getLowerChestInventory().getStackInSlot(index);
            if (stack != null) {
                return false;
            }
        }
        return true;
    }

    private float secRanFloat(float min, float max) {
        return new SecureRandom().nextFloat() * (max - min) + min;
    }

    private boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = getMc().thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }
}