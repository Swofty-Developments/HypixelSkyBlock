package net.swofty.pvp.damage;

import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.registry.RegistryKey;

import java.util.HashMap;
import java.util.Map;

public record DamageTypeInfo(boolean damagesHelmet, boolean bypassesArmor, boolean outOfWorld,
							 boolean unblockable, boolean fire, ScaleWithDifficulty scaleWithDifficulty,
							 boolean magic, boolean explosive, boolean fall, boolean thorns, boolean projectile,
							 boolean freeze) {
	//TODO check source and add missing
	public static final Map<RegistryKey<DamageType>, DamageTypeInfo> INFO_MAP = new HashMap<>() {
		{
			put(DamageType.IN_FIRE, new DamageTypeInfo().bypassesArmor(true).fire(true));
			put(DamageType.ON_FIRE, new DamageTypeInfo().bypassesArmor(true).fire(true));
			put(DamageType.LAVA, new DamageTypeInfo().fire(true));
			put(DamageType.HOT_FLOOR, new DamageTypeInfo().fire(true));
			put(DamageType.IN_WALL, new DamageTypeInfo().bypassesArmor(true));
			put(DamageType.CRAMMING, new DamageTypeInfo().bypassesArmor(true));
			put(DamageType.DROWN, new DamageTypeInfo().bypassesArmor(true));
			put(DamageType.STARVE, new DamageTypeInfo().bypassesArmor(true).unblockable(true));
			put(DamageType.FALL, new DamageTypeInfo().bypassesArmor(true).fall(true));
			put(DamageType.FLY_INTO_WALL, new DamageTypeInfo().bypassesArmor(true));
			put(DamageType.OUT_OF_WORLD, new DamageTypeInfo().bypassesArmor(true).outOfWorld(true));
			put(DamageType.GENERIC, new DamageTypeInfo().bypassesArmor(true));
			put(DamageType.GENERIC_KILL, new DamageTypeInfo().bypassesArmor(true));
			put(DamageType.MAGIC, new DamageTypeInfo().bypassesArmor(true).magic(true));
			put(DamageType.INDIRECT_MAGIC, new DamageTypeInfo().bypassesArmor(true).magic(true).scale(ScaleWithDifficulty.WHEN_CAUSED_BY_LIVING_NON_PLAYER));
			put(DamageType.WITHER, new DamageTypeInfo().bypassesArmor(true));
			put(DamageType.FALLING_ANVIL, new DamageTypeInfo().damagesHelmet(true));
			put(DamageType.FALLING_BLOCK, new DamageTypeInfo().damagesHelmet(true));
			put(DamageType.DRAGON_BREATH, new DamageTypeInfo().bypassesArmor(true));
			put(DamageType.FREEZE, new DamageTypeInfo().freeze(true).bypassesArmor(true));
			put(DamageType.FALLING_STALACTITE, new DamageTypeInfo().damagesHelmet(true));
			put(DamageType.STALAGMITE, new DamageTypeInfo().bypassesArmor(true).fall(true));
			put(DamageType.THORNS, new DamageTypeInfo().magic(true).thorns(true).scale(ScaleWithDifficulty.WHEN_CAUSED_BY_LIVING_NON_PLAYER));
			put(DamageType.EXPLOSION, new DamageTypeInfo().scale(ScaleWithDifficulty.ALWAYS).explosive(true));
			put(DamageType.PLAYER_EXPLOSION, new DamageTypeInfo().scale(ScaleWithDifficulty.ALWAYS).explosive(true));
			put(DamageType.BAD_RESPAWN_POINT, new DamageTypeInfo().scale(ScaleWithDifficulty.ALWAYS).explosive(true));
			put(DamageType.FIREBALL, new DamageTypeInfo().projectile(true).fire(true).scale(ScaleWithDifficulty.WHEN_CAUSED_BY_LIVING_NON_PLAYER));
			put(DamageType.UNATTRIBUTED_FIREBALL, new DamageTypeInfo().projectile(true).fire(true).scale(ScaleWithDifficulty.WHEN_CAUSED_BY_LIVING_NON_PLAYER));
			put(DamageType.ARROW, new DamageTypeInfo().projectile(true).scale(ScaleWithDifficulty.WHEN_CAUSED_BY_LIVING_NON_PLAYER));
			put(DamageType.WITHER_SKULL, new DamageTypeInfo().projectile(true).scale(ScaleWithDifficulty.WHEN_CAUSED_BY_LIVING_NON_PLAYER));
			put(DamageType.THROWN, new DamageTypeInfo().projectile(true).scale(ScaleWithDifficulty.WHEN_CAUSED_BY_LIVING_NON_PLAYER));
			put(DamageType.STING, new DamageTypeInfo().scale(ScaleWithDifficulty.WHEN_CAUSED_BY_LIVING_NON_PLAYER));
			put(DamageType.MOB_ATTACK, new DamageTypeInfo().scale(ScaleWithDifficulty.WHEN_CAUSED_BY_LIVING_NON_PLAYER));
			put(DamageType.MOB_PROJECTILE, new DamageTypeInfo().scale(ScaleWithDifficulty.WHEN_CAUSED_BY_LIVING_NON_PLAYER));
			put(DamageType.PLAYER_ATTACK, new DamageTypeInfo().scale(ScaleWithDifficulty.WHEN_CAUSED_BY_LIVING_NON_PLAYER));
			put(DamageType.TRIDENT, new DamageTypeInfo().scale(ScaleWithDifficulty.WHEN_CAUSED_BY_LIVING_NON_PLAYER));
			put(DamageType.FIREWORKS, new DamageTypeInfo().scale(ScaleWithDifficulty.WHEN_CAUSED_BY_LIVING_NON_PLAYER));
			put(DamageType.SONIC_BOOM, new DamageTypeInfo().bypassesArmor(true).scale(ScaleWithDifficulty.WHEN_CAUSED_BY_LIVING_NON_PLAYER));
			put(DamageType.OUTSIDE_BORDER, new DamageTypeInfo().bypassesArmor(true));
		}
	};
	private static final DamageTypeInfo DEFAULT = new DamageTypeInfo();

	public DamageTypeInfo() {
		this(
				false, false, false,
				false, false, ScaleWithDifficulty.NEVER,
				false, false, false, false, false, false
		);
	}

	public static DamageTypeInfo of(RegistryKey<DamageType> type) {
		return INFO_MAP.getOrDefault(type, DEFAULT);
	}

	public DamageTypeInfo damagesHelmet(boolean damagesHelmet) {
		return new DamageTypeInfo(
				damagesHelmet, bypassesArmor, outOfWorld,
				unblockable, fire, scaleWithDifficulty,
				magic, explosive, fall, thorns, projectile,
				freeze
		);
	}

	public DamageTypeInfo bypassesArmor(boolean bypassesArmor) {
		return new DamageTypeInfo(
				damagesHelmet, bypassesArmor, outOfWorld,
				unblockable, fire, scaleWithDifficulty,
				magic, explosive, fall, thorns, projectile,
				freeze
		);
	}

	public DamageTypeInfo outOfWorld(boolean outOfWorld) {
		return new DamageTypeInfo(
				damagesHelmet, bypassesArmor, outOfWorld,
				unblockable, fire, scaleWithDifficulty,
				magic, explosive, fall, thorns, projectile,
				freeze
		);
	}

	public DamageTypeInfo unblockable(boolean unblockable) {
		return new DamageTypeInfo(
				damagesHelmet, bypassesArmor, outOfWorld,
				unblockable, fire, scaleWithDifficulty,
				magic, explosive, fall, thorns, projectile,
				freeze
		);
	}

	public DamageTypeInfo fire(boolean fire) {
		return new DamageTypeInfo(
				damagesHelmet, bypassesArmor, outOfWorld,
				unblockable, fire, scaleWithDifficulty,
				magic, explosive, fall, thorns, projectile,
				freeze
		);
	}

	public DamageTypeInfo scale(ScaleWithDifficulty scale) {
		return new DamageTypeInfo(
				damagesHelmet, bypassesArmor, outOfWorld,
				unblockable, fire, scale,
				magic, explosive, fall, thorns, projectile,
				freeze
		);
	}

	public DamageTypeInfo magic(boolean magic) {
		return new DamageTypeInfo(
				damagesHelmet, bypassesArmor, outOfWorld,
				unblockable, fire, scaleWithDifficulty,
				magic, explosive, fall, thorns, projectile,
				freeze
		);
	}

	public DamageTypeInfo explosive(boolean explosive) {
		return new DamageTypeInfo(
				damagesHelmet, bypassesArmor, outOfWorld,
				unblockable, fire, scaleWithDifficulty,
				magic, explosive, fall, thorns, projectile,
				freeze
		);
	}

	public DamageTypeInfo fall(boolean fall) {
		return new DamageTypeInfo(
				damagesHelmet, bypassesArmor, outOfWorld,
				unblockable, fire, scaleWithDifficulty,
				magic, explosive, fall, thorns, projectile,
				freeze
		);
	}

	public DamageTypeInfo thorns(boolean thorns) {
		return new DamageTypeInfo(
				damagesHelmet, bypassesArmor, outOfWorld,
				unblockable, fire, scaleWithDifficulty,
				magic, explosive, fall, thorns, projectile,
				freeze
		);
	}

	public DamageTypeInfo projectile(boolean projectile) {
		return new DamageTypeInfo(
				damagesHelmet, bypassesArmor, outOfWorld,
				unblockable, fire, scaleWithDifficulty,
				magic, explosive, fall, thorns, projectile,
				freeze
		);
	}

	public DamageTypeInfo freeze(boolean freeze) {
		return new DamageTypeInfo(
				damagesHelmet, bypassesArmor, outOfWorld,
				unblockable, fire, scaleWithDifficulty,
				magic, explosive, fall, thorns, projectile,
				freeze
		);
	}

	public boolean shouldScaleWithDifficulty(Damage damage) {
		return switch (scaleWithDifficulty) {
			case ALWAYS -> true;
			case WHEN_CAUSED_BY_LIVING_NON_PLAYER ->
					damage.getAttacker() instanceof LivingEntity living && !(living instanceof Player);
			case NEVER -> false;
		};
	}

	public enum ScaleWithDifficulty {
		ALWAYS,
		WHEN_CAUSED_BY_LIVING_NON_PLAYER,
		NEVER
	}
}
