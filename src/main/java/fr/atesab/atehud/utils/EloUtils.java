package fr.atesab.atehud.utils;

public class EloUtils {
	public static double getNextElo(int playerAElo, int PlayerAPlayedParty, int playerBElo, boolean playerAWin) throws Exception {
		if(PlayerAPlayedParty < 0) throw new Exception("Math Error: Negative played party");
		double k = 40.0D;
		if(playerAElo>=2400) k = 10.0D;
		else if(PlayerAPlayedParty>=30) k = 20.0D;
		double r = 1.0D; if(!playerAWin) r = 0.0D;
		return playerAElo + k * (r - getWinProbability(playerAElo, playerBElo));
	}
	public static double getWinProbability(int playerAElo, int playerBElo) {
		return 1.0D / (1.0D + Math.pow(10, -((double)(playerAElo-playerBElo))/400.0D));
	}
}
