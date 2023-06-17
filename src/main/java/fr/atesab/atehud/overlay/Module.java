package fr.atesab.atehud.overlay;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.event.LoadModuleEvent;
import fr.atesab.atehud.superclass.ModuleConfig;
import net.minecraft.client.Minecraft;

public abstract class Module {
	
	public static List<Module> modules = new ArrayList<Module>();
	public static Minecraft mc = Minecraft.getMinecraft();
	
	public static Module getModuleById(String id) {
		for (Module m: modules) if(m.getModuleId().equalsIgnoreCase(id)) return m;
		return null;
	}

	public static void loadModules() {
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new LoadModuleEvent(modules));
		String log = "";
		int module = 0;
		for (int i = 0; i < modules.size(); i++) {
			if(modules.get(i)==null)continue;
			if(i > 0)log+=",";
			log+=modules.get(i).getModuleId();
			module++;
		}
		String s = "";
		if(module>1)s="s";
		System.out.println("Loaded " + module + " module" + s + " : ["+log+"]");
	}
	public String[] getDefaultConfig() {
		ArrayList<ModuleConfig> c = getConfigs(new ArrayList<ModuleConfig>());
		String[] s = new String[c.size()];
		for (int i = 0; i < s.length; i++) {
			s[i] = c.get(i).defaultValue[0];
		}
		return s;
	}
	public Module() {}

	public abstract ArrayList<ModuleConfig> getConfigs(ArrayList<ModuleConfig> list);
	
	public abstract String getDisplayName(String[] args);

	public abstract String getModuleId();

	public abstract int moduleSizeX(String[] args);

	public abstract int moduleSizeY(String[] args);

	public abstract int renderModule(int posX, int posY, String[] args);
}
