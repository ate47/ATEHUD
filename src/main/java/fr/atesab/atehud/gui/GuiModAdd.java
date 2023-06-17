package fr.atesab.atehud.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.Mod;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.config.GuiValueButton;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public abstract class GuiModAdd extends GuiScreen {
	public GuiScreen last;
	private int page = 0;
	private int elementByPage = 1;
	private List<GuiValueButton> modsList = new ArrayList<GuiValueButton>();
	private GuiButton lastPage;
	private GuiButton nextPage;
	public GuiModAdd(GuiScreen last) {
		this.last = last;
	}
	
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id==0)mc.displayGuiScreen(last);
		else if(button.id==1) {
			page--;
			defineButtons();
		} else if(button.id==2) {
			page++;
			defineButtons();
		} else if(button.id==3) {
			addMod((Mod)((GuiValueButton)button).getValue());
			mc.displayGuiScreen(last);
		}
		super.actionPerformed(button);
	}
	public abstract void addMod(Mod mod);
	private void defineButtons() {
		buttonList.removeAll(modsList);
		modsList.clear();
		
		elementByPage = (height-42)/21;
		lastPage.enabled = page > 0;
		nextPage.enabled = (page+1)*elementByPage < Mod.getMods().size();
		for (int i = page*elementByPage,j=0; i < Mod.getMods().size() && j < (page+1)*elementByPage; i++) {
			Mod m = Mod.getMods().get(i);
			if(ModMain.byPassAC|| m.autoGen) {
				modsList.add(new GuiValueButton(3, width/2-200, 21+21*(j%elementByPage), m.getName(), m));
				j++;
			}
		}
		buttonList.addAll(modsList);
	}
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("atehud.menu.edit.addMod"), width/2, 1, 20, Colors.GOLD);
		for (GuiValueButton b: modsList)
			GuiUtils.drawString(fontRendererObj, " - "+implode(((Mod)b.getValue()).getDesc(), " "), b.xPosition+b.width, b.yPosition, b.height, Colors.WHITE);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	private String implode(String[] a, String s) {
		String o = "";
		for (int i = 0; i < a.length; i++) {
			if(i<0)o+=s;
			o+=a[i];
		}
		return o;
	}
	public void initGui() {
		buttonList.add(new GuiButton(0, width/2-75, height-21, 150, 20, I18n.format("gui.done")));
		buttonList.add(lastPage = new GuiButton(1, width/2-116, height-21, 40, 20, "<"));
		buttonList.add(nextPage = new GuiButton(2, width/2+76, height-21, 40, 20, ">"));
		defineButtons();
		super.initGui();
	}
}
