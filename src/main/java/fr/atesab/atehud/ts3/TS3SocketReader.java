package fr.atesab.atehud.ts3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.event.TeamspeakEvent;
import fr.atesab.atehud.event.TeamspeakEvent.MessageReceivedEvent;
import fr.atesab.atehud.event.TeamspeakEvent.MessageReceivedEvent.MessageType;
import fr.atesab.atehud.event.TeamspeakEvent.SocketReceiveEvent.ReceiveType;
import net.minecraft.client.resources.I18n;

public class TS3SocketReader extends Thread {
	private String lastEvent = "";
	private final Queue<String> receiveQueue;
	private final BufferedReader in;
	public TS3SocketReader() throws UnsupportedEncodingException, IOException {
		receiveQueue = TS3Socket.getReceiveQueue();
		this.in = new BufferedReader(new InputStreamReader(TS3Socket.getSocket().getInputStream(), "UTF-8"));
	}
	public TS3Events getEventByName(String name) {
		for (TS3Events ev: TS3Events.class.getEnumConstants())if(ev.name.equalsIgnoreCase(name))return ev;
		return null;
	}
	public ReceiveType getMessageTypeByLine(String line) {
		for (ReceiveType tp: ReceiveType.class.getEnumConstants())if(tp.toString().startsWith(line))return tp;
		return TeamspeakEvent.SocketReceiveEvent.ReceiveType.other;
	}
	private boolean isDuplicate(String eventMessage) {
		if (!(eventMessage.startsWith("notifyclientmoved") || eventMessage.startsWith("notifycliententerview")
				|| eventMessage.startsWith("notifyclientleftview"))) {
			return false;
		}
		if (eventMessage.equals(lastEvent)) {
			lastEvent = "";
			return true;
		}
		lastEvent = eventMessage;
		return false;
	}
	public String removeNotifyPrefix(String line) {
		String[] lineDecompiled = line.split(" ");
		line = lineDecompiled[2];
		for (int i = 3; i < lineDecompiled.length; i++) {
			line += " " + lineDecompiled[i];
		}
		return line;
	}
	public void run() {
		while (!isInterrupted()) {
			String line;
			try {
				line = in.readLine();
			} catch (IOException io) {
				if (!isInterrupted()) {
					System.out.println("Connection error occurred.");
				}
				break;
			}
			if(line==null) {
				System.out.println("Connection closed by the server.");
				break;
			} else if (line.isEmpty()) {
				continue;
			}
			TeamspeakEvent.SocketReceiveEvent.ReceiveType messageType = getMessageTypeByLine(line);
			ArrayList<Map<String, String>> map = TS3Socket.createMap(line);
			if (isDuplicate(line))
				continue;
			TeamspeakEvent.SocketReceiveEvent event = new TeamspeakEvent.SocketReceiveEvent(messageType, map);
			if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
				continue;
			event.map = map;
			switch (messageType) {
			case notify:
				final String args[] = line.split(" ", 2);
				if (ModMain.TS3LogQuery)
					System.out.println("TS3"+args[0]+"> " + line);
				switch(getEventByName(args[0])) {
				case notifytextmessage: 
					if (ModMain.TS3Text) { // textMessageEvent
						String invokername = "", msg = "", from = "channel";
						TeamspeakEvent.MessageReceivedEvent.MessageType textMessageType = MessageType.channel;
						try {
							String targetmode = map.get(0).get("targetmode");
							if (targetmode.equals("3")) {
								from = "server";
								textMessageType = MessageType.server;
							} else if (targetmode.equals("1")) {
								from = "message";
								textMessageType = MessageType.message;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							invokername = map.get(0).get("invokername");
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							msg = map.get(0).get("msg");
						} catch (Exception e) {
							e.printStackTrace();
						}
						TeamspeakEvent.MessageReceivedEvent msgEvent = new MessageReceivedEvent(textMessageType,
								invokername, msg);
						if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(msgEvent))
							continue;
						invokername = msgEvent.invokerName;
						msg = msgEvent.msg;
						if (msg == null)
							msg = "";
						else
							msg = ": " + msg;
						Chat.teamspeak(I18n.format("ts3.text." + from) + "\u00a7r> \"" + invokername + "\"" + msg);
					} break; 
				case notifyclientpoke: 
					if (ModMain.TS3Poke) { // pokeEvent
						String invokername = "", msg = "";
						try {
							invokername = map.get(0).get("invokername");
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							msg = map.get(0).get("msg");
						} catch (Exception e) {
							e.printStackTrace();
						}
						TeamspeakEvent.MessageReceivedEvent msgEvent = new MessageReceivedEvent(MessageType.poke,
								invokername, msg);
						if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(msgEvent))
							continue;
						invokername = msgEvent.invokerName;
						msg = msgEvent.msg;
						if (msg == null)
							msg = "";
						else
							msg = ": " + msg;
						Chat.teamspeak(I18n.format("ts3.poke", "\"" + invokername + "\"\u00a7r", msg));
					}
					break; 
				case notifyservergrouplist: // ServerGroupListEvent
					System.out.println(TS3Socket.defineClientListPhase);
					try {
						TS3Socket.allServerGroup.clear();
						line = removeNotifyPrefix(line);
						String[] lined = line.split("\\|");
						for (int i = 0; i < lined.length; i++) {
							TS3ServerGroup serverGroup = new TS3ServerGroup(lined[i]);
							TS3Socket.allServerGroup.add(serverGroup);
						}
						TS3Socket.sendCommand("channelgrouplist");
					} catch (IOException e) {
						e.printStackTrace();
					}
				break; 
				case notifychannelgrouplist: // ChannelGroupListEvent
					TS3Socket.allChannelGroup.clear();
					line = removeNotifyPrefix(line);
					String[] lined = line.split("[|]");
					for (int i = 0; i < lined.length; i++) {
						TS3ChannelGroup channelGroup = new TS3ChannelGroup(lined[i]);
						TS3Socket.allChannelGroup.add(channelGroup);
					}
					TS3Socket.needReDefineGroups = false;
					break;
				case notifycurrentserverconnectionchanged:
					TS3Socket.schandlerid = Integer.valueOf((String)map.get(0).getOrDefault("schandlerid", String.valueOf(TS3Socket.schandlerid)));
					TS3Socket.defineClientListPhase = 0;
					TS3Socket.needReDefineGroups = true;
					break;
				default:
					break;
				}
				break;
			case error:
				if (ModMain.TS3LogQuery)
					System.out.println("TS3error> " + line);
				break;
			default:
				if (ModMain.TS3LogQuery)
					System.out.println("TS3msg> " + line); // ok
				if (line.startsWith("schandlerid") && TS3Socket.defineClientListPhase == 2) {
					String[] args1 = line.split("=");
					if (args1.length > 1)
						try {
							int a = Integer.valueOf(args1[1]);
							if (TS3Socket.schandlerid != a) {
								TS3Socket.needReDefineGroups = true;
							}
							TS3Socket.schandlerid = a;
							TS3Socket.defineClientListPhase = 4;
							TS3Socket.sendCommand("use " + TS3Socket.schandlerid);
						} catch (Exception e) {
							System.out.println("NaN: \"" + args1[1] + "\"");
						}
				} else if (line.startsWith("selected schandlerid") && TS3Socket.defineClientListPhase == 4) {
					String[] args1 = line.split("=");
					if (args1.length > 1)
						try {
							TS3Socket.defineClientListPhase = 6;
							TS3Socket.sendCommand("whoami");
						} catch (Exception e) {
							e.printStackTrace();
						}
				} else if (line.startsWith("clid") && TS3Socket.defineClientListPhase == 6) {
					if (map.size() > 0) {
						try {
							TS3Socket.currentCID = Integer
									.valueOf(map.get(0).getOrDefault("cid", String.valueOf(TS3Socket.currentCID)));
							TS3Socket.currentCLID = Integer
									.valueOf(map.get(0).getOrDefault("clid", String.valueOf(TS3Socket.currentCLID)));
							TS3Socket.defineClientListPhase = 8;
							TS3Socket.sendCommand("clientlist -uid -away -voice -groups -icon -country");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (line.startsWith("clid") && TS3Socket.defineClientListPhase == 8) {
					try {
						TS3Socket.clients.clear();
						TS3Socket.allClients.clear();
						String[] lined = line.split("\\|");
						for (int i = 0; i < lined.length; i++) {
							TS3Client client = new TS3Client(lined[i]);
							if (client.channel_id == TS3Socket.currentCID)
								TS3Socket.clients.add(client);
							if (client.client_id == TS3Socket.currentCLID)
								TS3Socket.currentClient = client;
							TS3Socket.allClients.add(client);
						}
						TS3Socket.clients.sort(new Comparator<TS3Client>() {
							public int compare(TS3Client o1, TS3Client o2) {
								return o1.client_nickname.compareTo(o2.client_nickname);
							}
						});
						TS3Socket.clients.sort(new Comparator<TS3Client>() {
							public int compare(TS3Client o1, TS3Client o2) {
								return o1.compareTo(o2);
							}
						});
						TS3Socket.sendCommand("channellist -topic -flags -voice -icon -limits");
						TS3Socket.defineClientListPhase = 10;
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (line.startsWith("cid") && TS3Socket.defineClientListPhase == 10) {
					try {
						TS3Socket.allChannel.clear();
						String[] lined = line.split("\\|");
						for (int i = 0; i < lined.length; i++) {
							TS3Channel channel = new TS3Channel(lined[i]);
							if (channel.channel_id == TS3Socket.currentCID)
								TS3Socket.currentChannel = channel;
							TS3Socket.allChannel.add(channel);
						}
						TS3Socket.defineClientListPhase = 0;
						TS3Socket.lastDefineClient = System.currentTimeMillis();
						if (TS3Socket.needReDefineGroups) {
							TS3Socket.sendCommand("servergrouplist");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;
			}
		}

		try {
			in.close();
		} catch (IOException ignored) {
			// Ignore
		}

		if (!isInterrupted()) {
			System.out.println("SocketReader has stopped!");
		}
	}
}
