package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildMuteRequestEvent extends GuildEvent {
    private final UUID muter;
    private final String target;
    private final long duration;

    public GuildMuteRequestEvent(UUID muter, String target, long duration) {
        super(null);
        this.muter = muter;
        this.target = target;
        this.duration = duration;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(muter);
    }

}
