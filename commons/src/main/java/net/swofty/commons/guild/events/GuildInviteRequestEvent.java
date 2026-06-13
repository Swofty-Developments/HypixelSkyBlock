package net.swofty.commons.guild.events;

import lombok.Getter;
import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class GuildInviteRequestEvent extends GuildEvent {
    private final UUID inviter;
    private final UUID invitee;

    public GuildInviteRequestEvent(UUID inviter, UUID invitee) {
        super(null);
        this.inviter = inviter;
        this.invitee = invitee;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(inviter, invitee);
    }

    @Override
    public Serializer<GuildInviteRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildInviteRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("inviter", value.inviter.toString());
                json.put("invitee", value.invitee.toString());
                return json.toString();
            }

            @Override
            public GuildInviteRequestEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GuildInviteRequestEvent(
                    UUID.fromString(obj.getString("inviter")),
                    UUID.fromString(obj.getString("invitee"))
                );
            }

            @Override
            public GuildInviteRequestEvent clone(GuildInviteRequestEvent value) {
                return new GuildInviteRequestEvent(value.inviter, value.invitee);
            }
        };
    }
}
