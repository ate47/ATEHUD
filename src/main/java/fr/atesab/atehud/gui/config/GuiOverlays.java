package fr.atesab.atehud.gui.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.GuiAdvancedMenu;
import fr.atesab.atehud.gui.GuiHudOptions;
import fr.atesab.atehud.gui.element.HoverText;
import fr.atesab.atehud.overlay.Module;
import fr.atesab.atehud.overlay.Overlay;
import fr.atesab.atehud.overlay.Overlay.ColorType;
import fr.atesab.atehud.overlay.Overlay.ModuleConfig;
import fr.atesab.atehud.overlay.Overlay.position;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiOverlays extends GuiScreen {
	public GuiScreen Last;
	public ArrayList<Overlay> values;
	
	public List<GuiValueButton> configButtons = new ArrayList<GuiValueButton>();
	public List<GuiValueButton> addButtons = new ArrayList<GuiValueButton>();
	public List<GuiValueButton> delButtons = new ArrayList<GuiValueButton>();
	public List<GuiValueButton> upButtons = new ArrayList<GuiValueButton>();
	public List<GuiValueButton> downButtons = new ArrayList<GuiValueButton>();
	public GuiButton nextPageButton;
	public GuiButton lastPageButton;
	public int page = 0;
	public int elementByPage=1;
	private boolean preventDoubleClickId=false;
	public GuiOverlays(GuiScreen last) {
		Last = last;
		this.values = ModMain.overlayList;
	}
	protected void actionPerformed(GuiButton button) throws IOException {
		if(!preventDoubleClickId) return; else preventDoubleClickId=false;
		if(button.id == 0) mc.displayGuiScreen(Last);
		if(button.id == 1) {
			final int id = Integer.valueOf(String.valueOf(((GuiValueButton)button).getValue())).intValue();
			mc.displayGuiScreen(new GuiConfigOverlay(this, values.get(id)) {
				public void saveOverlay(Overlay overlay) {
					values.set(id, overlay);
				}
			});
		}
		if(button.id == 2) {
			final int id = Integer.valueOf(String.valueOf(((GuiValueButton)button).getValue())).intValue();
			mc.displayGuiScreen(new GuiNewOverlay(this) {
				@Override
				public void setOverlay(Overlay overlay) {
					values.add(id, overlay);
					defineButtons();
				}
			});
		}
		if(button.id == 3) {
			values.remove(Integer.valueOf(String.valueOf(((GuiValueButton)button).getValue())).intValue());
			defineButtons();
		}
		if(button.id == 4) {
			int id = Integer.valueOf(String.valueOf(((GuiValueButton)button).getValue())).intValue();
			if(id-1>=0) {
				Overlay u = values.get(id-1);
				Overlay d = values.get(id);
				values.set(id, u);
				values.set(id-1, d);
			}
		}
		if(button.id == 5) {
			int id = Integer.valueOf(String.valueOf(((GuiValueButton)button).getValue())).intValue();
			if(id+1<values.size()) {
				Overlay d = values.get(id+1);
				Overlay u = values.get(id);
				values.set(id, d);
				values.set(id+1, u);
			}
		}
		if(button.id == 6) {
			page++;
			defineButtons();
		}
		if(button.id == 7) {
			page--;
			defineButtons();
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
		nextPageButton.enabled = (page+1)*elementByPage < values.size();
		lastPageButton.enabled = page > 0;
		int i;
		for (i = page * elementByPage; i < (page+1)*elementByPage && i < values.size(); i++) {
			configButtons.add(new GuiValueButton(1, width/2-100, 21+(i%elementByPage)*21, 150, 20, I18n.format("atehud.config.overlay.config"), i));
			addButtons.add(new GuiValueButton(2, width/2+51, 21+(i%elementByPage)*21, 20, 20, Chat.GREEN+"+", i));
			delButtons.add(new GuiValueButton(3, width/2+72, 21+(i%elementByPage)*21, 20, 20, Chat.RED+"-", i));
			upButtons.add(new GuiValueButton(4, width/2+93, 21+(i%elementByPage)*21, 20, 20, String.valueOf(Chat.c_arrow_up), i));
			downButtons.add(new GuiValueButton(5, width/2+114, 21+(i%elementByPage)*21, 20, 20, String.valueOf(Chat.c_arrow_down), i));
		}
		addButtons.add(new GuiValueButton(2, width/2+1, height-21, 99, 20, Chat.GREEN+"+", i));
		buttonList.addAll(configButtons);
		buttonList.addAll(addButtons);
		buttonList.addAll(delButtons);
		buttonList.addAll(upButtons);
		buttonList.addAll(downButtons);
	}
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("atehud.config.overlay"), width/2, 0, 20, Colors.GOLD);
		for (int i = page*elementByPage; i < configButtons.size() && i < (page+1)*elementByPage; i++) {
			GuiValueButton b = configButtons.get(i);
			Overlay o = values.get(Integer.valueOf(String.valueOf(((GuiValueButton)b).getValue())).intValue());
			GuiUtils.drawRightString(fontRendererObj, o.title.replaceAll("&", "\u00a7")+" : ", b.xPosition, b.yPosition, b.height, Colors.WHITE);
			if(GuiUtils.isHover(0, b.yPosition, width, b.height, mouseX, mouseY) && mc.currentScreen.equals(this))
				ATEEventHandler.addHoverElement(new HoverText(Colors.WHITE, ov(o)));
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	public void initGui() {
		buttonList.add(new GuiButton(0, width/2-100, height-21, 100, 20, I18n.format("gui.done")));
		buttonList.add(nextPageButton = new GuiButton(6, width/2+101, height-21, 20, 20, ">"));
		buttonList.add(lastPageButton = new GuiButton(7, width/2-121, height-21, 20, 20, "<"));
		defineButtons();
		super.initGui();
	}
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		preventDoubleClickId=true;
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	public void onGuiClosed() {
		ModMain.overlayList = values;
		ModMain.saveConfig();
	}
	private String[] ov(Overlay o) {
		String[] s = new String[o.modules.length+2];
		s[0]=I18n.format("atehud.config.overlay.position")+" : "+
				I18n.format("atehud.config.overlay.position."+Overlay.position.getPos(o.pos));
		s[1]=I18n.format("atehud.config.overlay.modules")+" : ";
		for (int i = 0; i < o.modules.length; i++) {
			if(Module.getModuleById(o.modules[i].id)!=null)
				s[i+2] = "- "+Module.getModuleById(o.modules[i].id).getDisplayName(o.modules[i].args);
		}
		return s;
	}
	public void updateScreen() {
		
		super.updateScreen();
	}
}
