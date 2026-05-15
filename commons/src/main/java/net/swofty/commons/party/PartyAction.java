package net.swofty.commons.party;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PartyAction.Invite.class, name = "Invite"),
        @JsonSubTypes.Type(value = PartyAction.AcceptInvite.class, name = "AcceptInvite"),
        @JsonSubTypes.Type(value = PartyAction.Leave.class, name = "Leave"),
        @JsonSubTypes.Type(value = PartyAction.Disband.class, name = "Disband"),
        @JsonSubTypes.Type(value = PartyAction.Transfer.class, name = "Transfer"),
        @JsonSubTypes.Type(value = PartyAction.Kick.class, name = "Kick"),
        @JsonSubTypes.Type(value = PartyAction.Promote.class, name = "Promote"),
        @JsonSubTypes.Type(value = PartyAction.Demote.class, name = "Demote"),
        @JsonSubTypes.Type(value = PartyAction.Warp.class, name = "Warp"),
        @JsonSubTypes.Type(value = PartyAction.Hijack.class, name = "Hijack"),
        @JsonSubTypes.Type(value = PartyAction.Chat.class, name = "Chat"),
        @JsonSubTypes.Type(value = PartyAction.SwitchedServer.class, name = "SwitchedServer"),
        @JsonSubTypes.Type(value = PartyAction.PlayerDisconnect.class, name = "PlayerDisconnect"),
        @JsonSubTypes.Type(value = PartyAction.PlayerRejoin.class, name = "PlayerRejoin")
})
public sealed interface PartyAction {

    record Invite(PendingParty party) implements PartyAction {}

    record AcceptInvite(UUID accepter, UUID inviter) implements PartyAction {}

    record Leave(UUID leaver) implements PartyAction {}

    record Disband(UUID disbander) implements PartyAction {}

    record Transfer(UUID currentLeader, UUID newLeader) implements PartyAction {}

    record Kick(UUID kicker, UUID target) implements PartyAction {}

    record Promote(UUID promoter, UUID target) implements PartyAction {}

    record Demote(UUID demoter, UUID target) implements PartyAction {}

    record Warp(UUID warper) implements PartyAction {}

    record Hijack(UUID hijacker, UUID target) implements PartyAction {}

    record Chat(UUID player, String message) implements PartyAction {}

    record SwitchedServer(UUID mover) implements PartyAction {}

    record PlayerDisconnect(UUID disconnectedPlayer) implements PartyAction {}

    record PlayerRejoin(UUID rejoinedPlayer) implements PartyAction {}
}
