package fr.atesab.atehud.event;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ReceivePacketEvent<T extends Packet> extends Event {
	private T packet;
	public ReceivePacketEvent(T packet) {
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
