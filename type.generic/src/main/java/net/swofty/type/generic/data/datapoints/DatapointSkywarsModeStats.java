package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.skywars.SkywarsLeaderboardMode;
import net.swofty.commons.skywars.SkywarsLeaderboardPeriod;
import net.swofty.commons.skywars.SkywarsModeStats;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.generic.leaderboard.MapLeaderboardTracked;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatapointSkywarsModeStats extends Datapoint<SkywarsModeStats>
        implements MapLeaderboardTracked {

    private static final String LEADERBOARD_PREFIX = "skywars";

    public DatapointSkywarsModeStats(String key, SkywarsModeStats value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(SkywarsModeStats value) {
                JSONObject json = new JSONObject();
                json.put("wins", new JSONObject(value.getWins()));
                json.put("losses", new JSONObject(value.getLosses()));
                json.put("kills", new JSONObject(value.getKills()));
                json.put("deaths", new JSONObject(value.getDeaths()));
                json.put("assists", new JSONObject(value.getAssists()));
                json.put("meleeKills", new JSONObject(value.getMeleeKills()));
                json.put("bowKills", new JSONObject(value.getBowKills()));
                json.put("voidKills", new JSONObject(value.getVoidKills()));
                json.put("arrowsShot", new JSONObject(value.getArrowsShot()));
                json.put("arrowsHit", new JSONObject(value.getArrowsHit()));
                json.put("chestsOpened", new JSONObject(value.getChestsOpened()));
                json.put("soulsGathered", new JSONObject(value.getSoulsGathered()));
                json.put("heads", new JSONObject(value.getHeads()));
                json.put("winstreaks", new JSONObject(value.getWinstreaks()));
                json.put("dailyReset", value.getDailyResetTimestamp());
                json.put("weeklyReset", value.getWeeklyResetTimestamp());
                json.put("monthlyReset", value.getMonthlyResetTimestamp());
                return json.toString();
            }

            @Override
            public SkywarsModeStats deserialize(String json) {
                if (json == null || json.isEmpty()) {
                    return SkywarsModeStats.empty();
                }
                JSONObject obj = new JSONObject(json);

                Map<String, Long> wins = parseMap(obj.optJSONObject("wins"));
                Map<String, Long> losses = parseMap(obj.optJSONObject("losses"));
                Map<String, Long> kills = parseMap(obj.optJSONObject("kills"));
                Map<String, Long> deaths = parseMap(obj.optJSONObject("deaths"));
                Map<String, Long> assists = parseMap(obj.optJSONObject("assists"));
                Map<String, Long> meleeKills = parseMap(obj.optJSONObject("meleeKills"));
                Map<String, Long> bowKills = parseMap(obj.optJSONObject("bowKills"));
                Map<String, Long> voidKills = parseMap(obj.optJSONObject("voidKills"));
                Map<String, Long> arrowsShot = parseMap(obj.optJSONObject("arrowsShot"));
                Map<String, Long> arrowsHit = parseMap(obj.optJSONObject("arrowsHit"));
                Map<String, Long> chestsOpened = parseMap(obj.optJSONObject("chestsOpened"));
                Map<String, Long> soulsGathered = parseMap(obj.optJSONObject("soulsGathered"));
                Map<String, Long> heads = parseMap(obj.optJSONObject("heads"));
                Map<String, Long> winstreaks = parseMap(obj.optJSONObject("winstreaks"));

                long dailyReset = obj.optLong("dailyReset", 0);
                long weeklyReset = obj.optLong("weeklyReset", 0);
                long monthlyReset = obj.optLong("monthlyReset", 0);

                SkywarsModeStats stats = new SkywarsModeStats(wins, losses, kills, deaths, assists,
                        meleeKills, bowKills, voidKills, arrowsShot, arrowsHit, chestsOpened,
                        soulsGathered, heads, winstreaks, dailyReset, weeklyReset, monthlyReset);
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
            public SkywarsModeStats clone(SkywarsModeStats value) {
                return value.copy();
            }
        });
    }

    public DatapointSkywarsModeStats(String key) {
        this(key, SkywarsModeStats.empty());
    }

    @Override
    public String getLeaderboardPrefix() {
        return LEADERBOARD_PREFIX;
    }

    @Override
    public Map<String, Double> getAllLeaderboardScores() {
        Map<String, Double> scores = new HashMap<>();
        SkywarsModeStats stats = getValue();
        if (stats == null) return scores;

        for (SkywarsLeaderboardMode mode : SkywarsLeaderboardMode.values()) {
            for (SkywarsLeaderboardPeriod period : SkywarsLeaderboardPeriod.values()) {
                String suffix = mode.getKey() + ":" + period.getKey();

                scores.put("wins:" + suffix, (double) stats.getWins(mode, period));
                scores.put("kills:" + suffix, (double) stats.getKills(mode, period));
            }
        }
        return scores;
    }
}
