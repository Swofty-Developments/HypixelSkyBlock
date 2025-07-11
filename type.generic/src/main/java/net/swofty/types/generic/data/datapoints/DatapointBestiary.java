package net.swofty.types.generic.data.datapoints;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.StringUtility;
import net.swofty.commons.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.entity.mob.BestiaryMob;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.event.custom.BestiaryUpdateEvent;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.json.JSONObject;

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

        public List<String> getDisplay(List<String> lore, double currentProgress, double currentRequirement, double totalKills, double totalRequirement, String prefix) {

            // Current tier progress
            int unlockedPercentage = (int) (currentProgress / currentRequirement * 100);
            lore.add("§7" + prefix + " §b" + unlockedPercentage + "%");

            String baseLoadingBar = "─────────────────";
            int maxBarLength = baseLoadingBar.length();
            int completedLength = (int) Math.round((currentProgress / currentRequirement) * maxBarLength);

            String completedLoadingBar = "§3§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
            int formattingCodeLength = 4;
            String uncompletedLoadingBar = "§f§m" + baseLoadingBar.substring(Math.min(
                    completedLoadingBar.length() - formattingCodeLength,
                    maxBarLength
            ));

            lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §b" +
                    StringUtility.commaify(currentProgress) + "§3/§b" + StringUtility.shortenNumber(currentRequirement));

            lore.add("");

            // Total kill progress*
            int totalUnlockedPercentage = (int) (totalKills / totalRequirement * 100);
            lore.add("§7Overall Progress: §b" + totalUnlockedPercentage + "%");

            int totalCompletedLength = (int) Math.round((totalKills / totalRequirement) * maxBarLength);
            String totalCompletedBar = "§3§m" + baseLoadingBar.substring(0, Math.min(totalCompletedLength, maxBarLength));
            String totalUncompletedBar = "§f§m" + baseLoadingBar.substring(Math.min(
                    totalCompletedBar.length() - formattingCodeLength,
                    maxBarLength
            ));

            lore.add(totalCompletedBar + totalUncompletedBar + "§r §b" +
                    StringUtility.commaify(totalKills) + "§3/§b" + StringUtility.shortenNumber(totalRequirement));

            return lore;
        }
    }
}
