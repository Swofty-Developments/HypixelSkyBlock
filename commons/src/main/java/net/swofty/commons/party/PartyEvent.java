package net.swofty.commons.party;

import lombok.Getter;
import lombok.NonNull;
import net.swofty.commons.protocol.Serializer;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class PartyEvent {
    private final Party party;

    public PartyEvent(Party party) {
        this.party = party;
    }

    public abstract Serializer getSerializer();

    public List<UUID> getParticipants() {
        return party != null ? party.getParticipants() : List.of();
    }

    public static @NonNull PartyEvent findFromType(String className) {
        String[] packageNames = {
                "net.swofty.commons.party.events",
                "net.swofty.commons.party.events.response"
        };

        for (String packageName : packageNames) {
            try {
                Class<?> clazz = Class.forName(packageName + "." + className);
                // Create a dummy instance just to get the serializer for deserialization
                // We'll never actually use this instance - it's just for getting the serializer
                return createDummyInstance(clazz);
            } catch (Exception e) {
                // Try next package
            }
        }

        throw new RuntimeException("Failed to find party event class: " + className + " in " + Arrays.toString(packageNames));
    }

    @SuppressWarnings("unchecked")
    private static PartyEvent createDummyInstance(Class<?> clazz) throws Exception {
        String className = clazz.getSimpleName();

        // Handle request events (these all have null party or specific constructors)
        switch (className) {
            case "PartyAcceptInviteEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyLeaveRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID());
            }
            case "PartyDisbandRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID());
            }
            case "PartyTransferRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyKickRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyPromoteRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyDemoteRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyPlayerSwitchedServerEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID());
            }
            case "PartyWarpRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID());
            }
            case "PartyListRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID());
            }
            case "PartyHijackRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyChatMessageEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, String.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID(), "");
            }
            // Handle response events
            case "PartyPlayerSwitchedServerResponseEvent" -> {
                var fullParty = net.swofty.commons.party.FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(net.swofty.commons.party.FullParty.class, UUID.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID());
            }
            case "PartyChatMessageResponseEvent" -> {
                var fullParty = net.swofty.commons.party.FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(net.swofty.commons.party.FullParty.class, UUID.class, String.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), "");
            }
            case "PartyInviteResponseEvent" -> {
                var pendingParty = net.swofty.commons.party.PendingParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(net.swofty.commons.party.PendingParty.class);
                return (PartyEvent) constructor.newInstance(pendingParty);
            }
            case "PartyWarpOverviewResponseEvent" -> {
                var fullParty = net.swofty.commons.party.FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(net.swofty.commons.party.FullParty.class, UUID.class, List.class, List.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), List.of(), List.of());
            }
            case "PartyStartedResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(FullParty.class);
                return (PartyEvent) constructor.newInstance(FullParty.create(UUID.randomUUID(), UUID.randomUUID()));
            }
            case "PartyDisbandResponseEvent" -> {
                var fullParty = net.swofty.commons.party.FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(net.swofty.commons.party.FullParty.class, UUID.class, String.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), "");
            }
            case "PartyLeaderTransferResponseEvent" -> {
                var fullParty = net.swofty.commons.party.FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(net.swofty.commons.party.FullParty.class, UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyMemberJoinResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(FullParty.create(UUID.randomUUID(), UUID.randomUUID()), UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyMemberKickResponseEvent" -> {
                var fullParty = net.swofty.commons.party.FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(net.swofty.commons.party.FullParty.class, UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyInviteExpiredResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, UUID.class);
                return (PartyEvent) constructor.newInstance(FullParty.create(UUID.randomUUID(), UUID.randomUUID()), UUID.randomUUID(), UUID.randomUUID());
            }
            case "PartyMemberLeaveResponseEvent" -> {
                var fullParty = net.swofty.commons.party.FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(net.swofty.commons.party.FullParty.class, UUID.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID());
            }
            case "PartyPromotionResponseEvent" -> {
                var fullParty = net.swofty.commons.party.FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(net.swofty.commons.party.FullParty.class, UUID.class, UUID.class, net.swofty.commons.party.FullParty.Role.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), UUID.randomUUID(), net.swofty.commons.party.FullParty.Role.MEMBER);
            }
            case "PartyWarpResponseEvent" -> {
                var fullParty = net.swofty.commons.party.FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(net.swofty.commons.party.FullParty.class, UUID.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID());
            }
            case "PartyInviteEvent" -> {
                var pendingParty = net.swofty.commons.party.PendingParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(net.swofty.commons.party.PendingParty.class);
                return (PartyEvent) constructor.newInstance(pendingParty);
            }
            case "PartyPlayerDisconnectEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID());
            }
            case "PartyPlayerRejoinEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class);
                return (PartyEvent) constructor.newInstance(UUID.randomUUID());
            }
            case "PartyMemberDisconnectedResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, long.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), 300L);
            }
            case "PartyMemberRejoinedResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID());
            }
            case "PartyMemberDisconnectTimeoutResponseEvent" -> {
                var fullParty = FullParty.create(UUID.randomUUID(), UUID.randomUUID());
                var constructor = clazz.getDeclaredConstructor(FullParty.class, UUID.class, boolean.class);
                return (PartyEvent) constructor.newInstance(fullParty, UUID.randomUUID(), false);
            }
            default -> throw new IllegalArgumentException("Unknown party event class: " + className);
        }
    }
}