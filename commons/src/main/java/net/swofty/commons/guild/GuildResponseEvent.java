package net.swofty.commons.guild;

import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
public abstract class GuildResponseEvent extends GuildEvent {
    public GuildResponseEvent(GuildData guild) {
        super(guild);
    }
}
