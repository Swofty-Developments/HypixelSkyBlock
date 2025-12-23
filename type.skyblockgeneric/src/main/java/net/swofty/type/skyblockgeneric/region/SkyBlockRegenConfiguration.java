package net.swofty.type.skyblockgeneric.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;

public abstract class SkyBlockRegenConfiguration {

    public ArrayList<MiningTask> activeMiningTasks = new ArrayList<>();

    public abstract MiningTask handleStageOne(MiningTask task, Pos brokenBlock);

    public abstract MiningTask handleStageTwo(MiningTask task, Pos brokenBlock);

    public abstract List<Material> getMineableBlocks(Instance instance, Point point);

    public abstract long getRegenerationTime();

    public void addToQueue(SkyBlockPlayer player, Pos block, SharedInstance instance) {
        Optional<MiningTask> taskExists = findMiningTask(block);

        if (taskExists.isPresent()) {
            // Stage 2 Handler
            var task = handleStageTwo(new MiningTask(
                    player,
                    block,
                    null,
                    null,
                    instance,
                    System.currentTimeMillis() + getRegenerationTime(),
                    2,
                    getInitialBlockFromPos(block).get(),
                    taskExists.get().getInitialMinedBlock()), block);
            if (task.getIntermediaryBlock() == null || task.getReviveBlock() == null) {
                // There is no stage 2 for this MiningConfiguration
                return;
            }

            instance.setBlock(block, task.getIntermediaryBlock());
            removeExistingTask(block);
            activeMiningTasks.add(task);
        } else {
            // Stage 1 Handler
            var task = handleStageOne(new MiningTask(
                    player,
                    block,
                    null,
                    null,
                    instance,
                    System.currentTimeMillis() + getRegenerationTime(),
                    1,
                    null,
                    instance.getBlock(block)), block);
            task.setInitialReviveBlock(task.getReviveBlock());
            activeMiningTasks.add(task);
            instance.setBlock(block, task.getIntermediaryBlock());
        }
    }

    private Optional<MiningTask> findMiningTask(Pos block) {
        return activeMiningTasks.stream().filter(task -> task.getPosition().equals(block)).findFirst();
    }

    public Optional<Block> getInitialBlockFromPos(Pos block) {
        return activeMiningTasks.stream()
                .filter(task -> task.getPosition().equals(block))
                .map(MiningTask::getInitialReviveBlock)
                .findFirst();
    }

    public void removeExistingTask(Pos block) {
        activeMiningTasks.removeIf(task -> task.getPosition().equals(block));
    }

    public static void startRepeater(Scheduler scheduler) {
        scheduler.submitTask(() -> {
            Arrays.stream(RegionType.values())
                    .parallel() // parallelism for time-consuming operations
                    .map(RegionType::getMiningHandler) // map to mining handlers
                    .filter(Objects::nonNull) // filter out null mining handlers
                    .forEach((mining) -> {
                        List<Pos> toRemove = new ArrayList<>();
                        List<MiningTask> toAdd = new ArrayList<>();

                        mining.activeMiningTasks.forEach(task -> {
                            if (task.getRegenerationTime() <= System.currentTimeMillis()) {
                                task.getInstance().setBlock(task.getPosition(), task.getReviveBlock());

                                toRemove.add(task.getPosition());

                                if (task.getStage() == 2) {
                                    task.setStage(1);
                                    task.setRegenerationTime(System.currentTimeMillis() + mining.getRegenerationTime());
                                    task.setIntermediaryBlock(task.getReviveBlock());
                                    task.setReviveBlock(task.getInitialReviveBlock());
                                    toAdd.add(task);
                                }
                            }
                        });

                        toRemove.forEach(mining::removeExistingTask);
                        mining.activeMiningTasks.addAll(toAdd);
                    });
            return TaskSchedule.tick(20);
        });
    }

    public static Block getRandomBlock(RegenerationConfig... regenerationConfigs) {
        int totalChance = Arrays.stream(regenerationConfigs).mapToInt(RegenerationConfig::chance).sum();

        int random = (int) (Math.random() * totalChance);
        int currentCumulativeChance = 0;

        for (RegenerationConfig config : regenerationConfigs) {
            currentCumulativeChance += config.chance();
            if (random <= currentCumulativeChance) {
                return config.block();
            }
        }
        return null;
    }

    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class MiningTask {
        SkyBlockPlayer playerWhoInitiated;
        Pos position;
        Block intermediaryBlock;
        Block reviveBlock;
        SharedInstance instance;
        long regenerationTime;
        int stage;
        Block initialReviveBlock;
        Block initialMinedBlock;

        public static MiningTask never() {
            return MiningTask.builder().build();
        }
    }

    public record RegenerationConfig(int chance, Block block) { }
}