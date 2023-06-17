package fr.atesab.atehud.gui;

import java.io.IOException;

import fr.atesab.atehud.Chat;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiXpInformation extends GuiScreen {
	public static int getLevelXp(int lvl) {
		int out = 0;
		if (lvl <= 0) {
			out = 0;
		} else if (lvl > 0 && lvl < 17) {
			out = lvl + 6 * lvl;
		} else if (lvl > 16 && lvl < 32) {
			out = (int) ((2.5 * lvl * lvl) - 40.5 * lvl + 360);
		} else if (lvl > 31) {
			out = (int) (lvl * lvl * 4.5 - 162.5 * lvl + 2220);
		}
		return out;
	}
	public static int getXpToGet(int lvl) {
		int out = 0;
		if (lvl <= 0) {
			out = 0;
		} else if (lvl > 0 && lvl < 17) {
			out = lvl * 2 + 7;
		} else if (lvl > 16 && lvl < 32) {
			out = (int) (5 * lvl - 38);
		} else if (lvl > 31) {
			out = (int) (lvl * 9 - 158);
		}
		return out;
	}

	GuiScreen Last;

	int lvl = 0;

	public GuiXpInformation(GuiScreen last) {
		Last = last;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(Last);
		if (button.id == 1) {
			Chat.show(I18n.format("atehud.setXp"));
			mc.thePlayer.sendChatMessage("/xp ATE47 set " + getLevelXp(lvl));
		}
		super.actionPerformed(button);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, width / 2 + 1, height / 2 - 63, I18n.format("gui.done")));
		buttonList.add(new GuiButton(1, width / 2 - 200, height / 2 - 63, I18n.format("gui.act.give")));
		buttonList.add(new GuiButton(2, width / 2 - 200, height / 2 - 63, I18n.format("gui.act.give")));
		super.initGui();
	}
}
