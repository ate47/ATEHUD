package fr.atesab.atehud.superclass;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.GsonBuilder;

public class Function {
	public Variable[] variables;
	public Action[] actions;
	public String name;

	public Function(Variable[] variables, Action[] actions, String name) {
		this.variables = variables;
		this.actions = actions;
		this.name = name;
	}

	public String getData() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		ArrayList<String> vars = new ArrayList<String>();
		ArrayList<String> act = new ArrayList<String>();
		for (int i = 0; i < variables.length; i++) {
			vars.add(variables[i].getData());
		}
		for (int i = 0; i < actions.length; i++) {
			act.add(actions[i].getData());
		}
		map.put("variables", vars);
		map.put("actions", act);

		return new GsonBuilder().create().toJson(map);
	}
}
