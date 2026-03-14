package net.swofty.commons.guild.events;

import lombok.Getter;
import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class GuildSettingRequestEvent extends GuildEvent {
    private final UUID changer;
    private final String setting;
    private final String value;

    public GuildSettingRequestEvent(UUID changer, String setting, String value) {
        super(null);
        this.changer = changer;
        this.setting = setting;
        this.value = value;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(changer);
    }

    @Override
    public Serializer<GuildSettingRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GuildSettingRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("changer", value.changer.toString());
                json.put("setting", value.setting);
                json.put("value", value.value);
                return json.toString();
            }

            @Override
            public GuildSettingRequestEvent deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GuildSettingRequestEvent(
                    UUID.fromString(obj.getString("changer")),
                    obj.getString("setting"),
                    obj.getString("value")
                );
            }

            @Override
            public GuildSettingRequestEvent clone(GuildSettingRequestEvent val) {
                return new GuildSettingRequestEvent(val.changer, val.setting, val.value);
            }
        };
    }
}
