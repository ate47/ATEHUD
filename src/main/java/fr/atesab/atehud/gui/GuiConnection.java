package fr.atesab.atehud.gui;

import java.io.IOException;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.superclass.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiConnection extends GuiScreen {
	public GuiButton bconnect, bdefaultpassword, bcancel;
	private boolean badPassword;
	public String method;
	public int addInfo;

	public GuiTextField password, password1;

	public GuiConnection(String method, int addInfo) {
		badPassword = false;
		this.addInfo = addInfo;
		this.method = method;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == bconnect) {
			if (password.getText().equals(password1.getText())) {
				doCoReg(password.getText());
				ModMain.isConnect = true;
			} else {
				badPassword = true;
			}
		}
		if (button == bcancel)
			mc.displayGuiScreen(null);
		if (button == bdefaultpassword) {
			doCoReg(ModMain.defaultPassword);
		}
		super.actionPerformed(button);
	}

	public void doCoReg() {
		doCoReg(ModMain.defaultPassword);
	}

	public void doCoReg(String password) {
		String message = "/" + method;
		for (int i = 0; i < addInfo; i++) {
			message += " " + password;
		}
		ModMain.ChangePassword(mc.getCurrentServerData().serverIP, password);
		ModMain.isConnect = true;
		mc.thePlayer.sendChatMessage(message);
		mc.displayGuiScreen(null);
	}

	public void drawRightString(String str, int posX, int posY) {
		drawString(fontRendererObj, str, posX - fontRendererObj.getStringWidth(str), posY, Colors.WHITE);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		drawRightString(I18n.format("gui.autologin.password"), password.xPosition - 5,
				password.yPosition - fontRendererObj.FONT_HEIGHT / 2 + password.height / 2);
		password.drawTextBox();
		drawRightString(I18n.format("gui.autologin.password.again"), password1.xPosition - 5,
				password1.yPosition - fontRendererObj.FONT_HEIGHT / 2 + password1.height / 2);
		password1.drawTextBox();
		if (badPassword)
			drawCenteredString(fontRendererObj, I18n.format("gui.autologin.badpassword"), width / 2, height / 2 + 63,
					Colors.RED);
		if (ModMain.defaultPassword.isEmpty())
			bdefaultpassword.visible = false;
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
		ModMain.isinConnect = true;
		Minecraft mc = Minecraft.getMinecraft();

		password = new GuiTextField(0, ModMain.passwordFontRenderer, width / 2 - 98, height / 2 - 21, 200, 20);
		password1 = new GuiTextField(1, ModMain.passwordFontRenderer, width / 2 - 98, height / 2, 200, 20);
		buttonList.add(bconnect = new GuiButton(0, width / 2 - 100, height / 2 + 21, 99, 20,
				I18n.format("gui.autologin.connect")));
		buttonList.add(bdefaultpassword = new GuiButton(1, width / 2 - 100, height / 2 + 42, 200, 20,
				I18n.format("gui.autologin.defaultpass")));
		buttonList.add(
				bcancel = new GuiButton(2, width / 2, height / 2 + 21, 100, 20, I18n.format("gui.autologin.cancel")));
		super.initGui();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		password.textboxKeyTyped(typedChar, keyCode);
		password1.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		password.mouseClicked(mouseX, mouseY, mouseButton);
		password1.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void onGuiClosed() {
		ModMain.isinConnect = false;
		super.onGuiClosed();
	}

	public void updateScreen() {
		password.updateCursorCounter();
		password1.updateCursorCounter();
		super.updateScreen();
	}

}
