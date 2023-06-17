package fr.atesab.atehud.command.act;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.command.MainCommand;
import fr.atesab.atehud.command.SubCommand;
import fr.atesab.atehud.command.SubCommand.CommandClickOption;
import fr.atesab.atehud.gui.GuiNbtCode;
import fr.atesab.atehud.utils.CommandUtils;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class SCACTOpenNBT extends SubCommand {
	private final ArrayList<String> aliases;

	public SCACTOpenNBT() {
		super("opennbt", I18n.format(I18n.format("key.atehud.nbtitem").replaceAll("::", " ")),
				CommandClickOption.suggestCommand);
		aliases = new ArrayList<String>();
		aliases.add(getName());
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return CommandUtils.getTabCompletion(CommandUtils.getPlayerList(), args);
	}

	@Override
	public ArrayList<String> getAlias() {
		return this.aliases;
	}

	@Override
	public String getSubCommandUsage(ICommandSender sender) {
		return getName() + " <data>";
	}

	@Override
	public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
			throws CommandException {
		if (args.length > 0) {
			args[0] = CommandBase.buildString(args, 0);
			GuiUtils.OpenGuiScreen(new GuiNbtCode(null, args[0]), 10);
		}
	}

}