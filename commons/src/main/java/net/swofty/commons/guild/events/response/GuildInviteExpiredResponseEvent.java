package net.swofty.commons.guild.events.response;

import lombok.Getter;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class GuildInviteExpiredResponseEvent extends GuildResponseEvent {
    private final UUID inviter;
    private final UUID invitee;

    public GuildInviteExpiredResponseEvent(GuildData guild, UUID inviter, UUID invitee) {
        super(guild);
        this.inviter = inviter;
        this.invitee = invitee;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(inviter, invitee);
    }

    @Override
    public Serializer<GuildInviteExpiredResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildInviteExpiredResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("guild", GuildData.getStaticSerializer().serialize(value.getGuild()));
                json.put("inviter", value.inviter.toString());
                json.put("invitee", value.invitee.toString());
                return json.toString();
            }

            @Override
            public GuildInviteExpiredResponseEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                GuildData guild = GuildData.getStaticSerializer().deserialize(obj.getString("guild"));
                return new GuildInviteExpiredResponseEvent(guild,
                        UUID.fromString(obj.getString("inviter")),
                        UUID.fromString(obj.getString("invitee")));
            }

            @Override
            public GuildInviteExpiredResponseEvent clone(GuildInviteExpiredResponseEvent value) {
                return new GuildInviteExpiredResponseEvent(value.getGuild(), value.inviter, value.invitee);
            }
        };
    }
}
