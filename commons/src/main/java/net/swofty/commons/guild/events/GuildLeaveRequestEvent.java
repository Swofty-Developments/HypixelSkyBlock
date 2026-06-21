package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildLeaveRequestEvent extends GuildEvent {
    @NotNull
    private UUID leaver;

    public GuildLeaveRequestEvent(@NotNull UUID leaver) {
        super(null);
        this.leaver = leaver;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(leaver);
    }

}
