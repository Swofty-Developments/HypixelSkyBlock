package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.protocol.Serializer;
import net.swofty.type.generic.data.Datapoint;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores per-kit statistics for SkyWars.
 */
public class DatapointSkywarsKitStats extends Datapoint<DatapointSkywarsKitStats.SkywarsKitStats> {

    public DatapointSkywarsKitStats(String key, SkywarsKitStats value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(SkywarsKitStats value) {
                JSONObject json = new JSONObject();

                JSONObject kitStatsJson = new JSONObject();
                for (Map.Entry<String, KitStatistics> entry : value.getKitStats().entrySet()) {
                    kitStatsJson.put(entry.getKey(), entry.getValue().toJson());
                }
                json.put("kitStats", kitStatsJson);

                return json.toString();
            }

            @Override
            public SkywarsKitStats deserialize(String json) {
                if (json == null || json.isEmpty()) {
                    return SkywarsKitStats.empty();
                }

                JSONObject obj = new JSONObject(json);
                Map<String, KitStatistics> kitStats = new HashMap<>();

                JSONObject kitStatsJson = obj.optJSONObject("kitStats");
                if (kitStatsJson != null) {
                    for (String kitId : kitStatsJson.keySet()) {
                        JSONObject statsJson = kitStatsJson.getJSONObject(kitId);
                        kitStats.put(kitId, KitStatistics.fromJson(statsJson));
                    }
                }

                return new SkywarsKitStats(kitStats);
            }

            @Override
            public SkywarsKitStats clone(SkywarsKitStats value) {
                return value.copy();
            }
        });
    }

    public DatapointSkywarsKitStats(String key) {
        this(key, SkywarsKitStats.empty());
    }

    /**
     * Per-kit statistics for a specific kit
     */
    public static class KitStatistics {
        // General stats
        private long timePlayed;  // In seconds
        private int wins;
        private long fastestWin;  // In seconds
        private int mostKillsInGame;
        private int mobsKilled;
        private int chestsOpened;
        private int headsGathered;

        // Kill stats
        private int kills;
        private int assists;
        private int meleeKills;
        private int bowKills;
        private int voidKills;
        private int mobKills;

        // Archery stats
        private int arrowsShot;
        private int arrowsHit;
        private int longestBowKill;  // In blocks
        private int longestBowShot;  // In blocks

        // Challenge wins
        private int archerWins;
        private int halfHealthWins;
        private int noBlockWins;
        private int noChestWins;
        private int paperWins;
        private int rookieWins;
        private int uhcWins;
        private int ultimateWarriorWins;

        public KitStatistics() {
            // Default constructor with all zeros
        }

        // Getters
        public long getTimePlayed() { return timePlayed; }
        public int getWins() { return wins; }
        public long getFastestWin() { return fastestWin; }
        public int getMostKillsInGame() { return mostKillsInGame; }
        public int getMobsKilled() { return mobsKilled; }
        public int getChestsOpened() { return chestsOpened; }
        public int getHeadsGathered() { return headsGathered; }

        public int getKills() { return kills; }
        public int getAssists() { return assists; }
        public int getMeleeKills() { return meleeKills; }
        public int getBowKills() { return bowKills; }
        public int getVoidKills() { return voidKills; }
        public int getMobKills() { return mobKills; }

        public int getArrowsShot() { return arrowsShot; }
        public int getArrowsHit() { return arrowsHit; }
        public int getLongestBowKill() { return longestBowKill; }
        public int getLongestBowShot() { return longestBowShot; }

        public int getArcherWins() { return archerWins; }
        public int getHalfHealthWins() { return halfHealthWins; }
        public int getNoBlockWins() { return noBlockWins; }
        public int getNoChestWins() { return noChestWins; }
        public int getPaperWins() { return paperWins; }
        public int getRookieWins() { return rookieWins; }
        public int getUhcWins() { return uhcWins; }
        public int getUltimateWarriorWins() { return ultimateWarriorWins; }

        // Computed stats
        public double getAccuracy() {
            if (arrowsShot == 0) return 0;
            return (double) arrowsHit / arrowsShot * 100;
        }

        public String getFormattedTimePlayed() {
            if (timePlayed == 0) return "N/A";
            long hours = timePlayed / 3600;
            long minutes = (timePlayed % 3600) / 60;
            if (hours > 0) {
                return hours + "h " + minutes + "m";
            } else if (minutes > 0) {
                return minutes + "m";
            } else {
                return timePlayed + "s";
            }
        }

        public String getFormattedFastestWin() {
            if (fastestWin == 0) return "N/A";
            long minutes = fastestWin / 60;
            long seconds = fastestWin % 60;
            return minutes + "m " + seconds + "s";
        }

        // Incrementers
        public void addTimePlayed(long seconds) { this.timePlayed += seconds; }
        public void addWin() { this.wins++; }
        public void setFastestWin(long seconds) {
            if (fastestWin == 0 || seconds < fastestWin) {
                this.fastestWin = seconds;
            }
        }
        public void setMostKillsInGame(int kills) {
            if (kills > mostKillsInGame) {
                this.mostKillsInGame = kills;
            }
        }
        public void addMobKilled() { this.mobsKilled++; }
        public void addChestOpened() { this.chestsOpened++; }
        public void addHeadGathered() { this.headsGathered++; }

        public void addKill() { this.kills++; }
        public void addAssist() { this.assists++; }
        public void addMeleeKill() { this.meleeKills++; this.kills++; }
        public void addBowKill() { this.bowKills++; this.kills++; }
        public void addVoidKill() { this.voidKills++; this.kills++; }
        public void addMobKill() { this.mobKills++; this.kills++; }

        public void addArrowShot() { this.arrowsShot++; }
        public void addArrowHit() { this.arrowsHit++; }
        public void setLongestBowKill(int blocks) {
            if (blocks > longestBowKill) this.longestBowKill = blocks;
        }
        public void setLongestBowShot(int blocks) {
            if (blocks > longestBowShot) this.longestBowShot = blocks;
        }

        public void addArcherWin() { this.archerWins++; }
        public void addHalfHealthWin() { this.halfHealthWins++; }
        public void addNoBlockWin() { this.noBlockWins++; }
        public void addNoChestWin() { this.noChestWins++; }
        public void addPaperWin() { this.paperWins++; }
        public void addRookieWin() { this.rookieWins++; }
        public void addUhcWin() { this.uhcWins++; }
        public void addUltimateWarriorWin() { this.ultimateWarriorWins++; }

        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            json.put("timePlayed", timePlayed);
            json.put("wins", wins);
            json.put("fastestWin", fastestWin);
            json.put("mostKillsInGame", mostKillsInGame);
            json.put("mobsKilled", mobsKilled);
            json.put("chestsOpened", chestsOpened);
            json.put("headsGathered", headsGathered);

            json.put("kills", kills);
            json.put("assists", assists);
            json.put("meleeKills", meleeKills);
            json.put("bowKills", bowKills);
            json.put("voidKills", voidKills);
            json.put("mobKills", mobKills);

            json.put("arrowsShot", arrowsShot);
            json.put("arrowsHit", arrowsHit);
            json.put("longestBowKill", longestBowKill);
            json.put("longestBowShot", longestBowShot);

            json.put("archerWins", archerWins);
            json.put("halfHealthWins", halfHealthWins);
            json.put("noBlockWins", noBlockWins);
            json.put("noChestWins", noChestWins);
            json.put("paperWins", paperWins);
            json.put("rookieWins", rookieWins);
            json.put("uhcWins", uhcWins);
            json.put("ultimateWarriorWins", ultimateWarriorWins);

            return json;
        }

        public static KitStatistics fromJson(JSONObject json) {
            KitStatistics stats = new KitStatistics();
            stats.timePlayed = json.optLong("timePlayed", 0);
            stats.wins = json.optInt("wins", 0);
            stats.fastestWin = json.optLong("fastestWin", 0);
            stats.mostKillsInGame = json.optInt("mostKillsInGame", 0);
            stats.mobsKilled = json.optInt("mobsKilled", 0);
            stats.chestsOpened = json.optInt("chestsOpened", 0);
            stats.headsGathered = json.optInt("headsGathered", 0);

            stats.kills = json.optInt("kills", 0);
            stats.assists = json.optInt("assists", 0);
            stats.meleeKills = json.optInt("meleeKills", 0);
            stats.bowKills = json.optInt("bowKills", 0);
            stats.voidKills = json.optInt("voidKills", 0);
            stats.mobKills = json.optInt("mobKills", 0);

            stats.arrowsShot = json.optInt("arrowsShot", 0);
            stats.arrowsHit = json.optInt("arrowsHit", 0);
            stats.longestBowKill = json.optInt("longestBowKill", 0);
            stats.longestBowShot = json.optInt("longestBowShot", 0);

            stats.archerWins = json.optInt("archerWins", 0);
            stats.halfHealthWins = json.optInt("halfHealthWins", 0);
            stats.noBlockWins = json.optInt("noBlockWins", 0);
            stats.noChestWins = json.optInt("noChestWins", 0);
            stats.paperWins = json.optInt("paperWins", 0);
            stats.rookieWins = json.optInt("rookieWins", 0);
            stats.uhcWins = json.optInt("uhcWins", 0);
            stats.ultimateWarriorWins = json.optInt("ultimateWarriorWins", 0);

            return stats;
        }

        public KitStatistics copy() {
            KitStatistics copy = new KitStatistics();
            copy.timePlayed = this.timePlayed;
            copy.wins = this.wins;
            copy.fastestWin = this.fastestWin;
            copy.mostKillsInGame = this.mostKillsInGame;
            copy.mobsKilled = this.mobsKilled;
            copy.chestsOpened = this.chestsOpened;
            copy.headsGathered = this.headsGathered;

            copy.kills = this.kills;
            copy.assists = this.assists;
            copy.meleeKills = this.meleeKills;
            copy.bowKills = this.bowKills;
            copy.voidKills = this.voidKills;
            copy.mobKills = this.mobKills;

            copy.arrowsShot = this.arrowsShot;
            copy.arrowsHit = this.arrowsHit;
            copy.longestBowKill = this.longestBowKill;
            copy.longestBowShot = this.longestBowShot;

            copy.archerWins = this.archerWins;
            copy.halfHealthWins = this.halfHealthWins;
            copy.noBlockWins = this.noBlockWins;
            copy.noChestWins = this.noChestWins;
            copy.paperWins = this.paperWins;
            copy.rookieWins = this.rookieWins;
            copy.uhcWins = this.uhcWins;
            copy.ultimateWarriorWins = this.ultimateWarriorWins;

            return copy;
        }
    }

    public static class SkywarsKitStats {
        private final Map<String, KitStatistics> kitStats;

        public SkywarsKitStats(Map<String, KitStatistics> kitStats) {
            this.kitStats = new HashMap<>(kitStats);
        }

        public static SkywarsKitStats empty() {
            return new SkywarsKitStats(new HashMap<>());
        }

        public Map<String, KitStatistics> getKitStats() {
            return kitStats;
        }

        /**
         * Get statistics for a specific kit (creates if doesn't exist)
         */
        public KitStatistics getStatsForKit(String kitId) {
            return kitStats.computeIfAbsent(kitId, k -> new KitStatistics());
        }

        /**
         * Check if there are any stats for a kit
         */
        public boolean hasStatsForKit(String kitId) {
            return kitStats.containsKey(kitId);
        }

        public SkywarsKitStats copy() {
            Map<String, KitStatistics> copyStats = new HashMap<>();
            for (Map.Entry<String, KitStatistics> entry : kitStats.entrySet()) {
                copyStats.put(entry.getKey(), entry.getValue().copy());
            }
            return new SkywarsKitStats(copyStats);
        }
    }
}
