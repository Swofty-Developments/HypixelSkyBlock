package net.swofty.commons.guild;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.Serializer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(force = true)
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

    @JsonCreator
    private GuildData(@JsonProperty("guildId") UUID guildId, @JsonProperty("name") String name, @JsonProperty("tag") String tag, @JsonProperty("tagColor") String tagColor,
                      @JsonProperty("members") List<GuildMember> members, @JsonProperty("ranks") List<GuildRank> ranks,
                      @JsonProperty("totalGexp") long totalGexp, @JsonProperty("level") int level, @JsonProperty("motd") String motd, @JsonProperty("description") String description,
                      @JsonProperty("discordLink") String discordLink, @JsonProperty("listedInFinder") boolean listedInFinder, @JsonProperty("slowChat") boolean slowChat,
                      @JsonProperty("everyoneMuted") boolean everyoneMuted, @JsonProperty("everyoneMutedExpiry") long everyoneMutedExpiry, @JsonProperty("createdAt") long createdAt) {
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

    @JsonIgnore
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

    @JsonIgnore
    public boolean isFull() {
        return members.size() >= MAX_MEMBERS;
    }

    @JsonIgnore
    public boolean canAddCustomRank() {
        long customCount = ranks.stream().filter(r -> !r.isDefault()).count();
        return customCount < MAX_CUSTOM_RANKS;
    }

    @JsonIgnore
    public boolean canSetTag() {
        return level >= TAG_UNLOCK_LEVEL;
    }

    @JsonIgnore
    public int getMaxTagLength() {
        if (level >= 50) return 7;
        return 5;
    }

    @JsonIgnore
    public boolean canUseSpecialTagChars() {
        return level >= 65;
    }

    @JsonIgnore
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

    @JsonIgnore
    public Serializer<GuildData> getSerializer() {
        return getStaticSerializer();
    }

    public static Serializer<GuildData> getStaticSerializer() {
        return new JacksonSerializer<>(GuildData.class);
    }
}
