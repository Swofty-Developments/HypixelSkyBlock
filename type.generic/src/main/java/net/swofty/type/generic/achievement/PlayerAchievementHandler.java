package net.swofty.type.generic.achievement;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointAchievementData;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;

@RequiredArgsConstructor
public class PlayerAchievementHandler {
    private final HypixelPlayer player;

    public AchievementData getAchievementData() {
        return player.getDataHandler()
                .get(HypixelDataHandler.Data.ACHIEVEMENT_DATA, DatapointAchievementData.class)
                .getValue();
    }

    public boolean addProgress(String achievementId, int amount) {
        AchievementDefinition def = AchievementRegistry.get(achievementId);
        if (def == null) return false;

        AchievementData data = getAchievementData();
        AchievementData.AchievementProgress progress = data.getOrCreate(achievementId);

        boolean unlocked = progress.addProgress(def, amount);

        if (unlocked) {
            onAchievementUnlocked(def, progress);
        }

        return unlocked;
    }

    public void addProgressByTrigger(String trigger, int amount) {
        List<AchievementDefinition> achievements = AchievementRegistry.getByTrigger(trigger);
        AchievementData data = getAchievementData();

        for (AchievementDefinition def : achievements) {
            if (def.isPerGame()) {
                continue;
            }

            if (def.getType() == AchievementType.TIERED) {
                if (!data.isTracked(def.getId())) {
                    continue;
                }
            }

            addProgress(def.getId(), amount);
        }
    }

    public void completeAchievement(String achievementId) {
        AchievementDefinition def = AchievementRegistry.get(achievementId);
        if (def == null) return;

        AchievementData data = getAchievementData();
        AchievementData.AchievementProgress progress = data.getOrCreate(achievementId);

        if (!progress.isCompleted()) {
            progress.complete();
            onAchievementUnlocked(def, progress);
        }
    }

    public boolean hasAchievement(String achievementId) {
        return getAchievementData().isCompleted(achievementId);
    }

    public boolean hasFullyCompletedAchievement(String achievementId) {
        return getAchievementData().isFullyCompleted(achievementId);
    }

    public int getAchievementTier(String achievementId) {
        return getAchievementData().getCurrentTier(achievementId);
    }

    public int getProgress(String achievementId) {
        return getAchievementData().getProgress(achievementId);
    }

    public AchievementData.AchievementProgress getProgressData(String achievementId) {
        return getAchievementData().get(achievementId);
    }

    public int getTotalPoints() {
        return getAchievementData().getTotalPoints();
    }

    public int getTotalPoints(AchievementCategory category) {
        return getAchievementData().getTotalPoints(category);
    }

    public int getPoints(AchievementCategory category, AchievementType type) {
        return getAchievementData().getPoints(category, type);
    }

    public int getUnlockedCount(AchievementCategory category) {
        return getAchievementData().getUnlockedCount(category);
    }

    public int getUnlockedCount(AchievementCategory category, AchievementType type) {
        return getAchievementData().getUnlockedCount(category, type);
    }

    public double getCompletionPercentage(AchievementCategory category) {
        return getAchievementData().getCompletionPercentage(category);
    }

    public int getTotalUnlockedCount() {
        return getAchievementData().getTotalUnlockedCount();
    }

    public boolean isTracked(String achievementId) {
        return getAchievementData().isTracked(achievementId);
    }

    public String getTrackedAchievement(AchievementCategory category) {
        return getAchievementData().getTrackedAchievement(category);
    }

    public boolean toggleTracking(String achievementId) {
        AchievementDefinition def = AchievementRegistry.get(achievementId);
        if (def == null || def.getType() != AchievementType.TIERED) {
            player.sendMessage("§cOnly tiered achievements can be tracked!");
            return false;
        }

        if (hasFullyCompletedAchievement(achievementId)) {
            player.sendMessage("§cYou have already completed all tiers of this achievement!");
            return false;
        }

        boolean nowTracking = getAchievementData().toggleTracking(achievementId);

        if (nowTracking) {
            player.sendMessage("§aNow tracking: §e" + def.getName());
            player.playSound(net.kyori.adventure.sound.Sound.sound(
                    net.minestom.server.sound.SoundEvent.BLOCK_NOTE_BLOCK_PLING,
                    net.kyori.adventure.sound.Sound.Source.MASTER, 1.0f, 2.0f));
        } else {
            player.sendMessage("§cStopped tracking: §e" + def.getName());
        }

        return nowTracking;
    }

    private void onAchievementUnlocked(AchievementDefinition def, AchievementData.AchievementProgress progress) {
        String tierText = "";
        if (def.getType() == AchievementType.TIERED) {
            tierText = " " + toRoman(progress.getCurrentTier());
        }

        // TODO: make this actually clickable to open the achievements menu
        Component tierHover = Component.text(def.getName() + tierText, NamedTextColor.GREEN).appendNewline()
                .append(Component.text(def.getDescription(), NamedTextColor.GRAY))
                .appendNewline().appendNewline()
                .append(Component.text("Reward:", NamedTextColor.GRAY))
                .appendNewline()
                .append(Component.text(" §8+§e5 §7Achievement Points"))
                .appendNewline().appendNewline()
                .append(Component.text("Click to open achievements menu!", NamedTextColor.YELLOW));

        Component obf = Component.text("A", NamedTextColor.YELLOW, TextDecoration.OBFUSCATED);
        player.sendMessage(
                obf
                        .append(Component.text(">>   Achievement Unlocked: ", NamedTextColor.GREEN).decorationIfAbsent(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(Component.text(def.getName() + tierText, NamedTextColor.GOLD).hoverEvent(HoverEvent.showText(tierHover)).decorationIfAbsent(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(Component.text("   <<", NamedTextColor.GREEN).decorationIfAbsent(TextDecoration.OBFUSCATED, TextDecoration.State.FALSE))
                        .append(obf)
        );

        player.playSound(net.kyori.adventure.sound.Sound.sound(
                net.minestom.server.sound.SoundEvent.ENTITY_PLAYER_LEVELUP,
                net.kyori.adventure.sound.Sound.Source.MASTER, 1.0f, 1.0f));
    }

    private String toRoman(int tier) {
        return switch (tier) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> String.valueOf(tier);
        };
    }
}
