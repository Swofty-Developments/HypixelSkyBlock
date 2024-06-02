package net.swofty.service.protocol;

import com.mongodb.util.JSON;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ProtocolSpecification {
    public abstract List<ProtocolEntries<?>> getServiceProtocolEntries();
    public abstract List<ProtocolEntries<?>> getReturnedProtocolEntries();
    public abstract String getEndpoint();

    @SneakyThrows
    public JSONObject toJSON(Map<String, ?> values, boolean serviceProtocol) {
        JSONObject json = new JSONObject();
        for (ProtocolEntries<?> entry : serviceProtocol ? getServiceProtocolEntries() : getReturnedProtocolEntries()) {
            if (values.containsKey(entry.key)) {
                Object value = values.get(entry.key);
                if (entry.serializer != null) {
                    @SuppressWarnings("unchecked") // Suppress unchecked warning
                    Serializer<Object> serializer = (Serializer<Object>) entry.serializer;
                    value = serializer.serialize(value);
                } else {
                    // Serialize the value to a string
                    value = new JacksonSerializer<>(Object.class).serialize(value);
                }
                json.put(entry.key, value);
            } else if (entry.required) {
                System.out.println("Message: " + json.toString());
                throw new IllegalArgumentException("Missing required field: " + entry.key);
            }
        }
        return json;
    }

    @SneakyThrows
    public Map<String, Object> fromJSON(JSONObject json, boolean serviceProtocol) {
        Map<String, Object> values = new HashMap<>();

        for (ProtocolEntries<?> entry : serviceProtocol ? getServiceProtocolEntries() : getReturnedProtocolEntries()) {
            if (!json.has(entry.key)) {
                if (entry.required) {
                    System.out.println("Message: " + json);
                    throw new IllegalArgumentException("Missing required field: " + entry.key);
                }
                // Optional fields are skipped if not present
                continue;
            }

            Object value;
            if (entry.serializer != null) {
                // Deserialize value using the provided serializer
                try {
                    value = entry.serializer.deserialize(json.getString(entry.key));
                } catch (Exception e) {
                    System.out.println("Message: " + json.toString());
                    throw new IllegalArgumentException("Failed to deserialize field: " + entry.key, e);
                }
            } else {
                // Deserialize value using Jackson
                value = new JacksonSerializer<>(Object.class).deserialize(json.getString(entry.key));
            }

            values.put(entry.key, value);
        }

        return values;
    }

    public List<String> getRequiredInboundFields() {
        List<String> requiredFields = new ArrayList<>();
        for (ProtocolEntries<?> entry : getReturnedProtocolEntries()) {
            if (entry.required) {
                requiredFields.add(entry.key);
            }
        }
        return requiredFields;
    }

    public List<String> getRequiredOutboundFields() {
        List<String> requiredFields = new ArrayList<>();
        for (ProtocolEntries<?> entry : getServiceProtocolEntries()) {
            if (entry.required) {
                requiredFields.add(entry.key);
            }
        }
        return requiredFields;
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class ProtocolEntries<T> {
        public final String key;
        public final boolean required;
        public Serializer<T> serializer = null;
    }
}
