package net.swofty.commons.guild.events;

import lombok.Getter;
import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class GuildChatRequestEvent extends GuildEvent {
    private final UUID sender;
    private final String message;
    private final boolean officerChat;

    public GuildChatRequestEvent(UUID sender, String message, boolean officerChat) {
        super(null);
        this.sender = sender;
        this.message = message;
        this.officerChat = officerChat;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(sender);
    }

    @Override
    public Serializer<GuildChatRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildChatRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("sender", value.sender.toString());
                json.put("message", value.message);
                json.put("officerChat", value.officerChat);
                return json.toString();
            }

            @Override
            public GuildChatRequestEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GuildChatRequestEvent(
                        UUID.fromString(obj.getString("sender")),
                        obj.getString("message"),
                        obj.getBoolean("officerChat")
                );
            }

            @Override
            public GuildChatRequestEvent clone(GuildChatRequestEvent value) {
                return new GuildChatRequestEvent(value.sender, value.message, value.officerChat);
            }
        };
    }
}
