package net.swofty.pvp.enchantment;

import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.registry.RegistryKey;
import net.swofty.pvp.enchantment.enchantments.DamageEnchantment;
import net.swofty.pvp.enchantment.enchantments.ImpalingEnchantment;
import net.swofty.pvp.enchantment.enchantments.ProtectionEnchantment;
import net.swofty.pvp.enchantment.enchantments.ThornsEnchantment;
import net.swofty.pvp.feature.FeatureType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CombatEnchantments {
	private static final Map<RegistryKey<Enchantment>, CombatEnchantment> ENCHANTMENTS = new HashMap<>();
	private static boolean registered = false;

	static {
		registerAll();
	}

	public static CombatEnchantment get(RegistryKey<Enchantment> enchantment) {
		return ENCHANTMENTS.get(enchantment);
	}

	public static void register(CombatEnchantment... enchantments) {
		for (CombatEnchantment enchantment : enchantments) {
			ENCHANTMENTS.put(enchantment.getEnchantment(), enchantment);
		}
	}

	public static FeatureType<?>[] getAllFeatureDependencies() {
		Set<FeatureType<?>> features = new HashSet<>();

		for (CombatEnchantment enchantment : ENCHANTMENTS.values()) {
			features.addAll(enchantment.getDependencies());
		}

		return features.toArray(FeatureType[]::new);
	}

	public static void registerAll() {
		if (registered) return;
		registered = true;

		EquipmentSlot[] ALL_ARMOR_SLOTS = EquipmentSlot.armors().toArray(EquipmentSlot[]::new);

		register(
				new ProtectionEnchantment(Enchantment.PROTECTION, ProtectionEnchantment.Type.ALL, ALL_ARMOR_SLOTS),
				new ProtectionEnchantment(Enchantment.FIRE_PROTECTION, ProtectionEnchantment.Type.FIRE, ALL_ARMOR_SLOTS),
				new ProtectionEnchantment(Enchantment.FEATHER_FALLING, ProtectionEnchantment.Type.FALL, ALL_ARMOR_SLOTS),
				new ProtectionEnchantment(Enchantment.BLAST_PROTECTION, ProtectionEnchantment.Type.EXPLOSION, ALL_ARMOR_SLOTS),
				new ProtectionEnchantment(Enchantment.PROJECTILE_PROTECTION, ProtectionEnchantment.Type.PROJECTILE, ALL_ARMOR_SLOTS),
				new CombatEnchantment(Enchantment.RESPIRATION, ALL_ARMOR_SLOTS),
				new CombatEnchantment(Enchantment.AQUA_AFFINITY, ALL_ARMOR_SLOTS),
				new ThornsEnchantment(ALL_ARMOR_SLOTS),
				new CombatEnchantment(Enchantment.DEPTH_STRIDER, ALL_ARMOR_SLOTS),
				new CombatEnchantment(Enchantment.FROST_WALKER, EquipmentSlot.BOOTS),
				new CombatEnchantment(Enchantment.BINDING_CURSE, ALL_ARMOR_SLOTS),
				new CombatEnchantment(Enchantment.SOUL_SPEED, EquipmentSlot.BOOTS),
				new DamageEnchantment(Enchantment.SHARPNESS, DamageEnchantment.Type.ALL, EquipmentSlot.MAIN_HAND),
				new DamageEnchantment(Enchantment.SMITE, DamageEnchantment.Type.UNDEAD, EquipmentSlot.MAIN_HAND),
				new DamageEnchantment(Enchantment.BANE_OF_ARTHROPODS, DamageEnchantment.Type.ARTHROPODS, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.KNOCKBACK, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.FIRE_ASPECT, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.LOOTING, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.SWEEPING_EDGE, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.EFFICIENCY, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.SILK_TOUCH, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.UNBREAKING, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.FORTUNE, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.POWER, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.PUNCH, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.FLAME, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.INFINITY, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.LUCK_OF_THE_SEA, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.LURE, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.LOYALTY, EquipmentSlot.MAIN_HAND),
				new ImpalingEnchantment(EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.RIPTIDE, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.CHANNELING, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.MULTISHOT, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.QUICK_CHARGE, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.PIERCING, EquipmentSlot.MAIN_HAND),
				new CombatEnchantment(Enchantment.MENDING, EquipmentSlot.values()),
				new CombatEnchantment(Enchantment.VANISHING_CURSE, EquipmentSlot.values())
		);
	}
}
