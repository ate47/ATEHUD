package fr.atesab.atehud.gui.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;

import static org.lwjgl.opengl.GL11.*;

import com.google.common.collect.Lists;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.Chat;
import fr.atesab.atehud.Mod;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.Mod.SliderMod;
import fr.atesab.atehud.gui.GuiMenu;
import fr.atesab.atehud.gui.GuiModAdd;
import fr.atesab.atehud.gui.GuiRename;
import fr.atesab.atehud.gui.element.Button.ModButton;
import fr.atesab.atehud.gui.element.ButtonsContainer.ModContainer;
import fr.atesab.atehud.gui.element.Slider.ModSlider;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;

public class ButtonsContainer extends Element {
	public static class ModContainer extends ButtonsContainer {
		public List<Mod> mods = new ArrayList<Mod>();

		public ModContainer(String category, int posX, int posY, int defaultposX, int defaultposY, Mod[] mods) {
			this(category, posX, posY, defaultposX, defaultposY, mods, false);
		}

		public ModContainer(String category, int posX, int posY, int defaultposX, int defaultposY, Mod[] mods,
				boolean open) {
			super(category, posX, posY, defaultposX, defaultposY, new ArrayList<Button>(), open);
			for (int i = 0; i < mods.length; i++) {
				if (mods[i] instanceof SliderMod)
					this.buttons
							.add(new Slider.ModSlider(0, 0, (((SliderMod) mods[i]).getValue()), (SliderMod) mods[i]));
				else
					this.buttons.add(new Button.ModButton(0, 0, mods[i]));
			}
			for (Mod m: mods) this.mods.add(m);
		}

		public String getData() {
			return Mod.getModContainerSave(this);
		}

		@Override
		public String toString() {
			return "ModContainer [mods=" + mods.toString() + ", title=" + title + ", open=" + open
					+ ", isDragged=" + isDragged + ", buttons=" + buttons + ", defaultposX=" + defaultposX
					+ ", defaultposY=" + defaultposY + ", dragRelativePosX=" + dragRelativePosX + ", dragRelativePosY="
					+ dragRelativePosY + ", visible=" + visible + ", posX=" + posX + ", posY=" + posY + ", height="
					+ height + ", width=" + width + ", enabled=" + enabled + ", hovered=" + hovered + "]";
		}
	}
	public String title;
	public boolean open, isDragged;
	public List<Button> buttons = new ArrayList<Button>();

	public int defaultposX, defaultposY, dragRelativePosX, dragRelativePosY;

	public ButtonsContainer(String title, int posX, int posY, int defaultposX, int defaultposY, List<Button> buttons) {
		this(title, posX, posY, defaultposX, defaultposY, buttons, true);
	}

	public ButtonsContainer(String title, int posX, int posY, int defaultposX, int defaultposY, List<Button> buttons,
			boolean open) {
		super(posX, posY, 0, 0);
		this.buttons = buttons;
		this.title = title;
		this.height = maxY();
		this.width = maxX();
		this.defaultposX = defaultposX;
		this.defaultposY = defaultposY;
		this.open = open;
	}

	public ButtonsContainer getInstance() {
		return this;
	}

	public int maxX() {
		int value = mc.fontRendererObj.getStringWidth(title) + 22; // Close/Hide btn+border 10*2+2
		for (int i = 0; i < buttons.size(); i++) {
			if (buttons.get(i) instanceof Slider) {
				Slider btn = (Slider) buttons.get(i);
				int size = mc.fontRendererObj.getStringWidth(btn.text + " : ") + 2 + 100;
				if (size > value)
					value = size;
			} else {
				Button btn = (Button) buttons.get(i);
				int size = mc.fontRendererObj.getStringWidth(btn.text) + 2;
				if (size > value)
					value = size;
			}
		}
		return value;
	}

	public int maxY() {
		if (!open)
			return (mc.fontRendererObj.FONT_HEIGHT + 4);
		return (mc.fontRendererObj.FONT_HEIGHT + 4) * (buttons.size() + 1);
	}

