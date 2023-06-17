package fr.atesab.atehud.gui.config;

import java.io.IOException;
import java.util.List;

import fr.atesab.atehud.ATEEventHandler;
import fr.atesab.atehud.Chat;
import fr.atesab.atehud.gui.element.Element;
import fr.atesab.atehud.gui.element.HoverText;
import fr.atesab.atehud.superclass.Colors;
import fr.atesab.atehud.superclass.ModuleConfig;
import fr.atesab.atehud.superclass.ModuleConfig.ModuleType;
import fr.atesab.atehud.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class GuiModuleConfigElement{
	private GuiTextField field;
	private GuiButton button;
	public ModuleConfig config;
	public GuiScreen parent;
	public int x;
	public int y;
	private Object value;
	public GuiModuleConfigElement(ModuleConfig config, Object value, GuiScreen parent, int x, int y) {
		this.value = value;
		this.config = config;
		this.parent = parent;
		this.x = x;
		this.y = y;
	}
	public void actionPerformed(GuiButton button) throws IOException {
		if(!button.equals(this.button))return;
		switch (config.type) {
		case BOOLEAN:
			value = !(Boolean.valueOf(String.valueOf(value)).booleanValue());
			button.displayString = I18n.format(String.valueOf(getValue()));
			break;
		case ENUM:
			for (int i = 0; i < config.defaultValue.length; i++) {
				if(config.defaultValue[i].equals(String.valueOf(value))) {
					value = config.defaultValue[(i+1)%config.defaultValue.length];
					button.displayString = I18n.format(config.getLocalizedName()+".value."+((i+1)%config.defaultValue.length));
					return;
				}
			}
			break;
		case INT:
			field.setText(config.defaultValue[0]);
			break;
		case STRING:
			field.setText(config.defaultValue[0]);
			break;
		}
	}
	public int drawElement(int mouseX, int mouseY, float partialTicks) {
		if(config.type.equals(ModuleType.ENUM)) {
			String[] text = new String[config.defaultValue.length];
			for (int i = 0; i < text.length; i++) {
				text[i] = I18n.format(config.getLocalizedName()+".value."+i);
				try {
					if(config.defaultValue[i].equals(String.valueOf(value)))
						text[i]=Chat.GOLD+">> "+text[i];
				} catch (Exception e) {}
			}
			if(GuiUtils.isHover(button, mouseX, mouseY))
				ATEEventHandler.addHoverElement(new HoverText(Colors.WHITE, text));
		}
		if(field!=null) {
			field.drawTextBox();
			GuiUtils.drawRightString(parent.mc.fontRendererObj, config.getName()+" ", field.xPosition, field.yPosition, 
					field.height, Colors.GOLD);
		} else if(button!=null) {
			GuiUtils.drawRightString(parent.mc.fontRendererObj, config.getName()+" ", button.xPosition, button.yPosition, 
					button.height, Colors.GOLD);
		}
		return 21;
	}
	public Object getValue() {
		try {
			switch (config.type) {
			case BOOLEAN: return Boolean.valueOf(String.valueOf(value));
			case ENUM: return String.valueOf(value);
			case INT: return Integer.valueOf(String.valueOf(field.getText())).intValue();
			case STRING: return field.getText();
			}
		} catch (Exception e) {e.printStackTrace();}
		return value;
		
	}
	public int initElement(List<GuiButton> buttonList) {
		if(button!=null && buttonList.contains(button)) buttonList.remove(button);
		switch (config.type) {
		case BOOLEAN:
			buttonList.add(button = new GuiButton(-1, x, y, 200, 20, I18n.format(String.valueOf(getValue()))));
			break;
		case ENUM:
			for (int i = 0; i < config.defaultValue.length; i++) {
				if(config.defaultValue[i].equals(String.valueOf(value)))
					buttonList.add(button = new GuiButton(-1, x, y, 200, 20, I18n.format(config.getLocalizedName()+".value."+i)));
			}
			break;
		case INT:
			field = new GuiTextField(0, parent.mc.fontRendererObj, x+2, y+2, 156, 16);
			field.setMaxStringLength(Integer.MAX_VALUE);
			field.setText(String.valueOf(value));
			buttonList.add(button = new GuiButton(-1, x+159, y, 40, 20, I18n.format("atehud.default.little")));
			break;
		case STRING:
			field = new GuiTextField(0, parent.mc.fontRendererObj, x+2, y+2, 156, 16);
			field.setMaxStringLength(Integer.MAX_VALUE);
			field.setText(String.valueOf(value));
			buttonList.add(button = new GuiButton(-1, x+159, y, 40, 20, I18n.format("atehud.default.little")));
			break;
		}
		return 21;
	}
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		if(field!=null)field.textboxKeyTyped(typedChar, keyCode);
	}
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if(field!=null)field.mouseClicked(mouseX, mouseY, mouseButton);
	}
	@Override
	public String toString() {
		return "GuiModuleConfigElement [field=" + field + ", button=" + button + ", config=" + config + ", x=" + x
				+ ", y=" + y + ", value=" + value + "]";
	}
	public void updateElement() {
		if(field!=null)field.updateCursorCounter();
		switch (config.type) {
		case BOOLEAN:
			button.packedFGColour=Colors.redGreenOptionInt(Boolean.valueOf(String.valueOf(value)).booleanValue());
			break;
		case INT:
			try {
				int value = Integer.valueOf(String.valueOf(field.getText())).intValue();
				int min = Integer.valueOf(config.defaultValue[1]).intValue();
				int max = Integer.valueOf(config.defaultValue[2]).intValue();
				if(!(value < min || value > max)) 
					field.setTextColor(Colors.RED);
				else {
					field.setTextColor(Colors.WHITE);
					this.value = value;
				}
			} catch (Exception e) {field.setTextColor(Colors.RED);}
			break;
		default: break;
		}
	}
}
