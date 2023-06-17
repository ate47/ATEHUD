package fr.atesab.atehud.command;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.command.SubCommand.CommandClickOption;
import fr.atesab.atehud.gui.GuiTeleporter;
import fr.atesab.atehud.utils.CommandUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class SCTeleport extends SubCommand {
	private final ArrayList<String> aliases;

	public SCTeleport() {
		super("tp", I18n.format(I18n.format("mod.others.tpgm.desc").replaceAll("::", " ")),
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
			GuiTeleporter.teleport(args[0]);
		else
			Chat.error("/" + mainCommand.getCommandName() + " tp <" + I18n.format("cmd.username") + ">");
	}
}
