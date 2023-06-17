package fr.atesab.atehud.utils;

import org.apache.commons.lang3.Validate;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

import scala.tools.nsc.doc.model.Public;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL13.GL_SAMPLE_ALPHA_TO_COVERAGE;

import com.google.common.base.Charsets;
import com.mojang.realmsclient.gui.ChatFormatting;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.gui.element.Button;
import fr.atesab.atehud.gui.element.Element;
import fr.atesab.atehud.superclass.Colors;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

public class GuiUtils {
	public static class DelayScreen {
		public int delayTicks;
		public GuiScreen delayScreen;

		public DelayScreen(GuiScreen guiScreen, int delayTicks) {
			this.delayScreen = guiScreen;
			this.delayTicks = delayTicks;
			FMLCommonHandler.instance().bus().register(this);
		}

		@SubscribeEvent
		public void onTick(TickEvent ev) {
			if (ev.phase.equals(Phase.START))
				return;
			if (this.delayTicks-- == 0) {
				FMLCommonHandler.instance().bus().unregister(this);
				Minecraft.getMinecraft().displayGuiScreen(this.delayScreen);
			}
			if (this.delayTicks < 0) {
				FMLCommonHandler.instance().bus().unregister(this);
			}
		}
	}

	private static Minecraft mc = Minecraft.getMinecraft();

	public static void buttonHoverMessage(GuiScreen guiScreen, Minecraft mc, GuiButton button, int mouseX, int mouseY,
			final FontRenderer fontRendererObj, String[] text, int color) {
		if (GuiUtils.isHover(button, mouseX, mouseY))
			GuiUtils.drawTextBox(guiScreen, mc, fontRendererObj, text, mouseX + 5, mouseY + 5, color);
	}

	public static void drawBackGround(int top, int left, int right, int bottom, int color) {
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();// -267386864
		color *= -1;
		drawGradientRect(left, top, right, bottom, color, color);
		GlStateManager.enableRescaleNormal();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.enableDepth();
		GlStateManager.enableLighting();
		GlStateManager.color(1.0F, 1.0F, 1.0F);
	}

	public static void drawBlackBackGround(int posX, int posY, int width, int height) {
		drawGradientRect(posX, posY, posX+width, posY+height, -1072689136, -804253680, 0F);
	}

