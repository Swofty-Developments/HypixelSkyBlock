package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildCreateRequestEvent extends GuildEvent {
    private final UUID creator;
    private final String guildName;

    public GuildCreateRequestEvent(UUID creator, String guildName) {
        super(null);
        this.creator = creator;
        this.guildName = guildName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(creator);
    }

}
