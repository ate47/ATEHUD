package fr.atesab.atehud.gui;

import java.io.IOException;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class GuiTicTacToe extends GuiScreen {
	public static boolean UseComputer = true;

	public static int score_you = 0, score_comp = 0, score_ties = 0;

	public static String[] textures = { "none", "circle", "cross" };
	public GuiScreen Last;
	private GuiButton soloMode;

	public int[][] game;

	public boolean partyFinish = false;

	public int playerToPlay = 0;

	public GuiTicTacToe(GuiScreen Last) {
		this.Last = Last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(Last);
		if (button.id == 1)
			initGame();
		if (button.id == 2) {
			UseComputer = !UseComputer;
			initGame();
		}
		super.actionPerformed(button);
	}

	public boolean checkSpace(int x, int y) {
		try {
			if (game[x][y] == 0)
				return true;
		} catch (Exception e) {
			Chat.error("Check void error : " + e.getMessage());
		}
		return false;
	}

	public void drawIcon(String name, int posX, int posY) {
		mc.getTextureManager().bindTexture(new ResourceLocation("textures/game/tictactoe/" + name + ".png"));
		drawScaledCustomSizeModalRect(posX, posY, 0, 0, 64, 64, 64, 64, 64, 64);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		soloMode.packedFGColour = Colors.redGreenOptionInt(UseComputer);
		if (UseComputer)
			drawCenteredString(fontRendererObj,
					I18n.format("atehud.game.tictactoe.score") + " : " + I18n.format("atehud.game.tictactoe.score.you")
							+ " " + score_you + " / " + I18n.format("atehud.game.tictactoe.score.computer") + " "
							+ score_comp + " / " + I18n.format("atehud.game.tictactoe.score.ties") + " " + score_ties
							+ "",
					width / 2, height / 2 - 98 - fontRendererObj.FONT_HEIGHT, Colors.WHITE);
		if (win() != 0) {
			drawCenteredString(fontRendererObj,
					I18n.format("atehud.game.tictactoe.win").replaceAll("WINNER", textures[win()]), width / 2,
					height / 2 - 100 - fontRendererObj.FONT_HEIGHT * 2, Colors.GREEN);
		} else if (nobodyWin()) {
			drawCenteredString(fontRendererObj, I18n.format("atehud.game.tictactoe.noWin"), width / 2,
					height / 2 - 100 - fontRendererObj.FONT_HEIGHT * 2, Colors.RED);
		} else if (!UseComputer) {
			drawCenteredString(fontRendererObj,
					I18n.format("atehud.game.tictactoe.turn").replaceAll("PLAYER", textures[playerToPlay + 1]),
					width / 2, height / 2 - 100 - fontRendererObj.FONT_HEIGHT * 2, Colors.WHITE);
		}
		drawIcon(textures[game[0][0]], width / 2 - 96, height / 2 - 96);
		drawIcon(textures[game[1][0]], width / 2 - 32, height / 2 - 96);
		drawIcon(textures[game[2][0]], width / 2 + 32, height / 2 - 96);
		drawIcon(textures[game[0][1]], width / 2 - 96, height / 2 - 32);
		drawIcon(textures[game[1][1]], width / 2 - 32, height / 2 - 32);
		drawIcon(textures[game[2][1]], width / 2 + 32, height / 2 - 32);
		drawIcon(textures[game[0][2]], width / 2 - 96, height / 2 + 32);
		drawIcon(textures[game[1][2]], width / 2 - 32, height / 2 + 32);
		drawIcon(textures[game[2][2]], width / 2 + 32, height / 2 + 32);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGame() {
		partyFinish = false;

		playerToPlay = 0;
		game = new int[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };
	}
	public void initGui() {
		initGame();
		buttonList.add(new GuiButton(0, width / 2 - 150, height / 2 + 100, 99, 20, I18n.format("gui.done")));
		buttonList.add(new GuiButton(1, width / 2 - 50, height / 2 + 100, 99, 20,
				I18n.format("atehud.game.tictactoe.restart")));
		buttonList.add(soloMode = new GuiButton(2, width / 2 + 50, height / 2 + 100, 100, 20,
				I18n.format("atehud.game.tictactoe.computer")));
		super.initGui();
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (GuiUtils.isHover(width / 2 - 96, height / 2 - 96, 64, 64, mouseX, mouseY))
			play(0, 0);
		if (GuiUtils.isHover(width / 2 - 32, height / 2 - 96, 64, 64, mouseX, mouseY))
			play(1, 0);
		if (GuiUtils.isHover(width / 2 + 32, height / 2 - 96, 64, 64, mouseX, mouseY))
			play(2, 0);
		if (GuiUtils.isHover(width / 2 - 96, height / 2 - 32, 64, 64, mouseX, mouseY))
			play(0, 1);
		if (GuiUtils.isHover(width / 2 - 32, height / 2 - 32, 64, 64, mouseX, mouseY))
			play(1, 1);
		if (GuiUtils.isHover(width / 2 + 32, height / 2 - 32, 64, 64, mouseX, mouseY))
			play(2, 1);
		if (GuiUtils.isHover(width / 2 - 96, height / 2 + 32, 64, 64, mouseX, mouseY))
			play(0, 2);
		if (GuiUtils.isHover(width / 2 - 32, height / 2 + 32, 64, 64, mouseX, mouseY))
			play(1, 2);
		if (GuiUtils.isHover(width / 2 + 32, height / 2 + 32, 64, 64, mouseX, mouseY))
			play(2, 2);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public boolean nobodyWin() {
		for (int i = 0; i < game.length; i++)
			for (int j = 0; j < game[i].length; j++) {
				if (game[i][j] == 0)
					return false;
			}
		return true;
	}

	public void onGuiClosed() {
		ModMain.saveConfig();
		super.onGuiClosed();
	}

	public void play(int x, int y) {
		if (!(win() != 0 || nobodyWin()))
			try {
				if (game[x][y] == 0) {
					game[x][y] = playerToPlay + 1;
					if (win() == 1 && UseComputer)
						score_you++;
					if (win() == 2 && UseComputer)
						score_comp++;
					if (nobodyWin() && UseComputer)
						score_ties++;
					playerToPlay = MathHelper.abs_int(playerToPlay - 1);
				}
			} catch (Exception e) {
				Chat.error("Play void error : " + e.getMessage());
			}
		if (playerToPlay == 1 && UseComputer && !(win() != 0 || nobodyWin()))
			playComputer();
	}

	public void playComputer() {
		int[] test = { (int) (Math.random() * 3), (int) (Math.random() * 3) };
		if (toPlay2() != null)
			test = toPlay2();
		if (toPlay1() != null)
			test = toPlay1();
		while (!checkSpace(test[0], test[1])) {
			test = new int[] { (int) (Math.random() * 3), (int) (Math.random() * 3) };
		}
		play(test[0], test[1]);
	}

	public int[] toPlay1() {
		if ((game[0][0] == 2) && (game[1][0] == 2) && (game[2][0] == 0))
			return new int[] { 2, 0 };
		if ((game[0][0] == 2) && (game[1][0] == 0) && (game[2][0] == 2))
			return new int[] { 1, 0 };
		if ((game[0][0] == 0) && (game[1][0] == 2) && (game[2][0] == 2))
			return new int[] { 0, 0 };
		if ((game[0][0] == 2) && (game[0][1] == 2) && (game[0][2] == 0))
			return new int[] { 0, 2 };
		if ((game[0][0] == 2) && (game[0][1] == 0) && (game[0][2] == 2))
			return new int[] { 0, 1 };
		if ((game[0][0] == 0) && (game[0][1] == 2) && (game[0][2] == 2))
			return new int[] { 0, 0 };
		if ((game[0][0] == 2) && (game[1][1] == 2) && (game[2][2] == 0))
			return new int[] { 2, 2 };
		if ((game[0][0] == 2) && (game[1][1] == 0) && (game[2][2] == 2))
			return new int[] { 1, 1 };
		if ((game[0][0] == 0) && (game[1][1] == 2) && (game[2][2] == 2))
			return new int[] { 0, 0 };
		if ((game[1][0] == 2) && (game[1][1] == 2) && (game[1][2] == 0))
			return new int[] { 1, 2 };
		if ((game[1][0] == 2) && (game[1][1] == 0) && (game[1][2] == 2))
			return new int[] { 1, 1 };
		if ((game[1][0] == 0) && (game[1][1] == 2) && (game[1][2] == 2))
			return new int[] { 1, 0 };
		if ((game[0][1] == 2) && (game[1][1] == 2) && (game[2][1] == 0))
			return new int[] { 2, 1 };
		if ((game[0][1] == 2) && (game[1][1] == 0) && (game[2][1] == 2))
			return new int[] { 1, 1 };
		if ((game[0][1] == 0) && (game[1][1] == 2) && (game[2][1] == 2))
			return new int[] { 0, 1 };
		if ((game[0][2] == 2) && (game[1][2] == 2) && (game[2][2] == 0))
			return new int[] { 2, 2 };
		if ((game[0][2] == 2) && (game[1][2] == 0) && (game[2][2] == 2))
			return new int[] { 1, 2 };
		if ((game[0][2] == 0) && (game[1][2] == 2) && (game[2][2] == 2))
			return new int[] { 0, 2 };
		if ((game[2][0] == 2) && (game[2][1] == 2) && (game[2][2] == 0))
			return new int[] { 2, 2 };
		if ((game[2][0] == 2) && (game[2][1] == 0) && (game[2][2] == 2))
			return new int[] { 2, 1 };
		if ((game[2][0] == 0) && (game[2][1] == 2) && (game[2][2] == 2))
			return new int[] { 2, 0 };
		if ((game[0][2] == 2) && (game[1][1] == 2) && (game[2][0] == 0))
			return new int[] { 2, 0 };
		if ((game[0][2] == 2) && (game[1][1] == 0) && (game[2][0] == 2))
			return new int[] { 1, 1 };
		if ((game[0][2] == 0) && (game[1][1] == 2) && (game[2][0] == 2))
			return new int[] { 0, 2 };
		return null;
	}

	public int[] toPlay2() {
		if ((game[0][0] == 1) && (game[1][0] == 1) && (game[2][0] == 0))
			return new int[] { 2, 0 };
		if ((game[0][0] == 1) && (game[1][0] == 0) && (game[2][0] == 1))
			return new int[] { 1, 0 };
		if ((game[0][0] == 0) && (game[1][0] == 1) && (game[2][0] == 1))
			return new int[] { 0, 0 };
		if ((game[0][0] == 1) && (game[0][1] == 1) && (game[0][2] == 0))
			return new int[] { 0, 2 };
		if ((game[0][0] == 1) && (game[0][1] == 0) && (game[0][2] == 1))
			return new int[] { 0, 1 };
		if ((game[0][0] == 0) && (game[0][1] == 1) && (game[0][2] == 1))
			return new int[] { 0, 0 };
		if ((game[0][0] == 1) && (game[1][1] == 1) && (game[2][2] == 0))
			return new int[] { 2, 2 };
		if ((game[0][0] == 1) && (game[1][1] == 0) && (game[2][2] == 1))
			return new int[] { 1, 1 };
		if ((game[0][0] == 0) && (game[1][1] == 1) && (game[2][2] == 1))
			return new int[] { 0, 0 };
		if ((game[1][0] == 1) && (game[1][1] == 1) && (game[1][2] == 0))
			return new int[] { 1, 2 };
		if ((game[1][0] == 1) && (game[1][1] == 0) && (game[1][2] == 1))
			return new int[] { 1, 1 };
		if ((game[1][0] == 0) && (game[1][1] == 1) && (game[1][2] == 1))
			return new int[] { 1, 0 };
		if ((game[0][1] == 1) && (game[1][1] == 1) && (game[2][1] == 0))
			return new int[] { 2, 1 };
		if ((game[0][1] == 1) && (game[1][1] == 0) && (game[2][1] == 1))
			return new int[] { 1, 1 };
		if ((game[0][1] == 0) && (game[1][1] == 1) && (game[2][1] == 1))
			return new int[] { 0, 1 };
		if ((game[0][2] == 1) && (game[1][2] == 1) && (game[2][2] == 0))
			return new int[] { 2, 2 };
		if ((game[0][2] == 1) && (game[1][2] == 0) && (game[2][2] == 1))
			return new int[] { 1, 2 };
		if ((game[0][2] == 0) && (game[1][2] == 1) && (game[2][2] == 1))
			return new int[] { 0, 2 };
		if ((game[2][0] == 1) && (game[2][1] == 1) && (game[2][2] == 0))
			return new int[] { 2, 2 };
		if ((game[2][0] == 1) && (game[2][1] == 0) && (game[2][2] == 1))
			return new int[] { 2, 1 };
		if ((game[2][0] == 0) && (game[2][1] == 1) && (game[2][2] == 1))
			return new int[] { 2, 0 };
		if ((game[0][2] == 1) && (game[1][1] == 1) && (game[2][0] == 0))
			return new int[] { 2, 0 };
		if ((game[0][2] == 1) && (game[1][1] == 0) && (game[2][0] == 1))
			return new int[] { 1, 1 };
		if ((game[0][2] == 0) && (game[1][1] == 1) && (game[2][0] == 1))
			return new int[] { 0, 2 };
		return null;
	}

	public int win() {
		if ((game[0][0] == game[0][1] && game[0][1] == game[0][2] && game[0][1] != 0))
			return game[0][1];
		if ((game[1][0] == game[1][1] && game[1][1] == game[1][2] && game[1][1] != 0))
			return game[1][1];
		if ((game[2][0] == game[2][1] && game[2][1] == game[2][2] && game[2][1] != 0))
			return game[2][1];

		if ((game[0][0] == game[1][0] && game[1][0] == game[2][0] && game[2][0] != 0))
			return game[0][0];
		if ((game[0][1] == game[1][1] && game[1][1] == game[2][1] && game[2][1] != 0))
			return game[0][1];
		if ((game[0][2] == game[1][2] && game[1][2] == game[2][2] && game[2][2] != 0))
			return game[0][2];

		if ((game[0][0] == game[1][1] && game[2][2] == game[1][1] && game[0][0] != 0))
			return game[1][1];
		if ((game[0][2] == game[1][1] && game[2][0] == game[1][1] && game[1][1] != 0))
			return game[1][1];
		return 0;
	}
}
