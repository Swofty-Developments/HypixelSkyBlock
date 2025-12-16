package net.swofty.type.skyblockgeneric.enchantment.debuff;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LethalityDebuff {

    public static final double[] DEFENSE_REDUCTION_PERCENTAGES = new double[]{0.012, 0.024, 0.036, 0.048, 0.06, 0.09};

    private static final int MAX_STACKS = 4;

    private static final int STACK_DURATION_SECONDS = 4;

    private static final Map<UUID, LethalityStacks> activeDebuffs = new ConcurrentHashMap<>();

    public static void applyStack(SkyBlockMob mob, int level) {
        if (level < 1 || level > 6) return;

        UUID mobId = mob.getUuid();
        double defenseReduction = DEFENSE_REDUCTION_PERCENTAGES[level - 1];

        LethalityStacks stacks = activeDebuffs.computeIfAbsent(mobId, k -> new LethalityStacks());

        if (stacks.getStackCount() < MAX_STACKS) {
            stacks.addStack(defenseReduction);
        } else {
            stacks.replaceOldestStack(defenseReduction);
        }

        if (stacks.getStackCount() == 1) {
            scheduleCleanup(mobId);
        }
    }

    public static double getTotalDefenseReduction(UUID mobId) {
        LethalityStacks stacks = activeDebuffs.get(mobId);
        return stacks != null ? stacks.getTotalReduction() : 0.0;
    }

    private static void scheduleCleanup(UUID mobId) {
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            LethalityStacks stacks = activeDebuffs.get(mobId);
            if (stacks != null) {
                stacks.removeExpiredStacks();
                if (stacks.getStackCount() == 0) {
                    activeDebuffs.remove(mobId);
                } else {
                    scheduleCleanup(mobId);
                }
            }
        }, TaskSchedule.seconds(STACK_DURATION_SECONDS), TaskSchedule.stop());
    }

    private static class LethalityStacks {
        private final LethalityStack[] stacks = new LethalityStack[MAX_STACKS];
        @Getter
        private int stackCount = 0;

        public void addStack(double defenseReduction) {
            if (stackCount < MAX_STACKS) {
                stacks[stackCount] = new LethalityStack(defenseReduction);
                stackCount++;
            }
        }

        public void replaceOldestStack(double defenseReduction) {
            if (stackCount > 0) {
                System.arraycopy(stacks, 1, stacks, 0, stackCount - 1);
                stacks[stackCount - 1] = new LethalityStack(defenseReduction);
            }
        }

        public void removeExpiredStacks() {
            long currentTime = System.currentTimeMillis();
            int newCount = 0;

            for (int i = 0; i < stackCount; i++) {
                if (!stacks[i].isExpired(currentTime)) {
                    stacks[newCount] = stacks[i];
                    newCount++;
                }
            }

            stackCount = newCount;
        }

        public double getTotalReduction() {
            double total = 0.0;
            long currentTime = System.currentTimeMillis();

            for (int i = 0; i < stackCount; i++) {
                if (!stacks[i].isExpired(currentTime)) {
                    total += stacks[i].getDefenseReduction();
                }
            }

            return total;
        }
    }

    private static class LethalityStack {
        @Getter
        private final double defenseReduction;
        private final long expirationTime;

        public LethalityStack(double defenseReduction) {
            this.defenseReduction = defenseReduction;
            this.expirationTime = System.currentTimeMillis() + (STACK_DURATION_SECONDS * 1000L);
        }

        public boolean isExpired(long currentTime) {
            return currentTime > expirationTime;
        }
    }
}

