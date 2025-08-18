package net.swofty.pvp.enchantment;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;

public enum EntityGroup {
	DEFAULT,
	UNDEAD,
	ARTHROPOD,
	ILLAGER,
	AQUATIC;
	
	public static EntityGroup ofEntity(LivingEntity entity) {
		EntityType entityType = entity.getEntityType();
		if (entityType == EntityType.BEE || entityType == EntityType.CAVE_SPIDER || entityType == EntityType.ENDERMITE || entityType == EntityType.SILVERFISH || entityType == EntityType.SPIDER) {
			return EntityGroup.ARTHROPOD;
		} else if (entityType == EntityType.COD || entityType == EntityType.DOLPHIN || entityType == EntityType.ELDER_GUARDIAN || entityType == EntityType.GUARDIAN || entityType == EntityType.PUFFERFISH || entityType == EntityType.SALMON || entityType == EntityType.SQUID || entityType == EntityType.TROPICAL_FISH || entityType == EntityType.TURTLE) {
			return EntityGroup.AQUATIC;
		} else if (EntityType.DROWNED == entityType || EntityType.HUSK == entityType || EntityType.PHANTOM == entityType || EntityType.SKELETON == entityType || EntityType.SKELETON_HORSE == entityType || EntityType.STRAY == entityType || EntityType.WITHER == entityType || EntityType.WITHER_SKELETON == entityType || EntityType.ZOGLIN == entityType || EntityType.ZOMBIE == entityType || EntityType.ZOMBIE_HORSE == entityType || EntityType.ZOMBIE_VILLAGER == entityType || EntityType.ZOMBIFIED_PIGLIN == entityType) {
			return EntityGroup.UNDEAD;
		} else if (EntityType.EVOKER == entityType || EntityType.ILLUSIONER == entityType || EntityType.PILLAGER == entityType || EntityType.VINDICATOR == entityType) {
			return EntityGroup.ILLAGER;
		}
		
		return EntityGroup.DEFAULT;
	}
	
	public boolean isUndead() {
		return this == UNDEAD;
	}
}
