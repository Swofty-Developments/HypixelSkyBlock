package net.swofty.commons.guild.events.response;

import lombok.Getter;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class GuildMuteChangedResponseEvent extends GuildResponseEvent {
    private final UUID muter;
    private final String target;
    private final long duration;
    private final boolean unmute;

    public GuildMuteChangedResponseEvent(GuildData guild, UUID muter, String target, long duration, boolean unmute) {
        super(guild);
        this.muter = muter;
        this.target = target;
        this.duration = duration;
        this.unmute = unmute;
    }

    @Override
    public Serializer<GuildMuteChangedResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildMuteChangedResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("guild", GuildData.getStaticSerializer().serialize(value.getGuild()));
                json.put("muter", value.muter.toString());
                json.put("target", value.target);
                json.put("duration", value.duration);
                json.put("unmute", value.unmute);
                return json.toString();
            }

            @Override
            public GuildMuteChangedResponseEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                GuildData guild = GuildData.getStaticSerializer().deserialize(obj.getString("guild"));
                return new GuildMuteChangedResponseEvent(guild,
                        UUID.fromString(obj.getString("muter")),
                        obj.getString("target"),
                        obj.getLong("duration"),
                        obj.getBoolean("unmute"));
            }

            @Override
            public GuildMuteChangedResponseEvent clone(GuildMuteChangedResponseEvent value) {
                return new GuildMuteChangedResponseEvent(value.getGuild(), value.muter, value.target, value.duration, value.unmute);
            }
        };
    }
}
