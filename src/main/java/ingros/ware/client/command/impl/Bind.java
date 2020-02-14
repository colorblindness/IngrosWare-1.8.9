package ingros.ware.client.command.impl;

import ingros.ware.client.Client;
import ingros.ware.client.command.Command;
import ingros.ware.client.module.Module;
import ingros.ware.client.utils.Printer;
import org.lwjgl.input.Keyboard;

import java.util.Objects;

public class Bind extends Command {

	public Bind() {
		super("Bind",new String[]{"bind","b"});
	}

	@Override
	public void onRun(final String[] args) {
		if (args.length == 2) {
			if (args[1].toLowerCase().equals("resetall")) {
				Client.INSTANCE.getModuleManager().getModuleMap().values().forEach(m -> m.setKeybind(0));
				return;
			}
		}
		if (args.length == 3) {
			String moduleName = args[1];
			Module module = Client.INSTANCE.getModuleManager().getModule(moduleName);
			if (module != null) {
				int keyCode = Keyboard.getKeyIndex(args[2].toUpperCase());
				if (keyCode != -1) {
					module.setKeybind(keyCode);
					Printer.print(module.getLabel() + " is now bound to \"" + Keyboard.getKeyName(keyCode) + "\".");
				} else {
					Printer.print("That is not a valid key code.");
				}
			} else {
                Printer.print("That module does not exist.");
				Printer.print("Type \".modules\" for a list of all modules.");
			}
		} else {
			Printer.print("Invalid arguments.");
			Printer.print("Usage: \".bind [module] [key]\"");
		}
	}
}
