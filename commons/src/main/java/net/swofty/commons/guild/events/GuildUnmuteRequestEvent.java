package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildUnmuteRequestEvent extends GuildEvent {
    @NotNull
    private UUID actor;
    @NotNull
    private String target; // TODO: UUID

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
