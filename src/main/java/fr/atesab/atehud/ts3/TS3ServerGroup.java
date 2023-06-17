package fr.atesab.atehud.ts3;

import java.util.HashMap;

import fr.atesab.atehud.ts3.TS3Group.GroupType;
import fr.atesab.atehud.ts3.TS3Group.NameMode;

public class TS3ServerGroup extends TS3Group {

	public TS3ServerGroup() {
	}

	public TS3ServerGroup(HashMap<String, String> map) {
		for (String key : map.keySet()) {
			String[] elem = new String[2];
			elem[0] = key;
			elem[1] = map.get(key);
			if (elem.length > 1) {
				if (elem[0].equals("name")) {
					name = TS3Socket.decompileText(elem[1]);
				} else if (elem[0].equals("sgid")) {
					group_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("icon_id")) {
					icon_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("type")) {
					type = GroupType.getGroupType(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("savedb")) {
					savedb = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("sortid")) {
					sortid = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("namemode")) {
					namemode = NameMode.getNameMode(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("n_modifyp")) {
					n_modifyp = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("n_member_addp")) {
					n_member_addp = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("n_member_removep")) {
					n_member_removep = Integer.valueOf(elem[1]);
				}
			}
		}
	}

	public TS3ServerGroup(String info) {
		String[] args = info.split(" ");
		for (int j = 0; j < args.length; j++) {
			String[] elem = args[j].split("=");
			if (elem.length > 1) {
				if (elem[0].equals("name")) {
					name = TS3Socket.decompileText(elem[1]);
				} else if (elem[0].equals("sgid")) {
					group_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("icon_id")) {
					icon_id = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("type")) {
					type = GroupType.getGroupType(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("savedb")) {
					savedb = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("sortid")) {
					sortid = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("namemode")) {
					namemode = NameMode.getNameMode(Integer.valueOf(elem[1]));
				} else if (elem[0].equals("n_modifyp")) {
					n_modifyp = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("n_member_addp")) {
					n_member_addp = Integer.valueOf(elem[1]);
				} else if (elem[0].equals("n_member_removep")) {
					n_member_removep = Integer.valueOf(elem[1]);
				}
			}
		}
	}
}
