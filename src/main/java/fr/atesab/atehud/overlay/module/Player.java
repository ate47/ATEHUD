package fr.atesab.atehud.overlay.module;

import java.util.ArrayList;

import fr.atesab.atehud.overlay.Module;
import fr.atesab.atehud.overlay.Overlay.position;
import fr.atesab.atehud.superclass.ModuleConfig;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class Player extends Module {
	public ArrayList<ModuleConfig> getConfigs(ArrayList<ModuleConfig> list) {
		return list;
	}

	public String getDisplayName(String[] args) {
		return I18n.format("atehud.config.overlay.module."+getModuleId()+".name");
	}

	public String getModuleId() {
		return "player";
	}

	public int moduleSizeX(String[] args) {
		return 50;
	}

	public int moduleSizeY(String[] args) {
		return 100;
	}

	public int renderModule(int posX, int posY, String[] args) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int width = new ScaledResolution(mc).getScaledWidth();
		int moduleWidth = moduleSizeX(args);
		int moduleHeight = moduleSizeY(args);
		int angle = 0;
		if (posX < width / 3)
			angle = -50;
		if (posX > width * 2 / 3)
			angle = 50;
		GuiInventory.drawEntityOnScreen(posX+moduleWidth/2, posY+moduleHeight, 50, angle, 0, mc.thePlayer);
		return moduleHeight;
	}
}
