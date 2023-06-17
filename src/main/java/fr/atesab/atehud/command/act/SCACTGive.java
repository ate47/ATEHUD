package fr.atesab.atehud.command.act;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.command.MainCommand;
import fr.atesab.atehud.command.SubCommand;
import fr.atesab.atehud.command.SubCommand.CommandClickOption;
import fr.atesab.atehud.utils.CommandUtils;
import fr.atesab.atehud.utils.GiveItemUtils;
import fr.atesab.atehud.utils.ItemHelp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.util.BlockPos;

public class SCACTGive extends SubCommand {
	private final ArrayList<String> aliases;

	public SCACTGive() {
		super("give", I18n.format(I18n.format("cmd.give").replaceAll("::", " ")), CommandClickOption.suggestCommand);
		aliases = new ArrayList<String>();
		aliases.add(getName());
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return CommandUtils.getTabCompletion(CommandUtils.getItemList(), args);
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
			try {
				GiveItemUtils.give(Minecraft.getMinecraft(), ItemHelp.getGive(args[0]));
			} catch (NumberInvalidException e) {
				Chat.error(I18n.format("gui.act.cantgive"));
			}
		} else {
			Chat.error("/" + mainCommand.getCommandName() + " give <data>");
		}
	}
}
