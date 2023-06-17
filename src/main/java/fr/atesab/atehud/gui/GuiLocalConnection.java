package fr.atesab.atehud.gui;

import java.io.IOException;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class GuiLocalConnection extends GuiScreen {
	public GuiScreen Last;
	public GuiTextField port;
	public GuiButton co;

	public GuiLocalConnection(GuiScreen Last) {
		this.Last = Last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(Last);
		if (button.id == 1)
			net.minecraftforge.fml.client.FMLClientHandler.instance().connectToServer(Last,
					new ServerData(I18n.format("atehud.local"), "127.0.0.1:" + port.getText(), true));
		super.actionPerformed(button);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Last.drawScreen(mouseX, mouseY, partialTicks);
		int elheight = 84;
		String b = I18n.format("atehud.local.port") + " : ";
		int a = -1;
		try {
			a = Integer.valueOf(port.getText());
		} catch (Exception e) {
			co.enabled = false;
		}
		if (a > 0 && a < 65535) {
			ModMain.localPort = a;
			co.enabled = true;
		}
		int elwidth = fontRendererObj.getStringWidth(b) * 2 + 210;
		GuiUtils.drawBox(width / 2 - elwidth / 2, height / 2 - 31, elwidth, 104);
		GuiUtils.drawRightString(fontRendererObj, b, port.xPosition, port.yPosition, 18, Colors.GOLD);
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("atehud.local"), width / 2, height / 2 - 21, 20,
				Colors.WHITE);
		port.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
		Last.width = width;
		Last.height = height;
		Last.initGui();
		port = new GuiTextField(0, fontRendererObj, width / 2 - 99, height / 2, 198, 18);
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 21, 99, 20, I18n.format("gui.done")));
		buttonList.add(
				co = new GuiButton(1, width / 2 + 1, height / 2 + 21, 99, 20, I18n.format("atehud.local.connect")));
		port.setText(String.valueOf(ModMain.localPort));
		super.initGui();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		port.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		String b = I18n.format("atehud.local.port") + " : ";
		int c = fontRendererObj.getStringWidth(b) * 2 + 210;
		if (!GuiUtils.isHover(width / 2 - c / 2, height / 2 - 31, c, 104, mouseX, mouseY))
			mc.displayGuiScreen(Last);
		port.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void onGuiClosed() {
		ModMain.saveConfig();
		super.onGuiClosed();
	}

	public void updateScreen() {
		port.updateCursorCounter();
		super.updateScreen();
	}
}
