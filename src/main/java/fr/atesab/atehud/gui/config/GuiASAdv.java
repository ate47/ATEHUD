package fr.atesab.atehud.gui.config;

import java.io.IOException;

import org.lwjgl.opengl.Display;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.GuiAdvancedMenu;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class GuiASAdv extends GuiScreen {
	public GuiScreen Last;
	public GuiButton advMode, advMenu, alEnable;
	public GuiTextField windowName;

	private String lastName = "";

	public GuiASAdv(GuiScreen last) {
		Last = last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(Last);
		else if (button.id == 2)
			ModMain.AdvancedModActived = !ModMain.AdvancedModActived;
		else if (button.id == 3)
			ModMain.useAdvancedMenu = !ModMain.useAdvancedMenu;
		else if (button.id == 4)
			ModMain.autoLogin = !ModMain.autoLogin;
		super.actionPerformed(button);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		windowName.drawTextBox();
		GuiUtils.drawRightString(fontRendererObj, I18n.format("atehud.config.window") + " : ", windowName.xPosition,
				windowName.yPosition, windowName.height, Colors.GOLD);
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("atehud.config.adv"), width / 2,
				height / 2 - 43 - fontRendererObj.FONT_HEIGHT, Colors.GOLD);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
		buttonList.add(
				advMode = new GuiButton(2, width / 2 - 100, height / 2 - 42, I18n.format("atehud.config.advMode")));
		buttonList.add(
				advMenu = new GuiButton(3, width / 2 - 100, height / 2 - 21, I18n.format("atehud.config.advMenu")));
		windowName = new GuiTextField(0, fontRendererObj, width / 2 - 98, height / 2 + 1, 196, 16);
		windowName.setMaxStringLength(Integer.MAX_VALUE);
		windowName.setText(ModMain.CustomName);
		buttonList.add(
				alEnable = new GuiButton(4, width / 2 - 100, height / 2 + 21, I18n.format("gui.autologin.enable")));
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 42, I18n.format("gui.done")));
		super.initGui();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		windowName.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		windowName.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	public void updateScreen() {
		windowName.updateCursorCounter();
		ModMain.CustomName = windowName.getText();
		if (!ModMain.CustomName.equals("") && lastName != ModMain.CustomName)
			GuiUtils.setWindowName(lastName=ModMain.CustomName);;
		advMode.packedFGColour = Colors.redGreenOptionInt(ModMain.AdvancedModActived);
		advMenu.packedFGColour = Colors.redGreenOptionInt(ModMain.useAdvancedMenu);
		alEnable.packedFGColour = Colors.redGreenOptionInt(ModMain.autoLogin);
		super.updateScreen();
	}
}
