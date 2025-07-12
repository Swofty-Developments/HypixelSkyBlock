package net.swofty.types.generic.data.datapoints;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.StringUtility;
import net.swofty.commons.protocol.Serializer;
import net.swofty.types.generic.bestiary.BestiaryData;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.entity.mob.BestiaryMob;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.event.custom.BestiaryUpdateEvent;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.bestiary.BestiaryEntry;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatapointBestiary extends Datapoint<DatapointBestiary.PlayerBestiary> {

    public DatapointBestiary(String key, PlayerBestiary value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(PlayerBestiary value) {
                JSONObject jsonObject = new JSONObject(value.mobs);
                return jsonObject.toString();
            }

            @Override
            public PlayerBestiary deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                Map<String, Integer> mobs = new HashMap<>();

                for (String key : jsonObject.keySet()) {
                    mobs.put(key, jsonObject.getInt(key));
                }

                return new PlayerBestiary(mobs);
            }

            @Override
            public PlayerBestiary clone(PlayerBestiary value) {
                return new PlayerBestiary(value.mobs == null ? new HashMap<>() : new HashMap<>(value.mobs));
            }
        });
    }

    public DatapointBestiary(String key) {
        this(key, new PlayerBestiary());
    }

    @NoArgsConstructor
    @Getter
    public static class PlayerBestiary {
        private Map<String, Integer> mobs = new HashMap<>();
        @Setter
        private SkyBlockPlayer attachedPlayer = null;

        public PlayerBestiary(Map<String, Integer> mobs) {
            this.mobs = mobs;
        }

        public void setRaw(BestiaryMob mob, int value) {
            int oldValue = getAmount(mob);
            mobs.put(mob.getMobID(), value);

            if (attachedPlayer != null) {
                SkyBlockEventHandler.callSkyBlockEvent(new BestiaryUpdateEvent(
                        attachedPlayer,
                        mob,
                        oldValue,
                        value
                ));
            }
        }

        public void set(BestiaryMob mob, int amount) {
            setRaw(mob, amount);
        }

        public void increase(BestiaryMob mob, Integer amount) {
            setRaw(mob, getAmount(mob) + amount);
        }

        public void decrease(BestiaryMob mob, Integer amount) {
            setRaw(mob, getAmount(mob) - amount);
        }

        public Integer getAmount(BestiaryMob mob) {
            return mobs.getOrDefault(mob.getMobID(), 0);
        }

        public Integer getAmount(List<BestiaryMob> mobs) {
            int kills = 0;
            for (BestiaryMob mob : mobs) {
                kills += getAmount(mob);
            }
            return kills;
        }

        public List<String> getDisplay(List<String> lore, int kills, BestiaryMob mob, BestiaryEntry bestiaryEntry) {
            BestiaryData bestiaryData = new BestiaryData();

            int bracket = mob.getBestiaryBracket();
            int tier = bestiaryData.getCurrentBestiaryTier(mob, kills);
            double currentProgress = bestiaryData.getKillsToNextTier(mob, kills);
            double currentRequirement = bestiaryData.getTotalKillsForNextTier(bracket, tier + 1);
            double totalRequirement = bestiaryData.getTotalKillsForMaxTier(mob);

            lore.add("§7" + bestiaryEntry.getDescription());
            lore.add("");
            lore.add("§7Kills: §a" + kills);
            lore.add("§7Deaths: §a TO BE DONE"); //TODO add datapoint for amount of deaths
            lore.add("");

            if (tier > 0) {
                bestiaryData.getTotalBonuses(lore, bestiaryEntry.getName(), tier);
                lore.add("");
            }

            // Current tier progress
            int unlockedPercentage = (int) (currentProgress / currentRequirement * 100);
            lore.add("§7Progress to Tier " + StringUtility.getAsRomanNumeral(tier + 1) + " §b" + unlockedPercentage + "%");

            String baseLoadingBar = "─────────────────";
            int maxBarLength = baseLoadingBar.length();
            int completedLength = (int) Math.round((currentProgress / currentRequirement) * maxBarLength);

            String completedLoadingBar = "§3§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
            int formattingCodeLength = 4;
            String uncompletedLoadingBar = "§f§m" + baseLoadingBar.substring(Math.min(completedLoadingBar.length() - formattingCodeLength, maxBarLength));

            lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §b" + StringUtility.commaify(currentProgress) + "§3/§b" + StringUtility.shortenNumber(currentRequirement));

            lore.add("");

            // Total kill progress*
            int totalUnlockedPercentage = (int) (kills / totalRequirement * 100);
            lore.add("§7Overall Progress: §b" + totalUnlockedPercentage + "%");

            int totalCompletedLength = (int) Math.round((kills / totalRequirement) * maxBarLength);
            String totalCompletedBar = "§3§m" + baseLoadingBar.substring(0, Math.min(totalCompletedLength, maxBarLength));
            String totalUncompletedBar = "§f§m" + baseLoadingBar.substring(Math.min(totalCompletedBar.length() - formattingCodeLength, maxBarLength));

            lore.add(totalCompletedBar + totalUncompletedBar + "§r §b" + StringUtility.commaify(kills) + "§3/§b" + StringUtility.shortenNumber(totalRequirement));

            if (mob.getMaxBestiaryTier() > tier) {
                lore.add("§8Capped at Tier " + StringUtility.getAsRomanNumeral(mob.getMaxBestiaryTier()));
                lore.add("");
            }

            if (tier < mob.getMaxBestiaryTier()) {
                bestiaryData.getNextBonuses(lore, bestiaryEntry.getName(), tier + 1);
                lore.add("");
            }

            return lore;
        }
    }
}
