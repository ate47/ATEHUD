package fr.atesab.atehud.gui;

import java.io.IOException;

import com.mojang.realmsclient.gui.ChatFormatting;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.superclass.Account;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiAccountsList extends GuiScreen {
	public static class GuiAccountButton extends GuiButton {
		public Account ac;

		public GuiAccountButton(int buttonId, int x, int y, Account Ac) {
			super(buttonId, x, y, Ac.username + " / " + I18n.format("account.mode." + Ac.type));
			ac = Ac;
		}

		public GuiAccountButton(int buttonId, int x, int y, int widthIn, int heightIn, Account Ac) {
			super(buttonId, x, y, widthIn, heightIn, "");
			ac = Ac;
			resetName();
		}

		public void connect() {
			try {
				this.displayString = ac.connect();
			} catch (Exception e) {
				this.displayString = e.getLocalizedMessage();
			}

		}

		public void resetName() {
			this.displayString = ac.username + " / " + I18n.format("account.mode." + ac.type);
		}
	}
	public GuiScreen Last;
	public GuiButton next, back;
	public GuiAccountButton[] accounts;
	public GuiButton[] delete;

	public int page = 0, elm, elmtopsize;

	public GuiAccountsList(GuiScreen last) {
		Last = last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(Last);
		else if (button.id == 1)
			page--;
		else if (button.id == 2)
			page++;
		;
		super.actionPerformed(button);
	}

	public void defineButtons() {
		accounts = new GuiAccountButton[ModMain.accountsList.size()];
		delete = new GuiButton[ModMain.accountsList.size()];
		for (int i = 0; i < accounts.length; i++) {
			Account ac = ModMain.accountsList.get(i);
			accounts[i] = new GuiAccountButton(3 + i, width / 2 - 140, elmtopsize + 21 * i % (elm * 21), ac);
			delete[i] = new GuiButton(3 + accounts.length + i, width / 2 + 65, elmtopsize + 21 * i % (elm * 21), 75, 20,
					I18n.format("account.list.remove"));
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		zLevel = 5F;
		// GuiUtils.drawBackGround(this, 0, 0, width, elmtopsize-1, Colors.GRAY);
		// GuiUtils.drawBackGround(this, height-21, 0, width, height, Colors.GRAY);
		for (int i = page * elm; i < (page + 1) * elm && i < accounts.length; i++) {
			accounts[i].drawButton(mc, mouseX, mouseY);
			delete[i].drawButton(mc, mouseX, mouseY);
		}
		GuiUtils.drawCenterString(fontRendererObj, I18n.format("account.list"), width / 2, 1, Colors.GOLD);
		GuiUtils.drawCenterString(fontRendererObj,
				I18n.format("account.account",
						ChatFormatting.YELLOW + Minecraft.getMinecraft().getSession().getUsername()),
				width / 2, 2 + mc.fontRendererObj.FONT_HEIGHT, Colors.RED);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
		if(mc.theWorld!=null) {
			mc.displayGuiScreen(Last);
			return;
		}
		elmtopsize = mc.fontRendererObj.FONT_HEIGHT * 2 + 3 + 1;
		elm = (height - 21 - elmtopsize) / 21;
		ModMain.saveAccounts();
		defineButtons();
		buttonList.add(new GuiButton(0, width / 2 - 50, height - 21, 99, 20, I18n.format("gui.done")));
		buttonList.add(back = new GuiButton(1, width / 2 - 100, height - 21, 40, 20, "<"));
		buttonList.add(next = new GuiButton(2, width / 2 + 60, height - 21, 40, 20, ">"));
		super.initGui();
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (int i = page * elm; i < (page + 1) * elm && i < accounts.length; i++) {
			if (accounts[i].isMouseOver()) {
				accounts[i].connect();
			} else
				accounts[i].resetName();
			if (delete[i].isMouseOver()) {
				ModMain.accountsList.remove(accounts[i].ac);
				defineButtons();
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void updateScreen() {
		back.enabled = page != 0;
		next.enabled = ModMain.accountsList.size() > (page + 1) * elm;
		super.updateScreen();
	}
}
