package fr.atesab.atehud.gui.element;

import java.util.List;

import fr.atesab.atehud.utils.GuiUtils;

public class HoverText extends Element {
	private String[] text;
	private int color;
	public HoverText(int color, String... text) {
		super(0, 0, 0, 0);
		this.text = text;
		this.color = color;
	}
	public void onHover(int mouseX, int mouseY) {
		GuiUtils.drawTextBox(mc.currentScreen, mc, mc.fontRendererObj, text, mouseX, mouseY, color);
		super.onHover(mouseX, mouseY);
	}
}
