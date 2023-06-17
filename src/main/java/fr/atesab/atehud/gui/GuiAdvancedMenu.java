package fr.atesab.atehud.gui;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.config.GuiATEHUDConfig;
import fr.atesab.atehud.gui.element.Element;
import fr.atesab.atehud.overlay.Overlay;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonLanguage;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;

public class GuiAdvancedMenu extends GuiScreen {
	public static int background = 0;

	public static int background_size = 4;

	public static ResourceLocation bg;
	public static void defineBackgroundId() {
		defineBackgroundId(true);
	}
	public static void defineBackgroundId(boolean change) {
		if (change) {
			int current = background;
			int pbackground = (int) ((new Random()).nextFloat() * (background_size-1));
			if(pbackground>=background)background++; else background=pbackground;
		} else {
			background = (int) ((new Random()).nextFloat() * background_size);
		}
		bg = new ResourceLocation("textures/gui/background" + background + ".jpeg");
	}

	/**
	 * Draw advanced Background
	 * 
	 * @param zLevel
	 *            useless
	 * @param gui
	 *            current gui
	 */
	public static void drawBackgroundAvanced(float zLevel, GuiScreen gui) {
		gui.drawDefaultBackground();
		if (ModMain.useAdvancedMenu) {
			if (gui.mc.theWorld == null) {
				GuiUtils.drawTexture(gui, 0, 0, gui.width, gui.height, bg);
				return;
			}
		}
	}

	public GuiMainMenu gmm;

	private GuiButton realmsButton, edit, edit_add, edit_res;

	public boolean inEditMode = false;

	List<String> info = new ArrayList<String>();
	public GuiAdvancedMenu(GuiMainMenu gmm) {
	}
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 1900) {
			mc.displayGuiScreen(new GuiATEHUDConfig(this));
		}
		if (button.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}

		if (button.id == 5) {
			this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
		}

		if (button.id == 1) {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}

		if (button.id == 2) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}

		if (button.id == 14 && this.realmsButton.visible) {
			this.switchToRealms();
		}

		if (button.id == 4) {
			this.mc.shutdown();
		}

		if (button.id == 6) {
			this.mc.displayGuiScreen(new net.minecraftforge.fml.client.GuiModList(this));
		}

		if (button.id == 11) {
			this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
		}

		if (button.id == 12) {
			ISaveFormat isaveformat = this.mc.getSaveLoader();
			WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

			if (worldinfo != null) {
				GuiYesNo guiyesno = GuiSelectWorld.func_152129_a(this, worldinfo.getWorldName(), 12);
				this.mc.displayGuiScreen(guiyesno);
			}
		}
		if (button.id == 7) {
			defineBackgroundId();
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawBackgroundAvanced(height, this);
		/*mouseParticule.setPositionVector(new PhysicVector(mouseX, mouseY, 0));
		for (Particule p: univers.getParticules())
			if(!p.equals(mouseParticule)) {
				fontRendererObj.drawString(".", (int)(p.getPositionVector().getX()), (int)(p.getPositionVector().getY()), Colors.WHITE);
			}
		univers.getParticules().set(1000, mouseParticule);*/
		int j = (new Color(0F, 0F, 0F, 0.5F)).getRGB();
		GuiUtils.drawGradientRect(width - 155, 0, width, height, j, j);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int i = 0;
		for (; i < info.size(); i++) {
			drawString(fontRendererObj, info.get(i), width - 150, 2 + i * (fontRendererObj.FONT_HEIGHT + 1),
					Colors.WHITE);
		}
		List<String> branding = com.google.common.collect.Lists
				.reverse(net.minecraftforge.fml.common.FMLCommonHandler.instance().getBrandings(true));
		for (; i < info.size() + branding.size(); i++) {
			drawString(fontRendererObj, branding.get(i - info.size()), width - 150,
					2 + i * (fontRendererObj.FONT_HEIGHT + 1), Colors.WHITE);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);

	}

	public void initGui() {
		if (!ModMain.useAdvancedMenu)
			mc.displayGuiScreen(gmm);
		int a = height - 148;
		this.buttonList.add(new GuiButton(1, width - 152, a, 150, 20, I18n.format("menu.singleplayer")));
		this.buttonList.add(new GuiButton(2, width - 152, a + 21, 150, 20, I18n.format("menu.multiplayer")));
		this.buttonList
				.add(this.realmsButton = new GuiButton(14, width - 152, a + 42, 150, 20, I18n.format("menu.online")));
		this.buttonList.add(new GuiButton(6, width - 152, a + 63, 74, 20, I18n.format("fml.menu.mods")));
		this.buttonList.add(new GuiButton(7, width - 77, a + 63, 75, 20, I18n.format("atehud.menu.nextImage")));
		this.buttonList.add(new GuiButton(0, width - 152, a + 84, 150, 20, I18n.format("menu.options")));

		this.buttonList.add(new GuiButton(1900, width - 152, a + 105, 150, 20, I18n.format("atehud.menu")));

		this.buttonList.add(new GuiButton(4, width - 152, a + 126, 150, 20, I18n.format("menu.quit")));
		this.buttonList.add(new GuiButtonLanguage(5, width - 21, 1));
		info.clear();
		info.add(mc.getSession().getUsername());
		info.add(ModMain.MOD_LITTLE_NAME + " " + ModMain.MOD_VERSION);
		if (ModMain.AdvancedModActived)
			info.set(info.size() - 1, info.get(info.size() - 1) + " Advanced");
		super.initGui();
	}

	public void onGuiClosed() {
		ModMain.saveConfig();
		super.onGuiClosed();
	}

	private void switchToRealms() {
		RealmsBridge realmsbridge = new RealmsBridge();
		realmsbridge.switchToRealms(this);
	}

	public void updateScreen() {
		/**
		 * univers.runUniversalTick();
		 */
		super.updateScreen();
	}
}
