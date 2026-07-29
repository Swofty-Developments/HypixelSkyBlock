package net.swofty.pvp.feature.projectile;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.entity.metadata.LivingEntityMeta;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.PlayerBeginItemUseEvent;
import net.minestom.server.event.item.PlayerCancelItemUseEvent;
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
import net.swofty.pvp.entity.projectile.FireworkRocketEntity;
import net.swofty.pvp.entity.projectile.SpectralArrow;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.RegistrableFeature;
import net.swofty.pvp.feature.block.BlockFeature;
import net.swofty.pvp.feature.config.DefinedFeature;
import net.swofty.pvp.feature.config.FeatureConfiguration;
import net.swofty.pvp.feature.effect.EffectFeature;
import net.swofty.pvp.feature.enchantment.EnchantmentFeature;
import net.swofty.pvp.feature.item.ItemDamageFeature;
import net.swofty.pvp.utils.ViewUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Vanilla implementation of {@link CrossbowFeature}
 */
public class VanillaCrossbowFeature implements CrossbowFeature, RegistrableFeature {
    public static final DefinedFeature<VanillaCrossbowFeature> DEFINED = new DefinedFeature<>(
        FeatureType.CROSSBOW, VanillaCrossbowFeature::new,
        FeatureType.ITEM_DAMAGE, FeatureType.EFFECT, FeatureType.ENCHANTMENT, FeatureType.PROJECTILE_ITEM, FeatureType.BLOCK
    );

    private static final Tag<@NotNull Boolean> START_SOUND_PLAYED = Tag.Transient("StartSoundPlayed");
    private static final Tag<@NotNull Boolean> MID_LOAD_SOUND_PLAYED = Tag.Transient("MidLoadSoundPlayed");

    private static final double DEFAULT_POWER = 3.15;
    private static final double FIREWORK_POWER = 1.6;
    private static final float DEFAULT_SPREAD = 1.0f;

    private final FeatureConfiguration configuration;

    private ItemDamageFeature itemDamageFeature;
    private EffectFeature effectFeature;
    private EnchantmentFeature enchantmentFeature;
    private ProjectileItemFeature projectileItemFeature;
    private BlockFeature blockFeature;

