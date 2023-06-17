package fr.atesab.atehud.gui.config;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import fr.atesab.atehud.gui.ConfigGUI;
import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.GuiAdvancedMenu;
import fr.atesab.atehud.gui.GuiAlt;
import fr.atesab.atehud.gui.GuiHudOptions;
import fr.atesab.atehud.gui.GuiLoginOption;
import fr.atesab.atehud.gui.GuiMenu;
import fr.atesab.atehud.gui.GuiToolTypConfig;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiATEHUDConfig extends GuiScreen {
	public GuiScreen Last;

	public GuiATEHUDConfig(GuiScreen last) {
		Last = last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			ModMain.saveConfig();
			ATEEventHandler.lastMouse = 0;
			mc.displayGuiScreen(Last);
		} else if (button.id == 1)
			mc.displayGuiScreen(new GuiOverlays(this));
		/*else if (button.id == 2)
			mc.displayGuiScreen(new GuiHudOptions(this));*/
		else if (button.id == 3)
			mc.displayGuiScreen(new GuiToolTypConfig(this));
		else if (button.id == 4)
			mc.displayGuiScreen(new GuiChatOption(this));
		else if (button.id == 5)
			mc.displayGuiScreen(new GuiLoginOption(this));
		else if (button.id == 6)
			mc.displayGuiScreen(new GuiAlt(this));
		else if (button.id == 7)
			mc.displayGuiScreen(new GuiKeyBinding(this));
		else if (button.id == 8)
			mc.displayGuiScreen(new GuiACTConfig(this));
		else if (button.id == 9)
			mc.displayGuiScreen(new GuiASAdv(this));
		super.actionPerformed(button);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		GuiUtils.drawCenterString(fontRendererObj, ModMain.MOD_LITTLE_NAME + " / v" + ModMain.MOD_VERSION, width / 2,
				height / 2 - 21 - (fontRendererObj.FONT_HEIGHT + 2) * 2, Colors.WHITE);
		if (ModMain.AdvancedModActived)
			GuiUtils.drawCenterString(fontRendererObj, "Advanced Mod Enabled", width / 2,
					height / 2 - 21 - (fontRendererObj.FONT_HEIGHT + 2), Colors.WHITE);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
		buttonList
				.add(new GuiButton(1, width / 2 - 200, height / 2 - 21, 199, 20, I18n.format("atehud.config.overlay")));
		buttonList.add(new GuiButton(6, width / 2, height / 2 - 21, 199, 20, I18n.format("account")));
		//buttonList.add(new GuiButton(2, width / 2, height / 2 - 21, I18n.format("gui.atehud.HUD")));
		buttonList.add(new GuiButton(3, width / 2 - 200, height / 2, 199, 20, I18n.format("gui.atehud.TT")));
		buttonList.add(new GuiButton(4, width / 2, height / 2, I18n.format("atehud.chat.option")));
		buttonList.add(
				new GuiButton(5, width / 2 - 200, height / 2 + 21, 199, 20, I18n.format("key.autologin.loginOption")));
		buttonList.add(new GuiButton(7, width / 2, height / 2 + 21, I18n.format("mod.commands.keyb.name")));
		buttonList.add(new GuiButton(8, width / 2 - 200, height / 2 + 42, 199, 20, I18n.format("gui.act.config")));
		buttonList.add(new GuiButton(9, width / 2, height / 2 + 42, I18n.format("atehud.config.adv")));

		buttonList.add(new GuiButton(0, width / 2, height / 2 + 63, I18n.format("gui.done")));
		super.initGui();
	}

}
