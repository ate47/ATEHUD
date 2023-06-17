package fr.atesab.atehud.gui.element;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.utils.GuiUtils;

public class RightClickElement extends Element {
	public int posX;
	public int posY;
	public List<Button> buttons;
	
	public RightClickElement(int mouseX, int mouseY, Button...buttons) {
		super(0, 0, 0, 0);
		this.buttons = new ArrayList<Button>();
		for (Button b: buttons) this.buttons.add(b);
		this.width = getMaxX();
		this.height = getMaxY();
		this.posX = mouseX + 5 > mc.currentScreen.width ? (mouseX + 5 - this.width < 0 ? 0 : mouseX + 5 - this.width) : mouseX + 5;
		this.posY = mouseY + 5 > mc.currentScreen.height ? (mouseY + 5 - this.height < 0 ? 0 : mouseY + 5 - this.height) : mouseY + 5;
	}
	public int getMaxX() {
		int sx = 0;
		for (Button b: buttons) {
			int i = mc.fontRendererObj.getStringWidth(b.text);
			sx = i > sx ? i : sx;
		}
		return sx+6;
	}
	public int getMaxY() {
		return (mc.fontRendererObj.FONT_HEIGHT+1)*buttons.size()+4;
	}
	@Override
	public void mouseClick(int mouseX, int mouseY, int b) {
		int cursorY = posY+2-mc.fontRendererObj.FONT_HEIGHT+1;
		for (Button button: this.buttons)
				if(GuiUtils.isHover(cursorY, posX, getMaxX(), mc.fontRendererObj.FONT_HEIGHT+1, mouseX, mouseY))
					if(b==0) button.onClick(mouseX, mouseY);
					else if(b==1) button.onRightClick(mouseX, mouseY);
		super.mouseClick(mouseX, mouseY, b);
	}
	@Override
	public void render(int mouseX, int mouseY) {
		Color MenuColor = ModMain.getGuiColor();
		Color TextColor = ModMain.getTextColor();
		//draw box
		GuiUtils.drawBlackBackGround(posX, posY, getMaxX(), getMaxY());
		GuiUtils.drawBox(posX, posY, posX + getMaxX(), posY + getMaxY(), MenuColor);
		int cursorY = posY+2-mc.fontRendererObj.FONT_HEIGHT+1;
		for (Button button: this.buttons)
			GuiUtils.drawString(mc.fontRendererObj, button.text, posX+3, cursorY+=mc.fontRendererObj.FONT_HEIGHT+1, 
				(GuiUtils.isHover(cursorY, posX, getMaxX(), mc.fontRendererObj.FONT_HEIGHT+1, mouseX, mouseY) ? MenuColor : TextColor).getRGB());
		super.render(mouseX, mouseY);
	}
}
