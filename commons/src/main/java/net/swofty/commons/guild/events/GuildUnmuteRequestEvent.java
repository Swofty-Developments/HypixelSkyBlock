package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildUnmuteRequestEvent extends GuildEvent {
    private final UUID unmuter;
    private final String target;

    public GuildUnmuteRequestEvent(UUID unmuter, String target) {
        super(null);
        this.unmuter = unmuter;
        this.target = target;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(unmuter);
    }

}
