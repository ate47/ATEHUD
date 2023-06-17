package fr.atesab.atehud.gui.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.GuiAdvancedMenu;
import fr.atesab.atehud.overlay.Module;
import fr.atesab.atehud.overlay.Overlay;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.superclass.ModuleConfig;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public abstract class GuiConfigModule extends GuiScreen {
	public GuiScreen last;
	public Overlay.ModuleConfig config;
	public List<GuiModuleConfigElement> configElement = new ArrayList<GuiModuleConfigElement>();
	public List<String> values = new ArrayList<String>();
	public GuiButton lastPage;
	public GuiButton nextPage;
	public int page = 0;
	public int elementByPage = 1;
	public Module module;
	private boolean preventDoubleClickId=false;
	public GuiConfigModule(GuiScreen last, Overlay.ModuleConfig config) {
		this.last = last;
		this.config = config;
		values = ModMain.getArray(this.config.args);
	}
	protected void actionPerformed(GuiButton button) throws IOException {
		if(!preventDoubleClickId) return; else preventDoubleClickId=false;
		if(button.id==0)mc.displayGuiScreen(last);
		else if(button.id==1) {
			page--;
			defineButtons();
		} else if(button.id==2) {
			page++;
			defineButtons();
		}
		for (int i = page*elementByPage; i < (page+1)*elementByPage && i < configElement.size(); i++)
			configElement.get(i).actionPerformed(button);
		super.actionPerformed(button);
	}
	private void defineButtons() {
		lastPage.enabled = page > 0;
		nextPage.enabled = (page+1)*elementByPage < configElement.size();
	}
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("atehud.config.overlay.modules"), width/2, 1, 20, Colors.GOLD);
		for (int i = page*elementByPage; i < (page+1)*elementByPage && i < configElement.size(); i++)
			configElement.get(i).drawElement(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	public void initGui() {
		module = Module.getModuleById(config.id);
		if(module==null) {
			System.err.println("Can't find the module \""+config.id+"\"");;
			config.args = new String[] {""};
			config.id = "text";
			module = Module.getModuleById(config.id);
		}
		buttonList.add(new GuiButton(0, width/2-79, height-21, 158, 20, I18n.format("gui.done")));
		buttonList.add(lastPage = new GuiButton(1, width/2-100, height-21, 20, 20, "<"));
		buttonList.add(nextPage = new GuiButton(2, width/2+80, height-21, 20, 20, ">"));
		configElement.clear();
		ArrayList<ModuleConfig> mcs = module.getConfigs(new ArrayList<ModuleConfig>());
		if(mcs.size() > values.size()) //auto-upgrade
			for (int i = values.size(); i < mcs.size(); i++)
				values.add(mcs.get(i).defaultValue[0]);
		elementByPage = (height-42)/21;
		for (int i = 0; i < mcs.size(); i++) {
			ModuleConfig mdc = mcs.get(i);
			GuiModuleConfigElement gmce = new GuiModuleConfigElement(mdc, values.get(i), this, width/2-100, 21+21*(i%elementByPage));
			configElement.add(gmce);
			gmce.initElement(buttonList);
		}
		defineButtons();
		super.initGui();
	}
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for (int i = page*elementByPage; i < (page+1)*elementByPage && i < configElement.size(); i++)
			configElement.get(i).keyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (int i = page*elementByPage; i < (page+1)*elementByPage && i < configElement.size(); i++)
			configElement.get(i).mouseClicked(mouseX, mouseY, mouseButton);
		preventDoubleClickId=true;
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	public void onGuiClosed() {
		config.args = values.toArray(new String[values.size()]);
		saveConfig(config);
		super.onGuiClosed();
	}
	public abstract void saveConfig(Overlay.ModuleConfig config);
	public void updateScreen() {
		for (int i = page*elementByPage; i < (page+1)*elementByPage && i < configElement.size(); i++)
			configElement.get(i).updateElement();
		updateValue();
		super.updateScreen();
	}
	public void updateValue() {
		for (int i = 0; i < configElement.size(); i++)
			values.set(i, String.valueOf(configElement.get(i).getValue()));
	}
}
