package fr.atesab.atehud.command;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.utils.CommandUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class SCOpenChat extends SubCommand {
	private ArrayList<String> aliases;
	public SCOpenChat() {
		super("openchat", I18n.format("cmd.openchat"), CommandClickOption.suggestCommand);
		this.aliases = new ArrayList<String>();
		this.aliases.add(getName());
	}
	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return CommandUtils.getTabCompletion(CommandUtils.getPlayerList(), args);
	}
	@Override
	public ArrayList<String> getAlias() {
		return aliases;
	}
	@Override
	public String getSubCommandUsage(ICommandSender sender) {
		return getName()+" [text]";
	}

	@Override
	public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
			throws CommandException {
		Minecraft.getMinecraft().displayGuiScreen(new GuiChat(CommandBase.buildString(args, 0)));
	}

}
