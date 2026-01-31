package net.swofty.type.bedwarsgame.game.v2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.MapTeam;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.commons.game.GameState;

import java.time.Duration;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BedWarsGeneratorManager {
    private final BedWarsGame game;
    private Map<TeamKey, List<Task>> teamGeneratorTasks = new EnumMap<>(TeamKey.class);
    private List<Task> globalGeneratorTasks = new ArrayList<>();

    // Track items at each global generator location
    private Map<Pos, Integer> diamondItemCounts = new HashMap<>();
    private Map<Pos, Integer> emeraldItemCounts = new HashMap<>();

    @Getter
    private int currentDiamondSpawnSeconds = 30;
    @Getter
    private int currentEmeraldSpawnSeconds = 60;

    public void startTeamGenerators(Map<TeamKey, MapTeam> activeTeams) {
        BedWarsMapsConfig.GeneratorSpeed speed = game.getMapEntry()
                .getConfiguration().getGeneratorSpeed();

        if (speed == null) return;

        activeTeams.forEach((teamKey, team) -> {
            BedWarsMapsConfig.Position genLocation = team.getGenerator();
            if (genLocation == null) return;

            Pos spawnPos = new Pos(genLocation.x(), genLocation.y(), genLocation.z());

            // Iron generator
            startGenerator(teamKey, Material.IRON_INGOT, speed.getIronAmount(),
                    speed.getIronDelaySeconds(), spawnPos);

            // Gold generator
            startGenerator(teamKey, Material.GOLD_INGOT, speed.getGoldAmount(),
                    speed.getGoldDelaySeconds(), spawnPos);
        });
    }

    private void startGenerator(TeamKey teamKey, Material material, int amount, int delaySeconds, Pos spawnPos) {
        Task task = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (game.getState() != GameState.IN_PROGRESS) return;

            // Check forge upgrades
            int forgeLevel = game.getTeam(teamKey.name())
                    .map(t -> t.getUpgradeLevel("forge"))
                    .orElse(0);

            double multiplier = calculateForgeMultiplier(material, forgeLevel);
            int finalAmount = (int) Math.max(1, Math.round(amount * multiplier));

            spawnItem(material, finalAmount, spawnPos);
        }).delay(TaskSchedule.seconds(delaySeconds))
                .repeat(TaskSchedule.seconds(delaySeconds))
                .schedule();

        teamGeneratorTasks.computeIfAbsent(teamKey, k -> new ArrayList<>()).add(task);
    }

    public void startGlobalGenerators() {
        var globalGens = game.getMapEntry().getConfiguration().getGlobal_generator();
        if (globalGens == null) return;

        // Diamond generators
        var diamondConfig = globalGens.get("diamond");
        if (diamondConfig != null && diamondConfig.getLocations() != null) {
            int maxDiamonds = diamondConfig.getMax();
            for (BedWarsMapsConfig.Position pos : diamondConfig.getLocations()) {
                Pos spawnPos = new Pos(pos.x(), pos.y(), pos.z());
                diamondItemCounts.put(spawnPos, 0);

                Task task = MinecraftServer.getSchedulerManager().buildTask(() -> {
                    if (game.getState() != GameState.IN_PROGRESS) return;

                    int currentCount = diamondItemCounts.getOrDefault(spawnPos, 0);
                    if (currentCount < maxDiamonds) {
                        spawnItem(Material.DIAMOND, 1, spawnPos);
                        diamondItemCounts.put(spawnPos, currentCount + 1);
                    }
                }).delay(TaskSchedule.seconds(currentDiamondSpawnSeconds))
                        .repeat(TaskSchedule.seconds(currentDiamondSpawnSeconds))
                        .schedule();

                globalGeneratorTasks.add(task);
            }
        }

        // Emerald generators
        var emeraldConfig = globalGens.get("emerald");
        if (emeraldConfig != null && emeraldConfig.getLocations() != null) {
            int maxEmeralds = emeraldConfig.getMax();
            for (BedWarsMapsConfig.Position pos : emeraldConfig.getLocations()) {
                Pos spawnPos = new Pos(pos.x(), pos.y(), pos.z());
                emeraldItemCounts.put(spawnPos, 0);

                Task task = MinecraftServer.getSchedulerManager().buildTask(() -> {
                    if (game.getState() != GameState.IN_PROGRESS) return;

                    int currentCount = emeraldItemCounts.getOrDefault(spawnPos, 0);
                    if (currentCount < maxEmeralds) {
                        spawnItem(Material.EMERALD, 1, spawnPos);
                        emeraldItemCounts.put(spawnPos, currentCount + 1);
                    }
                }).delay(TaskSchedule.seconds(currentEmeraldSpawnSeconds))
                        .repeat(TaskSchedule.seconds(currentEmeraldSpawnSeconds))
                        .schedule();

                globalGeneratorTasks.add(task);
            }
        }
    }

    /**
     * Updates global generator speeds based on game phase.
     */
    public void updateGlobalGeneratorSpeeds(int diamondSeconds, int emeraldSeconds) {
        if (diamondSeconds != currentDiamondSpawnSeconds || emeraldSeconds != currentEmeraldSpawnSeconds) {
            currentDiamondSpawnSeconds = diamondSeconds;
            currentEmeraldSpawnSeconds = emeraldSeconds;

            // Restart global generators with new speeds
            globalGeneratorTasks.forEach(Task::cancel);
            globalGeneratorTasks.clear();
            startGlobalGenerators();
        }
    }

    /**
     * Called when an item is picked up from a generator location.
     */
    public void onItemPickedUp(Pos location, Material material) {
        if (material == Material.DIAMOND) {
            diamondItemCounts.computeIfPresent(location, (k, v) -> Math.max(0, v - 1));
        } else if (material == Material.EMERALD) {
            emeraldItemCounts.computeIfPresent(location, (k, v) -> Math.max(0, v - 1));
        }
    }

    public void stopAllGenerators() {
        teamGeneratorTasks.values().forEach(tasks ->
                tasks.forEach(Task::cancel));
        teamGeneratorTasks.clear();

        globalGeneratorTasks.forEach(Task::cancel);
        globalGeneratorTasks.clear();

        diamondItemCounts.clear();
        emeraldItemCounts.clear();
    }

    private void spawnItem(Material material, int amount, Pos position) {
        ItemStack item = ItemStack.of(material, amount);
        ItemEntity entity = new ItemEntity(item);
        entity.setPickupDelay(Duration.ofMillis(500));
        entity.setInstance(game.getInstance(), position);
    }

    private double calculateForgeMultiplier(Material material, int forgeLevel) {
        if (forgeLevel <= 0) return 1.0;

        String materialName = material.name();
        if (materialName.equals("iron_ingot")) {
            return 1.0 + (forgeLevel * 0.5);
        } else if (materialName.equals("gold_ingot")) {
            return 1.0 + (forgeLevel * 0.25);
        }
        return 1.0;
    }

    /**
     * Adds a custom generator task for a team.
     */
    public void addTeamGeneratorTask(TeamKey teamKey, Task task) {
        teamGeneratorTasks.computeIfAbsent(teamKey, k -> new ArrayList<>()).add(task);
    }
}
