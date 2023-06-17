package fr.atesab.atehud.command;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.command.SubCommand.CommandClickOption;
import fr.atesab.atehud.ts3.TS3Socket;
import fr.atesab.atehud.utils.CommandUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class SCTeamspeak extends SubCommand {
	private final ArrayList<String> aliases;

	public SCTeamspeak() {
		super("ts", I18n.format(I18n.format("cmd.ts3").replaceAll("::", " ")), CommandClickOption.suggestCommand);
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
		if (args.length > 0) {
			args[0] = CommandUtils.getOptionText(CommandBase.buildString(args, 0));
			try {
				String txt = TS3Socket.compileText(args[0]);
				TS3Socket.sendCommand("sendtextmessage targetmode=2 msg=" + txt);
				System.out.println(txt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
