package net.swofty.commons.guild.events;

import lombok.Getter;
import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class GuildSetRankRequestEvent extends GuildEvent {
    private final UUID setter;
    private final UUID target;
    private final String rankName;

    public GuildSetRankRequestEvent(UUID setter, UUID target, String rankName) {
        super(null);
        this.setter = setter;
        this.target = target;
        this.rankName = rankName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(setter, target);
    }

    @Override
    public Serializer<GuildSetRankRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildSetRankRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("setter", value.setter.toString());
                json.put("target", value.target.toString());
                json.put("rankName", value.rankName);
                return json.toString();
            }

            @Override
            public GuildSetRankRequestEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GuildSetRankRequestEvent(
                    UUID.fromString(obj.getString("setter")),
                    UUID.fromString(obj.getString("target")),
                    obj.getString("rankName")
                );
            }

            @Override
            public GuildSetRankRequestEvent clone(GuildSetRankRequestEvent value) {
                return new GuildSetRankRequestEvent(value.setter, value.target, value.rankName);
            }
        };
    }
}
