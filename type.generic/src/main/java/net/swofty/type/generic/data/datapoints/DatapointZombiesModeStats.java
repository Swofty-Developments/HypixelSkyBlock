package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.zombies.ZombiesLeaderboardMode;
import net.swofty.commons.zombies.ZombiesLeaderboardPeriod;
import net.swofty.commons.zombies.ZombiesModeStats;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.generic.leaderboard.MapLeaderboardTracked;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatapointZombiesModeStats extends Datapoint<ZombiesModeStats> implements MapLeaderboardTracked {

    private static final String LEADERBOARD_PREFIX = "zombies";

    public DatapointZombiesModeStats(String key, ZombiesModeStats value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(ZombiesModeStats value) {
                JSONObject json = new JSONObject();

                json.put("roundsSurvived", value.getRoundsSurvived());
                json.put("wins", value.getWins());
                json.put("bestRound", value.getBestRound());
                json.put("kills", value.getKills());
                json.put("playersRevived", value.getPlayersRevived());
                json.put("doorsOpened", value.getDoorsOpened());
                json.put("windowsRepaired", value.getWindowsRepaired());
                json.put("timesKnockedDown", value.getTimesKnockedDown());
                json.put("deaths", value.getDeaths());

                json.put("dailyReset", value.getDailyResetTimestamp());
                json.put("weeklyReset", value.getWeeklyResetTimestamp());
                json.put("monthlyReset", value.getMonthlyResetTimestamp());
                return json.toString();
            }

            @Override
            public ZombiesModeStats deserialize(String json) {
                if (json == null || json.isEmpty()) {
                    return ZombiesModeStats.empty();
                }

                JSONObject obj = new JSONObject(json);

                Map<String, Long> roundsSurvived = parseMap(obj.optJSONObject("roundsSurvived"));
                Map<String, Long> wins = parseMap(obj.optJSONObject("wins"));
                Map<String, Long> bestRound = parseMap(obj.optJSONObject("bestRound"));
                Map<String, Long> kills = parseMap(obj.optJSONObject("kills"));
                Map<String, Long> playersRevived = parseMap(obj.optJSONObject("playersRevived"));
                Map<String, Long> doorsOpened = parseMap(obj.optJSONObject("doorsOpened"));
                Map<String, Long> windowsRepaired = parseMap(obj.optJSONObject("windowsRepaired"));
                Map<String, Long> timesKnockedDown = parseMap(obj.optJSONObject("timesKnockedDown"));
                Map<String, Long> deaths = parseMap(obj.optJSONObject("deaths"));

                long dailyReset = obj.optLong("dailyReset", 0);
                long weeklyReset = obj.optLong("weeklyReset", 0);
                long monthlyReset = obj.optLong("monthlyReset", 0);

                ZombiesModeStats stats = new ZombiesModeStats(
                        roundsSurvived, wins, bestRound, kills, playersRevived,
                        doorsOpened, windowsRepaired, timesKnockedDown, deaths,
                        dailyReset, weeklyReset, monthlyReset);
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
            public ZombiesModeStats clone(ZombiesModeStats value) {
                return value.copy();
            }
        });
    }

    public DatapointZombiesModeStats(String key) {
        this(key, ZombiesModeStats.empty());
    }

    @Override
    public String getLeaderboardPrefix() {
        return LEADERBOARD_PREFIX;
    }

    @Override
    public Map<String, Double> getAllLeaderboardScores() {
        Map<String, Double> scores = new HashMap<>();
        ZombiesModeStats stats = getValue();
        if (stats == null) return scores;

        for (ZombiesLeaderboardMode mode : ZombiesLeaderboardMode.values()) {
            for (ZombiesLeaderboardPeriod period : ZombiesLeaderboardPeriod.values()) {
                String suffix = mode.getKey() + ":" + period.getKey();

                scores.put("wins:" + suffix, (double) stats.getWins(mode, period));
                scores.put("rounds_survived:" + suffix, (double) stats.getRoundsSurvived(mode, period));
            }
        }
        return scores;
    }
}
