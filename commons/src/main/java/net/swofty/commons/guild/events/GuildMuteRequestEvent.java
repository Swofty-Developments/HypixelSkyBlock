package net.swofty.commons.guild.events;

import lombok.Getter;
import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class GuildMuteRequestEvent extends GuildEvent {
    private final UUID muter;
    private final String target;
    private final long duration;

    public GuildMuteRequestEvent(UUID muter, String target, long duration) {
        super(null);
        this.muter = muter;
        this.target = target;
        this.duration = duration;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(muter);
    }

    @Override
    public Serializer<GuildMuteRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildMuteRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("muter", value.muter.toString());
                json.put("target", value.target);
                json.put("duration", value.duration);
                return json.toString();
            }

            @Override
            public GuildMuteRequestEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GuildMuteRequestEvent(
                    UUID.fromString(obj.getString("muter")),
                    obj.getString("target"),
                    obj.getLong("duration")
                );
            }

            @Override
            public GuildMuteRequestEvent clone(GuildMuteRequestEvent value) {
                return new GuildMuteRequestEvent(value.muter, value.target, value.duration);
            }
        };
    }
}
