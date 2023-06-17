package fr.atesab.atehud.gui.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.input.Mouse;

import fr.atesab.atehud.Mod;
import fr.atesab.atehud.Mod.SliderMod;
import fr.atesab.atehud.gui.element.Button.SkinRenderer;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.Minecraft;

public class Slider extends Button {
	public static class ModSlider extends Slider {
		public SliderMod mod;

		public ModSlider(int posX, int posY, float value, SliderMod mod) {
			this(posX, posY, 100, 20, value, mod);
		}

		public ModSlider(int posX, int posY, int width, int height, float value, SliderMod mod) {
			super(mod.getName(), posX, posY, width, height, value);
			this.mod = mod;
			this.value = mod.getValue();
		}

		public void onHover(int mouseX, int mouseY) {
			if (mod.getDesc().length != 0 && !(mod.getDesc()[0].isEmpty() && mod.getDesc().length == 1))
				GuiUtils.drawTextBox(mc.currentScreen, mc, mc.fontRendererObj, mod.getDesc(), mouseX + 5, mouseY + 5,
						Colors.AQUA);
			super.onHover(mouseX, mouseY);
		}

		public void onValueChanged(float value) {
			mod.changeValue(this.value);
			super.onValueChanged(value);
		}
	}
	public interface SkinRenderer {
		public void render(Minecraft mc, Slider sld, int mouseX, int mouseY);
	}
	public static List<SkinRenderer> render = new ArrayList<Slider.SkinRenderer>();
	public static float clamp(float value, float min, float max) {
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}
	public float value;

	public int color = Colors.WHITE;
	
	public boolean isDragged = false;

	public HashMap<Float, String> specialName = new HashMap<Float, String>();

	private int skin = 0;

	public Slider(String text, int posX, int posY, float value) {
		this(text, posX, posY, 100, 20, value, new HashMap<Float, String>());
	}

	public Slider(String text, int posX, int posY, float value, HashMap<Float, String> specialName) {
		this(text, posX, posY, 100, 20, value, specialName);
	}

	public Slider(String text, int posX, int posY, int width, int height, float value) {
		this(text, posX, posY, width, height, value, new HashMap<Float, String>());
	}

	public Slider(String text, int posX, int posY, int width, int height, float value,
			HashMap<Float, String> specialName) {
		super(text, posX, posY, width, height);
		this.value = value;
		this.specialName = specialName;
	}

	private void drawBox(int posX, int posY, int width, int height) {
		GuiUtils.drawBox(posX, posY, posX+width, posY+height, color);
	}

	public int getSkin() {
		return skin;
	}

	public String getText() {
		return this.specialName.containsKey(this.value) ? this.specialName.get(this.value) : (int) (this.value * 100) + "% / 100%";
	}
	public void mouseClick(int mouseX, int mouseY, int button) {
		if(GuiUtils.isHover(this, mouseX, mouseY))this.isDragged = true;
	}
	public void onValueChanged(float value) {}
	public void render(int mouseX, int mouseY) {
		if (this.isDragged && Mouse.isButtonDown(0)) {
			this.value = this.clamp((mouseX-posX)/((float)width-8), 0.0F, 1.0F);
			this.onValueChanged(value);
		} else {
			this.isDragged = false;
		}
		if (visible) {
			if (!(skin < 0 || skin >= render.size()))
				render.get(skin).render(mc, this, mouseX, mouseY);
		}
	}
	public void setSkin(int skin) {
		this.skin = skin;
	}
}
