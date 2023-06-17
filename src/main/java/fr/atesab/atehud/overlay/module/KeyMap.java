package fr.atesab.atehud.overlay.module;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.overlay.Module;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.superclass.ModuleConfig;
import fr.atesab.atehud.utils.GuiUtils;
import fr.atesab.atehud.utils.PvpUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class KeyMap extends Module {
	public static final int OBJECT_SIZE_Y = 25;
	public static final int ELEMENT_SIZE_X = 79;
	public void drawKey(boolean keyDown, int posX, int posY, String[] title, int sizeX, int sizeY) {
		int c = (keyDown ? Colors.GOLD : Colors.WHITE);
		GuiUtils.drawBlackBackGround(posX, posY, sizeX, sizeY);
		GuiUtils.drawBox(posX, posY, posX+sizeX, posY+sizeY, c);
		int prePosY = posY + sizeY / 2 - (mc.fontRendererObj.FONT_HEIGHT+1)*title.length / 2;
		for (int i = 0; i < title.length; i++)
			GuiUtils.drawCenterString(mc.fontRendererObj, title[i], posX+sizeX/2, prePosY+i*(1+mc.fontRendererObj.FONT_HEIGHT), c);
	}

	public void drawKey(KeyBinding kb, int posX, int posY) {
		drawKey(kb,posX,posY, new String[] {Keyboard.getKeyName(kb.getKeyCode())});
	}
	public void drawKey(KeyBinding kb, int posX, int posY, String[] title) {
		drawKey(kb,posX,posY, title, OBJECT_SIZE_Y, OBJECT_SIZE_Y);
	}
	public void drawKey(KeyBinding kb, int posX, int posY, String[] title, int sizeX, int sizeY) {
		drawKey(kb.isKeyDown(),posX,posY, title, sizeX, sizeY);
	}
	@Override
	public ArrayList<ModuleConfig> getConfigs(ArrayList<ModuleConfig> list) {
		list.add(new ModuleConfig("keymap.showActionButton", ModuleConfig.ModuleType.BOOLEAN, "true"));
		list.add(new ModuleConfig("keymap.showMoveAction", ModuleConfig.ModuleType.BOOLEAN, "true"));
		list.add(new ModuleConfig("keymap.showJumpButton", ModuleConfig.ModuleType.BOOLEAN, "true"));
		list.add(new ModuleConfig("keymap.showActionCps", ModuleConfig.ModuleType.BOOLEAN, "true"));
		return list;
	}
	@Override
	public String getDisplayName(String[] args) {
		return I18n.format("atehud.config.overlay.module."+getModuleId()+".name");
	}
	@Override
	public String getModuleId() {
		return "keymap";
	}
	@Override
	public int moduleSizeX(String[] args) {
		return ELEMENT_SIZE_X;
	}
	@Override
	public int moduleSizeY(String[] args) {
		int sizeY = (OBJECT_SIZE_Y+2)*2;
		if(args.length==4) {
			try {
				if(Boolean.valueOf(args[0]))sizeY+=OBJECT_SIZE_Y-4;
				try {if(Boolean.valueOf(args[3]))sizeY+=8;} catch (Exception e) {}
			} catch (Exception e) {}
			try {if(Boolean.valueOf(args[1]))sizeY+=OBJECT_SIZE_Y-4;} catch (Exception e) {}
			try {if(Boolean.valueOf(args[2]))sizeY+=OBJECT_SIZE_Y-4;} catch (Exception e) {}
		}
		return sizeY;
	}
	@Override
	public int renderModule(int posX, int posY, String[] args) {
		boolean showActionButton = true;
		boolean showMoveAction = true;
		boolean showJumpButton = true;
		boolean showActionCps = true;
		if(args.length==4) {
			try {showActionButton = Boolean.valueOf(args[0]);} catch (Exception e) {}
			try {showMoveAction = Boolean.valueOf(args[1]);} catch (Exception e) {}
			try {showJumpButton = Boolean.valueOf(args[2]);} catch (Exception e) {}
			try {showActionCps = Boolean.valueOf(args[3]);} catch (Exception e) {}
		}
		int cursorY = posY;
		drawKey(mc.gameSettings.keyBindForward, posX+OBJECT_SIZE_Y+2, cursorY);
		drawKey(mc.gameSettings.keyBindLeft, posX, cursorY+=OBJECT_SIZE_Y+2);
		drawKey(mc.gameSettings.keyBindBack, posX+OBJECT_SIZE_Y+2, cursorY);
		drawKey(mc.gameSettings.keyBindRight, posX+(OBJECT_SIZE_Y+2)*2, cursorY);
		cursorY+=OBJECT_SIZE_Y+2;
		if(showActionButton) {
			int k = 0;
			if(showActionCps) k = 8;
			int esx = (ELEMENT_SIZE_X-2)/2;
			String lb = I18n.format("atehud.config.overlay.module.keymap.showActionButton.left");
			String rb = I18n.format("atehud.config.overlay.module.keymap.showActionButton.right");
			String[] titlel;
			String[] titler;
			if(showActionCps) {
				titlel = new String[] {lb, String.valueOf(PvpUtils.getLeftCpsCounter())+" "+I18n.format("atehud.config.overlay.module.keymap.showActionCps.unit")};
				titler = new String[] {rb, String.valueOf(PvpUtils.getRightCpsCounter())+" "+I18n.format("atehud.config.overlay.module.keymap.showActionCps.unit")};
			} else {
				titlel = new String[] {lb};
				titler = new String[] {rb};
			}
			drawKey(mc.gameSettings.keyBindAttack.isKeyDown() || ATEEventHandler.hasCountClick,
					posX, cursorY, titlel, 
					esx, OBJECT_SIZE_Y-6+k);
			drawKey(mc.gameSettings.keyBindUseItem, posX+ELEMENT_SIZE_X-esx, cursorY, 
					titler, esx, OBJECT_SIZE_Y-6+k);
			cursorY+=OBJECT_SIZE_Y-4+k;
		}
		if(showMoveAction) {
			int esx = (ELEMENT_SIZE_X-2)/2;
			drawKey(mc.thePlayer.isSneaking(), posX, cursorY, 
					new String[] {I18n.format("atehud.config.overlay.module.keymap.showMoveAction.sneak")}, esx, OBJECT_SIZE_Y-6);
			drawKey(mc.thePlayer.isSprinting(), posX+ELEMENT_SIZE_X-esx, cursorY, 
					new String[] {I18n.format("atehud.config.overlay.module.keymap.showMoveAction.sprint")}, esx, OBJECT_SIZE_Y-6);
			cursorY+=OBJECT_SIZE_Y-4;
		}
		if(showJumpButton) {
			drawKey(mc.gameSettings.keyBindJump, posX, cursorY, 
					new String[] {I18n.format("atehud.config.overlay.module.keymap.showJumpButton.jump")}, ELEMENT_SIZE_X, OBJECT_SIZE_Y-6);
			cursorY+=OBJECT_SIZE_Y-4;
		}
		return cursorY;
	}
}
