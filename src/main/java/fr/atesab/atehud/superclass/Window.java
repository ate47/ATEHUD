package fr.atesab.atehud.superclass;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

public class Window {
	public ArrayList<GuiButton> guibts;
	public ArrayList<GuiTextField> guitfs;
	public String name;

	public Window(String name, ArrayList<GuiButton> guibts, ArrayList<GuiTextField> guitfs) {
		this.guibts = guibts;
		this.guitfs = guitfs;
		this.name = name;
	}
}
