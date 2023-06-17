package fr.atesab.atehud.gui.config;

import java.io.IOException;

import fr.atesab.atehud.gui.GuiAdvancedMenu;
import fr.atesab.atehud.overlay.Overlay;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public abstract class GuiOverlayPositionSelector extends GuiScreen {
	public GuiScreen last;
	public GuiOverlayPositionSelector(GuiScreen last) {
		this.last = last;
	}
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id == 0) {
			mc.displayGuiScreen(last);
		} else if (button.id == 1) {
			savePosition((Overlay.position)((GuiValueButton)button).getValue());
			mc.displayGuiScreen(last);
		}
		super.actionPerformed(button);
	}
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("atehud.config.overlay.position"), width/2, height/2-62, 20, Colors.GOLD);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	public void initGui() {
		int i;
		for (i = 0; i < Overlay.position.class.getEnumConstants().length; i++) {
			buttonList.add(new GuiValueButton(1, width/2-150 + 100*(i%3), height/2-41 + 21*(i/3), 99, 20, I18n.format("atehud.config.overlay.position."+i),
					Overlay.position.class.getEnumConstants()[i]));
		}
		buttonList.add(new GuiButton(0, width/2-150 + 100*(i%3), height/2-41 + 21*(i/3), 199, 20, I18n.format("gui.done")));
		super.initGui();
	}
	public abstract void savePosition(Overlay.position pos);
}
