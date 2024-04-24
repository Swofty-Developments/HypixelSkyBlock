package net.swofty.types.generic.data.datapoints;

import lombok.Getter;
import lombok.Setter;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.serializer.SkyBlockItemDeserializer;
import net.swofty.types.generic.serializer.SkyBlockItemSerializer;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class DatapointMuseum extends Datapoint<DatapointMuseum.MuseumData> {
    private static final Serializer<MuseumData> serializer = new Serializer<>() {
        @Override
        public String serialize(MuseumData value) {
            return value.serialize().toString();
        }

        @Override
        public MuseumData deserialize(String json) {
            return MuseumData.deserialize(new JSONObject(json));
        }

        @Override
        public MuseumData clone(MuseumData value) {
            return MuseumData.deserialize(new JSONObject(value.serialize().toString()));
        }
    };

    public DatapointMuseum(String key, MuseumData value) {
        super(key, value, serializer);
    }

    public DatapointMuseum(String key) {
        super(key, new MuseumData(), serializer);
    }

    @Getter
    @Setter
    public static class MuseumData {
        private Map.Entry<UUID, UUID> currentlyViewing = new AbstractMap.SimpleEntry<>(UUID.randomUUID(), UUID.randomUUID());
        private boolean hasBoughtAppraisalService = false;
        private List<SkyBlockItem> currentlyInMuseum = new ArrayList<>();
        private List<SkyBlockItem> previouslyInMuseum = new ArrayList<>();

        public JSONObject serialize() {
            JSONObject json = new JSONObject();
            if (currentlyViewing != null)
                json.put("currentlyViewing", currentlyViewing.getKey() + "=" + currentlyViewing.getValue());
            else json.put("currentlyViewing", JSONObject.NULL);
            json.put("hasBoughtAppraisalService", hasBoughtAppraisalService);

            List<String> currentlyInMuseum = getCurrentlyInMuseum().stream().map(item ->
                    SkyBlockItemSerializer.serializeJSON(item).toString()
            ).toList();
            List<String> previouslyInMuseum = getPreviouslyInMuseum().stream().map(item ->
                    SkyBlockItemSerializer.serializeJSON(item).toString()
            ).toList();

            json.put("currentlyInMuseum", currentlyInMuseum);
            json.put("previouslyInMuseum", previouslyInMuseum);

            return json;
        }

        public static MuseumData deserialize(JSONObject json) {
            MuseumData data = new MuseumData();
            if (json.isNull("currentlyViewing"))
                data.currentlyViewing = null;
            else
                data.currentlyViewing = Map.entry(UUID.fromString(json.getString("currentlyViewing").split("=")[0]),
                        UUID.fromString(json.getString("currentlyViewing").split("=")[1]));
            data.hasBoughtAppraisalService = json.getBoolean("hasBoughtAppraisalService");

            List<SkyBlockItem> currentlyInMuseum = json.getJSONArray("currentlyInMuseum").toList().stream().map(item ->
                    SkyBlockItemDeserializer.deserializeJSON(new JSONObject((String) item))
            ).toList();
            List<SkyBlockItem> previouslyInMuseum = json.getJSONArray("previouslyInMuseum").toList().stream().map(item ->
                    SkyBlockItemDeserializer.deserializeJSON(new JSONObject((String) item))
            ).toList();

            data.currentlyInMuseum = currentlyInMuseum;
            data.previouslyInMuseum = previouslyInMuseum;

            return data;
        }
    }
}
