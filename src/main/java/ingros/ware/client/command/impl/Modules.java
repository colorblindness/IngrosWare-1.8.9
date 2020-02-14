package ingros.ware.client.command.impl;


import ingros.ware.client.Client;
import ingros.ware.client.command.Command;
import ingros.ware.client.utils.Printer;

public class Modules extends Command {

    public Modules() {
        super("Modules", new String[]{"modules","mods","m"});
    }

    @Override
    public void onRun(final String[] s) {
        StringBuilder mods = new StringBuilder("Modules (" + Client.INSTANCE.getModuleManager().getModuleMap().values().size() + "): ");
        Client.INSTANCE.getModuleManager().getModuleMap().values()
                .forEach(mod -> mods.append(mod.isEnabled() ? "\247a" : "\247c").append(mod.getLabel()).append("\247r, "));
        Printer.print(mods.toString().substring(0, mods.length() - 2));
    }
}