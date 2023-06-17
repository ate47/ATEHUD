package fr.atesab.atehud.overlay.module;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.GuiHudOptions;
import fr.atesab.atehud.overlay.Module;
import fr.atesab.atehud.overlay.Overlay.position;
import fr.atesab.atehud.superclass.ModuleConfig;
import fr.atesab.atehud.utils.GuiUtils;
import fr.atesab.atehud.utils.ItemHelp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Armor extends Module {
	public static final String[] enumArmorPart = {"helmet", "chest", "leggings", "boots", "soup", "arrow", "hand", "armor"};
	public ArrayList<ModuleConfig> getConfigs(ArrayList<ModuleConfig> list) {
		list.add(new ModuleConfig("armor.part", ModuleConfig.ModuleType.ENUM, enumArmorPart));
		list.add(new ModuleConfig("armor.name", ModuleConfig.ModuleType.BOOLEAN, "true"));
		list.add(new ModuleConfig("armor.enchant", ModuleConfig.ModuleType.BOOLEAN, "true"));
		list.add(new ModuleConfig("armor.durability", ModuleConfig.ModuleType.BOOLEAN, "true"));
		list.add(new ModuleConfig("armor.count", ModuleConfig.ModuleType.BOOLEAN, "true"));
		return list;
	}

	public String getDisplayName(String[] args) {
		for (int i = 0; i < enumArmorPart.length; i++) {
			if(args[0].equals(enumArmorPart[i]))return I18n.format("atehud.config.overlay.module.armor.part.value."+i);
		}
		return "";
	}

	public String getModuleId() {
		return "armor";
	}

	public int moduleSizeX(String[] args) {
		String[] dmgs = new String[5];
		ItemStack[] itemsToDisplay = new ItemStack[1];
		boolean showName = true;
		boolean ShowEnchant = true;
		boolean showDurability = true;
		boolean showCount = true;
		if(args.length==5) {
			if(args[0].equals("helmet")) {
				itemsToDisplay[0] = mc.thePlayer.inventory.armorInventory[3];
			} else if(args[0].equals("chest")) {
				itemsToDisplay[0] = mc.thePlayer.inventory.armorInventory[2];
			} else if(args[0].equals("leggings")) {
				itemsToDisplay[0] = mc.thePlayer.inventory.armorInventory[1];
			} else if(args[0].equals("boots")) {
				itemsToDisplay[0] = mc.thePlayer.inventory.armorInventory[0];
			} else if(args[0].equals("soup")) {
				itemsToDisplay[0] = new ItemStack(Items.mushroom_stew);
			} else if(args[0].equals("arrow")) {
				itemsToDisplay[0] = new ItemStack(Items.arrow);
			} else if(args[0].equals("hand")) {
				itemsToDisplay[0] = mc.thePlayer.getEquipmentInSlot(0);
			}else if(args[0].equals("armor")) {
				itemsToDisplay = mc.thePlayer.inventory.armorInventory;
			}
			try {showName = Boolean.valueOf(args[1]);} catch (Exception e) {}
			try {ShowEnchant = Boolean.valueOf(args[2]);} catch (Exception e) {}
			try {showDurability = Boolean.valueOf(args[3]);} catch (Exception e) {}
			try {showCount = Boolean.valueOf(args[4]);} catch (Exception e) {}
		} else return 0;
		int iconSizeX = 20;
		int elemSizeX = 0;
		int displayedItem = 0;

		for (int i = 0; i < itemsToDisplay.length; i++) {
			if (itemsToDisplay[i] != null) {
				List<ItemHelp.ColoredTextInformation> tor = ItemHelp.getInfoItem(itemsToDisplay[i], mc, showName, ShowEnchant, showDurability, showCount);
				for (ItemHelp.ColoredTextInformation t: tor)
					elemSizeX = Math.max(mc.fontRendererObj.getStringWidth(t.text), elemSizeX);
			}
		}
		return iconSizeX+elemSizeX;
	}

	public int moduleSizeY(String[] args) {
		ItemStack[] itemsToDisplay = new ItemStack[1];
		boolean showName = true;
		boolean ShowEnchant = true;
		boolean showDurability = true;
		boolean showCount = true;
		if(args.length==5) {
			if(args[0].equals("helmet")) {
				itemsToDisplay[0] = mc.thePlayer.inventory.armorInventory[3];
			} else if(args[0].equals("chest")) {
				itemsToDisplay[0] = mc.thePlayer.inventory.armorInventory[2];
			} else if(args[0].equals("leggings")) {
				itemsToDisplay[0] = mc.thePlayer.inventory.armorInventory[1];
			} else if(args[0].equals("boots")) {
				itemsToDisplay[0] = mc.thePlayer.inventory.armorInventory[0];
			} else if(args[0].equals("soup")) {
				itemsToDisplay[0] = new ItemStack(Items.mushroom_stew);
			} else if(args[0].equals("arrow")) {
				itemsToDisplay[0] = new ItemStack(Items.arrow);
			} else if(args[0].equals("hand")) {
				itemsToDisplay[0] = mc.thePlayer.getEquipmentInSlot(0);
			}else if(args[0].equals("armor")) {
				itemsToDisplay = mc.thePlayer.inventory.armorInventory;
			}
			try {showName = Boolean.valueOf(args[1]);} catch (Exception e) {}
			try {ShowEnchant = Boolean.valueOf(args[2]);} catch (Exception e) {}
			try {showDurability = Boolean.valueOf(args[3]);} catch (Exception e) {}
			try {showCount = Boolean.valueOf(args[4]);} catch (Exception e) {}
		} else return 0;
		int elemSizeY = 0;
		if(showName)elemSizeY+=mc.fontRendererObj.FONT_HEIGHT + 1;
		if(showDurability)elemSizeY+=mc.fontRendererObj.FONT_HEIGHT + 1;
		if(ShowEnchant)elemSizeY+=mc.fontRendererObj.FONT_HEIGHT + 1;
		if (elemSizeY < 20)
			elemSizeY = 20;
		int sizeY = 0;
		for (ItemStack is: itemsToDisplay)if(is!=null && ItemHelp.getCount(mc, is.getItem())!=0)sizeY+=elemSizeY;
		return sizeY;
	}
	public int renderModule(int posX, int posY, String[] args) {
		RenderItem itemRenderer = mc.getRenderItem();
		FontRenderer fontRender = mc.fontRendererObj;
		itemRenderer.zLevel = 200.0F;
		ItemStack[] itemsToDisplay = new ItemStack[1];
		boolean showName = true;
		boolean ShowEnchant = true;
		boolean showDurability = true;
		boolean showCount = true;
		if(args.length==5) {
			if(args[0].equals("helmet")) {
				itemsToDisplay[0] = mc.thePlayer.inventory.armorInventory[3];
			} else if(args[0].equals("chest")) {
				itemsToDisplay[0] = mc.thePlayer.inventory.armorInventory[2];
			} else if(args[0].equals("leggings")) {
				itemsToDisplay[0] = mc.thePlayer.inventory.armorInventory[1];
			} else if(args[0].equals("boots")) {
				itemsToDisplay[0] = mc.thePlayer.inventory.armorInventory[0];
			} else if(args[0].equals("soup")) {
				itemsToDisplay[0] = new ItemStack(Items.mushroom_stew);
			} else if(args[0].equals("arrow")) {
				itemsToDisplay[0] = new ItemStack(Items.arrow);
			} else if(args[0].equals("hand")) {
				itemsToDisplay[0] = mc.thePlayer.getEquipmentInSlot(0);
			}else if(args[0].equals("armor")) {
				itemsToDisplay = mc.thePlayer.inventory.armorInventory;
			}
			try {showName = Boolean.valueOf(args[1]);} catch (Exception e) {}
			try {ShowEnchant = Boolean.valueOf(args[2]);} catch (Exception e) {}
			try {showDurability = Boolean.valueOf(args[3]);} catch (Exception e) {}
			try {showCount = Boolean.valueOf(args[4]);} catch (Exception e) {}
		} else return 0;
		int lastElement = 0;

		int iconSizeX = 20;
		int elemSizeY = 0;
		if(showName)elemSizeY+=mc.fontRendererObj.FONT_HEIGHT + 1;
		if(showDurability)elemSizeY+=mc.fontRendererObj.FONT_HEIGHT + 1;
		if(ShowEnchant)elemSizeY+=mc.fontRendererObj.FONT_HEIGHT + 1;
		if (elemSizeY < 20)
			elemSizeY = 20;
		for (int i = 0; i < itemsToDisplay.length; i++) {
			if (itemsToDisplay[i] != null && ItemHelp.getCount(mc, itemsToDisplay[i].getItem())!=0) {
				elemSizeY = 20;
				int b = 0;
				List<ItemHelp.ColoredTextInformation> tor = ItemHelp.getInfoItem(itemsToDisplay[i], mc, showName, ShowEnchant, showDurability, showCount);
				for (Iterator iterator = tor.iterator(); iterator.hasNext();) {
					ItemHelp.ColoredTextInformation inf = (ItemHelp.ColoredTextInformation) iterator.next();
					fontRender.drawStringWithShadow(inf.text, iconSizeX + posX, posY + lastElement + (b), inf.color);
					b += (fontRender.FONT_HEIGHT + 1);
				}
				if (elemSizeY < b)
					elemSizeY = b;
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				RenderHelper.enableGUIStandardItemLighting();
				itemRenderer.renderItemAndEffectIntoGUI(itemsToDisplay[i], posX,
						posY + lastElement + elemSizeY / 2 - 10);
				itemRenderer.renderItemOverlays(fontRender, itemsToDisplay[i], posX,
						posY + lastElement + elemSizeY / 2 - 10);
				RenderHelper.disableStandardItemLighting();
				lastElement += elemSizeY;
			}
		}
		return lastElement;
	}
}
