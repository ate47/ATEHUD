package fr.atesab.atehud.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.utils.CommandUtils;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class GuiSpammer extends GuiScreen {
	public static int getColor(boolean b) {
		if(b)return new Color(85, 255, 85).getRGB();
		else return new Color(255, 85, 85).getRGB();
	}
	public GuiScreen Last;
	public GuiTextField msg,tick,nb;
	public GuiButton send, stoponclose, renderoverlay, showbutton;
	public GuiSpammer(GuiScreen Last){
		this.Last=Last;
	}
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id==0)mc.displayGuiScreen(Last);
		if(button.id==1){
			if(ATEEventHandler.isEnable){
				ATEEventHandler.isEnable=false;
			} else {
				ATEEventHandler.action=0;
				ATEEventHandler.tickw=0;
				ATEEventHandler.isEnable=true;
			}
		}
		if(button.id==2)CommandUtils.sendOptionMessage("/"+ModMain.spamCommand);
		if(button.equals(stoponclose)) ModMain.stopOnClose=!ModMain.stopOnClose;
		if(button.equals(renderoverlay)) ModMain.renderGameOverlay=!ModMain.renderGameOverlay;
		if(button.equals(showbutton)) ModMain.showButton=!ModMain.showButton;
		if(button.id==6)mc.displayGuiScreen(new GuiSpammerOptionsList(this) {
			public void addOption(String option) {
				msg.setFocused(true);
				msg.writeText(option);
			}
		});
		super.actionPerformed(button);
	}
	public boolean doesGuiPauseGame() {
		return false;
	}
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		GuiUtils.drawRightString(fontRendererObj, I18n.format("chatOption.spammer.msg")+" : /",msg.xPosition,msg.yPosition,18,Color.WHITE.getRGB());
		GuiUtils.drawRightString(fontRendererObj, I18n.format("chatOption.spammer.tick")+" : ",tick.xPosition,tick.yPosition,18,Color.WHITE.getRGB());
		GuiUtils.drawRightString(fontRendererObj, I18n.format("chatOption.spammer.nb")+" : ",nb.xPosition,nb.yPosition,18,Color.WHITE.getRGB());
		send.enabled=isEnable();
		msg.drawTextBox();
		tick.drawTextBox();
		nb.drawTextBox();
		if(ATEEventHandler.isEnable && !ModMain.renderGameOverlay)
			GuiUtils.drawRightString(Minecraft.getMinecraft().fontRendererObj, String.valueOf(ATEEventHandler.action) + " / " + String.valueOf(ModMain.numberOfAction), 
					width, 0, Color.CYAN.getRGB());
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	public void initGui() {
		buttonList.add(new GuiButton(6, width/2+1, height/2-42,199,20, I18n.format("cmd.spammer.optionslist")));
		buttonList.add(new GuiButton(2, width/2+1, height/2-21,199,20, I18n.format("chatOption.spammer.try")));
		buttonList.add(new GuiButton(0, width/2+1, height/2+42,199,20, I18n.format("gui.done")));
		buttonList.add(send=new GuiButton(1, width/2+1, height/2,199,20, I18n.format("chatOption.spammer.send")));
		buttonList.add(stoponclose=new GuiButton(3, width/2-200, height/2+21,I18n.format("cmd.spammer.set.stoponclose")));
		buttonList.add(renderoverlay=new GuiButton(4, width/2-200, height/2+42,I18n.format("cmd.spammer.set.renderoverlay")));
		buttonList.add(showbutton=new GuiButton(5, width/2+1, height/2+21,199,20,I18n.format("cmd.spammer.set.showbutton")));
		msg =new GuiTextField(0, fontRendererObj, width/2-200, height/2-41, 198, 18);
		tick=new GuiTextField(0, fontRendererObj, width/2-200, height/2-20, 198, 18);
		nb  =new GuiTextField(0, fontRendererObj, width/2-200, height/2+1, 198, 18);
		msg.setMaxStringLength(99);
		msg.setText(ModMain.spamCommand);
		tick.setText(String.valueOf(ModMain.ticktowait));
		nb.setText(String.valueOf(ModMain.numberOfAction));
		super.initGui();
	}
	private boolean isEnable(){
		boolean a=true;
		a=!msg.getText().isEmpty();
		try {
			Integer.valueOf(tick.getText());
			tick.setTextColor(Color.WHITE.getRGB());
		} catch (Exception e) {
			tick.setTextColor(Color.RED.getRGB());
			a=false;
		}
		try {
			Integer.valueOf(nb.getText());
			nb.setTextColor(Color.WHITE.getRGB());
		} catch (Exception e) {
			nb.setTextColor(Color.RED.getRGB());
			a=false;
		}
		return a;
	}
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		msg.textboxKeyTyped(typedChar, keyCode);
		tick.textboxKeyTyped(typedChar, keyCode);
		nb.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		msg.mouseClicked(mouseX, mouseY, mouseButton);
		tick.mouseClicked(mouseX, mouseY, mouseButton);
		nb.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	public void onGuiClosed() {
		ModMain.saveConfig();
		super.onGuiClosed();
	}
	public void updateScreen() {
		msg.updateCursorCounter();
		tick.updateCursorCounter();
		nb.updateCursorCounter();
		if(ATEEventHandler.isEnable) send.displayString = I18n.format("chatOption.spammer.stop");
		else send.displayString = I18n.format("chatOption.spammer.send");
		ModMain.spamCommand=msg.getText();
		try{ModMain.numberOfAction=Integer.valueOf(nb.getText());}catch (Exception e) {}
		try{ModMain.ticktowait=Integer.valueOf(tick.getText());}catch (Exception e) {}
		stoponclose.packedFGColour = getColor(ModMain.stopOnClose);
		renderoverlay.packedFGColour = getColor(ModMain.renderGameOverlay);
		showbutton.packedFGColour = getColor(ModMain.showButton);
		super.updateScreen();
	}
}