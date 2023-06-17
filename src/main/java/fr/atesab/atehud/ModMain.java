package fr.atesab.atehud;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.atesab.atehud.bridge.*;
import fr.atesab.atehud.command.CommandATEHUD;
import fr.atesab.atehud.gui.GuiAdvancedMenu;
import fr.atesab.atehud.gui.GuiHudOptions;
import fr.atesab.atehud.gui.GuiInvView;
import fr.atesab.atehud.gui.GuiMenu;
import fr.atesab.atehud.gui.GuiRndGame;
import fr.atesab.atehud.gui.GuiTicTacToe;
import fr.atesab.atehud.gui.config.GuiATEHUDConfig;
import fr.atesab.atehud.gui.element.Button;
import fr.atesab.atehud.gui.element.Slider;
import fr.atesab.atehud.overlay.Overlay;
import fr.atesab.atehud.overlay.Overlay.ModuleConfig;
import fr.atesab.atehud.superclass.Account;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.superclass.TextOption;
import fr.atesab.atehud.ts3.TS3Socket;
import fr.atesab.atehud.utils.GuiUtils;
import fr.atesab.atehud.utils.ItemHelp;
import fr.atesab.atehud.utils.PvpUtils;
import io.netty.channel.ChannelPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = ModMain.MOD_ID, version = ModMain.MOD_VERSION, name = ModMain.MOD_NAME, clientSideOnly = true)
@SideOnly(Side.CLIENT)
public class ModMain {
	public static interface ISaveConfig {
		public void save();
	}
	public static final String MOD_ID = "atehud";
	public static final String MOD_LITTLE_NAME = "ATEHUD";
	public static final String MOD_VERSION = "1.6";
	public static final String MOD_NAME = "ATEHUD";
	public static final String BRIDGE_NAME = "atian_bridge";
	/*
	 * Config
	 */
	public static String[] HeadNames = {"ATE47"};
	public static String[] AdvancedItem = {
			"wool 42 2 {display:{Name:\"Pink verity\",Lore:[\"42 is life\",\"wait what ?\"]}}" };
	public static String[] CustomFirework = {
			"{Fireworks:{Flight:1,Explosions:[{Type:1,Colors:[7995154,16252672,15615],FadeColors:[16777215]}]}}" };
	public static String[] CustomCommandBlock = { "/tellraw @a \"<SuperCommandBlock> Hello world !\"" };
	public static String[] AntiMessage = {};
	public static String[] HeyMessage = {};
	public static String[] MenuElements = {};
	public static String[] friend = {};
	public static String[] functions = {};
	public static String defaultPassword = "";
	public static String infoSong = "note.pling";
	public static String CustomName = "";
	public static String gamemode = "";
	public static String spamCommand = "";
	public static String ts3ClientQueryAdress = "127.0.0.1";
	public static String ts3ClientApiKey = "";
	public static String discordBotId = "392024433713217546";
	public static String discordSteamId = "";
	public static String iconSmall = "horse";
	public static String iconLarge = "default";
	public static String particleType = EnumParticleTypes.CRIT.name();
	public static boolean isConnect = false;
	public static boolean isinConnect = false;// Need enter password
	public static boolean HUDEnabled = true; // Show HUD
	public static boolean EntityDebug = true; // Show entity in debug
	public static boolean InfoDebug = false; // Show Hud in debug
	public static boolean backScreen = true; // BackScreenMod enabled
	public static boolean genFake1 = true; // Generate tab 1
	public static boolean genFake2 = true; // ... 2
	public static boolean genFake3 = true; // ... 3
	public static boolean Fullbright = false; // FullBright enabled
	public static boolean dateChat = false; // Show hour/minute/sceconde in chat
	public static boolean usernameSong = true;// Play a song when username is in chat
	public static boolean lockMessage = true; // Hide locked message
	public static boolean songMessage = true; // Play a song when songMessage is in chat
	public static boolean friendJoinMessage = true;
	public static boolean allJoinMessage = false;
	public static boolean displayChunk = false;// Display chunks
	public static boolean AdvancedModActived = true; // More data in log and other option
	public static boolean byPassAC = false; // ByPass AHAC
	public static boolean UCOsys=false;		//devmod option 1
	public static boolean UCOsysb=false;	//devmod option 2
	public static boolean UCOsysp=false;	//devmod option 3
	public static boolean UCO=true;			//devmod
	public static boolean invAdvanced=false;	//IVA
	public static boolean TTDura = true; // ToolTip : Display Durability
	public static boolean TTGiver = true; // ToolTip : Display giver
	public static boolean TTRepair = true; // ToolTip : Display repairCost
	public static boolean TTTags = true; // ToolTip : Display tags
	public static boolean useAdvancedMenu = true;
	public static boolean ServerInfo = true;
	public static boolean genTS3 = true;
	public static boolean autoLogin = true; //On/Off AutoLogin
	public static boolean TS3Poke = true; // Show poke TS3
	public static boolean TS3Text = true; // Show message TS3
	public static boolean TS3LogQuery = false; // Show QueryMessage
	public static boolean DiscordLogEvent = false; // Show EventMessage
	public static boolean showButton = true; //Show spammer button
	public static boolean renderGameOverlay = true; //Show spammer gameoverlay
	public static boolean stopOnClose = false; //stop spammer iteration on gui close
	public static boolean randomTextColor = false;
	public static boolean randomGuiColor = false;
	public static boolean onlyCritical = false;
	public static int hudArmorLocation = GuiHudOptions.TOPLEFT;// HUD location
	public static int hudEffectLocation = GuiHudOptions.TOPRIGHT;// HUD location
	public static int hudPlayerLocation = GuiHudOptions.NONE;
	public static int hudTS3Location = GuiHudOptions.MIDDLERIGHT;
	public static int moreParticles = 0;
	public static int localPort = 25565; // Minecraft LocalServer Port
	public static int guiColorRed = 255;
	public static int guiColorGreen = 255;// Color of gui
	public static int guiColorBlue = 255;
	public static int textColorRed = 255;
	public static int textColorGreen = 255;// Color of text
	public static int textColorBlue = 255;
	public static int chunkViewRange = 1; // Chunk view range ...
	public static int ts3ClientQueryPort = 25639; // TS3Config
	public static int ts3ClientListRefreshInMilli = 1000; // Time to wait between refresh
	public static int ts3ClientListRefreshTimedOutInMilli = 10000; // Time to wait between force re-refresh
	public static int ts3NameMaxLength = 20;
	public static int ticktowait = 100;
	public static int numberOfAction = 10;
	public static int maxJoinLeftMessage = 5;
	public static float zoom_value = 8.0F;

