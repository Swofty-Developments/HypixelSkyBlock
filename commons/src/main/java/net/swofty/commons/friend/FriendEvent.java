package net.swofty.commons.friend;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NonNull;
import net.swofty.commons.friend.events.*;
import net.swofty.commons.friend.events.response.*;
import net.swofty.commons.protocol.Serializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FriendAcceptRequestEvent.class),
        @JsonSubTypes.Type(value = FriendAddRequestEvent.class),
        @JsonSubTypes.Type(value = FriendDenyRequestEvent.class),
        @JsonSubTypes.Type(value = FriendListRequestEvent.class),
        @JsonSubTypes.Type(value = FriendRemoveAllRequestEvent.class),
        @JsonSubTypes.Type(value = FriendRemoveRequestEvent.class),
        @JsonSubTypes.Type(value = FriendRequestsListEvent.class),
        @JsonSubTypes.Type(value = FriendSetNicknameRequestEvent.class),
        @JsonSubTypes.Type(value = FriendToggleBestRequestEvent.class),
        @JsonSubTypes.Type(value = FriendToggleSettingRequestEvent.class),
        @JsonSubTypes.Type(value = FriendResponseEvent.class)
})
public abstract class FriendEvent {

    private static final String[] EVENT_PACKAGES = {
            "net.swofty.commons.friend.events",
            "net.swofty.commons.friend.events.response"
    };

    public FriendEvent() {
    }

    public abstract Serializer getSerializer();

    public abstract List<UUID> getParticipants();

    /**
     * Returns a placeholder instance of the named event subclass. Used by the
     * Redis dispatch layer to obtain a {@link Serializer} for incoming
     * payloads — the dummy itself is discarded once its serializer is held.
     *
     * <p>The dummy is built by picking the first declared constructor of the
     * subclass and filling every parameter with a default of the appropriate
     * type. This replaces a ~100-line switch over each event class name with
     * one reflection pass; adding a new event subtype no longer requires a
     * matching case here.</p>
     */
    public static @NonNull FriendEvent findFromType(String className) {
        for (String packageName : EVENT_PACKAGES) {
            try {
                Class<?> clazz = Class.forName(packageName + "." + className);
                return createDummyInstance(clazz);
            } catch (ClassNotFoundException ignored) {
                // try next package
            } catch (Exception e) {
                throw new RuntimeException(
                        "Failed to instantiate friend event class " + className, e);
            }
        }
        throw new RuntimeException(
                "Failed to find friend event class: " + className
                        + " in " + Arrays.toString(EVENT_PACKAGES));
    }

    private static FriendEvent createDummyInstance(Class<?> clazz) throws Exception {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length == 0) {
            throw new IllegalStateException("No constructors on " + clazz.getName());
        }

        Constructor<?> constructor = constructors[0];
        Object[] args = Arrays.stream(constructor.getParameters())
                .map(FriendEvent::placeholderFor)
                .toArray();
        constructor.setAccessible(true);
        return (FriendEvent) constructor.newInstance(args);
    }

    private static Object placeholderFor(Parameter parameter) {
        Class<?> type = parameter.getType();
        if (type == UUID.class) return UUID.randomUUID();
        if (type == String.class) return "";
        if (type == int.class || type == Integer.class) return 0;
        if (type == long.class || type == Long.class) return 0L;
        if (type == double.class || type == Double.class) return 0.0D;
        if (type == float.class || type == Float.class) return 0F;
        if (type == boolean.class || type == Boolean.class) return false;
        if (type == byte.class || type == Byte.class) return (byte) 0;
        if (type == short.class || type == Short.class) return (short) 0;
        if (type == char.class || type == Character.class) return '\0';
        if (type == List.class) return Collections.emptyList();
        if (type.isEnum()) {
            Object[] constants = type.getEnumConstants();
            return constants.length == 0 ? null : constants[0];
        }
        return null;
    }
}
