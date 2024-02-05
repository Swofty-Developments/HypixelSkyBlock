package net.swofty.service.generic;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONException;
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
    public JSONObject toJson(Map<String, ?> values) {
        JSONObject json = new JSONObject();
        for (ProtocolEntries<?> entry : getServiceProtocolEntries()) {
            if (values.containsKey(entry.key)) {
                Object value = values.get(entry.key);
                if (entry.serializer != null) {
                    @SuppressWarnings("unchecked") // Suppress unchecked warning
                    Serializer<Object> serializer = (Serializer<Object>) entry.serializer;
                    value = serializer.serialize(value);
                }
                // Put the value directly; JSONObject supports various types
                json.put(entry.key, value);
            } else if (entry.required) {
                throw new IllegalArgumentException("Missing required field: " + entry.key);
            }
        }
        return json;
    }

    public List<String> getRequiredInboundFields() {
        List<String> requiredFields = new ArrayList<>();
        for (ProtocolEntries<?> entry : getServiceProtocolEntries()) {
            if (entry.required) {
                requiredFields.add(entry.key);
            }
        }
        return requiredFields;
    }

    public List<String> getRequiredOutboundFields() {
        List<String> requiredFields = new ArrayList<>();
        for (ProtocolEntries<?> entry : getReturnedProtocolEntries()) {
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

    @SneakyThrows
    public Map<String, Object> fromJson(JSONObject json) {
        Map<String, Object> values = new HashMap<>();

        for (ProtocolEntries<?> entry : getServiceProtocolEntries()) {
            if (!json.has(entry.key)) {
                if (entry.required) {
                    throw new IllegalArgumentException("Missing required field: " + entry.key);
                }
                // Optional fields are skipped if not present
                continue;
            }

            Object value = json.get(entry.key);
            if (entry.serializer != null) {
                try {
                    // Deserialize value using the provided serializer
                    value = entry.serializer.deserialize((String) value);
                } catch (JSONException | JsonProcessingException e) {
                    throw new IllegalArgumentException("Error deserializing field: " + entry.key, e);
                }
            }

            values.put(entry.key, value);
        }

        return values;
    }
}
