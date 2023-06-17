package fr.atesab.atehud.command.act;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.command.MainCommand;
import fr.atesab.atehud.command.SubCommand;
import fr.atesab.atehud.command.SubCommand.CommandClickOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.BlockPos;

public class SCACTRename extends SubCommand {
	private final ArrayList<String> aliases;

	public SCACTRename() {
		super("rename", I18n.format("").replaceAll("::", " "), CommandClickOption.suggestCommand);
		aliases = new ArrayList<String>();
		aliases.add(getName());
	}

	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return new ArrayList<String>();
	}

	public ArrayList<String> getAlias() {
		return this.aliases;
	}

	public String getSubCommandUsage(ICommandSender sender) {
		return getName() + " <name>";
	}

	public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
			throws CommandException {
		if (args.length < 1) {
			Chat.error(mainCommand.getCommandName() + " " + getSubCommandUsage(sender));
			return;
		}
		if (args.length > 1)
			args[0] = CommandBase.buildString(args, 0);
		Minecraft mc = Minecraft.getMinecraft();
		int current = mc.thePlayer.inventory.currentItem;
		ItemStack is = mc.thePlayer.inventory.getStackInSlot(current);
		if (is != null) {
			mc.thePlayer.sendQueue.addToSendQueue((Packet) new C10PacketCreativeInventoryAction(36 + current,
					is.setStackDisplayName(args[0].replaceAll("&", "\u00a7"))));
		} else {
			Chat.error(I18n.format("cmd.act.noitem"));
		}
	}
}
