package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.StringUtility;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.bestiary.BestiaryData;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.entity.mob.BestiaryMob;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import net.swofty.type.skyblockgeneric.event.custom.BestiaryUpdateEvent;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bestiary.BestiaryCategories;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bestiary.BestiaryEntry;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.json.JSONObject;

import java.util.*;

public class DatapointBestiary extends SkyBlockDatapoint<DatapointBestiary.PlayerBestiary> {

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

        BestiaryData bestiaryData = new BestiaryData();

        public PlayerBestiary(Map<String, Integer> mobs) {
            this.mobs = mobs;
        }

        public void setRaw(BestiaryMob mob, int value) {
            int oldValue = getAmount(mob);
            mobs.put(mob.getMobID(), value);

            if (attachedPlayer != null) {
                HypixelEventHandler.callCustomEvent(new BestiaryUpdateEvent(
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

        public List<String> getMobDisplay(List<String> lore, int kills, BestiaryMob mob, BestiaryEntry bestiaryEntry) {

            int bracket = mob.getBestiaryBracket();
            int tier = bestiaryData.getCurrentBestiaryTier(mob, kills);
            double currentProgress = bestiaryData.getKillsToNextTier(mob, kills);
            double currentRequirement = bestiaryData.getTotalKillsForNextTier(bracket, tier + 1);
            double totalRequirement = bestiaryData.getTotalKillsForMaxTier(mob);
            DatapointDeaths.PlayerDeaths playerDeaths = attachedPlayer.getDeathData();
            int deaths = 0;

            for (BestiaryMob bestiaryMob : bestiaryEntry.getMobs()) {
                deaths += playerDeaths.getAmount(bestiaryMob.getMobID());
            }

            String baseLoadingBar = "─────────────────";
            int maxBarLength = baseLoadingBar.length();
            int formattingCodeLength = 4;

            List<MobType> mobtypes = mob.getMobTypes();

            if (mobtypes.size() == 1) {
                lore.add("§7Mob Type: " + mobtypes.getFirst().getFullDisplayName());
                lore.add("");
            } else if (mobtypes.size() > 1) {
                StringBuilder sb = new StringBuilder();
                for (MobType mobType : mobtypes) {
                    sb.append(mobType.getFullDisplayName());
                    sb.append("§7, ");
                }
                sb.delete(sb.chars().sum() - 3, sb.chars().sum());

                lore.add("§7Mob Types: " + sb);
                lore.add("");
            }

            lore.add("§7" + bestiaryEntry.getDescription());
            lore.add("");
            lore.add("§7Kills: §a" + kills);
            lore.add("§7Deaths: §a" + deaths);
            lore.add("");

            if (tier > 0) {
                bestiaryData.getTotalBonuses(lore, bestiaryEntry.getName(), tier);
                lore.add("");
            }

            // Current tier progress
            if (tier < mob.getMaxBestiaryTier()) {
                int unlockedPercentage = (int) (currentProgress / currentRequirement * 100);
                lore.add("§7Progress to Tier " + StringUtility.getAsRomanNumeral(tier + 1) + " §b" + unlockedPercentage + "%");

                int completedLength = (int) Math.round((currentProgress / currentRequirement) * maxBarLength);

                String completedLoadingBar = "§3§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
                String uncompletedLoadingBar = "§f§m" + baseLoadingBar.substring(Math.min(completedLoadingBar.length() - formattingCodeLength, maxBarLength));

                lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §b" + StringUtility.commaify(currentProgress) + "§3/§b" + StringUtility.shortenNumber(currentRequirement));
                lore.add("");
            }

            // Total kill progress*
            int totalUnlockedPercentage = (int) (kills / totalRequirement * 100);
            if (tier < mob.getMaxBestiaryTier()) {
                lore.add("§7Overall Progress: §b" + totalUnlockedPercentage + "%");
            } else {
                lore.add("§7Overall Progress: §b" + totalUnlockedPercentage + "% §7(§c§lMAX!§7)");
            }

            int totalCompletedLength = (int) Math.round((kills / totalRequirement) * maxBarLength);
            String totalCompletedBar = "§3§m" + baseLoadingBar.substring(0, Math.min(totalCompletedLength, maxBarLength));
            String totalUncompletedBar = "§f§m" + baseLoadingBar.substring(Math.min(totalCompletedBar.length() - formattingCodeLength, maxBarLength));

            lore.add(totalCompletedBar + totalUncompletedBar + "§r §b" + StringUtility.commaify(kills) + "§3/§b" + StringUtility.shortenNumber(totalRequirement));

            if (mob.getMaxBestiaryTier() > tier) {
                lore.add("§8Capped at Tier " + StringUtility.getAsRomanNumeral(mob.getMaxBestiaryTier()));
            }

            lore.add("");

            if (tier < mob.getMaxBestiaryTier()) {
                bestiaryData.getNextBonuses(lore, bestiaryEntry.getName(), tier + 1);
                lore.add("");
            }

            return lore;
        }

        public List<String> getTotalDisplay(List<String> lore) {

            List<BestiaryEntry> allEntries = new ArrayList<>();
            double totalFamilies = 0;
            double familiesFound = 0;
            double familiesCompleted = 0;

            String baseLoadingBar = "─────────────────";
            int maxBarLength = baseLoadingBar.length();
            int formattingCodeLength = 4;

            for (BestiaryCategories category : BestiaryCategories.values()) {
                allEntries.addAll(Arrays.asList(category.getEntries()));
            }

            totalFamilies = allEntries.size();

            for (BestiaryEntry entry : allEntries) {
                int kills = getAmount(entry.getMobs());
                if (kills > 0) familiesFound++;
                if (kills >= bestiaryData.getTotalKillsForMaxTier(entry.getMobs().getFirst())) familiesCompleted++;
            }

            lore.add("§7The Bestiary is a compendium of");
            lore.add("§7mobs in SkyBlock. It contains detailed");
            lore.add("§7information on loot drops, your mob");
            lore.add("§7stats, and more!");
            lore.add("");
            lore.add("§7Kill mobs within §aFamilies §7to progress");
            lore.add("§7and earn §arewards§7, including §b✯ Magic");
            lore.add("§bFind §7bonuses towards mobs in the");
            lore.add("§7Family.");
            lore.add("");

            // Families found
            int unlockedPercentage = (int) (familiesFound / totalFamilies * 100);
            if (familiesFound != totalFamilies) {
                lore.add("§7Families Found: §e" + unlockedPercentage + "%");
            } else {
                lore.add("§7Families Found: §e" + unlockedPercentage + "% §7(§c§lMAX!§7)");
            }

            int completedLength = (int) Math.round((familiesFound / totalFamilies) * maxBarLength);

            String completedLoadingBar = "§3§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
            String uncompletedLoadingBar = "§f§m" + baseLoadingBar.substring(Math.min(completedLoadingBar.length() - formattingCodeLength, maxBarLength));

            lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §b" + StringUtility.commaify(familiesFound) + "§3/§b" + StringUtility.shortenNumber(totalFamilies));
            lore.add("");

            // Families completed
            int totalUnlockedPercentage = (int) (familiesCompleted / totalFamilies * 100);
            if (familiesCompleted != totalFamilies) {
                lore.add("§7Families Completed: §e" + totalUnlockedPercentage + "%");
            } else {
                lore.add("§7Families Completed: §e" + totalUnlockedPercentage + "% §7(§c§lMAX!§7)");
            }

            int totalCompletedLength = (int) Math.round((familiesCompleted / totalFamilies) * maxBarLength);
            String totalCompletedBar = "§3§m" + baseLoadingBar.substring(0, Math.min(totalCompletedLength, maxBarLength));
            String totalUncompletedBar = "§f§m" + baseLoadingBar.substring(Math.min(totalCompletedBar.length() - formattingCodeLength, maxBarLength));

            lore.add(totalCompletedBar + totalUncompletedBar + "§r §b" + StringUtility.commaify(familiesCompleted) + "§3/§b" + StringUtility.shortenNumber(totalFamilies));

            return lore;
        }
    }
}
