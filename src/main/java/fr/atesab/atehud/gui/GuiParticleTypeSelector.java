package fr.atesab.atehud.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.gui.config.GuiValueButton;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumParticleTypes;

public abstract class GuiParticleTypeSelector extends GuiScreen {
	private GuiScreen last;
	private List<GuiValueButton> buttons = new ArrayList<GuiValueButton>();
	private GuiButton lastPage;
	private GuiButton nextPage;
	private EnumParticleTypes[] pt;
	private int page = 0;
	private int elementByPage = 1;
	public GuiParticleTypeSelector(GuiScreen last) {
		this.last = last;
		pt = EnumParticleTypes.class.getEnumConstants();
	}
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id==0)mc.displayGuiScreen(last);
		else if(button.id==1) {
			page++;
			defineButtons();
		} else if(button.id==2) {
			page--;
			defineButtons();
		} else if(button.id==3) {
			saveType(((EnumParticleTypes)((GuiValueButton)button).getValue()));
			mc.displayGuiScreen(last);
		}
		super.actionPerformed(button);
	}
	private void defineButtons() {
		elementByPage = (height-42)/21;
		nextPage.enabled = (page+1) * elementByPage < pt.length;
		lastPage.enabled = page > 0;
		buttonList.removeAll(buttons);
		buttons.clear();
		for (int i = page*elementByPage; i < pt.length && i < (page+1)*elementByPage; i++)
			buttons.add(new GuiValueButton(3, width/2-100, (1+i%elementByPage)*21, pt[i].getParticleName(), pt[i]));
		buttonList.addAll(buttons);
	}
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("atehud.particle.type"), width/2, 0, 20, Colors.GOLD);
		for (GuiValueButton vb: buttons) GuiUtils.drawRightString(fontRendererObj, ((EnumParticleTypes)vb.getValue()).getParticleID()+" : ",
				vb.xPosition, vb.yPosition, vb.height, Colors.WHITE);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, width/2-100, height-21, I18n.format("gui.done")));
		buttonList.add(nextPage = new GuiButton(1, width/2+101, height-21, 20, 20, ">"));
		buttonList.add(lastPage = new GuiButton(2, width/2-121, height-21, 20, 20, "<"));
		defineButtons();
		super.initGui();
	}
	public abstract void saveType(EnumParticleTypes type);
}
