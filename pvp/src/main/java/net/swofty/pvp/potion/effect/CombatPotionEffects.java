package net.swofty.pvp.potion.effect;

import net.kyori.adventure.key.Key;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.attribute.AttributeOperation;
import net.minestom.server.particle.Particle;
import net.minestom.server.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;

public class CombatPotionEffects {
	private static final Map<PotionEffect, CombatPotionEffect> POTION_EFFECTS = new HashMap<>();

	public static CombatPotionEffect get(PotionEffect potionEffect) {
		return POTION_EFFECTS.get(potionEffect);
	}

	public static void register(CombatPotionEffect... potionEffects) {
		for (CombatPotionEffect potionEffect : potionEffects) {
			POTION_EFFECTS.put(potionEffect.getPotionEffect(), potionEffect);
		}
	}

	public static void registerAll() {
		register(
				new CombatPotionEffect(PotionEffect.SPEED).addAttributeModifier(Attribute.MOVEMENT_SPEED, Key.key("minecraft:effect.speed"), 0.2, AttributeOperation.ADD_MULTIPLIED_TOTAL),
				new CombatPotionEffect(PotionEffect.SLOWNESS).addAttributeModifier(Attribute.MOVEMENT_SPEED, Key.key("minecraft:effect.slowness"), -0.15, AttributeOperation.ADD_MULTIPLIED_TOTAL),
				new CombatPotionEffect(PotionEffect.HASTE).addAttributeModifier(Attribute.ATTACK_SPEED, Key.key("minecraft:effect.haste"), 0.1, AttributeOperation.ADD_MULTIPLIED_TOTAL),
				new CombatPotionEffect(PotionEffect.MINING_FATIGUE).addAttributeModifier(Attribute.ATTACK_SPEED, Key.key("minecraft:effect.mining_fatigue"), -0.1, AttributeOperation.ADD_MULTIPLIED_TOTAL),
				new CombatPotionEffect(PotionEffect.STRENGTH).addAttributeModifier(Attribute.ATTACK_DAMAGE, Key.key("minecraft:effect.strength"), 3.0, AttributeOperation.ADD_VALUE).addLegacyAttributeModifier(Attribute.ATTACK_DAMAGE, Key.key("minecraft:effect.strength"), 1.3, AttributeOperation.ADD_MULTIPLIED_TOTAL),
				new CombatPotionEffect(PotionEffect.INSTANT_HEALTH),
				new CombatPotionEffect(PotionEffect.INSTANT_DAMAGE),
				new CombatPotionEffect(PotionEffect.JUMP_BOOST).addAttributeModifier(Attribute.SAFE_FALL_DISTANCE, Key.key("minecraft:effect.jump_boost"), 1.0, AttributeOperation.ADD_VALUE),
				new CombatPotionEffect(PotionEffect.NAUSEA),
				new CombatPotionEffect(PotionEffect.REGENERATION),
				new CombatPotionEffect(PotionEffect.RESISTANCE),
				new CombatPotionEffect(PotionEffect.FIRE_RESISTANCE),
				new CombatPotionEffect(PotionEffect.WATER_BREATHING),
				new CombatPotionEffect(PotionEffect.INVISIBILITY),
				new CombatPotionEffect(PotionEffect.BLINDNESS),
				new CombatPotionEffect(PotionEffect.NIGHT_VISION),
				new CombatPotionEffect(PotionEffect.HUNGER),
				new CombatPotionEffect(PotionEffect.WEAKNESS).addAttributeModifier(Attribute.ATTACK_DAMAGE, Key.key("minecraft:effect.weakness"), -4.0, AttributeOperation.ADD_VALUE).addLegacyAttributeModifier(Attribute.ATTACK_DAMAGE, Key.key("minecraft:effect.weakness"), -0.5, AttributeOperation.ADD_VALUE),
				new CombatPotionEffect(PotionEffect.POISON),
				new CombatPotionEffect(PotionEffect.WITHER),
				new HealthBoostPotionEffect().addAttributeModifier(Attribute.MAX_HEALTH, Key.key("minecraft:effect.health_boost"), 4.0, AttributeOperation.ADD_VALUE),
				new AbsorptionPotionEffect(),
				new CombatPotionEffect(PotionEffect.SATURATION),
				new GlowingPotionEffect(),
				new CombatPotionEffect(PotionEffect.LEVITATION),
				new CombatPotionEffect(PotionEffect.LUCK).addAttributeModifier(Attribute.LUCK, Key.key("minecraft:effect.luck"), 1.0, AttributeOperation.ADD_VALUE),
				new CombatPotionEffect(PotionEffect.UNLUCK).addAttributeModifier(Attribute.LUCK, Key.key("minecraft:effect.unluck"), -1.0, AttributeOperation.ADD_VALUE),
				new CombatPotionEffect(PotionEffect.SLOW_FALLING),
				new CombatPotionEffect(PotionEffect.CONDUIT_POWER),
				new CombatPotionEffect(PotionEffect.DOLPHINS_GRACE),
				new CombatPotionEffect(PotionEffect.BAD_OMEN),
				new CombatPotionEffect(PotionEffect.HERO_OF_THE_VILLAGE),
				new CombatPotionEffect(PotionEffect.DARKNESS),
				new CombatPotionEffect(PotionEffect.TRIAL_OMEN, potion -> Particle.TRIAL_OMEN),
				new CombatPotionEffect(PotionEffect.RAID_OMEN, potion -> Particle.RAID_OMEN),
				new CombatPotionEffect(PotionEffect.WIND_CHARGED, potion -> Particle.SMALL_GUST),
				new CombatPotionEffect(PotionEffect.WEAVING),
				new CombatPotionEffect(PotionEffect.OOZING),
				new CombatPotionEffect(PotionEffect.INFESTED)
		);
	}
}
