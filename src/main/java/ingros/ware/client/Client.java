package ingros.ware.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import ingros.ware.client.command.CommandManager;
import ingros.ware.client.module.ModuleManager;
import net.b0at.api.event.Event;
import net.b0at.api.event.EventManager;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.nio.file.Path;

public enum Client {
    INSTANCE;
    private final String label = ChatFormatting.LIGHT_PURPLE + "Ingros" + ChatFormatting.WHITE + "Ware", version = "0.1";
    private final Path path = new File(System.getProperty("user.home"), ChatFormatting.stripFormatting(label)).toPath();
    private final EventManager<Event> bus = new EventManager<>(Event.class);
    private final ModuleManager moduleManager = new ModuleManager();
    private final CommandManager commandManager = new CommandManager();

    public void startupClient() {
        moduleManager.setDir(new File(path.toString(), "modules"));
        moduleManager.initializeModules();
        moduleManager.loadModules();
        commandManager.initialize();
        System.out.println("[IngrosWare] Loaded");
    }

    public void shutdownClient() {
        moduleManager.saveModules();
        System.out.println("[IngrosWare] Unloaded");
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public EventManager<Event> getBus() {
        return bus;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public String getLabel() {
        return label;
    }

    public String getVersion() {
        return version;
    }

    public Path getPath() {
        return path;
    }
}
