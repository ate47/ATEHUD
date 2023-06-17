package fr.atesab.atehud.gui;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.mojang.authlib.GameProfile;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.Chat;
import fr.atesab.atehud.event.FriendElementEvent;
import fr.atesab.atehud.event.TeleportEvent;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class GuiTeleporter extends GuiScreen {
	public static NetworkPlayerInfo currentPlayer;

	public static ArrayList<NetworkPlayerInfo> toTpPlayers;

	public static ArrayList<String> info = new ArrayList<String>();
	public static boolean hasSendPacket = false;

	public static int currentPlayerShow = 0, currentTP = 0;

	public static long timeonsend;

	public static void teleport(GameProfile gf) {
		Minecraft mc = Minecraft.getMinecraft();
		try {
			int finalGameMode = 3;
			boolean creative = mc.thePlayer.capabilities.isCreativeMode;
			boolean spectator = mc.thePlayer.isSpectator();
			if (creative)
				finalGameMode = 1;
			else if (!creative && !spectator)
				finalGameMode = 0;
			if (!spectator)
				mc.thePlayer.sendChatMessage("/gamemode 3");
			TeleportEvent event = new TeleportEvent(gf, finalGameMode);
			if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
				return;
			mc.thePlayer.sendQueue.addToSendQueue(new C18PacketSpectate(gf.getId()));
			finalGameMode = event.finalGameMode;
			gf = event.gameProfile;
			Chat.show(I18n.format("teleporter.message", gf.getName()));
			if (finalGameMode != 3)
				mc.thePlayer.sendChatMessage("/gamemode " + finalGameMode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void teleport(String username) {
		Minecraft mc = Minecraft.getMinecraft();
		NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
		ArrayList<NetworkPlayerInfo> list = (ArrayList) ATEEventHandler.field_175252_a
				.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
		ArrayList<String> names = new ArrayList<String>();
		for (NetworkPlayerInfo a : list) {
			String name = (a.getGameProfile().getName());
			if (name.contains(username)) {
				NetworkPlayerInfo currentPlayer = a;
				teleport(currentPlayer.getGameProfile());
				return;
			}
		}
		Chat.error(I18n.format("teleporter.message.error", "\"" + currentPlayer.getGameProfile().getName() + "\""));
	}

	public GuiScreen Last;

	public GuiTextField uuid;
	public GuiButton tp, massTp;
	public GuiTeleporter(GuiScreen last) {
		Last = last;
	}
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(Last);
		if (button.id == 1) {
			try {
				boolean creative = mc.thePlayer.capabilities.isCreativeMode;
				if (creative)
					mc.thePlayer.sendChatMessage("/gamemode 3");
				mc.thePlayer.sendQueue.addToSendQueue(new C18PacketSpectate(currentPlayer.getGameProfile().getId()));
				if (creative)
					mc.thePlayer.sendChatMessage("/gamemode 1");
			} catch (Exception e) {
			}
		}
		if (button.id == 2) {
			NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
			ArrayList<NetworkPlayerInfo> list = (ArrayList<NetworkPlayerInfo>) ATEEventHandler.field_175252_a
					.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
			boolean creative = mc.thePlayer.capabilities.isCreativeMode;
			if (creative)
				mc.thePlayer.sendChatMessage("/gamemode 3");
			mc.thePlayer.sendQueue.addToSendQueue(new C18PacketSpectate(
					list.get((int) (list.size() * (new Random().nextFloat()))).getGameProfile().getId()));

			if (creative)
				mc.thePlayer.sendChatMessage("/gamemode 1");
		}
		if (button.id == 3) {
			if (mc.thePlayer.capabilities.isCreativeMode)
				mc.thePlayer.sendChatMessage("/gamemode 3");
			NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
			toTpPlayers = (ArrayList<NetworkPlayerInfo>) ATEEventHandler.field_175252_a
					.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
			button.enabled = false;
		}
		super.actionPerformed(button);
	}
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		uuid.drawTextBox();
		GuiUtils.drawRightString(fontRendererObj, I18n.format("teleporter.player") + " : ", uuid.xPosition,
				uuid.yPosition, 20, Colors.GOLD);
		if (currentPlayer != null && currentPlayer.getDisplayName() != null) {
			drawCenteredString(fontRendererObj, currentPlayer.getDisplayName().getFormattedText(), width / 2,
					uuid.yPosition - 2 - fontRendererObj.FONT_HEIGHT, Colors.WHITE);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	public void initGui() {
		uuid = new GuiTextField(0, fontRendererObj, width / 2 - 99, height / 2, 198, 18);
		buttonList.add(new GuiButton(0, width / 2 - 200, height / 2 + 21, 199, 20, I18n.format("gui.done")));
		buttonList.add(
				new GuiButton(2, width / 2 + 1, height / 2 + 21, 199, 20, I18n.format("teleporter.teleport.random")));
		buttonList.add(
				tp = new GuiButton(1, width / 2 + 1, height / 2 + 42, 199, 20, I18n.format("teleporter.teleport")));
		buttonList.add(massTp = new GuiButton(3, width / 2 - 200, height / 2 + 42, 199, 20,
				I18n.format("teleporter.teleport.all")));
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		uuid.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		uuid.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void updateScreen() {
		if (info == null)
			info = new ArrayList<String>();
		uuid.updateCursorCounter();
		if (toTpPlayers == null) {
			NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
			ArrayList<NetworkPlayerInfo> list = (ArrayList) ATEEventHandler.field_175252_a
					.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
			ArrayList<String> names = new ArrayList<String>();
			boolean b = true;
			for (NetworkPlayerInfo a : list) {
				String name = (a.getGameProfile().getName());
				if (name.contains(uuid.getText())) {
					currentPlayer = a;
					b = false;
				}
			}
			if (b)
				currentPlayer = null;
			tp.enabled = (currentPlayer != null);
		}
		super.updateScreen();
	}
}
