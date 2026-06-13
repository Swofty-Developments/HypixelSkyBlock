package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildPromoteRequestEvent extends GuildEvent {
    @NotNull
    private UUID promoter;
    @NotNull
    private UUID target;

    public GuildPromoteRequestEvent(@NotNull UUID promoter, @NotNull UUID target) {
        super(null);
        this.promoter = promoter;
        this.target = target;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(promoter, target);
    }

}