	public void mouseClick(int mouseX, int mouseY, int button) {
		boolean editMod = (mc.currentScreen instanceof GuiMenu && ((GuiMenu) mc.currentScreen).inEditMod);
		int edit = 0;
		if (editMod)
			edit = 22+mc.fontRendererObj.getStringWidth("+");
		if (open) {
			for (int i = 0; i < buttons.size(); i++) {
				if(!editMod) { 
					if (buttons.get(i) instanceof Slider) {
						Slider btn = (Slider) buttons.get(i);
						if (GuiUtils.isHover(posX + 1 + maxX() - 100, posY + (mc.fontRendererObj.FONT_HEIGHT + 4) * (i + 1),
								100, mc.fontRendererObj.FONT_HEIGHT + 3, mouseX, mouseY)) {
							btn.isDragged = true;
						}
					} else {
						Button btn = (Button) buttons.get(i);
						if (GuiUtils.isHover(posX + 1, posY + (mc.fontRendererObj.FONT_HEIGHT + 4) * (i + 1), maxX(),
								mc.fontRendererObj.FONT_HEIGHT + 3, mouseX, mouseY)) {
							if (button == 0)
								btn.onClick(mouseX, mouseY);
							if (button == 1)
								btn.onRightClick(mouseX, mouseY);
						}
					}
				} else {
					if(GuiUtils.isHover(posX + width + 20 - mc.fontRendererObj.getStringWidth("x"),
						posY + (mc.fontRendererObj.FONT_HEIGHT + 4) * (i + 1),
						mc.fontRendererObj.getStringWidth("x"), mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)
						&& this instanceof ModContainer) {
						if (buttons.get(i) instanceof ModButton) {
							((ModContainer) this).mods.remove(((ModButton) buttons.get(i)).mod);
							this.buttons.remove(((ModButton) buttons.get(i)));
						} else if (buttons.get(i) instanceof ModSlider) {
							((ModContainer) this).mods.remove(((ModSlider) buttons.get(i)).mod);
							this.buttons.remove(((ModSlider) buttons.get(i)));
						}
					} else if(GuiUtils.isHover(posX + width + 22, posY + 1 + (mc.fontRendererObj.FONT_HEIGHT + 4) * (i + 1),
							mc.fontRendererObj.getStringWidth("+"), mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)) {
						final int position = i;
						mc.displayGuiScreen(new GuiModAdd(mc.currentScreen) {
							public void addMod(Mod mod) {
								((ModContainer)getInstance()).mods.add(position, mod);
								if(mod instanceof SliderMod) {
									((ModContainer)getInstance()).buttons.add(position, new ModSlider(0, 0, ((SliderMod)mod).getValue(), (SliderMod) mod));
								} else {
									((ModContainer)getInstance()).buttons.add(position, new ModButton(0, 0, mod));
								}
								if(mc.currentScreen instanceof GuiMenu) 
									ModMain.MenuElements = Mod.GenElems(((GuiMenu)mc.currentScreen).cons, mc);
							}
						});
					}
				}
			}
		}
		int sizeInfo = 5;
		if (open)
			sizeInfo = 0;
		int cursor = posX + width + edit;
		if (GuiUtils.isHover((cursor-=mc.fontRendererObj.getCharWidth('X')), posY + 1, mc.fontRendererObj.getCharWidth('X'), 
				mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)) {// X
			this.posX = this.defaultposX;
			this.posY = this.defaultposY;
			this.open = false;
		} else if (GuiUtils.isHover((cursor-=2+mc.fontRendererObj.getCharWidth('-')), posY + 1, mc.fontRendererObj.getCharWidth('-'), 
				mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)) {
			open = !open; // -
		} else if (editMod && GuiUtils.isHover((cursor-=2+mc.fontRendererObj.getCharWidth('+')), posY + 1, mc.fontRendererObj.getCharWidth('+'), 
				mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)) {
			mc.displayGuiScreen(new GuiModAdd(mc.currentScreen) {
				public void addMod(Mod mod) {
					((ModContainer)getInstance()).mods.add(mod);
					if(mod instanceof SliderMod) {
						((ModContainer)getInstance()).buttons.add(new ModSlider(0, 0, ((SliderMod)mod).getValue(), (SliderMod) mod));
					} else {
						((ModContainer)getInstance()).buttons.add(new ModButton(0, 0, mod));
					}
				}
			});
		} else if (editMod && GuiUtils.isHover((cursor-=2+mc.fontRendererObj.getCharWidth('x')), posY + 1, mc.fontRendererObj.getCharWidth('x'), 
				mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)) {
			if(mc.currentScreen instanceof GuiMenu)
				((GuiMenu) mc.currentScreen).toRemove.add((ModContainer) this);
		} else if (editMod && GuiUtils.isHover((cursor-=2+mc.fontRendererObj.getCharWidth('e')), posY + 1, mc.fontRendererObj.getCharWidth('e'), 
				mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)) {
			mc.displayGuiScreen(new GuiRename(mc.currentScreen, this.title) {
				public void setName(String name) {
					getInstance().title = name;
				}
			});
		} else if (editMod && ModMain.AdvancedModActived && GuiUtils.isHover((cursor-=2+mc.fontRendererObj.getCharWidth('s')), posY + 1, mc.fontRendererObj.getCharWidth('s'), 
				mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)) {
			StringSelection select = new StringSelection(((ModContainer)this).getData());
			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			cb.setContents(select, select);
		} else if (GuiUtils.isHover(posX - 5, posY - 5, width + 5, mc.fontRendererObj.FONT_HEIGHT + 4 + sizeInfo,
				mouseX, mouseY)) {
			isDragged = true;
			dragRelativePosX = mouseX - posX;
			dragRelativePosY = mouseY - posY;
		}

	}

