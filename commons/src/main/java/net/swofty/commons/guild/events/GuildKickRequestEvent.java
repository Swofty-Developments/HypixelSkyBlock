package net.swofty.commons.guild.events;

import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class GuildKickRequestEvent extends GuildEvent {
    private final UUID kicker;
    private final UUID target;
    private final String reason;

    public GuildKickRequestEvent(UUID kicker, UUID target, String reason) {
        super(null);
        this.kicker = kicker;
        this.target = target;
        this.reason = reason;
    }

    public UUID getKicker() { return kicker; }
    public UUID getTarget() { return target; }
    public String getReason() { return reason; }

    @Override
    public List<UUID> getParticipants() {
        return List.of(kicker, target);
    }

    @Override
    public Serializer<GuildKickRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildKickRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("kicker", value.kicker.toString());
                json.put("target", value.target.toString());
                json.put("reason", value.reason);
                return json.toString();
            }

            @Override
            public GuildKickRequestEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GuildKickRequestEvent(
                        UUID.fromString(obj.getString("kicker")),
                        UUID.fromString(obj.getString("target")),
                        obj.getString("reason")
                );
            }

            @Override
            public GuildKickRequestEvent clone(GuildKickRequestEvent value) {
                return new GuildKickRequestEvent(value.kicker, value.target, value.reason);
            }
        };
    }
}