	public static void drawBox(int x, int y, int width, int height) {
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		int l = -267386864;
		drawGradientRect(x - 3, y - 4, x + width + 3, y - 3, l, l);
		drawGradientRect(x - 3, y + height + 3, x + width + 3, y + height + 4, l, l);
		drawGradientRect(x - 3, y - 3, x + width + 3, y + height + 3, l, l);
		drawGradientRect(x - 4, y - 3, x - 3, y + height + 3, l, l);
		drawGradientRect(x + width + 3, y - 3, x + width + 4, y + height + 3, l, l);
		int i1 = 1347420415;
		int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
		drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, i1, j1);
		drawGradientRect(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, i1, j1);
		drawGradientRect(x - 3, y - 3, x + width + 3, y - 3 + 1, i1, i1);
		drawGradientRect(x - 3, y + height + 2, x + width + 3, y + height + 3, j1, j1);
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.enableRescaleNormal();
	}
	public static void drawBox(int left, int top, int right, int bottom, Color color) {
		GlStateManager.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
		glColor4f(0.0F, 0.0F, 0.0F, 0.9F);
		drawRect(left, top, right, bottom, Colors.getColor(color));
		glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 0.9F);
		drawRect(left, top, right, top + 1, Colors.getColor(color));
		drawRect(left, top, left + 1, bottom, Colors.getColor(color));
		drawRect(right - 1, top, right, bottom, Colors.getColor(color));
		drawRect(left, bottom - 1, right, bottom, Colors.getColor(color));
		GlStateManager.color(1.0F, 1.0F, 1.0F);
	}

	public static void drawBox(int left, int top, int right, int bottom, int color) {
		drawBox(left, top, right, bottom, new Color(color));
	}

	public static void drawCenterString(final FontRenderer fontRendererObj, String text, int x, int y, int color) {
		drawCenterString(fontRendererObj, text, x, y, fontRendererObj.FONT_HEIGHT, color);
	}

	public static void drawCenterString(final FontRenderer fontRendererObj, String text, int x, int y, int HEIGHT,
			int color) {
		fontRendererObj.drawStringWithShadow(text, x - fontRendererObj.getStringWidth(text) / 2,
				y + HEIGHT / 2 - fontRendererObj.FONT_HEIGHT / 2, color);
	}

	public static void drawChunk(int chunkPosX, int chunkPosZ, Color color) {
		try {
			renderOffsetAABB(
					new AxisAlignedBB((double) (chunkPosX * 16) - mc.thePlayer.posX, 0.0D,
							(double) (chunkPosZ * 16) - mc.thePlayer.posZ,
							((double) (chunkPosX * 16) - mc.thePlayer.posX) + 16D, 0.1D,
							((double) (chunkPosZ * 16) - mc.thePlayer.posZ) + 16D),
					0, 0, 0, color.getRed(), color.getGreen(), color.getBlue(), 0.2F);
		} catch (Exception exception) {
		}
	}
	public static void drawColorBox(GuiScreen gs, int x, int y, int width, int height, int color) {
		if (gs != null) {
			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			int l = -267386864;
			drawGradientRect(x - 3, y - 4, x + width + 3, y - 3, l, l);
			drawGradientRect(x - 3, y + height + 3, x + width + 3, y + height + 4, l, l);
			drawGradientRect(x - 3, y - 3, x + width + 3, y + height + 3, l, l);
			drawGradientRect(x - 4, y - 3, x - 3, y + height + 3, l, l);
			drawGradientRect(x + width + 3, y - 3, x + width + 4, y + height + 3, l, l);
			int i1 = -color;
			int j1 = (i1 & color) >> 1 | i1 & -color;
			drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, i1, j1);
			drawGradientRect(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, i1, j1);
			drawGradientRect(x - 3, y - 3, x + width + 3, y - 3 + 1, i1, i1);
			drawGradientRect(x - 3, y + height + 2, x + width + 3, y + height + 3, j1, j1);
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableRescaleNormal();
		}
	}

	public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
		drawGradientRect(left, top, right, bottom, startColor, endColor, 200.0F);
	}

	/**
	 * Draws a rectangle with a vertical gradient between the specified colors (ARGB
	 * format). Args : x1, y1, x2, y2, topColor, bottomColor
	 */
	public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor, float zLevel) {
		float f = (float) (startColor >> 24 & 255) / 255.0F;
		float f1 = (float) (startColor >> 16 & 255) / 255.0F;
		float f2 = (float) (startColor >> 8 & 255) / 255.0F;
		float f3 = (float) (startColor & 255) / 255.0F;
		float f4 = (float) (endColor >> 24 & 255) / 255.0F;
		float f5 = (float) (endColor >> 16 & 255) / 255.0F;
		float f6 = (float) (endColor >> 8 & 255) / 255.0F;
		float f7 = (float) (endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
		worldrenderer.func_181662_b((double) right, (double) top, (double) zLevel).func_181666_a(f1, f2, f3, f)
				.func_181675_d();
		worldrenderer.func_181662_b((double) left, (double) top, (double) zLevel).func_181666_a(f1, f2, f3, f)
				.func_181675_d();
		worldrenderer.func_181662_b((double) left, (double) bottom, (double) zLevel).func_181666_a(f5, f6, f7, f4)
				.func_181675_d();
		worldrenderer.func_181662_b((double) right, (double) bottom, (double) zLevel).func_181666_a(f5, f6, f7, f4)
				.func_181675_d();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	public static void drawInventory(Minecraft mc, int posX, int poxY, ItemStack[]... toRender) {
		drawInventory(mc, posX, poxY, new String[] {}, toRender);
	}

	public static void drawInventory(Minecraft mc, int posX, int posY, String[] addText, EntityLivingBase entity,
			boolean renderBox, ItemStack[]... toRender) {
		int l = addText.length;
		for (int i = 0; i < toRender.length; i++)
			for (int j = 0; j < toRender[i].length; j++)
				if (toRender[i][j] != null)
					l++;
		if (l == 0)
			return;
		int sizeX = 0;
		int sizeY = 0;
		int itemSize = 20;
		if (mc.fontRendererObj.FONT_HEIGHT * 2 + 2 > itemSize)
			itemSize = mc.fontRendererObj.FONT_HEIGHT * 2 + 2;
		for (int i = 0; i < toRender.length; i++)
			for (int j = 0; j < toRender[i].length; j++)
				if (toRender[i][j] != null) {
					sizeY += itemSize;
					String ait = "";
					if (mc.gameSettings.advancedItemTooltips)
						ait = ChatFormatting.GRAY + " ("
								+ ((ResourceLocation) Item.itemRegistry.getNameForObject(toRender[i][j].getItem()))
										.toString()
								+ ")";
					int a = mc.fontRendererObj.getStringWidth(toRender[i][j].getDisplayName() + ait) + 22 + 10;
					if (a > sizeX)
						sizeX = a;
					a = mc.fontRendererObj.getStringWidth(getEnch(toRender[i][j])) + 22 + 10;
					if (a > sizeX)
						sizeX = a;
				}
		for (int i = 0; i < addText.length; i++) {
			sizeY += mc.fontRendererObj.FONT_HEIGHT + 1;
			int a = mc.fontRendererObj.getStringWidth(addText[i]) + 10;
			if (a > sizeX)
				sizeX = a;
		}
		if (entity != null) {
			sizeX += 100;
			if (sizeY < 100)
				sizeY = 100;
		}
		ScaledResolution sr = new ScaledResolution(mc);
		posX += 5;
		posY += 5;
		if (posX + sizeX > sr.getScaledWidth())
			posX -= sizeX + 10;
		if (posY + sizeY > sr.getScaledHeight())
			posY -= sizeY + 10;
		if (posX < 0)
			posX = 0;
		if (posY < 0)
			posY = 0;
		if(mc.currentScreen!=null)
			drawBox(posX + 5, posY + 5, sizeX + 10, sizeY + 10);
		int posY1 = posY + 5;
		for (int i = 0; i < addText.length; i++) {
			mc.fontRendererObj.drawStringWithShadow(addText[i], posX + 5, posY1, Colors.WHITE);
			posY1 += (mc.fontRendererObj.FONT_HEIGHT + 1);
		}
		RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
		for (int i = 0; i < toRender.length; i++)
			for (int j = 0; j < toRender[i].length; j++)
				if (toRender[i][j] != null) {
					String ait = "";
					if (mc.gameSettings.advancedItemTooltips)
						ait = ChatFormatting.GRAY + " ("
								+ ((ResourceLocation) Item.itemRegistry.getNameForObject(toRender[i][j].getItem()))
										.toString()
								+ ")";
					ait = "";
					if (mc.gameSettings.advancedItemTooltips)
						ait = ChatFormatting.GRAY + " ("
								+ ((ResourceLocation) Item.itemRegistry.getNameForObject(toRender[i][j].getItem()))
										.toString()
								+ ")";
					mc.fontRendererObj.drawStringWithShadow(toRender[i][j].getDisplayName() + ait, posX + 26, posY1,
							Colors.AQUA);
					mc.fontRendererObj.drawStringWithShadow(getEnch(toRender[i][j]), posX + 26,
							posY1 + mc.fontRendererObj.FONT_HEIGHT + 1, Colors.LIGHT_PURPLE);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					RenderHelper.enableGUIStandardItemLighting();
					itemRenderer.renderItemAndEffectIntoGUI(toRender[i][j], posX + 5, posY1);
					RenderHelper.disableStandardItemLighting();
					GlStateManager.disableRescaleNormal();
					GlStateManager.disableBlend();
					posY1 += itemSize;
				}
		if (entity != null) {
			GlStateManager.color(1.0F, 1.0F, 1.0F);
			GuiInventory.drawEntityOnScreen(posX + sizeX - 55, posY + 105, 50, 50, 0, entity);
		}
	}

	public static void drawInventory(Minecraft mc, int posX, int posY, String[] addText, EntityLivingBase entity,
			ItemStack[]... toRender) {
		drawInventory(mc, posX, posY, addText, entity, true, toRender);
	}

	public static void drawInventory(Minecraft mc, int posX, int posY, String[] addText, ItemStack[]... toRender) {
		drawInventory(mc, posX, posY, addText, null, toRender);
	}

	public static void drawItemStack(RenderItem itemRender, float zLevel, GuiScreen gui, ItemStack itemstack, int i,
			int j) {
		zLevel = 100.0F;
		itemRender.zLevel = 100.0F;

		GlStateManager.enableDepth();
		itemRender.renderItemAndEffectIntoGUI(itemstack, i, j);
		itemRender.renderItemOverlayIntoGUI(gui.mc.fontRendererObj, itemstack, i, j, null);

		itemRender.zLevel = 0.0F;
		zLevel = 0.0F;
	}

	public static void drawRect(double left, double top, double right, double bottom, int color) {
		Gui.drawRect((int) left, (int) top, (int) right, (int) bottom, color);
	}

	public static void drawRightString(final FontRenderer fontRendererObj, String text, int x, int y, int color) {
		drawRightString(fontRendererObj, text, x, y, fontRendererObj.FONT_HEIGHT, color);
	}

	public static void drawRightString(final FontRenderer fontRendererObj, String text, int x, int y, int HEIGHT,
			int color) {
		fontRendererObj.drawStringWithShadow(text, x - fontRendererObj.getStringWidth(text),
				y + HEIGHT / 2 - fontRendererObj.FONT_HEIGHT / 2, color);
	}

	public static void drawString(final FontRenderer fontRendererObj, String text, int x, int y, int color) {
		fontRendererObj.drawStringWithShadow(text, x, y, color);
	}

	public static void drawString(final FontRenderer fontRendererObj, String text, int x, int y, int HEIGHT,
			int color) {
		fontRendererObj.drawStringWithShadow(text, x, y + HEIGHT / 2 - fontRendererObj.FONT_HEIGHT / 2, color);
	}
	public static void drawTextBox(GuiScreen guiScreen, Minecraft mc, FontRenderer fontRendererObj, String[] text,
			int X, int Y, int TextColor) {
		int maxSize = 0;
		X += 5;
		Y += 5;
		for (int i = 0; i < text.length; i++) {
			if (fontRendererObj.getStringWidth(text[i]) > maxSize)
				maxSize = fontRendererObj.getStringWidth(text[i]);
		}
		if (X + maxSize + 10 > guiScreen.width)
			X = X - maxSize - 10;
		if (Y + (fontRendererObj.FONT_HEIGHT + 1) * text.length + 10 > guiScreen.height)
			Y = Y - ((fontRendererObj.FONT_HEIGHT + 1) * text.length + 10);
		if (X < 0) X = 0;
		if (Y < 0) Y = 0;
		drawBox(X + 5, Y + 5, maxSize + 10, (fontRendererObj.FONT_HEIGHT + 1) * text.length + 10);
		for (int i = 0; i < text.length; i++)
			fontRendererObj.drawString(text[i], X + 10, Y + 10 + (fontRendererObj.FONT_HEIGHT + 1) * i, TextColor);
	}
	public static void drawTexture(GuiScreen gui, int posX, int posY, int width, int height, ResourceLocation bg) {
		gui.mc.getTextureManager().bindTexture(bg);
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
		worldrenderer.func_181662_b((double) posX, (double) posY + (double) height, 0.0D)
				.func_181673_a(0.0D, (double) ((float) 1 + (float) 0)).func_181669_b(64, 64, 64, 255).func_181675_d();
		worldrenderer.func_181662_b((double) posX + (double) width, (double) posY + (double) height, 0.0D)
				.func_181673_a((double) ((float) 1), (double) ((float) 1 + (float) 0)).func_181669_b(64, 64, 64, 255)
				.func_181675_d();
		worldrenderer.func_181662_b((double) posX + (double) width, (double) posY, 0.0D)
				.func_181673_a((double) ((float) 1), (double) 0).func_181669_b(64, 64, 64, 255).func_181675_d();
		worldrenderer.func_181662_b((double) posX, (double) posY, 0.0D).func_181673_a(0.0D, (double) 0)
				.func_181669_b(64, 64, 64, 255).func_181675_d();
		tessellator.draw();
	}

	public static void drawTexture(GuiScreen gui, int posX, int posY, int width, int height, ResourceLocation bg,
			int color) {
		gui.mc.getTextureManager().bindTexture(bg);
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		Color c = new Color(color, true);
		GlStateManager.color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
		worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181709_i);
		worldrenderer.func_181662_b((double) posX, (double) posY + (double) height, 0.0D)
				.func_181673_a(0.0D, (double) ((float) 1 + (float) 0)).func_181669_b(64, 64, 64, 255).func_181675_d();
		worldrenderer.func_181662_b((double) posX + (double) width, (double) posY + (double) height, 0.0D)
				.func_181673_a((double) ((float) 1), (double) ((float) 1 + (float) 0)).func_181669_b(64, 64, 64, 255)
				.func_181675_d();
		worldrenderer.func_181662_b((double) posX + (double) width, (double) posY, 0.0D)
				.func_181673_a((double) ((float) 1), (double) 0).func_181669_b(64, 64, 64, 255).func_181675_d();
		worldrenderer.func_181662_b((double) posX, (double) posY, 0.0D).func_181673_a(0.0D, (double) 0)
				.func_181669_b(64, 64, 64, 255).func_181675_d();
		tessellator.draw();
	}

	/**
	 * Draws a textured rectangle using the texture currently bound to the
	 * TextureManager
	 */
	public static void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV) {
		float f2 = 0.00390625F;
		float f3 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.func_181668_a(4, worldrenderer.getVertexFormat());
		worldrenderer
				.func_181662_b((double) (xCoord + 0.0F), (double) (yCoord + (float) maxV),
						(double) mc.getRenderItem().zLevel)
				.func_181673_a((double) ((float) (minU + 0) * f2), (double) ((float) (minV + maxV) * f3));
		worldrenderer
				.func_181662_b((double) (xCoord + (float) maxU), (double) (yCoord + (float) maxV),
						(double) mc.getRenderItem().zLevel)
				.func_181673_a((double) ((float) (minU + maxU) * f2), (double) ((float) (minV + maxV) * f3));
		worldrenderer
				.func_181662_b((double) (xCoord + (float) maxU), (double) (yCoord + 0.0F),
						(double) mc.getRenderItem().zLevel)
				.func_181673_a((double) ((float) (minU + maxU) * f2), (double) ((float) (minV + 0) * f3));
		worldrenderer
				.func_181662_b((double) (xCoord + 0.0F), (double) (yCoord + 0.0F), (double) mc.getRenderItem().zLevel)
				.func_181673_a((double) ((float) (minU + 0) * f2), (double) ((float) (minV + 0) * f3));
		tessellator.draw();
	}
	/**
	 * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width,
	 * height
	 */
	public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		drawTexturedModalRect(x, y, textureX, textureY, width, height, mc.getRenderItem().zLevel);
	}

	public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, float zLevel) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.func_181668_a(4, worldrenderer.getVertexFormat());
		worldrenderer.func_181662_b((double) (x + 0), (double) (y + height), (double) zLevel)
				.func_181673_a((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1));
		worldrenderer.func_181662_b((double) (x + width), (double) (y + height), (double) zLevel)
				.func_181673_a((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1));
		worldrenderer.func_181662_b((double) (x + width), (double) (y + 0), (double) zLevel)
				.func_181673_a((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1));
		worldrenderer.func_181662_b((double) (x + 0), (double) (y + 0), (double) zLevel)
				.func_181673_a((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1));
		tessellator.draw();
	}

	public static void emitParticleAtEntity(Entity entityHit, EnumParticleTypes particule) {
    	Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(entityHit, particule);
    }

	public static GuiButton getButtonById(List<GuiButton> buttonList, int id) {
		for (Iterator iterator = buttonList.iterator(); iterator.hasNext();) {
			GuiButton guiButton = (GuiButton) iterator.next();
			if (guiButton.id == id)
				return guiButton;
		}
		return null;
	}

	public static String getEnch(ItemStack is) {
		String ec = "";
		if (is != null) {
			NBTTagList tl = is.getEnchantmentTagList();
			if (tl != null)
				for (int i = 0; i < tl.tagCount(); i++) {
					NBTTagCompound b = (NBTTagCompound) tl.getCompoundTagAt(i);
					if (b != null)
						ec += I18n.format((Enchantment.getEnchantmentById(b.getInteger("id")).getName())).substring(0,
								4) + "-" + b.getInteger("lvl") + " ";
				}
		}
		if (ec.length() > 50)
			return ec.substring(0, 50) + "...";
		return ec;
	}

	public static EnumParticleTypes getParticleByName(String name) {
		for (EnumParticleTypes type: EnumParticleTypes.class.getEnumConstants())
			if(type.name().equalsIgnoreCase(name))return type;
		return null;
	}

	public static String getUnformattedText(String txt) {
		return txt.replaceAll(Chat.c_Modifier + "[a-fk-or0-9]", "");
	}

	public static boolean isHover(Element button, int mouseX, int mouseY) {
		return isHover(button.posX, button.posY, button.width, button.height, mouseX, mouseY);
	}

	public static boolean isHover(GuiButton button, int mouseX, int mouseY) {
		if (button.visible)
			return isHover(button.xPosition, button.yPosition, button.width, button.height, mouseX, mouseY);
		else
			return false;
	}

	public static boolean isHover(GuiTextField guiTextField, int mouseX, int mouseY) {
		if (guiTextField.getVisible())
			return isHover(guiTextField.xPosition, guiTextField.yPosition, guiTextField.width, guiTextField.height,
					mouseX, mouseY);
		else
			return false;
	}
	public static boolean isHover(int X, int Y, int sizeX, int sizeY, int mouseX, int mouseY) {
		if (mouseX >= X && mouseX <= X + sizeX && mouseY >= Y && mouseY <= Y + sizeY)
			return true;
		else
			return false;
	}
    public static void OpenGuiScreen(GuiScreen guiScreen, int delayTicks) {
		new DelayScreen(guiScreen, delayTicks);
	}
	public static void renderBox(double x, double y, double z, double x2, double y2, double z2, float r, float g,
			float b, float a) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GL11.glColor4f(r, g, b, a);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(0.5F);
		GL11.glDisable(3553);
		GL11.glDepthMask(false);
		GL11.glEnable(2848);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glEnable(2848);
		worldrenderer.setTranslation(x, y, z);
		worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181708_h);

		worldrenderer.func_181662_b(x, y2, z).func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
		worldrenderer.func_181662_b(x2, y2, z).func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
		worldrenderer.func_181662_b(x2, y, z).func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
		worldrenderer.func_181662_b(x, y, z).func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();

		worldrenderer.func_181662_b(x, y, z2).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
		worldrenderer.func_181662_b(x2, y, z2).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
		worldrenderer.func_181662_b(x2, y2, z2).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
		worldrenderer.func_181662_b(x, y2, z2).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();

		worldrenderer.func_181662_b(x, y, z).func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(x2, y, z).func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(x2, y, z2).func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(x, y, z2).func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();

		worldrenderer.func_181662_b(x, y2, z2).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(x2, y2, z2).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(x2, y2, z).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(x, y2, z).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();

		worldrenderer.func_181662_b(x, y, z2).func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(x, y2, z2).func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(x, y2, z).func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(x, y, z).func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();

		worldrenderer.func_181662_b(x2, y, z).func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(x2, y2, z).func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(x2, y2, z2).func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(x2, y, z2).func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
		tessellator.draw();
		worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
		GL11.glDepthMask(true);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
	}
	public static void renderLabel(String str, double x, double y, double z, Minecraft mc) {
		RenderManager renderManager = new RenderManager(mc.renderEngine, mc.getRenderItem());
		double d0 = (x - mc.thePlayer.posX) * (x - mc.thePlayer.posX)
				+ (y - mc.thePlayer.posY) * (y - mc.thePlayer.posY) + (z - mc.thePlayer.posZ) * (z - mc.thePlayer.posZ);
		FontRenderer fontrenderer = mc.fontRendererObj;
		float f = 1.6F;
		float f1 = 0.016666668F * f;
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.0F, (float) y, (float) z);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(-f1, -f1, f1);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		int j = fontrenderer.getStringWidth(str) / 2;
		GlStateManager.disableTexture2D();
		worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
		worldrenderer.func_181662_b((double) (-j - 1), (double) (-1), 0.0D).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F)
				.func_181675_d();
		worldrenderer.func_181662_b((double) (-j - 1), (double) (8), 0.0D).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F)
				.func_181675_d();
		worldrenderer.func_181662_b((double) (j + 1), (double) (8), 0.0D).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F)
				.func_181675_d();
		worldrenderer.func_181662_b((double) (j + 1), (double) (-1), 0.0D).func_181666_a(0.0F, 0.0F, 0.0F, 0.25F)
				.func_181675_d();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, 553648127);
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, -1);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

	/**
	 * Renders a white box with the bounds of the AABB translated by the offset.
	 * Args: aabb, x, y, z
	 */
	public static void renderOffsetAABB(AxisAlignedBB boundingBox, double x, double y, double z, float r, float g,
			float b, float a) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GL11.glColor4f(r, g, b, a);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(0.5F);
		GL11.glDisable(3553);
		GL11.glDepthMask(false);
		GL11.glEnable(2848);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glEnable(2848);
		worldrenderer.setTranslation(x, y, z);
		worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181708_h);

		worldrenderer.func_181662_b(boundingBox.minX, boundingBox.maxY, boundingBox.minZ)
				.func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ)
				.func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.maxX, boundingBox.minY, boundingBox.minZ)
				.func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.minX, boundingBox.minY, boundingBox.minZ)
				.func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();

		worldrenderer.func_181662_b(boundingBox.minX, boundingBox.minY, boundingBox.maxZ)
				.func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ)
				.func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ)
				.func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ)
				.func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();

		worldrenderer.func_181662_b(boundingBox.minX, boundingBox.minY, boundingBox.minZ)
				.func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.maxX, boundingBox.minY, boundingBox.minZ)
				.func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ)
				.func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.minX, boundingBox.minY, boundingBox.maxZ)
				.func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();

		worldrenderer.func_181662_b(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ)
				.func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ)
				.func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ)
				.func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.minX, boundingBox.maxY, boundingBox.minZ)
				.func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();

		worldrenderer.func_181662_b(boundingBox.minX, boundingBox.minY, boundingBox.maxZ)
				.func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ)
				.func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.minX, boundingBox.maxY, boundingBox.minZ)
				.func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.minX, boundingBox.minY, boundingBox.minZ)
				.func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();

		worldrenderer.func_181662_b(boundingBox.maxX, boundingBox.minY, boundingBox.minZ)
				.func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ)
				.func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ)
				.func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
		worldrenderer.func_181662_b(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ)
				.func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
		tessellator.draw();
		worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
		GL11.glDepthMask(true);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
	}
	public static void setWindowName(String name) {
		Display.setTitle(name.replaceAll("[&]version", ModMain.MOD_VERSION)
				.replaceAll("[&]username", Minecraft.getMinecraft().getSession().getUsername()));
	}
	
}
