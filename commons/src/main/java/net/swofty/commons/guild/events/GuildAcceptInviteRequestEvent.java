package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildAcceptInviteRequestEvent extends GuildEvent {
    @NotNull
    private UUID accepter;
    @NotNull
    private UUID inviter;

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
