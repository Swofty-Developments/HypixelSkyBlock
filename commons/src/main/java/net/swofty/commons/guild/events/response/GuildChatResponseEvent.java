package net.swofty.commons.guild.events.response;

import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class GuildChatResponseEvent extends GuildResponseEvent {
    private final UUID sender;
    private final String message;
    private final boolean officerChat;

    public GuildChatResponseEvent(GuildData guild, UUID sender, String message, boolean officerChat) {
        super(guild);
        this.sender = sender;
        this.message = message;
        this.officerChat = officerChat;
    }

    public UUID getSender() { return sender; }
    public String getMessage() { return message; }
    public boolean isOfficerChat() { return officerChat; }

    @Override
    public Serializer<GuildChatResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildChatResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("guild", GuildData.getStaticSerializer().serialize(value.getGuild()));
                json.put("sender", value.sender.toString());
                json.put("message", value.message);
                json.put("officerChat", value.officerChat);
                return json.toString();
            }

            @Override
            public GuildChatResponseEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                GuildData guild = GuildData.getStaticSerializer().deserialize(obj.getString("guild"));
                return new GuildChatResponseEvent(guild,
                        UUID.fromString(obj.getString("sender")),
                        obj.getString("message"),
                        obj.getBoolean("officerChat"));
            }

            @Override
            public GuildChatResponseEvent clone(GuildChatResponseEvent value) {
                return new GuildChatResponseEvent(value.getGuild(), value.sender, value.message, value.officerChat);
            }
        };
    }
}
