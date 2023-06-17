package fr.atesab.atehud.gui;

import java.io.IOException;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.superclass.Colors;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class GuiGameMode extends GuiScreen {
	public GuiScreen Last;
	public GuiTextField nextGM;

	public GuiGameMode(GuiScreen last) {
		Last = last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 4)
			mc.displayGuiScreen(Last);
		else if (button.id == 5) {
			Chat.show(I18n.format("gui.act.setGm"));
			ATEEventHandler.tryChangingGameMode = true;
			mc.thePlayer.sendChatMessage("/gamemode " + nextGM.getText());
		} else {
			Chat.show(I18n.format("gui.act.setGm"));
			ATEEventHandler.tryChangingGameMode = true;
			mc.thePlayer.sendChatMessage("/gamemode " + button.id);
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		drawCenteredString(fontRendererObj, I18n.format("selectWorld.gameMode"), width / 2,
				height / 2 - 21 - (fontRendererObj.FONT_HEIGHT + 1) * 2, Colors.WHITE);
		drawCenteredString(fontRendererObj,
				Chat.c_Modifier + "l" + I18n.format("gui.act.setGm.hover").replaceAll("::", ""), width / 2,
				height / 2 - 21 - (fontRendererObj.FONT_HEIGHT + 1) * 1, Colors.RED);
		nextGM.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
		buttonList.add(new GuiButton(4, width / 2 - 200, height / 2 + 21, 199, 20, I18n.format("gui.done")));
		buttonList.add(new GuiButton(5, width / 2 + 180, height / 2 + 21, 20, 20, I18n.format("=>")));
		buttonList.add(new GuiButton(0, width / 2 - 200, height / 2 - 21, 199, 20, I18n.format("gameMode.survival")));
		buttonList.add(new GuiButton(1, width / 2, height / 2 - 21, 200, 20, I18n.format("gameMode.creative")));
		buttonList.add(new GuiButton(2, width / 2 - 200, height / 2, 199, 20, I18n.format("gameMode.adventure")));
		buttonList.add(new GuiButton(3, width / 2, height / 2, 200, 20, I18n.format("gameMode.spectator")));
		nextGM = new GuiTextField(0, fontRendererObj, width / 2 + 1, height / 2 + 22, 176, 18);
		nextGM.setText(ModMain.gamemode);
		super.initGui();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		nextGM.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		nextGM.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void onGuiClosed() {
		ModMain.saveConfig();
		super.onGuiClosed();
	}

	public void updateScreen() {
		ModMain.gamemode = nextGM.getText();
		nextGM.updateCursorCounter();
		super.updateScreen();
	}
}
