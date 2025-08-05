package net.swofty.commons.party;

import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;

public interface Party {
    Serializer<? extends Party> getSerializer();

    List<UUID> getParticipants();
}
