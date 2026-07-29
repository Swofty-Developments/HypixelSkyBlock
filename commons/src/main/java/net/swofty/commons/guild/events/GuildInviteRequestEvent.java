package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildInviteRequestEvent extends GuildEvent {
    private @NotNull UUID inviter;
    private @NotNull UUID invitee;

    public GuildInviteRequestEvent(@NotNull UUID inviter, @NotNull UUID invitee) {
        super(null);
        this.inviter = inviter;
        this.invitee = invitee;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(inviter, invitee);
    }

}
