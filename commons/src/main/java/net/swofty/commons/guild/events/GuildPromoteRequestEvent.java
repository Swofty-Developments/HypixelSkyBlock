package net.swofty.commons.guild.events;

import lombok.Getter;
import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class GuildPromoteRequestEvent extends GuildEvent {
    private final UUID promoter;
    private final UUID target;

    public GuildPromoteRequestEvent(UUID promoter, UUID target) {
        super(null);
        this.promoter = promoter;
        this.target = target;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(promoter, target);
    }

    @Override
    public Serializer<GuildPromoteRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildPromoteRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("promoter", value.promoter.toString());
                json.put("target", value.target.toString());
                return json.toString();
            }

            @Override
            public GuildPromoteRequestEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GuildPromoteRequestEvent(
                    UUID.fromString(obj.getString("promoter")),
                    UUID.fromString(obj.getString("target"))
                );
            }

            @Override
            public GuildPromoteRequestEvent clone(GuildPromoteRequestEvent value) {
                return new GuildPromoteRequestEvent(value.promoter, value.target);
            }
        };
    }
}
