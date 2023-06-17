package fr.atesab.atehud.gui.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiButton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.gui.GuiAdvancedMenu;
import fr.atesab.atehud.overlay.Module;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;

public abstract class GuiModuleTypeSelection extends GuiScreen {
	private GuiScreen last;
	private List<GuiValueButton> modulesButton = new ArrayList<GuiValueButton>();
	private GuiButton lastPage;
	private GuiButton nextPage;
	private int page = 0;
	private int elementByPage = 1;
	private List<Module> modules;
	public GuiModuleTypeSelection(GuiScreen last) {
		this.last = last;
		this.modules = Module.modules;
	}
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id==0) {
			mc.displayGuiScreen(last);
		} else if(button.id==1) {
			setModule(modules.get(Integer.valueOf(String.valueOf(((GuiValueButton)button).getValue())).intValue()));
			mc.displayGuiScreen(last);
		} else if(button.id==2) {
			page--;
			defineButtons();
		} else if(button.id==3) {
			page++;
			defineButtons();
		}
		super.actionPerformed(button);
	}
	private void defineButtons() {
		buttonList.removeAll(modulesButton);
		modulesButton.clear();
		nextPage.enabled = (page+1)*elementByPage < modules.size();
		lastPage.enabled = !((page-1)<0);
		for (int i = 0; i < modules.size() && i < (page+1)*elementByPage; i++)
			modulesButton.add(new GuiValueButton(1, width/2-100, 21 + 21*(i%elementByPage), 
					I18n.format("atehud.config.overlay.module."+modules.get(i).getModuleId()+".name"), i));
		buttonList.addAll(modulesButton);
	}
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("atehud.config.overlay.modules"), width/2, 1, 20, Colors.GOLD);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	public void initGui() {
		buttonList.add(new GuiButton(0, width/2-50, height-21, 100, 20, I18n.format("gui.done")));
		buttonList.add(lastPage = new GuiButton(2, width/2-100, height-21, 49, 20, "<"));
		buttonList.add(nextPage = new GuiButton(3, width/2+51, height-21, 49, 20, ">"));
		elementByPage = (height-42)/21;
		defineButtons();
		super.initGui();
	}
	public abstract void setModule(Module module);
}
