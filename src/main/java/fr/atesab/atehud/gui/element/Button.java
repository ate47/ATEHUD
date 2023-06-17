package fr.atesab.atehud.gui.element;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import com.google.common.collect.Lists;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.KeyBind;
import fr.atesab.atehud.Mod;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.Mod.ToggleMod;
import fr.atesab.atehud.gui.GuiMenu;
import fr.atesab.atehud.gui.config.GuiModKeySelection;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;

public class Button extends Element {
	public static class ModButton extends Button {
		public Mod mod;

		public ModButton(int posX, int posY, int width, int height, Mod mod) {
			super(mod.getName(), posX, posY, width, height);
			this.desc = mod.getDesc();
			this.mod = mod;
		}

		public ModButton(int posX, int posY, Mod mod) {
			super(mod.getName(), posX, posY);
			this.desc = mod.getDesc();
			this.mod = mod;
		}

		public void onClick(int mouseX, int mouseY) {
			boolean a = true;
			if (mod.requireCreative && !mc.thePlayer.capabilities.isCreativeMode && !ModMain.byPassAC)
				a = false;
			if (a) {
				if (!mod.isEnabled())
					mod.onEnabled();
				else
					mod.onDisabled();
				if (mod instanceof ToggleMod)
					text = mod.getName();
			} else {
				Chat.noCheat(I18n.format("gui.act.nocreative"));
			}
			super.onClick(mouseX, mouseY);
		}
		public void onHover(int mouseX, int mouseY) {
			String[] a1 = mod.getDesc();
			List<KeyBind> keys = ModMain.getKeyMod(mod);
			int l = 1;
			if(keys.size()!=0)l+=1;
			String[] a2;
			if(a1.length != 0 && !(a1[0].isEmpty() && a1.length == 1)) {
				a2 = new String[l+a1.length];
				System.arraycopy(a1, 0, a2, 0, a1.length);
			} else a2 = new String[l];
			
			if(keys.size()!=0) {
				String s = "";
				for (int i = 0; i < keys.size(); i++) {
					if(i>0)s+=", ";
					s+=Keyboard.getKeyName(keys.get(i).key);
				}
				a2[a2.length-2]=I18n.format("atehud.config.modkey")+": "+s;
			}
			a2[a2.length-1]=I18n.format("atehud.config.modkey.rightclick");
			GuiUtils.drawTextBox(mc.currentScreen, mc, mc.fontRendererObj, a2, mouseX + 5, mouseY + 5,
					Colors.AQUA);
			super.onHover(mouseX, mouseY);
		}

		public void onRightClick(int mouseX, int mouseY) {
			mc.displayGuiScreen(new GuiModKeySelection(mc.currentScreen, mod));
			super.onRightClick(mouseX, mouseY);
		}
	}
	public interface SkinRenderer {
		public void render(Minecraft mc, Button btn, int mouseX, int mouseY);
	}
	public static List<SkinRenderer> render = new ArrayList<Button.SkinRenderer>();
	public static int getHoverColor(Button btn) {
		if (!btn.enabled) {
			return 10526880;
		}
		if (btn.packedFGColour != 0) {
			return btn.packedFGColour;
		} else if (btn.hovered) {
			return 16777120;
		}
		return 14737632;
	}

	public String text;

	public String[] desc;

	public int packedFGColour;

	private int skin = 0;

	public boolean enabled = true, hovered;

	public Button(String text, int posX, int posY) {
		this(text, posX, posY, 200, 20);
	}

	public Button(String text, int posX, int posY, int width, int height) {
		super(posX, posY, width, height);
		this.enabled = true;
		this.visible = true;
		this.text = text;
	}

	public int getSkin() {
		return skin;
	}

	public void mouseClick(int mouseX, int mouseY, int button) {
		if (enabled)
			super.mouseClick(mouseX, mouseY, button);
	}

	public void render(int mouseX, int mouseY) {
		super.render(mouseX, mouseY);
		hovered = GuiUtils.isHover(this, mouseX, mouseY);
		if (visible) {
			if (!(skin < 0 || skin >= render.size()))
				render.get(skin).render(mc, this, mouseX, mouseY);
		}
	}

	/*
	 * Set skin of the button (0=Flat / 1=Default)
	 */
	public void setSkin(int skin) {
		this.skin = skin;
	}
}
