package fr.atesab.atehud.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class GuiInventory extends GuiScreen {
	public static boolean showCurrent = true;
	public static List<ItemStack> lastInventory = new ArrayList<ItemStack>();
	public static List<ItemStack> currentInventory = new ArrayList<ItemStack>();
	public GuiScreen last;
	public GuiButton give;

	public GuiInventory(GuiScreen Last) {
		last = Last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(last);
		if (button.id == 1) {
			showCurrent = !showCurrent;
			if (showCurrent)
				button.displayString = I18n.format("gui.act.invView.global.current");
			else
				button.displayString = I18n.format("gui.act.invView.global.last");
		}
		if (button.id == 2) {
			List<ItemStack> toChange;
			if (showCurrent)
				toChange = currentInventory;
			else
				toChange = lastInventory;
			lastInventory = currentInventory;
			mc.thePlayer.inventoryContainer.inventoryItemStacks = toChange;
		}
		super.actionPerformed(button);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		List<ItemStack> toDisplay;
		if (!mc.thePlayer.capabilities.isCreativeMode)
			give.enabled = false;
		if (showCurrent)
			toDisplay = currentInventory;
		else
			toDisplay = lastInventory;
		int x1 = 0, y1 = 0;
		ItemStack torender = null;
		for (int i = 0; i < toDisplay.size(); i++) {
			if (toDisplay.get(i) != null) {
				GuiUtils.drawItemStack(itemRender, partialTicks, this, toDisplay.get(i),
						width / 2 - (int) ((float) (4.5 * 21)) + 21 * x1, 40 + 21 * y1);
				if (GuiUtils.isHover(width / 2 - (int) ((float) (4.5 * 21)) + 21 * x1, 40 + 21 * y1, 20, 20, mouseX,
						mouseY))
					torender = toDisplay.get(i);
			}
			if (x1 > 8) {
				y1++;
				x1 = 0;
			} else {
				x1++;
			}
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (torender != null)
			renderToolTip(torender, mouseX, mouseY);
		GuiUtils.buttonHoverMessage(this, mc, give, mouseX, mouseY, fontRendererObj,
				I18n.format("gui.act.nocreative").split("::"), Colors.RED);
	}

	public void initGui() {
		buttonList.add(new GuiButton(1, width / 2 - 300, 20, 199, 20, I18n.format("gui.act.invView.global.current")));
		buttonList.add(new GuiButton(0, width / 2 - 99, 20, 198, 20, I18n.format("gui.done")));
		buttonList
				.add(give = new GuiButton(2, width / 2 + 101, 20, 199, 20, I18n.format("gui.act.invView.global.give")));
		super.initGui();
	}

}
