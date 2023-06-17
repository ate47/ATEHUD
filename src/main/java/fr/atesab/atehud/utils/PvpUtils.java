package fr.atesab.atehud.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PvpUtils {
	private static List<Long> rCpsUnit = new ArrayList<Long>();
	private static List<Long> lCpsUnit = new ArrayList<Long>();
	private static List<Long> reach = new ArrayList<Long>();
	
	public static void addLeftClickToCpsCounter() {
		while(lCpsUnit.size()>100) lCpsUnit.remove(lCpsUnit.size()-1);
		lCpsUnit.add(System.currentTimeMillis());
	}
	public static void addReach(double distance) {
		while(reach.size()>100) reach.remove(reach.size()-1);
		reach.add(System.currentTimeMillis()*1024L+(long)(distance*10));
	}
	public static void addRightClickToCpsCounter() {
		while(rCpsUnit.size()>100) rCpsUnit.remove(rCpsUnit.size()-1);
		rCpsUnit.add(System.currentTimeMillis());
	}
	public static int getLeftCpsCounter() {
		int i = 0;
		Iterator<Long> it = lCpsUnit.iterator();
		while(it.hasNext()) {
			if(it.next().longValue() > System.currentTimeMillis()-1000L) {
				i++;
			} else {
				it.remove();
			}
		}
		return i;
	}
	public static double getReach() {
		Iterator<Long> it = reach.iterator();
		double reach = 0.0D;
		long a;
		while(it.hasNext()) {
			if((a = it.next().longValue())/1024 > System.currentTimeMillis()-2500L) {
				reach = reach<((double)(a%1024)/10.0D) ? (double)(a%1024)/10.0D : reach;
			} else {
				it.remove();
			}
		}
		return reach;
	}

	public static int getRightCpsCounter() {
		int i = 0;
		Iterator<Long> it = rCpsUnit.iterator();
		while(it.hasNext()) {
			if(it.next().longValue() > System.currentTimeMillis()-1000L) {
				i++;
			} else {
				it.remove();
			}
		}
		return i;
	}
}
