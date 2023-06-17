package fr.atesab.atehud.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.superclass.Enchantments;
import fr.atesab.atehud.superclass.TextOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class CommandUtils {
	
	public static ArrayList<String> getEnchantList() {
		ArrayList<String> Enchants = new ArrayList<String>();
		for (int i = 0; i < Enchantments.enchantments.length; i++) {
			Enchants.add(Enchantments.enchantments[i].getName());
		}
		Enchants.sort(new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.toLowerCase().compareTo(o2.toLowerCase());
			}
		});
		return Enchants;
	}

	public static ArrayList<String> getInHandItemEnchantList() {
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		ItemStack is = player.inventory.getCurrentItem();
		ArrayList<String> Enchants = new ArrayList<String>();
		for (int i = 0; i < Enchantments.enchantments.length; i++) {
			for (int j = 0; j < is.getEnchantmentTagList().tagCount(); j++) {
				NBTTagCompound nbt = (NBTTagCompound) is.getEnchantmentTagList().get(j);
				if (nbt != null && nbt.getString("id").equals(String.valueOf(Enchantments.enchantments[i].getId())))
					Enchants.add(Enchantments.enchantments[i].getName());
			}
		}
		Enchants.sort(new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.toLowerCase().compareTo(o2.toLowerCase());
			}
		});
		return Enchants;
	}

	public static ArrayList<String> getItemList() {
		ArrayList<String> items = new ArrayList<String>();
		for (ResourceLocation name : Item.itemRegistry.getKeys()) {
			items.add(name.toString());
		}
		return items;
	}

	public static String getOptionText(String text) {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.thePlayer==null)return text;
		for (TextOption to: ModMain.textOptions) 
			if(mc.thePlayer!=null && mc.theWorld!=null)
				text=text.replaceAll("[&]"+to.getName(),to.getReplacement(mc));
			else text=text.replace('"',' ');
		return text;
		
	}

	public static ArrayList<String> getPlayerList() {
		ArrayList<String> Players = new ArrayList<String>();
		NetHandlerPlayClient nethandlerplayclient = Minecraft.getMinecraft().thePlayer.sendQueue;
		List<NetworkPlayerInfo> list = ATEEventHandler.field_175252_a
				.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
		for (int i = 0; i < list.size(); i++) {
			Players.add(list.get(i).getGameProfile().getName());
		}
		return Players;
	}

	public static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
		return networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText()
				: ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(),
						networkPlayerInfoIn.getGameProfile().getName());
	}
	public static List<String> getTabCompletion(List<String> options, String[] args) {
		List<String> options_End = new ArrayList<String>();
		if (args.length == 0)
			return options_End;
		String start = args[args.length - 1].toLowerCase();
		for (int i = 0; i < options.size(); i++) {
			if (options.get(i).toLowerCase().startsWith(start.toLowerCase()))
				options_End.add(options.get(i));
		}
		options_End.sort(new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		}); // sort by name
		return options_End;
	}
	public static void sendMessage(String message) {
		EntityPlayerSP p;
		if((p=Minecraft.getMinecraft().thePlayer)!=null) {
			if (net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(p, message) != 0) return;
	        p.sendChatMessage(message);
		}
	}
	public static void sendOptionMessage(String message) {
		sendMessage(getOptionText(message));
	}
}
