package fr.atesab.atehud.overlay;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.internal.LinkedTreeMap;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.event.LoadDefaultOverlayEvent;
import fr.atesab.atehud.overlay.Overlay.position;
import fr.atesab.atehud.overlay.module.Armor;
import fr.atesab.atehud.overlay.module.Effects;
import fr.atesab.atehud.overlay.module.Player;
import fr.atesab.atehud.overlay.module.Teamspeak;
import fr.atesab.atehud.overlay.module.Text;
import net.minecraft.client.resources.I18n;

public class Overlay {
	public static enum ColorType{
		inherit,
		custom;
	}
	public static interface DrawOption {
		public int getPosX(int sizeX, int width);
		public int getPosY(int sizeY, int height);
	}
	public static class ModuleConfig {
		public String id;
		public String[] args;

		public ModuleConfig(Module module) {
			this.id = module.getModuleId();
			this.args = module.getDefaultConfig();
		}

		public ModuleConfig(String id, String[] args) {
			this.id = id;
			this.args = args;
		}
	}
	public static enum position {
		topLeft(new DrawOption() {
			public int getPosX(int sizeX, int width) {
				return 0;
			}
			public int getPosY(int sizeY, int height) {
				return 0;
			}
		}), 
		topCenter(new DrawOption() {
			public int getPosX(int sizeX, int width) {
				return width/2-sizeX/2;
			}
			public int getPosY(int sizeY, int height) {
				return 0;
			}
		}), 
		topRight(new DrawOption() {
			public int getPosX(int sizeX, int width) {
				return width-sizeX;
			}
			public int getPosY(int sizeY, int height) {
				return 0;
			}
		}), 
		middleLeft(new DrawOption() {
			public int getPosX(int sizeX, int width) {
				return 0;
			}
			public int getPosY(int sizeY, int height) {
				return height/2-sizeY/2;
			}
		}), 
		middleCenter(new DrawOption() {
			public int getPosX(int sizeX, int width) {
				return width/2-sizeX/2;
			}
			public int getPosY(int sizeY, int height) {
				return height/2-sizeY/2;
			}
		}), 
		middleRight(new DrawOption() {
			public int getPosX(int sizeX, int width) {
				return width-sizeX;
			}
			public int getPosY(int sizeY, int height) {
				return height/2-sizeY/2;
			}
		}), 
		bottomLeft(new DrawOption() {
			public int getPosX(int sizeX, int width) {
				return 0;
			}
			public int getPosY(int sizeY, int height) {
				return height-sizeY;
			}
		}), 
		bottomCenter(new DrawOption() {
			public int getPosX(int sizeX, int width) {
				return width/2-sizeX/2;
			}
			public int getPosY(int sizeY, int height) {
				return height-sizeY;
			}
		}), 
		bottomRight(new DrawOption() {
			public int getPosX(int sizeX, int width) {
				return width-sizeX;
			}
			public int getPosY(int sizeY, int height) {
				return height-sizeY;
			}
		}), 
		hide(new DrawOption() {
			public int getPosX(int sizeX, int width) {
				return 0;
			}
			public int getPosY(int sizeY, int height) {
				return 0;
			}
		}), 
		;

		public static position getPos(int pos) {
			switch (pos) {
			case 0:
				return position.topLeft;
			case 1:
				return position.topCenter;
			case 2:
				return position.topRight;
			case 3:
				return position.middleLeft;
			case 4:
				return position.middleCenter;
			case 5:
				return position.middleRight;
			case 6:
				return position.bottomLeft;
			case 7:
				return position.bottomCenter;
			case 8:
				return position.bottomRight;
			default:
				return position.hide;
			}
		}
		public static int getPos(position pos) {
			switch (pos) {
			case topLeft:
				return 0;
			case topCenter:
				return 1;
			case topRight:
				return 2;
			case middleLeft:
				return 3;
			case middleCenter:
				return 4;
			case middleRight:
				return 5;
			case bottomLeft:
				return 6;
			case bottomCenter:
				return 7;
			case bottomRight:
				return 8;
			default:
				return 9;
			}
		}
		private DrawOption draw;
		private position(DrawOption draw) {
			this.draw = draw;
		}

		public DrawOption getDraw() {
			return draw;
		}

		public boolean isInver() {
			return (this.equals(bottomCenter) || this.equals(bottomLeft) || this.equals(bottomRight));
		}

		public boolean isMiddler() {
			return (this.equals(middleCenter) || this.equals(middleLeft) || this.equals(middleRight));
		}
	}
	public static List<OverlayDefaultConfig> overlayDefaultConfigs = new ArrayList<OverlayDefaultConfig>();
	public static void loadOverlayDefaultConfigs() {
		overlayDefaultConfigs.clear();
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new LoadDefaultOverlayEvent(overlayDefaultConfigs));
		String log = "";
		int overlay = 0;
		for (int i = 0; i < overlayDefaultConfigs.size(); i++) {
			if(overlayDefaultConfigs.get(i)==null) continue;
			if(i > 0)log+=",";
			log+=overlayDefaultConfigs.get(i).getTitle()+" by "+overlayDefaultConfigs.get(i).getAuthor();
			overlay++;
		}
		String s = "";
		if(overlay>1)s="s";
		System.out.println("Loaded " + overlay + " overlay default config" + s + " : ["+log+"]");
	}
	public ColorType colorText;
	public ColorType colorMenu;
	public int textColorRed = 0;
	public int textColorGreen = 0;
	public int textColorBlue = 0;
	public int menuColorRed = 0;
	public int menuColorGreen = 0;
	public int menuColorBlue = 0;
	public ModuleConfig[] modules;
	public position pos;
	public String title;
	public boolean showTitle = true;


	public boolean showInDebug = false;

	public Overlay() {
		this(position.topLeft, I18n.format("atehud.config.overlay.title.default"), ColorType.inherit, ColorType.inherit);
	}

	public Overlay(position pos, String title, ColorType colorText, ColorType colorMenu, ModuleConfig... modules) {
		this.modules = modules;
		this.pos = pos;
		this.title = title;
		this.colorText = colorText;
		this.colorMenu = colorMenu;
	}
	public int renderOverlay() {
		return 0;
	}
}
