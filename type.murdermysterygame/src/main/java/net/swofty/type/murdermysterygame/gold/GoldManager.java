package net.swofty.type.murdermysterygame.gold;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.item.ItemEntityMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.CollectItemPacket;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.murdermystery.map.MurderMysteryMapsConfig;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.type.murdermysterygame.game.GameStatus;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;

import java.util.*;

public class GoldManager {
    private static final int GOLD_FOR_BOW = 10;
    private static final int SPAWN_INTERVAL_SECONDS = 5;
    private static final int MAX_GOLD_SPAWNED = 50;
    private static final double PICKUP_DISTANCE = 1.5;

    private final Game game;
    private final List<Entity> spawnedGold = new ArrayList<>();
    private Task spawnTask;
    private Task pickupCheckTask;

    public GoldManager(Game game) {
        this.game = game;
    }

    public void startSpawning() {
        var config = game.getMapEntry().getConfiguration();
        if (config == null || config.getGoldSpawns() == null || config.getGoldSpawns().isEmpty()) {
            // No gold spawns configured, use default positions
            return;
        }

        List<MurderMysteryMapsConfig.Position> spawnLocations = config.getGoldSpawns();

        spawnTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (spawnedGold.size() < MAX_GOLD_SPAWNED && !spawnLocations.isEmpty()) {
                MurderMysteryMapsConfig.Position randomSpawn = spawnLocations.get(new Random().nextInt(spawnLocations.size()));
                spawnGoldCoin(new Pos(randomSpawn.x(), randomSpawn.y(), randomSpawn.z()));
            }
        }).delay(TaskSchedule.seconds(3)).repeat(TaskSchedule.seconds(SPAWN_INTERVAL_SECONDS)).schedule();

        pickupCheckTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (game.getGameStatus() != GameStatus.IN_PROGRESS) return;

            List<Entity> goldToRemove = new ArrayList<>();

            for (Entity goldEntity : new ArrayList<>(spawnedGold)) {
                if (goldEntity.isRemoved()) {
                    goldToRemove.add(goldEntity);
                    continue;
                }

                for (MurderMysteryPlayer player : game.getPlayers()) {
                    if (player.isEliminated()) continue;

                    double distance = goldEntity.getPosition().distance(player.getPosition());
                    if (distance <= PICKUP_DISTANCE) {
                        // Collect the gold
                        goldToRemove.add(goldEntity);

                        // Send pickup animation packet
                        player.sendPacket(new CollectItemPacket(goldEntity.getEntityId(), player.getEntityId(), 1));

                        player.addGold(1);
                        int current = player.getGoldCollected();

                        player.sendMessage(Component.text("+1 Gold", NamedTextColor.GOLD));
                        player.sendActionBar(Component.text("Gold: " + current + "/" + GOLD_FOR_BOW, NamedTextColor.GOLD));

                        if (current >= GOLD_FOR_BOW) {
                            game.getWeaponManager().giveInnocentBow(player);
                            player.resetGold();
                        }

                        goldEntity.remove();
                        break; // Only one player can pick up each gold
                    }
                }
            }

            spawnedGold.removeAll(goldToRemove);
        }).repeat(TaskSchedule.tick(2)).schedule();
    }

    public void stopSpawning() {
        if (spawnTask != null) {
            spawnTask.cancel();
        }
        if (pickupCheckTask != null) {
            pickupCheckTask.cancel();
        }
        spawnedGold.forEach(Entity::remove);
        spawnedGold.clear();
    }

    private void spawnGoldCoin(Pos position) {
        Entity goldEntity = new Entity(EntityType.ITEM);
        ItemEntityMeta meta = (ItemEntityMeta) goldEntity.getEntityMeta();
        meta.setItem(ItemStack.of(Material.GOLD_INGOT));
        goldEntity.setInstance(game.getInstanceContainer(), position);
        spawnedGold.add(goldEntity);
    }

    public boolean collectGold(MurderMysteryPlayer player, Entity goldEntity) {
        if (!spawnedGold.contains(goldEntity)) return false;

        spawnedGold.remove(goldEntity);
        goldEntity.remove();

        player.addGold(1);
        int current = player.getGoldCollected();

        player.sendActionBar(Component.text("Gold: " + current + "/" + GOLD_FOR_BOW, NamedTextColor.GOLD));

        if (current >= GOLD_FOR_BOW) {
            game.getWeaponManager().giveInnocentBow(player);
            player.resetGold();
            return true;
        }

        return false;
    }

    public int getGoldForBow() {
        return GOLD_FOR_BOW;
    }

    public List<Entity> getSpawnedGold() {
        return spawnedGold;
    }
}
