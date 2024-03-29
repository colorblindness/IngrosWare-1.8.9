package ingros.ware.client.command;


import ingros.ware.client.Client;
import ingros.ware.client.command.impl.Bind;
import ingros.ware.client.command.impl.Help;
import ingros.ware.client.command.impl.Modules;
import ingros.ware.client.command.impl.Toggle;
import ingros.ware.client.events.ChatEvent;
import ingros.ware.client.module.Module;
import ingros.ware.client.utils.Printer;
import ingros.ware.client.utils.value.Value;
import ingros.ware.client.utils.value.impl.EnumValue;
import net.b0at.api.event.Subscribe;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    public Map<String, Command> map = new HashMap<>();

    public void initialize() {
        register(Bind.class);
        register(Help.class);
        register(Toggle.class);
        register(Modules.class);
        Client.INSTANCE.getBus().registerListener(this);
    }

    @Subscribe
    public void onChatEvent(ChatEvent event) {
        final String message = event.getMsg();
        if (message.startsWith(".")) {
            event.setCancelled(true);
            dispatch(message.substring(1));
        }
    }

    private void register(Class<? extends Command> commandClass) {
        try {
            Command createdCommand = commandClass.newInstance();
            map.put(createdCommand.getLabel().toLowerCase(), createdCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dispatch(final String s) {
        final String[] command = s.split(" ");
        if (command.length > 1) {
            Module m = Client.INSTANCE.getModuleManager().getModule(command[0]);
            if (m != null) {
                if (command[1].equalsIgnoreCase("help")) {
                    if (!m.getValues().isEmpty()) {
                        Printer.print(m.getLabel() + "'s available properties are:");
                        for (Value value : m.getValues()) {
                            if (value.getParentValueObject() != null && !value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))continue;
                            Printer.print(value.getLabel() + ": " + value.getValue());
                            if (value instanceof EnumValue) {
                                Printer.print(value.getLabel() + "'s available modes are:");
                                StringBuilder modes = new StringBuilder();
                                for (Enum vals : ((EnumValue) value).getConstants()) {
                                    modes.append(vals);
                                    modes.append(" ");
                                }
                                Printer.print(modes.toString());
                            }
                        };
                    } else {
                        Printer.print("This cheat has no properties.");
                    }
                    Printer.print(m.getLabel() + " is a " + (m.isHidden() ? "hidden " : "shown ") + "module.");
                    Printer.print(m.getLabel() + " is bound to " + Keyboard.getKeyName(m.getKeybind()) + ".");
                    return;
                }
                if (command.length > 2) {
                    if (command[1].equalsIgnoreCase("hidden")) {
                        m.setHidden(Boolean.parseBoolean(command[2].toLowerCase()));
                        Printer.print("Set " + m.getLabel() + " to " + m.isHidden() + ".");
                        return;
                    }
                    for (Value value : m.getValues()) {
                        if (value.getParentValueObject() != null && !value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))continue;
                        if (value.getLabel().replace(" ","").toLowerCase().equals(command[1].toLowerCase())) {
                            value.setValue(command[2]);
                            Printer.print("Set " + command[0] + " " + value.getLabel() + " to " + value.getValue() + ".");
                        }
                    };
                }
            }
        }
        Client.INSTANCE.getCommandManager().getCommandMap().values().forEach(c -> {
            for (String handle : c.getHandles()) {
                if (handle.toLowerCase().equals(command[0])) c.onRun(command);
            }
        });
    }

    public Map<String, Command> getCommandMap() {
        return map;
    }
}

