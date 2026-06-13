package net.swofty.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildMuteChangedResponseEvent extends GuildResponseEvent {
    private final UUID muter;
    private final String target;
    private final long duration;
    private final boolean unmute;

    public GuildMuteChangedResponseEvent(GuildData guild, UUID muter, String target, long duration, boolean unmute) {
        super(guild);
        this.muter = muter;
        this.target = target;
        this.duration = duration;
        this.unmute = unmute;
    }

}
