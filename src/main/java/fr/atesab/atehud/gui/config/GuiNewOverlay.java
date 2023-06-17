package fr.atesab.atehud.gui.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.gui.GuiAdvancedMenu;
import fr.atesab.atehud.overlay.Overlay;
import fr.atesab.atehud.overlay.OverlayDefaultConfig;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public abstract class GuiNewOverlay extends GuiScreen {
	private GuiScreen parent;
	private GuiButton lastPageButton;
	private GuiButton nextPageButton;
	private List<GuiOverlayConfigElement> config = new ArrayList<GuiOverlayConfigElement>();
	private int regenMenu = 0;
	private int page = 0;
	private int elementByPage = 1;
	public GuiNewOverlay(GuiScreen parent) {
		this.parent = parent;
	}
	@Override
	public void initGui() {
		regenMenu++;
		buttonList.add(new GuiButton(0, width/2-100, height-21, 200, 20, I18n.format("gui.done")));
		buttonList.add(nextPageButton = new GuiButton(1, width/2+101, height-21, 20, 20, ">"));
		buttonList.add(lastPageButton = new GuiButton(2, width/2-121, height-21, 20, 20, "<"));
		defineButtons();
		super.initGui();
	}
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id == 0)mc.displayGuiScreen(parent);
		else if(button.id == 1) {
			page++;
			defineButtons();
		} else if(button.id == 2) {
			page--;
			defineButtons();
		}
		super.actionPerformed(button);
	}
	private void defineButtons() {
		elementByPage = (height-42) / 40;
		lastPageButton.enabled = page > 0;
		nextPageButton.enabled = (page+1)*elementByPage < Overlay.overlayDefaultConfigs.size();
		config.clear();
		for (int i = page*elementByPage; i < Overlay.overlayDefaultConfigs.size() && i < (page+1)*elementByPage; i++)
			config.add(new GuiOverlayConfigElement(width/2-200, 21+40*(i%elementByPage), 400, 32, Overlay.overlayDefaultConfigs.get(i)) {
				@Override
				public void onClick(int mouseX, int mouseY) {
					setOverlay(this.overlayDefaultConfig.getConfig());
					mc.displayGuiScreen(parent);
				}
			});
	}
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (int i = page*elementByPage; i < config.size() && i < (page+1)*elementByPage; i++)
			config.get(i).mouseClick(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if(regenMenu==1)parent.drawScreen(mouseX, mouseY, partialTicks);
		else GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		for (int i = page*elementByPage; i < config.size() && i < (page+1)*elementByPage; i++)
			config.get(i).render(mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	public abstract void setOverlay(Overlay overlay);
}
