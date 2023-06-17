package fr.atesab.atehud.gui;

import java.io.IOException;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.element.Slider;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumParticleTypes;

public class GuiMoreParticleMod extends GuiScreen {
	private GuiScreen last;
	private Slider slider;
	private GuiButton type;
	private GuiButton onlyCritical;
	public GuiMoreParticleMod(GuiScreen last) {
		this.last = last;
	}
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id==0) mc.displayGuiScreen(last);
		else if(button.id==1) mc.displayGuiScreen(new GuiParticleTypeSelector(this) {
			@Override
			public void saveType(EnumParticleTypes type) {
				ModMain.particleType = type.name();
			}
		});
		else if(button.id==2) button.packedFGColour = Colors.redGreenOptionInt(ModMain.onlyCritical=!ModMain.onlyCritical);
		super.actionPerformed(button);
	}
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("mod.others.particle.name"), width/2, height/2-63, 20, Colors.GOLD);
		GuiUtils.drawRightString(fontRendererObj, I18n.format("atehud.particle.number")+" : ", slider.posX, slider.posY, slider.height, Colors.WHITE);
		GuiUtils.drawRightString(fontRendererObj, I18n.format("atehud.particle.type")+" : ", type.xPosition, type.yPosition, type.height, Colors.WHITE);
		slider.render(mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	@Override
	public void initGui() {
		slider = new Slider("", width/2-100, height/2-21, 200, 20, ((float)ModMain.moreParticles)/100F) {
			public String getText() {
				return String.valueOf((int)(value * 100F));
			}
			@Override
			public void onValueChanged(float value) {
				ModMain.moreParticles = (int)(100F*value);
				super.onValueChanged(value);
			}
		};
		buttonList.add(new GuiButton(0, width/2-100, height/2+21, I18n.format("gui.done")));
		buttonList.add(type = new GuiButton(1, width/2-100, height/2, ModMain.particleType));
		buttonList.add(onlyCritical = new GuiButton(2, width/2-100, height/2-42, I18n.format("atehud.particle.onlyCritical")));
		super.initGui();
	}
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		slider.mouseClick(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	@Override
	public void updateScreen() {
		onlyCritical.packedFGColour = Colors.redGreenOptionInt(ModMain.onlyCritical);
		super.updateScreen();
	}
}
