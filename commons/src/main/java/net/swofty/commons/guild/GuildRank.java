package net.swofty.commons.guild;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.EnumSet;
import java.util.Set;

@Getter
@Setter
public class GuildRank implements Comparable<GuildRank> {
    private String name;
    private int priority;
    private boolean isDefault;
    private Set<GuildPermission> permissions;

    public GuildRank(String name, int priority, boolean isDefault, Set<GuildPermission> permissions) {
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
        return new Serializer<>() {
            @Override
            public String serialize(GuildRank value) {
                JSONObject json = new JSONObject();
                json.put("name", value.name);
                json.put("priority", value.priority);
                json.put("isDefault", value.isDefault);
                JSONArray perms = new JSONArray();
                for (GuildPermission perm : value.permissions) {
                    perms.put(perm.name());
                }
                json.put("permissions", perms);
                return json.toString();
            }

            @Override
            public GuildRank deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                Set<GuildPermission> perms = EnumSet.noneOf(GuildPermission.class);
                JSONArray permsArray = obj.getJSONArray("permissions");
                for (int i = 0; i < permsArray.length(); i++) {
                    perms.add(GuildPermission.valueOf(permsArray.getString(i)));
                }
                return new GuildRank(
                    obj.getString("name"),
                    obj.getInt("priority"),
                    obj.getBoolean("isDefault"),
                    perms
                );
            }

            @Override
            public GuildRank clone(GuildRank value) {
                return new GuildRank(value.name, value.priority, value.isDefault,
                    EnumSet.copyOf(value.permissions));
            }
        };
    }
}
