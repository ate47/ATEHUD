package fr.atesab.atehud.bridge;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BridgeMessage implements IMessage {
	public String getName() {
		return name;
	}

	public Map<String, Object> getArgs() {
		return args;
	}

	private static final Gson gson = new GsonBuilder().create(); 
	private String name;
	private Map<String, Object> args;
	public BridgeMessage(String name, Map<String, Object> args) {
		this.name = name;
		this.args = args;
	}
	public BridgeMessage(String name, String JsonArgs) {
		this.name = name;
		this.args = (HashMap<String,Object>) gson.fromJson(JsonArgs, HashMap.class);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		String[] s = buf.toString(Charset.forName("UTF-8")).split(" ", 2);
		System.out.println(s);
		name = s[0];
		args = (HashMap<String,Object>) gson.fromJson(s[1], HashMap.class);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBytes((name+" "+gson.toJson(args)).getBytes(Charset.forName("UTF-8")));
	}
}
