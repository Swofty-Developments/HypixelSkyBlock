package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildTransferRequestEvent extends GuildEvent {
    @NotNull
    private final UUID currentOwner;
    @NotNull
    private final UUID newOwner;

    public GuildTransferRequestEvent(@NotNull UUID currentOwner, @NotNull UUID newOwner) {
        super(null);
        this.currentOwner = currentOwner;
        this.newOwner = newOwner;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(currentOwner, newOwner);
    }

}
