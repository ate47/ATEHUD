package fr.atesab.atehud.command;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.command.SubCommand.CommandClickOption;
import fr.atesab.atehud.utils.CommandUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class SCSay extends SubCommand {
	private final ArrayList<String> aliases;

	public SCSay() {
		super("say", I18n.format(I18n.format("cmd.say").replaceAll("::", " ")), CommandClickOption.suggestCommand);
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
		return getName() + " <message>";
	}

	@Override
	public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
			throws CommandException {
		Minecraft mc = Minecraft.getMinecraft();
		if (args.length > 0) {
			args[0] = CommandUtils.getOptionText(CommandBase.buildString(args, 0));
			if (mc.thePlayer != null)
				mc.thePlayer.sendChatMessage(args[0]);
		} else {
			Chat.error("/" + mainCommand.getCommandName() + " say <message>");
		}
	}
}
