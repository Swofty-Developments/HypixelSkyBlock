package net.swofty.type.bedwarsgame.game.v2;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.MapTeam;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.entity.TextDisplayEntity;
import net.swofty.type.game.game.GameState;
import org.intellij.lang.annotations.Subst;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BedWarsGeneratorManager {
    private final BedWarsGame game;
    private final Map<TeamKey, List<Task>> teamGeneratorTasks = new EnumMap<>(TeamKey.class);
    private final Map<String, List<GeneratorDisplay>> generatorDisplays = new HashMap<>();
    private final Map<String, GeneratorLimits> generatorLimits = new HashMap<>();
    private Task globalTicker;

    public BedWarsGeneratorManager(BedWarsGame game) {
        this.game = game;
    }

    public void startTeamGenerators(Map<TeamKey, MapTeam> activeTeams) {
        BedWarsMapsConfig.MapEntry.MapConfiguration mapConfig = game.getMapEntry().getConfiguration();
        BedWarsMapsConfig.GeneratorSpeed generatorSpeed = mapConfig.getGeneratorSpeed();

        if (generatorSpeed == null) {
            Logger.warn("No generator speed configured for map");
            return;
        }

        activeTeams.forEach((teamKey, team) -> {
            BedWarsMapsConfig.Position genLocation = team.getGenerator();
            if (genLocation == null) return;

            Pos spawnPosition = new Pos(genLocation.x(), genLocation.y(), genLocation.z());

            // Start iron generator
            startTeamGenerator(teamKey, "iron", generatorSpeed.getIronAmount(),
                generatorSpeed.getIronDelaySeconds(), spawnPosition);

            // Start gold generator
            startTeamGenerator(teamKey, "gold", generatorSpeed.getGoldAmount(),
                generatorSpeed.getGoldDelaySeconds(), spawnPosition);
        });
    }

    // material type being string is annoying
    private void startTeamGenerator(TeamKey teamKey, String materialType,
                                    int baseAmount, int baseDelay, Pos spawnPosition) {
        Material itemMaterial = getMaterialFromType(materialType);
        if (itemMaterial == null) {
            Logger.warn("Invalid material type: {} for team {}", materialType, teamKey.getName());
            return;
        }

        Task task = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (game.getState() != GameState.IN_PROGRESS) return;

            int forgeLevel = game.getTeamUpgradeLevel(teamKey, "forge");
            double multiplier = calculateForgeMultiplier(itemMaterial, forgeLevel);

            int finalAmount = (int) Math.round(baseAmount * multiplier);
            if (finalAmount == 0 && baseAmount > 0 && multiplier > 1.0) finalAmount = 1;

            if (finalAmount > 0) {
                spawnItem(itemMaterial, finalAmount, spawnPosition, Duration.ofMillis(500));
            }
        }).delay(TaskSchedule.seconds(baseDelay)).repeat(TaskSchedule.seconds(baseDelay)).schedule();

        teamGeneratorTasks.computeIfAbsent(teamKey, k -> new ArrayList<>()).add(task);
    }

    public void startGlobalGenerators() {
        BedWarsMapsConfig.MapEntry.MapConfiguration mapConfig = game.getMapEntry().getConfiguration();
        if (mapConfig.getGlobal_generator() == null) return;

        mapConfig.getGlobal_generator().forEach(this::setupGlobalGenerator);

        if (globalTicker != null) globalTicker.cancel();
        globalTicker = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (game.getState() != GameState.IN_PROGRESS) return;
            updateGeneratorDisplays();
            tickGlobalGenerators();
        }).delay(TaskSchedule.seconds(1)).repeat(TaskSchedule.seconds(1)).schedule();
    }

    private void setupGlobalGenerator(String generatorType, BedWarsMapsConfig.MapEntry.MapConfiguration.GlobalGenerator config) {
        Material itemMaterial = getMaterialFromType(generatorType);
        if (itemMaterial == null) {
            Logger.warn("Invalid material for global generator: {}", generatorType);
            return;
        }

        List<BedWarsMapsConfig.Position> locations = config.getLocations();
        if (locations == null || locations.isEmpty()) return;

        if (generatorType.equals("diamond") || generatorType.equals("emerald")) {
            int delaySeconds = generatorType.equals("diamond")
                ? game.getGameEventManager().getCurrentPhase().getDiamondSpawnSeconds()
                : game.getGameEventManager().getCurrentPhase().getEmeraldSpawnSeconds();

            setupGlobalGeneratorDisplays(generatorType, locations, delaySeconds);
            generatorLimits.put(generatorType, new GeneratorLimits(
                itemMaterial, config.getAmount(), config.getMax(), locations));
        }
    }

    private void setupGlobalGeneratorDisplays(String generatorType, List<BedWarsMapsConfig.Position> locations, int delaySeconds) {
        NamedTextColor color = generatorType.equals("diamond") ? NamedTextColor.AQUA : NamedTextColor.GREEN;
        String capitalizedType = Character.toUpperCase(generatorType.charAt(0)) + generatorType.substring(1);

        for (BedWarsMapsConfig.Position location : locations) {
            double locY = location.y() + 4.0;

            TextDisplayEntity tierDisplay = new TextDisplayEntity(
                Component.text("Tier I").color(NamedTextColor.YELLOW));
            tierDisplay.setInstance(game.getInstance(), new Pos(location.x(), locY, location.z()));

            locY -= 0.3;
            TextDisplayEntity titleDisplay = new TextDisplayEntity(
                Component.text(capitalizedType).color(color).decorate(TextDecoration.BOLD));
            titleDisplay.setInstance(game.getInstance(), new Pos(location.x(), locY, location.z()));

            locY -= 0.3;
            TextDisplayEntity spawnDisplay = new TextDisplayEntity(
                MiniMessage.miniMessage().deserialize("<yellow>Spawns in <red>" + delaySeconds + "</red> seconds!</yellow>"));
            spawnDisplay.setInstance(game.getInstance(), new Pos(location.x(), locY, location.z()));

            generatorDisplays.computeIfAbsent(generatorType, k -> new ArrayList<>())
                .add(new GeneratorDisplay(tierDisplay, titleDisplay, spawnDisplay, delaySeconds));
        }
    }

    private void tickGlobalGenerators() {
        for (Map.Entry<String, GeneratorLimits> entry : generatorLimits.entrySet()) {
            String type = entry.getKey();
            GeneratorLimits limits = entry.getValue();
            List<GeneratorDisplay> displays = generatorDisplays.get(type);

            if (displays == null || displays.isEmpty()) continue;

            for (int i = 0; i < limits.locations.size(); i++) {
                BedWarsMapsConfig.Position location = limits.locations.get(i);
                GeneratorDisplay display = i < displays.size() ? displays.get(i) : null;

                if (display == null) continue;

                if (display.countdown <= 0) {
                    Pos spawnPos = new Pos(location.x(), location.y(), location.z());
                    long currentItemCount = game.getInstance().getNearbyEntities(spawnPos, 1.5)
                        .stream()
                        .filter(ItemEntity.class::isInstance)
                        .map(ItemEntity.class::cast)
                        .filter(entity -> entity.getItemStack().material() == limits.material)
                        .mapToLong(entity -> entity.getItemStack().amount())
                        .sum();

                    if (currentItemCount < limits.maxAmount) {
                        spawnItem(limits.material, limits.amount, spawnPos, Duration.ofSeconds(1));
                    }
                    display.countdown = display.maxCountdown;
                }
            }
        }
    }

    private void updateGeneratorDisplays() {
        for (List<GeneratorDisplay> displays : generatorDisplays.values()) {
            for (GeneratorDisplay display : displays) {
                display.countdown--;
                if (display.countdown <= 0) display.countdown = display.maxCountdown;

                display.spawnDisplay.setText(MiniMessage.miniMessage().deserialize(
                    "<yellow>Spawns in <red>" + display.countdown + "</red> seconds!</yellow>"));
            }
        }
    }

    public void updateDisplaysForEventChange() {
        for (Map.Entry<String, List<GeneratorDisplay>> e : generatorDisplays.entrySet()) {
            int max = e.getKey().equals("diamond") ? game.getGameEventManager().getDiamondSpawnSeconds() : game.getGameEventManager().getEmeraldSpawnSeconds();
            String tierLabel = getTierLabelFor(e.getKey());

            for (GeneratorDisplay d : e.getValue()) {
                d.maxCountdown = max;
                d.countdown = max;
                d.tierDisplay.setText(Component.text(tierLabel).color(NamedTextColor.YELLOW));
            }
        }
    }

    private String getTierLabelFor(String type) {
        BedWarsGameEventManager.GamePhase phase = game.getGameEventManager().getCurrentPhase();
        int tier = 1;

        if ("diamond".equals(type)) {
            if (phase.ordinal() >= BedWarsGameEventManager.GamePhase.DIAMOND_II.ordinal()) tier = 2;
            if (phase.ordinal() >= BedWarsGameEventManager.GamePhase.DIAMOND_III.ordinal()) tier = 3;
        } else if ("emerald".equals(type)) {
            if (phase.ordinal() >= BedWarsGameEventManager.GamePhase.EMERALD_II.ordinal()) tier = 2;
            if (phase.ordinal() >= BedWarsGameEventManager.GamePhase.EMERALD_III.ordinal()) tier = 3;
        }

        return switch (tier) {
            case 2 -> "Tier II";
            case 3 -> "Tier III";
            default -> "Tier I";
        };
    }

    public void stopAllGenerators() {
        teamGeneratorTasks.values().stream().flatMap(List::stream).forEach(Task::cancel);
        teamGeneratorTasks.clear();

        // Remove all display entities
        for (List<GeneratorDisplay> displays : generatorDisplays.values()) {
            for (GeneratorDisplay display : displays) {
                display.tierDisplay.remove();
                display.titleDisplay.remove();
                display.spawnDisplay.remove();
            }
        }
        generatorDisplays.clear();
        generatorLimits.clear();

        if (globalTicker != null) {
            globalTicker.cancel();
            globalTicker = null;
        }
    }

    private void spawnItem(Material material, int amount, Pos position, Duration pickupDelay) {
        ItemStack item = ItemStack.of(material, amount);
        ItemEntity entity = new ItemEntity(item);
        entity.setPickupDelay(pickupDelay);
        entity.setInstance(game.getInstance(), position);

        if (pickupDelay.equals(Duration.ofSeconds(1))) {
            entity.setVelocity(new Vec(0, 0.1, 0));
        }
    }

    private Material getMaterialFromType(String materialType) {
        @Subst("iron") String type = materialType.toLowerCase();
        return switch (type) {
            case "iron" -> Material.IRON_INGOT;
            case "gold" -> Material.GOLD_INGOT;
            case "diamond" -> Material.DIAMOND;
            case "emerald" -> Material.EMERALD;
            default -> Material.fromKey(Key.key(Key.MINECRAFT_NAMESPACE, type));
        };
    }

    private double calculateForgeMultiplier(Material material, int forgeLevel) {
        if (material != Material.IRON_INGOT && material != Material.GOLD_INGOT) return 1.0;

        return switch (forgeLevel) {
            case 1 -> 1.5;
            case 2 -> 2.0;
            case 3, 4 -> 3.0;
            default -> 1.0;
        };
    }

    public void addTeamGeneratorTask(TeamKey teamKey, Task task) {
        teamGeneratorTasks.computeIfAbsent(teamKey, k -> new ArrayList<>()).add(task);
    }

    private static class GeneratorDisplay {
        private final TextDisplayEntity tierDisplay;
        private final TextDisplayEntity titleDisplay;
        private final TextDisplayEntity spawnDisplay;
        private int maxCountdown;
        private int countdown;

        public GeneratorDisplay(TextDisplayEntity tierDisplay, TextDisplayEntity titleDisplay,
                                TextDisplayEntity spawnDisplay, int delay) {
            this.tierDisplay = tierDisplay;
            this.titleDisplay = titleDisplay;
            this.spawnDisplay = spawnDisplay;
            this.maxCountdown = delay;
            this.countdown = delay;
        }
    }

    private record GeneratorLimits(Material material, int amount, int maxAmount,
                                   List<BedWarsMapsConfig.Position> locations) {
    }
}
