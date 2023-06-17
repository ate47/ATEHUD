package fr.atesab.atehud.overlay;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.GuiHudOptions;
import fr.atesab.atehud.gui.GuiMenu;
import fr.atesab.atehud.overlay.Overlay.ModuleConfig;
import fr.atesab.atehud.overlay.Overlay.position;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.ts3.TS3Socket;
import fr.atesab.atehud.utils.GuiUtils;
import fr.atesab.atehud.utils.ItemHelp;

public class OverlayDisplayManager {
	private static ScaledResolution scaledResolution;
	/*
	 * Old stuff => To delete
	 */
	private static void displayArmorHUD(Minecraft mc, RenderItem itemRenderer, FontRenderer fontRender) {
		if (ModMain.hudArmorLocation == GuiHudOptions.NONE)
			return;
		// test items
		String[] dmgs = new String[5];
		int[] dmgsColor = new int[5];
		ItemStack[] itemsToDisplay = new ItemStack[5];
		if (ModMain.hudArmorLocation == GuiHudOptions.TOPCENTER || ModMain.hudArmorLocation == GuiHudOptions.TOPLEFT
				|| ModMain.hudArmorLocation == GuiHudOptions.TOPRIGHT) {
			itemsToDisplay[0] = mc.thePlayer.getCurrentEquippedItem();
			itemsToDisplay[1] = mc.thePlayer.inventory.armorInventory[3];
			itemsToDisplay[2] = mc.thePlayer.inventory.armorInventory[2];
			itemsToDisplay[3] = mc.thePlayer.inventory.armorInventory[1];
			itemsToDisplay[4] = mc.thePlayer.inventory.armorInventory[0];
		} else {
			itemsToDisplay[4] = mc.thePlayer.getCurrentEquippedItem();
			itemsToDisplay[3] = mc.thePlayer.inventory.armorInventory[3];
			itemsToDisplay[2] = mc.thePlayer.inventory.armorInventory[2];
			itemsToDisplay[1] = mc.thePlayer.inventory.armorInventory[1];
			itemsToDisplay[0] = mc.thePlayer.inventory.armorInventory[0];
		}
		int iconSizeX = 20;
		int elemSizeX = iconSizeX;
		int elemSizeY = (mc.fontRendererObj.FONT_HEIGHT + 1) * 3;
		if (elemSizeY < 20)
			elemSizeY = 20;
		int displayedItem = 0;
		for (int i = 0; i < itemsToDisplay.length; i++) {
			if (itemsToDisplay[i] != null) {
				int newElemSizeX = fontRender.getStringWidth(dmgs[i]);
				int a = fontRender.getStringWidth(itemsToDisplay[i].getDisplayName());
				if (newElemSizeX < a)
					newElemSizeX = a;
				if (GuiUtils.getEnch(itemsToDisplay[i]) != null)
					a = fontRender.getStringWidth(GuiUtils.getEnch(itemsToDisplay[i]));
				if (newElemSizeX < a)
					newElemSizeX = a;
				if (elemSizeX < newElemSizeX)
					elemSizeX = newElemSizeX;
			}
		}
		int locationX = 0;
		int locationY = 0;
		int width = scaledResolution.getScaledWidth();
		int height = scaledResolution.getScaledHeight();
		switch (ModMain.hudArmorLocation) {
		case GuiHudOptions.TOPLEFT:
			locationX = 0;
			locationY = 0;
			break;
		case GuiHudOptions.TOPCENTER:
			locationX = width / 2 - (elemSizeX + iconSizeX) / 2;
			locationY = 0;
			break;
		case GuiHudOptions.TOPRIGHT:
			locationX = width - (elemSizeX + iconSizeX);
			locationY = 0;
			break;
		case GuiHudOptions.MIDDLELEFT:
			locationX = 0;
			elemSizeY = 0 - elemSizeY;
			locationY = height / 2 + elemSizeY * 4;
			break;
		case GuiHudOptions.MIDDLECENTER:
			elemSizeY = 0 - elemSizeY;
			locationX = width / 2 - (elemSizeX + iconSizeX) / 2;
			locationY = height / 2 + elemSizeY * 4;
			break;
		case GuiHudOptions.MIDDLERIGHT:
			elemSizeY = 0 - elemSizeY;
			locationX = width - (elemSizeX + iconSizeX);
			locationY = height / 2 + elemSizeY * 4;
			break;
		case GuiHudOptions.BOTTOMLEFT:
			locationX = 0;
			elemSizeY = 0 - elemSizeY;
			locationY = height + elemSizeY;
			break;
		case GuiHudOptions.BOTTOMCENTER:
			elemSizeY = 0 - elemSizeY;
			locationX = width / 2 - (elemSizeX + iconSizeX) / 2;
			locationY = height + elemSizeY;
			break;
		case GuiHudOptions.BOTTOMRIGHT:
			elemSizeY = 0 - elemSizeY;
			locationX = width - elemSizeX - iconSizeX;
			locationY = height + elemSizeY;
			break;
		}
		int lastElement = 1;
		for (int i = 0; i < itemsToDisplay.length; i++) {
			if (itemsToDisplay[i] != null) {
				elemSizeY = 20;
				int b = 0;
				List<ItemHelp.ColoredTextInformation> tor = ItemHelp.getInfoItem(itemsToDisplay[i], mc);
				for (Iterator iterator = tor.iterator(); iterator.hasNext();) {
					ItemHelp.ColoredTextInformation inf = (ItemHelp.ColoredTextInformation) iterator.next();
					fontRender.drawStringWithShadow(inf.text, iconSizeX + locationX, locationY + lastElement + (b),
							inf.color);
					b += (fontRender.FONT_HEIGHT + 1);
				}
				if (elemSizeY < b)
					elemSizeY = b;
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				RenderHelper.enableGUIStandardItemLighting();
				itemRenderer.renderItemAndEffectIntoGUI(itemsToDisplay[i], locationX,
						locationY + lastElement + elemSizeY / 2 - 10);
				itemRenderer.renderItemOverlays(fontRender, itemsToDisplay[i], locationX,
						locationY + lastElement + elemSizeY / 2 - 10);
				RenderHelper.disableStandardItemLighting();
				lastElement += elemSizeY;
			}
		}

	}
	/*
	 * Old stuff => To delete
	 */
	private static void displayEffectHUD(Minecraft mc, RenderItem itemRenderer, FontRenderer fontRender) {
		if (ModMain.hudEffectLocation == GuiHudOptions.NONE)
			return;
		Collection collection = mc.thePlayer.getActivePotionEffects();

		int i = 0; // posX
		int j = 0; // posY
		int k = 20; // sizeY of the element
		int l = 128; // sizeX of the element
		int iconSize = 20;

		int locationX = 0;
		int locationY = 0;
		int width = scaledResolution.getScaledWidth();
		int height = scaledResolution.getScaledHeight();
		switch (ModMain.hudEffectLocation) {
		case GuiHudOptions.TOPLEFT:
			locationX = 0;
			locationY = 0;
			break;
		case GuiHudOptions.TOPCENTER:
			locationX = width / 2 - (l + iconSize) / 2;
			locationY = 0;
			break;
		case GuiHudOptions.TOPRIGHT:
			locationX = width - (l + iconSize);
			locationY = 0;
			break;
		case GuiHudOptions.MIDDLELEFT:
			locationX = 0;
			k = 0 - k;
			locationY = height / 2 + k;
			break;
		case GuiHudOptions.MIDDLECENTER:
			k = 0 - k;
			locationX = width / 2 - (l + iconSize) / 2;
			locationY = height / 2 + k;
			break;
		case GuiHudOptions.MIDDLERIGHT:
			k = 0 - k;
			locationX = width - (l + iconSize);
			locationY = height / 2 + k;
			break;
		case GuiHudOptions.BOTTOMLEFT:
			locationX = 0;
			k = 0 - k;
			locationY = height + k;
			break;
		case GuiHudOptions.BOTTOMCENTER:
			k = 0 - k;
			locationX = width / 2 - (l + iconSize) / 2;
			locationY = height + k;
			break;
		case GuiHudOptions.BOTTOMRIGHT:
			k = 0 - k;
			locationX = width - l - iconSize;
			locationY = height + k;
			break;
		}
		if (!collection.isEmpty()) {
			for (Iterator iterator = mc.thePlayer.getActivePotionEffects().iterator(); iterator.hasNext(); j += k) {
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.disableLighting();
				PotionEffect potioneffect = (PotionEffect) iterator.next();
				Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
				String s1 = I18n.format(potion.getName(), new Object[0]);
				if (potioneffect.getAmplifier() == 1) {
					s1 = s1 + " " + I18n.format("enchantment.level.2", new Object[0]);
				} else if (potioneffect.getAmplifier() == 2) {
					s1 = s1 + " " + I18n.format("enchantment.level.3", new Object[0]);
				} else if (potioneffect.getAmplifier() == 3) {
					s1 = s1 + " " + I18n.format("enchantment.level.4", new Object[0]);
				}
				String s = Potion.getDurationString(potioneffect);

				if (potion.hasStatusIcon()) {
					int m = potion.getStatusIconIndex();
					mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
					drawTexturedModalRect(itemRenderer, locationX + i, locationY + j, 0 + m % 8 * 18, 198 + m / 8 * 18,
							18, 18);
					potion.renderInventoryEffect(locationX + i, locationY + j, potioneffect, mc);
				}
				if (!potion.shouldRenderInvText(potioneffect))
					continue;
				fontRender.drawStringWithShadow(s1, locationX + (float) (i + iconSize + 2), locationY + (float) (j),
						ModMain.getTextColor().getRGB());
				fontRender.drawStringWithShadow(s, locationX + (float) (i + iconSize + 2), locationY + (float) (j + 10),
						8355711);
			}
		}
	}
	private static void displayOverlays(Minecraft mc, FontRenderer fontRender, RenderGameOverlayEvent ev) {
		Map<Overlay.position, List<Overlay>> map = new HashMap<Overlay.position, List<Overlay>>();
		for (position p: position.class.getEnumConstants())
			map.put(p, new ArrayList<Overlay>());
		for (Overlay ov: ModMain.overlayList)
			map.get(ov.pos).add(ov);
		map.remove(position.hide);
		for (position p: map.keySet()) {
			int sizeX = 0;
			int sizeY = 0;
			for (Overlay o: map.get(p)) {
				if(!o.showInDebug && ev.type.equals(ElementType.DEBUG)) continue;
				for (ModuleConfig mlc: o.modules) {
					Module m = Module.getModuleById(mlc.id);
					if(m!=null) {
						sizeX=Math.max(sizeX, m.moduleSizeX(mlc.args));
						sizeY+=m.moduleSizeY(mlc.args);
					} else System.out.println("Can't find the module \""+mlc.id+"\"");
				}
				if(o.showTitle && sizeY!=0) {
					sizeY+=2+fontRender.FONT_HEIGHT;
					sizeX=Math.max(sizeX, fontRender.getStringWidth(Chat.BOLD+Chat.UNDERLINE+o.title.replace('&', Chat.c_Modifier)));
				}
			}
			int posY = p.getDraw().getPosY(sizeY, scaledResolution.getScaledHeight());
			for (Overlay o: map.get(p)) {
				if(!o.showInDebug && mc.gameSettings.showDebugInfo) continue;
				if(o.showTitle && sizeY!=0) {
					String t = Chat.BOLD+Chat.UNDERLINE+o.title.replace('&', Chat.c_Modifier);
					int titleSizeX = fontRender.getStringWidth(t);
					sizeX=Math.max(sizeX, titleSizeX);
					fontRender.drawString(t, o.pos.getDraw().getPosX(titleSizeX, scaledResolution.getScaledWidth()), posY, 
							ModMain.getGuiColor().getRGB());
					posY+=2+fontRender.FONT_HEIGHT;
				}
				for (ModuleConfig mlc: o.modules) {
					Module m = Module.getModuleById(mlc.id);
					if(m!=null)
						posY+=m.renderModule(p.getDraw().getPosX(m.moduleSizeX(mlc.args), scaledResolution.getScaledWidth()), posY, mlc.args);
				}
			}
		}
	}
	/*
	 * Old stuff => To delete
	 */
	private static void displayPlayerHUD(Minecraft mc, FontRenderer fontRender, RenderGameOverlayEvent ev) {
		if (ModMain.hudPlayerLocation == GuiHudOptions.NONE)
			return;
		int locationX = 0, locationY = 0, angle = 50, width = scaledResolution.getScaledWidth(),
				height = scaledResolution.getScaledHeight();
		switch (ModMain.hudPlayerLocation) {
		case GuiHudOptions.TOPLEFT:
			locationX = 30;
			locationY = 100;
			angle = -50;
			break;
		case GuiHudOptions.TOPCENTER:
			locationX = width / 2 - 25;
			locationY = 100;
			angle = 0;
			break;
		case GuiHudOptions.TOPRIGHT:
			locationX = width - 50;
			locationY = 100;
			break;
		case GuiHudOptions.MIDDLELEFT:
			locationX = 30;
			locationY = height / 2 + 50;
			angle = -50;
			break;
		case GuiHudOptions.MIDDLECENTER:
			locationX = width / 2 - 25;
			locationY = height / 2 + 50;
			angle = 0;
			break;
		case GuiHudOptions.MIDDLERIGHT:
			locationX = width - 50;
			locationY = height / 2 + 50;
			break;
		case GuiHudOptions.BOTTOMLEFT:
			locationX = 30;
			locationY = height;
			angle = -50;
			break;
		case GuiHudOptions.BOTTOMCENTER:
			locationX = width / 2 - 25;
			locationY = height;
			angle = 0;
			break;
		case GuiHudOptions.BOTTOMRIGHT:
			locationX = width - 50;
			locationY = height;
			break;
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GuiInventory.drawEntityOnScreen(locationX, locationY, 50, angle, 0, mc.thePlayer);
	}
	/*
	 * Old stuff => To delete
	 */
	private static void displayTeamSpeak3HUD(Minecraft mc, RenderItem itemRenderer, FontRenderer fontRender) {
		if (ModMain.hudTS3Location == GuiHudOptions.NONE)
			return;
		if (!ModMain.genTS3)
			return;
		int locationX = 0;
		int locationY = 0;
		int width = scaledResolution.getScaledWidth();
		int height = scaledResolution.getScaledHeight();
		int lineSize = 8;
		if (mc.fontRendererObj.FONT_HEIGHT > lineSize)
			lineSize = mc.fontRendererObj.FONT_HEIGHT;
		int elemSizeX = mc.fontRendererObj.getStringWidth(TS3Socket.currentChannel.channel_name) + 10,
				elemSizeY = lineSize * (TS3Socket.clients.size() + 1) + 1;
		for (int i = 0; i < TS3Socket.clients.size(); i++) {
			if (mc.fontRendererObj.getStringWidth(TS3Socket.clients.get(i).client_nickname) + 10 > elemSizeX)
				elemSizeX = mc.fontRendererObj.getStringWidth(TS3Socket.clients.get(i).client_nickname) + 10;
		}
		switch (ModMain.hudTS3Location) {
		case GuiHudOptions.TOPLEFT:
			locationX = 0;
			locationY = 0;
			break;
		case GuiHudOptions.TOPCENTER:
			locationX = width / 2 - (elemSizeX) / 2;
			locationY = 0;
			break;
		case GuiHudOptions.TOPRIGHT:
			locationX = width - (elemSizeX);
			locationY = 0;
			break;
		case GuiHudOptions.MIDDLELEFT:
			locationX = 0;
			elemSizeY = 0 - elemSizeY;
			locationY = height / 2 - elemSizeY / 2;
			break;
		case GuiHudOptions.MIDDLECENTER:
			elemSizeY = 0 - elemSizeY;
			locationX = width / 2 - (elemSizeX) / 2;
			locationY = height / 2 - elemSizeY / 2;
			break;
		case GuiHudOptions.MIDDLERIGHT:
			elemSizeY = 0 - elemSizeY;
			locationX = width - (elemSizeX);
			locationY = height / 2 - elemSizeY / 2;
			break;
		case GuiHudOptions.BOTTOMLEFT:
			locationX = 0;
			elemSizeY = 0 - elemSizeY;
			locationY = height + elemSizeY;
			break;
		case GuiHudOptions.BOTTOMCENTER:
			elemSizeY = 0 - elemSizeY;
			locationX = width / 2 - (elemSizeX) / 2;
			locationY = height + elemSizeY;
			break;
		case GuiHudOptions.BOTTOMRIGHT:
			elemSizeY = 0 - elemSizeY;
			locationX = width - elemSizeX;
			locationY = height + elemSizeY;
			break;
		}
		GuiUtils.drawString(mc.fontRendererObj, TS3Socket.currentChannel.channel_name, locationX + 10, locationY + 1,
				ModMain.getGuiColor().getRGB());
		mc.getTextureManager()
				.bindTexture(new ResourceLocation("textures/icons/ts3/" + TS3Socket.currentChannel.getIcon() + ".png"));
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		GuiHudOptions.drawScaledCustomSizeModalRect(locationX, locationY + 1, 0, 0, 128, 128, 8, 8, 128, 128);
		for (int i = 0; i < TS3Socket.clients.size(); i++) {
			if (TS3Socket.clients.get(i) != null) {
				GuiUtils.drawString(mc.fontRendererObj, TS3Socket.clients.get(i).getName(), locationX + 10,
						locationY + 2 + (i + 1) * lineSize,
						ModMain.getTextColor().getRGB());
				mc.getTextureManager().bindTexture(
						new ResourceLocation("textures/icons/ts3/" + TS3Socket.clients.get(i).getIcon() + ".png"));
				GlStateManager.color(1.0F, 1.0F, 1.0F);
				GuiHudOptions.drawScaledCustomSizeModalRect(locationX, locationY + 2 + (i + 1) * lineSize, 0, 0, 128,
						128, 8, 8, 128, 128);
			}
		}
	}

	public static void drawTexturedModalRect(RenderItem itemRenderer, int x, int y, int textureX, int textureY,
			int width, int height) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.func_181668_a(4, worldrenderer.getVertexFormat());
		worldrenderer.func_181662_b((double) (x + 0), (double) (y + height), (double) itemRenderer.zLevel)
				.func_181673_a((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1));
		worldrenderer.func_181662_b((double) (x + width), (double) (y + height), (double) itemRenderer.zLevel)
				.func_181673_a((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1));
		worldrenderer.func_181662_b((double) (x + width), (double) (y + 0), (double) itemRenderer.zLevel)
				.func_181673_a((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1));
		worldrenderer.func_181662_b((double) (x + 0), (double) (y + 0), (double) itemRenderer.zLevel)
				.func_181673_a((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1));
		tessellator.draw();
	}
	
