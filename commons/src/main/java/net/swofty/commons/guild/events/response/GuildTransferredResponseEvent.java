package net.swofty.commons.guild.events.response;

import lombok.Getter;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class GuildTransferredResponseEvent extends GuildResponseEvent {
    private final UUID oldOwner;
    private final UUID newOwner;

    public GuildTransferredResponseEvent(GuildData guild, UUID oldOwner, UUID newOwner) {
        super(guild);
        this.oldOwner = oldOwner;
        this.newOwner = newOwner;
    }

    @Override
    public Serializer<GuildTransferredResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildTransferredResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("guild", GuildData.getStaticSerializer().serialize(value.getGuild()));
                json.put("oldOwner", value.oldOwner.toString());
                json.put("newOwner", value.newOwner.toString());
                return json.toString();
            }

            @Override
            public GuildTransferredResponseEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                GuildData guild = GuildData.getStaticSerializer().deserialize(obj.getString("guild"));
                return new GuildTransferredResponseEvent(guild,
                        UUID.fromString(obj.getString("oldOwner")),
                        UUID.fromString(obj.getString("newOwner")));
            }

            @Override
            public GuildTransferredResponseEvent clone(GuildTransferredResponseEvent value) {
                return new GuildTransferredResponseEvent(value.getGuild(), value.oldOwner, value.newOwner);
            }
        };
    }
}
