package net.swofty.commons.guild;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

@Getter
@Setter
public class GuildMember {
    private final UUID uuid;
    private String rankName;
    private long joinedAt;
    private long weeklyGexp;
    private long totalGexp;
    private long lastLogin;
    private boolean notificationsEnabled;
    private long mutedUntil;

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

    public GuildMember(UUID uuid, String rankName, long joinedAt, long weeklyGexp,
                       long totalGexp, long lastLogin, boolean notificationsEnabled, long mutedUntil) {
        this.uuid = uuid;
        this.rankName = rankName;
        this.joinedAt = joinedAt;
        this.weeklyGexp = weeklyGexp;
        this.totalGexp = totalGexp;
        this.lastLogin = lastLogin;
        this.notificationsEnabled = notificationsEnabled;
        this.mutedUntil = mutedUntil;
    }

    public boolean isMuted() {
        return mutedUntil > System.currentTimeMillis();
    }

    public static Serializer<GuildMember> serializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildMember value) {
                JSONObject json = new JSONObject();
                json.put("uuid", value.uuid.toString());
                json.put("rankName", value.rankName);
                json.put("joinedAt", value.joinedAt);
                json.put("weeklyGexp", value.weeklyGexp);
                json.put("totalGexp", value.totalGexp);
                json.put("lastLogin", value.lastLogin);
                json.put("notificationsEnabled", value.notificationsEnabled);
                json.put("mutedUntil", value.mutedUntil);
                return json.toString();
            }

            @Override
            public GuildMember deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GuildMember(
                    UUID.fromString(obj.getString("uuid")),
                    obj.getString("rankName"),
                    obj.getLong("joinedAt"),
                    obj.getLong("weeklyGexp"),
                    obj.getLong("totalGexp"),
                    obj.getLong("lastLogin"),
                    obj.getBoolean("notificationsEnabled"),
                    obj.getLong("mutedUntil")
                );
            }

            @Override
            public GuildMember clone(GuildMember value) {
                return new GuildMember(value.uuid, value.rankName, value.joinedAt,
                    value.weeklyGexp, value.totalGexp, value.lastLogin,
                    value.notificationsEnabled, value.mutedUntil);
            }
        };
    }
}
