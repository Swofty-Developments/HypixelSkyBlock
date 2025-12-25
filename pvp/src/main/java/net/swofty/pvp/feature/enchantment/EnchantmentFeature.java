package net.swofty.pvp.feature.enchantment;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.registry.RegistryKey;
import net.swofty.pvp.enchantment.EntityGroup;
import net.swofty.pvp.feature.CombatFeature;

import java.util.Map;

/**
 * Combat feature which manages enchantments.
 * <p>
 * It contains methods which other features depend on,
 * such as methods for the protection amount of armor enchantments and the attack damage amount of weapon enchantments.
 */
public interface EnchantmentFeature extends CombatFeature {
	EnchantmentFeature NO_OP = new EnchantmentFeature() {
		@Override
		public int getEquipmentLevel(LivingEntity entity, RegistryKey<Enchantment> enchantment) {
			return 0;
		}

		@Override
		public Map.Entry<EquipmentSlot, ItemStack> pickRandom(LivingEntity entity, RegistryKey<Enchantment> enchantment) {
			return null;
		}

		@Override
		public int getProtectionAmount(LivingEntity entity, DamageType damageType) {
			return 0;
		}

		@Override
		public float getAttackDamage(ItemStack stack, EntityGroup group) {
			return 0;
		}

		@Override
		public double getExplosionKnockback(LivingEntity entity, double strength) {
			return strength;
		}

		@Override
		public int getFireDuration(LivingEntity entity, int duration) {
			return duration;
		}

		@Override
		public int getKnockback(LivingEntity entity) {
			return 0;
		}

		@Override
		public int getSweeping(LivingEntity entity) {
			return 0;
		}

		@Override
		public int getFireAspect(LivingEntity entity) {
			return 0;
		}

		@Override
		public boolean shouldUnbreakingPreventDamage(ItemStack stack) {
			return false;
		}

		@Override
		public void onUserDamaged(LivingEntity user, LivingEntity attacker) {
		}

		@Override
		public void onTargetDamaged(LivingEntity user, Entity target) {
		}
	};

	/**
	 * Gets the highest level of en enchantment on an entity's equipment.
	 *
	 * @param entity      the entity for which to determine the equipment level
	 * @param enchantment the enchantment for which to determine the equipment level
	 * @return the equipment level
	 */
	int getEquipmentLevel(LivingEntity entity, RegistryKey<Enchantment> enchantment);

	/**
	 * Picks a random equipment piece which has the specified enchantment.
	 *
	 * @param entity      the entity to pick equipment from
	 * @param enchantment the enchantment which the equipment should have
	 * @return a map entry containing both the equipment slot and the item stack of the equipment piece
	 */
	Map.Entry<EquipmentSlot, ItemStack> pickRandom(LivingEntity entity, RegistryKey<Enchantment> enchantment);

	int getProtectionAmount(LivingEntity entity, DamageType damageType);

	float getAttackDamage(ItemStack stack, EntityGroup group);

	double getExplosionKnockback(LivingEntity entity, double strength);

	int getFireDuration(LivingEntity entity, int duration);

	int getKnockback(LivingEntity entity);

	int getSweeping(LivingEntity entity);

	int getFireAspect(LivingEntity entity);

	boolean shouldUnbreakingPreventDamage(ItemStack stack);

	/**
	 * Handles an entity being damaged by an attacker. Usually applies thorns.
	 *
	 * @param user     the entity being damaged
	 * @param attacker the attacker
	 */
	void onUserDamaged(LivingEntity user, LivingEntity attacker);

	/**
	 * Handles an entity damaging another entity.
	 * Some enchantments could for example add extra effects to the target.
	 *
	 * @param user   the attacker
	 * @param target the target
	 */
	void onTargetDamaged(LivingEntity user, Entity target);
}