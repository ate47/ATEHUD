package fr.atesab.atehud.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.config.GuiValueButton;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.superclass.TextOption;
import fr.atesab.atehud.utils.CommandUtils;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public abstract class GuiSpammerOptionsList extends GuiScreen {
	public GuiScreen last;
	public GuiButton lastPage;
	public GuiButton nextPage;
	public List<GuiValueButton> addButtons = new ArrayList<GuiValueButton>();
	public int page = 0;
	public int elementByPage=1;
	public GuiSpammerOptionsList(GuiScreen last) {
		this.last = last;
	}
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id==0) mc.displayGuiScreen(last);
		else if(button.id==1) {
			page--;
			defineButtons();
		} else if (button.id==2) {
			page++;
			defineButtons();
		} else if (button.id==3) {
			addOption(button.displayString);
			mc.displayGuiScreen(last);
		}
		super.actionPerformed(button);
	}
	public abstract void addOption(String option);
	private void defineButtons() {
		buttonList.removeAll(addButtons);
		addButtons.clear();
		
		lastPage.enabled = page>0;
		nextPage.enabled = elementByPage*(page+1) < ModMain.textOptions.size();
		for (int i = page*elementByPage; i < ModMain.textOptions.size() && i < (page+1)*elementByPage; i++) {
			TextOption to = ModMain.textOptions.get(i);
			addButtons.add(new GuiValueButton(3, width/2-75, 21+21*(i%elementByPage), 150, 20, "&"+to.getName(), to.getName()));
		}
		
		buttonList.addAll(addButtons);
	}
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("cmd.spammer.optionslist"), width/2, 0, 20, Colors.GOLD);
		for (GuiValueButton b: addButtons) {
			GuiUtils.drawRightString(fontRendererObj, I18n.format("chatOption.to."+String.valueOf(b.getValue()))+" : ", b.xPosition, b.yPosition, b.height, 
					Colors.WHITE);
			GuiUtils.drawString(fontRendererObj, " "+I18n.format("chatOption.to.example")+": &"+String.valueOf(b.getValue())+"="+
					CommandUtils.getOptionText("&"+String.valueOf(b.getValue())), b.xPosition+b.width, b.yPosition, b.height, Colors.WHITE);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	public void initGui() {
		elementByPage = (height-42)/21;
		buttonList.add(new GuiButton(0, width/2-100, height-21, I18n.format("gui.done")));
		buttonList.add(lastPage = new GuiButton(1, width/2-141, height-21, 40, 20, "<"));
		buttonList.add(nextPage = new GuiButton(2, width/2+101, height-21, 40, 20, ">"));
		defineButtons();
		super.initGui();
	}
}
