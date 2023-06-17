package fr.atesab.atehud.gui.element;

public class Title extends Button {

	public int color;
	public Title(String text, int posX, int posY, int color) {
		super(text, posX, posY);
		this.color = color;
		this.width = mc.fontRendererObj.getStringWidth(this.text);
		this.height = mc.fontRendererObj.FONT_HEIGHT;
	}
	@Override
	public void render(int mouseX, int mouseY) {
		this.width = mc.fontRendererObj.getStringWidth(this.text);
		this.height = mc.fontRendererObj.FONT_HEIGHT;
		mc.fontRendererObj.drawString(this.text, this.posX, this.posY, this.color);
		super.render(mouseX, mouseY);
	}
	
}
