package net.swofty.pvp.feature.totem;

import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.ConsumeEffect;
import net.minestom.server.item.component.DeathProtection;
import net.swofty.pvp.damage.DamageTypeInfo;
import net.swofty.pvp.events.TotemUseEvent;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.config.DefinedFeature;
import net.swofty.pvp.feature.food.VanillaFoodFeature;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Vanilla implementation of {@link TotemFeature}
 */
public class VanillaTotemFeature implements TotemFeature {
	public static final DefinedFeature<VanillaTotemFeature> DEFINED = new DefinedFeature<>(
			FeatureType.TOTEM, configuration -> new VanillaTotemFeature()
	);

	@Override
	public boolean tryProtect(LivingEntity entity, DamageType type) {
		if (DamageTypeInfo.of(MinecraftServer.getDamageTypeRegistry().getKey(type)).outOfWorld()) return false;

		DeathProtection deathProtection = null;
		for (PlayerHand hand : PlayerHand.values()) {
			ItemStack stack = entity.getItemInHand(hand);
			if (stack.has(DataComponents.DEATH_PROTECTION)) {
				TotemUseEvent totemUseEvent = new TotemUseEvent(entity, hand);
				EventDispatcher.call(totemUseEvent);

				if (totemUseEvent.isCancelled()) continue;

				deathProtection = stack.get(DataComponents.DEATH_PROTECTION);
				entity.setItemInHand(hand, stack.withAmount(stack.amount() - 1));
				break;
			}
		}

		if (deathProtection != null) {
			entity.setHealth(1.0f);

			Random random = ThreadLocalRandom.current();
			for (ConsumeEffect deathEffect : deathProtection.deathEffects()) {
				VanillaFoodFeature.applyConsumeEffect(entity, deathEffect, random);
			}

			// Totem particles
			entity.triggerStatus((byte) 35);
		}

		return deathProtection != null;
	}
}
