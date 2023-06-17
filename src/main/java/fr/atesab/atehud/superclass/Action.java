package fr.atesab.atehud.superclass;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.GsonBuilder;

import fr.atesab.atehud.Mod;
import fr.atesab.atehud.Mod.SliderMod;
import fr.atesab.atehud.gui.GuiTeleporter;
import fr.atesab.atehud.utils.GiveItemUtils;
import fr.atesab.atehud.utils.ItemHelp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.command.NumberInvalidException;

public class Action {
	private static ArrayList<Action> actions = new ArrayList<Action>();
	public static void defineAction() {
		actions.add(new Action("ChatMessage", "Send a chat message", 1) {
			public void doAction(String[] args) {
				Minecraft mc = Minecraft.getMinecraft();
				if (mc.thePlayer != null)
					mc.thePlayer.sendChatMessage(args[0]);
			}
		});
		actions.add(new Action("Giver", "Give you item", 1) {
			public void doAction(String[] args) {
				try {
					GiveItemUtils.give(Minecraft.getMinecraft(), ItemHelp.getGive(args[0]));
				} catch (NumberInvalidException e) {
				}
			}
		});
		actions.add(new Action("OpenChat", "Open chat", 1) {
			public void doAction(String[] args) {
				Minecraft mc = Minecraft.getMinecraft();
				mc.displayGuiScreen(new GuiChat(args[0]));
			}
		});
		actions.add(new Action("OpenGui", "Open a GUI", 1) {
			public void doAction(String[] args) {
				Mod mod = Mod.getModById(args[0]);
				try {
					mod.setEnabled(true);
				} catch (Exception e) {
				}
			}
		});
		actions.add(new Action("Teleporter", "Use the gm3 teleporter", 1) {
			public void doAction(String[] args) {
				GuiTeleporter.teleport(args[0]);
			}
		});
		actions.add(new Action("ToggleMod", "Set values of mod", 2) {
			public void doAction(String[] args) {
				Mod mod = Mod.getModById(args[0]);
				try {
					if (mod instanceof Mod.ToggleMod) {
						mod.setEnabled(Boolean.getBoolean(args[1]));
					} else if (mod instanceof Mod.SliderMod) {
						((SliderMod) mod).changeValue(Float.valueOf(args[1]).floatValue());
					}
				} catch (Exception e) {
				}
			}
		});
	}

	public static Action getActionByName(String name) {
		for (int i = 0; i < actions.size(); i++) {
			if (actions.get(i).name.equals(name))
				return actions.get(i);
		}
		return null;
	}

	public String name, description;

	public int arguments;;

	public Action(String name, String description, int arguments) {
		this.name = name;
		this.description = description;
		this.arguments = arguments;
	}

	public void doAction(String[] args) {
	}

	public String getData() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("arguments", arguments);
		map.put("description", description);

		return new GsonBuilder().create().toJson(map);
	}
}
