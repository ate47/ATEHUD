package fr.atesab.atehud.gui;

import java.io.IOException;
import java.util.ArrayList;

import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;

public class GuiButtonList extends GuiScreen {
	public interface GuiModifier {
		public GuiButton[] buttons = new GuiButton[] {};

		public void actionPerformer(int buttonId);
	}
	public GuiScreen Last;
	public GuiModifier modifier;
	public String title;
	public int page = 0, border, elements, maxpage;

	public GuiButton nextPage, lastPage, done;

	public GuiButtonList(GuiScreen Last, GuiModifier modifier, String title) {
		this.Last = Last;
		this.modifier = modifier;
		this.title = title;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.equals(done)) {
			mc.displayGuiScreen(Last);
		} else if (button.equals(nextPage)) {
			if (page + 1 < maxpage)
				page++;
		} else if (button.equals(lastPage)) {
			if (page - 1 >= 0)
				page--;
		} else
			modifier.actionPerformer(button.id);
		super.actionPerformed(button);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// Draw title (Page / MaxPage)
		GuiUtils.drawCenterString(fontRendererObj, title + " (" + (page + 1) + " / " + maxpage + ")", width / 2,
				border / 2 - fontRendererObj.FONT_HEIGHT / 2, Colors.GOLD);
		int x = 0, y = 0;
		for (int i = elements * page; i < elements * (page + 1) && i < modifier.buttons.length; i++) {
			(new GuiButton(modifier.buttons[i].id, -200 + 201 * x, border + 1 + 21 * y, 199, 20,
					modifier.buttons[i].displayString)).drawButton(mc, mouseX, mouseY);
			if (y >= elements / 2) {
				x++;
				y = 0;
			} else
				y++;
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void initGui() {
		border = 22 + fontRendererObj.FONT_HEIGHT;
		elements = MathHelper.clamp_int((width - border * 2) * 2 / 21, 0, modifier.buttons.length);
		maxpage = modifier.buttons.length / elements;
		super.initGui();
	}
}
