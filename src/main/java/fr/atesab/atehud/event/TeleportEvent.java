package fr.atesab.atehud.event;

import com.mojang.authlib.GameProfile;

import net.minecraftforge.fml.common.eventhandler.Event;

public class TeleportEvent extends Event {
	public GameProfile gameProfile;
	public int finalGameMode;

	public TeleportEvent(GameProfile gameProfile, int finalGameMode) {
		this.gameProfile = gameProfile;
		this.finalGameMode = finalGameMode;
	}

	public boolean isCancelable() {
		return true;
	}
}
