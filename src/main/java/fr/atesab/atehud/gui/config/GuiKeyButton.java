package fr.atesab.atehud.gui.config;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

public class GuiKeyButton extends GuiButton {
	private int key;
	private boolean waitkey;
	public GuiKeyButton(int buttonId, int x, int y, int key) {
		super(buttonId, x, y, Keyboard.getKeyName(key));
		this.key = key;
		this.waitkey = false;
	}

	public GuiKeyButton(int buttonId, int x, int y, int widthIn, int heightIn, int key) {
		super(buttonId, x, y, widthIn, heightIn, Keyboard.getKeyName(key));
		this.key = key;
		this.waitkey = false;
	}

	public int getKey() {
		return key;
	}
	public boolean isWaitkey() {
		return waitkey;
	}
	public void setKey(int key) {
		if(key>1) this.key = key;
		else this.key = 0;
		this.displayString = Keyboard.getKeyName(key);
	}
	public void setWaitkey(boolean waitkey) {
		this.waitkey = waitkey;
		if(!waitkey)
			if(key>1)this.displayString = Keyboard.getKeyName(key); else this.displayString = Keyboard.getKeyName(0);
		else this.displayString = I18n.format("atehud.config.keybinding.press");
	}
}
