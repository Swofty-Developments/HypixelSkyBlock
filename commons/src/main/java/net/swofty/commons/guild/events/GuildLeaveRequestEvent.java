package net.swofty.commons.guild.events;

import lombok.Getter;
import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class GuildLeaveRequestEvent extends GuildEvent {
    private final UUID leaver;

    public GuildLeaveRequestEvent(UUID leaver) {
        super(null);
        this.leaver = leaver;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(leaver);
    }

    @Override
    public Serializer<GuildLeaveRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildLeaveRequestEvent value) {
                return new JSONObject().put("leaver", value.leaver.toString()).toString();
            }

            @Override
            public GuildLeaveRequestEvent deserialize(String json) {
                return new GuildLeaveRequestEvent(UUID.fromString(new JSONObject(json).getString("leaver")));
            }

            @Override
            public GuildLeaveRequestEvent clone(GuildLeaveRequestEvent value) {
                return new GuildLeaveRequestEvent(value.leaver);
            }
        };
    }
}