	public static long fakePositionLastUpdate=0;
	public static double fakePositionX=0;
	public static double fakePositionY=0;
	public static double fakePositionZ=0;
	public static Item fakeItem;
	public static Item fakeItem2;
	public static Item fakeItem3;
	public static CommandATEHUD theCommand;
	public static CreativeTabs ATEcreativeTAB = new ATECreativeTab(CreativeTabs.getNextID(), "ateTab", Items.blaze_rod);
	public static CreativeTabs ATEcreativeTAB2 = new ATECreativeTab(CreativeTabs.getNextID(), "ateTab2", Items.skull, true);
	public static CreativeTabs ATEcreativeTAB3 = new ATECreativeTab(CreativeTabs.getNextID(), "ateTab3", Items.cake);
	public static Configuration configFile;
	public static Configuration password_config;
	public static Configuration headList;
	public static KeyBinding guifactory;
	public static KeyBinding nbtitem;
	public static KeyBinding zoom, nbttile;
	public static ServerData lastServerData = null;
	public static long lastDeconnectionTime = 0;
	public static long timeToWait = 5000L;
	public static boolean autoReco = false;
	public static HashMap<String, String> passwordList;
	public static ArrayList<Account> accountsList = new ArrayList<Account>();
	public static ArrayList<KeyBind> keyBindingList = new ArrayList<KeyBind>();
	public static ArrayList<Overlay> overlayList = new ArrayList<Overlay>();
	public static List<TextOption> textOptions = new ArrayList<TextOption>();
	private static AtianChannelBridge atianChannelBridge = null;
	public static AtianChannelBridge getAtianHudBridge() {
		return atianChannelBridge;
	}
	public static void createNewAtianHudBridge(ChannelPipeline pipeline) {
		atianChannelBridge = new AtianChannelBridge(pipeline);
	}
	public static FontRenderer passwordFontRenderer;
	@Mod.Instance(MOD_ID)
	public static ModMain instance;

	public static ArrayList<ISaveConfig> configs = new ArrayList<ISaveConfig>();

	/**
	 * Add a specific config (Yes It's useless but I like it ^^)
	 * 
	 * @param type
	 *            type=["AdvancedItem","HeadNames","CustomFirework","CustomCommandBlock"]
	 * @param value
	 */

	public static void addConfig(String type, String value) {
		if (type == "AdvancedItem")
			AdvancedItem = addToList(AdvancedItem, value);
		if (type == "HeadNames")
			HeadNames = addToList(HeadNames, value);
		if (type == "CustomFirework")
			CustomFirework = addToList(CustomFirework, value);
		if (type == "CustomCommandBlock")
			CustomCommandBlock = addToList(CustomCommandBlock, value);
		configFile.get(Configuration.CATEGORY_GENERAL, "Give command (Item/Block) (number) (meta) (nbt)", AdvancedItem)
				.setValues(AdvancedItem);
		configFile.get(Configuration.CATEGORY_GENERAL, "Custom Head", HeadNames).setValues(HeadNames);
		configFile.get(Configuration.CATEGORY_GENERAL, "Custom Firework (NBT)", CustomFirework)
				.setValues(CustomFirework);
		configFile.get(Configuration.CATEGORY_GENERAL, "Custom commandBlock (command)", CustomCommandBlock)
				.setValues(CustomCommandBlock);
		configFile.save();
	}

	public static String[] addToList(String[] list, String... toAdd) {
		String[] a = new String[list.length + toAdd.length];
		int i = 0;
		System.arraycopy(list, 0, a, 0, list.length);
		System.arraycopy(toAdd, 0, a, list.length, toAdd.length);
		return a;
	}

