package fr.atesab.atehud.gui;

import java.io.IOException;

import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GiveItemUtils;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GuiItemStackArrayViewer extends GuiScreen {
	ItemStack[][] isa;
	GuiScreen Last;

	public GuiItemStackArrayViewer(GuiScreen last, ItemStack[]... isa) {
		this.isa = isa;
		Last = last;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(Last);
		super.actionPerformed(button);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		ItemStack torender = null;
		drawCenteredString(fontRendererObj, I18n.format("gui.act.inhanditem.open"), width / 2, 26, Colors.WHITE);
		int x1 = 0, y1 = 0;
		for (int i = 0; i < isa.length; i++)
			for (int j = 0; j < isa[i].length; j++) {
				if (isa[i][j] != null) {
					GuiUtils.drawItemStack(itemRender, partialTicks, this, isa[i][j],
							width / 2 - (int) ((float) (4.5 * 21)) + 21 * x1, 40 + 21 * y1);
					if (GuiUtils.isHover(width / 2 - (int) ((float) (4.5 * 21)) + 21 * x1, 40 + 21 * y1, 20, 20, mouseX,
							mouseY))
						torender = isa[i][j];
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
	}

	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, width / 2 - 100, 5, I18n.format("gui.done")));
		super.initGui();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		int x1 = 0, y1 = 0;
		for (int i = 0; i < isa.length; i++)
			for (int j = 0; j < isa[i].length; j++) {
				if (isa[i][j] != null)
					if (GuiUtils.isHover(width / 2 - (int) ((float) (4.5 * 21)) + 21 * x1, 30 + 21 * y1, 20, 20, mouseX,
							mouseY)) {
						if (mouseButton == 0)
							mc.displayGuiScreen(new GuiNbtCode(this, isa[i][j]));
						if (mouseButton == 1)
							GiveItemUtils.give(mc, isa[i][j]);
					}
				if (x1 > 8) {
					y1++;
					x1 = 0;
				} else {
					x1++;
				}
			}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
