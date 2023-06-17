package fr.atesab.atehud.gui;

import fr.atesab.atehud.ModMain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.common.registry.LanguageRegistry;

public class ConfigGUI extends GuiConfig {
	public ConfigGUI(GuiScreen parent) {
		super(parent,
				new ConfigElement(ModMain.configFile.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
				ModMain.MOD_ID, false, false,
				LanguageRegistry.instance().getStringLocalization("gui.act.guifactorytitle"));
	}
}