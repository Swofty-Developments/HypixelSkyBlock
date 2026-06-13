package net.swofty.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
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

}
