package fr.atesab.atehud.command;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.command.SubCommand.CommandClickOption;
import fr.atesab.atehud.gui.GuiMenu;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public abstract class SCActionCommand extends SubCommand {
	private final ArrayList<String> aliases;

	public SCActionCommand(String name, String description, CommandClickOption clickOption) {
		super(name, description, clickOption);
		aliases = new ArrayList<String>();
		aliases.add(getName());
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return new ArrayList<String>();
	}

	@Override
	public ArrayList<String> getAlias() {
		return this.aliases;
	}

	@Override
	public String getSubCommandUsage(ICommandSender sender) {
		return getName();
	}

	@Override
	public abstract void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
			throws CommandException;
}
