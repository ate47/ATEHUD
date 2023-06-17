package fr.atesab.atehud;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.event.HoverEvent;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.JsonSerializableSet;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.*;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import fr.atesab.atehud.Mod.*;
import fr.atesab.atehud.bridge.AtianChannelBridge;
import fr.atesab.atehud.event.*;
import fr.atesab.atehud.gui.*;
import fr.atesab.atehud.gui.config.GuiATEHUDConfig;
import fr.atesab.atehud.gui.config.GuiChatOption;
import fr.atesab.atehud.gui.config.GuiKeyBinding;
import fr.atesab.atehud.gui.config.GuiOverlays;
import fr.atesab.atehud.gui.config.GuiTS3Config;
import fr.atesab.atehud.gui.element.Button;
import fr.atesab.atehud.gui.element.Element;
import fr.atesab.atehud.gui.element.OverlayData;
import fr.atesab.atehud.gui.element.RightClickElement;
import fr.atesab.atehud.overlay.Module;
import fr.atesab.atehud.overlay.Overlay;
import fr.atesab.atehud.overlay.OverlayDefaultConfig;
import fr.atesab.atehud.overlay.OverlayDisplayManager;
import fr.atesab.atehud.overlay.Overlay.ColorType;
import fr.atesab.atehud.overlay.Overlay.position;
import fr.atesab.atehud.overlay.module.*;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.ts3.TS3Socket;
import fr.atesab.atehud.utils.CommandUtils;
import fr.atesab.atehud.utils.GuiUtils;
import fr.atesab.atehud.utils.ItemHelp;
import fr.atesab.atehud.utils.MultiChatComponentText;
import fr.atesab.atehud.utils.MultiChatComponentText.FriendElement;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import fr.atesab.atehud.utils.PvpUtils;
import fr.atesab.atehud.utils.RightClickGuiScreen;

