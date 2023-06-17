package fr.atesab.atehud.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonParseException;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.command.SubCommand.CommandClickOption;
import fr.atesab.atehud.utils.CommandUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;

public class SCDisplay extends SubCommand {
	private final ArrayList<String> aliases;

	public SCDisplay() {
		super("display", I18n.format(I18n.format("cmd.display").replaceAll("::", " ")),
				CommandClickOption.suggestCommand);
		aliases = new ArrayList<String>();
		aliases.add(getName());
		aliases.add("ds");
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
		return getName() + " <json>";
	}

	@Override
	public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
			throws CommandException {
		if (args.length < 1) {
			Chat.error(getSubCommandUsage(sender));
			return;
		}
		String s = CommandUtils.getOptionText(CommandBase.buildString(args, 0));
		s = s.replaceAll("&", "\u00a7") + "";
		Chat.show(s);
	}
}
