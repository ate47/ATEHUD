package fr.atesab.atehud.overlay.module;

import java.awt.Color;
import java.util.ArrayList;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.GuiHudOptions;
import fr.atesab.atehud.overlay.Module;
import fr.atesab.atehud.overlay.Overlay.position;
import fr.atesab.atehud.superclass.ModuleConfig;
import fr.atesab.atehud.ts3.TS3Socket;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class Teamspeak extends Module {

	public ArrayList<ModuleConfig> getConfigs(ArrayList<ModuleConfig> list) {
		return list;
	}

	public String getDisplayName(String[] args) {
		return I18n.format("atehud.config.overlay.module."+getModuleId()+".name");
	}

	public String getModuleId() {
		return "teamspeak";
	}

	public int moduleSizeX(String[] args) {
		if (!ModMain.genTS3)
			return 0;
		int elemSizeX = mc.fontRendererObj.getStringWidth(TS3Socket.currentChannel.channel_name) + 10;
		for (int i = 0; i < TS3Socket.clients.size(); i++) {
			if (mc.fontRendererObj.getStringWidth(TS3Socket.clients.get(i).client_nickname) + 10 > elemSizeX)
				elemSizeX = mc.fontRendererObj.getStringWidth(TS3Socket.clients.get(i).client_nickname) + 10;
		}
		return elemSizeX;
	}

	public int moduleSizeY(String[] args) {
		if (!ModMain.genTS3)
			return 0;
		int lineSize = 8;
		if (mc.fontRendererObj.FONT_HEIGHT > lineSize)
			lineSize = mc.fontRendererObj.FONT_HEIGHT;
		int elemSizeY = lineSize * (TS3Socket.clients.size() + 1) + 1;
		return elemSizeY;
	}

	public int renderModule(int posX, int posY, String[] args) {
		if (!ModMain.genTS3)
			return 0;
		int lineSize = 8;
		if (mc.fontRendererObj.FONT_HEIGHT > lineSize)
			lineSize = mc.fontRendererObj.FONT_HEIGHT;
		int elemSizeY = lineSize * (TS3Socket.clients.size() + 1) + 1;
		GuiUtils.drawString(mc.fontRendererObj, TS3Socket.currentChannel.channel_name, posX + 10, posY + 1,
				ModMain.getGuiColor().getRGB());
		mc.getTextureManager()
				.bindTexture(new ResourceLocation("textures/icons/ts3/" + TS3Socket.currentChannel.getIcon() + ".png"));
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		GuiHudOptions.drawScaledCustomSizeModalRect(posX, posY + 1, 0, 0, 128, 128, 8, 8, 128, 128);
		for (int i = 0; i < TS3Socket.clients.size(); i++) {
			if (TS3Socket.clients.get(i) != null) {
				GuiUtils.drawString(mc.fontRendererObj, TS3Socket.clients.get(i).getName(), posX + 10,
						posY + 2 + (i + 1) * lineSize,
						ModMain.getTextColor().getRGB());
				mc.getTextureManager().bindTexture(
						new ResourceLocation("textures/icons/ts3/" + TS3Socket.clients.get(i).getIcon() + ".png"));
				GlStateManager.color(1.0F, 1.0F, 1.0F);
				GuiHudOptions.drawScaledCustomSizeModalRect(posX, posY + 2 + (i + 1) * lineSize, 0, 0, 128, 128, 8, 8,
						128, 128);
			}
		}
		return elemSizeY;

	}
}