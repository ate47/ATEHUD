package fr.atesab.atehud;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.gui.GuiNbtCode;
import fr.atesab.atehud.utils.MultiChatComponentText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

public class Chat {

	public static final char c_Modifier = '\u00a7';
	public static final String BLACK = c_Modifier + "0";
	public static final String DARK_BLUE = c_Modifier + "1";
	public static final String DARK_GREEN = c_Modifier + "2";
	public static final String DARK_AQUA = c_Modifier + "3";
	public static final String DARK_RED = c_Modifier + "4";
	public static final String DARK_PURPLE = c_Modifier + "5";
	public static final String GOLD = c_Modifier + "6";
	public static final String GRAY = c_Modifier + "7";
	public static final String DARK_GRAY = c_Modifier + "8";
	public static final String BLUE = c_Modifier + "9";
	public static final String GREEN = c_Modifier + "a";
	public static final String AQUA = c_Modifier + "b";
	public static final String RED = c_Modifier + "c";
	public static final String LIGHT_PURPLE = c_Modifier + "d";
	public static final String YELLOW = c_Modifier + "e";
	public static final String WHITE = c_Modifier + "f";
	public static final String RESET = c_Modifier + "r";
	public static final String BOLD = c_Modifier + "l";
	public static final String ITALIC = c_Modifier + "o";
	public static final String UNDERLINE = c_Modifier + "n";
	public static final String STRIKETHROUGH = c_Modifier + "m";
	public static final String RANDOM = c_Modifier + "k";
	public static final String YOLO_HEAD = LIGHT_PURPLE+"("+WHITE+"\u00B0"+LIGHT_PURPLE+"."+WHITE+"\u00B0"+LIGHT_PURPLE+")/";
	public static final char c_Star = '\u2605';
	public static final char c_Star2 = '\u2739';
	public static final char c_Point = '\u2022';
	public static final char c_Check = '\u2714';
	public static final char c_Heart = '\u2764';
	public static final char c_Plus = '\u25C6';
	public static final char c_Plus2 = '\u2726';
	public static final char c_Cross = '\u2720';
	public static final char c_pencil = '\u270F';
	public static final char c_arrow_right = '\u27A1';
	public static final char c_arrow_left = '\u2B05';
	public static final char c_arrow_up = '\u2B06';
	public static final char c_arrow_down = '\u2B07';
	public static final char c_EternitySign = '\u058E';
	public static final char c_DeadHead = '\u2620';
	public static final char c_Nuclear = '\u2622';
	public static final char c_BioHazard = '\u2623';
	public static final char c_sun = '\u2600';
	public static final char c_cloud = '\u2601';
	public static final char c_umbrella = '\u2602';
	public static final char c_snowmen = '\u2603';
	public static final char c_comet = '\u2604';
	public static final char c_Star3 = '\u2605';
	public static final char c_Star_Clear = '\u2606';
	public static final char c_circle = '\u2609';
	public static final char c_Phone = '\u260e';
	public static final char c_Phone_Clear = '\u260f';

	public static final char c_number1 = '\u2776';
	public static final char c_number2 = '\u2777';
	public static final char c_number3 = '\u2778';
	public static final char c_number4 = '\u2779';
	public static final char c_number5 = '\u277a';
	public static final char c_number6 = '\u277b';
	public static final char c_number7 = '\u277c';
	public static final char c_number8 = '\u277d';
	public static final char c_number9 = '\u277e';
	public static final char c_number10 = '\u277f';

	public static final char c_number1_clear = '\u2780';
	public static final char c_number2_clear = '\u2781';
	public static final char c_number3_clear = '\u2782';
	public static final char c_number4_clear = '\u2783';
	public static final char c_number5_clear = '\u2784';
	public static final char c_number6_clear = '\u2785';
	public static final char c_number7_clear = '\u2786';
	public static final char c_number8_clear = '\u2787';
	public static final char c_number9_clear = '\u2788';
	public static final char c_number10_clear = '\u2789';
	private static Minecraft mc = Minecraft.getMinecraft();

	public static String cl(int code) {
		return c_Modifier + String.valueOf(code);
	}

	public static String cl(String code) {
		return c_Modifier + code;
	}

	public static void error(String error) {
		if (mc.thePlayer != null)
			if (error == I18n.format("gui.act.nocreative")) {
				noCheat(error);
			} else {
				send(getPrefix("ERROR", EnumChatFormatting.GOLD, EnumChatFormatting.RED, false, EnumChatFormatting.RED,
						error));
			}
	}

	public static char[] getAllSign() {
		Field[] fld = Chat.class.getFields();
		char[] s = new char[fld.length];
		int j = 0;
		for (int i = 0; i < s.length; i++) {
			try {
				s[j] = (Character) fld[i].get(char.class);
				j++;
			} catch (Exception e) {
			}
		}
		char[] s1 = new char[j];
		for (int i = 0; i < s1.length; i++) {
			s1[i] = s[i];
		}
		return s1;
	}

	public static String getModifier(String text) {
		return text.replaceAll("&&", String.valueOf(c_Modifier));
	}

	public static IChatComponent getPrefix(String title, EnumChatFormatting borderColor, EnumChatFormatting mainColor,
			boolean bold, EnumChatFormatting color, String text) {
		return getPrefix(title, borderColor, mainColor, bold, color, text, null);
	}

