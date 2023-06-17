package fr.atesab.atehud.superclass;

import java.util.Arrays;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import fr.atesab.atehud.FakeItems;
import fr.atesab.atehud.FakeItems2;
import fr.atesab.atehud.ModMain;
import fr.atesab.atehud.utils.ItemHelp;

public class Firework {
	private Explosion[] exp = { new Explosion() };
	private String Name = null;
	private int FlightDuration = 1;

	public Firework() {

	}

	/**
	 * 
	 * @param explosions
	 *            Explosion of the firework
	 * @param flightDuration
	 *            Indicates the flight duration of the firework (equals the amount
	 *            of gunpowder used in crafting the rocket)(-128 to 127)
	 */
	public Firework(Explosion[] explosions, int flightDuration) {
		this.setExplosions(explosions);
		if (flightDuration > -129 && flightDuration < 128) {
			this.setFlightDuration(flightDuration);
		} else {
			this.setFlightDuration(1);
		}
	}

	public Explosion[] getExplosions() {
		return exp;
	}

	public int getFlightDuration() {
		return FlightDuration;
	}

	public ItemStack getItemStack() {
		ItemStack is = ItemHelp.getNBT(Items.fireworks, getNBTFirework());
		return is;
	}

	public String getName() {
		return Name;
	}

	public String getNBTFirework() {
		String nameout = "";
		if (Name != null)
			nameout = "display:{Name:\"" + Name + "\"}, ";
		String out = "{" + nameout + "Fireworks:{Flight:" + FlightDuration + "";
		if (exp.length != 0) {
			out = out + ", Explosions:[" + exp[0].getNBTExplosion();
			if (exp.length != 1) {
				for (int i = 1; i < (exp.length) && exp[i] != null; i++) {
					out = out + "," + exp[i].getNBTExplosion();
				}
			}
			return out + "]}}";
		} else {
			return out + "}}";
		}
	}

	public void setExplosions(Explosion[] Exp) {
		exp = Exp;
	}

	public void setFlightDuration(int flightDuration) {
		FlightDuration = flightDuration;
	}

	public void setName(String name) {
		Name = name;
	}

	public String toString() {
		String nameout = "";
		if (Name != null)
			nameout = "Name=\"" + Name + "\", ";
		return "Firework [" + nameout + "exp=" + Arrays.toString(exp) + ", FlightDuration=" + FlightDuration + "]";
	}
}
