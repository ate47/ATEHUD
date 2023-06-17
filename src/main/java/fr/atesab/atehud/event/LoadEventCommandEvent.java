package fr.atesab.atehud.event;

import java.util.List;

import fr.atesab.atehud.event.command.EventCommand;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LoadEventCommandEvent extends Event {
	private List<EventCommand> evCmd;
	public LoadEventCommandEvent(List<EventCommand> evCmd) {
		this.evCmd = evCmd;
	}
	public List<EventCommand> getEventCommandList() {
		return this.evCmd;
	}
	public boolean isCancelable() {
		return false;
	}
	public void registerEventCommand(EventCommand evCmd) {
		if(!this.evCmd.contains(evCmd)) this.evCmd.add(evCmd);
	}
	
}
