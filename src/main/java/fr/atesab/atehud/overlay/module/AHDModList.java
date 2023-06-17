package fr.atesab.atehud.overlay.module;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.Mod;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.Mod.ToggleMod;
import fr.atesab.atehud.overlay.Module;
import fr.atesab.atehud.superclass.ModuleConfig;
import net.minecraft.client.resources.I18n;

public class AHDModList extends Module {
	@Override
	public ArrayList<ModuleConfig> getConfigs(ArrayList<ModuleConfig> list) {
		return list;
	}

	@Override
	public String getDisplayName(String[] args) {
		return I18n.format("atehud.config.overlay.module.mods.name");
	}

	public List<Mod> getEnabledToggleMod(){
		List<Mod> mods = new ArrayList<Mod>();
		for (Mod m: Mod.getMods())if(m.autoGen && m instanceof ToggleMod && ((ToggleMod)m).isEnabled()) mods.add(m);
		return mods;
	}

	@Override
	public String getModuleId() {
		return "mods";
	}

	@Override
	public int moduleSizeX(String[] args) {
		List<Mod> mods = getEnabledToggleMod();
		int sizeX = 0;
		String en = " (" + I18n.format("addServer.resourcePack.enabled") + ")";
		for (Mod m: mods) {
			sizeX = Math.max(sizeX, mc.fontRendererObj.getStringWidth(
					m.getName().endsWith(en) ? m.getName().substring(0, m.getName().length()-en.length()) : m.getName()));
		}
		return sizeX;
	}

	@Override
	public int moduleSizeY(String[] args) {
		return getEnabledToggleMod().size()*(1+mc.fontRendererObj.FONT_HEIGHT);
	}
	@Override
	public int renderModule(int posX, int posY, String[] args) {
		List<Mod> mods = getEnabledToggleMod();
		String en = " (" + I18n.format("addServer.resourcePack.enabled") + ")";
		int sizeY = 0;
		for (Mod m: mods) {
			mc.fontRendererObj.drawString(m.getName().endsWith(en) ? m.getName().substring(0, m.getName().length()-en.length()) : m.getName()
					, posX, posY+sizeY, ModMain.getTextColor().getRGB(), true);
			sizeY+=1+mc.fontRendererObj.FONT_HEIGHT;
		}
		return sizeY;
	}
}
