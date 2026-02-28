package net.swofty.commons.guild.events.response;

import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class GuildMemberLeftResponseEvent extends GuildResponseEvent {
    private final UUID leaver;

    public GuildMemberLeftResponseEvent(GuildData guild, UUID leaver) {
        super(guild);
        this.leaver = leaver;
    }

    public UUID getLeaver() { return leaver; }

    @Override
    public List<UUID> getParticipants() {
        List<UUID> participants = new java.util.ArrayList<>(getGuild().getAllMemberUuids());
        if (!participants.contains(leaver)) participants.add(leaver);
        return participants;
    }

    @Override
    public Serializer<GuildMemberLeftResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildMemberLeftResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("guild", GuildData.getStaticSerializer().serialize(value.getGuild()));
                json.put("leaver", value.leaver.toString());
                return json.toString();
            }

            @Override
            public GuildMemberLeftResponseEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                GuildData guild = GuildData.getStaticSerializer().deserialize(obj.getString("guild"));
                return new GuildMemberLeftResponseEvent(guild, UUID.fromString(obj.getString("leaver")));
            }

            @Override
            public GuildMemberLeftResponseEvent clone(GuildMemberLeftResponseEvent value) {
                return new GuildMemberLeftResponseEvent(value.getGuild(), value.leaver);
            }
        };
    }
}
