package fr.atesab.atehud.overlay.module;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.overlay.Module;
import fr.atesab.atehud.overlay.OverlayDisplayManager;
import fr.atesab.atehud.superclass.ModuleConfig;
import fr.atesab.atehud.overlay.Overlay.position;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class Effects extends Module {

	public ArrayList<ModuleConfig> getConfigs(ArrayList<ModuleConfig> list) {
		list.add(new ModuleConfig("effects.name", ModuleConfig.ModuleType.BOOLEAN, "true"));
		list.add(new ModuleConfig("effects.time", ModuleConfig.ModuleType.BOOLEAN, "true"));
		list.add(new ModuleConfig("effects.icon", ModuleConfig.ModuleType.BOOLEAN, "true"));
		return list;
	}

	public String getDisplayName(String[] args) {
		return I18n.format("atehud.config.overlay.module."+getModuleId()+".name");
	}

	public String getModuleId() {
		return "effects";
	}

	public int moduleSizeX(String[] args) {
		return 128;
	}

	public int moduleSizeY(String[] args) {
		Collection collection = mc.thePlayer.getActivePotionEffects();
		return 20 * mc.thePlayer.getActivePotionEffects().size();
	}

	public int renderModule(int posX, int posY, String[] args) {
		boolean renderName = true;
		boolean renderTime = true;
		boolean renderIcon = true;
		if(args.length==3) {
			try {renderName = Boolean.valueOf(args[0]);} catch (Exception e) {}
			try {renderTime = Boolean.valueOf(args[1]);} catch (Exception e) {}
			try {renderIcon = Boolean.valueOf(args[2]);} catch (Exception e) {}
		}
		Collection collection = mc.thePlayer.getActivePotionEffects();
		int i = posX; // posX
		int j = posY; // posY
		int k = 20; // sizeY of the element
		int l = 128; // sizeX of the element
		int iconSize = 20;
		RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
		FontRenderer fontRender = mc.fontRendererObj;
		itemRenderer.zLevel = 200.0F;
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

				if (potion.hasStatusIcon() && renderIcon) {
					int m = potion.getStatusIconIndex();
					mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
					OverlayDisplayManager.drawTexturedModalRect(itemRenderer, i, j, m%8*18, 198+m/8*18, 18, 18);
					potion.renderInventoryEffect(i, j, potioneffect, mc);
				}
				if (!potion.shouldRenderInvText(potioneffect))
					continue;
				if(renderName)
					fontRender.drawStringWithShadow(s1, (float) (i + iconSize + 2), (float) (j),
							ModMain.getTextColor().getRGB());
				if(renderTime)
					fontRender.drawStringWithShadow(s, (float) (i + iconSize + 2), (float) (j + 10), 8355711);
			}
		}
		return 20 * mc.thePlayer.getActivePotionEffects().size();
	}

}
