package net.swofty.commons.guild.events.response;

import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class GuildRankChangedResponseEvent extends GuildResponseEvent {
    private final UUID changer;
    private final UUID target;
    private final String fromRank;
    private final String toRank;

    public GuildRankChangedResponseEvent(GuildData guild, UUID changer, UUID target, String fromRank, String toRank) {
        super(guild);
        this.changer = changer;
        this.target = target;
        this.fromRank = fromRank;
        this.toRank = toRank;
    }

    public UUID getChanger() { return changer; }
    public UUID getTarget() { return target; }
    public String getFromRank() { return fromRank; }
    public String getToRank() { return toRank; }

    @Override
    public Serializer<GuildRankChangedResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildRankChangedResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("guild", GuildData.getStaticSerializer().serialize(value.getGuild()));
                json.put("changer", value.changer.toString());
                json.put("target", value.target.toString());
                json.put("fromRank", value.fromRank);
                json.put("toRank", value.toRank);
                return json.toString();
            }

            @Override
            public GuildRankChangedResponseEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                GuildData guild = GuildData.getStaticSerializer().deserialize(obj.getString("guild"));
                return new GuildRankChangedResponseEvent(guild,
                        UUID.fromString(obj.getString("changer")),
                        UUID.fromString(obj.getString("target")),
                        obj.getString("fromRank"),
                        obj.getString("toRank"));
            }

            @Override
            public GuildRankChangedResponseEvent clone(GuildRankChangedResponseEvent value) {
                return new GuildRankChangedResponseEvent(value.getGuild(), value.changer, value.target, value.fromRank, value.toRank);
            }
        };
    }
}
