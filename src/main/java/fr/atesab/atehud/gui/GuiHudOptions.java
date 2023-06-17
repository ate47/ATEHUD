package fr.atesab.atehud.gui;

import java.io.IOException;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.overlay.OverlayDisplayManager;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiHudOptions extends GuiScreen {
	public static final int TOPLEFT = 0;

	public static final int TOPCENTER = 1;

	public static final int TOPRIGHT = 2;
	public static final int MIDDLELEFT = 3;
	public static final int MIDDLECENTER = 4;
	public static final int MIDDLERIGHT = 5;
	public static final int BOTTOMLEFT = 6;
	public static final int BOTTOMCENTER = 7;
	public static final int BOTTOMRIGHT = 8;
	public static final int NONE = 9;
	public static Object CheckScreenIconValue(int posX, int posY, int mouseX, int mouseY) {
		if (GuiUtils.isHover(posX + 3, posY + 8, 8, 8, mouseX, mouseY))
			return TOPLEFT;
		if (GuiUtils.isHover(posX + 56, posY + 8, 8, 8, mouseX, mouseY))
			return TOPCENTER;
		if (GuiUtils.isHover(posX + 118, posY + 8, 8, 8, mouseX, mouseY))
			return TOPRIGHT;
		if (GuiUtils.isHover(posX + 3, posY + 42, 8, 8, mouseX, mouseY))
			return MIDDLELEFT;
		if (GuiUtils.isHover(posX + 56, posY + 42, 8, 8, mouseX, mouseY))
			return MIDDLECENTER;
		if (GuiUtils.isHover(posX + 118, posY + 42, 8, 8, mouseX, mouseY))
			return MIDDLERIGHT;
		if (GuiUtils.isHover(posX + 3, posY + 75, 8, 8, mouseX, mouseY))
			return BOTTOMLEFT;
		if (GuiUtils.isHover(posX + 56, posY + 75, 8, 8, mouseX, mouseY))
			return BOTTOMCENTER;
		if (GuiUtils.isHover(posX + 118, posY + 75, 8, 8, mouseX, mouseY))
			return BOTTOMRIGHT;

		return null;
	}
	public static void drawScreenIcon(int value, int posX, int posY) {
		Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.blendFunc(770, 771);
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(new ResourceLocation("textures/icons/screen.png"));
		drawScaledCustomSizeModalRect(posX, posY, 0, 0, 128, 128, 128, 128, 128, 128);
		mc.getTextureManager().bindTexture(new ResourceLocation("textures/icons/screen_selector.png"));
		drawScaledCustomSizeModalRect(posX + 3, posY + 8, 0, 0, 8, 8, 8, 8, 8, 8);
		drawScaledCustomSizeModalRect(posX + 56, posY + 8, 0, 0, 8, 8, 8, 8, 8, 8);
		drawScaledCustomSizeModalRect(posX + 118, posY + 8, 0, 0, 8, 8, 8, 8, 8, 8);

		drawScaledCustomSizeModalRect(posX + 3, posY + 42, 0, 0, 8, 8, 8, 8, 8, 8);
		drawScaledCustomSizeModalRect(posX + 56, posY + 42, 0, 0, 8, 8, 8, 8, 8, 8);
		drawScaledCustomSizeModalRect(posX + 118, posY + 42, 0, 0, 8, 8, 8, 8, 8, 8);

		drawScaledCustomSizeModalRect(posX + 3, posY + 75, 0, 0, 8, 8, 8, 8, 8, 8);
		drawScaledCustomSizeModalRect(posX + 56, posY + 75, 0, 0, 8, 8, 8, 8, 8, 8);
		drawScaledCustomSizeModalRect(posX + 118, posY + 75, 0, 0, 8, 8, 8, 8, 8, 8);
		GlStateManager.color(1.0F, 1.0F, 0.33F, 1.0F); // Yellow
		switch (value) {
		case TOPLEFT:
			drawScaledCustomSizeModalRect(posX + 3, posY + 8, 0, 0, 8, 8, 8, 8, 8, 8);
			break;
		case TOPCENTER:
			drawScaledCustomSizeModalRect(posX + 56, posY + 8, 0, 0, 8, 8, 8, 8, 8, 8);
			break;
		case TOPRIGHT:
			drawScaledCustomSizeModalRect(posX + 118, posY + 8, 0, 0, 8, 8, 8, 8, 8, 8);
			break;
		case MIDDLELEFT:
			drawScaledCustomSizeModalRect(posX + 3, posY + 42, 0, 0, 8, 8, 8, 8, 8, 8);
			break;
		case MIDDLECENTER:
			drawScaledCustomSizeModalRect(posX + 56, posY + 42, 0, 0, 8, 8, 8, 8, 8, 8);
			break;
		case MIDDLERIGHT:
			drawScaledCustomSizeModalRect(posX + 118, posY + 42, 0, 0, 8, 8, 8, 8, 8, 8);
			break;
		case BOTTOMLEFT:
			drawScaledCustomSizeModalRect(posX + 3, posY + 75, 0, 0, 8, 8, 8, 8, 8, 8);
			break;
		case BOTTOMCENTER:
			drawScaledCustomSizeModalRect(posX + 56, posY + 75, 0, 0, 8, 8, 8, 8, 8, 8);
			break;
		case BOTTOMRIGHT:
			drawScaledCustomSizeModalRect(posX + 118, posY + 75, 0, 0, 8, 8, 8, 8, 8, 8);
			break;
		}
		GlStateManager.disableAlpha();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableLighting();
	}

	public GuiScreen Last;

	public GuiButton inChat, inDebug, Enabled, debugEntity, debugInfo;

	public GuiHudOptions(GuiScreen last) {
		Last = last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			ModMain.saveConfig();
			mc.displayGuiScreen(Last);
		}
		if (button.id == 3)
			ModMain.HUDEnabled = !ModMain.HUDEnabled;
		if (button.id == 4)
			ModMain.EntityDebug = !ModMain.EntityDebug;
		if (button.id == 5)
			ModMain.InfoDebug = !ModMain.InfoDebug;
		super.actionPerformed(button);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		OverlayDisplayManager.overlayDisplayer(mc, new ScaledResolution(mc), null);
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		drawCenteredString(fontRendererObj, I18n.format("gui.atehud.HUD.armor"), width / 2 - 140,
				height / 2 - 64 - fontRendererObj.FONT_HEIGHT - 1, Colors.GOLD);
		drawScreenIcon(ModMain.hudArmorLocation, width / 2 - 210, height / 2 - 64);
		drawCenteredString(fontRendererObj, I18n.format("gui.atehud.HUD.potion"), width / 2,
				height / 2 - 64 - fontRendererObj.FONT_HEIGHT - 1, Colors.GOLD);
		drawScreenIcon(ModMain.hudEffectLocation, width / 2 - 70, height / 2 - 64);
		drawCenteredString(fontRendererObj, I18n.format("gui.atehud.HUD.player"), width / 2 + 140,
				height / 2 - 64 - fontRendererObj.FONT_HEIGHT - 1, Colors.GOLD);
		drawScreenIcon(ModMain.hudPlayerLocation, width / 2 + 70, height / 2 - 64);
		Enabled.packedFGColour = Colors.redGreenOptionInt(ModMain.HUDEnabled);
		debugEntity.packedFGColour = Colors.redGreenOptionInt(ModMain.EntityDebug);
		debugInfo.packedFGColour = Colors.redGreenOptionInt(ModMain.InfoDebug);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
		buttonList.add(Enabled = new GuiButton(3, width / 2 - 200, height / 2 + 70, 199, 20,
				I18n.format("gui.atehud.HUD.show")));
		buttonList
				.add(debugEntity = new GuiButton(4, width / 2, height / 2 + 70, I18n.format("gui.atehud.HUD.entity")));
		buttonList.add(debugInfo = new GuiButton(5, width / 2 - 200, height / 2 + 91, 199, 20,
				I18n.format("gui.atehud.HUD.info")));
		buttonList.add(new GuiButton(0, width / 2, height / 2 + 91, I18n.format("gui.done")));
		super.initGui();
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (CheckScreenIconValue(width / 2 - 210, height / 2 - 64, mouseX, mouseY) != null)
			if (!(CheckScreenIconValue(width / 2 - 210, height / 2 - 64, mouseX, mouseY)
					.equals(ModMain.hudArmorLocation))) {
				ModMain.hudArmorLocation = (Integer) CheckScreenIconValue(width / 2 - 210, height / 2 - 64, mouseX,
						mouseY);
			} else {
				ModMain.hudArmorLocation = NONE;
			}
		if (CheckScreenIconValue(width / 2 - 70, height / 2 - 64, mouseX, mouseY) != null)
			if (!(CheckScreenIconValue(width / 2 - 70, height / 2 - 64, mouseX, mouseY)
					.equals(ModMain.hudEffectLocation))) {
				ModMain.hudEffectLocation = (Integer) CheckScreenIconValue(width / 2 - 70, height / 2 - 64, mouseX,
						mouseY);
			} else {
				ModMain.hudEffectLocation = NONE;
			}
		if (CheckScreenIconValue(width / 2 + 70, height / 2 - 64, mouseX, mouseY) != null)
			if (!(CheckScreenIconValue(width / 2 + 70, height / 2 - 64, mouseX, mouseY)
					.equals(ModMain.hudPlayerLocation))) {
				ModMain.hudPlayerLocation = (Integer) CheckScreenIconValue(width / 2 + 70, height / 2 - 64, mouseX,
						mouseY);
			} else {
				ModMain.hudPlayerLocation = NONE;
			}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
