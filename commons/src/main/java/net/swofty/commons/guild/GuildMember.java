package net.swofty.commons.guild;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class GuildMember {
    private final UUID uuid;
    private String rankName;
    private long joinedAt;
    private long weeklyGexp;
    private long totalGexp;
    private long lastLogin;
    private boolean notificationsEnabled;
    private long mutedUntil;
    private boolean guildChatEnabled = true;

    public GuildMember(UUID uuid, String rankName, long joinedAt) {
        this.uuid = uuid;
        this.rankName = rankName;
        this.joinedAt = joinedAt;
        this.weeklyGexp = 0;
        this.totalGexp = 0;
        this.lastLogin = System.currentTimeMillis();
        this.notificationsEnabled = true;
        this.mutedUntil = 0;
    }

    @JsonCreator
    public GuildMember(@JsonProperty("uuid") UUID uuid, @JsonProperty("rankName") String rankName, @JsonProperty("joinedAt") long joinedAt, @JsonProperty("weeklyGexp") long weeklyGexp,
                       @JsonProperty("totalGexp") long totalGexp, @JsonProperty("lastLogin") long lastLogin, @JsonProperty("notificationsEnabled") boolean notificationsEnabled,
                       @JsonProperty("mutedUntil") long mutedUntil, @JsonProperty("guildChatEnabled") Boolean guildChatEnabled) {
        this.uuid = uuid;
        this.rankName = rankName;
        this.joinedAt = joinedAt;
        this.weeklyGexp = weeklyGexp;
        this.totalGexp = totalGexp;
        this.lastLogin = lastLogin;
        this.notificationsEnabled = notificationsEnabled;
        this.mutedUntil = mutedUntil;
        this.guildChatEnabled = guildChatEnabled == null || guildChatEnabled;
    }

    @JsonIgnore
    public boolean isMuted() {
        return mutedUntil > System.currentTimeMillis();
    }

    public static Serializer<GuildMember> serializer() {
        return new JacksonSerializer<>(GuildMember.class);
    }
}
