package fr.atesab.atehud.ts3;

public class TS3Group implements Comparable<TS3Group> {
	public static enum GroupType {
		normal, template, query;
		public static GroupType getGroupType(int type) {
			switch (type) {
			case 0:
				return template;
			case 2:
				return query;
			default:
				return normal;
			}
		}
	}
	public static enum NameMode {
		none, before, behind;
		public static NameMode getNameMode(int nameMode) {
			switch (nameMode) {
			case 1:
				return before;
			case 2:
				return behind;
			default:
				return none;
			}
		}
	}
	public String name;
	public GroupType type;
	public NameMode namemode;
	public int group_id;
	public int icon_id;
	public int savedb;
	public int sortid;
	public int n_modifyp;

	public int n_member_addp;

	public int n_member_removep;

	@Override
	public int compareTo(TS3Group group) {
		return getSortId() - group.getSortId();
	}

	public int getSortId() {
		if (sortid > 0)
			return sortid;
		else
			return group_id;
	}

	@Override
	public String toString() {
		return name + "(" + group_id + ")";
	}

}
