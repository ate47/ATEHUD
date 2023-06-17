package fr.atesab.atehud.superclass;

import fr.atesab.atehud.utils.ConnectionUtils;

public class Account {
	public String username;
	public int type;
	public String password;

	public Account(String username, int type, String password) {
		this.username = username;
		this.type = type;
		this.password = password;
	}

	public String connect() {
		if (type == 1) {
			return ConnectionUtils.changeName(username);
		} else if (type == 0) {
			return ConnectionUtils.login(username, password);
		} else if (type == 2) {
			return ConnectionUtils.stealSession(username);
		}
		return "BadAccountType: " + type;
	}
}
