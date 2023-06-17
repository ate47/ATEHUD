package fr.atesab.atehud.gui.config;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import fr.atesab.atehud.gui.element.Element;
import fr.atesab.atehud.overlay.OverlayDefaultConfig;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;

public abstract class GuiOverlayConfigElement extends Element {
	protected OverlayDefaultConfig overlayDefaultConfig;
	public GuiOverlayConfigElement(int posX, int posY, int width, int height, OverlayDefaultConfig overlayDefaultConfig) {
		super(posX, posY, width, height);
		this.overlayDefaultConfig = overlayDefaultConfig;
	}
	@Override
	public void render(int mouseX, int mouseY) {
		//draw box
		GuiUtils.drawBlackBackGround(posX, posY, width, height);
		GuiUtils.drawBlackBackGround(posX, posY, width, height);
		GuiUtils.drawBox(posX, posY, posX + width, posY + height, new Color(Colors.WHITE));
		
		RenderItem itemRenderer = mc.getRenderItem();
		itemRenderer.zLevel = 200.0F;
		int delta = (height-20)/2;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		itemRenderer.renderItemAndEffectIntoGUI(overlayDefaultConfig.getIcon(), posX+delta, posY+delta);
		int posX = this.posX+delta*2+20;
		delta = (height-(mc.fontRendererObj.FONT_HEIGHT+1)*2)/2;
		int cursorY = posY+delta;
		GuiUtils.drawString(mc.fontRendererObj, I18n.format("atehud.config.overlay.default.name")+": "+overlayDefaultConfig.getTitle()+
				", "+I18n.format("atehud.config.overlay.default.author")+": "+overlayDefaultConfig.getAuthor(), posX, cursorY, Colors.WHITE);
		GuiUtils.drawString(mc.fontRendererObj, overlayDefaultConfig.getDescription(), posX, cursorY+=mc.fontRendererObj.FONT_HEIGHT+1, Colors.WHITE);
		RenderHelper.disableStandardItemLighting();
		super.render(mouseX, mouseY);
	}
	public abstract void onClick(int mouseX, int mouseY);
}
