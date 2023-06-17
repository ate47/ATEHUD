package fr.atesab.atehud.event;

import java.util.List;

import fr.atesab.atehud.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LoadModEvent extends Event {
	private List<Mod> mods;

	public LoadModEvent(List<Mod> mods) {
		this.mods = mods;
	}
	
	public List<Mod> getMods() {
		return mods;
	}
	
	public boolean isCancelable() {
		return false;
	}

	public void registerMod(Mod mod) {
		if(mod!=null)mods.add(mod);
	}
}
