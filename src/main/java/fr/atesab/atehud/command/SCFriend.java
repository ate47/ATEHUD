package fr.atesab.atehud.command;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.command.SubCommand.CommandClickOption;
import fr.atesab.atehud.gui.GuiMenu;
import fr.atesab.atehud.gui.config.GuiTextListConfig;
import fr.atesab.atehud.utils.CommandUtils;
import fr.atesab.atehud.utils.GuiUtils;
import fr.atesab.atehud.utils.MultiChatComponentText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.config.Configuration;

public class SCFriend extends SCMainCommand {
	public static SubCommand add;
	public static SubCommand del;
	public static SubCommand list;
	public static SubCommand openlist;
	
	public static ArrayList<SubCommand> getSubCommands() {
		ArrayList<SubCommand> SubCommands = new ArrayList<SubCommand>();
		SubCommands.add(add=new SubCommand("add", "", SubCommand.CommandClickOption.suggestCommand) {
			public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
				return CommandUtils.getTabCompletion(CommandUtils.getPlayerList(), args);
			}

			public ArrayList<String> getAlias() {
				ArrayList<String> list = new ArrayList<String>();
				list.add(getName());
				return list;
			}

			public String getSubCommandUsage(ICommandSender sender) {
				return getName() + " (" + I18n.format("cmd.username") + ")";
			}

			public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
					throws CommandException {
				if (args.length == 1) {
					ArrayList<String> fr = ModMain.getArray(ModMain.friend);
					if (!fr.contains(args[0])) {
						fr.add(args[0]);
						Chat.show(I18n.format("chatOption.friend.add", args[0]));
						ModMain.friend = fr.toArray(new String[fr.size()]);
						ModMain.configFile.get(Configuration.CATEGORY_GENERAL, "friend", ModMain.friend)
								.set(ModMain.friend);
						ModMain.configFile.save();
					} else
						Chat.error(I18n.format("chatOption.friend.add.error", args[0]));
				} else
					Chat.error("/" + mainCommand.getCommandName() + " " + getName() + " <" + I18n.format("cmd.username")
							+ ">");
			}
		});
		SubCommands.add(del=new SubCommand("del", "", SubCommand.CommandClickOption.suggestCommand) {
			public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
				return CommandUtils.getTabCompletion(ModMain.getArray(ModMain.friend), args);
			}

			public ArrayList<String> getAlias() {
				ArrayList<String> list = new ArrayList<String>();
				list.add(getName());
				return list;
			}

			public String getSubCommandUsage(ICommandSender sender) {
				return getName() + " (" + I18n.format("cmd.username") + ")";
			}

			public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
					throws CommandException {
				if (args.length == 1) {
					ArrayList<String> fr = ModMain.getArray(ModMain.friend);
					if (fr.contains(args[0])) {
						fr.remove(args[0]);
						Chat.show(I18n.format("chatOption.friend.remove", args[0]));
						ModMain.friend = fr.toArray(new String[fr.size()]);
						ModMain.configFile.get(Configuration.CATEGORY_GENERAL, "friend", ModMain.friend)
								.set(ModMain.friend);
						ModMain.configFile.save();
					} else
						Chat.error(I18n.format("chatOption.friend.remove.error", args[0]));
				} else
					Chat.error("/" + mainCommand.getCommandName() + " " + getName() + " <" + I18n.format("cmd.username")
							+ ">");
			}
		});
		SubCommands.add(list=new SubCommand("list", "", SubCommand.CommandClickOption.doCommand) {
			public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
				return new ArrayList<String>();
			}

			public ArrayList<String> getAlias() {
				ArrayList<String> list = new ArrayList<String>();
				list.add(getName());
				return list;
			}

			public String getSubCommandUsage(ICommandSender sender) {
				return getName() + " (" + I18n.format("cmd.username") + ")";
			}

			public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
					throws CommandException {
				if (ModMain.friend.length == 0) {
					Chat.error(I18n.format("chatOption.friend.noFriend"));
				} else {
					NetHandlerPlayClient nethandlerplayclient = Minecraft.getMinecraft().thePlayer.sendQueue;
					List list = ATEEventHandler.field_175252_a.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
					Chat.show(I18n.format("chatOption.friend.friendConnect") + " :");
					for (int i = 0; i < ModMain.friend.length; i++) {
						Chat.send(MultiChatComponentText.getFriendElements("", ModMain.friend[i], true));
					}
				}
				ArrayList<IChatComponent> chat = new ArrayList<IChatComponent>();
				chat.add(new ChatComponentText(I18n.format("chatOption.friend.cmd.addFriend")).setChatStyle(
						new ChatStyle().setColor(EnumChatFormatting.YELLOW).setChatClickEvent(new ClickEvent(
								ClickEvent.Action.SUGGEST_COMMAND, "/" + mainCommand.getCommandName() + " add "))));
				chat.add(new ChatComponentText(" | ")
						.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_GRAY)));
				chat.add(new ChatComponentText(I18n.format("chatOption.friend.cmd.openConfig")).setChatStyle(
						new ChatStyle().setColor(EnumChatFormatting.YELLOW).setChatClickEvent(new ClickEvent(
								ClickEvent.Action.RUN_COMMAND, "/" + mainCommand.getCommandName() + " openlist"))));
				Chat.send(new MultiChatComponentText(chat));
			}
		});
		SubCommands.add(openlist=new SCActionCommand("openlist",
				I18n.format(I18n.format("chatOption.friend.cmd.openConfig").replaceAll("::", " ")),
				SubCommand.CommandClickOption.doCommand) {
			public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
					throws CommandException {
				GuiUtils.OpenGuiScreen(
						new GuiTextListConfig(null, ModMain.friend, I18n.format("chatOption.friend.cmd.openConfig")) {
							public void onUpdateValue(String[] value) {
								ModMain.friend = value;
								ModMain.configFile.get(Configuration.CATEGORY_GENERAL, "friend", ModMain.friend)
										.set(ModMain.friend);
								ModMain.configFile.save();
							}
						}, 10);
			}
		});
		return SubCommands;
	}

	private final ArrayList<String> aliases;

	public SCFriend() {
		super(getSubCommands(), list.getName(), "friend",
				I18n.format(I18n.format("chatOption.friend.friendConnect").replaceAll("::", " ")),
				CommandClickOption.doCommand);
		aliases = new ArrayList<String>();
		aliases.add(getName());
	}

	@Override
	public ArrayList<String> getAlias() {
		return this.aliases;
	}

	@Override
	public String getSubCommandUsage(ICommandSender sender) {
		return getName();
	}
}
