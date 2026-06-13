package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildAcceptInviteRequestEvent extends GuildEvent {
    private final UUID accepter;
    private final UUID inviter;

    public GuildAcceptInviteRequestEvent(UUID accepter, UUID inviter) {
        super(null);
        this.accepter = accepter;
        this.inviter = inviter;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(accepter, inviter);
    }

}
