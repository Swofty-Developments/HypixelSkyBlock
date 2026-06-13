package net.swofty.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildDisbandedResponseEvent extends GuildResponseEvent {
    private final UUID disbander;

    public GuildDisbandedResponseEvent(GuildData guild, UUID disbander) {
        super(guild);
        this.disbander = disbander;
    }

}
