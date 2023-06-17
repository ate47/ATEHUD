package fr.atesab.atehud.command;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.superclass.TextOption;
import fr.atesab.atehud.utils.CommandUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class SCDisplayOptionsList extends SubCommand {
	private ArrayList<String> aliases = new ArrayList<String>();

	public SCDisplayOptionsList() {
		super("displayoptionslist", I18n.format("cmd.spammer.optionslist"), SubCommand.CommandClickOption.doCommand);
		aliases.add(getName());
		aliases.add("dol");
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
		IChatComponent msg = new ChatComponentText("-- "+I18n.format("chatOption.to")+" --").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
		for (TextOption to: ModMain.textOptions)
			msg.appendSibling(new ChatComponentText("\n- ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)))
			.appendSibling(new ChatComponentText("&"+to.getName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)))
			.appendSibling(new ChatComponentText(" : ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)))
			.appendSibling(new ChatComponentText(I18n.format("chatOption.to."+to.getName())).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)));
		sender.addChatMessage(msg);
	}

}
