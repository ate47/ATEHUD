package fr.atesab.atehud.event;

import java.util.ArrayList;

import fr.atesab.atehud.utils.MultiChatComponentText.FriendElement;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Event.HasResult;

public class FriendElementEvent extends Event {
	public ArrayList<FriendElement> elements = new ArrayList<FriendElement>();
	public String username;
	public boolean showOnline;
	public EnumChatFormatting chatColor;

	public FriendElementEvent(ArrayList<FriendElement> elements, String username, boolean showOnline,
			EnumChatFormatting chatColor) {
		this.elements = elements;
		this.username = username;
		this.showOnline = showOnline;
		this.chatColor = chatColor;
	}

	public boolean isCancelable() {
		return false;
	}
}