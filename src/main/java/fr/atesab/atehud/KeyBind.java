package fr.atesab.atehud;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class KeyBind {
	public int key = 0;
	public String command = "";
	
	public KeyBind(int key, String command) {
		this.key = key;
		this.command = command;
	}
	public KeyBind(String save) {
		Gson gson = new GsonBuilder().create();
		Map<String,Object> hm = gson.fromJson(save, Map.class);
		this.key = ((Double)hm.getOrDefault("key", key)).intValue();
		this.command = (String)hm.getOrDefault("command", command);
	}
	public String getSaveElement() {
		Gson gson = new GsonBuilder().create();
		Map<String,Object> hm = new HashMap<String, Object>();
		hm.put("key", key);
		hm.put("command", command);
		return gson.toJson(hm);
	}
}
