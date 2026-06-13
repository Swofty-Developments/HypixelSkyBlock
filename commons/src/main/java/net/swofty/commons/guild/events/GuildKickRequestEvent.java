package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildKickRequestEvent extends GuildEvent {
    @NotNull
    private final UUID kicker;
    @NotNull
    private final UUID target;
    @NotNull
    private final String reason;

    public GuildKickRequestEvent(@NotNull UUID kicker, @NotNull UUID target, @NotNull String reason) {
        super(null);
        this.kicker = kicker;
        this.target = target;
        this.reason = reason;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(kicker, target);
    }

}
