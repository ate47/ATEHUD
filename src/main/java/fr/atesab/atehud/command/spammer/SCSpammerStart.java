package fr.atesab.atehud.command.spammer;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.command.MainCommand;
import fr.atesab.atehud.command.SubCommand;
import fr.atesab.atehud.command.SubCommand.CommandClickOption;
import fr.atesab.atehud.utils.CommandUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class SCSpammerStart extends SubCommand {
	private ArrayList<String> aliases = new ArrayList<String>();
	public SCSpammerStart() {
		super("start", I18n.format("cmd.spammer.start"), CommandClickOption.doCommand);
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
	public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
			throws CommandException {
		ATEEventHandler.action=0;
		ATEEventHandler.tickw=0;
		ATEEventHandler.isEnable = true;
		sender.addChatMessage(new ChatComponentText(I18n.format("cmd.spammer.start.msg"))
				.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
	}

}
