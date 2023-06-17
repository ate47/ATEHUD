package fr.atesab.atehud.superclass;

import java.io.IOException;
import java.util.Arrays;

import fr.atesab.atehud.overlay.Module;
import net.minecraft.client.resources.I18n;

public class ModuleConfig {
	private static abstract class GuiConfigElement {
		private ModuleType type;
		public GuiConfigElement(ModuleType type) {
			this.type = type;
		}
		public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);
		public abstract void getCurrentValue();
		public ModuleType getType() {return type;}
		public abstract void initGui(String value, String ... defaultValue);
		public abstract void keyTyped(char typedChar, int keyCode) throws IOException;
		public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
		public abstract void updateScreen();
	}
	public static class GuiInteger extends GuiConfigElement{
		
		public GuiInteger() {
			super(ModuleType.INT);
		}
		public void drawScreen(int mouseX, int mouseY, float partialTicks) {
			
		}
		public void getCurrentValue() {
			
		}
		public void initGui(String value, String ... defaultValue) {
			
		}
		public void keyTyped(char typedChar, int keyCode) throws IOException {
			
		}
		public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
			
		}
		public void updateScreen() {
			
		}
	}
	public static enum ModuleType {
		INT,
		STRING,
		ENUM,
		BOOLEAN;
	}

	public String name;
	public ModuleType type;

	public String[] defaultValue;

	public ModuleConfig(String name, ModuleType type, String... defaultValue) {
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
	}
	public String getLocalizedName() {
		return "atehud.config.overlay.module." + name;
	}
	public String getName() {
		return I18n.format(getLocalizedName() + ".name");
	}
	@Override
	public String toString() {
		return "ModuleConfig [name=" + name + ", type=" + type + ", defaultValue=" + Arrays.toString(defaultValue)
				+ "]";
	}
}
