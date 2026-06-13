package net.swofty.commons.guild;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.Serializer;

import java.util.EnumSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class GuildRank implements Comparable<GuildRank> {
    private String name;
    private int priority;
    private boolean isDefault;
    private Set<GuildPermission> permissions;

    @JsonCreator
    public GuildRank(@JsonProperty("name") String name, @JsonProperty("priority") int priority, @JsonProperty("default") boolean isDefault, @JsonProperty("permissions") Set<GuildPermission> permissions) {
        this.name = name;
        this.priority = priority;
        this.isDefault = isDefault;
        this.permissions = permissions;
    }

    public boolean hasPermission(GuildPermission permission) {
        return permissions.contains(permission);
    }

    public boolean isHigherThan(GuildRank other) {
        return this.priority < other.priority;
    }

    @Override
    public int compareTo(GuildRank other) {
        return Integer.compare(this.priority, other.priority);
    }

    public static GuildRank guildMaster() {
        return new GuildRank("Guild Master", 0, true,
            EnumSet.allOf(GuildPermission.class));
    }

    public static GuildRank officer() {
        return new GuildRank("Officer", 1, true,
            EnumSet.of(
                GuildPermission.INVITE,
                GuildPermission.KICK,
                GuildPermission.OFFICER_CHAT,
                GuildPermission.MUTE_MEMBERS
            ));
    }

    public static GuildRank member() {
        return new GuildRank("Member", 100, true,
            EnumSet.noneOf(GuildPermission.class));
    }

    public static Serializer<GuildRank> serializer() {
        return new JacksonSerializer<>(GuildRank.class);
    }
}
