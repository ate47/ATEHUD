package fr.atesab.atehud.gui.config;

import java.io.IOException;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.GuiAdvancedMenu;
import fr.atesab.atehud.gui.GuiHudOptions;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.ts3.TS3Socket;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class GuiTS3Config extends GuiScreen {
	public GuiScreen Last;
	public GuiButton poke, text, gen;
	public GuiTextField tf;
	
	public GuiTS3Config(GuiScreen last) {
		Last = last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(Last);
		else if (button.id == 2)
			ModMain.TS3Poke = !ModMain.TS3Poke;
		else if (button.id == 3)
			ModMain.TS3Text = !ModMain.TS3Text;
		else if (button.id == 4)
			ModMain.genTS3 = !ModMain.genTS3;
		else if (button.id == 1) {
			TS3Socket.closeSocket();
			TS3Socket.openSocket();
		}
		super.actionPerformed(button);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		poke.packedFGColour = Colors.redGreenOptionInt(ModMain.TS3Poke);
		text.packedFGColour = Colors.redGreenOptionInt(ModMain.TS3Text);
		gen.packedFGColour = Colors.redGreenOptionInt(ModMain.genTS3);
		tf.drawTextBox();
		GuiUtils.drawRightString(fontRendererObj, I18n.format("ts3.apiKey")+" : ", tf.xPosition, tf.yPosition, tf.height, Colors.GOLD);
		//GuiHudOptions.drawScreenIcon(ModMain.hudTS3Location, width / 2 + 1, height / 2 - 21);
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("mod.others.ts3.name"), width / 2,
				height / 2 - 22 - fontRendererObj.FONT_HEIGHT, Colors.GOLD);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 2 - 21, 99, 20, I18n.format("ts3.restart")));
		buttonList.add(gen = new GuiButton(4, width / 2, height / 2 - 21, 100, 20, I18n.format("ts3.gen")));
		buttonList.add(poke = new GuiButton(2, width / 2-100, height / 2, I18n.format("ts3.config.poke")));
		buttonList.add(text = new GuiButton(3, width / 2-100, height / 2 + 21, I18n.format("ts3.config.text")));
		buttonList.add(new GuiButton(0, width / 2-100, height / 2 + 63, I18n.format("gui.done")));
		tf = new GuiTextField(0, fontRendererObj, width / 2-98, height / 2 + 43, 196, 16);
		tf.setText(ModMain.ts3ClientApiKey);
		super.initGui();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		tf.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (GuiHudOptions.CheckScreenIconValue(width / 2 + 1, height / 2 - 21, mouseX, mouseY) != null)
			if (!(GuiHudOptions.CheckScreenIconValue(width / 2 + 1, height / 2 - 21, mouseX, mouseY)
					.equals(ModMain.hudTS3Location))) {
				ModMain.hudTS3Location = (Integer) GuiHudOptions.CheckScreenIconValue(width / 2 + 1, height / 2 - 21,
						mouseX, mouseY);
			} else {
				ModMain.hudTS3Location = GuiHudOptions.NONE;
			}
		tf.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	public void onGuiClosed() {
		ModMain.saveConfig();
		super.onGuiClosed();
	}
	public void updateScreen() {
		ModMain.ts3ClientApiKey = tf.getText();
		tf.updateCursorCounter();
		super.updateScreen();
	}
}
