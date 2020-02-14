package ingros.ware.client.utils;

import com.mojang.realmsclient.gui.ChatFormatting;

import ingros.ware.client.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;

public class Printer {

    public static void print(final String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation(ChatFormatting.GRAY + "[" + Client.INSTANCE.getLabel()+ ChatFormatting.GRAY + "]" + ChatFormatting.WHITE + " > " + message));
    }
}