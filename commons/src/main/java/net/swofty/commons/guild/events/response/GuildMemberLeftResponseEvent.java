package net.swofty.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.guild.GuildData;
import net.swofty.commons.guild.GuildResponseEvent;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildMemberLeftResponseEvent extends GuildResponseEvent {
    private UUID leaver;

    public GuildMemberLeftResponseEvent(GuildData guild, UUID leaver) {
        super(guild);
        this.leaver = leaver;
    }

    @Override
    public List<UUID> getParticipants() {
        List<UUID> participants = new java.util.ArrayList<>(getGuild().getAllMemberUuids());
        if (!participants.contains(leaver)) participants.add(leaver);
        return participants;
    }

}
