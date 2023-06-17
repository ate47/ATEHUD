package fr.atesab.atehud;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import fr.atesab.atehud.event.LoadModEvent;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.atesab.atehud.gui.GuiFakeMessage;
import fr.atesab.atehud.gui.GuiFireWorkFactory;
import fr.atesab.atehud.gui.GuiGameMode;
import fr.atesab.atehud.gui.GuiHudOptions;
import fr.atesab.atehud.gui.GuiInvView;
import fr.atesab.atehud.gui.GuiInventory;
import fr.atesab.atehud.gui.GuiItemFactory;
import fr.atesab.atehud.gui.GuiLoginOption;
import fr.atesab.atehud.gui.GuiMenu;
import fr.atesab.atehud.gui.GuiNbtCode;
import fr.atesab.atehud.gui.GuiRndGame;
import fr.atesab.atehud.gui.GuiSkullGiver;
import fr.atesab.atehud.gui.GuiSpammer;
import fr.atesab.atehud.gui.GuiTeleporter;
import fr.atesab.atehud.gui.GuiTicTacToe;
import fr.atesab.atehud.gui.GuiToolTypConfig;
import fr.atesab.atehud.gui.GuiVariableChanger;
import fr.atesab.atehud.gui.config.GuiATEHUDConfig;
import fr.atesab.atehud.gui.config.GuiChatOption;
import fr.atesab.atehud.gui.config.GuiKeyBinding;
import fr.atesab.atehud.gui.config.GuiTS3Config;
import fr.atesab.atehud.gui.element.ButtonsContainer;
import fr.atesab.atehud.gui.element.ButtonsContainer.ModContainer;
import fr.atesab.atehud.utils.CommandUtils;
import fr.atesab.atehud.utils.GuiUtils;

public class Mod {
	public static class CommandMod extends Mod {
		public String[] commands;
		public String preMessage;

		public CommandMod(String id, String category, boolean requireCreative, List<String> commands) {
			this(id, category, requireCreative, commands.toArray(new String[commands.size()]));
		}

		public CommandMod(String id, String category, boolean requireCreative, List<String> commands,
				String preMessage) {
			this(id, category, requireCreative, commands.toArray(new String[commands.size()]), preMessage);
		}

		public CommandMod(String id, String category, boolean requireCreative, String[] commands) {
			this(id, category, requireCreative, commands, null);
		}

		public CommandMod(String id, String category, boolean requireCreative, String[] commands, String preMessage) {
			super(id, category, requireCreative);
			this.commands = commands;
			this.preMessage = preMessage;
		}

		public void onEnabled() {
			Minecraft mc = Minecraft.getMinecraft();
			if (mc.thePlayer != null) {
				if (preMessage != null)
					Chat.show(I18n.format(preMessage));
				for (int i = 0; i < commands.length; i++)
					CommandUtils.sendOptionMessage(commands[i]);
			}
			setEnabled(false);
		}
	}

	public static class ShowGuiMod extends Mod {
		public Class toLoad;

		public ShowGuiMod(String id, String category, boolean requireCreative, Class toShow) {
			this(id, category, requireCreative, toShow, true);
		}

		public ShowGuiMod(String id, String category, boolean requireCreative, Class toShow, boolean autoGen) {
			super(id, category, requireCreative, autoGen);
			toLoad = toShow;
		}

		public void onEnabled() {
			try {
				Minecraft mc = Minecraft.getMinecraft();
				mc.displayGuiScreen((GuiScreen) toLoad.getDeclaredConstructor(GuiScreen.class).newInstance(mc.currentScreen));
			} catch (Exception e) {
				Chat.error(e.getMessage());
			}
		}
	}

	public static class SliderMod extends Mod {
		public String className, fieldName;
		public float min, max;

		public SliderMod(String id, String category, boolean requireCreative, String className, String fieldName,
				float min, float max) {
			super(id, category, requireCreative);
			this.className = className;
			this.fieldName = fieldName;
			this.min = min;
			this.max = max;
		}

