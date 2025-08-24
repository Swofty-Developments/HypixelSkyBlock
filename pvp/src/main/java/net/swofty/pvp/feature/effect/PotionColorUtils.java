package net.swofty.pvp.feature.effect;

import net.minestom.server.potion.Potion;

import java.util.Collection;

class PotionColorUtils {
	public static int getPotionColor(Collection<Potion> effects) {
		int r = 0, g = 0, b = 0;
		int totalAmplifier = 0;
		
		for (Potion potion : effects) {
			if (potion.hasParticles()) {
				int color = potion.effect().registry().color();
				int amplifier = potion.amplifier() + 1;
				r += amplifier * (color >> 16 & 0xFF);
				g += amplifier * (color >> 8 & 0xFF);
				b += amplifier * (color & 0xFF);
				totalAmplifier += amplifier;
			}
		}
		
		if (totalAmplifier == 0) {
			return -1;
		} else {
			return rgba(255, r / totalAmplifier, g / totalAmplifier, b / totalAmplifier);
		}
	}
	
	public static int rgba(int a, int r, int g, int b) {
		return (a << 24) | (r << 16) | (g << 8) | b;
	}
}
