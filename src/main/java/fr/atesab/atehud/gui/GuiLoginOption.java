package fr.atesab.atehud.gui;

import java.io.IOException;
import java.util.List;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class GuiLoginOption extends GuiScreen {
	GuiScreen Last = null;

	private GuiTextField defaultpass, defaultnewpass1, defaultnewpass2, currentpass, currentnewpass1, currentnewpass2;

	private GuiButton changeCurrentPass, setDefaultToCurrentPass, useChangePassCommand, cancel, switchButton;
	
	private boolean defaultMenu;
	private boolean failpass = false;
	public GuiLoginOption() {
		this(null, false);
	}
	public GuiLoginOption(GuiScreen last) {
		this(last, false);
	}
	public GuiLoginOption(GuiScreen last, boolean defaultMenu) {
		Last = last;
		defaultMenu = true;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == changeCurrentPass) {
			if(defaultMenu) {
				if (currentnewpass1.getText().equals(currentnewpass2.getText())
						&& currentpass.getText().equals(ModMain.passwordList.get(mc.getCurrentServerData().serverIP))) {
					ModMain.ChangePassword(mc.getCurrentServerData().serverIP, currentnewpass1.getText());
					Chat.show(I18n.format("gui.autologin.change.password.done").replaceAll("SERVERIP",
							mc.getCurrentServerData().serverIP));
				} else {
					failpass = true;
				}
			} else {
				if (defaultnewpass1.getText().equals(defaultnewpass2.getText())
						&& (defaultpass.getText().equals(ModMain.defaultPassword) || ModMain.defaultPassword.isEmpty())) {
					ModMain.setDefaultPassword(defaultnewpass1.getText());
					Chat.show(I18n.format("gui.autologin.change.password.default.done"));
				} else {
					failpass = true;
				}
			}
		}
			
		if (button == setDefaultToCurrentPass) {
			String exPass = ModMain.passwordList.get(mc.getCurrentServerData().serverIP);
			ModMain.ChangePassword(mc.getCurrentServerData().serverIP, ModMain.defaultPassword);
			mc.thePlayer.sendChatMessage("/changepassword " + exPass + " " + ModMain.defaultPassword);
			Chat.show(I18n.format("gui.autologin.change.password.done").replaceAll("SERVERIP",
					mc.getCurrentServerData().serverIP));
		}
		if (button == useChangePassCommand) {
			if (currentnewpass1.getText().equals(currentnewpass2.getText())
					&& currentpass.getText().equals(ModMain.passwordList.get(mc.getCurrentServerData().serverIP))) {
				String exPass = ModMain.passwordList.get(mc.getCurrentServerData().serverIP);
				ModMain.ChangePassword(mc.getCurrentServerData().serverIP, currentnewpass1.getText());
				mc.thePlayer.sendChatMessage("/changepassword " + exPass + " " + currentnewpass2.getText());
				Chat.show(I18n.format("gui.autologin.change.password.done").replaceAll("SERVERIP",
						mc.getCurrentServerData().serverIP));
			} else {
				failpass = true;
			}
		}
		if (button == cancel)
			mc.displayGuiScreen(Last);
		if (button == switchButton) {
			defaultMenu=!defaultMenu;
			String n = "gui.autologin.change.currentpass"; if(defaultMenu)n="gui.autologin.change.defaultpass";
			switchButton.displayString = n;
		}
		super.actionPerformed(button);
	}

	public void doCoReg(String method, int addInfo) {
		doCoReg(ModMain.defaultPassword, method, addInfo);
	}

	public void doCoReg(String password, String method, int addInfo) {
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
		if (failpass)
			drawCenteredString(fontRendererObj, I18n.format("gui.autologin.badpassword2"), width / 2, height/2-43-fontRendererObj.FONT_HEIGHT,
					Colors.RED);
		GuiUtils.drawRightString(fontRendererObj, I18n.format("gui.autologin.password") + " : ", 
				currentpass.xPosition, currentpass.yPosition, currentpass.height, Colors.GOLD);
		GuiUtils.drawRightString(fontRendererObj, I18n.format("gui.autologin.newpassword") + " : ", 
				currentnewpass1.xPosition, currentnewpass1.yPosition, currentnewpass1.height, Colors.GOLD);
		GuiUtils.drawRightString(fontRendererObj, I18n.format("gui.autologin.newpassword.again") + " : ", 
				currentnewpass2.xPosition, currentnewpass2.yPosition, currentnewpass2.height, Colors.GOLD);
		if(defaultMenu) {
			if (!ModMain.defaultPassword.isEmpty())
				defaultpass.drawTextBox();
			defaultnewpass1.drawTextBox();
			defaultnewpass2.drawTextBox();
		} else {
			currentpass.drawTextBox();
			currentnewpass1.drawTextBox();
			currentnewpass2.drawTextBox();
		}
		if (ModMain.defaultPassword.isEmpty())
			setDefaultToCurrentPass.enabled = false;
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
		failpass = false;
		buttonList.add(changeCurrentPass = new GuiButton(2,width/2-200, height/2+21, 200, 20,
				I18n.format("gui.autologin.change.currentpass")));
		buttonList.add(useChangePassCommand = new GuiButton(4, width/2+1, height/2+21, 200, 20,
				I18n.format("gui.autologin.change.currentpass.cmd")));
		
		buttonList.add(setDefaultToCurrentPass = new GuiButton(3, width/2-200, height/2+42, 133, 20,
				I18n.format("gui.autologin.change.usedefaultpass")));
		String n = "gui.autologin.change.currentpass"; if(defaultMenu)n="gui.autologin.change.defaultpass";
		buttonList.add(switchButton = new GuiButton(4, width/2-68, height/2+42, 133, 20, I18n.format(n)));
		buttonList.add(cancel = new GuiButton(0, width/2+66, height/2+42, 133, 20, I18n.format("gui.autologin.cancel")));
		defaultpass = new GuiTextField(0, ModMain.passwordFontRenderer, width/2-199, height/2-20, 198, 18);
		defaultnewpass1 = new GuiTextField(1, ModMain.passwordFontRenderer, width/2-199, height/2+1, 198, 18);
		defaultnewpass2 = new GuiTextField(2, ModMain.passwordFontRenderer, width/2-199, height/2+22, 198, 18);
		
		currentpass = new GuiTextField(3, ModMain.passwordFontRenderer, width/2-199, height/2-41, 198, 18);
		currentnewpass1 = new GuiTextField(4, ModMain.passwordFontRenderer, width/2-199, height/2-20, 198, 18);
		currentnewpass2 = new GuiTextField(5, ModMain.passwordFontRenderer, width/2-199, height/2+1, 198, 18);
		super.initGui();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if(defaultMenu) {
			defaultpass.textboxKeyTyped(typedChar, keyCode);
			defaultnewpass1.textboxKeyTyped(typedChar, keyCode);
			defaultnewpass2.textboxKeyTyped(typedChar, keyCode);
		} else {
			currentpass.textboxKeyTyped(typedChar, keyCode);
			currentnewpass1.textboxKeyTyped(typedChar, keyCode);
			currentnewpass2.textboxKeyTyped(typedChar, keyCode);
		}
		super.keyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if(defaultMenu) {
			defaultpass.mouseClicked(mouseX, mouseY, mouseButton);
			defaultnewpass1.mouseClicked(mouseX, mouseY, mouseButton);
			defaultnewpass2.mouseClicked(mouseX, mouseY, mouseButton);
		} else {
			currentpass.mouseClicked(mouseX, mouseY, mouseButton);
			currentnewpass1.mouseClicked(mouseX, mouseY, mouseButton);
			currentnewpass2.mouseClicked(mouseX, mouseY, mouseButton);
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void updateScreen() {
		if(defaultMenu) {
			defaultpass.updateCursorCounter();
			defaultnewpass1.updateCursorCounter();
			defaultnewpass2.updateCursorCounter();
		} else {
			currentpass.updateCursorCounter();
			currentnewpass1.updateCursorCounter();
			currentnewpass2.updateCursorCounter();
		}
		super.updateScreen();
	}
}
