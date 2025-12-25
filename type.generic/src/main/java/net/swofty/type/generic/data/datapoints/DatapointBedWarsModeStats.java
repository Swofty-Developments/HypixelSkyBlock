package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.bedwars.BedWarsModeStats;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.generic.data.Datapoint;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatapointBedWarsModeStats extends Datapoint<BedWarsModeStats> {

	public DatapointBedWarsModeStats(String key, BedWarsModeStats value) {
		super(key, value, new Serializer<>() {
			@Override
			public String serialize(BedWarsModeStats value) {
				JSONObject json = new JSONObject();
				json.put("wins", new JSONObject(value.getWins()));
				json.put("finalKills", new JSONObject(value.getFinalKills()));
				json.put("bedsBroken", new JSONObject(value.getBedsBroken()));
				json.put("losses", new JSONObject(value.getLosses()));
				json.put("bedsLost", new JSONObject(value.getBedsLost()));
				json.put("kills", new JSONObject(value.getKills()));
				json.put("deaths", new JSONObject(value.getDeaths()));
				json.put("finalDeaths", new JSONObject(value.getFinalDeaths()));
				json.put("winstreaks", new JSONObject(value.getWinstreaks()));
				json.put("dailyReset", value.getDailyResetTimestamp());
				json.put("weeklyReset", value.getWeeklyResetTimestamp());
				json.put("monthlyReset", value.getMonthlyResetTimestamp());
				return json.toString();
			}

			@Override
			public BedWarsModeStats deserialize(String json) {
				if (json == null || json.isEmpty()) {
					return BedWarsModeStats.empty();
				}
				JSONObject obj = new JSONObject(json);

				Map<String, Long> wins = parseMap(obj.optJSONObject("wins"));
				Map<String, Long> finalKills = parseMap(obj.optJSONObject("finalKills"));
				Map<String, Long> bedsBroken = parseMap(obj.optJSONObject("bedsBroken"));
				Map<String, Long> losses = parseMap(obj.optJSONObject("losses"));
				Map<String, Long> bedsLost = parseMap(obj.optJSONObject("bedsLost"));
				Map<String, Long> kills = parseMap(obj.optJSONObject("kills"));
				Map<String, Long> deaths = parseMap(obj.optJSONObject("deaths"));
				Map<String, Long> finalDeaths = parseMap(obj.optJSONObject("finalDeaths"));
				Map<String, Long> winstreaks = parseMap(obj.optJSONObject("winstreaks"));

				long dailyReset = obj.optLong("dailyReset", 0);
				long weeklyReset = obj.optLong("weeklyReset", 0);
				long monthlyReset = obj.optLong("monthlyReset", 0);

				BedWarsModeStats stats = new BedWarsModeStats(wins, finalKills, bedsBroken,
						losses, bedsLost, kills, deaths, finalDeaths, winstreaks,
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
			public BedWarsModeStats clone(BedWarsModeStats value) {
				return value.copy();
			}
		});
	}

	public DatapointBedWarsModeStats(String key) {
		this(key, BedWarsModeStats.empty());
	}
}
