package fr.atesab.atehud.gui.element;

import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.Minecraft;

public class Element {
	public static Minecraft mc = Minecraft.getMinecraft();
	public static void setMinecraft(Minecraft mc) {
		Element.mc = mc;
	}
	public boolean visible;
	public int posX;
	public int posY;
	public int height;
	public int width;

	public boolean enabled = true, hovered;

	public Element(int posX, int posY, int width, int height) {
		this.visible = true;
		this.posX = posX;
		this.posY = posY;
		this.height = height;
		this.width = width;
	}

	public void mouseClick(int mouseX, int mouseY, int button) {
		hovered = GuiUtils.isHover(this, mouseX, mouseY);
		if (hovered && button == 0)
			onClick(mouseX, mouseY);
		if (hovered && button == 1)
			onRightClick(mouseX, mouseY);
	}

	public void onClick(int mouseX, int mouseY) {
	}

	public void onHover(int mouseX, int mouseY) {
	}

	public void onRightClick(int mouseX, int mouseY) {
	}

	public void render(int mouseX, int mouseY) {
		hovered = GuiUtils.isHover(this, mouseX, mouseY);
	}

	public void renderHover(int mouseX, int mouseY) {
		if (hovered)
			onHover(mouseX, mouseY);
	}
}
