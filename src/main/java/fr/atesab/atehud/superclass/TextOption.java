package fr.atesab.atehud.superclass;

import net.minecraft.client.Minecraft;

public abstract class TextOption {
	public abstract String getName();
	public abstract String getReplacement(Minecraft mc);
}