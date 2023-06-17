package fr.atesab.atehud.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mojang.authlib.GameProfile;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.Chat;
import fr.atesab.atehud.utils.CommandUtils;
import fr.atesab.atehud.utils.MultiChatComponentText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class SCList extends SubCommand {
	private final ArrayList<String> aliases;
	private String PlayerCommand;

	public SCList(String PlayerCommand) {
		super("list", I18n.format("cmd.list"), SubCommand.CommandClickOption.doCommand);
		this.PlayerCommand = PlayerCommand;
		aliases = new ArrayList<String>();
		aliases.add(this.getName());
		aliases.add("online");
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return new ArrayList<String>();
	}

	@Override
	public ArrayList<String> getAlias() {
		return aliases;
	}

	@Override
	public String getSubCommandUsage(ICommandSender sender) {
		return getName();
	}

	@Override
	public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
			throws CommandException {
		Minecraft mc = Minecraft.getMinecraft();
		HashMap<String, ArrayList<NetworkPlayerInfo>> players = new HashMap<String, ArrayList<NetworkPlayerInfo>>();
		NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
		List list = ATEEventHandler.field_175252_a.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
		for (int i = 0; i < list.size(); i++) {
			String team;
			if (((NetworkPlayerInfo) list.get(i)).getPlayerTeam() != null)
				team = ((NetworkPlayerInfo) list.get(i)).getPlayerTeam().getRegisteredName();
			else
				team = I18n.format("cmd.list.noTeam");
			NetworkPlayerInfo gf = ((NetworkPlayerInfo) list.get(i));
			ArrayList<NetworkPlayerInfo> array = players.getOrDefault(team, new ArrayList<NetworkPlayerInfo>());
			array.add(gf);
			players.put(team, array);
		}
		ArrayList<IChatComponent> message = new ArrayList<IChatComponent>();
		message.add(new ChatComponentText(I18n.format("cmd.list") + " (" + list.size() + ")")
				.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
		for (String team : players.keySet()) {
			ArrayList<NetworkPlayerInfo> glPlayer = players.get(team);
			ArrayList<IChatComponent> teamPlayers = new ArrayList<IChatComponent>();
			teamPlayers.add(new ChatComponentText(team + " (" + glPlayer.size() + ")")
					.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
			teamPlayers.add(new ChatComponentText(":").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
			for (int i = 0; i < glPlayer.size(); i++) {
				teamPlayers.add(new ChatComponentText(" "));
				teamPlayers.add(
						new ChatComponentText(glPlayer.get(i).getGameProfile().getName()).setChatStyle(new ChatStyle()
								.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
										PlayerCommand + " " + glPlayer.get(i).getGameProfile().getName()))
								.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
										new ChatComponentText(CommandUtils.getPlayerName(glPlayer.get(i)))))));
			}
			message.add(new MultiChatComponentText(teamPlayers));
		}
		Chat.send(message);
	}

}
