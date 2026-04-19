package net.swofty.commons.party;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FullParty.class, name = "FullParty"),
        @JsonSubTypes.Type(value = PendingParty.class, name = "PendingParty")
})
public interface Party {
    Serializer<? extends Party> getSerializer();

    List<UUID> getParticipants();
}
