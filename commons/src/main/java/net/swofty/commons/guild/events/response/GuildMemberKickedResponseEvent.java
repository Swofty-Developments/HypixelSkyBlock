package net.swofty.commons.guild.events.response;

import lombok.Getter;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class GuildMemberKickedResponseEvent extends GuildResponseEvent {
    private final UUID kicker;
    private final UUID kicked;
    private final String reason;

    public GuildMemberKickedResponseEvent(GuildData guild, UUID kicker, UUID kicked, String reason) {
        super(guild);
        this.kicker = kicker;
        this.kicked = kicked;
        this.reason = reason;
    }

    @Override
    public List<UUID> getParticipants() {
        List<UUID> participants = new java.util.ArrayList<>(getGuild().getAllMemberUuids());
        if (!participants.contains(kicked)) participants.add(kicked);
        return participants;
    }

    @Override
    public Serializer<GuildMemberKickedResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildMemberKickedResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("guild", GuildData.getStaticSerializer().serialize(value.getGuild()));
                json.put("kicker", value.kicker.toString());
                json.put("kicked", value.kicked.toString());
                json.put("reason", value.reason);
                return json.toString();
            }

            @Override
            public GuildMemberKickedResponseEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                GuildData guild = GuildData.getStaticSerializer().deserialize(obj.getString("guild"));
                return new GuildMemberKickedResponseEvent(guild,
                        UUID.fromString(obj.getString("kicker")),
                        UUID.fromString(obj.getString("kicked")),
                        obj.getString("reason"));
            }

            @Override
            public GuildMemberKickedResponseEvent clone(GuildMemberKickedResponseEvent value) {
                return new GuildMemberKickedResponseEvent(value.getGuild(), value.kicker, value.kicked, value.reason);
            }
        };
    }
}
