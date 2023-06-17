package fr.atesab.atehud.command.spammer;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.command.MainCommand;
import fr.atesab.atehud.command.SubCommand;
import fr.atesab.atehud.gui.GuiSpammer;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class SCSpammerGui extends SubCommand {
	private ArrayList<String> aliases = new ArrayList<String>();
	public SCSpammerGui() {
		super("gui", I18n.format("cmd.spammer.gui"), SubCommand.CommandClickOption.doCommand);
		aliases.add(getName());
	}

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
	public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
			throws CommandException {
		GuiUtils.OpenGuiScreen(new GuiSpammer(null), 10);
	}

}
