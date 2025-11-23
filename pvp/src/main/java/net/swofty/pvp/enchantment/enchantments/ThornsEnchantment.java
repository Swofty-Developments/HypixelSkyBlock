package net.swofty.pvp.enchantment.enchantments;

import net.swofty.pvp.enchantment.CombatEnchantment;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.config.FeatureConfiguration;
import net.swofty.pvp.feature.enchantment.EnchantmentFeature;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.enchant.Enchantment;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ThornsEnchantment extends CombatEnchantment {
	public ThornsEnchantment(EquipmentSlot... slotTypes) {
		super(Enchantment.THORNS, Set.of(FeatureType.ITEM_DAMAGE), slotTypes);
	}
	
	@Override
	public void onUserDamaged(LivingEntity user, LivingEntity attacker, int level,
	                          EnchantmentFeature feature, FeatureConfiguration configuration) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		if (!shouldDamageAttacker(level, random)) return;
		
		Map.Entry<EquipmentSlot, ItemStack> entry = feature.pickRandom(user, Enchantment.THORNS);
		
		if (attacker != null) {
			attacker.damage(new Damage(DamageType.THORNS, user, user, null, getDamageAmount(level, random)));
		}
		
		if (entry != null) {
			configuration.get(FeatureType.ITEM_DAMAGE).damageEquipment(user, entry.getKey(), 2);
		}
	}
	
	private static boolean shouldDamageAttacker(int level, ThreadLocalRandom random) {
		if (level <= 0) return false;
		return random.nextFloat() < 0.15f * level;
	}
	
	private static int getDamageAmount(int level, ThreadLocalRandom random) {
		return level > 10 ? level - 10 : 1 + random.nextInt(4);
	}
}
