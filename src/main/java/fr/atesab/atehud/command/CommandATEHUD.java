package fr.atesab.atehud.command;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.bridge.BridgeMessage;
import fr.atesab.atehud.command.SubCommand.CommandClickOption;
import fr.atesab.atehud.command.act.SCACTCommand;
import fr.atesab.atehud.command.elo.SCElo;
import fr.atesab.atehud.command.spammer.SCSpammerCommand;
import fr.atesab.atehud.gui.GuiMenu;
import fr.atesab.atehud.gui.config.GuiATEHUDConfig;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class CommandATEHUD extends MainCommand {
	private final List aliases;
	public SCACTCommand act;
	public SCSpammerCommand spam;
	public SCFriend friend;
	public SCElo elo;
	public SCMod mod;
	public SCHelp help;
	public SCActionCommand opengui;
	public SCActionCommand config;
	public SCSay say;
	public SCDisplayOptionsList optionslist;
	public SCDisplay display;
	public SCDisplayRaw displayraw;
	public SCTeamspeak ts;
	public SCTeleport tp;
	public SCTeleportMass tpmass;
	public SCTeleportRandom tpr;
	public SCList list;
	public SCInfo info;
	public SCOpenChat openchat;
	public CommandATEHUD(String defaultCommand) {
		super(new ArrayList<SubCommand>(), defaultCommand);
		subCommands.add(act = new SCACTCommand("help"));
		subCommands.add(spam = new SCSpammerCommand("help"));
		subCommands.add(friend = new SCFriend());
		subCommands.add(elo = new SCElo());
		subCommands.add(mod = new SCMod());
		subCommands.add(openchat = new SCOpenChat());
		subCommands.add(help = new SCHelp(ModMain.MOD_LITTLE_NAME + " v" + ModMain.MOD_VERSION, 10));
		subCommands.add(opengui = new SCActionCommand("opengui",
				I18n.format(I18n.format("key.atehud.guifactory").replaceAll("::", " ")),
				SubCommand.CommandClickOption.doCommand) {
			public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
					throws CommandException {
				GuiUtils.OpenGuiScreen(new GuiMenu(null), 10);
			}
		});
		subCommands.add(config = 
				new SCActionCommand("config", I18n.format(I18n.format("mod.config.desc").replaceAll("::", " ")),
						SubCommand.CommandClickOption.doCommand) {
					public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
							throws CommandException {
						GuiUtils.OpenGuiScreen(new GuiATEHUDConfig(null), 10);
					}
				});
		subCommands.add(say = new SCSay());
		subCommands.add(optionslist = new SCDisplayOptionsList());
		subCommands.add(display = new SCDisplay());
		subCommands.add(displayraw = new SCDisplayRaw());
		subCommands.add(ts = new SCTeamspeak());
		subCommands.add(tp = new SCTeleport());
		subCommands.add(tpmass = new SCTeleportMass());
		subCommands.add(tpr = new SCTeleportRandom());
		subCommands.add(list = new SCList("/atehud info"));
		subCommands.add(info = new SCInfo());
		subCommands.add(new SCActionCommand("atianbridge", "test atian bridge", CommandClickOption.suggestCommand) {
			@Override
			public String getSubCommandUsage(ICommandSender sender) {
				return getName() + "(name) (arg)*";
			}
			@Override
			public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
					throws CommandException {
				if(!ModMain.AdvancedModActived) {
					sender.addChatMessage(new ChatComponentText("AdvMod disabled").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
					return;
				} else if(args.length==0) {
					sender.addChatMessage(new ChatComponentText("No Arguments").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
					return;
				}
				for (int i = 2; i < args.length; i++) args[1] = args[i];
				ModMain.network.sendToServer(new BridgeMessage(args[0], args[1]));
				sender.addChatMessage(new ChatComponentText("Command sended.").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
			}
			
		});
		aliases = new ArrayList();
		aliases.add("atehud");
		aliases.add("ahd");
	}

	@Override
	public List<String> getCommandAliases() {
		return this.aliases;
	}

	@Override
	public String getCommandName() {
		return "atehud";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return I18n.format("cmd.ah.use");
	}

}