public class ATEEventHandler {
	@SideOnly(Side.CLIENT)
	static class PlayerComparator implements Comparator<NetworkPlayerInfo> {
		public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_) {
			ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
			ScorePlayerTeam scoreplayerteam1 = p_compare_2_.getPlayerTeam();
			return ComparisonChain.start()
					.compareTrueFirst(p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR,
							p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR)
					.compare(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "",
							scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : "")
					.compare(p_compare_1_.getGameProfile().getName(), p_compare_2_.getGameProfile().getName()).result();
		}
	}
	public static Minecraft mc = Minecraft.getMinecraft();
	public static int lastMouse = -1;
	public static float currentFOV = -1F;
	public static boolean isZoom = false;
	public static boolean tryChangingGameMode = false;
	// Spammer
	public static int action=0;
	public static int tickw=0;
	public static boolean isEnable=false;
	public static boolean hasDoFakemsg=false;
	public static boolean hasCountClick=false;
	public static GuiButton ATEHUDOptionsButton;
	public static GuiButton ATEHUDReconnect;
	public static GuiButton ATEHUDAutoReconnect;
	public static GuiButton ATEHUDLocal;
	public static GuiButton ATEHUDChatTest;
	public static GuiButton ATEHUDChatOption;
	public static GuiButton ATEHUDChatSpammer;
	public static GuiButton ATEHUDChatnextPage;
	public static GuiButton ATEHUDChatlastPage;
	public static GuiButton ATEHUDChatFriend;
	public static GuiButton ATEHUDChatLocation;
	public static GuiButton[] ATEHUDcharButton = new GuiButton[8];
	public static GuiTextField chat;
	public static int chatCharPage = 0;
	private static List<Element> hoverRenderer = new ArrayList<Element>();
	private static RightClickElement rightClickElement;
	private static int rightClickElementPosX = 0;
	private static int rightClickElementPosY = 0;
	private static int preventDoubleClick = 0;
	public static final Ordering field_175252_a = Ordering.from(new PlayerComparator());
	public static List<String> friendConnected = new ArrayList<String>();
	public static List<String> playerConnected = new ArrayList<String>();
	public static List<OverlayData> overlays = new ArrayList<OverlayData>();
	public static void addHoverElement(Element elem) {
		hoverRenderer.add(elem);
	}

	public static void ClearHoverElement() {
		hoverRenderer.clear();;
	}

	public static void getCharButtons(GuiScreen gui) {
		char[] c = Chat.getAllSign();
		ATEHUDChatlastPage.enabled = chatCharPage > 0;
		ATEHUDChatnextPage.enabled = (chatCharPage+1)*8<c.length;
		for (int j = chatCharPage * 8; j < c.length && j < (chatCharPage+1) * 8; j++)
			ATEHUDcharButton[j%8].displayString=String.valueOf(c[j]);
	}


	public static  List<Element> getHoverElements() {
		return hoverRenderer;
	}

	public static final GuiScreen getParent(GuiScreen gs) {
		Field[] fs = gs.getClass().getFields();
		for (int i = 0; i < fs.length; i++) {
			if (fs[i].getType().equals(GuiScreen.class)) {
				try {
					return (GuiScreen) fs[i].get(gs);
				} catch (Exception e) {
				}
			}
		}
		return null;
	}
	public static void tpOnplayer() {
		if (GuiTeleporter.currentTP < 6) {
			mc.thePlayer.sendQueue.addToSendQueue(new C18PacketSpectate(
					GuiTeleporter.toTpPlayers.get(GuiTeleporter.currentPlayerShow).getGameProfile().getId()));
			GuiTeleporter.hasSendPacket = true;
			GuiTeleporter.timeonsend = mc.getSystemTime();
			GuiTeleporter.currentTP++;
		} else {
			GuiTeleporter.currentPlayerShow++;
		}
	}
	public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.func_181668_a(4, worldrenderer.getVertexFormat());
		worldrenderer.func_181662_b(x + 0, y + height, mc.getRenderItem().zLevel)
				.func_181673_a((float) (textureX + 0) * f, (float) (textureY + height) * f1);
		worldrenderer.func_181662_b(x + width, y + height, mc.getRenderItem().zLevel)
				.func_181673_a((float) (textureX + width) * f, (float) (textureY + height) * f1);
		worldrenderer.func_181662_b(x + width, y + 0, mc.getRenderItem().zLevel)
				.func_181673_a((float) (textureX + width) * f, (float) (textureY + 0) * f1);
		worldrenderer.func_181662_b(x + 0, y + 0, mc.getRenderItem().zLevel)
				.func_181673_a((float) (textureX + 0) * f, (float) (textureY + 0) * f1);
		tessellator.draw();
	}
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void loadEventCommand(LoadEventCommandEvent ev) {
		
	}
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void loadMod(LoadModEvent ev) {
		/* GIVER */
		ev.registerMod(new ShowGuiMod("fw", "giver", false, GuiFireWorkFactory.class));
		ev.registerMod(new ShowGuiMod("ie", "giver", false, GuiItemFactory.class));
		ev.registerMod(new ShowGuiMod("head", "giver", false, GuiSkullGiver.class));
		ev.registerMod(new ShowGuiMod("nbt", "giver", false, GuiNbtCode.class));
		ev.registerMod(new ShowGuiMod("inv", "giver", false, GuiInventory.class));
		/* GAMES */
		ev.registerMod(new ShowGuiMod("ttt", "games", false, GuiTicTacToe.class));
		ev.registerMod(new ShowGuiMod("rnd", "games", false, GuiRndGame.class));
		/* Others */
		ev.registerMod(new ShowGuiMod("invView", "others", true, GuiInvView.class));
		ev.registerMod(new ShowGuiMod("fmsg", "others", false, GuiFakeMessage.class, false));
		ev.registerMod(new ShowGuiMod("teleporter", "others", false, GuiTeleporter.class));
		ev.registerMod(new ShowGuiMod("ts3", "others", false, GuiTS3Config.class));
		ev.registerMod(new ShowGuiMod("particle", "others", false, GuiMoreParticleMod.class));
		/* Commands */
		ev.registerMod(new ShowGuiMod("login", "commands", false, GuiLoginOption.class));
		ev.registerMod(new ShowGuiMod("gm", "commands", false, GuiGameMode.class));
		ev.registerMod(new ShowGuiMod("keyb", "commands", false, GuiKeyBinding.class));
		ev.registerMod(new ShowGuiMod("spam", "commands", false, GuiSpammer.class));
		/* Config */
		ev.registerMod(new ShowGuiMod("global", "config", false, GuiATEHUDConfig.class));
		ev.registerMod(new ShowGuiMod("hud", "config", false, GuiOverlays.class));
		/*
		 * COMMAND MOD
		 */
		ev.registerMod(new CommandMod("gach", "commands", false,
				new String[] { "/achievement give achievement.fullBeacon",
						"/achievement give achievement.exploreAllBiomes", "/achievement give achievement.onARail",
						"/achievement give achievement.cookFish", "/achievement give achievement.overkill",
						"/achievement give achievement.overpowered", "/achievement give achievement.snipeSkeleton",
						"/achievement give achievement.ghast", "/achievement give achievement.bakeCake",
						"/achievement give achievement.makeBread", "/achievement give achievement.flyPig",
						"/achievement give achievement.bookcase", "/achievement give achievement.diamondsToYou",
						"/achievement give achievement.potion", "/achievement give achievement.breedCow" },
				"atehud.menu.allSuccess.give"));
		ev.registerMod(new CommandMod("tach", "commands", false,
				new String[] { "/achievement take achievement.openInventory" }, "atehud.menu.noSuccess.take"));
		/*
		 * TOGGLE MOD
		 */
		ev.registerMod(new SliderMod("zoom", "others", false, "fr.atesab.atehud.ModMain", "zoom_value", 0, 20));
		ev.registerMod(new ToggleMod("fb", "others", false, "fr.atesab.atehud.ModMain", "Fullbright"));
		ev.registerMod(new ToggleMod("dc", "others", false, "fr.atesab.atehud.ModMain", "displayChunk"));
		
		ev.registerMod(new SliderMod("mr", "config", false, "fr.atesab.atehud.ModMain", "guiColorRed", 0, 255));
		ev.registerMod(new SliderMod("mg", "config", false, "fr.atesab.atehud.ModMain", "guiColorGreen", 0, 255));
		ev.registerMod(new SliderMod("mb", "config", false, "fr.atesab.atehud.ModMain", "guiColorBlue", 0, 255));

		ev.registerMod(new SliderMod("mtr", "config", false, "fr.atesab.atehud.ModMain", "textColorRed", 0, 255));
		ev.registerMod(new SliderMod("mtg", "config", false, "fr.atesab.atehud.ModMain", "textColorGreen", 0, 255));
		ev.registerMod(new SliderMod("mtb", "config", false, "fr.atesab.atehud.ModMain", "textColorBlue", 0, 255));
		
		ev.registerMod(new ToggleMod("rtc", "config", false, "fr.atesab.atehud.ModMain", "randomTextColor"));
		ev.registerMod(new ToggleMod("rmc", "config", false, "fr.atesab.atehud.ModMain", "randomGuiColor"));

		ev.registerMod(new ShowGuiMod("varc", "dev", false, GuiVariableChanger.class, false));
		ev.registerMod(new ToggleMod("bpahac", "dev", false, "fr.atesab.atehud.ModMain", "byPassAC", false));
	}
	public static final Armor armorModule = new Armor();
	public static final Effects effectModule = new Effects();
	public static final Teamspeak tsModule = new Teamspeak();
	public static final Player playerModule = new Player();
	public static final Text textModule = new Text();
	public static final CustomItemDisplay customItemDisplayModule = new CustomItemDisplay();
	public static final KeyMap keyMapModule = new KeyMap();
	public static final AHDModList aHDModListModule = new AHDModList();
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void loadModule(LoadModuleEvent ev) {
		ev.registerModule(armorModule);
		ev.registerModule(effectModule);
		ev.registerModule(tsModule);
		ev.registerModule(textModule);
		ev.registerModule(playerModule);
		ev.registerModule(customItemDisplayModule);
		ev.registerModule(aHDModListModule);
		ev.registerModule(keyMapModule);
	}
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void loadDefaultOverlay(LoadDefaultOverlayEvent ev) {
		ev.registerDefaultConfig(new OverlayDefaultConfig("atehud.config.overlay.default.name.empty", ModMain.MOD_NAME, 
				"atehud.config.overlay.default.name.empty.desc", new Overlay(position.topLeft, I18n.format("atehud.config.overlay.title.default")
						, ColorType.inherit, ColorType.inherit), new ItemStack(Items.paper)));
		ev.registerDefaultConfig(new OverlayDefaultConfig("atehud.config.overlay.default.name.armorhud", ModMain.MOD_NAME, 
				"atehud.config.overlay.default.name.armorhud.desc", new Overlay(position.topLeft, I18n.format("atehud.config.overlay.default.name.armorhud")
						, ColorType.inherit, ColorType.inherit, 
						new Overlay.ModuleConfig(armorModule.getModuleId(), new Object() {
							public String[] getConfig(String[] a) {
								a[0] = "armor";
								return a;
							}
						}.getConfig(armorModule.getDefaultConfig())), 
						new Overlay.ModuleConfig(armorModule.getModuleId(), new Object() {
							public String[] getConfig(String[] a) {
								a[0] = "hand";
								return a;
							}
						}.getConfig(armorModule.getDefaultConfig())), 
						new Overlay.ModuleConfig(armorModule.getModuleId(), new Object() {
							public String[] getConfig(String[] a) {
								a[0] = "arrow";
								return a;
							}
						}.getConfig(armorModule.getDefaultConfig())), 
						new Overlay.ModuleConfig(customItemDisplayModule.getModuleId(), new Object() {
							public String[] getConfig(String[] a) {
								a[0] = ItemHelp.getNBTCode(ItemHelp.setCount(ItemHelp.setName(new ItemStack(Items.mushroom_stew), Chat.GOLD+"Soup"), 1));
								a[5]="false";
								return a;
							}
						}.getConfig(customItemDisplayModule.getDefaultConfig()))
								)
				, new ItemStack(Items.diamond_chestplate)));
		ev.registerDefaultConfig(new OverlayDefaultConfig("atehud.config.overlay.default.name.effecthud", ModMain.MOD_NAME, 
				"atehud.config.overlay.default.name.effecthud.desc", new Overlay(position.topLeft, I18n.format("atehud.config.overlay.default.name.effecthud")
						, ColorType.inherit, ColorType.inherit,	new Overlay.ModuleConfig(effectModule)), new ItemStack(Items.experience_bottle)));
		ev.registerDefaultConfig(new OverlayDefaultConfig("atehud.config.overlay.default.name.information", ModMain.MOD_NAME, 
				"atehud.config.overlay.default.name.information.desc", new Overlay(position.topLeft, I18n.format("atehud.config.overlay.default.name.information")
						, ColorType.inherit, ColorType.inherit,	
						new Overlay.ModuleConfig(textModule.getModuleId(), textModule.getConfigs(Chat.BOLD+"XYZ"+Chat.RESET+": &px, &py, &pz")),
						new Overlay.ModuleConfig(textModule.getModuleId(), textModule.getConfigs(Chat.BOLD+I18n.format("chatOption.to.pname")+Chat.RESET+": &pname")),
						new Overlay.ModuleConfig(textModule.getModuleId(), textModule.getConfigs(Chat.BOLD+"Cps"+Chat.RESET+": &cps/&rcps")),
						new Overlay.ModuleConfig(textModule.getModuleId(), textModule.getConfigs(Chat.BOLD+I18n.format("chatOption.to.reach")+Chat.RESET+": &reach")),
						new Overlay.ModuleConfig(textModule.getModuleId(), textModule.getConfigs(Chat.BOLD+ModMain.MOD_LITTLE_NAME+" v"+ModMain.MOD_VERSION
								+" "+Chat.YOLO_HEAD)),
						new Overlay.ModuleConfig(aHDModListModule)
						), new ItemStack(Items.book)));
		ev.registerDefaultConfig(new OverlayDefaultConfig("atehud.config.overlay.default.name.keymap", ModMain.MOD_NAME, 
				"atehud.config.overlay.default.name.keymap.desc", new Overlay(position.topLeft, I18n.format("atehud.config.overlay.default.name.keymap")
						, ColorType.inherit, ColorType.inherit,	new Overlay.ModuleConfig(keyMapModule)), new ItemStack(Items.map)));
		ev.registerDefaultConfig(new OverlayDefaultConfig("atehud.config.overlay.default.name.ts", ModMain.MOD_NAME, 
				"atehud.config.overlay.default.name.ts.desc", new Overlay(position.topLeft, I18n.format("atehud.config.overlay.default.name.ts")
						, ColorType.inherit, ColorType.inherit,	new Overlay.ModuleConfig(tsModule)), new ItemStack(Blocks.noteblock)));
		
	}
	@SubscribeEvent
	public void onActionPerform(ActionPerformedEvent ev) {
		if (ev.gui instanceof GuiChat && preventDoubleClick == 0) {
			if (ev.button.id == 1905 && chat != null) {
				chat.writeText(ev.button.displayString);
			} else if (ev.button == ATEHUDChatTest) {
				Chat.show(chat.getText().replaceAll("&", String.valueOf(Chat.c_Modifier)));
			} else if (ev.button == ATEHUDChatOption) {
				ev.gui.mc.displayGuiScreen(new GuiChatOption(ev.gui));
			} else if (ev.button == ATEHUDChatSpammer) {
				ev.gui.mc.displayGuiScreen(new GuiSpammer(ev.gui));
			} else if (ev.button == ATEHUDChatlastPage) {
				chatCharPage--;
				getCharButtons(ev.gui);
			} else if (ev.button == ATEHUDChatnextPage) {
				chatCharPage++;
				getCharButtons(ev.gui);
			} else if (ev.button == ATEHUDChatLocation) {
				chat.writeText("[x:" + (int) ev.gui.mc.thePlayer.posX + ", y:" + (int) ev.gui.mc.thePlayer.posY + ", z:"
						+ (int) ev.gui.mc.thePlayer.posZ + "]");
			} else if (ev.button == ATEHUDChatFriend) {
				CommandUtils.sendMessage("/"+ModMain.theCommand.getCommandName()+" "+ModMain.theCommand.friend.getName()+" "
						+ModMain.theCommand.friend.list.getName());
			}
			preventDoubleClick++;
		}
		if (ev.gui instanceof GuiMainMenu)
			if (ev.button.id == ATEHUDOptionsButton.id)
				ev.gui.mc.displayGuiScreen(new GuiATEHUDConfig(ev.gui));
		if (ev.gui instanceof GuiDisconnected && preventDoubleClick == 0) {
			if (ev.button.id == ATEHUDReconnect.id)
				net.minecraftforge.fml.client.FMLClientHandler.instance().connectToServer(getParent(ev.gui),
						ModMain.lastServerData);
			else if(ev.button.id == ATEHUDAutoReconnect.id) {
				ModMain.lastDeconnectionTime = System.currentTimeMillis();
				ModMain.autoReco=!ModMain.autoReco;
				System.out.println(ModMain.autoReco);
			}
			preventDoubleClick++;
		}
		if (ev.gui instanceof GuiMultiplayer)
			if (ev.button.id == ATEHUDLocal.id)
				ev.gui.mc.displayGuiScreen(new GuiLocalConnection(ev.gui));
	}
	@SubscribeEvent
	public void onAttackEntity(AttackEntityEvent ev) {
		if(ev.target instanceof EntityLivingBase) {
			PvpUtils.addReach(ev.target.getDistanceSqToEntity(ev.entityPlayer));
			if((ModMain.onlyCritical && mc.thePlayer.fallDistance > 0.0F && !mc.thePlayer.onGround && 
					!mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isPotionActive(Potion.blindness) &&
					mc.thePlayer.ridingEntity == null) || !ModMain.onlyCritical) {
				EnumParticleTypes t = GuiUtils.getParticleByName(ModMain.particleType);
				t = t!=null ? t : EnumParticleTypes.CRIT;
				for (int i = 0; i < ModMain.moreParticles; i++)
					GuiUtils.emitParticleAtEntity(ev.target, t);
			}
		}
	}

	@SubscribeEvent
	public void onChatReceive(ClientChatReceivedEvent event) {
		if (event.type != 2) { // Text
			if (!event.message.getUnformattedText().equals("")) {
				if (ModMain.dateChat) {
					DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
					Calendar cal = Calendar.getInstance();
					if (event.message.getChatStyle() == null)
						event.message.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));
					IChatComponent message = new ChatComponentText(dateFormat.format(cal.getTime()))
							.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY))
							.appendSibling(new ChatComponentText(" ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)));
					event.message = message.appendSibling(event.message);
				}
				boolean a = true;
				if (ModMain.lockMessage)
					for (int i = 0; i < ModMain.AntiMessage.length; i++)
						if (event.message.getUnformattedText().toLowerCase()
								.contains(ModMain.AntiMessage[i].toLowerCase())) {
							a = false;
							Logger logger = LogManager.getLogger();
							logger.info((new StringBuilder()).append("[CHAT-REMOVED] ")
									.append(event.message.getUnformattedText()).toString());
							event.setCanceled(true);
						}
				if (ModMain.songMessage && a)
					for (int i = 0; i < ModMain.HeyMessage.length; i++)
						if (event.message.getUnformattedText().toLowerCase()
								.contains(ModMain.HeyMessage[i].toLowerCase())) {
							a = false;
							mc.thePlayer.playSound(ModMain.infoSong, 2.0F, 2.0F);
						}
				if (event.message.getUnformattedText().toLowerCase()
						.contains(mc.getSession().getUsername().toLowerCase()) && ModMain.usernameSong && a)
					mc.thePlayer.playSound("note.pling", 2.0F, 2.0F);
				if (!mc.isSingleplayer() && !ModMain.isinConnect) {
					if (event.message.getUnformattedText().contains("/register"))
						if (ModMain.passwordList.containsKey(mc.getCurrentServerData().serverIP)) {
							mc.thePlayer
									.sendChatMessage((new StringBuilder()).append("/register ")
											.append((String) ModMain.passwordList.get((new StringBuilder())
													.append(mc.getCurrentServerData().serverIP).append(" ")
													.append((String) ModMain.passwordList
															.get(mc.getCurrentServerData().serverIP))
													.toString()))
											.toString());
							ModMain.isConnect = true;
						} else
							mc.displayGuiScreen(new GuiConnection("register", 2));
					else if (event.message.getUnformattedText().contains("/login"))
						if (ModMain.passwordList.containsKey(mc.getCurrentServerData().serverIP)) {
							mc.thePlayer.sendChatMessage((new StringBuilder()).append("/login ")
									.append((String) ModMain.passwordList.get(mc.getCurrentServerData().serverIP))
									.toString());
							ModMain.isConnect = true;
						} else
							mc.displayGuiScreen(new GuiConnection("login", 1));
				}
			}
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (eventArgs.modID.equals(ModMain.MOD_ID))
			ModMain.syncConfig();
	}

	@SubscribeEvent
	public void onConnectOnServer(
			net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent event) {
		ModMain.createNewAtianHudBridge(event.manager.channel().pipeline());
		ModMain.isConnect = false;
		isEnable = false;
		friendConnected.clear();
	}
	@SubscribeEvent
	public void onDisconnectOnServer(
			net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
		ModMain.isConnect = false;
		isEnable = false;
		ModMain.lastDeconnectionTime = System.currentTimeMillis();
	}
	@SubscribeEvent
	public void onDrawScreen(DrawScreenEvent ev) {
		if(ev.gui instanceof GuiDisconnected) {
			ATEHUDAutoReconnect.packedFGColour = Colors.redGreenOptionInt(ModMain.autoReco);
			ATEHUDAutoReconnect.displayString = I18n.format("atehud.reconect.auto").replaceAll("TIME",
					(ModMain.autoReco ? 
							String.valueOf((int) ((ModMain.lastDeconnectionTime+ModMain.timeToWait-System.currentTimeMillis())/1000L)) : "5") + "s");
		}
		if(ev.gui instanceof GuiMainMenu) {
			ev.gui.mc.fontRendererObj.drawString(
					ModMain.MOD_LITTLE_NAME + " " + ModMain.MOD_VERSION + " - " + ev.gui.mc.getSession().getUsername(), 1, 1,
					Colors.RED);
		}
		if(ev.gui instanceof RightClickGuiScreen && rightClickElement!=null)
			rightClickElement.render(ev.mouseX, ev.mouseY);
		ScaledResolution sr = new ScaledResolution(mc);
		for (Element e: hoverRenderer)
			if(e!=null)
				e.onHover(Mouse.getX() / sr.getScaleFactor(),
					ev.gui.height - (Mouse.getY() / sr.getScaleFactor()));
		ATEEventHandler.ClearHoverElement();

	}
	@SubscribeEvent
	public void onInitGui(InitGuiEvent.Post ev) {
		rightClickElement = null;
		if (ev.gui instanceof GuiChat) {
			Field[] fs = GuiChat.class.getDeclaredFields();
			chat = null;
			boolean a = false;
			for (int i = 0; i < fs.length; i++) {
				if (fs[i].getType().getName().equals(GuiTextField.class.getName())) {
					try {
						fs[i].setAccessible(true);
						if (chat == null) {
							chat = (GuiTextField) fs[i].get(ev.gui);
							char[] c = Chat.getAllSign();
							ev.buttonList.add(ATEHUDChatnextPage = new GuiButton(1909, ev.gui.width - 42, 52, 41, 20, "-->"));
							ev.buttonList.add(ATEHUDChatlastPage = new GuiButton(1910, ev.gui.width - 4 * 21, 52, 41, 20, "<--"));
							int rp = 13;
							ev.buttonList.add(ATEHUDChatTest = new GuiButton(1906, ev.gui.width - 101, ev.gui.height - (rp+=21), 100, 20, I18n.format("atehud.chat.test")));
							ev.buttonList.add(ATEHUDChatOption = new GuiButton(1907, ev.gui.width - 101, ev.gui.height - (rp+=21), 100, 20, I18n.format("atehud.chat.option")));
							if(ModMain.showButton)
								ev.buttonList.add(ATEHUDChatSpammer = new GuiButton(1908, ev.gui.width - 101, ev.gui.height - (rp+=21), 100, 20, I18n.format("chatOption.spammer.button")));
							ev.buttonList.add(ATEHUDChatFriend = new GuiButton(1911, ev.gui.width - 101, ev.gui.height - (rp+=21), 100, 20, I18n.format("chatOption.friend.friendConnect")));
							ev.buttonList.add(ATEHUDChatLocation = new GuiButton(1912, ev.gui.width - 101, ev.gui.height - (rp+=21), 100, 20, I18n.format("chatOption.location")));
							for (int j = 0; j < 8; j++)
								ATEHUDcharButton[j]=(new GuiButton(1905,ev.gui.width - 4 * 21 + (j%4)*21, 10+21*((j/4)%2), 20, 20,String.valueOf(c[j])));
							getCharButtons(ev.gui);
							for (GuiButton b: ATEHUDcharButton) ev.buttonList.add(b);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else
					a = false;
			}
		}
		if (ev.gui instanceof GuiMainMenu) {
			if (ModMain.useAdvancedMenu) {
				ev.gui.mc.displayGuiScreen(new GuiAdvancedMenu((GuiMainMenu) ev.gui));
			} else
				ev.buttonList.add(ATEHUDOptionsButton = new GuiButton(1901, ev.gui.width / 2 - 100,
						ev.gui.height / 4 + 48 + 72 + 12 + 24, I18n.format("atehud.menu")));
		}
		if (ev.gui instanceof GuiDisconnected) {
			int posX = 0;
			for (int i = 0; i < ev.buttonList.size(); i++)
				if (((GuiButton) ev.buttonList.get(i)).yPosition > posX)
					posX = ((GuiButton) ev.buttonList.get(i)).yPosition;
			if (posX != 0) {
				ev.buttonList.add(ATEHUDReconnect = new GuiButton(1903, ev.gui.width / 2 - 100, posX + 21,
						I18n.format("atehud.reconect")));
				ev.buttonList.add(ATEHUDAutoReconnect = new GuiButton(1902, ev.gui.width / 2 - 100, posX + 42,
						I18n.format("atehud.reconect.auto").replaceAll("TIME",
								String.valueOf((int) ((ModMain.lastDeconnectionTime+ModMain.timeToWait-System.currentTimeMillis())/1000L)) + "s")));
			}
		}
		if (ev.gui instanceof GuiMultiplayer || ev.gui instanceof GuiLocalConnection)
			ev.buttonList.add(
					ATEHUDLocal = new GuiButton(1904, ev.gui.width / 2 - 154, 8, 70, 20, I18n.format("atehud.local")));
	}
	@SubscribeEvent
	public void onKeyInput(net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent event) {
		if (ModMain.guifactory.isPressed())
			mc.displayGuiScreen(new GuiMenu());
		if (ModMain.nbtitem.isPressed())
			mc.displayGuiScreen(new GuiNbtCode());
		if (ModMain.nbttile.isPressed())
			GuiNbtCode.loadVisible();
		if (mc.currentScreen==null) {
			for (KeyBind kb: ModMain.keyBindingList) {
				if(Keyboard.isKeyDown(kb.key) && !Keyboard.isRepeatEvent()) {
			        CommandUtils.sendMessage(kb.command);
				}
			}
		}
	}

	@SubscribeEvent
	public void onMouse(MouseEvent ev) {
		if(!hasCountClick && ev.buttonstate) {
			hasCountClick = true;
			if(ev.button==0)
				PvpUtils.addLeftClickToCpsCounter();
			else if(ev.button==1)
				PvpUtils.addRightClickToCpsCounter();
		}
	}

	@SubscribeEvent
	public void onMouseClick(MouseInputEvent.Post ev) {
		ScaledResolution sr = new ScaledResolution(mc);
		int mouseX = Mouse.getX()/sr.getScaleFactor();
		int mouseY = Mouse.getY()/sr.getScaleFactor();
		preventDoubleClick = 0;
		if(rightClickElement!=null) {
			if(GuiUtils.isHover(rightClickElement, mouseX, mouseY)) {
				int b = Mouse.isButtonDown(0) ? 0 : (Mouse.isButtonDown(1) ? 1 : (Mouse.isButtonDown(2) ? 2 : 0));
				rightClickElement.mouseClick(mouseX, mouseY, b);
				rightClickElement = null;
				ev.setCanceled(true);
				return;
			} else {
				rightClickElement = null;
				ev.setCanceled(true);
				return;
			}
		}
		if(ev.gui instanceof RightClickGuiScreen && Mouse.isButtonDown(1)) {
			rightClickElement = ((RightClickGuiScreen)ev.gui).getRightClickElement(mouseX, mouseY);
		}
	}

	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent ev) {
		if (GuiTeleporter.toTpPlayers != null) {
			if (GuiTeleporter.toTpPlayers.size() > GuiTeleporter.currentPlayerShow) {
				GuiUtils.drawCenterString(mc.fontRendererObj,
						I18n.format("teleporter.teleport.all") + ":  "
								+ GuiTeleporter.toTpPlayers.get(GuiTeleporter.currentPlayerShow).getGameProfile()
										.getName()
								+ Chat.AQUA + " [" + Chat.GOLD + (int) (GuiTeleporter.currentPlayerShow + 1) + Chat.AQUA
								+ " / " + Chat.GOLD + GuiTeleporter.toTpPlayers.size() + Chat.AQUA + "]",
						ev.resolution.getScaledWidth() / 2, ev.resolution.getScaledHeight() / 2, Colors.GOLD);
				NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
				List<NetworkPlayerInfo> list = ATEEventHandler.field_175252_a
						.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
				List<String> names = new ArrayList<String>();
				boolean a = false;
				for (Iterator<NetworkPlayerInfo> iterator = list.iterator(); iterator.hasNext();) {
					String string = (String) iterator.next().getGameProfile().getName();
					if (string.contains(
							GuiTeleporter.toTpPlayers.get(GuiTeleporter.currentPlayerShow).getGameProfile().getName()))
						a = true;
				}
				if (a) {
					if (!GuiTeleporter.hasSendPacket) {
						tpOnplayer();
					} else if (GuiTeleporter.timeonsend + 1000 < mc.getSystemTime()) {
						tpOnplayer();
					}
					Iterator<EntityPlayer> player = mc.theWorld.playerEntities.iterator();
					while (player.hasNext()) {
						EntityPlayer p = player.next();
						if (GuiTeleporter.toTpPlayers.size() > GuiTeleporter.currentPlayerShow && p.getDisplayName()
								.getUnformattedText().contains(((NetworkPlayerInfo) GuiTeleporter.toTpPlayers
										.get(GuiTeleporter.currentPlayerShow)).getGameProfile().getName())) {
							GuiTeleporter.info
									.add(p.getDisplayName().getUnformattedText() + " - " + p.posX
											+ " / " + p.posY + " / " + p.posZ);
							GuiTeleporter.hasSendPacket = false;
							GuiTeleporter.currentPlayerShow++;
							GuiTeleporter.currentTP = 0;
							System.out.println(p.getDisplayName().getUnformattedText());
						}
					}
				} else {
					GuiTeleporter.hasSendPacket = false;
					GuiTeleporter.currentPlayerShow++;
					GuiTeleporter.currentTP = 0;
				}
			} else {
				try {
					String s = "playerlist" + mc.getSystemTime()/(60*60*1000)%24+":"+
							mc.getSystemTime()/(60*1000)%60+":"+mc.getSystemTime()/1000%60 + ".txt";
					Writer writer = new FileWriter(s);
					for (int i = 0; i < GuiTeleporter.info.size(); i++) {
						writer.write('\n' + GuiTeleporter.info.get(i));
					}
					writer.close();
					Chat.show(I18n.format("teleporter.teleport.all.done", s));
				} catch (Exception e) {
					Chat.error(I18n.format("teleporter.teleport.all.error"));
					e.printStackTrace();
				}
				GuiTeleporter.info = null;
				GuiTeleporter.toTpPlayers = null;
			}
		}
		if (ev.type == RenderGameOverlayEvent.ElementType.TEXT) {
			OverlayDisplayManager.overlayDisplayer(mc, ev.resolution, ev);
		} else if (ev.type == RenderGameOverlayEvent.ElementType.DEBUG) {
			if (ModMain.EntityDebug) {
				if (mc.thePlayer.ridingEntity instanceof EntityHorse) {
					EntityHorse baby = (EntityHorse) mc.thePlayer.ridingEntity;
					GuiUtils.drawInventory(mc, ev.resolution.getScaledWidth(), ev.resolution.getScaledHeight(),
							new String[] { EnumChatFormatting.AQUA + baby.getDisplayName().getFormattedText(),
									I18n.format("gui.act.invView.horse.jump") + " : "
											+ GuiInvView.getJumpFormattedText(GuiInvView.GetHorseMaxJump(baby)),
									I18n.format("gui.act.invView.horse.speed") + " : "
											+ GuiInvView.getSpeedFormattedText(
													baby.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
															.getAttributeValue() * 43)
											+ " m/s",
									I18n.format("gui.act.invView.horse.health") + " : "
											+ GuiInvView.getHpFormattedText(
													((int) (baby.getEntityAttribute(SharedMonsterAttributes.maxHealth)
															.getAttributeValue()) * 10) / 20)
											+ " HP" },
							baby, false, new ItemStack[0]);
				} else {
					MovingObjectPosition obj = mc.objectMouseOver;
					if (obj != null && obj.typeOfHit.equals(MovingObjectType.ENTITY)
							&& obj.entityHit instanceof EntityLivingBase) {
						EntityLivingBase elb = (EntityLivingBase) obj.entityHit;

						List<String> text1 = Lists.newArrayList();

						text1.add(EnumChatFormatting.AQUA + elb.getDisplayName().getFormattedText());
						if (elb instanceof EntityHorse) {
							EntityHorse baby = (EntityHorse) elb;
							text1.add(I18n.format("gui.act.invView.horse.jump") + " : "
									+ GuiInvView.getJumpFormattedText(GuiInvView.GetHorseMaxJump(baby)));
							text1.add(I18n.format("gui.act.invView.horse.speed") + " : "
									+ GuiInvView.getSpeedFormattedText(
											baby.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
													.getAttributeValue() * 43)
									+ " m/s");
							text1.add(I18n.format("gui.act.invView.horse.health") + " : "
									+ GuiInvView.getHpFormattedText(
											((int) (baby.getEntityAttribute(SharedMonsterAttributes.maxHealth)
													.getAttributeValue()) * 10) / 20)
									+ " HP");
						}
						String[] text = new String[text1.size()];
						for (int i = 0; i < text1.size(); i++) {
							text[i] = text1.get(i);
						}
						GuiUtils.drawInventory(mc, ev.resolution.getScaledWidth(), ev.resolution.getScaledHeight(),
								text, elb, false, new ItemStack[0]);
					} else {
						GuiUtils.drawInventory(mc, ev.resolution.getScaledWidth(), ev.resolution.getScaledHeight(),
								new String[] { "" }, mc.thePlayer, false, new ItemStack[0]);
					}

				}
			}
		}
		if(isEnable && ModMain.renderGameOverlay) {
			GuiUtils.drawRightString(mc.fontRendererObj, String.valueOf(action) + " / " + String.valueOf(ModMain.numberOfAction), 
					ev.resolution.getScaledWidth(), 0, Color.CYAN.getRGB());
			GlStateManager.color(1.0F, 1.0F, 1.0F);
		}
		if (fr.atesab.atehud.Mod.modsLoaded)
			for (int i = 0; i < fr.atesab.atehud.Mod.getMods().size(); i++)
				fr.atesab.atehud.Mod.getMods().get(i).renderOverlay(ev);
		GlStateManager.color(1.0F, 1.0F, 1.0F);
	}
	@SubscribeEvent
	public void onTick(TickEvent ev) {
		//cps mod
		hasCountClick=false;
		if (mc.theWorld != null && mc.thePlayer != null
				&& mc.thePlayer.inventoryContainer.inventoryItemStacks != null) { // saveInventory
			if (mc.thePlayer.isDead) {
				if (fr.atesab.atehud.gui.GuiInventory.lastInventory != fr.atesab.atehud.gui.GuiInventory.currentInventory) {
					fr.atesab.atehud.gui.GuiInventory.lastInventory = fr.atesab.atehud.gui.GuiInventory.currentInventory;
					Chat.showWithCommand(
							I18n.format("gui.act.invView.global.last.save", (int) mc.thePlayer.posX,
									(int) mc.thePlayer.posY, (int) mc.thePlayer.posZ),
							"/tp " + (int) mc.thePlayer.posX + " " + (int) mc.thePlayer.posY + " "
									+ (int) mc.thePlayer.posZ,
							I18n.format("gui.act.invView.global.last.save.tp"));
				}
			} else {
				fr.atesab.atehud.gui.GuiInventory.currentInventory = mc.thePlayer.inventoryContainer.inventoryItemStacks;
			}
		}
		// Teamspeak3 INTEGRATIONs
		try {
			if (ModMain.genTS3 && ((TS3Socket.defineClientListPhase == 0
					&& TS3Socket.lastDefineClient + ModMain.ts3ClientListRefreshInMilli < System.currentTimeMillis())
					|| (TS3Socket.lastDefineClient + ModMain.ts3ClientListRefreshTimedOutInMilli < System
							.currentTimeMillis()))) {
				TS3Socket.defineClientListPhase = 2;
				TS3Socket.sendCommand("currentschandlerid");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// AutoReco
		if (ModMain.autoReco && mc.currentScreen instanceof GuiDisconnected) {
			if(ModMain.lastDeconnectionTime+ModMain.timeToWait<System.currentTimeMillis())
				net.minecraftforge.fml.client.FMLClientHandler.instance().connectToServer(getParent(mc.currentScreen),
						ModMain.lastServerData);
		}
		//Spammer
		if(isEnable){
			if(!(mc.currentScreen instanceof GuiSpammer) && ModMain.stopOnClose) {
				isEnable = false;
			}else if(action<ModMain.numberOfAction){
				if(tickw==ModMain.ticktowait)tickw=0;
				if(tickw==0){
					if(action%5==0 && !hasDoFakemsg){
						mc.thePlayer.sendChatMessage("/"+new Random());
						hasDoFakemsg=true;
					}else{
						hasDoFakemsg=false;
						action++;
						CommandUtils.sendOptionMessage("/"+ModMain.spamCommand);
					}
				}
				tickw++;
			}else{
				isEnable=false;
			}
		}
		if (mc.getCurrentServerData() != null)
			ModMain.lastServerData = mc.getCurrentServerData();
		if (ModMain.zoom.isKeyDown()) {
			if (currentFOV == -1F)
				currentFOV = mc.gameSettings.fovSetting;
			isZoom = true;
		} else
			isZoom = false;
		if (isZoom && mc.currentScreen == null) {
			if (ModMain.isScreenBacked())
				mc.gameSettings.fovSetting = 360F - ModMain.zoom_value;
			else
				mc.gameSettings.fovSetting = ModMain.zoom_value;
		} else {
			if (mc.gameSettings.fovSetting == 360F - ModMain.zoom_value
					|| mc.gameSettings.fovSetting == ModMain.zoom_value)
				mc.gameSettings.fovSetting = currentFOV;
			else if (currentFOV != -1F)
				mc.gameSettings.fovSetting = currentFOV;
		}
		// tickeventMod
		if (fr.atesab.atehud.Mod.modsLoaded)
			for (int i = 0; i < fr.atesab.atehud.Mod.getMods().size(); i++)
				fr.atesab.atehud.Mod.getMods().get(i).tick(ev);
		if (mc.thePlayer != null) { // friendSys
			NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
			List list = field_175252_a.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
			List<String> names = new ArrayList<String>();
			for (Iterator iterator2 = list.iterator(); iterator2.hasNext();) {
				names.add(((NetworkPlayerInfo) iterator2.next()).getGameProfile().getName());
			}
			ArrayList<String> lefts = new ArrayList<String>();
			ArrayList<String> joins = new ArrayList<String>();
			for (int j = 0; j < friendConnected.size(); j++) {
				String name = (String) friendConnected.get(j);
				if (!names.contains(name)) {
					friendConnected.remove(name);
					if (ModMain.friendJoinMessage)
						lefts.add(name);
				}
			}
			for (int j = 0; j < playerConnected.size(); j++) {
				String name = (String) playerConnected.get(j);
				if (!names.contains(name)) {
					playerConnected.remove(name);
					if (ModMain.allJoinMessage && !ModMain.getArray(ModMain.friend).contains(name))
						lefts.add(name);
				}
			}
			for (int i = 0; i < ModMain.friend.length; i++) {
				if (names.contains(ModMain.friend[i]) && !friendConnected.contains(ModMain.friend[i])) {
					if (ModMain.friendJoinMessage)
						joins.add(ModMain.friend[i]);
					friendConnected.add(ModMain.friend[i]);
				}
			}
			for (int i = 0; i < names.size(); i++) {
				if (!playerConnected.contains(names.get(i))) {
					if (ModMain.allJoinMessage)
						joins.add(names.get(i));
					playerConnected.add(names.get(i));
				}
			}
			if (lefts.size() > ModMain.maxJoinLeftMessage)
				Chat.show(I18n.format("chatOption.friend.massLeft", ModMain.maxJoinLeftMessage));
			else
				for (int i = 0; i < lefts.size(); i++) {
					Chat.send(MultiChatComponentText.getFriendElements(
							I18n.format("chatOption.friend.disconnected") + ">", lefts.get(i), false,
							EnumChatFormatting.DARK_GREEN));
				}
			if (joins.size() > ModMain.maxJoinLeftMessage)
				Chat.show(I18n.format("chatOption.friend.massJoin", ModMain.maxJoinLeftMessage));
			else
				for (int i = 0; i < joins.size(); i++) {
					Chat.send(MultiChatComponentText.getFriendElements(I18n.format("chatOption.friend.connected") + ">",
							joins.get(i), false, EnumChatFormatting.DARK_GREEN));
				}
		}
	}
	@SubscribeEvent
	public void onToolTip(ItemTooltipEvent ev) {
		if (ev.itemStack != null) {
			if (ModMain.TTDura && ev.itemStack.getMaxDamage() != 0) {
				for (int i = 0; i < ev.toolTip.size(); i++) {
					String type = (String) ev.toolTip.get(i);
					if (type.equals((new StringBuilder()).append("Durability: ")
							.append(ev.itemStack.getMaxDamage() - ev.itemStack.getItemDamage()).append(" / ")
							.append(ev.itemStack.getMaxDamage()).toString()))
						ev.toolTip.remove(i);
				}
				int dmg = Math.abs(ev.itemStack.getItemDamage() - ev.itemStack.getMaxDamage()) + 1;
				double maxdmg = (double)(ev.itemStack.getMaxDamage() + 1);
				ev.toolTip.add((new StringBuilder()).append("\247b")
						.append(I18n.format("gui.atehud.TT.duraInfo", new Object[0])
								.replaceAll("DURABILITY", String.valueOf(dmg))
								.replaceAll("MAXDURA", String.valueOf((int)maxdmg))
								.replaceAll("COLOR", (new StringBuilder()).append('\247').append(
										dmg < (int) (1.0D * maxdmg) ? "2" : 
											(dmg < (int) (0.75D * maxdmg) ? "a" : 
												(dmg < (int) (0.5D * maxdmg) ? "6" : 
													(dmg < (int) (0.25D * maxdmg) ? "c" : 
														(dmg < (int) (0.1D * maxdmg) ? "4" : "2"))))
										).toString()))
						.toString());
			}
			if (ModMain.TTRepair && ev.itemStack.getTagCompound() != null
					&& ev.itemStack.getTagCompound().getInteger("RepairCost") != 0) {
				int dmg = ev.itemStack.getTagCompound().getInteger("RepairCost");
				double maxdmg = 31;
				ev.toolTip.add((new StringBuilder()).append("\247b")
						.append(I18n.format("gui.atehud.TT.rpInfo", new Object[0])
								.replaceAll("REPAIRCOST", String.valueOf(dmg))
								.replaceAll("COLOR", (new StringBuilder()).append('\247').append(
										dmg < (int) (.1D * maxdmg) ? "2" : 
											(dmg < (int) (0.25D * maxdmg) ? "a" : 
												(dmg < (int) (0.5D * maxdmg) ? "6" : 
													(dmg < (int) (0.75D * maxdmg) ? "c" : 
														(dmg < (int) (1D * maxdmg) ? "4" : "2"))))
										).toString()))
						.toString());
			}
			if (ModMain.TTTags && ev.itemStack.getTagCompound() != null
					&& ev.itemStack.getTagCompound().getKeySet().size() != 0) {
				for (int i = 0; i < ev.toolTip.size(); i++) {
					String type = (String) ev.toolTip.get(i);
					if (type.equals((new StringBuilder()).append(EnumChatFormatting.DARK_GRAY).append("NBT: ")
							.append(ev.itemStack.getTagCompound().getKeySet().size()).append(" tag(s)").toString()))
						ev.toolTip.remove(i);
				}

				String a = "";
				for (Iterator iterator = ev.itemStack.getTagCompound().getKeySet().iterator(); iterator.hasNext();) {
					String tag = (String) iterator.next();
					a = (new StringBuilder()).append(a).append("\247f[\247e").append(tag).append('\247').append("f] ")
							.toString();
				}

				ev.toolTip.add((new StringBuilder()).append("\247b")
						.append(I18n.format("gui.atehud.TT.tagListInfo", new Object[0]).replaceAll("TAGLIST", a)
								.replace("SIZE", String.valueOf(ev.itemStack.getTagCompound().getKeySet().size())))
						.toString());
			}
			if (!(mc.currentScreen instanceof GuiNbtCode) && ModMain.TTGiver) {
				ItemStack itemStack = ev.itemStack;
				String lineToAdd[] = I18n.format("gui.act.inhanditem.tt", new Object[0])
						.replaceAll("KEY",
								(new StringBuilder()).append(EnumChatFormatting.AQUA)
										.append(Keyboard.getKeyName(ModMain.nbtitem.getKeyCode()))
										.append(EnumChatFormatting.YELLOW).toString())
						.split("::");
				for (int i = 0; i < lineToAdd.length; i++) {
					ev.toolTip.add(
							(new StringBuilder()).append(EnumChatFormatting.YELLOW).append(lineToAdd[i]).toString());
				}

				if (Keyboard.isKeyDown(ModMain.nbtitem.getKeyCode()) && mc.thePlayer != null) {
					mc.displayGuiScreen(new GuiNbtCode(mc.currentScreen, itemStack));
				}
			}
		}
	}
	@SubscribeEvent
	public void onWoldRender(RenderWorldLastEvent ev) {
		if (mc.theWorld != null) {
			if (ModMain.Fullbright) {
				if (mc.gameSettings.gammaSetting < 16F)
					mc.gameSettings.gammaSetting += 0.5F;
			} else if (mc.gameSettings.gammaSetting > 0.5F) {
				if (mc.gameSettings.gammaSetting < 1.0F)
					mc.gameSettings.gammaSetting = 0.5F;
				else
					mc.gameSettings.gammaSetting -= 0.5F;
			}
			if (ModMain.displayChunk) {
				for (int i = -ModMain.chunkViewRange; i < ModMain.chunkViewRange * 2 + 1; i++) {
					for (int j = -ModMain.chunkViewRange; j < ModMain.chunkViewRange + 1; j++) {
						GuiUtils.drawChunk(mc.thePlayer.chunkCoordX + i, mc.thePlayer.chunkCoordZ + j, Color.GREEN);
					}
				}
			}
			GuiMenu.uco.clear();
            GuiMenu.ucoE.clear();
            GuiMenu.bmc = 0;
            if(mc.theWorld!=null) {
            	for (TileEntity e: mc.theWorld.loadedTileEntityList) {
            		if(!(e instanceof TileEntityChest || e instanceof TileEntityMobSpawner || e instanceof TileEntityEnderChest))continue;
        			BlockPos bp = e.getPos();
            		if(ModMain.UCOsys){
            			AxisAlignedBB aabb = new AxisAlignedBB(
            					(double)bp.getX() - mc.thePlayer.posX, 
            					(double)bp.getY() - mc.thePlayer.posY, 
            					(double)bp.getZ() - mc.thePlayer.posZ, 
            					(double)bp.getX() - mc.thePlayer.posX+1.0D, 
            					(double)bp.getY() - mc.thePlayer.posY+1.0D, 
            					(double)bp.getZ() - mc.thePlayer.posZ+1.0D);
            			if(!(e instanceof TileEntityMobSpawner))
            				GuiUtils.renderOffsetAABB(aabb, 0, 0, 0, 0.0F, 255F, 0.0F,0.15F);
	                    if(e instanceof TileEntityChest)
	                    	GuiUtils.renderOffsetAABB(aabb, 0, 0, 0, 255F, 0.0F, 0.0F,0.15F);
	                    if(e instanceof TileEntityEnderChest)
	                    	GuiUtils.renderOffsetAABB(aabb, 0, 0, 0, 0.0F, 0.0F, 255F,0.15F);
            		}
            		GuiMenu.uco.add(e);
            		if(bp.getY() > 200)GuiMenu.bmc++;
            	}
            	for (Entity e: mc.theWorld.loadedEntityList) {
            		if(!(e instanceof EntityPlayer) || (e instanceof EntityPlayerSP)) continue;
            		EntityPlayer p = (EntityPlayer) e;
            		GuiMenu.ucoE.add(p);
                    if(ModMain.UCOsysp) 
                    	GuiUtils.renderOffsetAABB(
                    			new AxisAlignedBB(
                    					(double)p.posX - mc.thePlayer.posX-0.5D,(double)p.posY - mc.thePlayer.posY,(double)p.posZ - mc.thePlayer.posZ-0.5D,
                    					((double)p.posX - mc.thePlayer.posX)+0.5D,(double)p.posY - mc.thePlayer.posY+2.0D,((double)p.posZ - mc.thePlayer.posZ)+0.5D),
                    			0, 0, 0,0.0F, 255F, 255F,0.15F);
            	}
            }
			if (fr.atesab.atehud.Mod.modsLoaded)
				for (int i = 0; i < fr.atesab.atehud.Mod.getMods().size(); i++)
					fr.atesab.atehud.Mod.getMods().get(i).renderWorld(ev);
		}
	}
	@SubscribeEvent
	public void onAtianPluginMessage(AtianPluginMessageEvent ev) {
		System.out.println("AtianPluginMessage> "+ev.getName()+" "+ev.getArgs());
	}
}