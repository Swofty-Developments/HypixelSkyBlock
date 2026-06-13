package net.swofty.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildTransferredResponseEvent extends GuildResponseEvent {
    private final UUID oldOwner;
    private final UUID newOwner;

    public GuildTransferredResponseEvent(GuildData guild, UUID oldOwner, UUID newOwner) {
        super(guild);
        this.oldOwner = oldOwner;
        this.newOwner = newOwner;
    }

}
