package fr.atesab.atehud.command;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.utils.MultiChatComponentText;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class SCHelp extends SubCommand {

	private final ArrayList<String> aliases;
	private final String title;
	private final int elm;

	public SCHelp(String title, int elementByPage) {
		super("help", I18n.format("cmd.help.cmd"), CommandClickOption.suggestCommand);
		this.title = title;
		this.elm = elementByPage;
		aliases = new ArrayList<String>();
		aliases.add(getName());
		aliases.add("?");
	}

	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return new ArrayList<String>();
	}

	public ArrayList<String> getAlias() {
		return this.aliases;
	}

	public String getSubCommandUsage(ICommandSender sender) {
		return getName() + " (page)";
	}

	public ArrayList<SubCommand> getVisibleSubCommand(MainCommand mainCommand) {
		ArrayList<SubCommand> sc = new ArrayList<SubCommand>();
		for (int i = 0; i < mainCommand.subCommands.size(); i++) {
			if (mainCommand.subCommands.get(i).isDisplayInHelp())
				sc.add(mainCommand.subCommands.get(i));
		}
		return sc;
	}

	public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
			throws CommandException {
		ArrayList<SubCommand> sc = getVisibleSubCommand(mainCommand);
		int page = 0;
		int maxPage = mainCommand.subCommands.size() / elm;
		if (args.length == 1) {
			try {
				page = Integer.valueOf(args[0]) - 1;
				if (page > maxPage)
					page = maxPage;
				else if (page < 0)
					page = 0;
			} catch (Exception e) {
				Chat.error(I18n.format("cmd.NaN", '\"' + args[0] + '\"'));
				return;
			}
		}
		ArrayList<IChatComponent> help = new ArrayList<IChatComponent>();
		help.add((ChatComponentText) (new ChatComponentText(
				"-- " + I18n.format("cmd.help", title) + " (" + (page + 1) + " / " + (maxPage + 1) + ") --")
						.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED))));
		for (int i = page * elm; i < (page + 1) * elm && i < mainCommand.subCommands.size(); i++) {
			help.add(MultiChatComponentText.getHelp(mainCommand.getCommandName(), mainCommand.subCommands.get(i),
					sender));
		}
		help.add(MultiChatComponentText.getCommandPage(mainCommand.getCommandName() + " help", page + 1, maxPage + 1));
		Chat.send(help);

	}

}
