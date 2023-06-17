package fr.atesab.atehud.overlay;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class OverlayDefaultConfig {
	private String title;
	private String author;
	private String description;
	private Overlay config;
	private ItemStack icon;
	public OverlayDefaultConfig(String title, String author, String description, Overlay config, ItemStack icon) {
		this.title = title;
		this.author = author;
		this.description = description;
		this.config = config;
		this.icon = icon;
	}
	public String getTitle() {
		return I18n.format(title);
	}
	public String getAuthor() {
		return author;
	}
	public String getDescription() {
		return I18n.format(description);
	}
	public Overlay getConfig() {
		return config;
	}
	public ItemStack getIcon() {
		return icon;
	}
	
}
