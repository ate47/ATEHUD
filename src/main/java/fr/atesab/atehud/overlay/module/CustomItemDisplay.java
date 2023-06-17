package fr.atesab.atehud.overlay.module;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import fr.atesab.atehud.overlay.Module;
import fr.atesab.atehud.superclass.ModuleConfig;
import fr.atesab.atehud.superclass.ModuleConfig.ModuleType;
import fr.atesab.atehud.utils.ItemHelp;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CustomItemDisplay extends Module {

	@Override
	public ArrayList<ModuleConfig> getConfigs(ArrayList<ModuleConfig> list) {
		list.add(new ModuleConfig("item.nbt", ModuleType.STRING, "diamond 1 0 "));
		list.add(new ModuleConfig("item.name", ModuleConfig.ModuleType.BOOLEAN, "true"));
		list.add(new ModuleConfig("item.enchant", ModuleConfig.ModuleType.BOOLEAN, "true"));
		list.add(new ModuleConfig("item.durability", ModuleConfig.ModuleType.BOOLEAN, "true"));
		list.add(new ModuleConfig("item.count", ModuleConfig.ModuleType.BOOLEAN, "true"));
		list.add(new ModuleConfig("item.displayifempty", ModuleConfig.ModuleType.BOOLEAN, "true"));
		return list;
	}

	@Override
	public String getDisplayName(String[] args) {
		try {ItemHelp.getGive(args[0]).getDisplayName();} catch (Exception e) {}
		return "";
	}

	@Override
	public String getModuleId() {
		return "item";
	}

	@Override
	public int moduleSizeX(String[] args) {
		ItemStack itemsToDisplay = null;
		boolean showName = true;
		boolean ShowEnchant = true;
		boolean showDurability = true;
		boolean showCount = true;
		boolean displayifempty = true;
		if(args.length==6) {
			try {itemsToDisplay = ItemHelp.getGive(args[0]);} catch (Exception e) {}
			try {showName = Boolean.valueOf(args[1]);} catch (Exception e) {}
			try {ShowEnchant = Boolean.valueOf(args[2]);} catch (Exception e) {}
			try {showDurability = Boolean.valueOf(args[3]);} catch (Exception e) {}
			try {showCount = Boolean.valueOf(args[4]);} catch (Exception e) {}
			try {displayifempty = Boolean.valueOf(args[5]);} catch (Exception e) {}
		} else return 0;
		if(itemsToDisplay!=null) {
			if((ItemHelp.getCount(mc, itemsToDisplay.getItem())==0 && !displayifempty)) return 0;
			int elemSizeX = 0;
			List<ItemHelp.ColoredTextInformation> tor = ItemHelp.getInfoItem(itemsToDisplay, mc, showName, ShowEnchant, showDurability, showCount);
			for (ItemHelp.ColoredTextInformation t: tor)
				elemSizeX = Math.max(mc.fontRendererObj.getStringWidth(t.text), elemSizeX);
			return elemSizeX + 20;
		}
		return 0;
	}

	@Override
	public int moduleSizeY(String[] args) {
		ItemStack itemsToDisplay = null;
		boolean showName = true;
		boolean ShowEnchant = true;
		boolean showDurability = true;
		boolean showCount = true;
		boolean displayifempty = true;
		if(args.length==6) {
			try {itemsToDisplay = ItemHelp.getGive(args[0]);} catch (Exception e) {}
			try {showName = Boolean.valueOf(args[1]);} catch (Exception e) {}
			try {ShowEnchant = Boolean.valueOf(args[2]);} catch (Exception e) {}
			try {showDurability = Boolean.valueOf(args[3]);} catch (Exception e) {}
			try {showCount = Boolean.valueOf(args[4]);} catch (Exception e) {}
			try {displayifempty = Boolean.valueOf(args[5]);} catch (Exception e) {}
		} else return 0;
		if(itemsToDisplay!=null) {
			if((ItemHelp.getCount(mc, itemsToDisplay.getItem())==0 && !displayifempty)) return 0;
			int elemSizeY = 0;
			if(showName)elemSizeY+=mc.fontRendererObj.FONT_HEIGHT + 1;
			if(showDurability)elemSizeY+=mc.fontRendererObj.FONT_HEIGHT + 1;
			if(ShowEnchant)elemSizeY+=mc.fontRendererObj.FONT_HEIGHT + 1;
			if (elemSizeY < 20)
				elemSizeY = 20;
			return elemSizeY;
		}
		return 0;
	}

	@Override
	public int renderModule(int posX, int posY, String[] args) {
		RenderItem itemRenderer = mc.getRenderItem();
		FontRenderer fontRender = mc.fontRendererObj;
		itemRenderer.zLevel = 200.0F;
		ItemStack itemsToDisplay = null;
		boolean showName = true;
		boolean ShowEnchant = true;
		boolean showDurability = true;
		boolean showCount = true;
		boolean displayifempty = true;
		if(args.length==6) {
			try {itemsToDisplay = ItemHelp.getGive(args[0]);} catch (Exception e) {}
			try {showName = Boolean.valueOf(args[1]);} catch (Exception e) {}
			try {ShowEnchant = Boolean.valueOf(args[2]);} catch (Exception e) {}
			try {showDurability = Boolean.valueOf(args[3]);} catch (Exception e) {}
			try {showCount = Boolean.valueOf(args[4]);} catch (Exception e) {}
			try {displayifempty = Boolean.valueOf(args[5]);} catch (Exception e) {}
		} else return 0;

		int lastElement = 0;
		int iconSizeX = 20;
		int elemSizeY = 0;
		if(showName)elemSizeY+=mc.fontRendererObj.FONT_HEIGHT + 1;
		if(showDurability)elemSizeY+=mc.fontRendererObj.FONT_HEIGHT + 1;
		if(ShowEnchant)elemSizeY+=mc.fontRendererObj.FONT_HEIGHT + 1;
		if (elemSizeY < 20)
			elemSizeY = 20;
		if(itemsToDisplay!=null) {
			if((ItemHelp.getCount(mc, itemsToDisplay.getItem())==0 && !displayifempty)) return 0;
			elemSizeY = 20;
			int b = 0;
			List<ItemHelp.ColoredTextInformation> tor = ItemHelp.getInfoItem(itemsToDisplay, mc, showName, ShowEnchant, showDurability, showCount);
			for (Iterator iterator = tor.iterator(); iterator.hasNext();) {
				ItemHelp.ColoredTextInformation inf = (ItemHelp.ColoredTextInformation) iterator.next();
				fontRender.drawStringWithShadow(inf.text, iconSizeX + posX, posY + lastElement + (b), inf.color);
				b += (fontRender.FONT_HEIGHT + 1);
			}
			if (elemSizeY < b)
				elemSizeY = b;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			RenderHelper.enableGUIStandardItemLighting();
			itemRenderer.renderItemAndEffectIntoGUI(itemsToDisplay, posX,
					posY + lastElement + elemSizeY / 2 - 10);
			itemRenderer.renderItemOverlays(fontRender, itemsToDisplay, posX,
					posY + lastElement + elemSizeY / 2 - 10);
			RenderHelper.disableStandardItemLighting();
			lastElement += elemSizeY;
		}
		return lastElement;
	}

}
