package net.swofty.commons.party;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record PendingParty(UUID resultPartyUUID, UUID invitee, UUID leader) implements Party {

    @JsonCreator
    public PendingParty(
            @JsonProperty("resultPartyUUID") UUID resultPartyUUID,
            @JsonProperty("invitee") UUID invitee,
            @JsonProperty("leader") UUID leader) {
        this.resultPartyUUID = resultPartyUUID;
        this.invitee = invitee;
        this.leader = leader;
    }

    public static PendingParty create(UUID invitee, UUID leader) {
        return new PendingParty(UUID.randomUUID(), invitee, leader);
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(invitee, leader);
    }
}
