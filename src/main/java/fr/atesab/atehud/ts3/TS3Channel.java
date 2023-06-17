package fr.atesab.atehud.ts3;

import java.util.HashMap;

import fr.atesab.atehud.ModMain;

public class TS3Channel {
	public String channel_name;
	public String channel_topic;
	public int channel_needed_talk_power;
	public int channel_maxclients;
	public int channel_maxfamilyclients;
	public int channel_order;
	public int parent_id;
	public int channel_id;
	public int channel_codec;
	public int channel_codec_quality;
	public int channel_icon_id;
	public int total_clients;
	public boolean channel_flag_password;
	public boolean channel_flag_default;
	public boolean channel_flag_permanent;
	public boolean channel_flag_semi_permanent;
	public boolean channel_flag_are_subscribed;

	public TS3Channel() {
	}

	public TS3Channel(HashMap<String, String> map) {
		for (String key : map.keySet()) {
			String[] elem = new String[2];
			elem[0] = key;
			elem[1] = map.get(key);
			if (elem.length > 1) {
				if (elem[0].equals("channel_name")) {
					channel_name = TS3Socket.decompileText(elem[1]);
				} else if (elem[0].equals("channel_topic")) {
					channel_topic = TS3Socket.decompileText(elem[1]);
				} else if (elem[0].equals("cid")) {
					channel_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("pid")) {
					parent_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("channel_order")) {
					channel_order = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("channel_needed_talk_power")) {
					channel_needed_talk_power = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("channel_maxclients")) {
					channel_maxclients = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("channel_codec")) {
					channel_codec = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("channel_codec_quality")) {
					channel_codec_quality = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("channel_icon_id")) {
					channel_icon_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("channel_maxfamilyclients")) {
					channel_maxfamilyclients = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("total_clients")) {
					total_clients = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("channel_flag_password")) {
					channel_flag_password = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("channel_flag_default")) {
					channel_flag_default = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("channel_flag_permanent")) {
					channel_flag_permanent = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("channel_flag_semi_permanent")) {
					channel_flag_semi_permanent = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("channel_flag_are_subscribed")) {
					channel_flag_are_subscribed = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				}
			}
		}
	}

	public TS3Channel(String info) {
		String[] args = info.split(" ");
		for (int j = 0; j < args.length; j++) {
			String[] elem = args[j].split("=");
			if (elem.length > 1) {
				if (elem[0].equals("channel_name")) {
					channel_name = TS3Socket.decompileText(elem[1]);
				} else if (elem[0].equals("channel_topic")) {
					channel_topic = TS3Socket.decompileText(elem[1]);
				} else if (elem[0].equals("cid")) {
					channel_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("pid")) {
					parent_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("channel_order")) {
					channel_order = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("channel_needed_talk_power")) {
					channel_needed_talk_power = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("channel_maxclients")) {
					channel_maxclients = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("channel_codec")) {
					channel_codec = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("channel_codec_quality")) {
					channel_codec_quality = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("channel_icon_id")) {
					channel_icon_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("channel_maxfamilyclients")) {
					channel_maxfamilyclients = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("total_clients")) {
					total_clients = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("channel_flag_password")) {
					channel_flag_password = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("channel_flag_default")) {
					channel_flag_default = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("channel_flag_permanent")) {
					channel_flag_permanent = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("channel_flag_semi_permanent")) {
					channel_flag_semi_permanent = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("channel_flag_are_subscribed")) {
					channel_flag_are_subscribed = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				}
			}
		}
	}

	public String getIcon() {
		if (channel_maxclients != -1 && channel_maxclients <= TS3Socket.clients.size())
			return "channel_red_subscribed"; // No space
		else if (channel_flag_password)
			return "channel_yellow_subscribed"; // Password
		else
			return "channel_green_subscribed"; // Normal
	}
}
