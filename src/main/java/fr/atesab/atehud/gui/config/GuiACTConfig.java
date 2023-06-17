package fr.atesab.atehud.gui.config;

import java.io.IOException;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.GuiAdvancedMenu;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import fr.atesab.atehud.utils.ItemHelp;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GuiACTConfig extends GuiScreen {
	public GuiScreen Last;

	public GuiButton renderFI1, renderFI2, renderFI3;

	public GuiTextField maxLevel;
	public GuiACTConfig(GuiScreen last) {
		Last = last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(Last);
		else if (button.id == 2)
			ModMain.genFake1 = !ModMain.genFake1;
		else if (button.id == 3)
			ModMain.genFake2 = !ModMain.genFake2;
		else if (button.id == 4)
			ModMain.genFake3 = !ModMain.genFake3;
		else if (button.id == 5)
			mc.displayGuiScreen(new GuiTextListConfig(this, ModMain.AdvancedItem, I18n.format("gui.act.config.nbt")) {
				public void onUpdateValue(String[] value) {
					ModMain.AdvancedItem = value;
				}
			});// open NBTList
		else if (button.id == 6)
			mc.displayGuiScreen(new GuiTextListConfig(this, ModMain.CustomFirework, I18n.format("gui.act.config.fw")) {
				public void onUpdateValue(String[] value) {
					ModMain.CustomFirework = value;
				}
			});// open FWList
		else if (button.id == 7)
			mc.displayGuiScreen(
					new GuiTextListConfig(this, ModMain.CustomCommandBlock, I18n.format("gui.act.config.cmd")) {
						public void onUpdateValue(String[] value) {
							ModMain.CustomCommandBlock = value;
						}
					});// open CmdList
		else if (button.id == 8)
			mc.displayGuiScreen(new GuiTextListConfig(this, ModMain.HeadNames, I18n.format("gui.act.config.head")) {
				public void onUpdateValue(String[] value) {
					ModMain.HeadNames = value;
				}
			});// open HeadList
		super.actionPerformed(button);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("gui.act.config"), width / 2,
				height / 2 - 23 - fontRendererObj.FONT_HEIGHT * 2, Colors.GOLD);
		GuiUtils.drawItemStack(itemRender, zLevel, this, new ItemStack(Items.diamond_sword), width / 2 - 225,
				height / 2);
		GuiUtils.drawItemStack(itemRender, zLevel, this, new ItemStack(Blocks.command_block), width / 2 + 205,
				height / 2);
		GuiUtils.drawItemStack(itemRender, zLevel, this, new ItemStack(Items.fireworks), width / 2 - 225,
				height / 2 + 21);
		ItemStack is = new ItemStack(Items.skull);
		is.setItemDamage(3);
		GuiUtils.drawItemStack(itemRender, zLevel, this, is, width / 2 + 205, height / 2 + 21);
		maxLevel.drawTextBox();
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("gui.act.config.gen"), width / 2,
				height / 2 - 21 - fontRendererObj.FONT_HEIGHT, Colors.GOLD);
		GuiUtils.drawRightString(fontRendererObj, I18n.format("gui.act.config.maxLevel") + " : ", maxLevel.xPosition,
				maxLevel.yPosition, maxLevel.height, Colors.GOLD);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {

		buttonList.add(renderFI1 = new GuiButton(2, width / 2 - 200, height / 2 - 21, 132, 20,
				GuiUtils.getUnformattedText(I18n.format("itemGroup.ateTab"))));
		buttonList.add(renderFI2 = new GuiButton(3, width / 2 - 66, height / 2 - 21, 132, 20,
				GuiUtils.getUnformattedText(I18n.format("itemGroup.ateTab2"))));
		buttonList.add(renderFI3 = new GuiButton(4, width / 2 + 68, height / 2 - 21, 132, 20,
				GuiUtils.getUnformattedText(I18n.format("itemGroup.ateTab3"))));

		buttonList.add(new GuiButton(5, width / 2 - 200, height / 2, I18n.format("gui.act.config.nbt")));
		buttonList.add(new GuiButton(6, width / 2 - 200, height / 2 + 21, I18n.format("gui.act.config.fw")));
		buttonList.add(new GuiButton(7, width / 2 + 1, height / 2, I18n.format("gui.act.config.cmd")));
		buttonList.add(new GuiButton(8, width / 2 + 1, height / 2 + 21, I18n.format("gui.act.config.head")));
		maxLevel = new GuiTextField(0, fontRendererObj, width / 2 - 199, height / 2 + 43, 196, 18);
		maxLevel.setText(String.valueOf(ItemHelp.maxXp));
		buttonList.add(new GuiButton(0, width / 2 + 1, height / 2 + 42, I18n.format("gui.done")));
		super.initGui();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		maxLevel.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		maxLevel.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void onGuiClosed() {
		ModMain.saveConfig();
		super.onGuiClosed();
	}

	public void updateScreen() {
		maxLevel.updateCursorCounter();
		renderFI1.packedFGColour = Colors.redGreenOptionInt(ModMain.genFake1);
		renderFI2.packedFGColour = Colors.redGreenOptionInt(ModMain.genFake2);
		renderFI3.packedFGColour = Colors.redGreenOptionInt(ModMain.genFake3);
		try {
			ItemHelp.maxXp = Integer.valueOf(maxLevel.getText());
			maxLevel.setTextColor(Colors.WHITE);
		} catch (Exception e) {
			maxLevel.setTextColor(Colors.RED);
		}
		super.updateScreen();
	}
}
