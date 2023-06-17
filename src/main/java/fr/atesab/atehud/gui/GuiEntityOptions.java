package fr.atesab.atehud.gui;

import java.io.IOException;

import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;

public class GuiEntityOptions extends GuiScreen {
	public GuiScreen Last;
	public GuiTextField nbt;
	public EntityLivingBase entity;

	public GuiEntityOptions(GuiScreen Last, EntityLivingBase entity) {
		this.Last = Last;
		this.entity = entity;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(Last);
		super.actionPerformed(button);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		GuiUtils.drawRightString(fontRendererObj, I18n.format("gui.act.entityView.tags") + " : ", width / 2 - 100,
				height / 2, 20, Colors.GOLD);
		drawCenteredString(fontRendererObj, entity.getDisplayName().getFormattedText(), width / 2,
				height / 2 - fontRendererObj.FONT_HEIGHT - 2, Colors.WHITE);
		nbt.drawTextBox();
		GuiInventory.drawEntityOnScreen(width / 2 - 200, height / 2 + 21, 50, width / 2, height / 2, entity);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 42, 200, 20, I18n.format("gui.done")));
		nbt = new GuiTextField(0, fontRendererObj, width / 2 - 99, height / 2 + 1, 198, 18);
		nbt.setMaxStringLength(Integer.MAX_VALUE);
		nbt.setText(entity.getEntityData().toString());
		super.initGui();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		nbt.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		nbt.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void updateScreen() {
		nbt.updateCursorCounter();
		super.updateScreen();
	}
}
