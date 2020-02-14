package ingros.ware.client.command.impl;

import ingros.ware.client.Client;
import ingros.ware.client.command.Command;
import ingros.ware.client.utils.Printer;
import org.apache.commons.lang3.text.WordUtils;

public class Help extends Command {

	public Help() {
		super("Help", new String[]{"h", "help"});
	}

	@Override
	public void onRun(final String[] s) {
		Client.INSTANCE.getCommandManager().getCommandMap().values().forEach(command -> {
			Printer.print(WordUtils.capitalizeFully(command.getLabel()));
		});
	}
}
