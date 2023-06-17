package fr.atesab.atehud.gui;

import java.io.IOException;
import java.lang.reflect.Field;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class GuiVariableChanger extends GuiScreen {
	public GuiScreen Last;
	public GuiTextField var, cls, var1;

	public GuiVariableChanger(GuiScreen Last) {
		this.Last = Last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(Last);
		if (button.id == 1) {
			try {
				Field f = Class.forName(cls.getText()).getField(var.getText());
				f.setAccessible(true);
				f.set(mc, var1.getText());
			} catch (Exception e) {
			}
		}
		super.actionPerformed(button);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		var.drawTextBox();
		cls.drawTextBox();
		var1.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 21, 99, 20, I18n.format("gui.done")));
		buttonList.add(new GuiButton(1, width / 2 + 1, height / 2 + 21, 99, 20, I18n.format("atehud.varchang.change")));
		var = new GuiTextField(0, fontRendererObj, width / 2 - 98, height / 2, 198, 18);
		cls = new GuiTextField(1, fontRendererObj, width / 2 - 98, height / 2 - 21, 198, 18);
		var1 = new GuiTextField(2, fontRendererObj, width / 2 - 98, height / 2 - 42, 198, 18);
		super.initGui();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		var.textboxKeyTyped(typedChar, keyCode);
		cls.textboxKeyTyped(typedChar, keyCode);
		var1.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		var.mouseClicked(mouseX, mouseY, mouseButton);
		cls.mouseClicked(mouseX, mouseY, mouseButton);
		var1.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void updateScreen() {
		var.updateCursorCounter();
		cls.updateCursorCounter();
		var1.updateCursorCounter();
		super.updateScreen();
	}
}
