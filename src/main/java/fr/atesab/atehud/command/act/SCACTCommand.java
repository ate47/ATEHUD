package fr.atesab.atehud.command.act;

import java.util.ArrayList;

import fr.atesab.atehud.command.SCHelp;
import fr.atesab.atehud.command.SCMainCommand;
import fr.atesab.atehud.command.SubCommand;
import fr.atesab.atehud.command.SubCommand.CommandClickOption;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;

public class SCACTCommand extends SCMainCommand {
	public SCACTGive give;
	public SCACTHead head;
	public SCACTOpenNBT opennbt;
	public SCACTRename rename;
	public SCACTEnchant enchant;
	public SCHelp help;
	private final ArrayList<String> aliases;

	public SCACTCommand(String defaultCommand) {
		super(new ArrayList<SubCommand>(), defaultCommand, "act", I18n.format("cmd.act").replaceAll("::", " "),
				SubCommand.CommandClickOption.doCommand);
		subCommands.add(give = new SCACTGive());
		subCommands.add(head = new SCACTHead());
		subCommands.add(opennbt = new SCACTOpenNBT());
		subCommands.add(rename = new SCACTRename());
		subCommands.add(enchant = new SCACTEnchant());
		subCommands.add(help = new SCHelp(I18n.format("cmd.act"), 10));
		aliases = new ArrayList<String>();
		aliases.add(getName());
	}

	public ArrayList<String> getAlias() {
		return this.aliases;
	}

	public String getSubCommandUsage(ICommandSender sender) {
		return getName();
	}

}
