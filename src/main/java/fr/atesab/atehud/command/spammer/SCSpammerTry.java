package fr.atesab.atehud.command.spammer;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.command.MainCommand;
import fr.atesab.atehud.command.SubCommand;
import fr.atesab.atehud.utils.CommandUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class SCSpammerTry extends SubCommand {
	private ArrayList<String> aliases = new ArrayList<String>();

	public SCSpammerTry() {
		super("try", I18n.format("cmd.spammer.try"), CommandClickOption.doCommand);
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
		CommandUtils.sendOptionMessage("/"+ModMain.spamCommand);
	}

}
