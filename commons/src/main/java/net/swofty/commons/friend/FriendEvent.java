package net.swofty.commons.friend;

import lombok.Getter;
import lombok.NonNull;
import net.swofty.commons.protocol.Serializer;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class FriendEvent {
    public FriendEvent() {
    }

    public abstract Serializer getSerializer();

    public abstract List<UUID> getParticipants();

    public static @NonNull FriendEvent findFromType(String className) {
        String[] packageNames = {
                "net.swofty.commons.friend.events",
                "net.swofty.commons.friend.events.response"
        };

        for (String packageName : packageNames) {
            try {
                Class<?> clazz = Class.forName(packageName + "." + className);
                return createDummyInstance(clazz);
            } catch (Exception e) {
                // Try next package
            }
        }

        throw new RuntimeException("Failed to find friend event class: " + className + " in " + Arrays.toString(packageNames));
    }

    @SuppressWarnings("unchecked")
    private static FriendEvent createDummyInstance(Class<?> clazz) throws Exception {
        String className = clazz.getSimpleName();

        switch (className) {
            // Request events
            case "FriendAddRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "FriendAcceptRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "FriendDenyRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "FriendRemoveRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "FriendRemoveAllRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID());
            }
            case "FriendToggleBestRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID());
            }
            case "FriendSetNicknameRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class, String.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID(), "");
            }
            case "FriendToggleSettingRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, FriendSettingType.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), FriendSettingType.ACCEPTING_REQUESTS);
            }
            case "FriendListRequestEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, int.class, boolean.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), 1, false);
            }
            case "FriendRequestsListEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, int.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), 1);
            }
            // Response events
            case "FriendRequestSentResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class, String.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID(), "");
            }
            case "FriendRequestReceivedResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class, String.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID(), "");
            }
            case "FriendAddedResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class, String.class, String.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID(), "", "");
            }
            case "FriendDeniedResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class, String.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID(), "");
            }
            case "FriendRemovedResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class, String.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID(), "");
            }
            case "FriendRemoveAllResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, int.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), 0);
            }
            case "FriendBestToggledResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class, String.class, boolean.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID(), "", false);
            }
            case "FriendNicknameSetResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class, String.class, String.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID(), "", "");
            }
            case "FriendSettingToggledResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, FriendSettingType.class, boolean.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), FriendSettingType.ACCEPTING_REQUESTS, false);
            }
            case "FriendJoinNotificationEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class, String.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID(), "");
            }
            case "FriendLeaveNotificationEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class, String.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID(), "");
            }
            case "FriendRequestExpiredResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, UUID.class, String.class, String.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), UUID.randomUUID(), "", "");
            }
            case "FriendListResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, List.class, int.class, int.class, boolean.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), List.of(), 1, 1, false);
            }
            case "FriendRequestsListResponseEvent" -> {
                var constructor = clazz.getDeclaredConstructor(UUID.class, List.class, int.class, int.class);
                return (FriendEvent) constructor.newInstance(UUID.randomUUID(), List.of(), 1, 1);
            }
            default -> throw new IllegalArgumentException("Unknown friend event class: " + className);
        }
    }
}
