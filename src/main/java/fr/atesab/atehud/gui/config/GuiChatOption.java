package fr.atesab.atehud.gui.config;

import java.io.IOException;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.GuiAdvancedMenu;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiChatOption extends GuiScreen {
	GuiScreen Last;
	GuiButton friend, connect, userS, lockM, songM, date;
	GuiTextField song;

	public GuiChatOption(GuiScreen Last) {
		this.Last = Last;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			if (!(Last instanceof GuiATEHUDConfig))
				ModMain.saveConfig();
			mc.displayGuiScreen(Last);
		}
		if (button.id == 1)
			ModMain.usernameSong = !ModMain.usernameSong;
		if (button.id == 2)
			ModMain.lockMessage = !ModMain.lockMessage;
		if (button.id == 3)
			ModMain.songMessage = !ModMain.songMessage;
		if (button.id == 4) {
			ModMain.infoSong = "note.pling";
			song.setText(ModMain.infoSong);
		}
		if (button.id == 5)
			ModMain.dateChat = !ModMain.dateChat;
		if (button.id == 6)
			ModMain.friendJoinMessage = !ModMain.friendJoinMessage;
		if (button.id == 7)
			ModMain.allJoinMessage = !ModMain.allJoinMessage;

		if (button.id == 8)
			mc.displayGuiScreen(new GuiTS3Config(this));
		if (button.id == 9)
			mc.displayGuiScreen(
					new GuiTextListConfig(this, ModMain.HeyMessage, I18n.format("atehud.chat.songMessage")) {
						public void onUpdateValue(String[] value) {
							ModMain.HeyMessage = value;
						}
					});
		if (button.id == 10)
			mc.displayGuiScreen(
					new GuiTextListConfig(this, ModMain.AntiMessage, I18n.format("atehud.chat.songMessage")) {
						public void onUpdateValue(String[] value) {
							ModMain.AntiMessage = value;
						}
					});
		if (button.id == 11)
			mc.displayGuiScreen(new GuiTextListConfig(this, ModMain.friend, I18n.format("chatOption.friend.friend")) {
				public void onUpdateValue(String[] value) {
					ModMain.friend = value;
				}
			});
		super.actionPerformed(button);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		song.drawTextBox();
		GuiUtils.drawRightString(fontRendererObj, I18n.format("atehud.chat.infoSong") + " : ", song.xPosition - 2,
				song.yPosition, 20, Colors.GOLD);
		userS.packedFGColour = Colors.redGreenOptionInt(ModMain.usernameSong);
		lockM.packedFGColour = Colors.redGreenOptionInt(ModMain.lockMessage);
		songM.packedFGColour = Colors.redGreenOptionInt(ModMain.songMessage);
		date.packedFGColour = Colors.redGreenOptionInt(ModMain.dateChat);
		friend.packedFGColour = Colors.redGreenOptionInt(ModMain.friendJoinMessage);
		connect.packedFGColour = Colors.redGreenOptionInt(ModMain.allJoinMessage);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, width / 2 + 1, height / 2 + 42, 100, 20, I18n.format("gui.done")));
		buttonList.add(
				date = new GuiButton(5, width / 2 - 100, height / 2 + 42, 99, 20, I18n.format("atehud.chat.date")));
		buttonList.add(
				friend = new GuiButton(6, width / 2 - 100, height / 2 + 21, I18n.format("chatOption.friend.friend")));
		buttonList.add(new GuiButton(11, width / 2 + 101, height / 2 + 21, 80, 20, I18n.format("gui.act.factory")));
		buttonList.add(connect = new GuiButton(7, width / 2 - 100, height / 2, I18n.format("chatOption.friend.all")));
		buttonList.add(
				userS = new GuiButton(1, width / 2 - 100, height / 2 - 21, I18n.format("atehud.chat.usernameSong")));
		buttonList.add(
				lockM = new GuiButton(2, width / 2 - 100, height / 2 - 42, I18n.format("atehud.chat.lockMessage")));
		buttonList.add(new GuiButton(10, width / 2 + 101, height / 2 - 42, 80, 20, I18n.format("gui.act.factory")));
		buttonList.add(
				songM = new GuiButton(3, width / 2 - 100, height / 2 - 63, I18n.format("atehud.chat.songMessage")));
		buttonList.add(new GuiButton(9, width / 2 + 101, height / 2 - 63, 80, 20, I18n.format("gui.act.factory")));
		buttonList
				.add(new GuiButton(4, width / 2 + 105, height / 2 - 84, 20, 20, I18n.format("atehud.default.little")));
		buttonList.add(new GuiButton(8, width / 2 - 100, height / 2 + 63, 200, 20, I18n.format("mod.others.ts3.name")));
		song = new GuiTextField(0, fontRendererObj, width / 2 - 98, height / 2 - 83, 196, 18);
		song.setText(ModMain.infoSong);
		super.initGui();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		song.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		song.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void updateScreen() {
		song.updateCursorCounter();
		ModMain.infoSong = song.getText();
		super.updateScreen();
	}
}
