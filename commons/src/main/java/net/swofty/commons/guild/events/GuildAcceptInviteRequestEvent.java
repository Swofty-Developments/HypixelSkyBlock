package net.swofty.commons.guild.events;

import lombok.Getter;
import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class GuildAcceptInviteRequestEvent extends GuildEvent {
    private final UUID accepter;
    private final UUID inviter;

    public GuildAcceptInviteRequestEvent(UUID accepter, UUID inviter) {
        super(null);
        this.accepter = accepter;
        this.inviter = inviter;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(accepter, inviter);
    }

    @Override
    public Serializer<GuildAcceptInviteRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildAcceptInviteRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("accepter", value.accepter.toString());
                json.put("inviter", value.inviter.toString());
                return json.toString();
            }

            @Override
            public GuildAcceptInviteRequestEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GuildAcceptInviteRequestEvent(
                        UUID.fromString(obj.getString("accepter")),
                        UUID.fromString(obj.getString("inviter"))
                );
            }

            @Override
            public GuildAcceptInviteRequestEvent clone(GuildAcceptInviteRequestEvent value) {
                return new GuildAcceptInviteRequestEvent(value.accepter, value.inviter);
            }
        };
    }
}