	public static void overlayDisplayer(Minecraft mc, ScaledResolution ScaledResolution, RenderGameOverlayEvent ev) {
		try {
			scaledResolution = ScaledResolution;
			if (mc.thePlayer != null) {
				GlStateManager.pushMatrix();
				RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
				FontRenderer fontRender = mc.fontRendererObj;
				itemRenderer.zLevel = 200.0F;
				if(ModMain.UCOsysb && GuiMenu.bmc!=0)
					fontRender.drawStringWithShadow("C"+GuiMenu.bmc, scaledResolution.getScaledWidth()/2+20, scaledResolution.getScaledHeight()/2+20, Colors.RED);
				if (ModMain.HUDEnabled) {
					/*if (!(mc.gameSettings.showDebugInfo) || (mc.gameSettings.showDebugInfo && ModMain.InfoDebug)) {
						 displayArmorHUD(mc, itemRenderer, fontRender);
						 displayEffectHUD(mc, itemRenderer, fontRender);
						 displayTeamSpeak3HUD(mc, itemRenderer, fontRender);
						 displayPlayerHUD(mc, fontRender, ev);
					}*/
					//New Overlay System
					displayOverlays(mc, fontRender, ev);
				}
				GlStateManager.popMatrix();
			}
		} catch (Exception e) {
		}
	}
}
