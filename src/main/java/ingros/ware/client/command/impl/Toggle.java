package ingros.ware.client.command.impl;

import ingros.ware.client.Client;
import ingros.ware.client.command.Command;
import ingros.ware.client.module.Module;
import ingros.ware.client.utils.Printer;

public class Toggle extends Command {

    public Toggle() {
        super("Toggle", new String[]{"t", "toggle"});
    }

    @Override
    public void onRun(final String[] s) {
        if (s.length <= 1) {
            Printer.print("Not enough args.");
            return;
        }
        if (!Client.INSTANCE.getModuleManager().isModule(s[1])) {
        	Printer.print("Invalid module!");
		}
        for (Module m : Client.INSTANCE.getModuleManager().getModuleMap().values()) {
            if (m.getLabel().toLowerCase().equals(s[1])) {
                m.toggle();
                Printer.print("Toggled " + m.getLabel());
                break;
            }
        }
    }
}
