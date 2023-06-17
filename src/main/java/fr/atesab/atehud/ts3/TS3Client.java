package fr.atesab.atehud.ts3;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.ts3.TS3Group.NameMode;

public class TS3Client {
	public String client_nickname;
	public String client_away_message;
	public String client_country;
	public String client_unique_identifier;
	public int client_type;
	public int client_database_id;
	public int channel_id;
	public int client_id;
	public int client_talk_power;
	public int client_channel_group_id;
	public int client_icon_id;
	public int[] client_servergroups;
	public boolean client_flag_talking;
	public boolean client_input_muted;
	public boolean client_output_muted;
	public boolean client_input_hardware;
	public boolean client_output_hardware;
	public boolean client_is_talker;
	public boolean client_is_priority_speaker;
	public boolean client_is_recording;
	public boolean client_is_channel_commander;
	public boolean client_is_muted;
	public boolean client_away;

	public TS3Client() {
	}

	public TS3Client(HashMap<String, String> map) {
		for (String key : map.keySet()) {
			String[] elem = new String[2];
			elem[0] = key;
			elem[1] = map.get(key);
			if (elem.length > 1) {
				if (elem[0].equals("client_nickname")) {
					client_nickname = TS3Socket.decompileText(elem[1]);
				} else if (elem[0].equals("client_away_message")) {
					client_away_message = TS3Socket.decompileText(elem[1]);
				} else if (elem[0].equals("client_country")) {
					client_country = TS3Socket.decompileText(elem[1]);
				} else if (elem[0].equals("client_unique_identifier")) {
					client_unique_identifier = TS3Socket.decompileText(elem[1]);
				} else if (elem[0].equals("client_type")) {
					client_type = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("client_database_id")) {
					client_database_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("cid")) {
					channel_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("clid")) {
					client_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("client_icon_id")) {
					client_icon_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("client_channel_group_id")) {
					client_channel_group_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("client_talk_power")) {
					client_talk_power = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("client_flag_talking")) {
					client_flag_talking = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_input_muted")) {
					client_input_muted = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_output_muted")) {
					client_output_muted = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_input_hardware")) {
					client_input_hardware = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_output_hardware")) {
					client_output_hardware = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_is_talker")) {
					client_is_talker = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_is_priority_speaker")) {
					client_is_priority_speaker = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_is_recording")) {
					client_is_recording = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_is_channel_commander")) {
					client_is_channel_commander = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_is_muted")) {
					client_is_muted = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_away")) {
					client_away = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_servergroups")) {
					String[] grps = elem[1].split(",");
					int[] grps2 = new int[grps.length];
					for (int i = 0; i < grps.length; i++)
						grps2[i] = Integer.valueOf(grps2[i]);
					client_servergroups = grps2;
				}
			}
		}
	}

