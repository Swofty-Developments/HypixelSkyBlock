package net.swofty.pvp.feature.projectile;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.metadata.LivingEntityMeta;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerTickEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.tag.Tag;
import net.swofty.pvp.entity.projectile.AbstractArrow;
import net.swofty.pvp.entity.projectile.Arrow;
import net.swofty.pvp.entity.projectile.SpectralArrow;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.RegistrableFeature;
import net.swofty.pvp.feature.config.DefinedFeature;
import net.swofty.pvp.feature.config.FeatureConfiguration;
import net.swofty.pvp.feature.effect.EffectFeature;
import net.swofty.pvp.feature.enchantment.EnchantmentFeature;
import net.swofty.pvp.feature.item.ItemDamageFeature;
import net.swofty.pvp.utils.ViewUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Vanilla implementation of {@link CrossbowFeature}
 */
public class VanillaCrossbowFeature implements CrossbowFeature, RegistrableFeature {
	public static final DefinedFeature<VanillaCrossbowFeature> DEFINED = new DefinedFeature<>(
			FeatureType.CROSSBOW, VanillaCrossbowFeature::new,
			FeatureType.ITEM_DAMAGE, FeatureType.EFFECT, FeatureType.ENCHANTMENT, FeatureType.PROJECTILE_ITEM
	);

	private static final Tag<Boolean> START_SOUND_PLAYED = Tag.Transient("StartSoundPlayed");
	private static final Tag<Boolean> MID_LOAD_SOUND_PLAYED = Tag.Transient("MidLoadSoundPlayed");

	private final FeatureConfiguration configuration;

	private ItemDamageFeature itemDamageFeature;
	private EffectFeature effectFeature;
	private EnchantmentFeature enchantmentFeature;
	private ProjectileItemFeature projectileItemFeature;

