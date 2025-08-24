package net.swofty.pvp.feature.potion;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerTickEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.sound.SoundEvent;
import net.swofty.pvp.entity.projectile.ThrownPotion;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.RegistrableFeature;
import net.swofty.pvp.feature.config.DefinedFeature;
import net.swofty.pvp.feature.config.FeatureConfiguration;
import net.swofty.pvp.feature.effect.EffectFeature;
import net.swofty.pvp.feature.food.ExhaustionFeature;
import net.swofty.pvp.feature.food.FoodFeature;
import net.swofty.pvp.potion.effect.CombatPotionEffect;
import net.swofty.pvp.potion.effect.CombatPotionEffects;
import net.swofty.pvp.utils.ViewUtil;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Vanilla implementation of {@link PotionFeature}
 */
public class VanillaPotionFeature implements PotionFeature, RegistrableFeature {
	public static final DefinedFeature<VanillaPotionFeature> DEFINED = new DefinedFeature<>(
			FeatureType.POTION, VanillaPotionFeature::new,
			FeatureType.EFFECT, FeatureType.EXHAUSTION, FeatureType.FOOD
	);

	private static final int USE_TICKS = 32;

	private final FeatureConfiguration configuration;

	private EffectFeature effectFeature;
	private ExhaustionFeature exhaustionFeature;
	private FoodFeature foodFeature;

	public VanillaPotionFeature(FeatureConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void initDependencies() {
		this.effectFeature = configuration.get(FeatureType.EFFECT);
		this.exhaustionFeature = configuration.get(FeatureType.EXHAUSTION);
		this.foodFeature = configuration.get(FeatureType.FOOD);
	}

	@Override
	public void init(EventNode<EntityInstanceEvent> node) {
		node.addListener(PlayerUseItemEvent.class, event -> {
			if (event.getItemStack().material() == Material.POTION) {
				event.setItemUseTime(USE_TICKS); // Potion use time is always 32 ticks
			}
		});

		node.addListener(PlayerFinishItemUseEvent.class, event -> {
			if (event.getItemStack().material() != Material.POTION) return;

			Player player = event.getPlayer();
			ItemStack stack = event.getItemStack();

			triggerDrinkingSound(player);

			List<Potion> potions = effectFeature.getAllPotions(stack.get(DataComponents.POTION_CONTENTS));

			// Apply the potions
			for (Potion potion : potions) {
				CombatPotionEffect combatPotionEffect = CombatPotionEffects.get(potion.effect());

				if (combatPotionEffect.isInstant()) {
					combatPotionEffect.applyInstantEffect(player, player, player, potion.amplifier(),
							1.0, exhaustionFeature, foodFeature);
				} else {
					player.addEffect(potion);
				}
			}

			if (player.getGameMode() != GameMode.CREATIVE) {
				ItemStack remainder = stack.get(DataComponents.USE_REMAINDER);

				if (remainder != null && !remainder.isAir()) {
					if (stack.amount() == 1) {
						player.setItemInHand(event.getHand(), remainder);
					} else {
						player.setItemInHand(event.getHand(), stack.withAmount(stack.amount() - 1));
						player.getInventory().addItemStack(remainder);
					}
				} else {
					player.setItemInHand(event.getHand(), stack.withAmount(stack.amount() - 1));
				}
			}
		});

		node.addListener(PlayerTickEvent.class, event -> {
			Player player = event.getPlayer();
			if (player.isSilent() || !player.isEating()) return;

			tickDrinkingSounds(player);
		});

		node.addListener(PlayerUseItemEvent.class, event -> {
			if (event.getItemStack().material() != Material.SPLASH_POTION) return;

			ThreadLocalRandom random = ThreadLocalRandom.current();
			ViewUtil.viewersAndSelf(event.getPlayer()).playSound(Sound.sound(
					SoundEvent.ENTITY_SPLASH_POTION_THROW, Sound.Source.PLAYER,
					0.5f, 0.4f / (random.nextFloat() * 0.4f + 0.8f)
			), event.getPlayer());

			throwPotion(event.getPlayer(), event.getItemStack(), event.getHand());
		});

		node.addListener(PlayerUseItemEvent.class, event -> {
			if (event.getItemStack().material() != Material.LINGERING_POTION) return;

			ThreadLocalRandom random = ThreadLocalRandom.current();
			ViewUtil.viewersAndSelf(event.getPlayer()).playSound(Sound.sound(
					SoundEvent.ENTITY_LINGERING_POTION_THROW, Sound.Source.NEUTRAL,
					0.5f, 0.4f / (random.nextFloat() * 0.4f + 0.8f)
			), event.getPlayer());

			throwPotion(event.getPlayer(), event.getItemStack(), event.getHand());
		});
	}

	protected void throwPotion(Player player, ItemStack stack, PlayerHand hand) {
		ThrownPotion thrownPotion = new ThrownPotion(player, effectFeature, false);
		thrownPotion.setItem(stack);

		Pos position = player.getPosition().add(0, player.getEyeHeight(), 0);
		thrownPotion.shootFromRotation(position.pitch(), position.yaw(), -20, 0.5, 1.0);
		thrownPotion.setInstance(Objects.requireNonNull(player.getInstance()), position.withView(thrownPotion.getPosition()));

		Vec playerVel = player.getVelocity();
		thrownPotion.setVelocity(thrownPotion.getVelocity().add(playerVel.x(),
				player.isOnGround() ? 0.0 : playerVel.y(), playerVel.z()));

		if (player.getGameMode() != GameMode.CREATIVE) {
			player.setItemInHand(hand, stack.withAmount(stack.amount() - 1));
		}
	}

	protected void tickDrinkingSounds(Player player) {
		ItemStack stack = player.getItemInHand(Objects.requireNonNull(player.getItemUseHand()));
		if (stack.material() != Material.POTION) return;

		long usedTicks = player.getCurrentItemUseTime();
		long remainingUseTicks = USE_TICKS - usedTicks;

		boolean canTrigger = remainingUseTicks <= USE_TICKS - 7;
		boolean shouldTrigger = canTrigger && remainingUseTicks % 4 == 0;
		if (!shouldTrigger) return;

		triggerDrinkingSound(player);
	}

	protected void triggerDrinkingSound(Player player) {
		ThreadLocalRandom random = ThreadLocalRandom.current();

		player.getViewersAsAudience().playSound(Sound.sound(
				SoundEvent.ENTITY_GENERIC_DRINK, Sound.Source.PLAYER,
				0.5f, random.nextFloat() * 0.1f + 0.9f
		), player);
	}
}