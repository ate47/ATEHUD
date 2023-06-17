package fr.atesab.atehud.gui;

import java.io.IOException;

import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public abstract class GuiRename extends GuiScreen {
	public GuiScreen last;
	public GuiTextField field;
	public String name;
	public GuiRename(GuiScreen last, String name) {
		this.last = last;
		this.name = name;
	}
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id==0)mc.displayGuiScreen(last);
		else if(button.id==1) {
			setName(name=field.getText());
			mc.displayGuiScreen(last);
		}
		super.actionPerformed(button);
	}
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		GuiUtils.drawRightString(fontRendererObj, I18n.format("atehud.menu.newTab.name")+" : ", field.xPosition, field.yPosition, field.height, Colors.GOLD);
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("atehud.menu.edit.renameCat"), width/2, field.yPosition-1-fontRendererObj.FONT_HEIGHT, Colors.GOLD);
		field.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	public void initGui() {
		field = new GuiTextField(0, fontRendererObj, width/2-99, height/2-20, 198, 18);
		field.setMaxStringLength(Integer.MAX_VALUE);
		field.setText(name);
		
		buttonList.add(new GuiButton(1, width/2-100, height/2, I18n.format("gui.done")));
		buttonList.add(new GuiButton(0, width/2-100, height/2+21, I18n.format("atehud.menu.edit.undo")));
		super.initGui();
	}
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		field.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		field.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	public abstract void setName(String name);
	public void updateScreen() {
		name = field.getText();
		field.updateCursorCounter();
		super.updateScreen();
	}
}
