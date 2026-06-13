package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildSettingRequestEvent extends GuildEvent {
    @NotNull
    private UUID changer;
    private String setting;
    private String value;

    public GuildSettingRequestEvent(@NotNull UUID changer, String setting, String value) {
        super(null);
        this.changer = changer;
        this.setting = setting;
        this.value = value;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(changer);
    }

}
