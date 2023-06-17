package fr.atesab.atehud.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.FakeItems3;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GiveItemUtils;
import fr.atesab.atehud.utils.GuiUtils;
import fr.atesab.atehud.utils.ItemHelp;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

public class GuiNbtCode extends GuiScreen {
	public static String getTextFromIS(ItemStack is) {
		String nbt = "";
		if (is.getTagCompound() != null)
			nbt = is.getTagCompound().toString();
		return ((ResourceLocation) Item.itemRegistry.getNameForObject(is.getItem())).toString() + " " + is.stackSize
				+ " " + is.getMetadata() + " " + nbt;
	}
	public static void loadVisible() {
		loadVisible(null);
	}
	public static void loadVisible(GuiScreen Last) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.theWorld != null) {
			ItemStack is = null;
			TileEntity te = null;
			try {
				BlockPos blockpos = mc.objectMouseOver.getBlockPos();
				IBlockState iblockstate = mc.theWorld.getBlockState(blockpos);
				is = iblockstate.getBlock().getPickBlock(mc.objectMouseOver, mc.theWorld,
						mc.objectMouseOver.getBlockPos());
				te = mc.theWorld.getTileEntity(mc.objectMouseOver.getBlockPos());
			} catch (Exception e) {
			}
			if (is != null) {
				try {
					NBTTagCompound nbt = new NBTTagCompound();
					te.writeToNBT(nbt);
					is.setTagInfo("BlockEntityTag", nbt);
				} catch (Exception e) {
				}
				try {
					NBTTagCompound display = new NBTTagCompound();
					is.setTagInfo("display", display);
				} catch (Exception e) {
				}
				mc.displayGuiScreen(new GuiNbtCode(null, is));
			}
		}
	}
	public static String nullTest(Object test) {
		if (test == null)
			return "";
		return test.toString();
	}
	private String text = "";

	private GuiScreen Last = null;

	private ItemStack nameIS = null;

	private GuiButton useParButton;

	public GuiTextField name;

	String Pre;

	ItemStack PreIs;

	TileEntity PreTi;

	public GuiNbtCode() {
	}

	public GuiNbtCode(GuiScreen last) {
		Last = last;
	}

	public GuiNbtCode(GuiScreen last, ItemStack pre) {
		Last = last;
		PreIs = pre;
	}

	public GuiNbtCode(GuiScreen last, String pre) {
		Last = last;
		Pre = pre;
	}

	public GuiNbtCode(GuiScreen last, TileEntity pre) {
		Last = last;
		PreTi = pre;
	}
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 1)
			Minecraft.getMinecraft().displayGuiScreen(Last);
		if (button.id == 2) {
			ModMain.addConfig("AdvancedItem", name.getText());
			Chat.show(I18n.format("gui.act.add.msg"));
		}
		if (button.id == 3) {
			StringSelection select = new StringSelection(name.getText());
			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			cb.setContents(select, select);
		}
		if (button.id == 4)
			try {
				GiveItemUtils.give(mc, ItemHelp.getGive(name.getText()));
			} catch (NumberInvalidException e) {
				Chat.error(I18n.format("gui.act.cantgive"));
			}
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
		super.drawScreen(mouseX, mouseY, partialTicks);
		String str = I18n.format("gui.act.inhanditem");
		fontRendererObj.drawString(str, width / 2 - fontRendererObj.getStringWidth(str) / 2,
				height / 2 - 23 - fontRendererObj.FONT_HEIGHT, Colors.WHITE);
		drawItemStack(nameIS, width / 2 + 152, height / 2 - 22);
		// GuiContainer
		name.drawTextBox();
		if (nameIS != null && GuiUtils.isHover(width / 2 + 152, height / 2 - 22, 32, 32, mouseX, mouseY))
			this.renderToolTip(nameIS, mouseX, mouseY);
		if (!mc.thePlayer.capabilities.isCreativeMode
				&& GuiUtils.isHover(width / 2 - 50, height / 2 + 21, 99, 20, mouseX, mouseY))
			GuiUtils.drawTextBox(this, mc, fontRendererObj, new String[] { I18n.format("gui.act.nocreative") }, mouseX,
					mouseY, Colors.RED);
		;
	}

	public void initGui() {
		Minecraft mc = Minecraft.getMinecraft();
		if (Pre != null) {
			text = Pre;
		} else if (PreIs != null) {
			nameIS = PreIs;
			ItemStack is = nameIS;
			text = getTextFromIS(is);
		} else if (PreTi != null) {
			String nbt = "";
			if (PreTi.getTileData() != null)
				nbt = PreTi.getTileData().toString();
			text = (nullTest((ResourceLocation) Item.itemRegistry
					.getNameForObject(PreTi.getBlockType().getItem(PreTi.getWorld(), PreTi.getPos())))).toString()
					+ " 1 " + nullTest(PreTi.getBlockMetadata()) + " " + nbt;
		} else {
			if (mc.thePlayer.inventory != null)
				if (mc.thePlayer.inventory.getCurrentItem() != null) {
					nameIS = mc.thePlayer.inventory.getCurrentItem();
					text = ItemHelp.getNBTCode(mc.thePlayer.inventory.getCurrentItem());
				}
		}
		buttonList.add(new GuiButton(1, width / 2 - 150, height / 2, 300, 20, I18n.format("gui.done")));
		buttonList.add(new GuiButton(2, width / 2 - 150, height / 2 + 21, 99, 20, I18n.format("gui.act.add")));
		buttonList.add(new GuiButton(4, width / 2 - 50, height / 2 + 21, 99, 20, I18n.format("gui.act.give")));
		buttonList.add(new GuiButton(3, width / 2 + 50, height / 2 + 21, 100, 20, I18n.format("gui.act.clipload")));
		name = new GuiTextField(1, fontRendererObj, width / 2 - 148, height / 2 - 22, 296, 20);
		name.setMaxStringLength(Integer.MAX_VALUE);
		text = text.replaceAll("" + Chat.c_Modifier, "&&");
		name.setText(text);
		super.initGui();
	}

	protected void keyTyped(char par1, int par2) throws IOException {
		name.textboxKeyTyped(par1, par2);
		super.keyTyped(par1, par2);
	}

	protected void mouseClicked(int x, int y, int btn) throws IOException {
		super.mouseClicked(x, y, btn);
		name.mouseClicked(x, y, btn);
	}

	public void updateScreen() {
		name.updateCursorCounter();
		try {
			nameIS = ItemHelp.getGive(name.getText());
		} catch (Exception e) {
			try {
				nameIS = ItemHelp.getGive("barrier 1 0 {Name:\"" + Chat.RED
						+ I18n.format("key.atehud.nbtitem.error.name").replaceAll("\"", "") + "\",Lore:[\"" + Chat.RED
						+ I18n.format("key.atehud.nbtitem.error.desc").replaceAll("\"", "") + "\"],ench:[]}");
			} catch (NumberInvalidException e1) {
				nameIS = new ItemStack(Blocks.barrier);
				nameIS.setStackDisplayName(Chat.RED + I18n.format("key.atehud.nbtitem.error.name"));
			}
		}
	}
}