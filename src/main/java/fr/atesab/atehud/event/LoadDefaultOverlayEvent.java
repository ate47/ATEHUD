package fr.atesab.atehud.event;

import java.util.List;

import fr.atesab.atehud.overlay.OverlayDefaultConfig;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LoadDefaultOverlayEvent extends Event {
	private List<OverlayDefaultConfig> defaultConfigs;

	public LoadDefaultOverlayEvent(List<OverlayDefaultConfig> defaultConfigs) {
		this.defaultConfigs = defaultConfigs;
	}
	
	public List<OverlayDefaultConfig> getDefaultConfig() {
		return defaultConfigs;
	}
	
	public boolean isCancelable() {
		return false;
	}

	public void registerDefaultConfig(OverlayDefaultConfig defaultConfig) {
		if(defaultConfig!=null) defaultConfigs.add(defaultConfig);
	}
}
