package fr.atesab.atehud.gui.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.KeyBind;
import fr.atesab.atehud.Mod;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.GuiAdvancedMenu;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiModKeySelection extends GuiScreen {
	public GuiScreen last;
	public Mod mod;
	public String cmd;
	public List<KeyBind> oldKeys;
	public List<KeyBind> keys;
	public List<GuiKeyButton> keyButtons = new ArrayList<GuiKeyButton>();
	public List<GuiValueButton> delButtons = new ArrayList<GuiValueButton>();
	public List<GuiValueButton> addButtons = new ArrayList<GuiValueButton>();
	
	public GuiButton lastPage;
	public GuiButton nextPage;
	public int page = 0;
	public int elementByPage = 1;
	public boolean preventDoubleClick = false;
	public GuiModKeySelection(GuiScreen last, Mod mod) {
		this.last = last;
		this.mod = mod;
		this.keys = ModMain.getKeyMod(mod);
		this.oldKeys = new ArrayList<KeyBind>(this.keys);
		this.cmd = "/"+ModMain.theCommand.getCommandName()+" "+ModMain.theCommand.mod.getName()+" "+mod.getId();
	}
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(!preventDoubleClick)return; else preventDoubleClick = false;
		if(button.id==0)mc.displayGuiScreen(last);
		else if(button.id==1) {
			page++;
			defineButtons();
		} else if(button.id==2) {
			page--;
			defineButtons();
		} else if(button.id==3) {
			int id = Integer.valueOf(String.valueOf(((GuiValueButton)button).getValue())).intValue();
			keys.add(id, new KeyBind(0, cmd));
			defineButtons();
		} else if(button.id==4) {
			int id = Integer.valueOf(String.valueOf(((GuiValueButton)button).getValue())).intValue();
			keys.remove(id);
			defineButtons();
		} else if (button.id == 5) {
			((GuiKeyButton)button).setWaitkey(true);
		}
		super.actionPerformed(button);
	}
	private void defineButtons() {
		buttonList.removeAll(keyButtons);
		buttonList.removeAll(addButtons);
		buttonList.removeAll(delButtons);
		
		keyButtons.clear();
		addButtons.clear();
		delButtons.clear();
		
		elementByPage = (height-42)/21;
		lastPage.enabled = page > 0;
		nextPage.enabled = (page+1)*elementByPage < keys.size();
		
		int i;
		for (i = 0; i < keys.size(); i++) {
			keyButtons.add(new GuiKeyButton(5, width/2-80, 21+21*(i%elementByPage), 79, 20, keys.get(i).key));
			addButtons.add(new GuiValueButton(3, width/2, 21+21*(i%elementByPage), 20, 20, Chat.GREEN+"+", i));
			delButtons.add(new GuiValueButton(4, width/2+21, 21+21*(i%elementByPage), 20, 20, Chat.RED+"-", i));
		}
		addButtons.add(new GuiValueButton(3, width/2+1, height-21, 98, 20, Chat.GREEN+"+", i));
		
		buttonList.addAll(keyButtons);
		buttonList.addAll(addButtons);
		buttonList.addAll(delButtons);
	}
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		for (int i = page*elementByPage; i < (page+1)*elementByPage && i < keyButtons.size(); i++)
			GuiUtils.drawRightString(fontRendererObj, i+" : ", keyButtons.get(i).xPosition, keyButtons.get(i).yPosition, keyButtons.get(i).height, Colors.WHITE);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, width/2-100, height-21, 100, 20, I18n.format("gui.done")));
		buttonList.add(nextPage = new GuiButton(1, width/2+101, height-21, 20, 20, ">"));
		buttonList.add(lastPage = new GuiButton(2, width/2-121, height-21, 20, 20, "<"));
		defineButtons();
		super.initGui();
	}
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for (int i = 0; i < keyButtons.size(); i++) {
			GuiKeyButton kbb = keyButtons.get(i);
			if(kbb.isWaitkey()) {
				kbb.setWaitkey(false);
				kbb.setKey(keyCode);
				keys.get(i).key=keyCode;
				return;
			}
		}
		super.keyTyped(typedChar, keyCode);
	}
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		preventDoubleClick = true;
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	@Override
	public void onGuiClosed() {
		ModMain.keyBindingList.removeAll(oldKeys);
		for (KeyBind k: keys) k.command = cmd;
		ModMain.keyBindingList.addAll(keys);
		super.onGuiClosed();
	}
	
}
