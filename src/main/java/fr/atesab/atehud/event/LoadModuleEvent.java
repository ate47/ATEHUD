package fr.atesab.atehud.event;

import java.util.List;

import fr.atesab.atehud.overlay.Module;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LoadModuleEvent extends Event {
	private List<Module> modules;

	public LoadModuleEvent(List<Module> modules) {
		this.modules = modules;
	}
	
	public List<Module> getModules() {
		return modules;
	}
	
	public boolean isCancelable() {
		return false;
	}

	public void registerModule(Module module) {
		if(module!=null) modules.add(module);
	}
}
