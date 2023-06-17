package fr.atesab.atehud.utils;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.UserType;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.exceptions.UserMigratedException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.request.RefreshRequest;
import com.mojang.authlib.yggdrasil.response.RefreshResponse;
import com.mojang.authlib.yggdrasil.response.Response;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Session;

public class ConnectionUtils {
	public static String[] username_primary = { "HyDrA", "Arko", "Casual", "Dark", "xEnOn", "ProX", "Hyper", "ExOtiIk",
			"LamBDa", "SoLiD", "The", "Sugar", "King", "GeNeRaL", "B1G", "AggR0", "HItA", "Cloky", "FlO", "Dzt" },
			username_secondary = { "L0vEr", "Gamer", "Player", "HeAvEn", "J1m", "Dr4g0n", "TeAMGnr", "PeGaSus",
					"Sp0nge", "WoRlD", "SyS", "L3g3NdE", "LoGyk", "Sky", "HD", "15885", "95786", "42587", "36842",
					"28694" };

	public static String changeName(String newName) {
		setSession(new Session(newName, "", "", "mojang"));
		return Chat.GREEN + I18n.format("account.connect");
	}

	public static String getRandomUsername() {
		Random rnd = new Random();
		return username_primary[(int) (rnd.nextFloat() * (float) username_primary.length)]
				+ username_secondary[(int) (rnd.nextFloat() * (float) username_secondary.length)];
	}

	public static String login(String email, String password) {
		YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(
				Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);

		authentication.setUsername(email);
		authentication.setPassword(password);
		try {
			authentication.logIn();
			setSession(new Session(authentication.getSelectedProfile().getName(),
					authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(),
					"mojang"));
			return Chat.GREEN + I18n.format("account.connect");
		} catch (AuthenticationUnavailableException e) {
			return Chat.RED + I18n.format("account.change.error.serveroff");
		} catch (AuthenticationException e) {
			e.printStackTrace();
			if (e.getMessage().contains("Invalid username or password.")
					|| e.getMessage().toLowerCase().contains("account migrated"))
				return Chat.RED + I18n.format("account.change.error.password");
			else
				return Chat.RED + I18n.format("account.change.error.serveroff");
		} catch (NullPointerException e) {
			return Chat.RED + I18n.format("account.change.error.password");
		}
	}

	public static void openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void openWebpage(URL url) {
		try {
			openWebpage(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static void setSession(Session session) {
		System.out.println("Changing session to [name="+session.getUsername()+", id="+session.getPlayerID()+", token="+session.getToken()+"]");
		Field[] fs = Minecraft.getMinecraft().getClass().getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			if (fs[i].getType().getName().equals(Session.class.getName())) {
				try {
					fs[i].setAccessible(true);
					fs[i].set(Minecraft.getMinecraft(), session);
				} catch (Exception e) {
				}
			}
		}
		ModMain.syncPasswordList(); // AutoLoginMod
	}

	public static String stealSession(String token) {
		if (token.length() != 65 || !token.substring(32, 33).equals(":") || token.split(":").length != 2) {
			return Chat.RED + I18n.format("account.change.error.nottoken");
		}
		String uuid = token.split(":")[1];
		if (uuid.contains("-")) {
			return Chat.RED + I18n.format("account.change.error.nottoken");
		}
		JsonElement rawJson;
		try {
			rawJson = new JsonParser()
					.parse(new InputStreamReader(new URL("https://api.mojang.com/user/profiles/" + uuid + "/names")
							.openConnection().getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
			return Chat.RED + I18n.format("account.change.error.error");
		}
		System.out.println(rawJson);
		if (!rawJson.isJsonArray()) {
			return Chat.RED + I18n.format("account.change.error.uuid");
		}
		JsonArray json = rawJson.getAsJsonArray();
		String name = json.get(json.size() - 1).getAsJsonObject().get("name").getAsString();
		try {
			Proxy proxy = MinecraftServer.getServer() == null ? null : MinecraftServer.getServer().getServerProxy();
			if (proxy == null)
				proxy = Proxy.NO_PROXY;
			HttpURLConnection connection = (HttpURLConnection) new URL("https://authserver.mojang.com/validate")
					.openConnection(proxy);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			String content = "{\"accessToken\":\"" + token.split(":")[0] + "\"}";
			connection.setRequestProperty("Content-Length", "" + content.getBytes().length);
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			DataOutputStream output = new DataOutputStream(connection.getOutputStream());
			output.writeBytes(content);
			output.flush();
			output.close();

			if (connection.getResponseCode() != 204)
				throw new IOException();
		} catch (IOException e) {
			return Chat.RED + I18n.format("account.change.error.session");
		}
		// use session
		setSession(new Session(name, uuid, token.split(":")[0], "mojang"));
		return Chat.GREEN + I18n.format("account.change");
	}
}
