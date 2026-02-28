package net.swofty.commons.guild.events.response;

import lombok.Getter;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class GuildSettingChangedResponseEvent extends GuildResponseEvent {
    private final UUID changer;
    private final String setting;
    private final String value;

    public GuildSettingChangedResponseEvent(GuildData guild, UUID changer, String setting, String value) {
        super(guild);
        this.changer = changer;
        this.setting = setting;
        this.value = value;
    }

    @Override
    public Serializer<GuildSettingChangedResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildSettingChangedResponseEvent val) {
                JSONObject json = new JSONObject();
                json.put("guild", GuildData.getStaticSerializer().serialize(val.getGuild()));
                json.put("changer", val.changer.toString());
                json.put("setting", val.setting);
                json.put("value", val.value);
                return json.toString();
            }

            @Override
            public GuildSettingChangedResponseEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                GuildData guild = GuildData.getStaticSerializer().deserialize(obj.getString("guild"));
                return new GuildSettingChangedResponseEvent(guild,
                        UUID.fromString(obj.getString("changer")),
                        obj.getString("setting"),
                        obj.getString("value"));
            }

            @Override
            public GuildSettingChangedResponseEvent clone(GuildSettingChangedResponseEvent val) {
                return new GuildSettingChangedResponseEvent(val.getGuild(), val.changer, val.setting, val.value);
            }
        };
    }
}
