package fr.atesab.atehud.command.elo;

import java.util.ArrayList;
import java.util.List;

import fr.atesab.atehud.Chat;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.command.MainCommand;
import fr.atesab.atehud.command.SubCommand;
import fr.atesab.atehud.utils.CommandUtils;
import fr.atesab.atehud.utils.EloUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class SCEloWin extends SubCommand {

	public SCEloWin() {
		super("win", I18n.format("cmd.elo.win"), CommandClickOption.suggestCommand);
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
		return getName()+" ["+I18n.format("cmd.elo.arguments.eloPlayer.1")+"] ["+I18n.format("cmd.elo.arguments.eloPlayer.2")+"]";
	}

	@Override
	public void processSubCommand(ICommandSender sender, String[] args, MainCommand mainCommand)
			throws CommandException {
		if(args.length!=2) {
			sender.addChatMessage(new ChatComponentText(mainCommand.getCommandName()+" "+getSubCommandUsage(sender)).setChatStyle(
					new ChatStyle().setColor(EnumChatFormatting.RED)));
		} else {
			int eloPlayer1;
			int eloPlayer2;
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
			double probPlayerA = EloUtils.getWinProbability(eloPlayer1, eloPlayer2);
			double probPlayerB = 1 - probPlayerA;
			sender.addChatMessage(Chat.getPrefix(I18n.format("cmd.elo"), EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW, true, 
					EnumChatFormatting.YELLOW, I18n.format("cmd.elo.win.probability")));
			sender.addChatMessage(new ChatComponentText(I18n.format("cmd.elo.win.player")+" 1=").setChatStyle(
					new ChatStyle().setColor(EnumChatFormatting.YELLOW))
					.appendSibling(new ChatComponentText(String.valueOf(ModMain.significantNumbers(probPlayerA*100.0D, 3))+"%").setChatStyle(
							new ChatStyle().setColor(EnumChatFormatting.GOLD))));
			sender.addChatMessage(new ChatComponentText(I18n.format("cmd.elo.win.player")+" 2=").setChatStyle(
					new ChatStyle().setColor(EnumChatFormatting.YELLOW))
					.appendSibling(new ChatComponentText(String.valueOf(ModMain.significantNumbers(probPlayerB*100.0D, 3))+"%").setChatStyle(
							new ChatStyle().setColor(EnumChatFormatting.GOLD))));
		}
	}

}
