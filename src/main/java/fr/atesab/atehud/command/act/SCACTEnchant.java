package fr.atesab.atehud.command.act;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.command.MainCommand;
import fr.atesab.atehud.command.SCMainCommand;
import fr.atesab.atehud.command.SubCommand;
import fr.atesab.atehud.command.SubCommand.CommandClickOption;
import fr.atesab.atehud.superclass.Enchantments;
import fr.atesab.atehud.utils.CommandUtils;
import fr.atesab.atehud.utils.MultiChatComponentText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class SCACTEnchant extends SCMainCommand {
	public static ArrayList<SubCommand> getSubCommands() {
		ArrayList<SubCommand> SubCommands = new ArrayList<SubCommand>();
		SubCommands.add(new SubCommand("list", I18n.format("cmd.act.enchant.list").replaceAll("::", " "),
				SubCommand.CommandClickOption.doCommand) {
			public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
				return new ArrayList<String>();
			}

			public ArrayList<String> getAlias() {
				ArrayList<String> list = new ArrayList<String>();
				list.add(getName());
				return list;
			}

			public String getSubCommandUsage(ICommandSender sender) {
				return getName();
			}

			public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
					throws CommandException {
				ArrayList<IChatComponent> elems = new ArrayList<IChatComponent>();
				elems.add(new ChatComponentText(" --- " + I18n.format("cmd.act.enchant.list") + " --- ")
						.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
				for (int i = 0; i < Enchantments.enchantments.length; i++) {
					Enchantments ename = Enchantments.enchantments[i];
					elems.add(new MultiChatComponentText(new IChatComponent[] {
							new ChatComponentText("[+] ").setChatStyle(new ChatStyle()
									.setColor(EnumChatFormatting.GREEN)
									.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
											new ChatComponentText(I18n.format("cmd.act.enchant.add"))
													.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))))
									.setChatClickEvent(new ClickEvent(Action.SUGGEST_COMMAND,
											"/" + mainCommand.getCommandName() + " add " + ename.getName() + " "))),
							new ChatComponentText("[-] ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)
									.setChatClickEvent(new ClickEvent(Action.RUN_COMMAND,
											"/" + mainCommand.getCommandName() + " del " + ename.getName()))
									.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
											new ChatComponentText(I18n.format("cmd.act.enchant.del")).setChatStyle(
													new ChatStyle().setColor(EnumChatFormatting.YELLOW))))),
							new ChatComponentText(
									"| " + StatCollector.translateToLocal(ename.getEnchantment().getName()) + " ")
											.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)),
							new ChatComponentText("(" + ename.getName().toLowerCase() + ")")
									.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)) }));
				}
				Chat.send(elems);
			}
		});
		SubCommands.add(new SubCommand("add", I18n.format("cmd.act.enchant.add").replaceAll("::", " "),
				SubCommand.CommandClickOption.suggestCommand) {
			public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
				return CommandUtils.getTabCompletion(CommandUtils.getEnchantList(), args);
			}

			public ArrayList<String> getAlias() {
				ArrayList<String> list = new ArrayList<String>();
				list.add(getName());
				return list;
			}

			public String getSubCommandUsage(ICommandSender sender) {
				return getName() + " (Enchant) (Level)";
			}

			public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
					throws CommandException {
				EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
				ItemStack is = player.inventory.getCurrentItem();
				int level;
				if (is == null) {
					Chat.error(I18n.format("cmd.act.noitem"));
					return;
				}
				if (args.length != 2) {
					Chat.error("/" + mainCommand.getCommandName() + " " + getSubCommandUsage(sender));
					return;
				}
				try {
					level = Integer.valueOf(args[1]);
				} catch (Exception e) {
					Chat.error(I18n.format("cmd.NaN", '\"' + args[1] + '\"'));
					return;
				}
				Enchantments enchant = Enchantments.getEnchantmentsByName(args[0]);
				if (enchant == null) {
					Chat.error(I18n.format("cmd.act.enchant.unknow", (args[0])));
					return;
				}
				if (is.getTagCompound() == null) {
					is.setTagCompound(new NBTTagCompound());
				}

				if (!is.getTagCompound().hasKey("ench", 9)) {
					is.getTagCompound().setTag("ench", new NBTTagList());
				}

				is.addEnchantment(enchant.getEnchantment(), level);
				player.sendQueue.addToSendQueue(
						(Packet) new C10PacketCreativeInventoryAction(36 + player.inventory.currentItem, is));
			}
		});
		SubCommands.add(new SubCommand("del", I18n.format("cmd.act.enchant.del").replaceAll("::", " "),
				SubCommand.CommandClickOption.suggestCommand) {
			public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
				return CommandUtils.getTabCompletion(CommandUtils.getInHandItemEnchantList(), args);
			}

			public ArrayList<String> getAlias() {
				ArrayList<String> list = new ArrayList<String>();
				list.add(getName());
				return list;
			}

			public String getSubCommandUsage(ICommandSender sender) {
				return getName() + " (Enchant)";
			}

			public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
					throws CommandException {
				EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
				ItemStack is = player.inventory.getCurrentItem();
				if (args.length != 1) {
					Chat.error("/" + mainCommand.getCommandName() + " " + getSubCommandUsage(sender));
					return;
				}
				ArrayList<String> Enchants = new ArrayList<String>();
				for (int i = 0; i < Enchantments.enchantments.length; i++) {
					for (int j = 0; j < is.getEnchantmentTagList().tagCount(); j++) {
						NBTTagCompound nbt = (NBTTagCompound) is.getEnchantmentTagList().get(j);
						if (nbt != null
								&& nbt.getString("id").equals(String.valueOf(Enchantments.enchantments[i].getId())))
							is.getEnchantmentTagList().removeTag(j);
					}
				}
				player.sendQueue.addToSendQueue(
						(Packet) new C10PacketCreativeInventoryAction(36 + player.inventory.currentItem, is));
			}
		});
		return SubCommands;
	}

	private final ArrayList<String> aliases;

	public SCACTEnchant() {
		super(getSubCommands(), "list", "enchant", I18n.format("cmd.act.enchant").replaceAll("::", " "),
				CommandClickOption.doCommand);
		aliases = new ArrayList<String>();
		aliases.add(getName());
	}

	public ArrayList<String> getAlias() {
		return this.aliases;
	}

	public String getSubCommandUsage(ICommandSender sender) {
		return getName() + " <list|add|del> (enchant) (level)";
	}

}
