package net.swofty.commons.guild.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import net.swofty.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class GuildCreateRequestEvent extends GuildEvent {
    @NotNull
    private UUID creator;
    @NotNull
    private String guildName;

    @JsonCreator
    public GuildCreateRequestEvent(
        @JsonProperty("creator") final @NotNull UUID creator,
        @JsonProperty("guildName") final @NotNull String guildName
    ) {
        super(null);
        this.creator = Objects.requireNonNull(creator, "creator");
        this.guildName = Objects.requireNonNull(guildName, "guildName");
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(creator);
    }

}
