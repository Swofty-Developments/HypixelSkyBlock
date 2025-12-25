package net.swofty.pvp.enchantment.enchantments;

import net.swofty.pvp.enchantment.CombatEnchantment;
import net.swofty.pvp.enchantment.EntityGroup;
import net.swofty.pvp.feature.config.FeatureConfiguration;
import net.swofty.pvp.feature.enchantment.EnchantmentFeature;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.enchant.Enchantment;

public class ImpalingEnchantment extends CombatEnchantment {
	public ImpalingEnchantment(EquipmentSlot... slotTypes) {
		super(Enchantment.IMPALING, slotTypes);
	}
	
	@Override
	public float getAttackDamage(int level, EntityGroup group,
	                             EnchantmentFeature feature, FeatureConfiguration configuration) {
		return group == EntityGroup.AQUATIC ? (float) level * 2.5F : 0.0F;
	}
}
