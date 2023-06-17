package fr.atesab.atehud.event;

import java.util.Map;

import fr.atesab.atehud.bridge.BridgeMessage;
import net.minecraftforge.fml.common.eventhandler.Event;

public class AtianPluginMessageEvent extends Event {
	public AtianPluginMessageEvent(BridgeMessage message, Map<String,Object> answer) {
		this.name = message.getName();
		this.args = message.getArgs();
		this.answer = answer;
	}
	public String getName() {
		return name;
	}
	public Map<String, Object> getArgs() {
		return args;
	}
	private String name;
	private Map<String,Object> args;
	private Map<String,Object> answer;
	public Map<String, Object> getAnswer() {
		return answer;
	}
	public void setResult(int result) {
		answer.put("result", result);
	}
	/**
	 * Cancel to avoid response to the server
	 */
	@Override
	public void setCanceled(boolean cancel) {
		super.setCanceled(cancel);
	}
	@Override
	public boolean isCancelable() {
		return true;
	}
}
