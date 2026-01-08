package net.swofty.type.skywarsgame.luckyblock.spawns;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.metadata.monster.zombie.ZombieMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.time.TimeUnit;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.time.Duration;
import java.util.*;

/**
 * Handles spawning of mobs and complex spawn effects from Lucky Blocks.
 */
public class LuckyBlockSpawnHandler {

    private static final Random RANDOM = new Random();
    private static final Duration MOB_DESPAWN_TIME = Duration.ofSeconds(60);

    private final Map<UUID, RideableMob> activeRideables = new HashMap<>();
    private final List<EntityCreature> spawnedMobs = new ArrayList<>();

    /**
     * Spawn a zombie army to assist the player.
     */
    public void spawnZombieArmy(SkywarsPlayer player, Instance instance, Pos position) {
        player.sendMessage(Component.text("A zombie army rises to help you!", NamedTextColor.GREEN));

        for (int i = 0; i < 5; i++) {
            double offsetX = RANDOM.nextDouble() * 4 - 2;
            double offsetZ = RANDOM.nextDouble() * 4 - 2;
            Pos spawnPos = position.add(offsetX, 0, offsetZ);

            EntityCreature zombie = new EntityCreature(EntityType.ZOMBIE);
            zombie.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.23);
            zombie.setCustomName(Component.text(player.getUsername() + "'s Zombie", NamedTextColor.GREEN));
            zombie.setCustomNameVisible(true);

            // Add AI to target enemies (not the owner)
            zombie.addAIGroup(
                    List.of(new MeleeAttackGoal(zombie, 1.0, 20, TimeUnit.SERVER_TICK)),
                    List.of(new ClosestEntityTarget(zombie, 32,
                            entity -> entity instanceof SkywarsPlayer && entity != player))
            );

            zombie.setInstance(instance, spawnPos);
            spawnedMobs.add(zombie);

            // Auto-despawn
            zombie.scheduler().buildTask(zombie::remove)
                    .delay(MOB_DESPAWN_TIME)
                    .schedule();
        }
    }

    /**
     * Spawn three friendly wolves.
     */
    public void spawnWolves(SkywarsPlayer player, Instance instance, Pos position) {
        player.sendMessage(Component.text("Three loyal wolves join you!", NamedTextColor.GREEN));

        String[] names = {"Fido", "Rex", "Buddy"};

        for (int i = 0; i < 3; i++) {
            double offsetX = RANDOM.nextDouble() * 3 - 1.5;
            double offsetZ = RANDOM.nextDouble() * 3 - 1.5;
            Pos spawnPos = position.add(offsetX, 0, offsetZ);

            EntityCreature wolf = new EntityCreature(EntityType.WOLF);
            wolf.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.3);
            wolf.setCustomName(Component.text(names[i], NamedTextColor.WHITE));
            wolf.setCustomNameVisible(true);

            // Add AI to target enemies
            wolf.addAIGroup(
                    List.of(new MeleeAttackGoal(wolf, 1.0, 15, TimeUnit.SERVER_TICK)),
                    List.of(new ClosestEntityTarget(wolf, 24,
                            entity -> entity instanceof SkywarsPlayer && entity != player))
            );

            wolf.setInstance(instance, spawnPos);
            spawnedMobs.add(wolf);

            wolf.scheduler().buildTask(wolf::remove)
                    .delay(MOB_DESPAWN_TIME)
                    .schedule();
        }
    }

    /**
     * Spawn multiple hostile slimes to make up for inability to set size.
     */
    public void spawnBigSlime(SkywarsPlayer player, Instance instance, Pos position) {
        player.sendMessage(Component.text("SLIMES ARE ATTACKING!", NamedTextColor.RED));

        // Spawn multiple slimes since we can't modify size easily
        for (int i = 0; i < 4; i++) {
            double offsetX = RANDOM.nextDouble() * 3 - 1.5;
            double offsetZ = RANDOM.nextDouble() * 3 - 1.5;
            Pos spawnPos = position.add(offsetX, 0, offsetZ);

            EntityCreature slime = new EntityCreature(EntityType.SLIME);
            slime.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.2);
            slime.setCustomName(Component.text("Slime", NamedTextColor.GREEN));
            slime.setCustomNameVisible(true);

            // Target the player who opened the block
            slime.addAIGroup(
                    List.of(new MeleeAttackGoal(slime, 1.0, 30, TimeUnit.SERVER_TICK)),
                    List.of(new ClosestEntityTarget(slime, 48, entity -> entity == player))
            );

            slime.setInstance(instance, spawnPos);
            spawnedMobs.add(slime);

            slime.scheduler().buildTask(slime::remove)
                    .delay(MOB_DESPAWN_TIME)
                    .schedule();
        }
    }

    /**
     * Spawn the Golden Child - a baby zombie in gold armor.
     */
    public void spawnGoldenChild(SkywarsPlayer player, Instance instance, Pos position) {
        player.sendMessage(Component.text("THE GOLDEN CHILD HAS ARRIVED!", NamedTextColor.GOLD));

        EntityCreature babyZombie = new EntityCreature(EntityType.ZOMBIE);
        // Baby zombies are faster than adults (0.46 vs 0.23)
        babyZombie.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.46);
        babyZombie.setCustomName(Component.text("The Golden Child", NamedTextColor.GOLD));
        babyZombie.setCustomNameVisible(true);

        if (babyZombie.getEntityMeta() instanceof ZombieMeta zombieMeta) {
            zombieMeta.setBaby(true);
        }

        // Give gold armor
        babyZombie.setHelmet(ItemStack.of(Material.GOLDEN_HELMET));
        babyZombie.setChestplate(ItemStack.of(Material.GOLDEN_CHESTPLATE));
        babyZombie.setLeggings(ItemStack.of(Material.GOLDEN_LEGGINGS));
        babyZombie.setBoots(ItemStack.of(Material.GOLDEN_BOOTS));
        babyZombie.setItemInMainHand(ItemStack.of(Material.GOLDEN_SWORD));

        // Target the player
        babyZombie.addAIGroup(
                List.of(new MeleeAttackGoal(babyZombie, 1.0, 15, TimeUnit.SERVER_TICK)),
                List.of(new ClosestEntityTarget(babyZombie, 32, entity -> entity == player))
        );

        babyZombie.setInstance(instance, position);
        spawnedMobs.add(babyZombie);

        babyZombie.scheduler().buildTask(babyZombie::remove)
                .delay(MOB_DESPAWN_TIME)
                .schedule();
    }

    /**
     * Spawn a rideable horse for the player.
     */
    public void spawnRideableHorse(SkywarsPlayer player, Instance instance, Pos position) {
        RideableHorse horse = new RideableHorse(player, instance);
        horse.spawn(position);
        activeRideables.put(player.getUuid(), horse);
    }

    /**
     * Spawn a rideable blaze for the player.
     */
    public void spawnRidingBlaze(SkywarsPlayer player, Instance instance, Pos position) {
        RidingBlaze blaze = new RidingBlaze(player, instance);
        blaze.spawn(position);
        activeRideables.put(player.getUuid(), blaze);
    }

    /**
     * Spawn a rideable wither for the player.
     */
    public void spawnRidingWither(SkywarsPlayer player, Instance instance, Pos position) {
        RidingWither wither = new RidingWither(player, instance);
        wither.spawn(position);
        activeRideables.put(player.getUuid(), wither);
    }

    /**
     * Handle player sneak for dismounting.
     */
    public void handlePlayerSneak(SkywarsPlayer player) {
        RideableMob mount = activeRideables.get(player.getUuid());
        if (mount != null && mount.isActive()) {
            mount.handleSneak();
            activeRideables.remove(player.getUuid());
        }
    }

    /**
     * Handle player right-click while riding (for shooting projectiles).
     */
    public void handleRidingAttack(SkywarsPlayer player) {
        RideableMob mount = activeRideables.get(player.getUuid());
        if (mount == null || !mount.isActive()) return;

        if (mount instanceof RidingBlaze blaze) {
            blaze.fireFireball();
        } else if (mount instanceof RidingWither wither) {
            wither.fireWitherSkull();
        }
    }

    /**
     * Get the active rideable for a player, if any.
     */
    public RideableMob getActiveRideable(UUID playerUuid) {
        return activeRideables.get(playerUuid);
    }

    /**
     * Clean up all spawned entities.
     */
    public void cleanup() {
        for (RideableMob rideable : activeRideables.values()) {
            if (rideable.isActive()) {
                rideable.remove();
            }
        }
        activeRideables.clear();

        for (EntityCreature mob : spawnedMobs) {
            if (!mob.isRemoved()) {
                mob.remove();
            }
        }
        spawnedMobs.clear();
    }
}
