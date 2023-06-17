package fr.atesab.atehud.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import tv.twitch.chat.ChatUserList;

import com.google.common.collect.Lists;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.Chat;
import fr.atesab.atehud.Mod;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.element.Button;
import fr.atesab.atehud.gui.element.ButtonsContainer;
import fr.atesab.atehud.gui.element.Slider;
import fr.atesab.atehud.gui.element.Title;
import fr.atesab.atehud.gui.element.ButtonsContainer.ModContainer;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.LanguageRegistry;

public class GuiMenu extends GuiScreen {
	public static float[][] defaultposition;
	public static float[][] position;

	public static List<TileEntity> uco = Lists.newArrayList();
	public static List<EntityPlayer> ucoE=Lists.newArrayList();
	public static int bmc = 0;
	public static int[] konamiCode = { 0xC8, 0xC8, 0xD0, 0xD0, 0xCB, 0xCD, 0xCB, 0xCD, 0x30, 0x1E };// /\ /\ \/ \/ <- ->
	public static String enableOrNot(boolean value) {
		if(value) return " (" + I18n.format("addServer.resourcePack.enabled") + ")";
		else return "";
	}
	private boolean ucoOpen = false;
	
	private Button ucoB,ucoB2,ucoB3,ucoB4,iva,purge;

	public EntityPlayer hover;

	private GuiScreen Last = null;

	private int nextGuiId = 0;

	private Minecraft mc = Minecraft.getMinecraft();

	// <- -> B A
	public int lastKeyTiped = 0;

	public int konamiPoint = 0;

	public boolean needWait = false;
	public GuiButton addtab, editMod, normalMod, undoMod;
	public List<ModContainer> cons = new ArrayList<ModContainer>();
	public List<ModContainer> backupCons = new ArrayList<ModContainer>();
	public List<ModContainer> toRemove = new ArrayList<ModContainer>();
	public boolean inEditMod = false;
	public GuiMenu() {
		this(null);
	}

