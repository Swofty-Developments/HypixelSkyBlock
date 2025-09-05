package net.swofty.pvp.potion.item;

import net.swofty.pvp.utils.PotionFlags;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.PotionType;

import java.util.HashMap;
import java.util.Map;

public class CombatPotionTypes {
	private static final Map<PotionType, CombatPotionType> POTION_EFFECTS = new HashMap<>();
	
	public static CombatPotionType get(PotionType potionType) {
		return POTION_EFFECTS.get(potionType);
	}
	
	public static void register(CombatPotionType... potionTypes) {
		for (CombatPotionType potionType : potionTypes) {
			POTION_EFFECTS.put(potionType.getPotionType(), potionType);
		}
	}
	
	public static void registerAll() {
		register(
				new CombatPotionType(PotionType.WATER),
				new CombatPotionType(PotionType.MUNDANE),
				new CombatPotionType(PotionType.THICK),
				new CombatPotionType(PotionType.AWKWARD),
				
				new CombatPotionType(PotionType.NIGHT_VISION, new Potion(PotionEffect.NIGHT_VISION, (byte) 0, 3600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.LONG_NIGHT_VISION, new Potion(PotionEffect.NIGHT_VISION, (byte) 0, 9600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.INVISIBILITY, new Potion(PotionEffect.INVISIBILITY, (byte) 0, 3600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.LONG_INVISIBILITY, new Potion(PotionEffect.INVISIBILITY, (byte) 0, 9600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.LEAPING, new Potion(PotionEffect.JUMP_BOOST, (byte) 0, 3600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.LONG_LEAPING, new Potion(PotionEffect.JUMP_BOOST, (byte) 0, 9600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.STRONG_LEAPING, new Potion(PotionEffect.JUMP_BOOST, (byte) 1, 1800, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.FIRE_RESISTANCE, new Potion(PotionEffect.FIRE_RESISTANCE, (byte) 0, 3600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.LONG_FIRE_RESISTANCE, new Potion(PotionEffect.FIRE_RESISTANCE, (byte) 0, 9600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.SWIFTNESS, new Potion(PotionEffect.SPEED, (byte) 0, 3600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.LONG_SWIFTNESS, new Potion(PotionEffect.SPEED, (byte) 0, 9600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.STRONG_SWIFTNESS, new Potion(PotionEffect.SPEED, (byte) 1, 1800, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.SLOWNESS, new Potion(PotionEffect.SLOWNESS, (byte) 0, 1800, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.LONG_SLOWNESS, new Potion(PotionEffect.SLOWNESS, (byte) 0, 4800, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.STRONG_SLOWNESS, new Potion(PotionEffect.SLOWNESS, (byte) 3, 400, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.TURTLE_MASTER, new Potion(PotionEffect.SLOWNESS, (byte) 3, 400, PotionFlags.defaultFlags()), new Potion(PotionEffect.RESISTANCE, (byte) 2, 400, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.LONG_TURTLE_MASTER, new Potion(PotionEffect.SLOWNESS, (byte) 3, 800, PotionFlags.defaultFlags()), new Potion(PotionEffect.RESISTANCE, (byte) 2, 800, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.STRONG_TURTLE_MASTER, new Potion(PotionEffect.SLOWNESS, (byte) 5, 400, PotionFlags.defaultFlags()), new Potion(PotionEffect.RESISTANCE, (byte) 3, 400, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.WATER_BREATHING, new Potion(PotionEffect.WATER_BREATHING, (byte) 0, 3600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.LONG_WATER_BREATHING, new Potion(PotionEffect.WATER_BREATHING, (byte) 0, 9600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.HEALING, new Potion(PotionEffect.INSTANT_HEALTH, (byte) 0, 1, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.STRONG_HEALING, new Potion(PotionEffect.INSTANT_HEALTH, (byte) 1, 1, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.HARMING, new Potion(PotionEffect.INSTANT_DAMAGE, (byte) 0, 1, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.STRONG_HARMING, new Potion(PotionEffect.INSTANT_DAMAGE, (byte) 1, 1, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.POISON, new Potion(PotionEffect.POISON, (byte) 0, 900, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.LONG_POISON, new Potion(PotionEffect.POISON, (byte) 0, 1800, PotionFlags.defaultFlags())).legacy(new Potion(PotionEffect.POISON, (byte) 0, 2400, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.STRONG_POISON, new Potion(PotionEffect.POISON, (byte) 1, 432, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.REGENERATION, new Potion(PotionEffect.REGENERATION, (byte) 0, 900, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.LONG_REGENERATION, new Potion(PotionEffect.REGENERATION, (byte) 0, 1800, PotionFlags.defaultFlags())).legacy(new Potion(PotionEffect.REGENERATION, (byte) 0, 2400, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.STRONG_REGENERATION, new Potion(PotionEffect.REGENERATION, (byte) 1, 450, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.STRENGTH, new Potion(PotionEffect.STRENGTH, (byte) 0, 3600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.LONG_STRENGTH, new Potion(PotionEffect.STRENGTH, (byte) 0, 9600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.STRONG_STRENGTH, new Potion(PotionEffect.STRENGTH, (byte) 1, 1800, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.WEAKNESS, new Potion(PotionEffect.WEAKNESS, (byte) 0, 1800, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.LONG_WEAKNESS, new Potion(PotionEffect.WEAKNESS, (byte) 0, 4800, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.LUCK, new Potion(PotionEffect.LUCK, (byte) 0, 6000, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.SLOW_FALLING, new Potion(PotionEffect.SLOW_FALLING, (byte) 0, 1800, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.LONG_SLOW_FALLING, new Potion(PotionEffect.SLOW_FALLING, (byte) 0, 4800, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.WIND_CHARGED, new Potion(PotionEffect.WIND_CHARGED, (byte) 0, 3600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.WEAVING, new Potion(PotionEffect.WEAVING, (byte) 0, 3600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.OOZING, new Potion(PotionEffect.OOZING, (byte) 0, 3600, PotionFlags.defaultFlags())),
				new CombatPotionType(PotionType.INFESTED, new Potion(PotionEffect.INFESTED, (byte) 0, 3600, PotionFlags.defaultFlags()))
		);
	}
}
