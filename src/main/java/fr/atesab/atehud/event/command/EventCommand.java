package fr.atesab.atehud.event.command;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.event.LoadEventCommandEvent;

public abstract class EventCommand {
	public static List<EventCommand> eventCommands = new ArrayList<EventCommand>();
	public static void loadEventCommand() {
		LoadEventCommandEvent event = new LoadEventCommandEvent(eventCommands);
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
		String log = "";
		int evCmd = 0;
		for (int i = 0; i < eventCommands.size(); i++) {
			if(eventCommands.get(i)==null)continue;
			if(i > 0)log+=",";
			log+=eventCommands.get(i).getId();
			evCmd++;
		}
		String s = "";
		if(evCmd>1)s="s";
		System.out.println("Loaded " + evCmd + " event command" + s + " : ["+log+"]");
		
	}
	private String id;
	public abstract String getDisplayName();
	public String getId() {
		return id;
	}
	
}
