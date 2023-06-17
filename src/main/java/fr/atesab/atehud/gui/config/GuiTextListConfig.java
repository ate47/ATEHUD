package fr.atesab.atehud.gui.config;

import java.io.IOException;
import java.util.ArrayList;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.GuiAdvancedMenu;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class GuiTextListConfig extends GuiScreen {
	public GuiScreen Last;
	public ArrayList<String> values;
	public GuiTextField[] tfs;
	public GuiButton next, last;
	public GuiButton[] btsDel, btsAdd;
	public String title;
	public int elms;
	public int page = 0;

	public GuiTextListConfig(GuiScreen last, String[] values, String title) {
		Last = last;
		this.values = ModMain.getArray(values);
		this.title = title;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(Last);
		else if (button.id == 1) {
			page--;
			button.enabled = page != 0;
			next.enabled = page + 1 <= values.size() / elms;
		} else if (button.id == 2) {
			page++;
			last.enabled = page != 0;
			button.enabled = page + 1 <= values.size() / elms;
		} else if (button.id == 5) {
			int index = Integer.valueOf(String.valueOf(((GuiValueButton) button).getValue()));
			values.remove(index);
			defineMenu();
		} else if (button.id == 6) {
			values.add(Integer.valueOf(String.valueOf(((GuiValueButton) button).getValue())), "");
			defineMenu();
		}
		super.actionPerformed(button);
	}

	public void defineMenu() {
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height - 21, I18n.format("gui.done")));
		buttonList.add(last = new GuiButton(1, width / 2 - 121, height - 21, 20, 20, "<="));
		buttonList.add(next = new GuiButton(2, width / 2 + 101, height - 21, 20, 20, "=>"));
		last.enabled = page != 0;
		next.enabled = page + 1 <= values.size() / elms;
		tfs = new GuiTextField[values.size()];
		btsDel = new GuiButton[values.size()];
		btsAdd = new GuiButton[values.size() + 1];
		int i;
		for (i = 0; i < values.size(); i++) {
			tfs[i] = new GuiTextField(0, fontRendererObj, width / 2 - 198, 21 + 21 * i % (elms * 21) + 2, 396, 16);
			tfs[i].setMaxStringLength(Integer.MAX_VALUE);
			tfs[i].setText(values.get(i));
			buttonList.add(btsDel[i] = new GuiValueButton(5, width / 2 + 205, 21 + 21 * i % (elms * 21), 20, 20,
					Chat.RED + "-", i));
			buttonList.add(btsAdd[i] = new GuiValueButton(6, width / 2 + 226, 21 + 21 * i % (elms * 21), 20, 20,
					Chat.GREEN + "+", i));
		}
		buttonList.add(btsAdd[i] = new GuiValueButton(6, width / 2 - 100, 21 + 21 * i % (elms * 21), 200, 20,
				Chat.GREEN + "+", i));

	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		// GuiUtils.drawBackGround(this, 0, 0, width, 21, Colors.GRAY);
		// GuiUtils.drawBackGround(this, height-21, 0, width, height, Colors.GRAY);
		super.drawScreen(mouseX, mouseY, partialTicks);
		GuiUtils.drawCenterString(fontRendererObj, title, width / 2, 1, 20, Colors.GOLD);
		for (int i = page * elms; i < (page + 1) * elms && i < tfs.length; i++) {
			GuiUtils.drawRightString(fontRendererObj, i + " : ", tfs[i].xPosition, tfs[i].yPosition, tfs[i].height,
					Colors.WHITE);
			tfs[i].drawTextBox();
		}
	}

	public void initGui() {
		elms = (height - 42) / 21;
		defineMenu();
		super.initGui();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for (int i = page * elms; i < (page + 1) * elms && i < values.size(); i++) {
			tfs[i].textboxKeyTyped(typedChar, keyCode);
		}
		super.keyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (int i = page * elms; i < (page + 1) * elms && i < values.size(); i++) {
			tfs[i].mouseClicked(mouseX, mouseY, mouseButton);
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void onGuiClosed() {
		onUpdateValue(values.toArray(new String[values.size()]));
		super.onGuiClosed();
	}
	public void onUpdateValue(String[] value) {
	}
	public void updateScreen() {
		for (int i = page * elms; i < (page + 1) * elms && i < values.size(); i++) {
			tfs[i].updateCursorCounter();
			values.set(i, tfs[i].getText());
		}
		for (int i = 0; i < btsAdd.length; i++)
			if (i < (page + 1) * elms && i >= page * elms) {
				if (btsAdd[i] != null)
					btsAdd[i].visible = true;
				if (i < btsDel.length && btsDel[i] != null)
					btsDel[i].visible = true;
			} else {
				if (btsAdd[i] != null)
					btsAdd[i].visible = false;
				if (i < btsDel.length && btsDel[i] != null)
					btsDel[i].visible = false;
			}
		super.updateScreen();
	}
}