	public static void backScreen() {
		boolean value = false;
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.gameSettings.fovSetting < 180)
			value = true;
		backScreen(value);
	}

	public static void backScreen(boolean value) {
		backScreen = value;
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.gameSettings.fovSetting < 180 && value) {
			mc.gameSettings.fovSetting = 180 - (mc.gameSettings.fovSetting - 180);
		} else if (mc.gameSettings.fovSetting > 180 && !value) {
			mc.gameSettings.fovSetting = 180 - (mc.gameSettings.fovSetting - 180);
		}
	}

	public static void ChangePassword(String server, String newPassword) {
		passwordList.put(server, newPassword);
		String[] strs = new String[passwordList.size()];
		int i = 0;
		for (String name : passwordList.keySet()) {
			String key = name.toString();
			String value = passwordList.get(name).toString();
			String valueEncode = Base64.getEncoder().encodeToString(String.format(key + "," + value).getBytes());
			strs[i] = valueEncode;
			i++;
		}
		password_config.get(Minecraft.getMinecraft().getSession().getUsername(), "passwords", strs).set(strs);
		password_config.save();
	}

	public static <T> ArrayList<T> getArray(T[] list) {
		ArrayList<T> array = new ArrayList<T>();
		for (T o: list) array.add(o);
		return array;
	}

	public static Color getGuiColor() {
		return randomGuiColor ? getRandomColorByTime(0.5F) : new Color(guiColorRed, guiColorGreen, guiColorBlue);
	}

	public static List<KeyBind> getKeyMod(fr.atesab.atehud.Mod mod) {
		List<KeyBind> a = new ArrayList<KeyBind>();
		for (KeyBind kb: keyBindingList) {
			String cmd = kb.command;
			if(cmd.startsWith("/"))cmd=cmd.substring(1);
			String[] args = cmd.split(" ");
			if(args.length!=3)continue;
			for (String alias: theCommand.getCommandAliases())
				if(alias.equalsIgnoreCase(args[0]))
					for (String alias2: theCommand.mod.getAlias())
						if(alias2.equalsIgnoreCase(args[1]))
							if(mod.getId().equalsIgnoreCase(args[2]))
								a.add(kb);
		}
		return a;
	}

	public static String getLowString(String text, int maxSize) {
		if (text.length() > maxSize)
			return text.substring(0, maxSize) + "...";
		else
			return text;
	}
	public static final float TETA = (float)(2*Math.PI*500F);
	/**
	 * clamp an integer between a max and a minimum value
	 * @param i the integer to clamp
	 * @param min minimum value
	 * @param max maximum value
	 * @return the clamped value
	 */
	public static int clamp_int(int i, int min, int max) {
		return i<min?min:i>max?max:i;
	}
	/**
	 * Get a random color with the current system time
	 * @param shift the percentage of TETA to shift
	 * @return the color
	 */
	public static Color getRandomColorByTime(float shift) {
		float f = (float)(System.currentTimeMillis()%((long)TETA))/500F+shift*TETA/500F;
		return new Color(
				clamp_int((int)(Math.sin(f)*200F), 0, 200)+55, 
				clamp_int((int)(Math.sin(f+(float)(2F/3F*Math.PI))*200F), 0, 200)+55, 
				clamp_int((int)(Math.sin(f+(float)(4F/3F*Math.PI))*200F), 0, 200)+55);
	}

	public static Color getTextColor() {
		return randomTextColor ? getRandomColorByTime(0F) : new Color(textColorRed, textColorGreen, textColorBlue);
	}
	public static boolean intToBoolean(int value) {
		if (value == 0)
			return false;
		else
			return true;
	}

	public static boolean isScreenBacked() {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.gameSettings.fovSetting < 180)
			return false;
		return true;
	}

	public static fr.atesab.atehud.Mod[] removeMod(String id, fr.atesab.atehud.Mod[] lst) {
		if (lst != null) {
			int j = 0;
			for (int i = 0; i < lst.length; i++) {
				if (lst[i] != null && lst[i].getId().equals(id)) {
					lst[i] = null;
				} else {
					j++;
				}
			}
			fr.atesab.atehud.Mod[] lst2 = new fr.atesab.atehud.Mod[j];
			j = 0;
			for (int i = 0; i < lst.length; i++) {
				if (lst[i] != null) {
					lst2[j] = lst[i];
					j++;
				}
			}
			return lst2;
		}
		return lst;
	}

	public static void saveAccounts() {
		String[] accounts = new String[accountsList.size()];
		for (int i = 0; i < accounts.length; i++) {
			Account ac = accountsList.get(i);
			accounts[i] = Base64.getEncoder()
					.encodeToString(String.format(ac.username + "," + ac.type + "," + ac.password).getBytes());
		}
		password_config.get("AltAccountsData", "accounts", new String[] {}).set(accounts);
		password_config.save();
	}

	public static void saveConfig() {
		for (int i = 0; i < configs.size(); i++) {
			configs.get(i).save();
		}
		configFile.get(Configuration.CATEGORY_GENERAL, "Advanced Mode", AdvancedModActived).set(AdvancedModActived);
		configFile.get(Configuration.CATEGORY_GENERAL, "Custom Window Name", CustomName).set(CustomName);
		configFile.get(Configuration.CATEGORY_GENERAL, "Display Chunks", displayChunk).set(displayChunk);
		configFile.get(Configuration.CATEGORY_GENERAL, "backScreen", backScreen).set(backScreen);
		configFile.get(Configuration.CATEGORY_GENERAL, "fullBright", Fullbright).set(Fullbright);
		configFile.get(Configuration.CATEGORY_GENERAL, "usernameSong", usernameSong).set(usernameSong);
		configFile.get(Configuration.CATEGORY_GENERAL, "lockMessage", lockMessage).set(lockMessage);
		configFile.get(Configuration.CATEGORY_GENERAL, "songMessage", songMessage).set(songMessage);
		configFile.get(Configuration.CATEGORY_GENERAL, "infoSong", infoSong).set(infoSong);
		configFile.get(Configuration.CATEGORY_GENERAL, "date", dateChat).set(dateChat);
		configFile.get(Configuration.CATEGORY_GENERAL, "autoLogin", autoLogin).set(autoLogin);
		configFile.get("INVVIEW", "entity", GuiInvView.drawEntityWithInventory).set(GuiInvView.drawEntityWithInventory);
		configFile.get("INVVIEW", "coor", GuiInvView.drawCoorWithInventory).set(GuiInvView.drawCoorWithInventory);
		configFile.get("INVVIEW", "dis", GuiInvView.drawDistanceWithPlayer).set(GuiInvView.drawDistanceWithPlayer);
		configFile.get("MENU", "randomTextColor", randomTextColor).set(randomTextColor);
		configFile.get("MENU", "randomGuiColor", randomGuiColor).set(randomGuiColor);
		configFile.get("MENU", "guiColorRed", guiColorRed).set(guiColorRed);
		configFile.get("MENU", "guiColorGreen", guiColorGreen).set(guiColorGreen);
		configFile.get("MENU", "guiColorBlue", guiColorBlue).set(guiColorBlue);
		configFile.get("MENU", "textColorRed", textColorRed).set(textColorRed);
		configFile.get("MENU", "textColorGreen", textColorGreen).set(textColorGreen);
		configFile.get("MENU", "textColorBlue", textColorBlue).set(textColorBlue);
		configFile.get("MENU-DATA", "elems", fr.atesab.atehud.Mod.DefaultElems()).set(MenuElements);
		Gson gson = new GsonBuilder().create();
		String[] keys = new String[keyBindingList.size()];
		for (int i = 0; i < keys.length; i++)
			keys[i] = gson.toJson(keyBindingList.get(i));
		String[] overlays = new String[overlayList.size()];
		for (int i = 0; i < overlays.length; i++)
			overlays[i] = gson.toJson(overlayList.get(i));
		configFile.get("MENU-DATA", "keys", keys).set(keys);
		configFile.get("MENU-DATA", "overlays", overlays).set(overlays);
		configFile.get("GAME", "TTT-ComputerUsing", GuiTicTacToe.UseComputer).set(GuiTicTacToe.UseComputer);
		configFile.get("GAME", "TTT-score_comp", GuiTicTacToe.score_comp).set(GuiTicTacToe.score_comp);
		configFile.get("GAME", "TTT-score_ties", GuiTicTacToe.score_ties).set(GuiTicTacToe.score_ties);
		configFile.get("GAME", "TTT-score_you", GuiTicTacToe.score_you).set(GuiTicTacToe.score_you);
		configFile.get("GAME", "RND-highScore", GuiRndGame.highScore).set(GuiRndGame.highScore);
		configFile.get("GAME", "RND-level", GuiRndGame.level).set(GuiRndGame.level);
		configFile.get("ToolTip", "Dura", TTDura).set(TTDura);
		configFile.get("ToolTip", "Giver", TTGiver).set(TTGiver);
		configFile.get("ToolTip", "Repair", TTRepair).set(TTRepair);
		configFile.get("ToolTip", "Tags", TTTags).set(TTTags);
		configFile.get("hudInfo", "hudArmorLocation", hudArmorLocation).set(hudArmorLocation);
		configFile.get("hudInfo", "hudEffectLocation", hudEffectLocation).set(hudEffectLocation);
		configFile.get("hudInfo", "hudPlayerLocation", hudPlayerLocation).set(hudPlayerLocation);
		configFile.get("hudInfo", "hudTS3Location", hudTS3Location).set(hudTS3Location);

		configFile.get("save", "moreParticles", moreParticles).set(moreParticles);
		configFile.get("save", "gamemode", gamemode).set(gamemode);
		configFile.get("save", "zoom_value", zoom_value).set(zoom_value);
		configFile.get("save", "particleType", particleType).set(particleType);
		configFile.get("save", "onlyCritical", onlyCritical).set(onlyCritical);
		configFile.get("ts3", "ts3ClientQueryPort", ts3ClientQueryPort).set(ts3ClientQueryPort);
		configFile.get("ts3", "ts3ClientQueryAdress", ts3ClientQueryAdress).set(ts3ClientQueryAdress);
		configFile.get("ts3", "TS3PokeNotif", TS3Poke).set(TS3Poke);
		configFile.get("ts3", "TS3TextNotif", TS3Text).set(TS3Text);
		configFile.get("ts3", "ts3ClientListRefreshInMilli", ts3ClientListRefreshInMilli)
				.set(ts3ClientListRefreshInMilli);
		configFile.get("ts3", "ts3NameMaxLength", ts3NameMaxLength).set(ts3NameMaxLength);
		configFile.get("ts3", "genTS3", genTS3).set(genTS3);
		configFile.get("ts3", "ts3ClientApiKey", ts3ClientApiKey).set(ts3ClientApiKey);

		configFile.get("discord", "discordBotId", discordBotId).set(discordBotId);
		configFile.get("discord", "discordSteamId", discordSteamId).set(discordSteamId);
		
		configFile.get("discord", "iconLarge", iconLarge).set(iconLarge);
		configFile.get("discord", "iconSmall", iconSmall).set(iconSmall);
		
		configFile.get(Configuration.CATEGORY_GENERAL, "serverInfo", ServerInfo).set(ServerInfo);
		configFile.get("HUD_INFO", "entityDebug", EntityDebug).set(EntityDebug);
		configFile.get("HUD_INFO", "InfoDebug", InfoDebug).set(InfoDebug);
		configFile.get("HUD_INFO", "enabled", HUDEnabled).set(HUDEnabled);
		configFile.get("ChatOptions", "charPage", ATEEventHandler.chatCharPage).set(ATEEventHandler.chatCharPage);
		
		configFile.get("ChatOptions", "spamCommand", spamCommand).set(spamCommand);
		configFile.get("ChatOptions", "ticktowait", ticktowait).set(ticktowait);
		configFile.get("ChatOptions", "numberOfAction", numberOfAction).set(numberOfAction);
		configFile.get("ChatOptions", "stopOnClose", stopOnClose).set(stopOnClose);
		configFile.get("ChatOptions", "showButton", showButton).set(showButton);
		configFile.get("ChatOptions", "renderGameOverlay", renderGameOverlay).set(renderGameOverlay);

		configFile.get("ChatOptions", "friendJoinMessage", friendJoinMessage).set(friendJoinMessage);
		configFile.get("ChatOptions", "allJoinMessage", allJoinMessage).set(allJoinMessage);

		configFile.get(Configuration.CATEGORY_GENERAL, "friend", friend).set(friend);
		configFile.get(Configuration.CATEGORY_GENERAL, "useAdvancedMenu", useAdvancedMenu).set(useAdvancedMenu);
		configFile.save();
	}

	public static void setDefaultPassword(String newPassword) {
		defaultPassword = newPassword;
		String valueEncode = Base64.getEncoder().encodeToString(String.format(newPassword).getBytes());
		password_config.get(Minecraft.getMinecraft().getSession().getUsername(), "defaultpassword", defaultPassword)
				.set(valueEncode);
		password_config.save();
	}

	public static String significantNumbers(double d, int n){
	    String s = String.format("%."+n+"G", d);
	    if (s.contains("E+")){
	        s = String.format(Locale.US, "%.0f", Double.valueOf(String.format("%."+n+"G", d)));
	    }
	    return s;
	}

	public static void syncConfig() {
		ts3ClientListRefreshInMilli = configFile.getInt("ts3ClientListRefreshInMilli", "ts3",
				ts3ClientListRefreshInMilli, 0, 20000, "");
		ts3ClientQueryPort = configFile.getInt("ts3ClientQueryPort", "ts3", ts3ClientQueryPort, 0, 65535, "");
		ts3NameMaxLength = configFile.getInt("ts3NameMaxLength", "ts3", ts3NameMaxLength, 0, Integer.MAX_VALUE, "");
		ts3ClientQueryAdress = configFile.getString("ts3ClientQueryAdress", "ts3", ts3ClientQueryAdress, "");
		TS3Poke = configFile.getBoolean("TS3PokeNotif", "ts3", TS3Poke, "");
		TS3Text = configFile.getBoolean("TS3TextNotif", "ts3", TS3Text, "");
		genTS3 = configFile.getBoolean("genTS3", "ts3", genTS3, "");
		ts3ClientApiKey = configFile.getString("ts3ClientApiKey", "ts3", ts3ClientApiKey, "");
		discordBotId = configFile.getString("discordBotId", "discord", discordBotId, "Change that only if you know what you're doing !");
		discordSteamId = configFile.getString("discordSteamId", "discord", discordSteamId, "Change that only if you know what you're doing !");
		iconSmall = configFile.getString("iconSmall", "discord", iconSmall, "");
		iconLarge = configFile.getString("iconLarge", "discord", iconLarge, "");
		
		useAdvancedMenu = configFile.getBoolean("useAdvancedMenu", Configuration.CATEGORY_GENERAL, useAdvancedMenu, "");
		friendJoinMessage = configFile.getBoolean("friendJoinMessage", "ChatOptions", friendJoinMessage, "");
		allJoinMessage = configFile.getBoolean("allJoinMessage", "ChatOptions", allJoinMessage, "");

		spamCommand=configFile.getString("spamCommand", "ChatOptions", spamCommand, "");
		ticktowait=configFile.getInt("ticktowait", "ChatOptions", ticktowait, 0, Integer.MAX_VALUE, "", spamCommand);
		numberOfAction=configFile.getInt("numberOfAction", "ChatOptions", numberOfAction, 0, Integer.MAX_VALUE, "");
		stopOnClose=configFile.getBoolean("stopOnClose", "ChatOptions", stopOnClose, "stop the iteration when you close the menu");
		showButton=configFile.getBoolean("showButton", "ChatOptions", showButton, "Set to false to hide the Spammer button");
		renderGameOverlay=configFile.getBoolean("renderGameOverlay", "ChatOptions", renderGameOverlay, "Render iteration count out of the menu");
		
		ATEEventHandler.chatCharPage = configFile.getInt("charPage", "ChatOptions", ATEEventHandler.chatCharPage, 0,
				Integer.MAX_VALUE, "");

		hudArmorLocation = configFile.getInt("hudArmorLocation", "hudInfo", hudArmorLocation, 0,
				GuiHudOptions.BOTTOMRIGHT + 1, "");
		hudEffectLocation = configFile.getInt("hudEffectLocation", "hudInfo", hudEffectLocation, 0, GuiHudOptions.NONE,
				"");
		hudPlayerLocation = configFile.getInt("hudPlayerLocation", "hudInfo", hudPlayerLocation, 0, GuiHudOptions.NONE,
				"");
		hudTS3Location = configFile.getInt("hudTS3Location", "hudInfo", hudTS3Location, 0, GuiHudOptions.MIDDLERIGHT,
				"");
		localPort = configFile.getInt("localPort", "save", localPort, 0, 65535, "");
		ServerInfo = configFile.getBoolean("ServerInfo", Configuration.CATEGORY_GENERAL, ServerInfo, "");

		HUDEnabled = configFile.getBoolean("enabled", "HUD_INFO", HUDEnabled, "");
		EntityDebug = configFile.getBoolean("entityDebug", "HUD_INFO", EntityDebug, "");
		InfoDebug = configFile.getBoolean("InfoDebug", "HUD_INFO", InfoDebug, "");

		CustomName = configFile.getString("Custom Window Name", Configuration.CATEGORY_GENERAL, CustomName, "");

		if (!CustomName.equals(""))
			GuiUtils.setWindowName(CustomName);

		ItemHelp.maxXp = configFile.getInt("Max enchant level", Configuration.CATEGORY_GENERAL, ItemHelp.maxXp, 0,
				32700, "");
		displayChunk = configFile.getBoolean("Display Chunks", Configuration.CATEGORY_GENERAL, displayChunk, "");
		backScreen = configFile.getBoolean("backScreen", Configuration.CATEGORY_GENERAL, backScreen, "");
		Fullbright = configFile.getBoolean("fullBright", Configuration.CATEGORY_GENERAL, Fullbright, "");
		infoSong = configFile.getString("infoSong", Configuration.CATEGORY_GENERAL, infoSong, "");
		dateChat = configFile.getBoolean("date", Configuration.CATEGORY_GENERAL, dateChat, "");
		autoLogin = configFile.getBoolean("autoLogin", Configuration.CATEGORY_GENERAL, autoLogin, "");
		
		usernameSong = configFile.getBoolean("usernameSong", Configuration.CATEGORY_GENERAL, usernameSong,
				"Send a song when your username is said");
		lockMessage = configFile.getBoolean("lockMessage", Configuration.CATEGORY_GENERAL, lockMessage,
				"Hide some message");
		songMessage = configFile.getBoolean("songMessage", Configuration.CATEGORY_GENERAL, songMessage,
				"Send a song when message is said");
		AntiMessage = configFile.getStringList("Lock Message", Configuration.CATEGORY_GENERAL, AntiMessage, "");
		HeyMessage = configFile.getStringList("Song Message", Configuration.CATEGORY_GENERAL, HeyMessage, "");
		friend = configFile.getStringList("friend", Configuration.CATEGORY_GENERAL, friend, "");

		TTGiver = configFile.getBoolean("Giver", "ToolTip", TTGiver, "");
		TTDura = configFile.getBoolean("Dura", "ToolTip", TTDura, "");
		TTRepair = configFile.getBoolean("Repair", "ToolTip", TTRepair, "");
		TTTags = configFile.getBoolean("Tags", "ToolTip", TTTags, "");

		genFake1 = configFile.getBoolean("Generate Fake Items", Configuration.CATEGORY_GENERAL, genFake1, "");
		genFake2 = configFile.getBoolean("Generate Head", Configuration.CATEGORY_GENERAL, genFake2, "");
		genFake3 = configFile.getBoolean("Generate Customs Items", Configuration.CATEGORY_GENERAL, genFake3, "");

		AdvancedModActived = configFile.getBoolean("Advanced Mode", Configuration.CATEGORY_GENERAL, false,
				"Open access to advanced options");
		AdvancedItem = configFile.getStringList("Give command (Item/Block) (number) (meta) (nbt)",
				Configuration.CATEGORY_GENERAL, (String[]) AdvancedItem,
				LanguageRegistry.instance().getStringLocalization("act.advlist"));
		HeadNames = configFile.getStringList("Custom Head", Configuration.CATEGORY_GENERAL, HeadNames,
				LanguageRegistry.instance().getStringLocalization("act.headlist"));
		CustomFirework = configFile.getStringList("Custom Firework (NBT)", Configuration.CATEGORY_GENERAL,
				CustomFirework, LanguageRegistry.instance().getStringLocalization("act.cstfirework"));
		CustomCommandBlock = configFile.getStringList("Custom commandBlock (command)", Configuration.CATEGORY_GENERAL,
				CustomCommandBlock, LanguageRegistry.instance().getStringLocalization("act.cstcommand"));

		MenuElements = configFile.getStringList("elems", "MENU-DATA", fr.atesab.atehud.Mod.DefaultElems(), "");
		String[] keys = configFile.getStringList("keys", "MENU-DATA", new String[] {}, "");
		String[] overlays = configFile.getStringList("overlays", "MENU-DATA", new String[] {}, "");
		keyBindingList.clear();
		Gson gson = new GsonBuilder().create();
		for (String s: keys)
			keyBindingList.add(gson.fromJson(s, KeyBind.class));
		overlayList.clear();
		for (String s: overlays)
			overlayList.add(gson.fromJson(s, Overlay.class));
		randomTextColor = configFile.getBoolean("randomTextColor", "MENU", randomTextColor, "");
		randomGuiColor = configFile.getBoolean("randomGuiColor", "MENU", randomGuiColor, "");
		guiColorRed = configFile.getInt("guiColorRed", "MENU", guiColorRed, 0, 255, "");
		guiColorGreen = configFile.getInt("guiColorGreen", "MENU", guiColorGreen, 0, 255, "");
		guiColorBlue = configFile.getInt("guiColorBlue", "MENU", guiColorBlue, 0, 255, "");
		textColorRed = configFile.getInt("textColorRed", "MENU", textColorRed, 0, 255, "");
		textColorGreen = configFile.getInt("textColorGreen", "MENU", textColorGreen, 0, 255, "");
		textColorBlue = configFile.getInt("textColorBlue", "MENU", textColorBlue, 0, 255, "");
		GuiTicTacToe.UseComputer = configFile.getBoolean("TTT-ComputerUsing", "GAME", GuiTicTacToe.UseComputer, "");
		GuiTicTacToe.score_comp = configFile.getInt("TTT-score_comp", "GAME", GuiTicTacToe.score_comp, 0,
				Integer.MAX_VALUE, "Don't touch this ...");
		GuiTicTacToe.score_ties = configFile.getInt("TTT-score_ties", "GAME", GuiTicTacToe.score_ties, 0,
				Integer.MAX_VALUE, "Don't touch this ...");
		GuiTicTacToe.score_you = configFile.getInt("TTT-score_you", "GAME", GuiTicTacToe.score_you, 0,
				Integer.MAX_VALUE, "Don't touch this ...");
		GuiRndGame.highScore = configFile.getInt("RND-highScore", "GAME", GuiRndGame.highScore, 0, Integer.MAX_VALUE,
				"Don't touch this ...");
		GuiRndGame.level = configFile.getInt("RND-level", "GAME", GuiRndGame.level, 0, Integer.MAX_VALUE, "");
		GuiInvView.drawEntityWithInventory = configFile.getBoolean("entity", "INVVIEW",
				GuiInvView.drawEntityWithInventory, "");
		GuiInvView.drawCoorWithInventory = configFile.getBoolean("coor", "INVVIEW", GuiInvView.drawCoorWithInventory,
				"");
		GuiInvView.drawDistanceWithPlayer = configFile.getBoolean("dis", "INVVIEW", GuiInvView.drawDistanceWithPlayer,
				"");
		gamemode = configFile.getString("gamemode", "save", gamemode, "");
		moreParticles = configFile.getInt("moreParticles", "save", moreParticles, 0, 500, "");
		onlyCritical = configFile.getBoolean("onlyCritical", "save", onlyCritical, "");
		particleType = configFile.getString("particleType", "save", particleType, "");
		zoom_value = configFile.getFloat("zoom_value", "save", zoom_value, 0.0F, 10.0F, "");
		configFile.save();
	}

	public static void syncHeadList() {
		FakeItems2.vip = headList.getStringList("VIP", "Heads", FakeItems2.vip, "");
		FakeItems2.mhf = headList.getStringList("MHF", "Heads", FakeItems2.mhf, "");
		FakeItems2.list = headList.getStringList("LIST", "Heads", FakeItems2.list, "");

		headList.save();
	}

	public static void syncPasswordList() {
		String category = Minecraft.getMinecraft().getSession().getUsername();
		passwordList.clear();
		accountsList.clear();
		String passEncode = Base64.getEncoder().encodeToString(String.format(defaultPassword).getBytes());
		password_config.setCategoryComment(category, "Category for the username : " + category);
		passEncode = password_config.getString("defaultpassword", category, passEncode, "");
		defaultPassword = new String(Base64.getDecoder().decode(passEncode));
		String[] pass = password_config.getStringList("passwords", category, new String[] {}, "");
		String[] account = password_config.getStringList("accounts", "AltAccountsData", new String[] {}, "");
		for (int i = 0; i < pass.length; i++) {
			String[] valueDecode = new String(Base64.getDecoder().decode(pass[i])).split(",");
			if (valueDecode.length > 1) {
				if (valueDecode.length > 2) {
					for (int j = 2; j < valueDecode.length; j++) {
						valueDecode[1] += "," + valueDecode[j];
					}
				}
				passwordList.put(valueDecode[0], valueDecode[1]);
			}
		}
		for (int i = 0; i < account.length; i++) {
			String[] valueDecode = new String(Base64.getDecoder().decode(account[i])).split(",");
			if (valueDecode.length > 2) {
				if (valueDecode.length > 3) {
					for (int j = 3; j < valueDecode.length; j++) {
						valueDecode[2] += "," + valueDecode[j];
					}
				}
				try {
					accountsList.add(new Account(valueDecode[0], Integer.valueOf(valueDecode[1]), valueDecode[2]));
				} catch (Exception e) {
				}
			}
		}
		password_config.save();
	}

	public static void updateFakeLocation() {
		if(System.currentTimeMillis() > fakePositionLastUpdate) {
			fakePositionX = Math.random()*10000;
			fakePositionY = 1+Math.random()*254;
			fakePositionZ = Math.random()*10000;
			fakePositionLastUpdate = System.currentTimeMillis() + 1000;
		}
	}

	private IReloadableResourceManager mcResourceManager;
	private final IMetadataSerializer metadataSerializer_ = new IMetadataSerializer();
	public static SimpleNetworkWrapper network;
	public IMetadataSerializer getMetadataSerializer_() {
		return metadataSerializer_;
	}
	@EventHandler
	public void init(FMLInitializationEvent event) throws ExecutionException {
		GuiAdvancedMenu.defineBackgroundId(false);
		textOptions.add(new TextOption() {
			public String getName() {return "i";}
			public String getReplacement(Minecraft mc) {return String.valueOf(ATEEventHandler.action);}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "cps";}
			public String getReplacement(Minecraft mc) {return String.valueOf(PvpUtils.getLeftCpsCounter());}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "rcps";}
			public String getReplacement(Minecraft mc) {return String.valueOf(PvpUtils.getRightCpsCounter());}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "reach";}
			public String getReplacement(Minecraft mc) {return String.valueOf(PvpUtils.getReach());}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "version";}
			public String getReplacement(Minecraft mc) {return ModMain.MOD_VERSION;}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "pname";}
			public String getReplacement(Minecraft mc) {return mc.thePlayer.getGameProfile().getName();}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "px";}
			public String getReplacement(Minecraft mc) {return String.valueOf((int)mc.thePlayer.posX);}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "plx";}
			public String getReplacement(Minecraft mc) {return String.valueOf(mc.thePlayer.posX);}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "prx";}
			public String getReplacement(Minecraft mc) {
				updateFakeLocation();
				return String.valueOf((int)(ModMain.fakePositionX));
			}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "plrx";}
			public String getReplacement(Minecraft mc) {
				updateFakeLocation();
				return String.valueOf(ModMain.fakePositionX);
			}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "py";}
			public String getReplacement(Minecraft mc) {return String.valueOf((int)mc.thePlayer.posY);}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "ply";}
			public String getReplacement(Minecraft mc) {return String.valueOf(mc.thePlayer.posY);}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "pry";}
			public String getReplacement(Minecraft mc) {
				updateFakeLocation();
				return String.valueOf((int)(ModMain.fakePositionY));
			}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "plry";}
			public String getReplacement(Minecraft mc) {
				updateFakeLocation();
				return String.valueOf(ModMain.fakePositionY);
			}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "pz";}
			public String getReplacement(Minecraft mc) {return String.valueOf((int)mc.thePlayer.posZ);}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "plz";}
			public String getReplacement(Minecraft mc) {return String.valueOf(mc.thePlayer.posZ);}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "prz";}
			public String getReplacement(Minecraft mc) {
				updateFakeLocation();
				return String.valueOf((int)(ModMain.fakePositionZ));
			}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "plrz";}
			public String getReplacement(Minecraft mc) {
				updateFakeLocation();
				return String.valueOf(ModMain.fakePositionZ);
			}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "hp";}
			public String getReplacement(Minecraft mc) {return String.valueOf(mc.thePlayer.getHealth());}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "mhp";}
			public String getReplacement(Minecraft mc) {return String.valueOf(mc.thePlayer.getMaxHealth());}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "tname";}
			public String getReplacement(Minecraft mc) {if(mc.thePlayer.getTeam()!=null)
				return String.valueOf(mc.thePlayer.getTeam().getRegisteredName());else return "?";}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "sip";} 
			public String getReplacement(Minecraft mc) {
				if(!mc.isIntegratedServerRunning() && mc.getCurrentServerData()!=null)
					return mc.getCurrentServerData().serverIP;
				return "127.0.0.1";
			}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "sname";} 
			public String getReplacement(Minecraft mc) {
				if(!mc.isIntegratedServerRunning() && mc.getCurrentServerData()!=null)
					return mc.getCurrentServerData().serverName;
				return "Minecraft";
			}
		});
		textOptions.add(new TextOption() {
			public String getName() {return "ping";} 
			public String getReplacement(Minecraft mc) {
				if(!mc.isIntegratedServerRunning() && mc.getCurrentServerData()!=null) {
					long l = mc.getCurrentServerData().pingToServer;
					return (l < 0 ? Chat.WHITE : (l < 150 ? Chat.DARK_GREEN : (l < 300 ? Chat.GREEN : (l < 600 ? Chat.GOLD : (l < 1000 ? Chat.RED : Chat.DARK_RED)))))
							+ mc.getCurrentServerData().pingToServer+Chat.RESET;
				}
				return Chat.DARK_GREEN+"0"+Chat.RESET;
			}
		});
		textOptions.sort(new Comparator<TextOption>() {
			public int compare(TextOption o1, TextOption o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		Button.render.add(new Button.SkinRenderer() {
			public void render(Minecraft mc, Button btn, int mouseX, int mouseY) {
				FontRenderer fontrenderer = mc.fontRendererObj;
				int l = Button.getHoverColor(btn);
				GuiUtils.drawBlackBackGround(btn.posX, btn.posY, btn.width, btn.height);
				GuiUtils.drawBox(btn.posX, btn.posY, btn.posX + btn.width, btn.posY + btn.height, new Color(l));
				GuiUtils.drawCenterString(fontrenderer, btn.text, btn.posX + btn.width / 2, btn.posY, btn.height, l);
				if (btn.hovered && btn.desc != null) {
					GuiUtils.drawTextBox(mc.currentScreen, mc, fontrenderer, btn.desc, mouseX + 5, mouseY + 5,
							Colors.BLUE);
				}
			}
		});
		Button.render.add(new Button.SkinRenderer() {
			public void render(Minecraft mc, Button btn, int mouseX, int mouseY) {
				FontRenderer fontrenderer = mc.fontRendererObj;
				int l = Button.getHoverColor(btn);
				mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/widgets.png"));
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				btn.hovered = mouseX >= btn.posX && mouseY >= btn.posY && mouseX < btn.posX + btn.width
						&& mouseY < btn.posY + btn.height;
				int k = 1;
				if (!btn.enabled)
					k = 0;
				else if (btn.hovered)
					k = 2;
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.blendFunc(770, 771);
				GuiUtils.drawTexturedModalRect(btn.posX, btn.posY, 0, 46 + k * 20, btn.width / 2, btn.height);
				GuiUtils.drawTexturedModalRect(btn.posX + btn.width / 2, btn.posY, 200 - btn.width / 2, 46 + k * 20,
						btn.width / 2, btn.height);
				GuiUtils.drawCenterString(fontrenderer, btn.text, btn.posX + btn.width / 2,
						btn.posY + btn.height / 2 - fontrenderer.FONT_HEIGHT / 2, fontrenderer.FONT_HEIGHT, l);
				if (btn.hovered && btn.desc != null) {
					GuiUtils.drawTextBox(mc.currentScreen, mc, fontrenderer, btn.desc, mouseX + 5, mouseY + 5,
							Colors.BLUE);
				}
			}
		});
		Slider.render.add(new Slider.SkinRenderer() {
			private void drawBox(int posX, int posY, int width, int height, Slider sld) {
				GuiUtils.drawBox(posX, posY, posX+width, posY+height, sld.color);
			}
			public void render(Minecraft mc, Slider sld, int mouseX, int mouseY) {
				GuiUtils.drawBlackBackGround(sld.posX, sld.posY, sld.width, sld.height);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				drawBox(sld.posX, sld.posY, sld.width, sld.height, sld);
				drawBox((int)(sld.posX+(sld.value*((float)(sld.width - 8)))), sld.posY, 8, sld.height, sld);
				GuiUtils.drawCenterString(mc.fontRendererObj, sld.getText(), sld.posX+sld.width/2, sld.posY, sld.height, sld.color);
			}
		});
		Slider.render.add(new Slider.SkinRenderer() {
			@Override
			public void render(Minecraft mc, Slider sld, int mouseX, int mouseY) {
				mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/widgets.png"));
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.blendFunc(770, 771);
				GuiUtils.drawTexturedModalRect(sld.posX, sld.posY, 0, 46, sld.width / 2, sld.height,90F);
				GuiUtils.drawTexturedModalRect(sld.posX + sld.width / 2, sld.posY, 200 - sld.width / 2, 46, sld.width / 2, sld.height);
	            GuiUtils.drawTexturedModalRect(sld.posX + (int)(sld.value * (float)(sld.width - 8)), sld.posY, 0, 66, 4, sld.height);
	            GuiUtils.drawTexturedModalRect(sld.posX + (int)(sld.value * (float)(sld.width - 8)) + 4, sld.posY, 196, 66, 4, sld.height);
				GuiUtils.drawCenterString(mc.fontRendererObj, sld.getText(), sld.posX+sld.width/2, sld.posY, sld.height, sld.color);
			}
		});
		for (int i = 0; i < fr.atesab.atehud.Mod.getMods().size(); i++)
			fr.atesab.atehud.Mod.getMods().get(i).Init();
	}
	@EventHandler
	public void modDisabled(FMLModDisabledEvent event) {
		System.out.println("You disabled me, why ? :c");
	}
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		GameSettings.Options.FOV.setValueMax(120.0F);
		GameSettings.Options.CHAT_WIDTH.setValueMax(2.0F);
		GameSettings.Options.CHAT_HEIGHT_FOCUSED.setValueMax(2.0F);
		GameSettings.Options.CHAT_HEIGHT_UNFOCUSED.setValueMax(2.0F);
		GameSettings.Options.GAMMA.setValueMax(5.0F);
		passwordFontRenderer = new FontRenderer(mc.gameSettings, new ResourceLocation("textures/font/password.png"),
				mc.renderEngine, false);
		this.mcResourceManager = new SimpleReloadableResourceManager(getMetadataSerializer_());
		this.mcResourceManager.registerReloadListener(passwordFontRenderer);
		for (int i = 0; i < fr.atesab.atehud.Mod.getMods().size(); i++)
			fr.atesab.atehud.Mod.getMods().get(i).PostInit();
	}
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		configFile = new Configuration(new File(new File(Minecraft.getMinecraft().mcDataDir, MOD_NAME), "config.cfg"));
		password_config = new Configuration(
				new File(new File(Minecraft.getMinecraft().mcDataDir, "ATEHUD"), "password.cfg"));
		headList = new Configuration(new File(new File(Minecraft.getMinecraft().mcDataDir, MOD_NAME), "headList.cfg"));
		passwordList = new HashMap<String, String>();
		FMLCommonHandler.instance().bus().register(instance);
		FMLCommonHandler.instance().bus().register(new ATEEventHandler());
		fr.atesab.atehud.Mod.loadMods();
		fr.atesab.atehud.overlay.Module.loadModules();
		fr.atesab.atehud.overlay.Overlay.loadOverlayDefaultConfigs();
		fr.atesab.atehud.event.command.EventCommand.loadEventCommand();
		fr.atesab.atehud.superclass.Action.defineAction();
		syncPasswordList();
		syncHeadList();
		syncConfig();
		TS3Socket.openSocket();
		network = NetworkRegistry.INSTANCE.newSimpleChannel(BRIDGE_NAME);
		network.registerMessage(BridgeMessageHandler.class, BridgeMessage.class, 116, Side.CLIENT);
		network.registerMessage(BridgeMessageHandler.class, BridgeMessage.class, 117, Side.SERVER);
		GameRegistry.registerItem(fakeItem = new FakeItems(), "fakeItem");
		GameRegistry.registerItem(fakeItem2 = new FakeItems2(), "fakeItem2");
		GameRegistry.registerItem(fakeItem3 = new FakeItems3(), "fakeItem3");
		try {
			ClientCommandHandler.instance.registerCommand(theCommand = new CommandATEHUD("help"));
		} catch (Throwable e) {
			System.err.println("Can't register ATEHUD command\n"+e.getMessage());
		}
		LanguageRegistry.instance().addStringLocalization("itemGroup.ateTab", "fr_FR", "\u00a7bObjet Impossible");
		LanguageRegistry.instance().addStringLocalization("itemGroup.ateTab2", "fr_FR", "\u00a7bTetes");
		LanguageRegistry.instance().addStringLocalization("itemGroup.ateTab3", "fr_FR", "\u00a7bObjet perso");
		LanguageRegistry.instance().addStringLocalization("itemGroup.ateTab", "fr_CA", "\u00a7bObjet Impossible");
		LanguageRegistry.instance().addStringLocalization("itemGroup.ateTab2", "fr_CA", "\u00a7bTetes");
		LanguageRegistry.instance().addStringLocalization("itemGroup.ateTab3", "fr_CA", "\u00a7bObjet perso");
		LanguageRegistry.instance().addStringLocalization("itemGroup.ateTab", "en_US", "\u00a7bUnreal Items");
		LanguageRegistry.instance().addStringLocalization("itemGroup.ateTab2", "en_US", "\u00a7bHeads");
		LanguageRegistry.instance().addStringLocalization("itemGroup.ateTab3", "en_US", "\u00a7bCustom Item");
		ClientRegistry.registerKeyBinding(
				guifactory = new KeyBinding("key.atehud.guifactory", Keyboard.KEY_N, "key.atehud.categories"));
		ClientRegistry.registerKeyBinding(
				nbtitem = new KeyBinding("key.atehud.nbtitem", Keyboard.KEY_Y, "key.atehud.categories"));
		ClientRegistry
				.registerKeyBinding(zoom = new KeyBinding("key.atehud.zoom", Keyboard.KEY_C, "key.atehud.categories"));
		ClientRegistry.registerKeyBinding(
				nbttile = new KeyBinding("key.atehud.nbttile", Keyboard.KEY_H, "key.atehud.categories"));

	}
}
