package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildChatRequestEvent extends GuildEvent {
    private final UUID sender;
    private final String message;
    private final boolean officerChat;

    public GuildChatRequestEvent(UUID sender, String message, boolean officerChat) {
        super(null);
        this.sender = sender;
        this.message = message;
        this.officerChat = officerChat;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(sender);
    }

}
