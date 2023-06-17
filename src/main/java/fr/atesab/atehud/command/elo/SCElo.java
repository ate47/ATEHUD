package fr.atesab.atehud.command.elo;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.command.SCHelp;
import fr.atesab.atehud.command.SCMainCommand;
import fr.atesab.atehud.command.SubCommand;
import fr.atesab.atehud.command.SubCommand.CommandClickOption;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class SCElo extends SCMainCommand {
	public SCHelp help;
	public SCEloWin win;
	public SCEloNext next;
	private ArrayList<String> aliases;
	public SCElo() {
		super(new ArrayList<SubCommand>(), "help", "elo", I18n.format("cmd.elo"), CommandClickOption.doCommand);
		this.subCommands.add(win = new SCEloWin());
		this.subCommands.add(next = new SCEloNext());
		this.subCommands.add(help = new SCHelp("Elo", 5));
		aliases = new ArrayList<String>();
		aliases.add(getName());
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
