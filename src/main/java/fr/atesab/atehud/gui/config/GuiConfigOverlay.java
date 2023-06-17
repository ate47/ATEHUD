package fr.atesab.atehud.gui.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.Chat;
import fr.atesab.atehud.gui.GuiAdvancedMenu;
import fr.atesab.atehud.gui.element.HoverText;
import fr.atesab.atehud.overlay.Module;
import fr.atesab.atehud.overlay.Overlay;
import fr.atesab.atehud.overlay.Overlay.ModuleConfig;
import fr.atesab.atehud.overlay.Overlay.position;
import fr.atesab.atehud.overlay.module.Text;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public abstract class GuiConfigOverlay extends GuiScreen {
	public GuiScreen last;
	public Overlay overlay;
	public List<Overlay.ModuleConfig> configs = new ArrayList<Overlay.ModuleConfig>();
	
	public List<GuiValueButton> configButtons = new ArrayList<GuiValueButton>();
	public List<GuiValueButton> addButtons = new ArrayList<GuiValueButton>();
	public List<GuiValueButton> delButtons = new ArrayList<GuiValueButton>();
	public List<GuiValueButton> upButtons = new ArrayList<GuiValueButton>();
	public List<GuiValueButton> downButtons = new ArrayList<GuiValueButton>();
	public GuiTextField name;
	public GuiButton positionSelector;
	public GuiButton ShowTitle;
	public GuiButton ShowInDebug;
	
	public GuiButton nextPageButton;
	public GuiButton lastPageButton;
	
	private int page = 0;
	private int elementByPage=1;
	private boolean preventDoubleClickId=false;
	
	public GuiConfigOverlay(GuiScreen Last, Overlay overlay) {
		last = Last;
		this.overlay = overlay;
		for (Overlay.ModuleConfig m: overlay.modules)
			configs.add(m);
	}
	protected void actionPerformed(GuiButton button) throws IOException {
		if(!preventDoubleClickId) return; else preventDoubleClickId=false;
		if(button.id == 0) mc.displayGuiScreen(last);
		if(button.id == 1) {
			final int id = Integer.valueOf(String.valueOf(((GuiValueButton)button).getValue())).intValue();
			mc.displayGuiScreen(new GuiConfigModule(this, configs.get(id)) {
				public void saveConfig(ModuleConfig config) {
					configs.set(id, config);
				}
			});
		} else if(button.id == 2) {
			final GuiValueButton b = ((GuiValueButton)button);
			mc.displayGuiScreen(new GuiModuleTypeSelection(this) {
				public void setModule(Module module) {
					configs.add(Integer.valueOf(String.valueOf(b.getValue())).intValue(), 
							new Overlay.ModuleConfig(module));
					defineButtons();
				}
			});
		} else if(button.id == 3) {
			configs.remove(Integer.valueOf(String.valueOf(((GuiValueButton)button).getValue())).intValue());
			defineButtons();
		} else if(button.id == 4) {
			int id = Integer.valueOf(String.valueOf(((GuiValueButton)button).getValue())).intValue();
			if(id-1>=0) {
				Overlay.ModuleConfig u = configs.get(id-1);
				Overlay.ModuleConfig d = configs.get(id);
				configs.set(id, u);
				configs.set(id-1, d);
			}
		} else if(button.id == 5) {
			int id = Integer.valueOf(String.valueOf(((GuiValueButton)button).getValue())).intValue();
			if(id+1<configs.size()) {
				Overlay.ModuleConfig d = configs.get(id+1);
				Overlay.ModuleConfig u = configs.get(id);
				configs.set(id, d);
				configs.set(id+1, u);
			}
		} else if(button.id == 6) {
			page++;
			defineButtons();
		} else if(button.id == 7) {
			page--;
			defineButtons();
		} else if(button.id == 8) {
			mc.displayGuiScreen(new GuiOverlayPositionSelector(this) {
				public void savePosition(position pos) {
					overlay.pos = pos;
					positionSelector.displayString = I18n.format("atehud.config.overlay.position."+position.getPos(overlay.pos));
				}
			});
		} else if(button.id == 9) {
			overlay.showTitle = !overlay.showTitle;
		} else if(button.id == 10) {
			overlay.showInDebug = !overlay.showInDebug;
		}
		super.actionPerformed(button);
	}
	private void defineButtons() {
		buttonList.removeAll(configButtons);
		buttonList.removeAll(addButtons);
		buttonList.removeAll(delButtons);
		buttonList.removeAll(upButtons);
		buttonList.removeAll(downButtons);
		
		configButtons.clear();
		addButtons.clear();
		delButtons.clear();
		upButtons.clear();
		downButtons.clear();
		
		elementByPage = (height-42) /21;
		nextPageButton.enabled = (page+1)*elementByPage < configs.size();
		lastPageButton.enabled = page > 0;
		int i;
		for (i = page * elementByPage; i < (page+1)*elementByPage && i < configs.size(); i++) {
			configButtons.add(new GuiValueButton(1, width/2+20, 21+(i%elementByPage)*21, 116, 20, I18n.format("atehud.config.overlay.config"), i));
			addButtons.add(new GuiValueButton(2, width/2+137, 21+(i%elementByPage)*21, 20, 20, Chat.GREEN+"+", i));
			delButtons.add(new GuiValueButton(3, width/2+158, 21+(i%elementByPage)*21, 20, 20, Chat.RED+"-", i));
			upButtons.add(new GuiValueButton(4, width/2+179, 21+(i%elementByPage)*21, 20, 20, String.valueOf(Chat.c_arrow_up), i));
			downButtons.add(new GuiValueButton(5, width/2+200, 21+(i%elementByPage)*21, 20, 20, String.valueOf(Chat.c_arrow_down), i));
		}
		addButtons.add(new GuiValueButton(2, width/2+1, height-21, 98, 20, Chat.GREEN+"+", i));
		
		buttonList.addAll(configButtons);
		buttonList.addAll(addButtons);
		buttonList.addAll(delButtons);
		buttonList.addAll(upButtons);
		buttonList.addAll(downButtons);
	}
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("atehud.config.overlay"), width/2,1, 20, Colors.GOLD);
		GuiUtils.drawRightString(fontRendererObj, I18n.format("atehud.config.overlay.title")+" : ", 
				name.xPosition, name.yPosition, name.height, Colors.GOLD);
		GuiUtils.drawRightString(fontRendererObj, I18n.format("atehud.config.overlay.position")+" : ", 
				positionSelector.xPosition, positionSelector.yPosition, positionSelector.height, Colors.GOLD);
		for (GuiValueButton b: configButtons) {
			ModuleConfig c = configs.get(Integer.valueOf(String.valueOf(((GuiValueButton)b).getValue())).intValue());
			String title = Module.getModuleById(c.id).getDisplayName(c.args);//I18n.format("atehud.config.overlay.module."+c.id+".name")+" : ";
			GuiUtils.drawRightString(fontRendererObj, title+" : ", b.xPosition, 
					b.yPosition, b.height, Colors.WHITE);
		}
		name.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	public void initGui() {
		buttonList.add(new GuiButton(0, width/2-100, height-21, 100, 20, I18n.format("gui.done")));
		buttonList.add(nextPageButton = new GuiButton(6, width/2+101, height-21, 20, 20, ">"));
		buttonList.add(lastPageButton = new GuiButton(7, width/2-121, height-21, 20, 20, "<"));
		name = new GuiTextField(0, fontRendererObj, width/2-198, height/2+2, 156, 16);
		name.setMaxStringLength(Integer.MAX_VALUE);
		name.setText(overlay.title);
		buttonList.add(positionSelector=new GuiButton(8, width/2-200, height/2-21, 159, 20, 
				I18n.format("atehud.config.overlay.position."+position.getPos(overlay.pos))));
		buttonList.add(ShowTitle=new GuiButton(9, width/2-200, height/2+42, 159, 20, 
				I18n.format("atehud.config.overlay.title.show")));
		buttonList.add(ShowInDebug=new GuiButton(10, width/2-200, height/2+21, 159, 20, 
				I18n.format("atehud.config.overlay.debug.show")));
		defineButtons();
		super.initGui();
	}
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		name.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		preventDoubleClickId=true;
		name.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	public void onGuiClosed() {
		overlay.modules = configs.toArray(new ModuleConfig[configs.size()]);
		saveOverlay(overlay);
		super.onGuiClosed();
	}
	public abstract void saveOverlay(Overlay overlay);
	@Override
	public void updateScreen() {
		name.updateCursorCounter();
		overlay.title = name.getText();
		ShowInDebug.packedFGColour=Colors.redGreenOptionInt(overlay.showInDebug);
		ShowTitle.packedFGColour=Colors.redGreenOptionInt(overlay.showTitle);
		super.updateScreen();
	}
}
