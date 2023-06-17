package fr.atesab.atehud.event;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SendPacketEvent<T extends Packet> extends Event {
	private T packet;
	public SendPacketEvent(T packet) {
		this.packet = packet;
	}
	public T getPacket() {
		return packet;
	}
	public void setPacket(T packet) {
		this.packet = packet;
	}
	public boolean isCancelable() {
		return true;
	}
}
