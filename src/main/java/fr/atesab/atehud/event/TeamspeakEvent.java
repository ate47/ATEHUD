package fr.atesab.atehud.event;

import java.util.ArrayList;
import java.util.Map;

import net.minecraftforge.fml.common.eventhandler.Event;

public class TeamspeakEvent extends Event {
	public static class MessageReceivedEvent extends Event {
		public static enum MessageType {
			channel, poke, server, message;
		}
		public MessageType target;
		public String invokerName;

		public String msg;

		public MessageReceivedEvent(MessageType target, String invokerName, String msg) {
			this.target = target;
			this.msg = msg;
			this.invokerName = invokerName;
		}

		public boolean isCancelable() {
			return true;
		}
	}

	public static class SocketReceiveEvent extends Event {
		public static enum ReceiveType {
			notify("notify"), error("error"), other("other");
			String type;
			private ReceiveType(String type) {
				this.type=type;
			}
			public String toString() {
				return type;
			}
		}
		public ReceiveType type;

		public ArrayList<Map<String, String>> map;

		public SocketReceiveEvent(ReceiveType type, ArrayList<Map<String, String>> map) {
			this.type = type;
			this.map = map;
		}

		public boolean isCancelable() {
			return true;
		}
	}
}
