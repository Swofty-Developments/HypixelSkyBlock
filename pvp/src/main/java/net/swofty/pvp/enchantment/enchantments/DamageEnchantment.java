package net.swofty.pvp.enchantment.enchantments;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.registry.RegistryKey;
import net.swofty.pvp.enchantment.CombatEnchantment;
import net.swofty.pvp.enchantment.EntityGroup;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.config.FeatureConfiguration;
import net.swofty.pvp.feature.enchantment.EnchantmentFeature;
import net.swofty.pvp.utils.PotionFlags;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class DamageEnchantment extends CombatEnchantment {
	private final Type type;

	public DamageEnchantment(RegistryKey<Enchantment> enchantment, Type type, EquipmentSlot... slotTypes) {
		super(enchantment, Set.of(FeatureType.VERSION), slotTypes);
		this.type = type;
	}

	@Override
	public float getAttackDamage(int level, EntityGroup group,
								 EnchantmentFeature feature, FeatureConfiguration configuration) {
		if (type == Type.ALL) {
			if (configuration.get(FeatureType.VERSION).legacy()) return level * 1.25F;
			return 1.0F + (float) Math.max(0, level - 1) * 0.5F;
		} else if (type == Type.UNDEAD && group == EntityGroup.UNDEAD) {
			return (float) level * 2.5F;
		} else {
			return type == Type.ARTHROPODS && group == EntityGroup.ARTHROPOD ? (float) level * 2.5F : 0.0F;
		}
	}

	@Override
	public void onTargetDamaged(LivingEntity user, Entity target, int level,
								EnchantmentFeature feature, FeatureConfiguration configuration) {
		if (target instanceof LivingEntity livingEntity) {
			if (type == Type.ARTHROPODS && EntityGroup.ofEntity(livingEntity) == EntityGroup.ARTHROPOD) {
				int i = 20 + ThreadLocalRandom.current().nextInt(10 * level);
				livingEntity.addEffect(new Potion(PotionEffect.SLOWNESS, (byte) 3, i, PotionFlags.defaultFlags()));
			}
		}
	}

	public enum Type {
		ALL, UNDEAD, ARTHROPODS
	}
}
