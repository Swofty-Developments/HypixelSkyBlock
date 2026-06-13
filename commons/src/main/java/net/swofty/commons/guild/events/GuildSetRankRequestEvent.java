package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildSetRankRequestEvent extends GuildEvent {
    @NotNull
    private final UUID setter;
    @NotNull
    private final UUID target;
    @NotNull
    private final String rankName;

    public GuildSetRankRequestEvent(@NotNull UUID setter, @NotNull UUID target, @NotNull String rankName) {
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