	public VanillaCrossbowFeature(FeatureConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void initDependencies() {
		this.itemDamageFeature = configuration.get(FeatureType.ITEM_DAMAGE);
		this.effectFeature = configuration.get(FeatureType.EFFECT);
		this.enchantmentFeature = configuration.get(FeatureType.ENCHANTMENT);
		this.projectileItemFeature = configuration.get(FeatureType.PROJECTILE_ITEM);
	}

	@Override
	public void init(EventNode<EntityInstanceEvent> node) {
		node.addListener(PlayerUseItemEvent.class, event -> {
			ItemStack stack = event.getItemStack();
			if (stack.material() != Material.CROSSBOW) return;
			Player player = event.getPlayer();

			if (isCrossbowCharged(stack)) {
				// Make sure the animation event is not called, because this is not an animation
				event.setCancelled(true);

				stack = performCrossbowShooting(player, event.getHand(), stack, getCrossbowPower(stack), 1.0);
				player.setItemInHand(event.getHand(), setCrossbowProjectile(stack, null));
			} else {
				if (projectileItemFeature.getCrossbowProjectile(player) == null) {
					event.setCancelled(true);
				} else {
					player.setTag(START_SOUND_PLAYED, false);
					player.setTag(MID_LOAD_SOUND_PLAYED, false);
				}
			}
		});

		node.addListener(PlayerTickEvent.class, event -> {
			Player player = event.getPlayer();

			// If not charging crossbow, return
			LivingEntityMeta meta = (LivingEntityMeta) player.getEntityMeta();
			if (!meta.isHandActive() || player.getItemInHand(meta.getActiveHand()).material() != Material.CROSSBOW)
				return;

			PlayerHand hand = player.getPlayerMeta().getActiveHand();
			ItemStack stack = player.getItemInHand(hand);

			int quickCharge = stack.get(DataComponents.ENCHANTMENTS).level(Enchantment.QUICK_CHARGE);

			long useTicks = player.getCurrentItemUseTime();
			double progress = (getCrossbowUseDuration(stack) - useTicks) / (double) getCrossbowChargeDuration(stack);

			Boolean startSoundPlayed = player.getTag(START_SOUND_PLAYED);
			Boolean midLoadSoundPlayed = player.getTag(MID_LOAD_SOUND_PLAYED);
			if (startSoundPlayed == null) startSoundPlayed = false;
			if (midLoadSoundPlayed == null) midLoadSoundPlayed = false;

			if (progress >= 0.2 && !startSoundPlayed) {
				SoundEvent startSound = getCrossbowStartSound(quickCharge);
				ViewUtil.viewersAndSelf(player).playSound(Sound.sound(
						startSound, Sound.Source.PLAYER,
						0.5f, 1.0f
				), player);

				player.setTag(START_SOUND_PLAYED, true);
				player.setItemInHand(hand, stack);
			}

			SoundEvent midLoadSound = quickCharge == 0 ? SoundEvent.ITEM_CROSSBOW_LOADING_MIDDLE : null;
			if (progress >= 0.5F && midLoadSound != null && !midLoadSoundPlayed) {
				ViewUtil.viewersAndSelf(player).playSound(Sound.sound(
						midLoadSound, Sound.Source.PLAYER,
						0.5f, 1.0f
				), player);

				player.setTag(MID_LOAD_SOUND_PLAYED, true);
				player.setItemInHand(hand, stack);
			}
		});

		node.addListener(PlayerFinishItemUseEvent.class, event -> {
			Player player = event.getPlayer();
			ItemStack stack = event.getItemStack();
			if (stack.material() != Material.CROSSBOW) return;

			int quickCharge = stack.get(DataComponents.ENCHANTMENTS).level(Enchantment.QUICK_CHARGE);

			if (quickCharge < 6) {
				long useTicks = player.getCurrentItemUseTime();
				double power = getCrossbowPowerForTime(useTicks, stack);
				if (!(power >= 1.0F) || isCrossbowCharged(stack))
					return;
			}

			stack = loadCrossbowProjectiles(player, stack);
			if (stack == null) return;

			ThreadLocalRandom random = ThreadLocalRandom.current();
			ViewUtil.viewersAndSelf(player).playSound(Sound.sound(
					SoundEvent.ITEM_CROSSBOW_LOADING_END, Sound.Source.PLAYER,
					1.0f, 1.0f / (random.nextFloat() * 0.5f + 1.0f) + 0.2f
			), player);

			player.setItemInHand(event.getHand(), stack);
		});
	}

	protected AbstractArrow createArrow(ItemStack stack, @Nullable Entity shooter) {
		if (stack.material() == Material.SPECTRAL_ARROW) {
			return new SpectralArrow(shooter, enchantmentFeature);
		} else {
			Arrow arrow = new Arrow(shooter, effectFeature, enchantmentFeature);
			arrow.setItemStack(stack);
			return arrow;
		}
	}

	protected double getCrossbowPower(ItemStack stack) {
		return crossbowContainsProjectile(stack, Material.FIREWORK_ROCKET) ? 1.6 : 3.15;
	}

	protected double getCrossbowPowerForTime(long ticks, ItemStack stack) {
		double power = ticks / (double) getCrossbowChargeDuration(stack);
		if (power > 1) {
			power = 1;
		}

		return power;
	}

	protected boolean isCrossbowCharged(ItemStack stack) {
		return stack.has(DataComponents.CHARGED_PROJECTILES) &&
				!Objects.requireNonNull(stack.get(DataComponents.CHARGED_PROJECTILES)).isEmpty();
	}

	protected ItemStack setCrossbowProjectile(ItemStack stack, @Nullable ItemStack projectile) {
		return stack.with(DataComponents.CHARGED_PROJECTILES, projectile == null ? List.of() : List.of(projectile));
	}

	protected ItemStack setCrossbowProjectiles(ItemStack stack, ItemStack projectile1,
											   ItemStack projectile2, ItemStack projectile3) {
		return stack.with(DataComponents.CHARGED_PROJECTILES, List.of(projectile1, projectile2, projectile3));
	}

	protected boolean crossbowContainsProjectile(ItemStack stack, Material projectile) {
		List<ItemStack> projectiles = stack.get(DataComponents.CHARGED_PROJECTILES);
		if (projectiles == null) return false;

		for (ItemStack itemStack : projectiles) {
			if (itemStack.material() == projectile) return true;
		}

		return false;
	}

	protected int getCrossbowUseDuration(ItemStack stack) {
		return getCrossbowChargeDuration(stack) + 3;
	}

	protected int getCrossbowChargeDuration(ItemStack stack) {
		int quickCharge = stack.get(DataComponents.ENCHANTMENTS).level(Enchantment.QUICK_CHARGE);
		return quickCharge == 0 ? 25 : 25 - 5 * quickCharge;
	}

	protected SoundEvent getCrossbowStartSound(int quickCharge) {
		return switch (quickCharge) {
			case 1 -> SoundEvent.ITEM_CROSSBOW_QUICK_CHARGE_1;
			case 2 -> SoundEvent.ITEM_CROSSBOW_QUICK_CHARGE_2;
			case 3 -> SoundEvent.ITEM_CROSSBOW_QUICK_CHARGE_3;
			default -> SoundEvent.ITEM_CROSSBOW_LOADING_START;
		};
	}

	protected ItemStack loadCrossbowProjectiles(Player player, ItemStack stack) {
		boolean multiShot = stack.get(DataComponents.ENCHANTMENTS).level(Enchantment.MULTISHOT) > 0;

		ItemStack projectileItem;
		int projectileSlot;

		ProjectileItemFeature.ProjectileItem projectile = projectileItemFeature.getCrossbowProjectile(player);
		if (projectile == null && player.getGameMode() == GameMode.CREATIVE) {
			projectileItem = Arrow.DEFAULT_ARROW;
			projectileSlot = -1;
		} else if (projectile != null) {
			projectileItem = projectile.stack();
			projectileSlot = projectile.slot();
		} else {
			// Should not happen
			return ItemStack.AIR;
		}

		if (multiShot) {
			stack = setCrossbowProjectiles(stack, projectileItem, projectileItem, projectileItem);
		} else {
			stack = setCrossbowProjectile(stack, projectileItem);
		}

		if (player.getGameMode() != GameMode.CREATIVE && projectileSlot >= 0) {
			player.getInventory().setItemStack(projectileSlot, projectileItem.withAmount(projectileItem.amount() - 1));
		}

		return stack;
	}

	protected ItemStack performCrossbowShooting(Player player, PlayerHand hand, ItemStack stack,
												double power, double spread) {
		List<ItemStack> projectiles = stack.get(DataComponents.CHARGED_PROJECTILES);
		if (projectiles == null || projectiles.isEmpty()) return ItemStack.AIR;

		ItemStack projectile = projectiles.getFirst();
		if (!projectile.isAir()) {
			shootCrossbowProjectile(player, hand, stack, projectile, 1.0F, power, spread, 0.0F);
		}

		if (projectiles.size() > 2) {
			ThreadLocalRandom random = ThreadLocalRandom.current();
			boolean firstHighPitch = random.nextBoolean();
			float firstPitch = getRandomShotPitch(firstHighPitch, random);
			float secondPitch = getRandomShotPitch(!firstHighPitch, random);

			projectile = projectiles.get(1);
			if (!projectile.isAir()) {
				shootCrossbowProjectile(player, hand, stack, projectile, firstPitch, power, spread, -10.0F);
			}
			projectile = projectiles.get(2);
			if (!projectile.isAir()) {
				shootCrossbowProjectile(player, hand, stack, projectile, secondPitch, power, spread, 10.0F);
			}
		}

		return setCrossbowProjectile(stack, ItemStack.AIR);
	}

	protected void shootCrossbowProjectile(Player player, PlayerHand hand, ItemStack crossbowStack,
										   ItemStack projectile, float soundPitch,
										   double power, double spread, float yaw) {
		boolean firework = projectile.material() == Material.FIREWORK_ROCKET;
		if (firework) return; //TODO firework

		AbstractArrow arrow = getCrossbowArrow(player, crossbowStack, projectile);
		if (player.getGameMode() == GameMode.CREATIVE || yaw != 0.0) {
			arrow.setPickupMode(AbstractArrow.PickupMode.CREATIVE_ONLY);
		}

		//TODO fix velocity and yaw
		Pos position = player.getPosition().add(0, player.getEyeHeight() - 0.1, 0);
		arrow.setInstance(Objects.requireNonNull(player.getInstance()), position);

		position = position.withYaw(position.yaw() + yaw);
		//Vec direction = position.direction();
		//position = position.add(direction).sub(0, 0.2, 0); //????????

		//TODO probably use shootFromRotation
		arrow.shootFrom(position, power, spread);

		itemDamageFeature.damageEquipment(player, hand == PlayerHand.MAIN ?
				EquipmentSlot.MAIN_HAND : EquipmentSlot.OFF_HAND, firework ? 3 : 1);

		ViewUtil.viewersAndSelf(player).playSound(Sound.sound(
				SoundEvent.ITEM_CROSSBOW_SHOOT, Sound.Source.PLAYER,
				1.0f, soundPitch
		), player);
	}

	protected AbstractArrow getCrossbowArrow(Player player, ItemStack crossbowStack, ItemStack projectile) {
		AbstractArrow arrow = createArrow(projectile.withAmount(1), player);
		arrow.setCritical(true); // Player shooter is always critical
		arrow.setSound(SoundEvent.ITEM_CROSSBOW_HIT);

		int piercing = crossbowStack.get(DataComponents.ENCHANTMENTS).level(Enchantment.PIERCING);
		if (piercing > 0) {
			arrow.setPiercingLevel((byte) piercing);
		}

		return arrow;
	}

	protected float getRandomShotPitch(boolean high, ThreadLocalRandom random) {
		float base = high ? 0.63F : 0.43F;
		return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + base;
	}
}