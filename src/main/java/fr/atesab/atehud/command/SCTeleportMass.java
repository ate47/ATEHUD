package fr.atesab.atehud.command;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.command.SubCommand.CommandClickOption;
import fr.atesab.atehud.gui.GuiTeleporter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class SCTeleportMass extends SubCommand {
	private final ArrayList<String> aliases;

	public SCTeleportMass() {
		super("tpm", I18n.format(I18n.format("teleporter.teleport.all").replaceAll("::", " ")),
				CommandClickOption.doCommand);
		aliases = new ArrayList<String>();
		aliases.add(getName());
		aliases.add("tpmass");
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
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.thePlayer.capabilities.isCreativeMode)
			mc.thePlayer.sendChatMessage("/gamemode 3");
		NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
		GuiTeleporter.toTpPlayers = (ArrayList<NetworkPlayerInfo>) ATEEventHandler.field_175252_a
				.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
	}
}
