package fr.atesab.atehud.utils;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.FakeItems3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.ChatComponentText;

public class GiveItemUtils {
	public static boolean canGive(Minecraft mc) {
		for (int i = 0; i < 9; i++)
			if (mc.thePlayer.inventory.getStackInSlot(i) == null) {
				return true;
			}
		return false;
	}

	public static void give(Minecraft mc, ItemStack stack) {
		if (mc.thePlayer.capabilities.isCreativeMode) {
			for (int i = 0; i < 9; i++)
				if (mc.thePlayer.inventory.getStackInSlot(i) == null) {
					mc.thePlayer.sendQueue.addToSendQueue((Packet) new C10PacketCreativeInventoryAction(36 + i, stack));
					Chat.itemStack(stack);
					return;
				}
			Chat.error(I18n.format("gui.act.give.fail"));
		} else {
			Chat.error(I18n.format("gui.act.nocreative"));
		}
	}
}
