package fr.atesab.atehud.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.command.SubCommand;
import fr.atesab.atehud.event.FriendElementEvent;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MultiChatComponentText implements IChatComponent {

	public static class FriendElement {
		public char display;
		public EnumChatFormatting color;
		public String command;
		public IChatComponent hoverElement;
		public ClickEvent.Action action;

		public FriendElement(char display, EnumChatFormatting color, String command, ClickEvent.Action action,
				IChatComponent hoverElement) {
			this.display = display;
			this.color = color;
			this.command = command;
			this.hoverElement = hoverElement;
			this.action = action;
		}

		public IChatComponent getElement() {
			return new ChatComponentText("[" + display + "] ")
					.setChatStyle(new ChatStyle().setColor(color).setChatClickEvent(new ClickEvent(action, command))
							.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverElement)));
		}
	}

	public static IChatComponent getCommandPage(String commandName, int page, int maxPage) {
		List<IChatComponent> lst = new ArrayList<IChatComponent>();
		if (page != 1)
			lst.add(new ChatComponentText("<--").setChatStyle(new ChatStyle()
					.setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/" + commandName + " " + (int) (page - 1)))
					.setColor(EnumChatFormatting.RED)));
		else
			lst.add(new ChatComponentText("<--").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_GRAY)));
		lst.add(new ChatComponentText(" | ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
		if (page != maxPage)
			lst.add(new ChatComponentText("-->").setChatStyle(new ChatStyle()
					.setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/" + commandName + " " + (int) (page + 1)))
					.setColor(EnumChatFormatting.RED)));
		else
			lst.add(new ChatComponentText("-->").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_GRAY)));
		return new MultiChatComponentText(lst);
	}

	public static IChatComponent getFriendElements(String prefix, String username, boolean showOnline) {
		return getFriendElements(prefix, username, showOnline, EnumChatFormatting.WHITE);
	}

	public static IChatComponent getFriendElements(String prefix, String username, boolean showOnline,
			EnumChatFormatting chatColor) {
		ChatStyle cs = new ChatStyle();
		String connect = "";
		if (ATEEventHandler.friendConnected.contains(username)) {
			connect = Chat.YELLOW + I18n.format("chatOption.friend.connect");
			if (showOnline)
				prefix += Chat.YELLOW;
		} else {
			connect = Chat.RED + I18n.format("chatOption.friend.disconnect");
			if (showOnline)
				prefix += Chat.RED;
		}
		ArrayList<FriendElement> elements = new ArrayList<FriendElement>();
		elements.add(new FriendElement(Chat.c_arrow_right, EnumChatFormatting.YELLOW, "/atehud tp " + username,
				ClickEvent.Action.RUN_COMMAND, new ChatComponentText(I18n.format("chatOption.friend.tp"))
						.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))));
		elements.add(new FriendElement('x', EnumChatFormatting.RED, "/atehud friend del " + username,
				ClickEvent.Action.RUN_COMMAND, new ChatComponentText(I18n.format("chatOption.friend.remove.msg"))
						.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))));
		elements.add(new FriendElement(Chat.c_pencil, EnumChatFormatting.AQUA, "/msg " + username + " ",
				ClickEvent.Action.SUGGEST_COMMAND, new ChatComponentText(I18n.format("chatOption.friend.msg"))
						.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))));
		FriendElementEvent event = new FriendElementEvent(elements, username, showOnline, chatColor);
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
		elements = event.elements;
		ArrayList<IChatComponent> chatElements = new ArrayList<IChatComponent>();
		for (int i = 0; i < elements.size(); i++) {
			chatElements.add(elements.get(i).getElement());
		}
		chatElements.add(new ChatComponentText(chatColor + prefix + " " + username + " ")
				.setChatStyle(new ChatStyle().setColor(chatColor).setChatHoverEvent(
						new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(connect)))));
		return new MultiChatComponentText(chatElements);
	}

	public static IChatComponent getHelp(String commandName, SubCommand cmd, ICommandSender sender) {
		ArrayList<IChatComponent> lst = new ArrayList<IChatComponent>();
		ChatStyle cs = new ChatStyle().setColor(EnumChatFormatting.GOLD);
		switch (cmd.getClickOption()) {
		case suggestCommand:
			cs.setChatClickEvent(
					new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + commandName + " " + cmd.getName() + " "))
					.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new ChatComponentText(I18n.format("cmd.help.click"))
									.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))));
			break;
		default:
			cs.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + commandName + " " + cmd.getName()))
					.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new ChatComponentText(I18n.format("cmd.help.do"))
									.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))));
			break;
		}
		lst.add(new ChatComponentText("/" + commandName + " " + cmd.getSubCommandUsage(sender)).setChatStyle(cs));
		lst.add(new ChatComponentText(" : ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
		lst.add(new ChatComponentText(cmd.getDescription())
				.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)));
		return new MultiChatComponentText(lst);
	}

	public List<IChatComponent> elements = new ArrayList<IChatComponent>();

	public MultiChatComponentText(List<IChatComponent> elements) {
		this.elements = elements;
	}

	public MultiChatComponentText(IChatComponent[] elements) {
		this.elements.clear();
		for (int i = 0; i < elements.length; i++) {
			this.elements.add(elements[i]);
		}
	}

	@Override
	public IChatComponent appendSibling(IChatComponent component) {
		elements.add(component);
		return this;
	}

	@Override
	public IChatComponent appendText(String text) {
		this.elements.add(new ChatComponentText(text));
		return this;
	}

	@Override
	public IChatComponent createCopy() {
		return this;
	}

	@Override
	public ChatStyle getChatStyle() {
		IChatComponent elem = new ChatComponentText("");
		for (int i = 0; i < elements.size(); i++) {
			elem.appendSibling(elements.get(i));
		}
		return elem.getChatStyle();
	}

	@Override
	public String getFormattedText() {
		String out = "";
		for (Iterator iterator = iterator(); iterator.hasNext();) {
			IChatComponent chatComponent = (IChatComponent) iterator.next();
			out += chatComponent.getFormattedText();
		}
		return out;
	}

	@Override
	public List<IChatComponent> getSiblings() {
		return elements;
	}

	@Override
	public String getUnformattedText() {
		String out = "";
		for (Iterator iterator = iterator(); iterator.hasNext();) {
			IChatComponent chatComponent = (IChatComponent) iterator.next();
			out += chatComponent.getUnformattedText();
		}
		return out;
	}

	@Override
	public String getUnformattedTextForChat() {
		String out = "";
		for (Iterator iterator = iterator(); iterator.hasNext();) {
			IChatComponent chatComponent = (IChatComponent) iterator.next();
			out += chatComponent.getUnformattedTextForChat();
		}
		return out;
	}

	public Iterator<IChatComponent> iterator() {
		return elements.iterator();
	}

	@Override
	public IChatComponent setChatStyle(ChatStyle style) {
		return this;
	}

}
