package net.swofty.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildChatResponseEvent extends GuildResponseEvent {
    private final UUID sender;
    private final String message;
    private final boolean officerChat;

    public GuildChatResponseEvent(GuildData guild, UUID sender, String message, boolean officerChat) {
        super(guild);
        this.sender = sender;
        this.message = message;
        this.officerChat = officerChat;
    }

}
