package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class GuildCreateRequestEvent extends GuildEvent {
    @NotNull
    private final UUID creator;
    @NotNull
    private final String guildName;

    public GuildCreateRequestEvent(final @NotNull UUID creator, final @NotNull String guildName) {
        super(null);
        this.creator = creator;
        this.guildName = guildName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(creator);
    }

}
