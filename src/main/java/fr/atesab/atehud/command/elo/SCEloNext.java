package fr.atesab.atehud.command.elo;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.command.MainCommand;
import fr.atesab.atehud.command.SubCommand;
import fr.atesab.atehud.utils.EloUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class SCEloNext extends SubCommand {

	public SCEloNext() {
		super("next", I18n.format("cmd.elo.next"), CommandClickOption.suggestCommand);
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return new ArrayList<String>();
	}

	@Override
	public ArrayList<String> getAlias() {
		return new ArrayList<String>();
	}

	@Override
	public String getSubCommandUsage(ICommandSender sender) {
		return getName()+" ["+I18n.format("cmd.elo.arguments.eloPlayer.1")+"] ["+I18n.format("cmd.elo.arguments.eloPlayer.2")+"] ["+
				I18n.format("cmd.elo.arguments.play")+"] ["+I18n.format("cmd.elo.arguments.win")+"="+I18n.format("cmd.elo.arguments.win.yes")
						+"|"+I18n.format("cmd.elo.arguments.win.no")+"]";
	}

	@Override
	public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
			throws CommandException {
		if(args.length!=4) {
			sender.addChatMessage(new ChatComponentText(mainCommand.getCommandName()+" "+getSubCommandUsage(sender)).setChatStyle(
					new ChatStyle().setColor(EnumChatFormatting.RED)));
		} else {
			int eloPlayer1;
			int eloPlayer2;
			int parties;
			boolean win;
			try {
				eloPlayer1 = Integer.valueOf(args[0]);
			} catch (Exception e) {
				sender.addChatMessage(new ChatComponentText(I18n.format("cmd.NaN",args[0])).setChatStyle(
						new ChatStyle().setColor(EnumChatFormatting.RED)));
				return;
			}
			try {
				eloPlayer2 = Integer.valueOf(args[1]);
			} catch (Exception e) {
				sender.addChatMessage(new ChatComponentText(I18n.format("cmd.NaN",args[1])).setChatStyle(
						new ChatStyle().setColor(EnumChatFormatting.RED)));
				return;
			}
			try {
				parties = Integer.valueOf(args[2]);
				if(parties < 0) throw new Exception();
			} catch (Exception e) {
				sender.addChatMessage(new ChatComponentText(I18n.format("cmd.NaN",args[2])).setChatStyle(
						new ChatStyle().setColor(EnumChatFormatting.RED)));
				return;
			}
			List<String> yes = ModMain.getArray(new String[] {"yes","true", I18n.format("cmd.elo.arguments.win.true").toLowerCase(),
					I18n.format("cmd.elo.arguments.win.yes").toLowerCase()});
			List<String> no = ModMain.getArray(new String[] {"no","false", I18n.format("cmd.elo.arguments.win.false").toLowerCase(),
					I18n.format("cmd.elo.arguments.win.no").toLowerCase()});
			if(yes.contains(args[3].toLowerCase()) || no.contains(args[3].toLowerCase())) {
				win = yes.contains(args[3].toLowerCase());
			} else {
				sender.addChatMessage(new ChatComponentText(I18n.format("cmd.NaB",args[3])).setChatStyle(
						new ChatStyle().setColor(EnumChatFormatting.RED)));
				return;
			}
			try {
				double r = EloUtils.getNextElo(eloPlayer1, parties, eloPlayer2, win);
				sender.addChatMessage(Chat.getPrefix(I18n.format("cmd.elo"), EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW, true, 
						EnumChatFormatting.YELLOW, I18n.format("cmd.elo.next.message")+" = ")
						.appendSibling(new ChatComponentText(String.valueOf((int) r)).setChatStyle(
						new ChatStyle().setColor(EnumChatFormatting.GOLD))));
			} catch (Exception e) {}
			
		}
	}

}
