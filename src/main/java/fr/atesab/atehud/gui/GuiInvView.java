package fr.atesab.atehud.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.element.Button;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class GuiInvView extends GuiScreen {
	public class StackButtons extends Button {
		public EntityLivingBase entity;

		public StackButtons(String text, int posX, int posY, EntityLivingBase entity) {
			super(text, posX, posY);
			this.entity = entity;
		}

		public StackButtons(String text, int posX, int posY, int width, int height, EntityLivingBase entity) {
			super(text, posX, posY, width, height);
			this.entity = entity;
		}

		public ItemStack[][] getIts() {
			return its(new ItemStack[] { entity.getEquipmentInSlot(0) }, entity.getInventory());
		}

		public ItemStack[][] its(ItemStack[]... data) {
			return data;
		}

		public void onClick(int mouseX, int mouseY) {
			mc.displayGuiScreen(new GuiItemStackArrayViewer(mc.currentScreen, getIts()));
			super.onClick(mouseX, mouseY);
		}

		public void onHover(int mouseX, int mouseY) {
			ItemStack[][] data = getIts();
			List<String> text1 = Lists.newArrayList();
			int k = 0;
			for (int i = 0; i < data.length; i++)
				for (int j = 0; j < data[i].length; j++)
					if (data[i][j] != null)
						k++;
			/*
			 * if(k!=0){ k=1; text1.add(I18n.format("gui.act.invView.name")+" : "); for (int
			 * i = 0; i < data.length; i++) for (int j = 0; j < data[i].length; j++)
			 * if(data[i][j]!=null){ text1.add(data[i][j].getDisplayName()); k++; } }
			 */
			String ait = "";
			if (mc.gameSettings.advancedItemTooltips)
				ait = Chat.GRAY;
			text1.add(entity.getDisplayName().getFormattedText() + Chat.RESET + ait);
			text1.add(getHealth(entity));
			text1.add(Chat.GREEN + I18n.format("gui.act.invView.coor2") + " : XYZ: " + (int) entity.posX + "/"
					+ (int) entity.posY + "/" + (int) entity.posZ + "");
			text1.add(Chat.GOLD + I18n.format("gui.act.invView.dis2") + " : " + (int) getDistance(entity, mc.thePlayer)
					+ "m");
			List<String> effect = getEffect(entity);
			for (int i = 0; i < effect.size(); i++) {
				text1.add(effect.get(i));
			}
			if (entity instanceof EntityHorse) {
				EntityHorse obj = (EntityHorse) entity;
				text1.add(
						I18n.format("gui.act.invView.horse.jump") + " : " + getJumpFormattedText(GetHorseMaxJump(obj)));
				text1.add(I18n.format("gui.act.invView.horse.speed") + " : "
						+ getSpeedFormattedText(
								obj.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue() * 43)
						+ " m/s");
				text1.add(I18n.format("gui.act.invView.horse.health") + " : " + getHpFormattedText(
						((int) (entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue()) * 10)
								/ 20)
						+ " HP");
			}
			if (entity instanceof EntityWolf && ((EntityWolf) (entity)).getOwner() != null
					&& ((EntityWolf) (entity)).getOwner() instanceof EntityPlayer)
				text1.add(Chat.YELLOW + I18n.format("gui.act.invView.wolf.owner")
						+ ((EntityPlayer) ((EntityWolf) (entity)).getOwner()).getDisplayName().getFormattedText());
			text1.add(Chat.YELLOW + I18n.format("gui.atehud.invView.click"));
			text1.add(Chat.YELLOW + I18n.format("gui.atehud.invView.click2"));
			String[] text = new String[text1.size()];
			for (int i = 0; i < text1.size(); i++) {
				text[i] = text1.get(i);
			}
			// if(text.length>0)
			// GuiUtils.drawTextBox(mc.currentScreen, mc, fontRendererObj, text, mouseX+5,
			// mouseY+5, Colors.AQUA);
			// https://i.imgur.com/ESFv1Bj.png <3
			if (data.length != 0)
				GuiUtils.drawInventory(mc, mouseX, mouseY, text, entity, data);
			super.onHover(mouseX, mouseY);
		}

		public void onRightClick(int mouseX, int mouseY) {
			mc.displayGuiScreen(new GuiEntityOptions(mc.currentScreen, entity));
		}
	}

	public static boolean drawEntityWithInventory = false, drawCoorWithInventory = true, drawDistanceWithPlayer = false;

	public static double perfectSpeed = 14;
	public static double exellentSpeed = 13;
	public static double badSpeed = 9;
	public static double perfectJump = 5.5;
	public static double exellentJump = 5;
	public static double badJump = 2.75;
	public static double perfectHP = 15;
	public static double exellentHP = 14;

	public static double badHP = 10;

	public static double getDistance(double posX, double posY, double posZ, double posX2, double posY2, double posZ2) {
		return Math.sqrt(scare(posX - posX2) + scare(posY - posY2) + scare(posZ - posZ2));
	}
	public static double getDistance(Entity et1, Entity et2) {
		return getDistance(et1.posX, et1.posY, et1.posZ, et2.posX, et2.posY, et2.posZ);
	}
	public static List<String> getEffect(EntityLivingBase entity) {
		List<String> a = new ArrayList<String>() {
		};
		for (Iterator it = entity.getActivePotionEffects().iterator(); it.hasNext();) {
			PotionEffect pe = (PotionEffect) it.next();
			a.add(Chat.RED + I18n.format(pe.getEffectName()) + " " + I18n.format("potion.potency." + pe.getAmplifier())
					+ " " + Chat.GRAY + Potion.getDurationString(pe));
		}
		return a;
	}
	public static String getHealth(EntityLivingBase entity) {
		return getHealth((int) entity.getHealth(), (int) entity.getMaxHealth());
	}
	public static String getHealth(int health, int maxHealth) {
		String a = Chat.WHITE + health + "/" + maxHealth + Chat.DARK_RED;
		return a + Chat.RESET;
	}
	public static double GetHorseMaxJump(EntityHorse horse) {
		double yVelocity = horse.getHorseJumpStrength();
		double jumpHeight = 0;
		while (yVelocity > 0) {
			jumpHeight += yVelocity;
			yVelocity -= 0.08;
			yVelocity *= 0.98;
		}
		return jumpHeight;
	}
	public static String getHpFormattedText(double hp) {
		String a = Chat.GREEN.toString();
		;
		if (hp > exellentHP)
			a = Chat.GOLD.toString();
		if (hp >= perfectHP)
			a = Chat.DARK_RED.toString();
		if (hp < badHP)
			a = Chat.RED.toString();
		return a + hp;
	}
	public static String getJumpFormattedText(double jump) {
		String a = Chat.GREEN.toString();
		;
		if (jump > exellentJump)
			a = Chat.GOLD.toString();
		if (jump >= perfectJump)
			a = Chat.DARK_RED.toString();
		if (jump < badJump)
			a = Chat.RED.toString();
		int jump1 = (int) (jump * 100);
		return a + (double) (jump1 / 100D);
	}
	public static String getSpeedFormattedText(double speed) {
		String a = Chat.GREEN.toString();
		;
		if (speed > exellentSpeed)
			a = Chat.GOLD.toString();
		if (speed >= perfectSpeed)
			a = Chat.DARK_RED.toString();
		if (speed < badSpeed)
			a = Chat.RED.toString();
		int jump1 = (int) (speed * 100);
		return a + (double) (jump1 / 100D);
	}

	public static double scare(double a) {
		return a * a;
	}

	public GuiScreen Last;

	public List ep = Lists.newArrayList();

	public List<StackButtons> btns = Lists.newArrayList();

	public EntityLivingBase[] eps;

	public StackButtons[] btlist;

	public GuiButton done, ent, dis, coor, page1, page2;

	public StackButtons rid;

	public GuiTextField search;

	public int page = 0;

	public int maxValue = 0;

	public GuiInvView(GuiScreen last) {
		Last = last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 2)
			page--;
		if (button.id == 3)
			page++;
		super.actionPerformed(button);
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	public void drawBackground(int posX, int posY, int elementWidth, int elementHeight) {
		int width = posX + elementWidth;
		int height = posY + elementHeight;
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		this.mc.getTextureManager().bindTexture(optionsBackground);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		float f = 32.0F;
		worldrenderer.func_181668_a(4, worldrenderer.getVertexFormat());
		Color c = new Color(4210752);
		GlStateManager.color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
		worldrenderer.func_181662_b((double) posX, (double) height, 0.0D).func_181673_a(0.0D,
				(double) ((float) height / f));
		worldrenderer.func_181662_b((double) width, (double) height, 0.0D).func_181673_a((double) ((float) width / f),
				(double) ((float) height / f));
		worldrenderer.func_181662_b((double) width, (double) posY, 0.0D).func_181673_a((double) ((float) width / f),
				(double) 0);
		worldrenderer.func_181662_b((double) posX, (double) posY, 0.0D).func_181673_a(0.0D, (double) 0);
		tessellator.draw();
	}
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (page == 0)
			page1.enabled = false;
		else
			page1.enabled = true;
		if (maxValue > maxX() * maxY() * (page + 1))
			page2.enabled = true;
		else
			page2.enabled = false;
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		ent.packedFGColour = Colors.redGreenOptionInt(drawEntityWithInventory);
		coor.packedFGColour = Colors.redGreenOptionInt(drawCoorWithInventory);
		dis.packedFGColour = Colors.redGreenOptionInt(drawDistanceWithPlayer);
		Button[] btns = searchButton();
		for (int i = 0; i < btns.length; i++) {
			if (btns[i] != null)
				btns[i].render(mouseX, mouseY);
		}
		drawBackground(0, 0, width, 20);
		drawBackground(0, height - 20, width, 20);
		GuiUtils.drawRightString(fontRendererObj, I18n.format("gui.act.invView.search") + " : ", search.xPosition,
				search.yPosition, 18, Colors.WHITE);
		search.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (rid != null) {
			rid.render(mouseX, mouseY);
			rid.renderHover(mouseX, mouseY);
		}
		for (int i = 0; i < btns.length; i++)
			if (btns[i] != null)
				btns[i].renderHover(mouseX, mouseY);
	}

	public void initGui() {
		int r = 0;
		if (mc.thePlayer.ridingEntity != null && mc.thePlayer.ridingEntity instanceof EntityLivingBase) {
			rid = new StackButtons(
					I18n.format("gui.act.invView.ride") + " : " + mc.thePlayer.ridingEntity.getDisplayName(),
					width / 2 - 150, height - 21, 99, 20, (EntityLivingBase) mc.thePlayer.ridingEntity);
			r = 1;
		}
		buttonList.add(done = new GuiButton(0, width / 2 - 200 - r * 50, height - 21, 99, 20, I18n.format("gui.done")));
		buttonList.add(ent = new GuiButton(1, width / 2 - 100 + r * 50, height - 21, 99, 20,
				I18n.format("gui.act.invView.entities")));
		buttonList.add(coor = new GuiButton(4, width / 2 + 100 + r * 50, height - 21, 99, 20,
				I18n.format("gui.act.invView.coor")));
		buttonList.add(
				dis = new GuiButton(5, width / 2 + r * 50, height - 21, 99, 20, I18n.format("gui.act.invView.dis")));
		buttonList.add(page1 = new GuiButton(2, width / 2 - 221 - r * 50, height - 21, 20, 20, "<="));
		buttonList.add(page2 = new GuiButton(3, width / 2 + 201 + r * 50, height - 21, 20, 20, "=>"));

		search = new GuiTextField(0, fontRendererObj, width / 2 - 100, 0, 200, 20) {
			private int maxStringLength = Integer.MAX_VALUE;

			public void mouseClicked(int p_146192_1_, int p_146192_2_, int p_146192_3_) {
				if (p_146192_3_ == 1)
					this.setText("");
				super.mouseClicked(p_146192_1_, p_146192_2_, p_146192_3_);
			}
		};
		loadEntity();
		super.initGui();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		search.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	public void loadEntity() {
		btns.clear();
		ep.clear();
		maxValue = 0;
		for (Iterator iterator = mc.theWorld.loadedEntityList.iterator(); iterator.hasNext();) {
			Entity object = (Entity) iterator.next();
			if (object instanceof EntityPlayer || (drawEntityWithInventory && object instanceof EntityLivingBase)) {
				ep.add(object);
				maxValue++;
			}
		}
		btlist = new StackButtons[ep.size()];
		eps = new EntityLivingBase[ep.size()];
		int i = 0;
		int x1 = 0;
		int y1 = 0;
		int sizeY = maxY();
		for (Iterator iterator = ep.iterator(); iterator.hasNext();) {
			EntityLivingBase object = (EntityLivingBase) iterator.next();
			eps[i] = object;
			String location = "";
			if (drawCoorWithInventory)
				location = Chat.GREEN + " (XYZ: " + (int) object.posX + "/" + (int) object.posY + "/"
						+ (int) object.posZ + ")";
			if (drawDistanceWithPlayer)
				location += Chat.GOLD + " " + ((int) (getDistance(object, mc.thePlayer) * 10) / 10D) + "m";
			btlist[i] = new StackButtons(object.getDisplayName().getFormattedText() + location, 201 * x1, 21 + y1 * 21,
					object);
			btns.add(btlist[i]);
			if (y1 >= sizeY - 1) {
				y1 = 0;
				x1++;
			} else {
				y1++;
			}
			if (x1 >= maxX()) {
				y1 = 0;
				x1 = 0;
			}
			i++;
		}
	}

	public int maxX() {
		return width / 200;
	}

	public int maxY() {
		return (height - 42) / 21;
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		search.mouseClicked(mouseX, mouseY, mouseButton);
		Button[] btns = searchButton();
		for (int i = 0; i < btns.length; i++) {
			if (btns[i] != null)
				btns[i].mouseClick(mouseX, mouseY, mouseButton);
		}
		if (GuiUtils.isHover(done, mouseX, mouseY) && mouseButton == 0)
			mc.displayGuiScreen(Last);
		if (GuiUtils.isHover(ent, mouseX, mouseY) && mouseButton == 0) {
			drawEntityWithInventory = !drawEntityWithInventory;
			loadEntity();
		}
		if (GuiUtils.isHover(coor, mouseX, mouseY) && mouseButton == 0) {
			drawCoorWithInventory = !drawCoorWithInventory;
			loadEntity();
		}
		if (GuiUtils.isHover(dis, mouseX, mouseY) && mouseButton == 0) {
			drawDistanceWithPlayer = !drawDistanceWithPlayer;
			loadEntity();
		}
		if (rid != null && GuiUtils.isHover(rid, mouseX, mouseY) && mouseButton == 0) {
			rid.mouseClick(mouseX, mouseY, mouseButton);
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void onGuiClosed() {
		ModMain.saveConfig();
		super.onGuiClosed();
	}

	public Button[] searchButton() {
		Button[] btns = new Button[maxX() * maxY()];
		int j = 0;
		for (int i = maxX() * maxY() * page; i < maxX() * maxY() * (page + 1) && i < btlist.length; i++)
			if (i > 0 && btlist.length > i && btlist[i] != null
					&& btlist[i].text.toLowerCase().contains(search.getText().toLowerCase())) {
				btns[j] = btlist[i];
				j++;
			}
		return btns;
	}

	public void updateScreen() {
		search.updateCursorCounter();
		super.updateScreen();
	}
}
