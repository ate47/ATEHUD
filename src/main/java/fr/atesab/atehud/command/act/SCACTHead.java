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
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class SCACTHead extends SubCommand {
	private final ArrayList<String> aliases;

	public SCACTHead() {
		super("head", I18n.format(I18n.format("mod.giver.head.desc").replaceAll("::", " ")),
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
		return getName() + " <" + I18n.format("cmd.username") + ">";
	}

	@Override
	public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
			throws CommandException {
		if (args.length == 1)
			GiveItemUtils.give(Minecraft.getMinecraft(), ItemHelp.getHead(args[0]));
		else
			Chat.error("/" + mainCommand.getCommandName() + " head <" + I18n.format("cmd.username") + ">");
	}
}
