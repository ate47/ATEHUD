package fr.atesab.atehud.gui.element;

import net.minecraft.client.gui.GuiScreen;

public class OverlayData {
	public GuiScreen[] whereRender = {};
	public Element[] toRender = {};

	public OverlayData() {
	};

	public OverlayData(GuiScreen[] whereRender, Element[] toRender) {
		this.whereRender = whereRender;
		this.toRender = toRender;
	};
}
