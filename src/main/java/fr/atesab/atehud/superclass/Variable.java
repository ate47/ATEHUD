package fr.atesab.atehud.superclass;

import java.util.HashMap;
import com.google.gson.GsonBuilder;

public class Variable {
	public static enum Types {
		String("String"), Integer("Integer"), Boolean("Boolean"), Float("Float");

		public String name;

		Types(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
	}
	public String name;
	public Types type;

	public String value;

	public Variable(String name, Types type) {
		this.name = name;
		this.type = type;
	}

	public String getData() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("type", type.toString());
		map.put("value", value);

		return new GsonBuilder().create().toJson(map);
	}
}
