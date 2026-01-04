package net.swofty.type.skywarsgame.manager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.attribute.Attribute;
import net.swofty.type.skywarsgame.entity.DragonEntity;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.skywarsgame.game.SkywarsGame;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.UUID;
import java.util.function.Consumer;

public class DragonManager {
    private static final double CIRCLE_RADIUS = 35;
    private static final double CIRCLE_HEIGHT = 25;
    private static final double CIRCLE_SPEED = 0.03;
    private static final double FLIGHT_SPEED = 0.8;
    private static final double DIVE_SPEED = 1.2;
    private static final double RETURN_SPEED = 0.6;
    private static final long DIVE_COOLDOWN_MS = 12000;
    private static final double ATTACK_RANGE = 6.0;
    private static final float ATTACK_DAMAGE = 10f;

    private final SkywarsGame game;
    private final Instance instance;
    private final Pos centerPos;
    private DragonEntity dragon;
    private Task behaviorTask;
    private boolean dragonSpawned = false;

    private enum DragonState { CIRCLING, DIVING, RETURNING }
    private DragonState state = DragonState.CIRCLING;

    private double circleAngle = 0;
    private SkywarsPlayer diveTarget = null;
    private long lastDiveTime = 0;
    private long diveStartTime = 0;
    private Consumer<Component> broadcaster;

    public DragonManager(SkywarsGame game, Instance instance, Pos centerPos) {
        this.game = game;
        this.instance = instance;
        this.centerPos = centerPos;
    }

    public void spawnDragonNow(Consumer<Component> broadcaster) {
        spawnDragon(broadcaster);
    }

    public void scheduleDragonSpawn(Consumer<Component> broadcaster) {
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            broadcaster.accept(Component.text("The Ender Dragon will spawn in 1 minute!", NamedTextColor.RED));
        }).delay(TaskSchedule.seconds(SkywarsGame.DRAGON_SPAWN_SECONDS - 60)).schedule();

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            spawnDragon(broadcaster);
        }).delay(TaskSchedule.seconds(SkywarsGame.DRAGON_SPAWN_SECONDS)).schedule();
    }

    private void spawnDragon(Consumer<Component> broadcaster) {
        if (dragonSpawned) return;
        dragonSpawned = true;
        this.broadcaster = broadcaster;

        broadcaster.accept(Component.text("The Ender Dragon has spawned!", NamedTextColor.DARK_PURPLE));

        dragon = new DragonEntity();
        dragon.getAttribute(Attribute.MAX_HEALTH).setBaseValue(200);
        dragon.setHealth(200);
        dragon.setCustomName(Component.text("Ender Dragon", NamedTextColor.DARK_PURPLE));
        dragon.setCustomNameVisible(true);

        double startX = centerPos.x() + CIRCLE_RADIUS;
        double startY = centerPos.y() + CIRCLE_HEIGHT;
        double startZ = centerPos.z();
        dragon.setInstance(instance, new Pos(startX, startY, startZ));

        state = DragonState.CIRCLING;
        circleAngle = 0;
        lastDiveTime = System.currentTimeMillis();

        behaviorTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (dragon == null || dragon.isDead() || !dragonSpawned) {
                if (behaviorTask != null) {
                    behaviorTask.cancel();
                }
                return;
            }
            dragonBehaviorTick();
        }).repeat(TaskSchedule.tick(1)).schedule();
    }

    private void dragonBehaviorTick() {
        switch (state) {
            case CIRCLING -> handleCircling();
            case DIVING -> handleDiving();
            case RETURNING -> handleReturning();
        }
    }

    private void handleCircling() {
        circleAngle += CIRCLE_SPEED;
        if (circleAngle >= Math.PI * 2) {
            circleAngle -= Math.PI * 2;
        }

        double targetX = centerPos.x() + Math.cos(circleAngle) * CIRCLE_RADIUS;
        double targetZ = centerPos.z() + Math.sin(circleAngle) * CIRCLE_RADIUS;
        double targetY = centerPos.y() + CIRCLE_HEIGHT;

        moveToward(targetX, targetY, targetZ, FLIGHT_SPEED);

        long now = System.currentTimeMillis();
        if (now - lastDiveTime > DIVE_COOLDOWN_MS) {
            SkywarsPlayer target = findClosestPlayer();
            if (target != null) {
                diveTarget = target;
                state = DragonState.DIVING;
                lastDiveTime = now;
                diveStartTime = now;
                broadcaster.accept(Component.text("The Ender Dragon is diving at " + target.getUsername() + "!", NamedTextColor.RED));
            }
        }
    }

    private void handleDiving() {
        if (diveTarget == null || diveTarget.isEliminated() || !diveTarget.isOnline()) {
            state = DragonState.RETURNING;
            diveTarget = null;
            return;
        }

        if (System.currentTimeMillis() - diveStartTime > 5000) {
            state = DragonState.RETURNING;
            return;
        }

        Pos targetPos = diveTarget.getPosition();
        moveToward(targetPos.x(), targetPos.y() + 2, targetPos.z(), DIVE_SPEED);

        double dist = dragon.getPosition().distance(diveTarget.getPosition());
        if (dist < ATTACK_RANGE) {
            diveTarget.damage(DamageType.MOB_ATTACK, ATTACK_DAMAGE);
            broadcaster.accept(Component.text(diveTarget.getUsername() + " was struck by the Ender Dragon!", NamedTextColor.RED));
            state = DragonState.RETURNING;
            diveTarget = null;
            return;
        }

        if (dragon.getPosition().y() < targetPos.y() - 10) {
            state = DragonState.RETURNING;
            diveTarget = null;
        }
    }

    private void handleReturning() {
        double targetY = centerPos.y() + CIRCLE_HEIGHT;

        double targetX = centerPos.x() + Math.cos(circleAngle) * CIRCLE_RADIUS;
        double targetZ = centerPos.z() + Math.sin(circleAngle) * CIRCLE_RADIUS;

        moveToward(targetX, targetY, targetZ, RETURN_SPEED);

        if (dragon.getPosition().y() >= targetY - 3) {
            state = DragonState.CIRCLING;
            diveTarget = null;
        }
    }

    private void moveToward(double x, double y, double z, double speed) {
        dragon.setTarget(new Pos(x, y, z), speed);
    }

    private SkywarsPlayer findClosestPlayer() {
        SkywarsPlayer closest = null;
        double closestDist = Double.MAX_VALUE;

        for (SkywarsPlayer player : game.getPlayers()) {
            if (player.isEliminated() || !player.isOnline()) continue;
            double dist = dragon.getPosition().distance(player.getPosition());
            if (dist < closestDist) {
                closestDist = dist;
                closest = player;
            }
        }

        return closest;
    }

    public void onDragonDamaged(UUID damagerUuid, float damage) {
        if (dragon == null) return;

        float newHealth = dragon.getHealth() - damage;
        if (newHealth <= 0) {
            onDragonKilled(damagerUuid);
        } else {
            dragon.setHealth(newHealth);
        }
    }

    private void onDragonKilled(UUID killerUuid) {
        if (behaviorTask != null) {
            behaviorTask.cancel();
        }

        dragon.kill();
        game.onDragonKilled(killerUuid);
    }

    public boolean isDragonSpawned() {
        return dragonSpawned;
    }

    public void cleanup() {
        if (behaviorTask != null) {
            behaviorTask.cancel();
        }
        if (dragon != null && !dragon.isDead()) {
            dragon.remove();
        }
        dragonSpawned = false;
        state = DragonState.CIRCLING;
        diveTarget = null;
    }
}
