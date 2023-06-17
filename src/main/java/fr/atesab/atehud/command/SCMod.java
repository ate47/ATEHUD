package fr.atesab.atehud.command;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.Mod;
import fr.atesab.atehud.Mod.ShowGuiMod;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.utils.CommandUtils;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class SCMod extends SubCommand {

	private final ArrayList<String> aliases;
	public SCMod() {
		super("mod", I18n.format("cmd.mod", ModMain.MOD_NAME), CommandClickOption.doCommand, true);
		aliases = new ArrayList<String>();
		aliases.add(getName());
	}
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		List<String> lst = new ArrayList<String>();
		for (Mod mod: Mod.getMods()) {
			if(mod.autoGen)
				lst.add(mod.getId());
		}
		return CommandUtils.getTabCompletion(lst, args);
	}
	public ArrayList<String> getAlias() {
		return this.aliases;
	}
	public String getSubCommandUsage(ICommandSender sender) {
		return getName()+" <"+I18n.format("cmd.mod.name")+">";
	}

	@Override
	public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
			throws CommandException {
		if(args.length==1) {
			Mod mod = Mod.getModById(args[0]);
			if(mod!=null) {
				Minecraft mc = Minecraft.getMinecraft();
				if(mod.requireCreative && !mc.thePlayer.capabilities.isCreativeMode && !ModMain.byPassAC)
					Chat.noCheat(I18n.format("gui.act.nocreative"));
				else if(!mod.isEnabled()) {
						if(mod instanceof ShowGuiMod) try {
							GuiUtils.OpenGuiScreen(
									(GuiScreen) ((ShowGuiMod)mod).toLoad.getDeclaredConstructor(GuiScreen.class).newInstance(mc.currentScreen), 2);
							} catch (Exception e) {
								Chat.error(e.getMessage());
							}
						else mod.onEnabled();
				} else mod.onDisabled();
			} else {
				Chat.error(I18n.format("cmd.mod.unknow") + " : " + args[0]);
			}
		} else if (args.length==0) {
			IChatComponent component = new ChatComponentText(I18n.format("cmd.mod.list")+" : \n")
					.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
			boolean a = false;
			for (Mod mod: Mod.getMods()) {
				if(!mod.autoGen)continue;
				if(a)component.appendSibling(new ChatComponentText(", ").setChatStyle(new ChatStyle()
						.setColor(EnumChatFormatting.WHITE)
						.setChatClickEvent(null).setChatHoverEvent(null))); else a=true;
				component.appendSibling(new ChatComponentText(mod.getId()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_GREEN)
						.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + mainCommand.getCommandName() + " "+getName()+" "+mod.getId()))
						.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(mod.getName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN))))));
			}
			Chat.send(component.appendSibling(new ChatComponentText(".").setChatStyle(new ChatStyle()
					.setColor(EnumChatFormatting.WHITE)
					.setChatClickEvent(null).setChatHoverEvent(null))));
		} else {
			Chat.error("/" + mainCommand.getCommandName() + " "+getSubCommandUsage(sender));
		}
	}

}
