package fr.atesab.atehud.command.spammer;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.command.MainCommand;
import fr.atesab.atehud.command.SubCommand;
import fr.atesab.atehud.utils.CommandUtils;
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

public class SCSpammerSet extends SubCommand {

	public static abstract class Value {
		public abstract String getName();
		public abstract String getValue();
		public abstract void setValue(String s) throws Exception ;
		
	}
	private Value[] values = new Value[] {
			new Value()	{
				public String getName() {return "cmd";}
				public String getValue() {return ModMain.spamCommand;}
				public void setValue(String s) throws Exception {ModMain.spamCommand=s;}
			},
			new Value()	{
				public String getName() {return "showbutton";}
				public String getValue() {return String.valueOf(ModMain.showButton);}
				public void setValue(String s) throws Exception {ModMain.showButton=Boolean.valueOf(s);}
			},
			new Value()	{
				public String getName() {return "renderoverlay";}
				public String getValue() {return String.valueOf(ModMain.renderGameOverlay);}
				public void setValue(String s) throws Exception {ModMain.renderGameOverlay=Boolean.valueOf(s);}
			},
			new Value()	{
				public String getName() {return "stoponclose";}
				public String getValue() {return String.valueOf(ModMain.stopOnClose);}
				public void setValue(String s) throws Exception {ModMain.stopOnClose=Boolean.valueOf(s);}
			},
			new Value()	{
				public String getName() {return "action";}
				public String getValue() {return String.valueOf(ModMain.numberOfAction);}
				public void setValue(String s) throws Exception {ModMain.numberOfAction=Integer.valueOf(s);}
			},
			new Value()	{
				public String getName() {return "tick";}
				public String getValue() {return String.valueOf(ModMain.ticktowait);}
				public void setValue(String s) throws Exception {ModMain.ticktowait=Integer.valueOf(s);}
			}
		};
	private ArrayList<String> aliases = new ArrayList<String>();
	public SCSpammerSet() {
		super("set", I18n.format("cmd.spammer.set"), CommandClickOption.doCommand);
		aliases.add(getName());
	}
	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		if(args.length!=1)return new ArrayList<String>();
		List<String> tab = new ArrayList<String>();
		for (Value v: values) tab.add(v.getName());
		return CommandUtils.getTabCompletion(tab, args);
	}

	@Override
	public ArrayList<String> getAlias() {
		return this.aliases;
	}

	@Override
	public String getSubCommandUsage(ICommandSender sender) {
		return this.getName()+" <"+I18n.format("cmd.spammer.set.name")+"> <"+I18n.format("cmd.spammer.set.value")+">";
	}
	private Value getValueByName(String name) {
		for (Value v: values)
			if(v.getName().equalsIgnoreCase(name))return v;
		return null;
	}
	@Override
	public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
			throws CommandException {
		if(args.length==0) {
			IChatComponent msg = new ChatComponentText("-- "+I18n.format("cmd.spammer.set.list")+" --")
					.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
			for (Value v: values) {
				HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new ChatComponentText(I18n.format("cmd.help.do"))
						.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
				ClickEvent ce = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + mainCommand.getCommandName() + " " + getName()+" "+v.getName()+" ");
				msg.appendSibling(new ChatComponentText("\n- ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY).setChatClickEvent(ce).setChatHoverEvent(he)))
					.appendSibling(new ChatComponentText(v.getName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD).setChatClickEvent(ce).setChatHoverEvent(he)))
					.appendSibling(new ChatComponentText(" : ").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY).setChatClickEvent(ce).setChatHoverEvent(he)))
					.appendSibling(new ChatComponentText(I18n.format("cmd.spammer.set."+v.getName())).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE).setChatClickEvent(ce).setChatHoverEvent(he)));
			}
			sender.addChatMessage(msg.appendSibling(new ChatComponentText("\n/"+mainCommand.getCommandName()+" "+getSubCommandUsage(sender))
					.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)
							.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new ChatComponentText(I18n.format("cmd.help.do"))
							.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))))
							.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + mainCommand.getCommandName() + " " + getName()+" ")))));
		} else {
			Value v;
			if((v=getValueByName(args[0]))!=null) {
				if(args.length>1) {
					try {
						for (int i = 2; i < args.length; i++)
							args[1]+=" "+args[i];
						v.setValue(args[1]);
						sender.addChatMessage(new ChatComponentText(v.getName()+"="+v.getValue())
								.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
						ModMain.saveConfig();
					} catch (Exception e) {
						sender.addChatMessage(new ChatComponentText(I18n.format("cmd.spammer.set.error.exception"))
								.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
					}
				} else {
					sender.addChatMessage(new ChatComponentText(v.getName()+"="+v.getValue())
							.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
				}
			} else {
				sender.addChatMessage(new ChatComponentText(I18n.format("cmd.spammer.set.error", "/"+mainCommand.getCommandName()+" "+getName()))
						.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
			}
		}
	}

}
