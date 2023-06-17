package fr.atesab.atehud.gui;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiRndGame extends GuiScreen {
	public static int highScore = 0, level = 0;
	public static Color[] clr2 = { Color.GREEN, Color.YELLOW, Color.ORANGE, Color.MAGENTA };
	/*
	 * Dev 1st on a Casio Graph 35+ ;)
	 */
	public GuiScreen Last;

	private boolean inGame = false, waitStart = true;

	private int waitedKey = 0, timeDone = 0, score = 0, time = 0;

	private int currentColor = 0;

	public int[] keys = { Keyboard.KEY_C, Keyboard.KEY_V, Keyboard.KEY_B, Keyboard.KEY_N };

	public GuiRndGame(GuiScreen Last) {
		this.Last = Last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(Last);
		if (button.id == 1)
			resetGame();
		super.actionPerformed(button);
	}
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		if (inGame && !waitStart) {
			time -= level + 1;
			timeDone++;
		}
		if (keys.length == 4) {
			Color[] clr = { Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE };
			if (inGame && time > 0) {
				if (waitedKey == keys[0])
					clr[0] = clr2[currentColor];
				else if (waitedKey == keys[1])
					clr[1] = clr2[currentColor];
				else if (waitedKey == keys[2])
					clr[2] = clr2[currentColor];
				else if (waitedKey == keys[3])
					clr[3] = clr2[currentColor];
			} else {
				clr = new Color[] { Color.RED, Color.RED, Color.RED, Color.RED };
				inGame = false;
			}
			drawCenteredString(fontRendererObj, I18n.format("atehud.game.rndGame"), width / 2,
					height / 2 - 43 - fontRendererObj.FONT_HEIGHT * 3, Colors.WHITE);
			mc.fontRendererObj.drawString(I18n.format("atehud.game.rndGame.highScore") + " " + highScore,
					width / 2 - 100, height / 2 - 42 - fontRendererObj.FONT_HEIGHT * 2, Colors.WHITE);
			mc.fontRendererObj.drawString(I18n.format("atehud.game.rndGame.score") + " " + score, width / 2 - 100,
					height / 2 - 41 - fontRendererObj.FONT_HEIGHT * 1, Colors.WHITE);
			GuiUtils.drawRightString(fontRendererObj, I18n.format("atehud.game.rndGame.time") + " " + time,
					width / 2 + 100, height / 2 - 41 - fontRendererObj.FONT_HEIGHT * 1, fontRendererObj.FONT_HEIGHT,
					Colors.WHITE);
			GuiUtils.drawBox(width / 2 - 100, height / 2 - 40, width / 2 - 60, height / 2, clr[0]);
			drawCenteredString(fontRendererObj, Keyboard.getKeyName(keys[0]), width / 2 - 80,
					height / 2 - 20 - fontRendererObj.FONT_HEIGHT / 2, clr[0].getRGB());
			GuiUtils.drawBox(width / 2 - 50, height / 2 - 40, width / 2 - 10, height / 2, clr[1]);
			drawCenteredString(fontRendererObj, Keyboard.getKeyName(keys[1]), width / 2 - 30,
					height / 2 - 20 - fontRendererObj.FONT_HEIGHT / 2, clr[1].getRGB());
			GuiUtils.drawBox(width / 2 + 10, height / 2 - 40, width / 2 + 50, height / 2, clr[2]);
			drawCenteredString(fontRendererObj, Keyboard.getKeyName(keys[2]), width / 2 + 30,
					height / 2 - 20 - fontRendererObj.FONT_HEIGHT / 2, clr[2].getRGB());
			GuiUtils.drawBox(width / 2 + 60, height / 2 - 40, width / 2 + 100, height / 2, clr[3]);
			drawCenteredString(fontRendererObj, Keyboard.getKeyName(keys[3]), width / 2 + 80,
					height / 2 - 20 - fontRendererObj.FONT_HEIGHT / 2, clr[3].getRGB());
		} else {
			Chat.error("Modified Game Detected => Crash");
			mc.displayGuiScreen(null);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 10, 99, 20, I18n.format("gui.done")));
		buttonList
				.add(new GuiButton(1, width / 2, height / 2 + 10, 100, 20, I18n.format("atehud.game.rndGame.restart")));
		resetGame();
		super.initGui();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (inGame)
			if (keyCode == keys[0])
				if (keyCode == waitedKey) {
					time += 30 - level * 2;
					score += 30 - level * 2;
					setNewWaitKey();
				} else
					time -= level * 10;
			else if (keyCode == keys[1])
				if (keyCode == waitedKey) {
					time += 30 - level * 2;
					score += 30 - level * 2;
					setNewWaitKey();
				} else
					time -= level * 10;
			else if (keyCode == keys[2])
				if (keyCode == waitedKey) {
					time += 30 - level * 2;
					score += 30 - level * 2;
					setNewWaitKey();
				} else
					time -= level * 10;
			else if (keyCode == keys[3])
				if (keyCode == waitedKey) {
					time += 30 - level * 2;
					score += 30 - level * 2;
					setNewWaitKey();
				} else
					time -= level * 10;
		super.keyTyped(typedChar, keyCode);
	}

	public void resetGame() {
		time = 200;
		timeDone = 0;
		setNewWaitKey();
		waitStart = true;
		inGame = true;
	}

	public void setNewWaitKey() {
		waitedKey = keys[(int) (Math.random() * keys.length)];
		waitStart = false;
		inGame = true;
		currentColor++;
		if (currentColor == clr2.length)
			currentColor = 0;
	}
}
