package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildSetRankRequestEvent extends GuildEvent {
    private final UUID setter;
    private final UUID target;
    private final String rankName;

    public GuildSetRankRequestEvent(UUID setter, UUID target, String rankName) {
        super(null);
        this.setter = setter;
        this.target = target;
        this.rankName = rankName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(setter, target);
    }

}
