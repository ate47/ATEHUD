package fr.atesab.atehud.bridge;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.event.ReceivePacketEvent;
import fr.atesab.atehud.event.SendPacketEvent;
import fr.atesab.atehud.event.TeleportEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import io.netty.channel.ChannelPipeline;

public class AtianChannelBridge extends ChannelDuplexHandler {
	public static final String CHANNEL_NAME = "atian_packet_handler";
	private final ChannelPipeline pipeline;
	public AtianChannelBridge(ChannelPipeline pipeline) {
		this.pipeline = pipeline.addBefore("packet_handler", CHANNEL_NAME, this);
		pipeline.addFirst(new ChannelDuplexHandler() {
			@Override
			public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
				System.out.println(ctx.name());
				super.channelRead(ctx, msg);
			}
			@Override
			public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
				System.out.println(ctx.name());
				super.write(ctx, msg, promise);
			}
		});
	}
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if(msg!=null && msg instanceof Packet) {
			if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new SendPacketEvent((Packet) msg)))
				return;
		}
		super.write(ctx, msg, promise);
	}
	@Override
	public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
		if(msg!=null && msg instanceof Packet) {
			if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new ReceivePacketEvent((Packet) msg)))
				return;
		}
		super.channelRead(channelHandlerContext, msg);
	}
	public ChannelPipeline getPipeline() {
		return pipeline;
	}
}
