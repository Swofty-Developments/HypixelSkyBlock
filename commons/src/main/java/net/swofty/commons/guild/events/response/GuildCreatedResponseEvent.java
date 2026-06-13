package net.swofty.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildCreatedResponseEvent extends GuildResponseEvent {
    private UUID creator;

    public GuildCreatedResponseEvent(GuildData guild, UUID creator) {
        super(guild);
        this.creator = creator;
    }

}
