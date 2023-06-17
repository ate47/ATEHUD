package fr.atesab.atehud;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import fr.atesab.atehud.utils.ItemHelp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.PlayerSelector;
import net.minecraft.command.WrongUsageException;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

public class FakeItems3 extends Item {

	public FakeItems3() {
		setCreativeTab(ModMain.ATEcreativeTAB3);
		setHasSubtypes(true);
	}

	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		if (ModMain.genFake3) {
			for (int i = 0; i < ModMain.HeadNames.length; i++)
				subItems.add(ItemHelp.getHead(ModMain.HeadNames[i]));
			for (int i = 0; i < ModMain.CustomCommandBlock.length; i++) {
				String cmd = ModMain.CustomCommandBlock[i].replaceFirst("\"", "\\\"");
				cmd = ModMain.CustomCommandBlock[i].replaceAll("&&", "\u00a7") + "";
				subItems.add(ItemHelp.getCMD(cmd, I18n.format("act.cstcommand") + i));
			}
			for (int i = 0; i < ModMain.CustomFirework.length; i++) {
				String nbt = ModMain.CustomFirework[i].replaceAll("&&", "\u00a7") + "";
				subItems.add(
						ItemHelp.setName(ItemHelp.getNBT(Items.fireworks, nbt), I18n.format("act.cstfirework") + i));
			}
			for (int i = 0; i < ModMain.AdvancedItem.length; i++)
				try {
					if (ItemHelp.getGive(ModMain.AdvancedItem[i]) == null) {
					} else {
						subItems.add(ItemHelp.getGive(ModMain.AdvancedItem[i]));
					}
				} catch (NumberInvalidException e) {}

		} else subItems.add(ItemHelp.noGen);
	}
}
