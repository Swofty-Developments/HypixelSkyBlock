package net.swofty.commons.guild;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GuildData {
    private final UUID guildId;
    private String name;
    private String tag;
    private String tagColor;
    private final List<GuildMember> members;
    private final List<GuildRank> ranks;
    private long totalGexp;
    private int level;
    private String motd;
    private String description;
    private String discordLink;
    private boolean listedInFinder;
    private boolean slowChat;
    private boolean everyoneMuted;
    private long everyoneMutedExpiry;
    private long createdAt;

    public static final int MAX_MEMBERS = 125;
    private static final int MAX_CUSTOM_RANKS = 3;
    private static final int TAG_UNLOCK_LEVEL = 5;

    private static final long[] LEVEL_THRESHOLDS = {
        100000, 150000, 250000, 500000, 750000,
        1000000, 1250000, 1500000, 2000000, 2500000,
        2500000, 3000000, 3000000, 3500000, 4000000,
        4500000, 5000000, 5500000, 6000000, 6500000,
        7000000, 7500000, 8000000, 8500000, 9000000,
        10000000, 11000000, 12000000, 13000000, 14000000
    };

    public GuildData(UUID guildId, String name, UUID masterUuid) {
        this.guildId = guildId;
        this.name = name;
        this.tag = "";
        this.tagColor = "§7";
        this.members = new ArrayList<>();
        this.ranks = new ArrayList<>(List.of(
            GuildRank.guildMaster(),
            GuildRank.officer(),
            GuildRank.member()
        ));
        this.totalGexp = 0;
        this.level = 0;
        this.motd = "";
        this.description = "";
        this.discordLink = "";
        this.listedInFinder = true;
        this.slowChat = false;
        this.everyoneMuted = false;
        this.everyoneMutedExpiry = 0;
        this.createdAt = System.currentTimeMillis();

        this.members.add(new GuildMember(masterUuid, "Guild Master", System.currentTimeMillis()));
    }

    private GuildData(UUID guildId, String name, String tag, String tagColor,
                      List<GuildMember> members, List<GuildRank> ranks,
                      long totalGexp, int level, String motd, String description,
                      String discordLink, boolean listedInFinder, boolean slowChat,
                      boolean everyoneMuted, long everyoneMutedExpiry, long createdAt) {
        this.guildId = guildId;
        this.name = name;
        this.tag = tag;
        this.tagColor = tagColor;
        this.members = members;
        this.ranks = ranks;
        this.totalGexp = totalGexp;
        this.level = level;
        this.motd = motd;
        this.description = description;
        this.discordLink = discordLink;
        this.listedInFinder = listedInFinder;
        this.slowChat = slowChat;
        this.everyoneMuted = everyoneMuted;
        this.everyoneMutedExpiry = everyoneMutedExpiry;
        this.createdAt = createdAt;
    }

    public UUID getMasterUuid() {
        return members.stream()
            .filter(m -> m.getRankName().equals("Guild Master"))
            .map(GuildMember::getUuid)
            .findFirst()
            .orElseThrow();
    }

    public GuildMember getMember(UUID uuid) {
        return members.stream()
            .filter(m -> m.getUuid().equals(uuid))
            .findFirst()
            .orElse(null);
    }

    public GuildRank getRank(String name) {
        return ranks.stream()
            .filter(r -> r.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    public GuildRank getMemberRank(UUID uuid) {
        GuildMember member = getMember(uuid);
        if (member == null) return null;
        return getRank(member.getRankName());
    }

    public boolean isFull() {
        return members.size() >= MAX_MEMBERS;
    }

    public boolean canAddCustomRank() {
        long customCount = ranks.stream().filter(r -> !r.isDefault()).count();
        return customCount < MAX_CUSTOM_RANKS;
    }

    public boolean canSetTag() {
        return level >= TAG_UNLOCK_LEVEL;
    }

    public int getMaxTagLength() {
        if (level >= 50) return 7;
        return 5;
    }

    public boolean canUseSpecialTagChars() {
        return level >= 65;
    }

    public List<UUID> getAllMemberUuids() {
        return members.stream().map(GuildMember::getUuid).toList();
    }

    public void addGexp(long amount) {
        this.totalGexp += amount;
        recalculateLevel();
    }

    public long getGexpForLevel(int targetLevel) {
        if (targetLevel <= 0) return 0;
        int idx = Math.min(targetLevel - 1, LEVEL_THRESHOLDS.length - 1);
        return LEVEL_THRESHOLDS[idx];
    }

    private void recalculateLevel() {
        long accumulated = 0;
        int newLevel = 0;
        for (int i = 0; i < 200; i++) {
            long needed = getGexpForLevel(i + 1);
            accumulated += needed;
            if (totalGexp >= accumulated) {
                newLevel = i + 1;
            } else {
                break;
            }
        }
        this.level = newLevel;
    }

    public GuildRank getNextRankUp(GuildRank currentRank) {
        return ranks.stream()
            .filter(r -> r.getPriority() < currentRank.getPriority())
            .max(Comparator.comparingInt(GuildRank::getPriority))
            .orElse(null);
    }

    public GuildRank getNextRankDown(GuildRank currentRank) {
        return ranks.stream()
            .filter(r -> r.getPriority() > currentRank.getPriority())
            .min(Comparator.comparingInt(GuildRank::getPriority))
            .orElse(null);
    }

    public Serializer<GuildData> getSerializer() {
        return getStaticSerializer();
    }

    public static Serializer<GuildData> getStaticSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildData value) {
                JSONObject json = new JSONObject();
                json.put("guildId", value.guildId.toString());
                json.put("name", value.name);
                json.put("tag", value.tag);
                json.put("tagColor", value.tagColor);
                json.put("totalGexp", value.totalGexp);
                json.put("level", value.level);
                json.put("motd", value.motd);
                json.put("description", value.description);
                json.put("discordLink", value.discordLink);
                json.put("listedInFinder", value.listedInFinder);
                json.put("slowChat", value.slowChat);
                json.put("everyoneMuted", value.everyoneMuted);
                json.put("everyoneMutedExpiry", value.everyoneMutedExpiry);
                json.put("createdAt", value.createdAt);

                JSONArray membersArr = new JSONArray();
                Serializer<GuildMember> memberSerializer = GuildMember.serializer();
                for (GuildMember m : value.members) {
                    membersArr.put(new JSONObject(memberSerializer.serialize(m)));
                }
                json.put("members", membersArr);

                JSONArray ranksArr = new JSONArray();
                Serializer<GuildRank> rankSerializer = GuildRank.serializer();
                for (GuildRank r : value.ranks) {
                    ranksArr.put(new JSONObject(rankSerializer.serialize(r)));
                }
                json.put("ranks", ranksArr);

                return json.toString();
            }

            @Override
            public GuildData deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                Serializer<GuildMember> memberSerializer = GuildMember.serializer();
                Serializer<GuildRank> rankSerializer = GuildRank.serializer();

                List<GuildMember> members = new ArrayList<>();
                JSONArray membersArr = obj.getJSONArray("members");
                for (int i = 0; i < membersArr.length(); i++) {
                    members.add(memberSerializer.deserialize(membersArr.getJSONObject(i).toString()));
                }

                List<GuildRank> ranks = new ArrayList<>();
                JSONArray ranksArr = obj.getJSONArray("ranks");
                for (int i = 0; i < ranksArr.length(); i++) {
                    ranks.add(rankSerializer.deserialize(ranksArr.getJSONObject(i).toString()));
                }

                return new GuildData(
                    UUID.fromString(obj.getString("guildId")),
                    obj.getString("name"),
                    obj.getString("tag"),
                    obj.getString("tagColor"),
                    members,
                    ranks,
                    obj.getLong("totalGexp"),
                    obj.getInt("level"),
                    obj.getString("motd"),
                    obj.getString("description"),
                    obj.getString("discordLink"),
                    obj.getBoolean("listedInFinder"),
                    obj.getBoolean("slowChat"),
                    obj.getBoolean("everyoneMuted"),
                    obj.getLong("everyoneMutedExpiry"),
                    obj.getLong("createdAt")
                );
            }

            @Override
            public GuildData clone(GuildData value) {
                return deserialize(serialize(value));
            }
        };
    }
}
