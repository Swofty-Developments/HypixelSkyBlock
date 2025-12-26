package net.swofty.type.generic.experience;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.sound.SoundEvent;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointHypixelExperience;
import net.swofty.type.generic.user.HypixelPlayer;

@RequiredArgsConstructor
public class PlayerExperienceHandler {
    private final HypixelPlayer player;

    public long getExperience() {
        return player.getDataHandler()
                .get(HypixelDataHandler.Data.HYPIXEL_EXPERIENCE, DatapointHypixelExperience.class)
                .getValue();
    }

    public void setExperience(long xp) {
        player.getDataHandler()
                .get(HypixelDataHandler.Data.HYPIXEL_EXPERIENCE, DatapointHypixelExperience.class)
                .setValue(xp);
    }

    public boolean addExperience(long amount) {
        long oldXP = getExperience();
        long newXP = oldXP + amount;

        int oldLevel = HypixelExperience.xpToLevel(oldXP);
        int newLevel = HypixelExperience.xpToLevel(newXP);

        setExperience(newXP);

        player.sendMessage("§b+§3" + HypixelExperience.formatXP(amount) + " Hypixel Experience");

        if (newLevel > oldLevel) {
            for (int level = oldLevel + 1; level <= newLevel; level++) {
                onLevelUp(level);
            }
            return true;
        }

        return false;
    }

    public int getLevel() {
        return HypixelExperience.xpToLevel(getExperience());
    }

    public double getProgressToNextLevel() {
        return HypixelExperience.progressToNextLevel(getExperience());
    }

    public long getXPForNextLevel() {
        return HypixelExperience.xpForNextLevel(getExperience());
    }

    public long getXPInCurrentLevel() {
        return HypixelExperience.getXPInCurrentLevel(getExperience());
    }

    public double getCoinMultiplier() {
        return HypixelExperience.getCoinMultiplier(getLevel());
    }

    public boolean isVeteran() {
        return HypixelExperience.isVeteran(getLevel());
    }

    public String getFormattedLevel() {
        return HypixelExperience.formatLevel(getLevel());
    }

    private void onLevelUp(int newLevel) {
        player.sendMessage("§6§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        player.sendMessage("§b§lLEVEL UP! §7You are now Hypixel Level §e" + newLevel);
        player.sendMessage("");

        double multiplier = HypixelExperience.getCoinMultiplier(newLevel);
        int prevLevel = newLevel - 1;
        double prevMultiplier = HypixelExperience.getCoinMultiplier(prevLevel);
        if (multiplier > prevMultiplier) {
            player.sendMessage("§6NEW PERK! §eCoin Multiplier: §6" + multiplier + "x");
        }

        if (newLevel == 100) {
            player.sendMessage("§d§lVETERAN STATUS UNLOCKED!");
            player.sendMessage("§7Access exclusive veteran rewards!");
        }

        player.sendMessage("§6§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

        player.playSound(Sound.sound(SoundEvent.ENTITY_PLAYER_LEVELUP, Sound.Source.MASTER, 1.0f, 1.0f));
    }
}
