package ingros.ware.client.module;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ingros.ware.client.Client;
import ingros.ware.client.events.KeyPressedEvent;
import ingros.ware.client.module.impl.combat.NoVelocity;
import ingros.ware.client.module.impl.exploits.CivBreak;
import ingros.ware.client.module.impl.exploits.Phase;
import ingros.ware.client.module.impl.movement.Sprint;
import ingros.ware.client.module.impl.player.ChestStealer;
import ingros.ware.client.module.impl.visuals.ESP;
import ingros.ware.client.module.impl.visuals.HUD;
import ingros.ware.client.utils.font.Fonts;
import net.b0at.api.event.Subscribe;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModuleManager {
    private Map<String, Module> map = new HashMap<>();
    private File dir;

    public void initializeModules() {
        registerMod(Sprint.class);
        registerMod(NoVelocity.class);
        registerMod(Phase.class);
        registerMod(ChestStealer.class);
        registerMod(ESP.class);
        registerMod(CivBreak.class);
        registerMod(HUD.class);
        Client.INSTANCE.getBus().registerListener(this);
    }

    @Subscribe
    public void onKeyPressed(KeyPressedEvent event) {
        for (Module module : map.values()) {
            if (module.getKeybind() == event.getKey()) module.setEnabled(!module.isEnabled());
        }
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public Map<String, Module> getMap() {
        return map;
    }

    public File getDir() {
        return dir;
    }

    public Map<String, Module> getModuleMap() {
        return map;
    }

    public boolean isModule(final String modulename) {
        for (Module mod : getModuleMap().values()) {
            if (mod.getLabel().equalsIgnoreCase(modulename)) {
                return true;
            }
        }
        return false;
    }

    public Module getModuleClass(final Class<?> clazz) {
        for (Module mod : getModuleMap().values()) {
            if (mod.getClass().equals(clazz)) {
                return mod;
            }
        }
        return null;
    }

    public ArrayList<Module> getModulesInCategory(Module.Category category) {
        final ArrayList<Module> mods = new ArrayList<>();
        for (Module module : map.values()) {
            if (module.getCategory() == category) {
                mods.add(module);
            }
        }
        return mods;
    }

    public float getLongestModInCategory(Module.Category category) {
        float width = Fonts.font.getStringWidth(getModulesInCategory(category).get(0).getLabel());
        for (Module module : getModulesInCategory(category)) {
            if (Fonts.font.getStringWidth(module.getLabel()) > width) {
                width = Fonts.font.getStringWidth(module.getLabel());
            }
        }
        return width;
    }

    public Module getModule(String name) {
        return getModuleMap().get(name.toLowerCase());
    }


    private void registerMod(Class<? extends Module> moduleClass) {
        try {
            Module createdModule = moduleClass.newInstance();
            map.put(createdModule.getLabel().toLowerCase(), createdModule);
        } catch (Exception ignored) {
        }
    }

    public void saveModules() {
        File[] files = dir.listFiles();
        if (!dir.exists()) {
            dir.mkdir();
        } else if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        getModuleMap().values().forEach(module -> {
            File file = new File(dir, module.getLabel() + ".json");
            JsonObject node = new JsonObject();
            module.save(node, true);
            if (node.entrySet().isEmpty()) {
                return;
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                return;
            }
            try (Writer writer = new FileWriter(file)) {
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(node));
            } catch (IOException e) {
                file.delete();
            }
        });
        files = dir.listFiles();
        if (files == null || files.length == 0) {
            dir.delete();
        }
    }

    public void loadModules() {
        getModuleMap().values().forEach(module -> {
            final File file = new File(dir, module.getLabel() + ".json");
            if (!file.exists()) {
                return;
            }
            try (Reader reader = new FileReader(file)) {
                JsonElement node = new JsonParser().parse(reader);
                if (!node.isJsonObject()) {
                    return;
                }
                module.load(node.getAsJsonObject());
            } catch (IOException e) {
            }
        });
    }
}
