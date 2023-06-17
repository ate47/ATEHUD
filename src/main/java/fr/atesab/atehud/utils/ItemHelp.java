package fr.atesab.atehud.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;
import java.util.UUID;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.FakeItems;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.superclass.Firework;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ItemHelp {

	public static class ColoredTextInformation {
		public int color;
		public String text;

		public ColoredTextInformation(String text, int color) {
			this.color = color;
			this.text = text;
		}
	}

	public static ItemStack noGen = getNBT(new ItemStack(Blocks.barrier),
			"{display:{Name:\"" + Chat.c_Modifier + "e" + I18n.format("act.noGenItem") + "\",Lore:[\"" + Chat.c_Modifier
					+ "c" + I18n.format("act.noGenItem.lore") + "\"]}}");

	public static int maxXp = 127;

	public static final String book_nbt = "{pages:[\"[\\\"\\\",{\\\"text\\\":\\\"" + I18n.format("act.book.command")
			+ " :\\\",\\\"bold\\\":true,\\\"underlined\\\":true},{\\\"text\\\":\\\"\\n\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"run_command"
			+ "\\\",\\\"value\\\":\\\"/home\\\"},\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":{\\\"text\\\":\\\""
			+ I18n.format("act.book.executecommand")
			+ "\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_red\\\"}}},{\\\"text\\\":\\\""
			+ I18n.format("act.book.home") + "\\n\\\",\\\"hoverEvent"
			+ "\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":{\\\"text\\\":\\\""
			+ I18n.format("act.book.executecommand")
			+ "\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_red\\\"}}},{\\\"text\\\":\\\"\\\",\\\"color\\\":\\\"dark_gray\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"run_com"
			+ "mand\\\",\\\"value\\\":\\\"/plot home\\\"},\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":{\\\"text\\\":\\\""
			+ I18n.format("act.book.executecommand")
			+ "\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_red\\\"}}},{\\\"text\\\":\\\""
			+ I18n.format("act.book.plothome") + "\\\","
			+ "\\\"color\\\":\\\"dark_gray\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":{\\\"text\\\":\\\""
			+ I18n.format("act.book.executecommand")
			+ "\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_red\\\"}}},{\\\"text\\\":\\\"\\n\\\",\\\"color\\\":\\\"reset\\\",\\\"hoverEvent"
			+ "\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":{\\\"text\\\":\\\""
			+ I18n.format("act.book.executecommand")
			+ "\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_red\\\"}}},{\\\"text\\\":\\\"\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"run_command\\\",\\\"value\\\":\\\"/spawn\\\"},"
			+ "\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":{\\\"text\\\":\\\""
			+ I18n.format("act.book.executecommand")
			+ "\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_red\\\"}}},{\\\"text\\\":\\\""
			+ I18n.format("act.book.spawn") + "\\n\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\","
			+ "\\\"value\\\":{\\\"text\\\":\\\"" + I18n.format("act.book.executecommand")
			+ "\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_red\\\"}}},{\\\"text\\\":\\\""
			+ I18n.format("act.book.helloworld")
			+ "\\\",\\\"color\\\":\\\"dark_gray\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"run_command\\\",\\\"value\\\":\\\"/tellraw"
			+ " @a {\\\\\\\"text\\\\\\\":\\\\\\\"" + I18n.format("act.book.helloworld")
			+ "\\\\\\\",\\\\\\\"bold\\\\\\\":true,\\\\\\\"color\\\\\\\":\\\\\\\"dark_red\\\\\\\"}\\\"},\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":{\\\"text\\\":\\\""
			+ I18n.format("act.book.executecommand") + "\\\",\\\"bold\\\":t"
			+ "rue,\\\"color\\\":\\\"dark_red\\\"}}},{\\\"text\\\":\\\"\\n\\\",\\\"color\\\":\\\"reset\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":{\\\"text\\\":\\\""
			+ I18n.format("act.book.executecommand")
			+ "\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_red\\\"}}},{\\\"te" + "xt\\\":\\\""
			+ I18n.format("act.book.givehead")
			+ "\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"run_command\\\",\\\"value\\\":\\\"/give @p minecraft:skull 1 3 {SkullOwner: "
			+ Minecraft.getMinecraft().getSession().getUsername()
			+ "}\\\"},\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":{\\\"text\\\":\\\""
			+ I18n.format("act.book.executecommand") + ""
			+ "e\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_red\\\"}}},{\\\"text\\\":\\\"\\n\\n\\\"},{\\\"text\\\":\\\""
			+ I18n.format("act.book.page2")
			+ "\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":2},\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":{\\\"text\\\":\\\""
			+ I18n.format("act.book.gopage")
			+ "\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_green\\\"}}},{\\\"text\\\":\\\"\\n\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":{\\\"text\\\":\\\""
			+ I18n.format("act.book.gopage") + "\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_gr"
			+ "een\\\"}}},{\\\"text\\\":\\\"" + I18n.format("act.book.page3")
			+ "\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":3},\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":{\\\"text\\\":\\\""
			+ I18n.format("act.book.gopage") + "\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_gree"
			+ "n\\\"}}},{\\\"text\\\":\\\"\\n \\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"" + I18n.format("act.book.format")
			+ " :\\\",\\\"bold\\\":true,\\\"underlined\\\":true,\\\"color\\\":\\\"dark_red\\\"},{\\\"text\\\":\\\"\\n\\n"
			+ I18n.format("act.book.colors")
			+ " :\\\",\\\"color\\\":\\\"dark_red\\\"},{\\\"text\\\":\\\"\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"te"
			+ "xt\\\":\\\"&0 \\\",\\\"bold\\\":true},{\\\"text\\\":\\\"&1\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_blue\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"&2\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_green\\\"},{\\\"text\\\":\\\" \\\",\\\"colo"
			+ "r\\\":\\\"reset\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"&3\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_aqua\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"&4\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_red\\\"},{\\\"text\\\":\\\"\\n\\\",\\\"color"
			+ "\\\":\\\"reset\\\"},{\\\"text\\\":\\\"&5\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_purple\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"&6\\\",\\\"bold\\\":true,\\\"color\\\":\\\"gold\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\",\\\"bo"
			+ "ld\\\":true},{\\\"text\\\":\\\"&7\\\",\\\"bold\\\":true,\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"&8\\\",\\\"bold\\\":true,\\\"color\\\":\\\"dark_gray\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\",\\\"bo"
			+ "ld\\\":true},{\\\"text\\\":\\\"&9\\\",\\\"bold\\\":true,\\\"color\\\":\\\"blue\\\"},{\\\"text\\\":\\\"\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"&a\\\",\\\"bold\\\":true,\\\"color\\\":\\\"green\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\",\\\"bold\\\":true},{\\\"t"
			+ "ext\\\":\\\"&b\\\",\\\"bold\\\":true,\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"&c\\\",\\\"bold\\\":true,\\\"color\\\":\\\"red\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\",\\\"bold\\\":true},{\\\"text\\\""
			+ ":\\\"&d\\\",\\\"bold\\\":true,\\\"color\\\":\\\"light_purple\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\",\\\"bold\\\":true},{\\\"text\\\":\\\"&e\\\",\\\"bold\\\":true,\\\"color\\\":\\\"yellow\\\"},{\\\"text\\\":\\\"\\n\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\""
			+ "" + I18n.format("act.book.formating")
			+ "\\\",\\\"color\\\":\\\"dark_red\\\"},{\\\"text\\\":\\\"\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"&n\\\",\\\"underlined\\\":true},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"&m\\\",\\\"strikethrough\\\":true},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\""
			+ "reset\\\"},{\\\"text\\\":\\\"&l\\\",\\\"bold\\\":true},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"k\\\",\\\"obfuscated\\\":true},{\\\"text\\\":\\\"&k \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"&o\\\",\\\"italic\\\":true},{\\\"text\\\":\\\"\\n\\n \\\",\\\"c"
			+ "olor\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"ACT - Advanced Creative Tab\\\",\\\"bold\\\":true,\\\"underlined\\\":true,\\\"color\\\":\\\"dark_aqua\\\"},{\\\"text\\\":\\\"\\n\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\""
			+ I18n.format("act.book.author") + "\\\",\\\"bold\\\":true},{\\\"text\\\":\\\": ATE"
			+ "47\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"" + I18n.format("act.book.version")
			+ "\\\",\\\"bold\\\":true},{\\\"text\\\":\\\": " + ModMain.MOD_VERSION
			+ "\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Minecraft\\\",\\\"bold\\\":true},{\\\"text\\\":\\\": 1.8\\n\\nBorried about the old minecraft stuff ? This mod is made for you ! it"
			+ " add new unreal Item in your creative mode\\\",\\\"color\\\":\\\"reset\\\"}]\"],title:\""
			+ I18n.format("act.book.title") + "\",author:ATE47,display:{Lore:[\"" + I18n.format("act.book.lore")
			+ "\"]}}";

	public static ItemStack barrier = setMeta(new ItemStack(Blocks.stained_glass_pane), 3, " ");

	public static ItemStack border = setMeta(new ItemStack(Blocks.stained_glass_pane), 0, " ");
	public static List addBarrier(List subItems, int number) {
		return addBarrier(subItems, number, barrier);
	}
	public static List addBarrier(List subItems, int number, ItemStack Barrier) {
		for (int i = 0; i < number; i++) {
			subItems.add(Barrier);
		}
		return subItems;
	}

	public static List addGroup(List subItems, String[] group, String name) {
		addTitle(subItems, name);
		int i;
		for (i = 0; i < group.length; i++) {
			String[] a = group[i].split("::");
			if (a.length == 2)
				subItems.add(getHead(a[0], a[1]));
			else
				subItems.add(getHead(a[0]));
		}
		i = 9 - i % 9;
		if (i != 9)
			addBarrier(subItems, i);
		return subItems;
	}

	public static List addTitle(List subItems, String text) {
		addBarrier(subItems, 4);
		subItems.add(setMeta(new ItemStack(Blocks.stained_glass_pane), 4, text));
		addBarrier(subItems, 4);
		return subItems;
	}

	public static String createNBTFirework() {

		return null;
	}

	public static ItemStack getChestNBT(String name) {
		ItemStack is = new ItemStack(Items.fireworks);
		if (is.getTagCompound() == null) {
			is.setTagCompound(new NBTTagCompound());
		}
		if (is.getTagCompound() != null)
			is.getTagCompound().setString("Items", "");
		is.setStackDisplayName(name);
		return is;
	}

	public static ItemStack getCMD(String cmd, String name) {
		String cmdmod = cmd.replaceFirst("\\\\", "\\\\");
		cmdmod = cmd.replaceFirst("\"", "\\\"");
		ItemStack is = new ItemStack(Blocks.command_block);
		if (is.getTagCompound() == null) {
			is.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound cmd2 = new NBTTagCompound();
		cmd2.setString("Command", cmdmod);
		if (is.getTagCompound() != null)
			is.getTagCompound().setTag("BlockEntityTag", cmd2);
		System.out.println("Create commandblock with cmd=" + cmd);
		System.out.println(is.getTagCompound().toString());
		is.setStackDisplayName(name);
		return is;
	}

	public static int getCount(Minecraft mc, Item it) {
		int arrows = 0;
		for (int i = 0; i < mc.thePlayer.inventory.mainInventory.length; i++) {
			if (mc.thePlayer.inventory.mainInventory[i] != null
					&& mc.thePlayer.inventory.mainInventory[i].getItem().equals(it)) {
				arrows += mc.thePlayer.inventory.mainInventory[i].stackSize;
			}
		}
		for (int i = 0; i < mc.thePlayer.inventory.armorInventory.length; i++) {
			if (mc.thePlayer.inventory.armorInventory[i] != null
					&& mc.thePlayer.inventory.armorInventory[i].getItem().equals(it)) {
				arrows += mc.thePlayer.inventory.armorInventory[i].stackSize;
			}
		}
		return arrows;
	}

	/**
	 * Not implemented yet / Add custom head
	 * 
	 * @param url
	 *            link to your skin image :
	 *            skins.minecraft.net/MinecraftSkins/USERNAME.png
	 * @param name
	 *            Your Username
	 * @return
	 */
	public static ItemStack getCustomSkull(String url, String name) {

		String uuid = UUID.randomUUID().toString();
		byte[] encodedData = Base64.getEncoder().encode(String.format("{\"timestamp\":1429453091873,\"profileId\":\""
				+ uuid.replaceAll("-", "") + "\",textures:{SKIN:{url:\"" + url + "\"}}}", url).getBytes());
		ItemStack head = getNBT(new ItemStack(Items.skull, 1, (short) 3), "{SkullOwner:{Id:\"" + uuid
				+ "\",Properties:{textures:[{Value:\"" + (new String(encodedData)) + "\"}]}}}");
		if (!name.isEmpty())
			head = setName(head, name);
		return head;
	}

	public static ItemStack getFireWork(Firework fw) {
		ItemStack is = getNBT(Items.fireworks, fw.getNBTFirework());
		is.addEnchantment(Enchantment.unbreaking, 10);
		System.out.println(is.getTagCompound().toString());
		return is;
	}

	public static ItemStack getGive(String code) throws NumberInvalidException {
		ItemStack itemstack = null;
		String[] args = code.split(" ");
		ResourceLocation resourcelocation = new ResourceLocation(args[0]);
		Item itema = (Item) Item.itemRegistry.getObject(resourcelocation);
		Item item = null;
		if (itema == null) {
			System.out.println("Bad item id : " + args[0]);
		} else {
			item = itema;
			int i = args.length >= 2 ? CommandBase.parseInt(args[1]) : 1;
			int j = args.length >= 3 ? CommandBase.parseInt(args[2]) : 0;
			itemstack = new ItemStack(item, i, j);

			if (args.length >= 4) {
				String arg5 = args[3];
				if (args.length > 4) {
					for (int i1 = 4; i1 < args.length; i1++) {
						arg5 = arg5 + " " + args[i1];
					}
				}
				String arg5b = arg5.replaceAll("&&", "\u00a7") + "";
				itemstack = getNBT(itemstack, arg5b);
			}
		}
		return itemstack;
	}

	public static ItemStack getHead(String name) {
		return getHead(name, "");
	}

	public static ItemStack getHead(String name, String addtext) {
		ItemStack is = new ItemStack(Items.skull);
		is.setItemDamage(3);
		if (is.getTagCompound() == null)
			is.setTagCompound(new NBTTagCompound());
		if (is.getTagCompound() != null)
			is.getTagCompound().setString("SkullOwner", name);
		String str = I18n.format("act.head");

		str = str.replaceFirst("PLAYERNAME", name);
		str = str.replaceFirst("ADDTEXT", addtext);
		is.setStackDisplayName(str);
		return is;
	}

	public static ArrayList<ColoredTextInformation> getInfoItem(ItemStack is, Minecraft mc) {
		return getInfoItem(is, mc, true, true, true, true);
	}
	public static ArrayList<ColoredTextInformation> getInfoItem(ItemStack is, Minecraft mc, boolean showName,boolean ShowEnchant,
			boolean showDurability, boolean showCount) {
		ArrayList<ColoredTextInformation> lst = new ArrayList<ItemHelp.ColoredTextInformation>();
		if(showName || showCount) {
			String a = "(" + getCount(mc, is.getItem()) + ")";
			if(getCount(mc, is.getItem()) < 2)
				a = "";
			if(is.getItem() == Items.bow && getCount(mc, Items.arrow) != 0) {
				a = " (" + I18n.format(Items.arrow.getUnlocalizedName() + ".name") + ": " + getCount(mc, Items.arrow) + ")";
				if (getCount(mc, Items.arrow) > 0 && HasInfinity(is))
					a = Chat.AQUA + " (" + I18n.format("enchantment.arrowInfinite") + Chat.AQUA + ")";
			}
			String t = "";
			if(showName)t=is.getDisplayName();
			if(showCount)t+=Chat.AQUA+a;
			lst.add(new ColoredTextInformation(t,
					ModMain.getTextColor().getRGB()));
		}
		if(!GuiUtils.getEnch(is).isEmpty() && ShowEnchant) {
			lst.add(new ColoredTextInformation(GuiUtils.getEnch(is), Colors.GOLD));
		}
		if(showDurability) {
			if(isUnbreakable(is)) {
				lst.add(new ColoredTextInformation(I18n.format("item.unbreakable"), Colors.BLUE));
			} else if (is.getMaxDamage() != 0) {
				int dmg = Math.abs(is.getMetadata() - is.getMaxDamage() - 1);
				int maxdmg = is.getMaxDamage() + 1;
				int Color = Colors.DARK_GREEN;
				if (dmg < (int) ((float) (1.0 * maxdmg)))
					Color = Colors.DARK_GREEN;
				if (dmg < (int) ((float) (0.75 * maxdmg)))
					Color = Colors.GreenYellow;
				if (dmg < (int) ((float) (0.50 * maxdmg)))
					Color = Colors.GOLD;
				if (dmg < (int) ((float) (0.25 * maxdmg)))
					Color = Colors.RED;
				if (dmg < (int) ((float) (0.10 * maxdmg)))
					Color = Colors.DARK_RED;
				int dmgsColor = Color;
				lst.add(new ColoredTextInformation(dmg + "  / " + maxdmg, dmgsColor));
			}
		}
		return lst;
	}

	public static ItemStack getNBT(Item item, String nbt) {
		ItemStack is = new ItemStack(item);
		return getNBT(is, nbt);
	}

	public static ItemStack getNBT(ItemStack is, String nbt) {
		try {
			is.setTagCompound(JsonToNBT.getTagFromJson(nbt));
		} catch (NBTException e) {
			e.printStackTrace();
		}
		return is;
	}
	public static String getNBTCode(ItemStack is) {
		String nbt = "";
		if (is.getTagCompound() != null)
			nbt = is.getTagCompound().toString();
		return ((ResourceLocation) Item.itemRegistry.getNameForObject(is.getItem())).toString() + " "
				+ is.stackSize + " " + is.getMetadata() + " " + nbt;
	}
	public static boolean HasInfinity(ItemStack bow) {
		if (bow.getEnchantmentTagList() != null) {
			for (int i = 0; i < bow.getEnchantmentTagList().tagCount(); i++) {
				NBTBase n = bow.getEnchantmentTagList().get(i);
				if (n instanceof NBTTagCompound) {
					NBTTagCompound n1 = (NBTTagCompound) n;
					if (n1.hasKey("id") && n1.getInteger("id") == 51)
						return true;
				}
			}
		}
		return false;
	}

	public static boolean isUnbreakable(ItemStack is) {
		if (is.getTagCompound() != null) {
			if (is.getTagCompound().getInteger("Unbreakable") == 1)
				return true;
		}
		return false;
	}

	public static ItemStack setHyperProtectionEnchant(ItemStack itst, String name, boolean Thorns, boolean cheat) {
		int lv = 10;
		if (cheat) {
			lv = maxXp;
		} else {
			lv = 10;
		}
		itst.setStackDisplayName(name);
		itst.addEnchantment(Enchantment.unbreaking, lv);
		itst.addEnchantment(Enchantment.protection, lv);
		itst.addEnchantment(Enchantment.projectileProtection, lv);
		itst.addEnchantment(Enchantment.featherFalling, lv);
		itst.addEnchantment(Enchantment.fireProtection, lv);
		itst.addEnchantment(Enchantment.respiration, lv);
		itst.addEnchantment(Enchantment.aquaAffinity, 1);
		itst.addEnchantment(Enchantment.blastProtection, lv);
		itst.addEnchantment(Enchantment.depthStrider, lv);
		if (Thorns) {
			itst.addEnchantment(Enchantment.thorns, 42);
		}
		return itst;
	}

	public static ItemStack setMaxEnchant(ItemStack itst, String name) {
		itst.addEnchantment(Enchantment.fireAspect, maxXp);
		itst.addEnchantment(Enchantment.aquaAffinity, maxXp);
		itst.addEnchantment(Enchantment.baneOfArthropods, maxXp);
		itst.addEnchantment(Enchantment.blastProtection, maxXp);
		itst.addEnchantment(Enchantment.depthStrider, maxXp);
		itst.addEnchantment(Enchantment.efficiency, maxXp);
		itst.addEnchantment(Enchantment.featherFalling, maxXp);
		itst.addEnchantment(Enchantment.fireProtection, maxXp);
		itst.addEnchantment(Enchantment.flame, maxXp);
		itst.addEnchantment(Enchantment.sharpness, maxXp);
		itst.addEnchantment(Enchantment.silkTouch, maxXp);
		itst.addEnchantment(Enchantment.smite, maxXp);
		itst.addEnchantment(Enchantment.fortune, maxXp);
		itst.addEnchantment(Enchantment.infinity, maxXp);
		itst.addEnchantment(Enchantment.knockback, maxXp);
		itst.addEnchantment(Enchantment.looting, maxXp);
		itst.addEnchantment(Enchantment.luckOfTheSea, maxXp);
		itst.addEnchantment(Enchantment.lure, maxXp);
		itst.addEnchantment(Enchantment.power, maxXp);
		itst.addEnchantment(Enchantment.thorns, maxXp);
		itst.addEnchantment(Enchantment.projectileProtection, maxXp);
		itst.addEnchantment(Enchantment.unbreaking, maxXp);
		itst.addEnchantment(Enchantment.respiration, maxXp);
		itst.addEnchantment(Enchantment.punch, maxXp);
		itst.addEnchantment(Enchantment.protection, maxXp);
		itst.setStackDisplayName(name);

		return itst;
	}

	public static ItemStack setMeta(ItemStack is, int meta) {
		return setMeta(is, meta, null);
	}

	public static ItemStack setMeta(ItemStack is, int meta, String name) {
		is.setItemDamage(meta);
		if (name != null) {
			if (name.isEmpty())
				name = " ";
			is.setStackDisplayName(name);
		}
		return is;
	}

	public static ItemStack setName(ItemStack is, String name) {
		if (name.isEmpty())
			name = " ";
		is.setStackDisplayName(name);
		return is;
	}

	public static ItemStack setPushUpEnchant(ItemStack itst, String name) {
		itst = getNBT(itst, "{display:{Name:\"" + name + "\",Lore:[\"" + I18n.format("act.minigame.pu.kopeople")
				+ "\"]},AttributeModifiers:[{AttributeName:\"generic.attackDamage\",Name:\"generic.attackDamage\",Amount:0,Operation:1,UUIDLeast:8000,UUIDMost:4000},{AttributeName:\"generic.movementSpeed\",Name:\"generic.movementSpeed\",Amount:0.5,Operation:1,UUIDLeast:8000,UUIDMost:4000}]}");
		itst.addEnchantment(Enchantment.unbreaking, 10);
		itst.addEnchantment(Enchantment.punch, 5);
		itst.addEnchantment(Enchantment.knockback, 5);
		itst.addEnchantment(Enchantment.infinity, 1);
		return itst;
	}

	private ArrayList<AttributeModifier> modifiers = new ArrayList();
	public static ItemStack setCount(ItemStack is, int i) {
		is.stackSize=i;
		return is;
	}
}
