package fr.atesab.atehud.gui;

import fr.atesab.atehud.superclass.Window;
import net.minecraft.client.gui.GuiScreen;

public class GuiMultiSelector extends GuiScreen {
	public GuiScreen Last;
	public float selector_position = 0.0F;
	public Window wd;

	public GuiMultiSelector(GuiScreen last, Window wd) {
		Last = last;
		this.wd = wd;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {

		super.initGui();
	}
}
