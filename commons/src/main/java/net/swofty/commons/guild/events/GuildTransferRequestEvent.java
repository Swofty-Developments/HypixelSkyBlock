package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildTransferRequestEvent extends GuildEvent {
    private final UUID currentOwner;
    private final UUID newOwner;

    public GuildTransferRequestEvent(UUID currentOwner, UUID newOwner) {
        super(null);
        this.currentOwner = currentOwner;
        this.newOwner = newOwner;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(currentOwner, newOwner);
    }

}
