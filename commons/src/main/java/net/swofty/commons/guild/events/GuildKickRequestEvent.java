package net.swofty.commons.guild.events;

import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(force = true)
public class GuildKickRequestEvent extends GuildEvent {
    private final UUID kicker;
    private final UUID target;
    private final String reason;

    public GuildKickRequestEvent(UUID kicker, UUID target, String reason) {
        super(null);
        this.kicker = kicker;
        this.target = target;
        this.reason = reason;
    }

    public UUID getKicker() {
        return kicker;
    }

    public UUID getTarget() {
        return target;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(kicker, target);
    }

}
