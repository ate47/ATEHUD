package fr.atesab.atehud.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class SCInfo extends SubCommand {

	private final ArrayList<String> aliases;

	public SCInfo() {
		super("info", I18n.format("cmd.info").replaceAll("::", " "), CommandClickOption.suggestCommand);
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
		Minecraft mc = Minecraft.getMinecraft();
		if (args.length != 1)
			Chat.error("/" + mainCommand.getCommandName() + " "+getSubCommandUsage(sender));
		HashMap<String, ArrayList<NetworkPlayerInfo>> players = new HashMap<String, ArrayList<NetworkPlayerInfo>>();
		NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
		List list = ATEEventHandler.field_175252_a.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
		for (int i = 0; i < list.size(); i++) {
			String team;
			NetworkPlayerInfo gf = ((NetworkPlayerInfo) list.get(i));
			if (gf.getPlayerTeam() != null)
				team = gf.getPlayerTeam().getColorPrefix() + gf.getPlayerTeam().getRegisteredName();
			else
				team = I18n.format("cmd.list.noTeam");
			if (gf.getGameProfile().getName().equals(args[0])) {
				long ping = gf.getResponseTime();
				EnumChatFormatting color = (ping < 0 ? EnumChatFormatting.WHITE : (ping < 150 ? EnumChatFormatting.DARK_GREEN : (ping < 300 ? EnumChatFormatting.GREEN : (ping < 600 ? EnumChatFormatting.GOLD : (ping < 1000 ? EnumChatFormatting.RED : EnumChatFormatting.DARK_RED)))));
				Chat.send(new MultiChatComponentText(new IChatComponent[] {
						new ChatComponentText(I18n.format("cmd.info.name"))
								.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)),
						new ChatComponentText(" : ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)),
						new ChatComponentText(gf.getGameProfile().getName())
								.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)),
						new ChatComponentText(" (").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)),
						new ChatComponentText(gf.getResponseTime() + "ms")
								.setChatStyle(new ChatStyle().setColor(color)),
						new ChatComponentText(")").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)) }));
				Chat.send(new MultiChatComponentText(new IChatComponent[] {
						new ChatComponentText(I18n.format("cmd.info.display"))
								.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)),
						new ChatComponentText(" : ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)),
						new ChatComponentText(CommandUtils.getPlayerName(gf))
								.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)) }));
				Chat.send(new MultiChatComponentText(new IChatComponent[] {
						new ChatComponentText(I18n.format("cmd.info.team"))
								.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)),
						new ChatComponentText(" : ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)),
						new ChatComponentText(team)
								.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)) }));
				ArrayList<IChatComponent> elements = new ArrayList<IChatComponent>();
				elements.add(new ChatComponentText("[" + Chat.c_arrow_right + "] ")
						.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)
								.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
										"/atehud tp " + gf.getGameProfile().getName()))
								.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
										new ChatComponentText(I18n.format("chatOption.friend.tp"))
												.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))))));
				if (ModMain.getArray(ModMain.friend).contains(gf.getGameProfile().getName())) {
					elements.add(new ChatComponentText("[x] ").setChatStyle(new ChatStyle()
							.setColor(EnumChatFormatting.RED)
							.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
									"/atehud friend del " + gf.getGameProfile().getName()))
							.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ChatComponentText(I18n.format("chatOption.friend.remove.msg"))
											.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))))));
				} else {
					elements.add(new ChatComponentText("[+] ").setChatStyle(new ChatStyle()
							.setColor(EnumChatFormatting.GREEN)
							.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
									"/atehud friend add " + gf.getGameProfile().getName()))
							.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ChatComponentText(I18n.format("cmd.info.friend.add"))
											.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))))));
				}
				elements.add(new ChatComponentText("[" + Chat.c_pencil + "] ")
						.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)
								.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
										"/msg " + gf.getGameProfile().getName() + " "))
								.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
										new ChatComponentText(I18n.format("chatOption.friend.msg"))
												.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))))));
				Chat.send(new MultiChatComponentText(elements));
				return;
			}
		}
		Chat.error(I18n.format("teleporter.message.error", args[0]));
	}

}
