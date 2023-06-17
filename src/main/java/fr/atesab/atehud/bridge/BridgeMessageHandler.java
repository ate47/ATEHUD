package fr.atesab.atehud.bridge;

import java.util.HashMap;
import java.util.Map;

import fr.atesab.atehud.bridge.BridgeMessage;
import fr.atesab.atehud.event.AtianPluginMessageEvent;
import fr.atesab.atehud.event.SendPacketEvent;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BridgeMessageHandler implements IMessageHandler<BridgeMessage, IMessage> {
	@Override
	public IMessage onMessage(BridgeMessage message, MessageContext ctx) {
		Map<String,Object> answer_args = new HashMap<String, Object>();
		answer_args.put("result", 0);
		AtianPluginMessageEvent ev = new AtianPluginMessageEvent(message, answer_args);
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(ev))
			return null;
		return new BridgeMessage("answer", answer_args);
	}

}
