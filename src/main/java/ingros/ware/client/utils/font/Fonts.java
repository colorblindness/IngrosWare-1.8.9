package ingros.ware.client.utils.font;

import java.awt.Font;
import java.io.File;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Fonts {
    public static final MCFontRenderer font = new MCFontRenderer(fontFromTTF("assets/Comfortaa.ttf", 18, Font.PLAIN), true, true);

    private static Font fontFromTTF(String fontLocation, float fontSize, int fontType) {
        Fonts fonts = new Fonts();
        Font output = null;
        try {
            output = Font.createFont(fontType, fonts.getClass().getClassLoader().getResource(fontLocation).openStream());
            output = output.deriveFont(fontSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
