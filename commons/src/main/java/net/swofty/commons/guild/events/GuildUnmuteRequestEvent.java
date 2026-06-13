package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildUnmuteRequestEvent extends GuildEvent {
    @NotNull
    private final UUID actor;
    @NotNull
    private final String target; // TODO: UUID

    public GuildUnmuteRequestEvent(@NotNull UUID actor, @NotNull String target) {
        super(null);
        this.actor = actor;
        this.target = target;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(actor);
    }

}
