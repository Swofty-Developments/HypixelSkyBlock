package net.swofty.types.generic.data.datapoints;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.event.custom.SkyBlockXPModificationEvent;
import net.swofty.types.generic.levels.SkyBlockEmblems;
import net.swofty.types.generic.levels.SkyBlockLevelCause;
import net.swofty.types.generic.levels.SkyBlockLevelRequirement;
import net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.*;

public class DatapointSkyBlockExperience extends Datapoint<DatapointSkyBlockExperience.PlayerSkyBlockExperience> {
    private static final Serializer<PlayerSkyBlockExperience> serializer = new Serializer<>() {
        @Override
        public String serialize(PlayerSkyBlockExperience value) {
            List<String> serialized = new ArrayList<>();

            value.getCompletedExperienceCauses().forEach((cause) -> {
                serialized.add(SkyBlockLevelCause.getKey(cause));
            });

            if (value.getCurrentEmblem() == null) return new JSONObject(new HashMap<>(Map.of(
                    "values", serialized
            ))).toString();

            return new JSONObject(new HashMap<>(Map.of(
                    "values", serialized,
                    "emblem", value.getCurrentEmblem().getKey().name() + ":" + value.getCurrentEmblem().getValue()
            ))).toString();
        }

        @Override
        public PlayerSkyBlockExperience deserialize(String json) {
            List<net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr> experience = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(json);
            jsonObject.getJSONArray("values").forEach((value) -> {
                if (value instanceof String)
                    experience.add(SkyBlockLevelCause.getCause((String) value));
            });

            if (!jsonObject.has("emblem") || jsonObject.isNull("emblem")) return new PlayerSkyBlockExperience(experience, null);
            String[] emblem = jsonObject.getString("emblem").split(":");
            AbstractMap.SimpleEntry<SkyBlockEmblems, Integer> currentEmblem = new HashMap.SimpleEntry<>(SkyBlockEmblems.valueOf(emblem[0]), Integer.parseInt(emblem[1]));

            return new PlayerSkyBlockExperience(experience, currentEmblem);
        }

        @Override
        public PlayerSkyBlockExperience clone(PlayerSkyBlockExperience value) {
            return new PlayerSkyBlockExperience(value.getCompletedExperienceCauses(), value.getCurrentEmblem());
        }
    };

    public DatapointSkyBlockExperience(String key, PlayerSkyBlockExperience value) {
        super(key, value, serializer);
    }

    public DatapointSkyBlockExperience(String key) {
        this(key, new PlayerSkyBlockExperience());
    }

    @NoArgsConstructor
    @Getter
    public static class PlayerSkyBlockExperience {
        private List<net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr> completedExperienceCauses = new ArrayList<>();
        private Map.Entry<SkyBlockEmblems, Integer> currentEmblem = null;
        @Setter
        private SkyBlockPlayer attachedPlayer = null;

        public PlayerSkyBlockExperience(List<net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr> completedExperienceCauses, Map.Entry<SkyBlockEmblems, Integer> currentEmblem) {
            this.completedExperienceCauses = completedExperienceCauses;
            this.currentEmblem = currentEmblem;
        }

        public List<SkyBlockLevelCauseAbstr> getOfType(Class type) {
            return completedExperienceCauses.stream().filter(type::isInstance).toList();
        }

        public @Nullable SkyBlockEmblems.SkyBlockEmblem getEmblem() {
            if (currentEmblem == null) return null;
            return currentEmblem.getKey().getEmblems().get(currentEmblem.getValue());
        }

        public void setEmblem(SkyBlockEmblems emblems, SkyBlockEmblems.SkyBlockEmblem emblem) {
            currentEmblem = new HashMap.SimpleEntry<>(SkyBlockEmblems.getCategoryFromEmblem(emblem),
                    emblems.getEmblems().indexOf(emblem));
        }

        public boolean hasExperienceFor(net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr cause) {
            return completedExperienceCauses.contains(cause);
        }

        public void addExperience(net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr cause) {
            if (completedExperienceCauses.contains(cause)) return;
            String causeKey = SkyBlockLevelCause.getKey(cause);
            if (completedExperienceCauses.stream().anyMatch((c) -> SkyBlockLevelCause.getKey(c).equals(causeKey))) return;

            double oldXP = getTotalXP();
            completedExperienceCauses.add(cause);
            double newXP = getTotalXP();

            if (getAttachedPlayer() != null)
                SkyBlockEventHandler.callSkyBlockEvent(new SkyBlockXPModificationEvent(
                        getAttachedPlayer(), cause, oldXP, newXP));
        }

        public String getNextLevelDisplay() {
            SkyBlockLevelRequirement nextLevel = getLevel().getNextLevel();
            if (nextLevel == null) return "§cMAX";

            int nextLevelXP = nextLevel.getExperience() - nextLevel.getExperienceOfAllPreviousLevels();
            double totalXP = getTotalXP() - nextLevel.getExperienceOfAllPreviousLevels();

            String baseLoadingBar = "─────────────────";
            int maxBarLength = baseLoadingBar.length();
            int completedLength = (int) ((totalXP / (double) nextLevelXP) * maxBarLength);

            String completedLoadingBar = "§3§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
            int formattingCodeLength = 4;  // Adjust this if you add or remove formatting codes
            String uncompletedLoadingBar = "§f§m" + baseLoadingBar.substring(Math.min(
                    completedLoadingBar.length() - formattingCodeLength,  // Adjust for added formatting codes
                    maxBarLength
            ));

            return "§7" + completedLoadingBar + uncompletedLoadingBar + "§r §b" + Math.round(totalXP) + "§3/§b" + nextLevelXP;
        }

        public Double getTotalXP() {
            if (completedExperienceCauses.isEmpty()) return 0.0;
            return completedExperienceCauses.stream().mapToDouble(net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr::xpReward).sum();
        }

        public SkyBlockLevelRequirement getLevel() {
            return SkyBlockLevelRequirement.getFromTotalXP(getTotalXP());
        }
    }
}
