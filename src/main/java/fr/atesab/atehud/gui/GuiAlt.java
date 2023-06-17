package fr.atesab.atehud.gui;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.UserType;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.realmsclient.gui.ChatFormatting;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.superclass.Account;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.utils.ConnectionUtils;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Session;

public class GuiAlt extends GuiScreen {
	public GuiScreen Last;
	public String lastMessage;
	public GuiTextField username, password;
	public GuiButton random, mode;
	public int modeV = 0; // 0:Online 1:Offline 2:SS

	public GuiAlt(GuiScreen last) {
		Last = last;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0)
			mc.displayGuiScreen(Last);
		else if (button.id == 1) {
			if (!username.getText().isEmpty()) {
				if (modeV == 1) {
					lastMessage = ConnectionUtils.changeName(username.getText());
				} else if (modeV == 0) {
					lastMessage = ConnectionUtils.login(username.getText(), password.getText());
				} else if (modeV == 2) {
					lastMessage = ConnectionUtils.stealSession(username.getText());
				}
			} else
				lastMessage = Chat.RED + I18n.format("account.username.empty");
		} else if (button.id == 2) {
			if (modeV == 1) {
				ConnectionUtils.changeName(ConnectionUtils.getRandomUsername());
				lastMessage = Chat.GREEN + I18n.format("account.change");
			} else {
				ConnectionUtils.openWebpage(new URL("https://mcleaks.net/"));
			}
		} else if (button.id == 3) {
			if (modeV++ > 1)
				modeV = 0;
		} else if (button.id == 4) {
			mc.displayGuiScreen(new GuiAccountsList(this));
		} else if (button.id == 5) {
			ModMain.accountsList.add(new Account(username.getText(), modeV, password.getText()));
			ModMain.saveAccounts();
		}
		super.actionPerformed(button);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GuiAdvancedMenu.drawBackgroundAvanced(zLevel, this);
		if (modeV == 0 || modeV == 1)
			GuiUtils.drawRightString(fontRendererObj, I18n.format("account.username") + " : ", username.xPosition,
					username.yPosition, username.height, Colors.GOLD);
		if (modeV == 2 || modeV == 3)
			GuiUtils.drawRightString(fontRendererObj, I18n.format("account.token") + " : ", username.xPosition,
					username.yPosition, username.height, Colors.GOLD);
		if (modeV == 0)
			GuiUtils.drawRightString(fontRendererObj, I18n.format("account.password") + " : ", password.xPosition,
					password.yPosition, password.height, Colors.GOLD);
		GuiUtils.drawCenterString(fontRendererObj, lastMessage, width / 2,
				height / 2 - 43 - fontRendererObj.FONT_HEIGHT, Colors.WHITE);
		GuiUtils.drawCenterString(fontRendererObj,
				I18n.format("account.account",
						ChatFormatting.YELLOW + Minecraft.getMinecraft().getSession().getUsername()),
				width / 2, height / 2 - 44 - fontRendererObj.FONT_HEIGHT * 2, Colors.RED);
		username.drawTextBox();
		if (modeV == 0)
			password.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (GuiUtils.isHover(random, mouseX, mouseY)) {
			String s = I18n.format("account.random");
			if (modeV == 3)
				s = I18n.format("account.mode.3");
			GuiUtils.drawTextBox(this, mc, fontRendererObj, new String[] { s }, mouseX + 5, mouseY + 5, Colors.GOLD);
		} else if (GuiUtils.isHover(mode, mouseX, mouseY))
			GuiUtils.drawTextBox(this, mc, fontRendererObj,
					new String[] {
							I18n.format("account.mode") + " : " + Chat.RED + I18n.format("account.mode." + modeV) },
					mouseX + 5, mouseY + 5, Colors.GOLD);
	}

	public void initGui() {
		username = new GuiTextField(0, fontRendererObj, width / 2 - 99, height / 2 - 41, 198, 18);
		password = new GuiTextField(1, ModMain.passwordFontRenderer, width / 2 - 99, height / 2 - 20, 198, 18);
		username.setMaxStringLength(Integer.MAX_VALUE);
		password.setMaxStringLength(Integer.MAX_VALUE);
		lastMessage = "";
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 2, 99, 20, I18n.format("gui.done")));
		buttonList.add(new GuiButton(1, width / 2 + 1, height / 2, 99, 20, I18n.format("account.change")));
		buttonList.add(random = new GuiButton(2, username.xPosition + username.width + 3, username.yPosition - 1, 60,
				20, I18n.format("account.random")));
		buttonList.add(mode = new GuiButton(3, password.xPosition + password.width + 3, password.yPosition - 1, 60, 20,
				I18n.format("account.mode")));

		buttonList.add(new GuiButton(4, width / 2 - 100, height / 2 + 21, 99, 20, I18n.format("account.list")));
		buttonList.add(new GuiButton(5, width / 2 + 1, height / 2 + 21, 99, 20, I18n.format("account.list.save")));
		super.initGui();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		username.textboxKeyTyped(typedChar, keyCode);
		if (modeV == 0)
			password.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		username.mouseClicked(mouseX, mouseY, mouseButton);
		if (GuiUtils.isHover(username, mouseX, mouseY) && mouseButton == 1) {
			username.setText("");
		}
		if (modeV == 0) {
			password.mouseClicked(mouseX, mouseY, mouseButton);
			if (GuiUtils.isHover(password, mouseX, mouseY) && mouseButton == 1) {
				password.setText("");
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void updateScreen() {
		random.enabled = modeV == 1 || modeV == 3;
		random.visible = modeV == 1 || modeV == 3;
		if (modeV == 1)
			random.displayString = I18n.format("account.random");
		else
			random.displayString = I18n.format("account.mode.3");
		username.updateCursorCounter();
		if (modeV == 0)
			password.updateCursorCounter();
		super.updateScreen();
	}
}
