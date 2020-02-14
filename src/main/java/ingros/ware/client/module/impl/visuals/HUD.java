package ingros.ware.client.module.impl.visuals;

import com.mojang.realmsclient.gui.ChatFormatting;
import ingros.ware.client.Client;
import ingros.ware.client.events.Render2DEvent;
import ingros.ware.client.module.Module;
import ingros.ware.client.utils.font.Fonts;
import ingros.ware.client.utils.value.impl.BooleanValue;
import net.b0at.api.event.Subscribe;

import java.util.ArrayList;
import java.util.Comparator;

public class HUD extends Module {
    public HUD() {
        super("HUD", Category.VISUALS, 0xffff0020);
        setEnabled(true);
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        Fonts.font.drawStringWithShadow(Client.INSTANCE.getLabel() +" " + Client.INSTANCE.getVersion(), 2, 4, -1);
        int posY = 4;
        final ArrayList<Module> sorted = new ArrayList<>(Client.INSTANCE.getModuleManager().getModuleMap().values());
        sorted.sort(Comparator.comparingDouble(module -> -Fonts.font.getStringWidth(getFinishedLabel(module))));
        for (Module module : sorted) {
            if (!module.isEnabled() || module.isHidden()) continue;
            Fonts.font.drawStringWithShadow(module.getLabel(), event.getScaledResolution().getScaledWidth() - Fonts.font.getStringWidth(module.getLabel()) - 2, posY, module.getColor());
            posY += Fonts.font.getHeight() + 4;
        }
    }

    private String getFinishedLabel(Module module) {
        final StringBuilder label = new StringBuilder();
        label.append(module.getRenderLabel() != null ? module.getRenderLabel() : module.getLabel());
        if (module.getSuffix() != null) label.append(" ").append(ChatFormatting.GRAY).append(module.getSuffix());
        return label.toString();
    }
}
