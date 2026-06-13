package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildDemoteRequestEvent extends GuildEvent {
    @NotNull
    private final UUID demoter;
    @NotNull
    private final UUID target;

    public GuildDemoteRequestEvent(@NotNull UUID demoter, @NotNull UUID target) {
        super(null);
        this.demoter = demoter;
        this.target = target;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(demoter, target);
    }

}
