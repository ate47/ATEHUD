package fr.atesab.atehud;

import java.util.Iterator;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ATECreativeTab extends CreativeTabs {
	private Item tabIconItem;
	private boolean searchBar;
	public ATECreativeTab(int par1, String par2Str, Item icon) {
		this(par1, par2Str, icon, false);
	}
	public ATECreativeTab(int par1, String par2Str, Item icon, boolean searchBar) {
		super(par1, par2Str);
		this.tabIconItem = icon;
		this.searchBar = searchBar;
	}
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return tabIconItem;
	}
	public boolean hasSearchBar() {
		return true;
	}
}