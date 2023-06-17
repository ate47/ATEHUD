package fr.atesab.atehud.gui;

import java.io.IOException;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.FakeItems;
import fr.atesab.atehud.FakeItems2;
import fr.atesab.atehud.FakeItems3;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GiveItemUtils;
import fr.atesab.atehud.utils.GuiUtils;
import fr.atesab.atehud.utils.ItemHelp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.registry.LanguageRegistry;

public class GuiSkullGiver extends GuiScreen {
	private GuiScreen Last;
	private GuiButton done, give, add;

	public GuiTextField name, links;

	public GuiSkullGiver() {
		Last = null;
	}

	public GuiSkullGiver(GuiScreen last) {
		Last = last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == give) {
			if (!links.getText().isEmpty()) {
				String str = "";
				if (!name.getText().isEmpty()) {
					str = I18n.format("act.head");
					str = str.replaceAll("PLAYERNAME", name.getText());
					str = str.replaceAll("ADDTEXT", "");
				}
				GiveItemUtils.give(mc, ItemHelp.getCustomSkull(links.getText(), str));
			} else
				GiveItemUtils.give(mc, ItemHelp.getHead(name.getText()));
		}
		if (button == done)
			Minecraft.getMinecraft().displayGuiScreen(Last);
		if (button == add) {
			ModMain.addConfig("HeadNames", name.getText());
			Chat.show(I18n.format("gui.act.add.msg"));
		}
		super.actionPerformed(button);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		fontRendererObj.drawString(I18n.format("gui.act.skullfactory"),
				width / 2 - fontRendererObj.getStringWidth(I18n.format("gui.act.skullfactory")) / 2,
				height / 2 - 1 - fontRendererObj.FONT_HEIGHT, Colors.GOLD);
		GuiUtils.drawRightString(fontRendererObj, I18n.format("gui.act.fwf.name") + " : ", width / 2 - 102,
				height / 2 - 22, 20, Colors.GOLD);
		GuiUtils.drawRightString(fontRendererObj, I18n.format("gui.act.skullfactory.link") + " : ", width / 2 - 102,
				height / 2 - 44, 20, Colors.GOLD);
		name.drawTextBox();
		links.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (!mc.thePlayer.capabilities.isCreativeMode)
			GuiUtils.buttonHoverMessage(this, mc, give, mouseX, mouseY, fontRendererObj,
					new String[] { I18n.format("gui.act.nocreative") }, Colors.RED);
	}

	public void initGui() {
		buttonList.add(give = new GuiButton(1, width / 2 - 100, height / 2, 200, 20, I18n.format("gui.act.give")));
		buttonList.add(done = new GuiButton(2, width / 2 - 100, height / 2 + 42, 200, 20, I18n.format("gui.done")));
		buttonList.add(add = new GuiButton(3, width / 2 - 100, height / 2 + 21, 200, 20, I18n.format("gui.act.add")));
		name = new GuiTextField(1, fontRendererObj, width / 2 - 100, height / 2 - 22, 200, 20);
		links = new GuiTextField(2, fontRendererObj, width / 2 - 100, height / 2 - 44, 200, 20);
		links.setMaxStringLength(Integer.MAX_VALUE);
		super.initGui();
	}

	protected void keyTyped(char par1, int par2) throws IOException {
		name.textboxKeyTyped(par1, par2);
		links.textboxKeyTyped(par1, par2);
		super.keyTyped(par1, par2);
	}

	protected void mouseClicked(int x, int y, int btn) throws IOException {
		name.mouseClicked(x, y, btn);
		links.mouseClicked(x, y, btn);
		super.mouseClicked(x, y, btn);
	}

	public void updateScreen() {
		name.updateCursorCounter();
		links.updateCursorCounter();
	}
}