		public void changeValue(float vl) {
			vl = ((max - min) * vl);
			try {
				Field f = Class.forName(className).getField(fieldName);
				f.setAccessible(true);
				if (f.getType() == Float.class)
					f.set(float.class, ((float) vl));
				else
					f.set(int.class, ((int) vl));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public float getValue() {
			Field f;
			try {
				f = Class.forName(className).getField(fieldName);
			} catch (Exception e) {
				f = null;
			}
			if (f != null) {
				try {
					if (f.getType() == float.class)
						return (Float) ((Float) (f.get(this)) / (max - min));
					else
						return (Float) ((Integer) (f.get(this)) / (max - min));
				} catch (Exception e) {
					System.out.println("Error when load " + className + "." + fieldName + " (type : "
							+ f.getType().getName() + ")");
					e.printStackTrace();
					return 1.0F;
				}
			} else {
				return 1.0F;
			}
		}
	}
	public static class ToggleMod extends Mod {
		public String className, fieldName;

		public ToggleMod(String id, String category, boolean requireCreative, String className, String fieldName) {
			super(id, category, requireCreative);
			this.className = className;
			this.fieldName = fieldName;
		}

		public ToggleMod(String id, String category, boolean requireCreative, String className, String fieldName,
				boolean autoGen) {
			super(id, category, requireCreative, autoGen);
			this.className = className;
			this.fieldName = fieldName;
		}

		public String getName() {
			return super.getName() + GuiMenu.enableOrNot(this.isEnabled());
		}

		public boolean isEnabled() {
			boolean a = false;
			try {
				a = (boolean) Class.forName(className).getField(fieldName).getBoolean(boolean.class);
			} catch (Exception e) {
			}
			return a;
		}

		public void onDisabled() {
			try {
				Class.forName(className).getField(fieldName).set(boolean.class, false);
			} catch (IllegalArgumentException e) {
				Chat.error("IllegalArgumentException");
			} catch (IllegalAccessException e) {
				Chat.error("IllegalAccessException");
			} catch (NoSuchFieldException e) {
				Chat.error("Field not found : " + fieldName);
			} catch (SecurityException e) {
				Chat.error("SecurityException");
			} catch (ClassNotFoundException e) {
				Chat.error("Class not found : " + className);
			}
			super.onDisabled();
		}

		public void onEnabled() {
			try {
				Class.forName(className).getField(fieldName).set(boolean.class, true);
			} catch (IllegalArgumentException e) {
				Chat.error("IllegalArgumentException");
			} catch (IllegalAccessException e) {
				Chat.error("IllegalAccessException");
			} catch (NoSuchFieldException e) {
				Chat.error("Field not found : " + fieldName);
			} catch (SecurityException e) {
				Chat.error("SecurityException");
			} catch (ClassNotFoundException e) {
				Chat.error("Class not found : " + className);
			}
			super.onEnabled();
		}
	}
	private static List<Mod> mods = Lists.newArrayList();
	public static boolean modsLoaded = false;

	public static Mod[][] CategoryMods() {
		List<Mod[]> a = new ArrayList<Mod[]>();
		for (int i = 0; i < getCategorys().length; i++) {
			List<Mod> mods = getModsByCategory(new String[] { getCategorys()[i] });
			if(mods.size()!=0)
				a.add(mods.toArray(new Mod[mods.size()]));
		}
		return a.toArray(new Mod[a.size()][]);
	}
	public static String[] DefaultElems() {
		// ModMain.MenuElements
		Mod[][] mods = CategoryMods();
		List<String> elems = new ArrayList<String>();
		Minecraft mc = Minecraft.getMinecraft();
		int size = 0;
		for (int i = 0; i < mods.length; i++) {
			if(mods[i].length<1)continue;
			String a = "";
			List<String> modsList = new ArrayList<String>();
			for (int j = 0; j < mods[i].length; j++) {
				if (mods[i][j].autoGen)
					modsList.add(mods[i][j].id);
			}
			if(modsList.size()==0)continue; else size++;
			String b = "";
			b = getModContainerSave(modsList, 0	, 40 + (22) * i, false, I18n.format("mod."+mods[i][0].category+".name"));
			if (!b.isEmpty())
				elems.add(b);
		}
		return elems.toArray(new String[size]);
	}

	public static List<Mod> findMods(String name) {
		List<Mod> lst = Lists.newArrayList();
		for (Iterator iterator = mods.iterator(); iterator.hasNext();) {
			Mod mod = (Mod) iterator.next();
			if (mod.getName().toLowerCase().contentEquals(name.toLowerCase()))
				lst.add(mod);
		}
		return lst;
	}

	public static String[] GenElems(List<ModContainer> menuElems, Minecraft mc) {
		List<String> elem = Lists.newArrayList();
		ScaledResolution sr = new ScaledResolution(mc);
		for (ModContainer mdc: menuElems) {
			if (mdc != null) {
				elem.add(mdc.getData());
			}
		}
		return elem.toArray(new String[elem.size()]);
	}

	public static String[] getCategoryDescription(String category) {
		return I18n.format("mod." + category + ".desc").split("::");
	}

	public static String getCategoryName(String category) {
		return I18n.format("mod." + category + ".name");
	}

	public static String[] getCategorys() {
		List<String> cat = Lists.newArrayList();
		for (int i = 0; i < mods.size(); i++)
			if (!cat.contains(mods.get(i).category))
				cat.add(mods.get(i).category);
		String[] categorys = new String[cat.size()];
		for (int i = 0; i < categorys.length; i++)
			categorys[i] = cat.get(i);
		return categorys;
	}

	public static ModContainer getContainer(String elem, Minecraft mc, int defaultPosX, int defaultPosY) {
		if (elem != null) {
			Gson gson = new GsonBuilder().create();
			Map<String,Object> elm = gson.fromJson(elem, HashMap.class);
			try {
				List<Mod> mods = new ArrayList<Mod>();
				for (String mod: (ArrayList<String>)elm.getOrDefault("mods", new ArrayList<String>())) {
					Mod a = getModById(mod);
					if (a != null)
						mods.add(a);
				}
				ScaledResolution sr = new ScaledResolution(mc);
				boolean b = ((Boolean)elm.getOrDefault("open", false)).booleanValue();
				return new ModContainer((String)elm.getOrDefault("category", "unknow"), (int) ((Double)elm.getOrDefault("posx", "0") * sr.getScaledWidth()),
						(int) ((Double)elm.getOrDefault("posy", "0") * sr.getScaledHeight()), defaultPosX, defaultPosY,
						mods.toArray(new Mod[mods.size()]), b);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else
			return null;
	}
	public static Mod getModById(String id) {
		for (Iterator iterator = mods.iterator(); iterator.hasNext();) {
			Mod mod = (Mod) iterator.next();
			if (mod.id.equals(id))
				return mod;
		}
		return null;
	}
	
	public static String getModContainerSave(List<String> modsList, int posX, int posY, boolean open, String title) {
		Minecraft mc = Minecraft.getMinecraft();
		Gson gson = new GsonBuilder().create();
		Map<String,Object> elem = new HashMap<String,Object>();
		ScaledResolution sr = new ScaledResolution(mc);
		elem.put("mods", modsList);
		elem.put("posx", (float) posX / (sr.getScaledWidth_double()));
		elem.put("posy", (float) posY / (sr.getScaledHeight_double()));
		elem.put("open", open);
		elem.put("category", title);
		return gson.toJson(elem);
	}

	public static String getModContainerSave(ModContainer modContainer) {
		List<String> mods = new ArrayList<String>();
		for (Mod m: modContainer.mods) {
			mods.add(m.id);
		}
		return getModContainerSave(mods, modContainer.posX, modContainer.posY, modContainer.open, modContainer.title);
	}

	public static List<Mod> getMods() {
		return mods;
	}

	public static List<Mod> getModsByCategory(String[] categorys) {
		List<Mod> lst = Lists.newArrayList();
		for (Iterator iterator = mods.iterator(); iterator.hasNext();) {
			Mod mod = (Mod) iterator.next();
			for (int i = 0; i < categorys.length; i++)
				if (mod.category.equals(categorys[i]) && !lst.contains(mod))
					lst.add(mod);
		}
		return lst;
	}

	public static void loadMods() {
		LoadModEvent event = new LoadModEvent(mods);
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
		String log = "";
		mods = event.getMods();
		int mod = 0;
		for (int i = 0; i < mods.size(); i++) {
			if(mods.get(i)==null)continue;
			if(i > 0)log+=",";
			log+=mods.get(i).getName();
			mod++;
		}
		String s = "";
		if(mod>1)s="s";
		System.out.println("Loaded " + mod + " mod" + s + " : ["+log+"]");
		modsLoaded = true;
		ModMain.MenuElements = DefaultElems();
	}

	private String id;

	public String category;

	public boolean enabled, requireCreative, autoGen;

	public Mod(String id, String category, boolean requireCreative) {
		this(id, category, requireCreative, true);
	}

	public Mod(String id, String category, boolean requireCreative, boolean autoGen) {
		super();
		this.id = id;
		this.category = category;
		this.requireCreative = requireCreative;
		this.autoGen = autoGen;
	}

	public String[] getDesc() {
		return I18n.format("mod." + category + "." + id + ".desc").split("::");
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return I18n.format("mod." + category + "." + id + ".name");
	}

	public boolean getRequireCreative() {
		return requireCreative;
	}


	public void Init() {}

	public boolean isEnabled() {
		return enabled;
	}

	public void onDisabled() {}
	
	public void onEnabled() {}

	public void PostInit() {}

	public void renderOverlay(RenderGameOverlayEvent ev) {}

	public void renderWorld(RenderWorldLastEvent ev) {}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void setRequireCreative(boolean requireCreative) {
		this.requireCreative = requireCreative;
	}

	public void tick(TickEvent ev) {}

}