    public VanillaCrossbowFeature(FeatureConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void initDependencies() {
        this.itemDamageFeature = configuration.get(FeatureType.ITEM_DAMAGE);
        this.effectFeature = configuration.get(FeatureType.EFFECT);
        this.enchantmentFeature = configuration.get(FeatureType.ENCHANTMENT);
        this.projectileItemFeature = configuration.get(FeatureType.PROJECTILE_ITEM);
        this.blockFeature = configuration.get(FeatureType.BLOCK);
    }

    @Override
    public void init(EventNode<@NotNull EntityInstanceEvent> node) {

        // Player Loading the crossbow
        node.addListener(PlayerBeginItemUseEvent.class, event -> {
            var itemStack = event.getPlayer().getItemInHand(event.getHand());
            var player = event.getPlayer();
            if (itemStack.material() != Material.CROSSBOW) return;

            if (!isCrossbowCharged(itemStack) && projectileItemFeature.getCrossbowProjectile(player) == null && player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
            }
        });

        node.addListener(PlayerTickEvent.class, event -> {
            Player player = event.getPlayer();
            var meta = (LivingEntityMeta) player.getEntityMeta();

            // Check if player is actively charging a crossbow
            if (!meta.isHandActive()) return;

            PlayerHand hand = meta.getActiveHand();
            ItemStack stack = player.getItemInHand(hand);

            if (stack.material() != Material.CROSSBOW) return;
            if (isCrossbowCharged(stack)) return; // Already loaded

            // Calculate progress based on use time
            long useTicks = player.getCurrentItemUseTime();
            int chargeDuration = getCrossbowChargeDuration(stack);
            double progress = useTicks / (double) chargeDuration;

            int quickCharge = Objects.requireNonNull(stack.get(DataComponents.ENCHANTMENTS)).level(Enchantment.QUICK_CHARGE);

            Boolean startSoundPlayed = player.getTag(START_SOUND_PLAYED);
            if (progress >= 0.2 && (startSoundPlayed == null || !startSoundPlayed)) {
                SoundEvent startSound = getCrossbowStartSound(quickCharge);
                ViewUtil.viewersAndSelf(player).playSound(Sound.sound(
                    startSound, Sound.Source.PLAYER,
                    0.5f, 1.0f
                ), player);
                player.setTag(START_SOUND_PLAYED, true);
            }

            Boolean midLoadSoundPlayed = player.getTag(MID_LOAD_SOUND_PLAYED);
            SoundEvent midLoadSound = quickCharge == 0 ? SoundEvent.ITEM_CROSSBOW_LOADING_MIDDLE : null;
            if (progress >= 0.5 && midLoadSound != null && (midLoadSoundPlayed == null || !midLoadSoundPlayed)) {
                ViewUtil.viewersAndSelf(player).playSound(Sound.sound(
                    midLoadSound, Sound.Source.PLAYER,
                    0.5f, 1.0f
                ), player);
                player.setTag(MID_LOAD_SOUND_PLAYED, true);
            }

            if (progress >= 1) {
                var chargedCrossbow = loadCrossbowProjectiles(player, stack);
                player.setItemInHand(hand, chargedCrossbow);
                event.getInstance().playSound(Sound.sound()
                        .type(SoundEvent.ITEM_CROSSBOW_LOADING_END)
                        .build(),
                    event.getPlayer().getPosition());
                player.removeTag(START_SOUND_PLAYED);
                player.removeTag(MID_LOAD_SOUND_PLAYED);
            }
        });

        node.addListener(PlayerCancelItemUseEvent.class, event -> {
            var player = event.getPlayer();

            player.removeTag(START_SOUND_PLAYED);
            player.removeTag(MID_LOAD_SOUND_PLAYED);
        });

        // Shooting projectile from crossbow
        node.addListener(PlayerUseItemEvent.class, event -> {
            var player = event.getPlayer();
            var itemStack = event.getItemStack();
            if (itemStack.material() != Material.CROSSBOW) return;
            if (!isCrossbowCharged(itemStack)) return;

            performCrossbowShooting(player, event.getHand(), itemStack);
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

    protected boolean isCrossbowCharged(ItemStack stack) {
        return stack.has(DataComponents.CHARGED_PROJECTILES) &&
            !Objects.requireNonNull(stack.get(DataComponents.CHARGED_PROJECTILES)).isEmpty();
    }

    protected ItemStack setCrossbowProjectile(ItemStack stack, @NotNull List<ItemStack> projectiles) {
        return stack.with(DataComponents.CHARGED_PROJECTILES, projectiles);
    }

    protected ItemStack setCrossbowProjectile(ItemStack stack, @NotNull ItemStack projectile) {
        return stack.with(DataComponents.CHARGED_PROJECTILES, List.of(projectile));
    }

    protected boolean crossbowContainsProjectile(ItemStack stack, Material projectile) {
        List<ItemStack> projectiles = stack.get(DataComponents.CHARGED_PROJECTILES);
        if (projectiles == null) return false;

        for (ItemStack itemStack : projectiles) {
            if (itemStack.material() == projectile) return true;
        }

        return false;
    }

    protected int getCrossbowChargeDuration(ItemStack stack) {
        int quickCharge = Objects.requireNonNull(stack.get(DataComponents.ENCHANTMENTS)).level(Enchantment.QUICK_CHARGE);
        return Math.max(0, quickCharge == 0 ? 25 : 25 - 5 * quickCharge);
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
        int multiShot = Objects.requireNonNull(stack.get(DataComponents.ENCHANTMENTS)).level(Enchantment.MULTISHOT);

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

        ArrayList<ItemStack> projectiles = new ArrayList<>(List.of(projectileItem));
        if (multiShot > 0) {
            for (int i = 0; i < multiShot; i++) {
                projectiles.add(projectileItem);
                projectiles.add(projectileItem);
            }
        }
        stack = setCrossbowProjectile(stack, projectiles);

        if (player.getGameMode() != GameMode.CREATIVE && projectileSlot >= 0) {
            player.getInventory().setItemStack(projectileSlot, projectileItem.withAmount(projectileItem.amount() - 1));
        }

        return stack;
    }

    protected void performCrossbowShooting(Player player, PlayerHand hand, ItemStack stack) {
        List<ItemStack> projectiles = stack.get(DataComponents.CHARGED_PROJECTILES);
        if (projectiles == null || projectiles.isEmpty()) return;

        var offsetYaw = 0.0f;
        for (int turn = 0; turn < projectiles.size(); turn++) {
            var projectile = projectiles.get(turn);

            var soundPitch = getSoundPitch(ThreadLocalRandom.current(), turn);

            if (turn == 0) {
                shootCrossbowProjectile(player, hand, stack, projectile, soundPitch, offsetYaw);
                offsetYaw += 10.0f;
            } else {
                if (turn % 2 == 1) {
                    shootCrossbowProjectile(player, hand, stack, projectile, soundPitch, offsetYaw);
                }
                if (turn % 2 == 0) {
                    shootCrossbowProjectile(player, hand, stack, projectile, soundPitch, -offsetYaw);
                    offsetYaw += 10.0f;
                }
            }
        }

        player.setItemInHand(hand, setCrossbowProjectile(player.getItemInHand(hand), List.of()));
    }

    protected void shootCrossbowProjectile(Player player, PlayerHand hand, ItemStack crossbowStack,
                                           ItemStack projectile, float soundPitch,
                                           float yaw) {
        boolean isFirework = projectile.material() == Material.FIREWORK_ROCKET;
        if (isFirework) {
            FireworkRocketEntity firework = getFireworkRocket(player, projectile);

            Pos position = player.getPosition().add(0, player.getEyeHeight() - 0.1, 0);
            position = position.add(position.direction().normalize().mul(0.1));

            firework.setInstance(Objects.requireNonNull(player.getInstance()), position);

            firework.shootFromRotation(position.pitch(), position.yaw(), 0, FIREWORK_POWER, DEFAULT_SPREAD, yaw);
            firework.setVelocity(firework.getVelocity()
                .add(getRandomOffset(), getRandomOffset(), getRandomOffset())
            );
        } else {
            AbstractArrow arrow = getCrossbowArrow(player, crossbowStack, projectile);
            if (player.getGameMode() == GameMode.CREATIVE || yaw != 0.0) {
                arrow.setPickupMode(AbstractArrow.PickupMode.CREATIVE_ONLY);
            }

            Pos position = player.getPosition().add(0, player.getEyeHeight() - 0.1, 0);

            arrow.setInstance(Objects.requireNonNull(player.getInstance()), position);

            arrow.shootFromRotation(position.pitch(), position.yaw(), 0, DEFAULT_POWER, DEFAULT_SPREAD, yaw);
            arrow.setVelocity(arrow.getVelocity()
                .add(getRandomOffset(), getRandomOffset(), getRandomOffset())
            );

            var direction = player.getPosition().direction().normalize();
            arrow.setView(
                (float) Math.toDegrees(Math.atan2(direction.x(), direction.z())),
                (float) Math.toDegrees(Math.asin(direction.y()))
            );
        }

        itemDamageFeature.damageEquipment(player,
            hand == PlayerHand.MAIN ? EquipmentSlot.MAIN_HAND : EquipmentSlot.OFF_HAND,
            isFirework ? 3 : 1);

        ViewUtil.viewersAndSelf(player).playSound(Sound.sound(
            SoundEvent.ITEM_CROSSBOW_SHOOT, Sound.Source.PLAYER,
            1.0f, soundPitch
        ), player);
    }

    protected double getRandomOffset() {
        return ThreadLocalRandom.current().nextDouble(-0.4, 0.4);
    }

    // Multishot shoot sound being higher pitched or lower pitched randomly
    private static float getSoundPitch(ThreadLocalRandom random, int index) {
        return index == 0 ? 1.0F : getSoundPitch((index & 1) == 1, random);
    }

    private static float getSoundPitch(boolean flag, ThreadLocalRandom random) {
        float pitchOffset = flag ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + pitchOffset;
    }

    protected AbstractArrow getCrossbowArrow(Player player, ItemStack crossbowStack, ItemStack projectile) {
        AbstractArrow arrow = createArrow(projectile.withAmount(1), player);
        arrow.setCritical(true); // Player shooter is always critical
        arrow.setSound(SoundEvent.ITEM_CROSSBOW_HIT);

        int piercing = Objects.requireNonNull(crossbowStack.get(DataComponents.ENCHANTMENTS)).level(Enchantment.PIERCING);
        if (piercing > 0) {
            arrow.setPiercingLevel((byte) piercing);
        }

        return arrow;
    }

    protected FireworkRocketEntity getFireworkRocket(Player player, ItemStack projectile) {
        return new FireworkRocketEntity(player, projectile, true, blockFeature);
    }

}