	public TS3Client(String info) {
		String[] args = info.split(" ");
		for (int j = 0; j < args.length; j++) {
			String[] elem = args[j].split("=");
			if (elem.length > 1) {
				if (elem[0].equals("client_nickname")) {
					client_nickname = TS3Socket.decompileText(elem[1]);
				} else if (elem[0].equals("client_away_message")) {
					client_away_message = TS3Socket.decompileText(elem[1]);
				} else if (elem[0].equals("client_country")) {
					client_country = TS3Socket.decompileText(elem[1]);
				} else if (elem[0].equals("client_unique_identifier")) {
					client_unique_identifier = TS3Socket.decompileText(elem[1]);
				} else if (elem[0].equals("client_type")) {
					client_type = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("client_database_id")) {
					client_database_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("cid")) {
					channel_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("clid")) {
					client_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("client_icon_id")) {
					client_icon_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("client_channel_group_id")) {
					client_channel_group_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("client_talk_power")) {
					client_talk_power = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("client_flag_talking")) {
					client_flag_talking = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_input_muted")) {
					client_input_muted = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_output_muted")) {
					client_output_muted = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_input_hardware")) {
					client_input_hardware = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_output_hardware")) {
					client_output_hardware = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_is_talker")) {
					client_is_talker = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_is_priority_speaker")) {
					client_is_priority_speaker = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_is_recording")) {
					client_is_recording = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_is_channel_commander")) {
					client_is_channel_commander = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_is_muted")) {
					client_is_muted = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_away")) {
					client_away = ModMain.intToBoolean(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("client_servergroups")) {
					String[] grps = elem[1].split(",");
					int[] grps2 = new int[grps.length];
					for (int i = 0; i < grps.length; i++)
						grps2[i] = Integer.valueOf(grps[i]);
					client_servergroups = grps2;
				}
			}
		}
	}

	public int compareTo(TS3Client client) {
		return client.client_talk_power - client_talk_power;
	}

	public String getIcon() {
		if (client_type == 1)
			return "connect"; // ServerQueryClient
		else if (client_is_muted)
			return "input_muted_local"; // LocalMuted
		else if (client_away)
			return "away"; // AFK
		else if (!client_output_hardware) {
			if (client_is_channel_commander && client_flag_talking)
				return "player_commander_on";
			else if (client_flag_talking)
				return "player_on";
			return "hardware_output_muted"; // OutputMuted (HardWare)
		} else if (client_output_muted)
			return "output_muted"; // OutputMuted
		else if (!client_input_hardware)
			return "hardware_input_muted"; // InputMuted (HardWare)
		else if (client_input_muted)
			return "input_muted"; // InputMuted
		else if (client_is_channel_commander) { // ChannelCommander ?
			if (client_flag_talking)
				return "player_commander_on"; // -> CC_on
			else
				return "player_commander_off"; // -> CC_off
		} else if (client_flag_talking)
			return "player_on"; // on
		else
			return "player_off"; // off
	}

	public String getName() {
		ArrayList<TS3Group> clientsGroups = new ArrayList<TS3Group>();
		for (int i = 0; i < TS3Socket.allServerGroup.size(); i++) {
			TS3Group group = TS3Socket.allServerGroup.get(i);
			for (int j = 0; j < client_servergroups.length; j++) {
				if (group.group_id == client_servergroups[j] && group.namemode != TS3Group.NameMode.none)
					clientsGroups.add(group);
			}
		}
		for (int i = 0; i < TS3Socket.allChannelGroup.size(); i++) {
			TS3Group group = TS3Socket.allChannelGroup.get(i);
			if (group.group_id == client_channel_group_id && group.namemode != TS3Group.NameMode.none)
				clientsGroups.add(group);
		}
		clientsGroups.sort(new Comparator<TS3Group>() {
			public int compare(TS3Group o1, TS3Group o2) {
				return o1.name.compareTo(o2.name);
			}
		});
		clientsGroups.sort(new Comparator<TS3Group>() {
			public int compare(TS3Group o1, TS3Group o2) {
				return o1.compareTo(o2);
			}
		});
		String prefix = "";
		String sufix = " ";
		for (int i = 0; i < clientsGroups.size(); i++) {
			if (clientsGroups.get(i).namemode == NameMode.before)
				prefix += "[" + clientsGroups.get(i).name + "]";
			else
				sufix += "[" + clientsGroups.get(i).name + "]";
		}
		if (!prefix.isEmpty())
			prefix += " ";
		if (this.equals(TS3Socket.currentClient))
			prefix = Chat.BOLD + prefix;
		if (client_away && client_away_message != null && !client_away_message.isEmpty())
			return client_nickname + " [" + ModMain.getLowString(client_away_message, ModMain.ts3NameMaxLength) + "]";
		return ModMain.getLowString(prefix + client_nickname + sufix,
				client_nickname.length() + 3 + ModMain.ts3NameMaxLength);
	}

	@Override
	public String toString() {
		return client_nickname + "(" + client_id + ")";
	}
}
