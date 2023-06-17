package fr.atesab.atehud.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
//TODO: A better menu
public class GuiFakeMessage extends GuiScreen {
	public static int maxFakeMessage = 10;
	public static List messages = Lists.newArrayList();
	GuiScreen Last;
	GuiTextField[] fakeMessage;
	GuiTextField bigFakeMessage;
	String[] prevalue;
	GuiButton send, done;

	public GuiFakeMessage(GuiScreen last) {
		Last = last;

	}

	public GuiFakeMessage(GuiScreen last, String[] preEnterValues) {
		Last = last;
		prevalue = preEnterValues;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			String str = "";
			String a = "";
			for (int i = 0; i < fakeMessage.length; i++) {
				if (!fakeMessage[i].getText().isEmpty()) {
					str += a + fakeMessage[i].getText();
					a = "\n";
				}
			}
			mc.thePlayer.addChatComponentMessage(new ChatComponentText(str));
		}
		if (button.id == 1)
			mc.displayGuiScreen(Last);
		if (button.id == 2) {
			String str = bigFakeMessage.getText().replaceAll("NEWLINE", "\n");
			str = str.replaceAll("&&", new String(new char[] { Chat.c_Modifier }));
			mc.thePlayer.addChatComponentMessage(new ChatComponentText(str));
		}
		super.actionPerformed(button);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for (int i = 0; i < fakeMessage.length; i++) {
			if (fakeMessage[i] != null) {
				fakeMessage[i].drawTextBox();
				GuiUtils.drawRightString(fontRendererObj,
						I18n.format("atehud.menu.fakeMessage.line") + " " + (int) (i + 1), fakeMessage[i].xPosition,
						fakeMessage[i].yPosition, fakeMessage[i].height, Colors.WHITE);
			}
		}
		bigFakeMessage.drawTextBox();
		GuiUtils.drawRightString(fontRendererObj, I18n.format("atehud.menu.fakeMessage.bigline") + " (NEWLINE=\\n)",
				bigFakeMessage.xPosition, bigFakeMessage.yPosition, bigFakeMessage.height, Colors.WHITE);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		fakeMessage = new GuiTextField[maxFakeMessage];
		int i;
		for (i = 0; i < fakeMessage.length; i++) {
			fakeMessage[i] = new GuiTextField(0, fontRendererObj, width / 2 - 199, 21 * i, 399, 20);
			fakeMessage[i].setMaxStringLength(Integer.MAX_VALUE);
			if (prevalue != null && i >= prevalue.length && !prevalue[i].isEmpty()) {
				fakeMessage[i].setText(prevalue[i]);
			}
		}
		bigFakeMessage = new GuiTextField(0, fontRendererObj, width / 2 - 199, 21 * i, 399, 20);
		bigFakeMessage.setMaxStringLength(Integer.MAX_VALUE);
		buttonList.add(new GuiButton(2, width / 2 - 200, 21 * (maxFakeMessage + 1), 199, 20,
				I18n.format("atehud.menu.fakeMessage.sendBig")));
		buttonList.add(send = new GuiButton(0, width / 2 - 200, 21 * (maxFakeMessage + 2), 199, 20,
				I18n.format("atehud.menu.fakeMessage.send")));
		buttonList.add(
				done = new GuiButton(1, width / 2 + 1, 21 * (maxFakeMessage + 2), 199, 20, I18n.format("gui.done")));
		super.initGui();
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for (int i = 0; i < fakeMessage.length; i++) {
			if (fakeMessage[i] != null)
				fakeMessage[i].textboxKeyTyped(typedChar, keyCode);
		}
		bigFakeMessage.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (int i = 0; i < fakeMessage.length; i++) {
			if (fakeMessage[i] != null)
				fakeMessage[i].mouseClicked(mouseX, mouseY, mouseButton);
		}
		bigFakeMessage.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		for (int i = 0; i < fakeMessage.length; i++) {
			if (fakeMessage[i] != null)
				fakeMessage[i].updateCursorCounter();
		}
		bigFakeMessage.updateCursorCounter();
		super.updateScreen();
	}
}
