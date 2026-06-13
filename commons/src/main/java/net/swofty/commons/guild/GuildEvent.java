package net.swofty.commons.guild;

import lombok.Getter;
import lombok.NonNull;
import net.swofty.commons.protocol.Serializer;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class GuildEvent {
    private final GuildData guild;

    public GuildEvent(GuildData guild) {
        this.guild = guild;
    }

    public abstract Serializer getSerializer();

    public List<UUID> getParticipants() {
        return guild != null ? guild.getAllMemberUuids() : List.of();
    }

    public static @NonNull GuildEvent findFromType(String className) {
        String[] packageNames = {
            "net.swofty.commons.guild.events",
            "net.swofty.commons.guild.events.response"
        };

        for (String packageName : packageNames) {
            try {
                Class<?> clazz = Class.forName(packageName + "." + className);
                return createDummyInstance(clazz);
            } catch (Exception e) {
                // Try next package
            }
        }

        throw new RuntimeException("Failed to find guild event class: " + className + " in " + Arrays.toString(packageNames));
    }

    @SuppressWarnings("unchecked")
    private static GuildEvent createDummyInstance(Class<?> clazz) throws Exception {
        String className = clazz.getSimpleName();

        switch (className) {
            case "GuildCreateRequestEvent" -> {
                var c = clazz.getDeclaredConstructor(UUID.class, String.class);
                return (GuildEvent) c.newInstance(UUID.randomUUID(), "");
            }
            case "GuildInviteRequestEvent" -> {
                var c = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (GuildEvent) c.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "GuildAcceptInviteRequestEvent" -> {
                var c = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (GuildEvent) c.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "GuildLeaveRequestEvent" -> {
                var c = clazz.getDeclaredConstructor(UUID.class);
                return (GuildEvent) c.newInstance(UUID.randomUUID());
            }
            case "GuildKickRequestEvent" -> {
                var c = clazz.getDeclaredConstructor(UUID.class, UUID.class, String.class);
                return (GuildEvent) c.newInstance(UUID.randomUUID(), UUID.randomUUID(), "");
            }
            case "GuildDisbandRequestEvent" -> {
                var c = clazz.getDeclaredConstructor(UUID.class);
                return (GuildEvent) c.newInstance(UUID.randomUUID());
            }
            case "GuildPromoteRequestEvent" -> {
                var c = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (GuildEvent) c.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "GuildDemoteRequestEvent" -> {
                var c = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (GuildEvent) c.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "GuildTransferRequestEvent" -> {
                var c = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (GuildEvent) c.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "GuildChatRequestEvent" -> {
                var c = clazz.getDeclaredConstructor(UUID.class, String.class, boolean.class);
                return (GuildEvent) c.newInstance(UUID.randomUUID(), "", false);
            }
            case "GuildSettingRequestEvent" -> {
                var c = clazz.getDeclaredConstructor(UUID.class, String.class, String.class);
                return (GuildEvent) c.newInstance(UUID.randomUUID(), "", "");
            }
            case "GuildMuteRequestEvent" -> {
                var c = clazz.getDeclaredConstructor(UUID.class, String.class, long.class);
                return (GuildEvent) c.newInstance(UUID.randomUUID(), "", 0L);
            }
            case "GuildUnmuteRequestEvent" -> {
                var c = clazz.getDeclaredConstructor(UUID.class, String.class);
                return (GuildEvent) c.newInstance(UUID.randomUUID(), "");
            }
            case "GuildSetRankRequestEvent" -> {
                var c = clazz.getDeclaredConstructor(UUID.class, UUID.class, String.class);
                return (GuildEvent) c.newInstance(UUID.randomUUID(), UUID.randomUUID(), "");
            }
            case "GuildCreatedResponseEvent" -> {
                var dummy = new GuildData(UUID.randomUUID(), "dummy", UUID.randomUUID());
                var c = clazz.getDeclaredConstructor(GuildData.class, UUID.class);
                return (GuildEvent) c.newInstance(dummy, UUID.randomUUID());
            }
            case "GuildInviteSentResponseEvent" -> {
                var dummy = new GuildData(UUID.randomUUID(), "dummy", UUID.randomUUID());
                var c = clazz.getDeclaredConstructor(GuildData.class, UUID.class, UUID.class);
                return (GuildEvent) c.newInstance(dummy, UUID.randomUUID(), UUID.randomUUID());
            }
            case "GuildMemberJoinedResponseEvent" -> {
                var dummy = new GuildData(UUID.randomUUID(), "dummy", UUID.randomUUID());
                var c = clazz.getDeclaredConstructor(GuildData.class, UUID.class);
                return (GuildEvent) c.newInstance(dummy, UUID.randomUUID());
            }
            case "GuildMemberLeftResponseEvent" -> {
                var dummy = new GuildData(UUID.randomUUID(), "dummy", UUID.randomUUID());
                var c = clazz.getDeclaredConstructor(GuildData.class, UUID.class);
                return (GuildEvent) c.newInstance(dummy, UUID.randomUUID());
            }
            case "GuildMemberKickedResponseEvent" -> {
                var dummy = new GuildData(UUID.randomUUID(), "dummy", UUID.randomUUID());
                var c = clazz.getDeclaredConstructor(GuildData.class, UUID.class, UUID.class, String.class);
                return (GuildEvent) c.newInstance(dummy, UUID.randomUUID(), UUID.randomUUID(), "");
            }
            case "GuildDisbandedResponseEvent" -> {
                var dummy = new GuildData(UUID.randomUUID(), "dummy", UUID.randomUUID());
                var c = clazz.getDeclaredConstructor(GuildData.class, UUID.class);
                return (GuildEvent) c.newInstance(dummy, UUID.randomUUID());
            }
            case "GuildRankChangedResponseEvent" -> {
                var dummy = new GuildData(UUID.randomUUID(), "dummy", UUID.randomUUID());
                var c = clazz.getDeclaredConstructor(GuildData.class, UUID.class, UUID.class, String.class, String.class);
                return (GuildEvent) c.newInstance(dummy, UUID.randomUUID(), UUID.randomUUID(), "", "");
            }
            case "GuildTransferredResponseEvent" -> {
                var dummy = new GuildData(UUID.randomUUID(), "dummy", UUID.randomUUID());
                var c = clazz.getDeclaredConstructor(GuildData.class, UUID.class, UUID.class);
                return (GuildEvent) c.newInstance(dummy, UUID.randomUUID(), UUID.randomUUID());
            }
            case "GuildChatResponseEvent" -> {
                var dummy = new GuildData(UUID.randomUUID(), "dummy", UUID.randomUUID());
                var c = clazz.getDeclaredConstructor(GuildData.class, UUID.class, String.class, boolean.class);
                return (GuildEvent) c.newInstance(dummy, UUID.randomUUID(), "", false);
            }
            case "GuildSettingChangedResponseEvent" -> {
                var dummy = new GuildData(UUID.randomUUID(), "dummy", UUID.randomUUID());
                var c = clazz.getDeclaredConstructor(GuildData.class, UUID.class, String.class, String.class);
                return (GuildEvent) c.newInstance(dummy, UUID.randomUUID(), "", "");
            }
            case "GuildMuteChangedResponseEvent" -> {
                var dummy = new GuildData(UUID.randomUUID(), "dummy", UUID.randomUUID());
                var c = clazz.getDeclaredConstructor(GuildData.class, UUID.class, String.class, long.class, boolean.class);
                return (GuildEvent) c.newInstance(dummy, UUID.randomUUID(), "", 0L, false);
            }
            case "GuildInviteExpiredResponseEvent" -> {
                var dummy = new GuildData(UUID.randomUUID(), "dummy", UUID.randomUUID());
                var c = clazz.getDeclaredConstructor(GuildData.class, UUID.class, UUID.class);
                return (GuildEvent) c.newInstance(dummy, UUID.randomUUID(), UUID.randomUUID());
            }
            default -> throw new IllegalArgumentException("Unknown guild event class: " + className);
        }
    }
}
