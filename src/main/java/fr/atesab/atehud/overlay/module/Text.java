package fr.atesab.atehud.overlay.module;

import java.awt.Color;
import java.util.ArrayList;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.overlay.Module;
import fr.atesab.atehud.overlay.Overlay.position;
import fr.atesab.atehud.superclass.ModuleConfig;
import fr.atesab.atehud.utils.CommandUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;

public class Text extends Module {
	public Text() {
	}

	public ArrayList<ModuleConfig> getConfigs(ArrayList<ModuleConfig> array) {
		array.add(new fr.atesab.atehud.superclass.ModuleConfig("text", ModuleConfig.ModuleType.STRING, ""));
		array.add(new fr.atesab.atehud.superclass.ModuleConfig("text.randomColor", ModuleConfig.ModuleType.BOOLEAN, String.valueOf(false)));
		return array;
	}

	public String getDisplayName(String[] args) {
		String s = CommandUtils.getOptionText(args[0]);
		s = s.replaceAll("&", "\u00a7") + "";
		return s;
	}

	public String getModuleId() {
		return "text";
	}

	public int moduleSizeX(String[] args) {
		String text = "";
		if(args.length==2) {
			return mc.fontRendererObj.getStringWidth(args[0]);
		} else return 0;
	}

	public int moduleSizeY(String[] args) {
		return mc.fontRendererObj.FONT_HEIGHT;
	}

	public int renderModule(int posX, int posY, String[] args) {
		boolean randomColor = false;
		String text = "";
		if(args.length==2) {
			text = args[0];
			try {randomColor = Boolean.valueOf(args[1]);} catch (Exception e) {}
		} else return 0;
		if (!text.isEmpty()) {
			String s = CommandUtils.getOptionText(text);
			s = s.replaceAll("&", "\u00a7") + "";
			mc.fontRendererObj.drawString(s, posX, posY,
					randomColor ? ModMain.getRandomColorByTime(0.25F).getRGB() : ModMain.getTextColor().getRGB(),
					true);
		}
		return mc.fontRendererObj.FONT_HEIGHT;
	}

	public String[] getConfigs(String text) {
		String[] s = getDefaultConfig();
		s[0] = text;
		return s;
	}
}