package net.swofty.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildSetRankRequestEvent extends GuildEvent {
    @NotNull
    private UUID setter;
    @NotNull
    private UUID target;
    @NotNull
    private String rankName;

    public GuildSetRankRequestEvent(@NotNull UUID setter, @NotNull UUID target, @NotNull String rankName) {
        super(null);
        this.setter = setter;
        this.target = target;
        this.rankName = rankName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(setter, target);
    }

}
