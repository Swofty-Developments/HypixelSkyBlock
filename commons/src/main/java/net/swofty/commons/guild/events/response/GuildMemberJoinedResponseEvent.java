package net.swofty.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildMemberJoinedResponseEvent extends GuildResponseEvent {
    private final UUID joiner;

    public GuildMemberJoinedResponseEvent(GuildData guild, UUID joiner) {
        super(guild);
        this.joiner = joiner;
    }

}
