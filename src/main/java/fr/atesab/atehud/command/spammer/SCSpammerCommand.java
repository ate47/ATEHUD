package fr.atesab.atehud.command.spammer;

import java.util.ArrayList;

import fr.atesab.atehud.command.SCHelp;
import fr.atesab.atehud.command.SCMainCommand;
import fr.atesab.atehud.command.SubCommand;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;

public class SCSpammerCommand extends SCMainCommand {

	private final ArrayList<String> aliases;
	public SCHelp help;
	public SCSpammerGui gui;
	public SCSpammerSet set;
	public SCSpammerStart start;
	public SCSpammerStop stop;
	public SCSpammerTry trycmd;
	public SCSpammerCommand(String defaultCommand) {
		super(new ArrayList<SubCommand>(), defaultCommand, "spammer", I18n.format("cmd.spammer"), SubCommand.CommandClickOption.doCommand);
		subCommands.add(help = new SCHelp(I18n.format("cmd.spammer"), 5));
		subCommands.add(gui = new SCSpammerGui());
		subCommands.add(set = new SCSpammerSet());
		subCommands.add(start = new SCSpammerStart());
		subCommands.add(stop = new SCSpammerStop());
		subCommands.add(trycmd = new SCSpammerTry());
		aliases = new ArrayList<String>();
		aliases.add(getName());
		aliases.add("spam");
	}

	@Override
	public ArrayList<String> getAlias() {
		return aliases;
	}

	@Override
	public String getSubCommandUsage(ICommandSender sender) {
		return getName();
	}

}
