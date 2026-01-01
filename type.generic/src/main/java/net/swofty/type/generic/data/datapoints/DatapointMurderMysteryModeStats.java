package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.murdermystery.MurderMysteryLeaderboardMode;
import net.swofty.commons.murdermystery.MurderMysteryLeaderboardPeriod;
import net.swofty.commons.murdermystery.MurderMysteryModeStats;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.generic.leaderboard.MapLeaderboardTracked;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatapointMurderMysteryModeStats extends Datapoint<MurderMysteryModeStats>
        implements MapLeaderboardTracked {

    private static final String LEADERBOARD_PREFIX = "murdermystery";

    public DatapointMurderMysteryModeStats(String key, MurderMysteryModeStats value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(MurderMysteryModeStats value) {
                JSONObject json = new JSONObject();
                json.put("wins", new JSONObject(value.getWins()));
                json.put("kills", new JSONObject(value.getKills()));
                json.put("gamesPlayed", new JSONObject(value.getGamesPlayed()));
                json.put("bowKills", new JSONObject(value.getBowKills()));
                json.put("knifeKills", new JSONObject(value.getKnifeKills()));
                json.put("thrownKnifeKills", new JSONObject(value.getThrownKnifeKills()));
                json.put("trapKills", new JSONObject(value.getTrapKills()));
                json.put("detectiveWins", new JSONObject(value.getDetectiveWins()));
                json.put("murdererWins", new JSONObject(value.getMurdererWins()));
                json.put("killsAsHero", new JSONObject(value.getKillsAsHero()));
                json.put("killsAsMurderer", new JSONObject(value.getKillsAsMurderer()));
                json.put("survivorWins", new JSONObject(value.getSurvivorWins()));
                json.put("alphaWins", new JSONObject(value.getAlphaWins()));
                json.put("killsAsInfected", new JSONObject(value.getKillsAsInfected()));
                json.put("killsAsSurvivor", new JSONObject(value.getKillsAsSurvivor()));
                json.put("timeSurvived", new JSONObject(value.getTimeSurvived()));
                json.put("quickestDetectiveWin", new JSONObject(value.getQuickestDetectiveWin()));
                json.put("quickestMurdererWin", new JSONObject(value.getQuickestMurdererWin()));
                json.put("tokens", new JSONObject(value.getTokens()));
                json.put("weeklyReset", value.getWeeklyResetTimestamp());
                json.put("monthlyReset", value.getMonthlyResetTimestamp());
                return json.toString();
            }

            @Override
            public MurderMysteryModeStats deserialize(String json) {
                if (json == null || json.isEmpty()) {
                    return MurderMysteryModeStats.empty();
                }
                JSONObject obj = new JSONObject(json);

                Map<String, Long> wins = parseMap(obj.optJSONObject("wins"));
                Map<String, Long> kills = parseMap(obj.optJSONObject("kills"));
                Map<String, Long> gamesPlayed = parseMap(obj.optJSONObject("gamesPlayed"));
                Map<String, Long> bowKills = parseMap(obj.optJSONObject("bowKills"));
                Map<String, Long> knifeKills = parseMap(obj.optJSONObject("knifeKills"));
                Map<String, Long> thrownKnifeKills = parseMap(obj.optJSONObject("thrownKnifeKills"));
                Map<String, Long> trapKills = parseMap(obj.optJSONObject("trapKills"));
                Map<String, Long> detectiveWins = parseMap(obj.optJSONObject("detectiveWins"));
                Map<String, Long> murdererWins = parseMap(obj.optJSONObject("murdererWins"));
                Map<String, Long> killsAsHero = parseMap(obj.optJSONObject("killsAsHero"));
                Map<String, Long> killsAsMurderer = parseMap(obj.optJSONObject("killsAsMurderer"));
                Map<String, Long> survivorWins = parseMap(obj.optJSONObject("survivorWins"));
                Map<String, Long> alphaWins = parseMap(obj.optJSONObject("alphaWins"));
                Map<String, Long> killsAsInfected = parseMap(obj.optJSONObject("killsAsInfected"));
                Map<String, Long> killsAsSurvivor = parseMap(obj.optJSONObject("killsAsSurvivor"));
                Map<String, Long> timeSurvived = parseMap(obj.optJSONObject("timeSurvived"));
                Map<String, Long> quickestDetectiveWin = parseMap(obj.optJSONObject("quickestDetectiveWin"));
                Map<String, Long> quickestMurdererWin = parseMap(obj.optJSONObject("quickestMurdererWin"));
                Map<String, Long> tokens = parseMap(obj.optJSONObject("tokens"));

                long weeklyReset = obj.optLong("weeklyReset", 0);
                long monthlyReset = obj.optLong("monthlyReset", 0);

                MurderMysteryModeStats stats = new MurderMysteryModeStats(
                        wins, kills, gamesPlayed, bowKills, knifeKills, thrownKnifeKills, trapKills,
                        detectiveWins, murdererWins, killsAsHero, killsAsMurderer, survivorWins, alphaWins,
                        killsAsInfected, killsAsSurvivor, timeSurvived,
                        quickestDetectiveWin, quickestMurdererWin,
                        tokens,
                        weeklyReset, monthlyReset
                );
                stats.checkAndResetExpiredPeriods();
                return stats;
            }

            private Map<String, Long> parseMap(JSONObject obj) {
                Map<String, Long> map = new HashMap<>();
                if (obj != null) {
                    for (String key : obj.keySet()) {
                        map.put(key, obj.getLong(key));
                    }
                }
                return map;
            }

            @Override
            public MurderMysteryModeStats clone(MurderMysteryModeStats value) {
                return value.copy();
            }
        });
    }

    public DatapointMurderMysteryModeStats(String key) {
        this(key, MurderMysteryModeStats.empty());
    }

    // ============ MapLeaderboardTracked Implementation ============

    @Override
    public String getLeaderboardPrefix() {
        return LEADERBOARD_PREFIX;
    }

    @Override
    public Map<String, Double> getAllLeaderboardScores() {
        Map<String, Double> scores = new HashMap<>();
        MurderMysteryModeStats stats = getValue();
        if (stats == null) return scores;

        for (MurderMysteryLeaderboardMode mode : MurderMysteryLeaderboardMode.values()) {
            for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
                String suffix = mode.getKey() + ":" + period.getKey();

                // Stats displayed on leaderboard holograms
                scores.put("wins:" + suffix, (double) stats.getWins(mode, period));
                scores.put("kills:" + suffix, (double) stats.getKills(mode, period));
                scores.put("kills_as_murderer:" + suffix, (double) stats.getKillsAsMurderer(mode, period));

                // Additional stats that could be displayed
                scores.put("games_played:" + suffix, (double) stats.getGamesPlayed(mode, period));
                scores.put("detective_wins:" + suffix, (double) stats.getDetectiveWins(mode, period));
                scores.put("murderer_wins:" + suffix, (double) stats.getMurdererWins(mode, period));
                scores.put("kills_as_hero:" + suffix, (double) stats.getKillsAsHero(mode, period));
            }
        }
        return scores;
    }
}
