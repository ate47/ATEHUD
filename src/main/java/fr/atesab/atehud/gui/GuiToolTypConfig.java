package fr.atesab.atehud.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.FakeItems;
import fr.atesab.atehud.FakeItems3;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import fr.atesab.atehud.utils.ItemHelp;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class GuiToolTypConfig extends GuiScreen {
	public GuiScreen Last;
	public GuiButton dura, openG, repair, tagl;

	public GuiToolTypConfig(GuiScreen Last) {
		this.Last = Last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(Last);
		if (button.id == 1)
			ModMain.TTDura = !ModMain.TTDura;
		if (button.id == 2)
			ModMain.TTGiver = !ModMain.TTGiver;
		if (button.id == 3)
			ModMain.TTRepair = !ModMain.TTRepair;
		if (button.id == 4)
			ModMain.TTTags = !ModMain.TTTags;
		super.actionPerformed(button);
	}

	private void drawItemStack(ItemStack stack, int x, int y) {
		GlStateManager.translate(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		this.itemRender.zLevel = 200.0F;
		FontRenderer font = null;
		if (stack != null)
			font = stack.getItem().getFontRenderer(stack);
		if (font == null)
			font = fontRendererObj;
		this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		this.zLevel = 0.0F;
		this.itemRender.zLevel = 0.0F;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		dura.packedFGColour = Colors.redGreenOptionInt(ModMain.TTDura);
		openG.packedFGColour = Colors.redGreenOptionInt(ModMain.TTGiver);
		repair.packedFGColour = Colors.redGreenOptionInt(ModMain.TTRepair);
		tagl.packedFGColour = Colors.redGreenOptionInt(ModMain.TTTags);
		super.drawScreen(mouseX, mouseY, partialTicks);
		ItemStack stack = ItemHelp.getNBT(Items.diamond_pickaxe,
				"{display:{Name:\"Demo\"},ench:[{id:0,lvl:2}],RepairCost:14}");
		stack.setItemDamage(250);
		drawItemStack(stack, width / 2 - 10, height / 2 - 63);
		if (GuiUtils.isHover(width / 2 - 10, height / 2 - 63, 20, 20, mouseX, mouseY))
			this.renderToolTip(stack, mouseX, mouseY);
		if (GuiUtils.isHover(dura, mouseX, mouseY))
			GuiUtils.drawTextBox(this, mc, fontRendererObj, (I18n.format("gui.atehud.TT.example") + " : ::"
					+ Chat.c_Modifier + "b"
					+ I18n.format("gui.atehud.TT.duraInfo").replaceAll("DURABILITY", String.valueOf(458))
							.replaceAll("MAXDURA", String.valueOf(512)).replaceAll("COLOR", Chat.c_Modifier + "a"))
									.split("::"),
					mouseX + 5, mouseY + 5, Colors.WHITE);
		if (GuiUtils.isHover(openG, mouseX, mouseY))
			GuiUtils.drawTextBox(this, mc, fontRendererObj,
					(I18n.format("gui.atehud.TT.example") + " : ::"
							+ I18n.format("gui.act.inhanditem.tt").replaceAll("KEY",
									EnumChatFormatting.AQUA + Keyboard.getKeyName(ModMain.nbtitem.getKeyCode())
											+ EnumChatFormatting.YELLOW)).split("::"),
					mouseX + 5, mouseY + 5, Colors.WHITE);
		if (GuiUtils.isHover(repair, mouseX, mouseY))
			GuiUtils.drawTextBox(this, mc, fontRendererObj,
					(I18n.format("gui.atehud.TT.example") + " : ::" + Chat.c_Modifier + "b"
							+ I18n.format("gui.atehud.TT.rpInfo").replaceAll("REPAIRCOST", String.valueOf(2))
									.replaceAll("COLOR", Chat.c_Modifier + "2")).split("::"),
					mouseX + 5, mouseY + 5, Colors.WHITE);
		if (GuiUtils.isHover(tagl, mouseX, mouseY))
			GuiUtils.drawTextBox(this, mc, fontRendererObj,
					(I18n.format("gui.atehud.TT.example") + " : ::" + Chat.c_Modifier + "b"
							+ I18n.format("gui.atehud.TT.tagListInfo")
									.replaceAll("TAGLIST",
											Chat.c_Modifier + "f[" + Chat.c_Modifier + "e" + "display" + Chat.c_Modifier
													+ "f] ")
									.replace("SIZE", String.valueOf(1))).split("::"),
					mouseX + 5, mouseY + 5, Colors.WHITE);

	}

	public void initGui() {
		buttonList.add(tagl = new GuiButton(4, width / 2 - 100, height / 2 - 42, I18n.format("gui.atehud.TT.tagList")));
		buttonList.add(
				repair = new GuiButton(3, width / 2 - 100, height / 2 - 21, I18n.format("gui.atehud.TT.repairCost")));
		buttonList.add(dura = new GuiButton(1, width / 2 - 100, height / 2, I18n.format("gui.atehud.TT.durability")));
		buttonList.add(openG = new GuiButton(2, width / 2 - 100, height / 2 + 21, I18n.format("gui.atehud.TT.giver")));
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 42, I18n.format("gui.done")));
		super.initGui();
	}

}