	public void render(int mouseX, int mouseY) {
		boolean editMod = (mc.currentScreen instanceof GuiMenu && ((GuiMenu) mc.currentScreen).inEditMod);
		Color MenuColor = ModMain.getGuiColor();
		Color TextColor = ModMain.getTextColor();
		this.height = maxY();
		this.width = maxX();
		Element hoverElem = null;
		int edit = 0;
		if (editMod)
			edit = 22+mc.fontRendererObj.getStringWidth("+");
		//draw box
		GuiUtils.drawBlackBackGround(posX - 5, posY - 5, maxX() + 10 + edit, maxY() + 10);
		GuiUtils.drawBox(posX - 5, posY - 5, posX - 5 + maxX() + 10 + edit, posY - 5 + maxY() + 10, MenuColor);
		if (isDragged && Mouse.isButtonDown(0)) {
			this.posX = mouseX - dragRelativePosX;
			this.posY = mouseY - dragRelativePosY;
		} else {
			isDragged = false;
		}
		// drawtitle
		mc.fontRendererObj.drawString(Chat.getModifier(title), posX + 1, posY + 1, MenuColor.getRGB());
		int clr1 = Colors.RED;
		int cursor = posX + width + edit;
		if (GuiUtils.isHover((cursor-=mc.fontRendererObj.getCharWidth('X')), posY + 1, mc.fontRendererObj.getCharWidth('X'), 
				mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY))
			clr1 = Colors.DARK_RED;
		mc.fontRendererObj.drawString("X", cursor, posY + 1, clr1);
		int clr2 = TextColor.getRGB();
		if (GuiUtils.isHover((cursor-=2+mc.fontRendererObj.getCharWidth('-')), posY + 1, 
				mc.fontRendererObj.getStringWidth("-"), mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY))
			clr2 = Colors.GRAY;
		mc.fontRendererObj.drawString("-", cursor, posY + 1, clr2);
		if(editMod) {
			int clr3 = Colors.GREEN;
			if (GuiUtils.isHover((cursor-=2+mc.fontRendererObj.getCharWidth('+')), posY + 1, mc.fontRendererObj.getCharWidth('+'), 
					mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)) {
				clr3 = Colors.DARK_GREEN;
				hoverElem = new HoverText(Colors.AQUA, I18n.format("atehud.menu.edit.addMod"));
			}
			mc.fontRendererObj.drawString("+", cursor, posY + 1, clr3);
			int clr4 = Colors.RED;
			if (GuiUtils.isHover((cursor-=2+mc.fontRendererObj.getCharWidth('x')), posY + 1, mc.fontRendererObj.getCharWidth('x'), 
					mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)) {
				clr4 = Colors.DARK_RED;
				hoverElem = new HoverText(Colors.AQUA, I18n.format("atehud.menu.edit.delCat"));
			}
			mc.fontRendererObj.drawString("x", cursor, posY + 1, clr4);
			int clr5 = Colors.AQUA;
			if (GuiUtils.isHover((cursor-=2+mc.fontRendererObj.getCharWidth('e')), posY + 1, mc.fontRendererObj.getCharWidth('e'), 
					mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)) {
				clr5 = Colors.DARK_AQUA;
				hoverElem = new HoverText(Colors.AQUA, I18n.format("atehud.menu.edit.renameCat"));
			}
			mc.fontRendererObj.drawString("e", cursor, posY + 1, clr5);
			if(ModMain.AdvancedModActived) {
				int clr6 = Colors.YELLOW;
				if (GuiUtils.isHover((cursor-=2+mc.fontRendererObj.getCharWidth('s')), posY + 1, mc.fontRendererObj.getCharWidth('s'), 
						mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)) {
					clr6 = Colors.GOLD;
					hoverElem = new HoverText(Colors.AQUA, I18n.format("atehud.menu.newTab.data.copy"));
				}
				mc.fontRendererObj.drawString("s", cursor, posY + 1, clr6);
			}
		}
		if (open) {
			for (int i = 0; i < buttons.size(); i++) {
				if (buttons.get(i) instanceof Slider) {
					Slider btn = (Slider) buttons.get(i);
					GuiUtils.drawRightString(mc.fontRendererObj, btn.text + " : ", posX + 1 + maxX() - 100,
							posY + (mc.fontRendererObj.FONT_HEIGHT + 4) * (i + 1), mc.fontRendererObj.FONT_HEIGHT,
							TextColor.getRGB());
					GuiUtils.drawBox(posX + 1 + maxX() - 100, posY + (mc.fontRendererObj.FONT_HEIGHT + 4) * (i + 1) - 2,
							posX + 1 + maxX(),
							posY + (mc.fontRendererObj.FONT_HEIGHT + 4) * (i + 1) + mc.fontRendererObj.FONT_HEIGHT,
							MenuColor);
					int posiY = posY + (mc.fontRendererObj.FONT_HEIGHT + 4) * (i + 1);
					int posiX = posX + 1 + maxX() - 100 + (int) (btn.value * 100);
					GuiUtils.drawBox(posiX - 2, posiY - 2, posiX + 2, posiY + mc.fontRendererObj.FONT_HEIGHT,
							MenuColor);
					GuiUtils.drawCenterString(mc.fontRendererObj, btn.getText(), posX + 1 + maxX() - 50, posiY,
							mc.fontRendererObj.FONT_HEIGHT, TextColor.getRGB());
					if (btn.isDragged && Mouse.isButtonDown(0)) {
						btn.value = btn.clamp((mouseX - (posX + 1 + maxX() - 100)) / 100.0F, 0.0F, 1.0F);
						btn.onValueChanged(btn.value);
					} else {
						btn.isDragged = false;
					}
				} else if (buttons.get(i) instanceof Title) {
					Title btn = (Title) buttons.get(i);
					mc.fontRendererObj.drawString(btn.text,
							posX + 1 + maxX() / 2 - mc.fontRendererObj.getStringWidth(btn.text) / 2,
							posY + (mc.fontRendererObj.FONT_HEIGHT + 4) * (i + 1), TextColor.getRGB());
				} else {
					Button btn = (Button) buttons.get(i);
					int color = TextColor.getRGB();// GL settings
					if (GuiUtils.isHover(posX + 1, posY + (mc.fontRendererObj.FONT_HEIGHT + 4) * (i + 1), maxX(),
							mc.fontRendererObj.FONT_HEIGHT + 4, mouseX, mouseY)) {
						color = Colors.GOLD;
						hoverElem = btn;
					}
					mc.fontRendererObj.drawString(btn.text, posX + 1,
							posY + (mc.fontRendererObj.FONT_HEIGHT + 4) * (i + 1), color);
				}
				if (editMod) {
					int ebc = Colors.RED;
					int abc = Colors.GREEN;
					if (GuiUtils.isHover(posX + width + 20 - mc.fontRendererObj.getStringWidth("x"),
							posY + (mc.fontRendererObj.FONT_HEIGHT + 4) * (i + 1),
							mc.fontRendererObj.getStringWidth("x"), mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)) {
						ebc = Colors.DARK_RED;
						hoverElem = new HoverText(Colors.AQUA, I18n.format("atehud.menu.edit.delMod"));
					}
					if (GuiUtils.isHover(posX + width + 22,
							posY + 1 + (mc.fontRendererObj.FONT_HEIGHT + 4) * (i + 1),
							mc.fontRendererObj.getStringWidth("+"), mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)) {
						abc = Colors.DARK_GREEN;
						hoverElem = new HoverText(Colors.AQUA, I18n.format("atehud.menu.edit.addMod"));
					}
					mc.fontRendererObj.drawString("x", posX + width + 20 - mc.fontRendererObj.getStringWidth("x"),
							posY + (mc.fontRendererObj.FONT_HEIGHT + 4) * (i + 1), ebc);
					mc.fontRendererObj.drawString("+", posX + width + 22,
							posY + 1 + (mc.fontRendererObj.FONT_HEIGHT + 4) * (i + 1), abc);
				}
			}
			for (int i = 0; i < buttons.size(); i++) {
				Button btn = (Button) buttons.get(i);
				if (GuiUtils.isHover(posX + 1, posY + (mc.fontRendererObj.FONT_HEIGHT + 4) * (i + 1), maxX(),
						mc.fontRendererObj.FONT_HEIGHT + 4, mouseX, mouseY))
					hoverElem = btn;
			}
		}
		if(hoverElem!=null) ATEEventHandler.addHoverElement(hoverElem);
	}
	@Override
	public String toString() {
		return "ButtonsContainer [title=" + title + ", open=" + open + ", isDragged=" + isDragged + ", buttons="
				+ buttons + ", defaultposX=" + defaultposX + ", defaultposY=" + defaultposY + ", dragRelativePosX="
				+ dragRelativePosX + ", dragRelativePosY=" + dragRelativePosY + "]";
	}
}
