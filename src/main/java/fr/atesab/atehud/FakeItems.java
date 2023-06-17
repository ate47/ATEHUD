package fr.atesab.atehud;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Multimap;

import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.superclass.Explosion;
import fr.atesab.atehud.superclass.Firework;
import fr.atesab.atehud.utils.ItemHelp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiConfigEntries.ChatColorEntry;

public class FakeItems extends Item {
	public FakeItems() {
		setCreativeTab(ModMain.ATEcreativeTAB);
		setHasSubtypes(true);
	}

	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		if (ModMain.genFake1) {
			subItems = ItemHelp.addTitle(subItems, I18n.format("act.urealobjet"));

			subItems.add(new ItemStack(Blocks.barrier)); // 1
			subItems.add(new ItemStack(Blocks.red_mushroom_block)); // 2
			subItems.add(new ItemStack(Blocks.brown_mushroom_block)); // 3
			subItems.add(new ItemStack(Blocks.command_block)); // 4
			subItems.add(new ItemStack(Blocks.dragon_egg)); // 5
			subItems.add(new ItemStack(Blocks.mob_spawner)); // 6
			subItems.add(new ItemStack(Blocks.farmland)); // 7
			subItems.add(new ItemStack(Blocks.lit_furnace)); // 8
			subItems.add(new ItemStack(Items.enchanted_book)); // 9
			subItems.add(ItemHelp.setMeta(new ItemStack(Items.spawn_egg), 0));// 1
			subItems.add(new ItemStack(Items.command_block_minecart));
			subItems.add(ItemHelp.getCMD(
					"/tellraw @a \"\u00a7f<\u00a74SuperCommandBlock\u00a7f> \u00a74H\u00a73e\u00a7dl\u00a72l\u00a7eo \u00a7fW\u00a7ao\u00a76r\u00a7bl\u00a79d\"",
					"\u00a74H\u00a73e\u00a7dl\u00a72l\u00a7eo \u00a7fW\u00a7ao\u00a76r\u00a7bl\u00a79d \u00a7b(Solo Only)")); // 2
			// LE PUTAIN DE BOUQUIN DES FAMILLES
			subItems.add(ItemHelp.getNBT(Items.written_book, ItemHelp.book_nbt)); // 3

			subItems = ItemHelp.addBarrier(subItems, 5);
			subItems = ItemHelp.addTitle(subItems, I18n.format("act.cheatobjet"));

			subItems.add(ItemHelp.setMaxEnchant(new ItemStack(Items.blaze_rod), I18n.format("act.cheat.wand")));
			subItems.add(ItemHelp.setMaxEnchant(ItemHelp.getNBT(Items.diamond_sword,
					"{Unbreakable:1,AttributeModifiers:[{AttributeName:\"generic.attackDamage\",Name:\"generic.attackDamage\",Amount:2000000,Operation:200,UUIDLeast:8000,UUIDMost:4000}]}"),
					I18n.format("act.cheat.sword")));
			subItems.add(ItemHelp.setMaxEnchant(new ItemStack(Items.bow), I18n.format("act.cheat.bow")));
			subItems.add(ItemHelp.setMaxEnchant(new ItemStack(Items.enchanted_book), I18n.format("act.cheat.book")));
			subItems.add(
					ItemHelp.setMaxEnchant(new ItemStack(Items.diamond_pickaxe), I18n.format("act.cheat.pickaxe")));
			subItems.add(ItemHelp.setHyperProtectionEnchant(ItemHelp.getNBT(Items.diamond_boots,
					"{Unbreakable:1,AttributeModifiers:[{AttributeName:\"generic.attackDamage\",Name:\"generic.attackDamage\",Amount:2000,Operation:20,UUIDLeast:8000,UUIDMost:4000},"
							+ "{AttributeName:\"generic.maxHealth\",Name:\"generic.maxHealth\",Amount:2000,Operation:20,UUIDLeast:8000,UUIDMost:4000},"
							+ "{AttributeName:\"generic.movementSpeed\",Name:\"generic.movementSpeed\",Amount:2,Operation:1,UUIDLeast:8000,UUIDMost:4000},"
							+ "{AttributeName:\"generic.knockbackResistance\",Name:\"generic.knockbackResistance\",Amount:1,Operation:1,UUIDLeast:8000,UUIDMost:4000}]}"),
					I18n.format("act.cheat.boots"), true, true));
			subItems.add(ItemHelp.setHyperProtectionEnchant(ItemHelp.getNBT(Items.diamond_leggings,
					"{Unbreakable:1,AttributeModifiers:[{AttributeName:\"generic.attackDamage\",Name:\"generic.attackDamage\",Amount:2000,Operation:20,UUIDLeast:8000,UUIDMost:4000},"
							+ "{AttributeName:\"generic.maxHealth\",Name:\"generic.maxHealth\",Amount:2000,Operation:20,UUIDLeast:8000,UUIDMost:4000},"
							+ "{AttributeName:\"generic.movementSpeed\",Name:\"generic.movementSpeed\",Amount:2,Operation:1,UUIDLeast:8000,UUIDMost:4000},"
							+ "{AttributeName:\"generic.knockbackResistance\",Name:\"generic.knockbackResistance\",Amount:1,Operation:1,UUIDLeast:8000,UUIDMost:4000}]}"),
					I18n.format("act.cheat.leggins"), true, true));
			subItems.add(ItemHelp.setHyperProtectionEnchant(ItemHelp.getNBT(Items.diamond_chestplate,
					"{Unbreakable:1,AttributeModifiers:[{AttributeName:\"generic.attackDamage\",Name:\"generic.attackDamage\",Amount:2000,Operation:20,UUIDLeast:8000,UUIDMost:4000},"
							+ "{AttributeName:\"generic.maxHealth\",Name:\"generic.maxHealth\",Amount:2000,Operation:20,UUIDLeast:8000,UUIDMost:4000},"
							+ "{AttributeName:\"generic.movementSpeed\",Name:\"generic.movementSpeed\",Amount:2,Operation:1,UUIDLeast:8000,UUIDMost:4000},"
							+ "{AttributeName:\"generic.knockbackResistance\",Name:\"generic.knockbackResistance\",Amount:1,Operation:1,UUIDLeast:8000,UUIDMost:4000}]}"),
					I18n.format("act.cheat.chestplate"), true, true));
			subItems.add(ItemHelp.setHyperProtectionEnchant(ItemHelp.getNBT(Items.diamond_helmet,
					"{Unbreakable:1,AttributeModifiers:[{AttributeName:\"generic.attackDamage\",Name:\"generic.attackDamage\",Amount:2000,Operation:20,UUIDLeast:8000,UUIDMost:4000},"
							+ "{AttributeName:\"generic.maxHealth\",Name:\"generic.maxHealth\",Amount:2000,Operation:20,UUIDLeast:8000,UUIDMost:4000},"
							+ "{AttributeName:\"generic.movementSpeed\",Name:\"generic.movementSpeed\",Amount:2,Operation:1,UUIDLeast:8000,UUIDMost:4000},"
							+ "{AttributeName:\"generic.knockbackResistance\",Name:\"generic.knockbackResistance\",Amount:1,Operation:1,UUIDLeast:8000,UUIDMost:4000}]}"),
					I18n.format("act.cheat.helmet"), true, true));

			subItems = ItemHelp.addTitle(subItems, I18n.format("act.hyperprotectobjet"));

			subItems.add(ItemHelp.setHyperProtectionEnchant(new ItemStack(Items.diamond_boots),
					I18n.format("act.hyper.boots"), false, false));
			subItems.add(ItemHelp.setHyperProtectionEnchant(new ItemStack(Items.diamond_leggings),
					I18n.format("act.hyper.leggins"), false, false));
			subItems.add(ItemHelp.setHyperProtectionEnchant(new ItemStack(Items.diamond_chestplate),
					I18n.format("act.hyper.chestplate"), false, false));
			subItems.add(ItemHelp.setHyperProtectionEnchant(new ItemStack(Items.diamond_helmet),
					I18n.format("act.hyper.helmet"), false, false));
			subItems.add(ItemHelp.setMeta(new ItemStack(Blocks.stained_glass_pane), 3, " ")); // 5
			subItems.add(ItemHelp.setHyperProtectionEnchant(new ItemStack(Items.chainmail_boots),
					I18n.format("act.hyperthorns.boots"), true, false));
			subItems.add(ItemHelp.setHyperProtectionEnchant(new ItemStack(Items.chainmail_leggings),
					I18n.format("act.hyperthorns.leggins"), true, false));
			subItems.add(ItemHelp.setHyperProtectionEnchant(new ItemStack(Items.chainmail_chestplate),
					I18n.format("act.hyperthorns.chestplate"), true, false));
			subItems.add(ItemHelp.setHyperProtectionEnchant(new ItemStack(Items.chainmail_helmet),
					I18n.format("act.hyperthorns.helmet"), true, false));

			subItems = ItemHelp.addTitle(subItems, I18n.format("act.minigameobjet"));

			subItems.add(
					ItemHelp.setPushUpEnchant(new ItemStack(Items.diamond_hoe), I18n.format("act.minigame.pu.hoe")));
			subItems.add(ItemHelp.setPushUpEnchant(new ItemStack(Items.stick), I18n.format("act.minigame.pu.stick")));
			subItems.add(ItemHelp.setPushUpEnchant(new ItemStack(Items.bow), I18n.format("act.minigame.pu.bow")));
			subItems.add(ItemHelp.setName(new ItemStack(Items.arrow), I18n.format("act.minigame.pu.arrow")));

			subItems = ItemHelp.addBarrier(subItems, 5);
			subItems = ItemHelp.addTitle(subItems, I18n.format("act.fireworks"));

			subItems.add(ItemHelp.setName(new Firework(
					new Explosion[] {
							new Explosion(1, 1, 2, new int[] { Explosion.getColorWithRGB(255, 0, 0) }, new int[] {}),
							new Explosion(1, 1, 1, new int[] { Explosion.getColorWithRGB(170, 0, 0) }, new int[] {}),
							new Explosion(0, 0, 4, new int[] { Explosion.getColorWithRGB(255, 85, 255) }, new int[] {}),
							new Explosion(1, 1, 0, new int[] { Explosion.getColorWithRGB(255, 255, 85) }, new int[] {}),
							new Explosion(1, 0, 0, new int[] { Explosion.getColorWithRGB(170, 0, 170) }, new int[] {}),
							new Explosion(1, 1, 1, new int[] { Explosion.getColorWithRGB(85, 255, 255) }, new int[] {}),
							new Explosion(0, 1, 2, new int[] { Explosion.getColorWithRGB(255, 70, 0) }, new int[] {}) },
					1).getItemStack(), Chat.GOLD+"Orange life"));
			subItems.add(ItemHelp.setName(new Firework(new Explosion[] {
					new Explosion(1, 1, 2, new int[] { Explosion.getColorWithRGB(255, 0, 0) }, new int[] {}),
					new Explosion(1, 1, 1, new int[] { Explosion.getColorWithRGB(255, 255, 0) }, new int[] {}),
					new Explosion(0, 1, 1, new int[] { Explosion.getColorWithRGB(255, 0, 255) }, new int[] {}),
					new Explosion(1, 1, 0, new int[] { Explosion.getColorWithRGB(0, 255, 255) }, new int[] {}),
					new Explosion(1, 0, 0, new int[] { Explosion.getColorWithRGB(255, 255, 128) }, new int[] {}),
					new Explosion(1, 1, 1, new int[] { Explosion.getColorWithRGB(255, 0, 128) }, new int[] {}),
					new Explosion(0, 1, 2, new int[] { Explosion.getColorWithRGB(128, 0, 255) }, new int[] {}),
					new Explosion(1, 1, 1, new int[] { Explosion.getColorWithRGB(255, 128, 0) }, new int[] {}) }, -20)
							.getItemStack(),
					"\u00a7dMulticolors boom"));

			subItems.add(
					ItemHelp.setName(
							new Firework(
									new Explosion[] { new Explosion(1, 1, 2, new int[] { Colors.RED }, new int[] {}),
											new Explosion(1, 1, 4, new int[] { Colors.GOLD }, new int[] {}),
											new Explosion(0, 1, 1, new int[] { Colors.DARK_RED }, new int[] {}),
											new Explosion(1, 1, 0, new int[] { Colors.YELLOW }, new int[] {}),
											new Explosion(1, 0, 0, new int[] { Colors.WHITE }, new int[] {}),
											new Explosion(1, 1, 3, new int[] { Colors.LIGHT_PURPLE }, new int[] {}) },
									1).getItemStack(),
							"\u00a76Go\u00a7cld\u00a76en\u00a7c Re\u00a76d"));
			subItems.add(ItemHelp.setName(
					new Firework(new Explosion[] { new Explosion(1, 1, 2, new int[] { Colors.azure1 }, new int[] {}),
							new Explosion(1, 1, 4, new int[] { Colors.aquamarine4 }, new int[] {}),
							new Explosion(0, 1, 1, new int[] { Colors.blue1 }, new int[] {}),
							new Explosion(1, 0, 0, new int[] { Colors.BLUE }, new int[] {}),
							new Explosion(1, 0, 0, new int[] { Colors.DarkSeaGreen1 }, new int[] {}),
							new Explosion(0, 1, 4, new int[] { Colors.DARK_BLUE }, new int[] {}),
							new Explosion(1, 1, 2, new int[] { Colors.DARK_AQUA }, new int[] {}) }, 1).getItemStack(),
					"\u00a79Blue Fire"));
			subItems.add(ItemHelp.setName(new Firework().getItemStack(), "\u00a7fB\u00a77asic"));
		} else {
			subItems.add(ItemHelp.noGen);
		}
	}

}