	public GuiMenu(GuiScreen last) {
		Last = last;
		mc = Minecraft.getMinecraft();
	}
	
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			ModMain.MenuElements = Mod.DefaultElems();
			reloadMod();
		}
		if (button.id == 1)
			for (int i = 0; i < cons.size(); i++) {
				ModContainer mdc = cons.get(i);
				if (mdc != null) {
					mdc.posX = 0;
					mdc.posY = (40 + (22) * i);
					mdc.open = false;
				}
			}
		if (button.id == 2)
			mc.displayGuiScreen(new GuiAddTab(this));
		if (button.id == 3)
			inEditMod = false;
		if (button.id == 5) {
			cons = new ArrayList<ButtonsContainer.ModContainer>(backupCons);
			inEditMod = false;
		}
		if (button.id == 6) {
			backupCons = new ArrayList<ButtonsContainer.ModContainer>(cons);
			inEditMod = true;
		}
	}
	
	public ButtonsContainer[] container(String[] config) {
		ButtonsContainer[] a = new ButtonsContainer[config.length];
		int i = 0;

		ButtonsContainer[] b = new ButtonsContainer[i];
		return b;
	}
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	public void drawIcon(String name, int posX, int posY) {
		mc.getTextureManager().bindTexture(new ResourceLocation("textures/icons/" + name + ".png"));
		drawScaledCustomSizeModalRect(posX, posY, 0, 0, 64, 64, 64, 64, 64, 64);
	}
	private void drawItemStack(ItemStack stack, int x, int y) {
		GlStateManager.translate(0.0F, 0.0F, 21.0F);
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
		if(ucoOpen){
			ucoB.packedFGColour=Colors.redGreenOptionInt(ModMain.UCOsys);
			ucoB2.packedFGColour=Colors.redGreenOptionInt(ModMain.UCOsysb);
			ucoB3.packedFGColour=Colors.redGreenOptionInt(ModMain.UCOsysp);
			ucoB4.packedFGColour=Colors.redGreenOptionInt(ModMain.byPassAC);
			iva.packedFGColour=Colors.redGreenOptionInt(ModMain.invAdvanced);
			int sizeY=height/fontRendererObj.FONT_HEIGHT;int sizeMax=0,i=0;
			for (i = 0; i < uco.size(); i++) {
				BlockPos p = uco.get(i).getPos();
				String type="?";int color = Colors.WHITE;
				if(uco.get(i) instanceof TileEntityChest){
					color = Colors.RED;
					type="Ch";
				}
				if(uco.get(i) instanceof TileEntityEnderChest){
					color=Colors.getColorWithRGB(0, 0, 255);
					type="EC";
				}
				if(uco.get(i) instanceof TileEntityMobSpawner){
					color=Colors.getColorWithRGB(0, 255,0);
					type="Sp";
				}
				String str=type+" : ["+p.getX()+","+p.getY()+","+p.getZ()+"]";
				drawString(fontRendererObj, str, 150*(i/sizeY), fontRendererObj.FONT_HEIGHT*(i%sizeY), color);
			}
			hover = null;
			for (i = 0; i < ucoE.size(); i++) {
				EntityPlayer p=ucoE.get(i);
				int color=Colors.getColorWithRGB(255, 255,0);
				String str="P : ["+p.posX+","+p.posY+","+p.posZ+"]";
				drawString(fontRendererObj, str, 150*(i/sizeY), fontRendererObj.FONT_HEIGHT*(i%sizeY), color);
				if(GuiUtils.isHover(150*(i/sizeY), fontRendererObj.FONT_HEIGHT*(i%sizeY), 150,fontRendererObj.FONT_HEIGHT, mouseX, mouseY)){
					GuiUtils.drawTextBox(this, mc, fontRendererObj, (I18n.format("gui.act.fwf.name")+" : "+p.getDisplayNameString()+"::-------------::"+I18n.format("gui.atehud.invView.click")).split("::"), mouseX, mouseY, Colors.WHITE);
					hover=p;
				}
			}
			GuiUtils.drawColorBox(this, width/2-100, height/2-63-3-fontRendererObj.FONT_HEIGHT, 200, 63+63+3+fontRendererObj.FONT_HEIGHT, Colors.WHITE);
			ucoB.render(mouseX, mouseY);
			ucoB2.render(mouseX, mouseY);
			ucoB3.render(mouseX, mouseY);
			ucoB4.render(mouseX, mouseY);
			iva.render(mouseX, mouseY);
			purge.render(mouseX, mouseY);
			drawString(fontRendererObj, "UCO Sys", width/2-100, height/2-63-2-fontRendererObj.FONT_HEIGHT, Colors.GOLD);
			if(GuiUtils.isHover(width/2+100-fontRendererObj.getStringWidth("x"), height/2-63-2-fontRendererObj.FONT_HEIGHT, fontRendererObj.getStringWidth("x"), fontRendererObj.FONT_HEIGHT, mouseX, mouseY))
				drawString(fontRendererObj, "x", width/2+100-fontRendererObj.getStringWidth("x"), height/2-63-2-fontRendererObj.FONT_HEIGHT, Colors.DARK_RED);
			else drawString(fontRendererObj, "x", width/2+100-fontRendererObj.getStringWidth("x"), height/2-63-2-fontRendererObj.FONT_HEIGHT, Colors.RED);
		} else {
			editMod.visible = !inEditMod;
			normalMod.visible = inEditMod;
			addtab.visible = inEditMod;
			undoMod.visible = inEditMod;
			if (inEditMod)
				GuiUtils.drawRightString(fontRendererObj, I18n.format("atehud.menu.edit.edit"), width - 1, 1, Colors.RED);
			for (ModContainer mdc: cons)
				if (mdc != null)
					mdc.render(mouseX, mouseY);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private int getNextGuiId() {
		nextGuiId++;
		return nextGuiId;
	}

	public int[] getRelativePosition(float[] Pos) {
		int[] a = { (int) (Pos[0] * width), (int) (Pos[1] * height) };
		return a;
	}

	public void initGui() {
		purge=new Button("LOCK UCO", width/2-100, height/2-63);
		ucoB4=new Button("ByPassAC", width/2-100, height/2-42);
		ucoB=new Button("UCO", width/2-100, height/2-21);
		ucoB2=new Button("UCOInfo", width/2-100, height/2);
		ucoB3=new Button("UCOPlayer", width/2-100, height/2+21);
		iva=new Button("I.V.A.", width/2-100, height/2+42);
		buttonList.add(normalMod = new GuiButton(3, 202, height - 20, 33, 20, I18n.format("gui.done")));
		buttonList.add(addtab = new GuiButton(2, 236, height - 20, 31, 20, Chat.GREEN + "+"));
		buttonList.add(undoMod = new GuiButton(5, 268, height - 20, 33, 20, I18n.format("atehud.menu.edit.undo")));
		buttonList.add(editMod = new GuiButton(6, 202, height - 20, 100, 20, I18n.format("atehud.menu.edit.edit")));
		reloadMod();
		buttonList.add(new GuiButton(0, 0, height - 20, 100, 20, I18n.format("atehud.menu.defaultMenu")));
		buttonList.add(new GuiButton(1, 101, height - 20, 100, 20, I18n.format("atehud.menu.defaultPos")));
		super.initGui();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE || keyCode == ModMain.guifactory.getKeyCode()) {
			mc.displayGuiScreen(Last);
		}
		if (lastKeyTiped == Keyboard.KEY_4 && keyCode == Keyboard.KEY_2)
			Chat.show("Now you know the answer");
		lastKeyTiped = keyCode;
		if (keyCode == konamiCode[konamiPoint]) {
			konamiPoint++;
		} else {
			konamiPoint = 0;
		}
		if (konamiCode.length == konamiPoint) {
			konamiPoint = 0;
			Chat.show("*NOTHING HAPPEN*, What did you expect ?");
		}
		super.keyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if(ucoOpen){
			if(GuiUtils.isHover(
					width/2+100-fontRendererObj.getStringWidth("w"), 
					height/2-63-2-fontRendererObj.FONT_HEIGHT,
					fontRendererObj.getStringWidth("w"),fontRendererObj.FONT_HEIGHT,mouseX,mouseY)) {
				ModMain.saveConfig();
				ucoOpen=false;
			}
			if(GuiUtils.isHover(ucoB, mouseX, mouseY) && ucoB.enabled)ModMain.UCOsys=!ModMain.UCOsys;
			if(GuiUtils.isHover(ucoB2, mouseX, mouseY) && ucoB2.enabled)ModMain.UCOsysb=!ModMain.UCOsysb;
			if(GuiUtils.isHover(ucoB3, mouseX, mouseY) && ucoB3.enabled)ModMain.UCOsysp=!ModMain.UCOsysp;
			if(GuiUtils.isHover(ucoB4, mouseX, mouseY) && ucoB4.enabled)ModMain.byPassAC=!ModMain.byPassAC;
			if(GuiUtils.isHover(iva, mouseX, mouseY) && iva.enabled)ModMain.invAdvanced=!ModMain.invAdvanced;
			if(GuiUtils.isHover(purge, mouseX, mouseY) && purge.enabled){
				if(purge.packedFGColour==Colors.RED){
					purge.packedFGColour=Colors.WHITE;
					ucoB.enabled=true;
					ucoB2.enabled=true;
					ucoB3.enabled=true;
					ucoB4.enabled=true;
					iva.enabled=true;
					ModMain.UCO=true;
					purge.text="Lock UCO";
				} else {
					purge.packedFGColour=Colors.RED;
					ucoB.enabled=false;
					ucoB2.enabled=false;
					ucoB3.enabled=false;
					ucoB4.enabled=false;
					iva.enabled=false;
					ModMain.UCO=false;
					ModMain.UCOsys=false;
					ModMain.UCOsysb=false;
					ModMain.UCOsysp=false;
					ModMain.invAdvanced=false;
					ModMain.byPassAC=false;
					purge.text="UCO Dis";
				}
			}
		} else {
			for (ModContainer mdc: cons)
				if (mdc != null)
					mdc.mouseClick(mouseX, mouseY, mouseButton);
			cons.removeAll(toRemove);
			toRemove.clear();
		}
		//DevMode -> TODO To clear
		//if(ModMain.UCO && ModMain.AdvancedModActived && GuiUtils.isHover(width-10, height-10,10,10, mouseX, mouseY))ucoOpen=true;
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void onGuiClosed() {
		if(inEditMod)return;
		ModMain.MenuElements = Mod.GenElems(cons, mc);
		ModMain.saveConfig();
		super.onGuiClosed();
	}

	public void reloadMod() {
		if(inEditMod)return;
		cons.clear();
		for (int i = 0; i < ModMain.MenuElements.length; i++)
			cons.add(Mod.getContainer(ModMain.MenuElements[i], mc, 0,
					(40 + (fontRendererObj.FONT_HEIGHT + 13) * i)));
	}
}