package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildDisbandRequestEvent extends GuildEvent {
    @NotNull
    private final UUID disbander;

    public GuildDisbandRequestEvent(@NotNull UUID disbander) {
        super(null);
        this.disbander = disbander;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(disbander);
    }

}
