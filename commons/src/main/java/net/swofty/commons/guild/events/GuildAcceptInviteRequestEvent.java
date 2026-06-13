package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildAcceptInviteRequestEvent extends GuildEvent {
    @NotNull
    private final UUID accepter;
    @NotNull
    private final UUID inviter;

    public GuildAcceptInviteRequestEvent(@NotNull UUID accepter, @NotNull UUID inviter) {
        super(null);
        this.accepter = accepter;
        this.inviter = inviter;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(accepter, inviter);
    }

}