	public static IChatComponent getPrefix(String title, EnumChatFormatting borderColor, EnumChatFormatting mainColor,
			boolean bold, EnumChatFormatting color, String text, String upMessage) {
		return getPrefix(title, borderColor, mainColor, bold, color, text, upMessage, null);
	}

	public static IChatComponent getPrefix(String title, EnumChatFormatting borderColor, EnumChatFormatting mainColor,
			boolean bold, EnumChatFormatting color, String text, String upMessage, String command) {
		List<IChatComponent> lst = new ArrayList<IChatComponent>();
		lst.add(new ChatComponentText("[").setChatStyle(new ChatStyle().setColor(borderColor)));
		lst.add(new ChatComponentText(title).setChatStyle(new ChatStyle().setColor(mainColor).setBold(bold)));
		lst.add(new ChatComponentText("]").setChatStyle(new ChatStyle().setColor(borderColor)));
		lst.add(new ChatComponentText(" "));
		IChatComponent textElem = new ChatComponentText(text);
		ChatStyle style = new ChatStyle().setColor(color);
		if (command != null)
			style.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		if (upMessage != null)
			style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ChatComponentText(upMessage).setChatStyle(new ChatStyle().setColor(color))));
		lst.add(textElem.setChatStyle(style));
		return new MultiChatComponentText(lst);
	}

	public static void itemStack(ItemStack itemStack) {
		if (mc.thePlayer != null)
			if (itemStack != null) {
				String tag = "";
				if (itemStack.getTagCompound() != null) {
					tag = ",tag:" + itemStack.getTagCompound().toString();
				}
				List<IChatComponent> lst = new ArrayList<IChatComponent>();
				lst.add(new ChatComponentText("[").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
				lst.add(new ChatComponentText(ModMain.MOD_LITTLE_NAME)
						.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED).setBold(true)));
				lst.add(new ChatComponentText("]").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
				lst.add(new ChatComponentText(" "));
				IChatComponent textElem = new ChatComponentText(I18n.format("gui.act.give.msg"));
				ChatStyle style = new ChatStyle().setColor(EnumChatFormatting.GOLD);
				style.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
						"/atehud act opennbt " + GuiNbtCode.getTextFromIS(itemStack)));
				style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(
						"{id:" + ((ResourceLocation) Item.itemRegistry.getNameForObject(itemStack.getItem())).toString()
								+ tag + "}")));
				lst.add(textElem.setChatStyle(style));
				send(new MultiChatComponentText(lst));
			} else
				Chat.error(I18n.format("gui.act.give.fail2"));
	}

	public static void noCheat(String cheat) {
		if (mc.thePlayer != null)
			send(getPrefix("NoCheat", EnumChatFormatting.GOLD, EnumChatFormatting.RED, false, EnumChatFormatting.RED,
					cheat));

	}

	public static void send(List<IChatComponent> messageList) {
		if (mc.thePlayer != null) {
			for (int i = 0; i < messageList.size(); i++) {
				send(messageList.get(i));
			}
		}
	}

	public static void send(IChatComponent message) {
		if (mc.thePlayer != null) {
			mc.thePlayer.addChatComponentMessage(message);
		}
	}

	public static void send(IChatComponent[] messages) {
		if (mc.thePlayer != null) {
			for (int i = 0; i < messages.length; i++) {
				send(messages[i]);
			}
		}
	}

	public static void send(String message) {
		send(message, EnumChatFormatting.RESET, null);
	}

	public static void send(String message, EnumChatFormatting format) {
		send(message, format, null);
	}

	public static void send(String message, EnumChatFormatting format, String upMessage) {
		if (mc.thePlayer != null) {
			IChatComponent txt = new ChatComponentText(message);
			ChatStyle cs = new ChatStyle();
			if (upMessage != null)
				cs.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(upMessage)));
			cs.setColor(format);
			txt.setChatStyle(cs);
			send(txt);
		}
	}

	public static void send(String message, String upMessage) {
		send(message, EnumChatFormatting.RESET, upMessage);
	}

	public static void show(String message) {
		show(message, EnumChatFormatting.DARK_GREEN, null);
	}

	public static void show(String message, EnumChatFormatting format) {
		show(message, format, null);
	}

	public static void show(String message, EnumChatFormatting format, String upMessage) {
		if (mc.thePlayer != null) {
			send(getPrefix(ModMain.MOD_LITTLE_NAME, EnumChatFormatting.GOLD, EnumChatFormatting.RED, true, format,
					message, upMessage));
		}
	}

	public static void showWithCommand(String message, String command) {
		showWithCommand(message, command, null);
	}

	public static void showWithCommand(String message, String command, String upMessage) {
		if (mc.thePlayer != null) {
			send(getPrefix(ModMain.MOD_LITTLE_NAME, EnumChatFormatting.GOLD, EnumChatFormatting.RED, true,
					EnumChatFormatting.DARK_GREEN, message, upMessage, command));
		}
	}

	public static void teamspeak(String message) {
		if (mc.thePlayer != null) {
			send(getPrefix("ts", EnumChatFormatting.BLUE, EnumChatFormatting.AQUA, true, EnumChatFormatting.WHITE,
					message));
		}
	}
}
