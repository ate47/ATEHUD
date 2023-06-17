package fr.atesab.atehud.gui;

import java.io.IOException;
import java.util.List;

import fr.atesab.atehud.Mod;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.element.ButtonsContainer.ModContainer;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class GuiAddTab extends GuiScreen {
	public static boolean AdvancedAdd = false;
	public GuiMenu Last;
	public GuiButton simple, advance;
	public GuiTextField data;

	public GuiAddTab(GuiMenu Last) {
		this.Last = Last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(Last);
		if (button.id == 1) {
			// ADD TAB
			ModContainer con = null;
			if (AdvancedAdd) {
				con = Mod.getContainer(data.getText(), mc, 0, 0);
			} else {
				con = new ModContainer(data.getText(), 0, 0, 0, 0, new Mod[] {});
			}
			if (con != null)
				Last.cons.add(con);
			mc.displayGuiScreen(Last);
		}
		if (button.id == 2)
			AdvancedAdd = true;
		if (button.id == 3)
			AdvancedAdd = false;
		super.actionPerformed(button);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		data.drawTextBox();
		if (AdvancedAdd) {
			GuiUtils.drawRightString(fontRendererObj, I18n.format("atehud.menu.newTab.data") + " : ", data.xPosition,
					data.yPosition, data.height, Colors.GOLD);
			simple.packedFGColour = Colors.RED;
			advance.packedFGColour = Colors.GREEN;
		} else {
			GuiUtils.drawRightString(fontRendererObj, I18n.format("atehud.menu.newTab.name") + " : ", data.xPosition,
					data.yPosition, data.height, Colors.GOLD);
			simple.packedFGColour = Colors.GREEN;
			advance.packedFGColour = Colors.RED;
		}
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("atehud.menu.edit.addCat"), width/2, height/2-22-fontRendererObj.FONT_HEIGHT, Colors.GOLD);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
		data = new GuiTextField(0, fontRendererObj, width / 2 - 99, height / 2 - 20, 198, 18);
		data.setMaxStringLength(Integer.MAX_VALUE);
		buttonList.add(advance = new GuiButton(2, width / 2 - 100, height / 2, 99, 20,
				I18n.format("atehud.menu.newTab.advance")));
		buttonList.add(
				simple = new GuiButton(3, width / 2 + 1, height / 2, 99, 20, I18n.format("atehud.menu.newTab.simple")));
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 2 + 21, I18n.format("atehud.menu.newTab.create")));
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 42, I18n.format("gui.done")));
		super.initGui();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		data.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		data.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void updateScreen() {
		data.updateCursorCounter();
		super.updateScreen();
	}
}
