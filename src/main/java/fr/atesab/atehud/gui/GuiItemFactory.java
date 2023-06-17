package fr.atesab.atehud.gui;

import java.io.IOException;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.FakeItems3;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.config.GuiTextListConfig;
import fr.atesab.atehud.superclass.Attribute;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.superclass.EnchantmentInfo;
import fr.atesab.atehud.superclass.Enchantments;
import fr.atesab.atehud.utils.GiveItemUtils;
import fr.atesab.atehud.utils.GuiUtils;
import fr.atesab.atehud.utils.ItemHelp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.item.ItemStack;

public class GuiItemFactory extends GuiScreen {
	public GuiScreen Last = null;
	public String StockName = "", StockMeta = "", StockItem = "";
	public boolean unbreakable = false;
	public GuiTextField name, meta, item;
	public GuiButton unbreak;
	public EnchantmentInfo[] enchantments = new EnchantmentInfo[] {};
	public String[] lores = new String[] {};
	public Attribute[] attributes = new Attribute[] {};

	public GuiItemFactory() {
	}

	public GuiItemFactory(GuiScreen last) {
		Last = last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		ItemStack is = null;
		switch (button.id) {
		case 0:// add
			ModMain.addConfig("AdvancedItem", item.getText() + " 1 " + meta.getText() + " " + getNbt());
			Chat.show(I18n.format("gui.act.add.msg"));
			break;
		case 1:// Give
			try {
				is = ItemHelp.getGive(item.getText() + " 1 " + meta.getText() + " " + getNbt());
				GiveItemUtils.give(mc, is);
			} catch (NumberInvalidException e) {
				Chat.error(I18n.format("gui.act.give.fail2"));
			}
			break;
		case 2:// Cancel/Done
			Minecraft.getMinecraft().displayGuiScreen(Last);
			break;
		case 3:// Enchants
			Minecraft.getMinecraft().displayGuiScreen(new GuiEnchantSelector(this));
			break;
		case 4:// Attribute
			Minecraft.getMinecraft().displayGuiScreen(new GuiAttributeSelector(this));
			break;
		case 5:// Lore
			Minecraft.getMinecraft()
					.displayGuiScreen(new GuiTextListConfig(this, lores, I18n.format("gui.act.itemfactory.lores")) {
						public void onUpdateValue(String[] value) {
							((GuiItemFactory) Last).lores = value;
						}
					});
			break;
		case 6:
			unbreakable = !unbreakable;
			break;
		}
		super.actionPerformed(button);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		StockItem = item.getText();
		StockMeta = meta.getText();
		StockName = name.getText();
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		meta.drawTextBox();
		name.drawTextBox();
		item.drawTextBox();
		GuiUtils.drawRightString(fontRendererObj, " : ", width / 2 + 122, height / 2 - 19, 20, Colors.WHITE);
		GuiUtils.drawRightString(fontRendererObj, I18n.format("gui.act.fwf.name") + " = ", width / 2 - 148,
				height / 2 - 40, 20, Colors.GOLD);
		GuiUtils.drawRightString(fontRendererObj, I18n.format("gui.act.itemfactory.itemid") + " = ", width / 2 - 148,
				height / 2 - 20, 20, Colors.GOLD);
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (!mc.thePlayer.capabilities.isCreativeMode
				&& GuiUtils.isHover(width / 2 - 50, height / 2 + 42, 99, 20, mouseX, mouseY))
			GuiUtils.drawTextBox(this, mc, fontRendererObj, new String[] { I18n.format("gui.act.nocreative") }, mouseX,
					mouseY, Colors.RED);
	}

	public String getAttributeNbt() {
		String str = "[";
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i] != null)
				str += "," + attributes[i].getAttriNbt();
		}
		return str.replaceFirst(",", "") + "]";
	}

	public String getEnchNbt() {
		String str = "[";
		for (int i = 0; i < enchantments.length; i++) {
			if (enchantments[i] != null)
				str += "," + enchantments[i].getEnchNbt();
		}
		return str.replaceFirst(",", "") + "]";
	}

	public String getLoreNbt() {
		String[] lores2 = lores;
		int j = 0;
		for (int i = 0; i < lores2.length; i++) {
			if (!lores2[i].isEmpty()) {
				lores2[j] = lores[i].replaceAll("&&", "\u00a7");
				j++;
			}
		}
		for (int i = j; i < lores2.length; i++) {
			lores2[i] = null;
		}
		String str = "[";
		for (int i = 0; i < lores2.length; i++) {
			if (lores2[i] != null)
				str += ",\"" + lores2[i] + "\"";
		}
		return str.replaceFirst(",", "") + "]";
	}

	public String getNbt() {
		String nameout = name.getText().replaceAll("&&", "\u00a7");
		String unb = "";
		if (unbreakable)
			unb = "Unbreakable:1,";
		return "{" + unb + "display:{Name:\"" + name.getText() + "\",Lore:" + getLoreNbt() + "},AttributeModifiers:"
				+ getAttributeNbt() + ",ench:" + getEnchNbt() + "}";
	}

	public void initGui() {
		name = new GuiTextField(6, fontRendererObj, width / 2 - 148, height / 2 - 40, 296, 16);
		item = new GuiTextField(6, fontRendererObj, width / 2 - 148, height / 2 - 19,
				296 - 30 - fontRendererObj.getStringWidth(" : "), 16);
		meta = new GuiTextField(6, fontRendererObj, width / 2 + 122, height / 2 - 19, 26, 16);
		name.setText(StockName);
		meta.setText(StockMeta);
		item.setText(StockItem);
		buttonList
				.add(new GuiButton(5, width / 2 - 150, height / 2, 149, 20, I18n.format("gui.act.itemfactory.lores")));
		unbreak = new GuiButton(6, width / 2, height / 2, 150, 20,
				I18n.format("item.unbreakable").replaceAll(Chat.c_Modifier + "[a-fk-or0-9]", ""));
		buttonList.add(unbreak);
		buttonList.add(new GuiButton(4, width / 2 - 150, height / 2 + 21, 149, 20,
				I18n.format("gui.act.itemfactory.attributes")));
		buttonList.add(
				new GuiButton(3, width / 2, height / 2 + 21, 150, 20, I18n.format("gui.act.itemfactory.enchants")));
		buttonList.add(new GuiButton(2, width / 2 - 150, height / 2 + 42, 99, 20, I18n.format("gui.done")));
		buttonList.add(new GuiButton(1, width / 2 - 50, height / 2 + 42, 99, 20, I18n.format("gui.act.give")));
		buttonList.add(new GuiButton(0, width / 2 + 50, height / 2 + 42, 100, 20, I18n.format("gui.act.add")));
		super.initGui();
	}

	protected void keyTyped(char par1, int par2) throws IOException {
		name.textboxKeyTyped(par1, par2);
		meta.textboxKeyTyped(par1, par2);
		item.textboxKeyTyped(par1, par2);
		super.keyTyped(par1, par2);
	}

	protected void mouseClicked(int x, int y, int btn) throws IOException {
		name.mouseClicked(x, y, btn);
		meta.mouseClicked(x, y, btn);
		item.mouseClicked(x, y, btn);
		super.mouseClicked(x, y, btn);
	}

	public void updateScreen() {
		unbreak.packedFGColour = Colors.redGreenOptionInt(unbreakable);
		name.updateCursorCounter();
		meta.updateCursorCounter();
		item.updateCursorCounter();
	}
}
