package net.swofty.types.generic.data.datapoints;

import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.Setter;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.museum.MuseumDisplays;
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
        private Map<SkyBlockItem, Map.Entry<MuseumDisplays, Integer>> currentlyInMuseum = new HashMap<>();
        private List<SkyBlockItem> previouslyInMuseum = new ArrayList<>();
        private Map<UUID, Long> insertionTimes = new HashMap<>();
        private Map<UUID, Double> calculatedPrices = new HashMap<>();

        public Map<SkyBlockItem, Integer> getInDisplay(MuseumDisplays display) {
            // Note that the Map.Entry can be null, so we need to check for that
            return currentlyInMuseum.entrySet().stream().filter(entry -> entry.getValue().getKey().equals(display))
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getValue()));
        }

        public void removeFromDisplay(MuseumDisplays display, Integer slot) {
            // Find the item in currentlyInMusuem, and then set its Map.Entry to null
            currentlyInMuseum.entrySet().stream().filter(entry -> entry.getValue().getKey().equals(display) && entry.getValue().getValue().equals(slot))
                    .findFirst().ifPresent(entry -> currentlyInMuseum.put(entry.getKey(), null));
        }

        public List<SkyBlockItem> getNotInDisplay() {
            return currentlyInMuseum.entrySet().stream().filter(entry -> entry.getValue() == null)
                    .map(Map.Entry::getKey).collect(Collectors.toList());
        }

        public void addToDisplay(SkyBlockItem item, MuseumDisplays display, Integer slot) {
            currentlyInMuseum.put(item, Map.entry(display, slot));
            UUID itemUUID = UUID.fromString(item.getAttributeHandler().getUniqueTrackedID());
            insertionTimes.put(itemUUID, System.currentTimeMillis());
        }

        public JSONObject serialize() {
            JSONObject json = new JSONObject();
            if (currentlyViewing != null)
                json.put("currentlyViewing", currentlyViewing.getKey() + "=" + currentlyViewing.getValue());
            else json.put("currentlyViewing", JSONObject.NULL);
            json.put("hasBoughtAppraisalService", hasBoughtAppraisalService);

            List<String> currentlyInMuseum = getCurrentlyInMuseum().entrySet().stream().map(entry -> {
                JSONObject item = SkyBlockItemSerializer.serializeJSON(entry.getKey());
                item.put("display", entry.getValue().getKey().name());
                item.put("position", entry.getValue().getValue());
                return item.toString();
            }).toList();
            List<String> previouslyInMuseum = getPreviouslyInMuseum().stream().map(item ->
                    SkyBlockItemSerializer.serializeJSON(item).toString()
            ).toList();

            json.put("currentlyInMuseum", currentlyInMuseum);
            json.put("previouslyInMuseum", previouslyInMuseum);
            json.put("insertionTimes", new JSONObject(insertionTimes).toString());
            json.put("calculatedPrices", new JSONObject(calculatedPrices).toString());

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

            List<SkyBlockItem> currentlyInMuseum = json.getJSONArray("currentlyInMuseum").toList().stream().map(item -> {
                JSONObject itemJson = new JSONObject((String) item);
                SkyBlockItem skyBlockItem = SkyBlockItemDeserializer.deserializeJSON(itemJson);
                MuseumDisplays display = MuseumDisplays.valueOf(itemJson.getString("display"));
                int position = itemJson.getInt("position");
                data.currentlyInMuseum.put(skyBlockItem, Map.entry(display, position));
                return skyBlockItem;
            }).toList();
            List<SkyBlockItem> previouslyInMuseum = json.getJSONArray("previouslyInMuseum").toList().stream().map(item ->
                    SkyBlockItemDeserializer.deserializeJSON(new JSONObject((String) item))
            ).toList();

            data.currentlyInMuseum = currentlyInMuseum.stream().collect(Collectors.toMap(item -> item, item -> Map.entry(MuseumDisplays.ATRIUM_SLOTS, 0)));
            data.previouslyInMuseum = previouslyInMuseum;
            data.insertionTimes = new JSONObject(json.getString("insertionTimes"))
                    .toMap().entrySet().stream().map(entry -> Map.entry(UUID.fromString(entry.getKey()), (Long) entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            data.calculatedPrices = new JSONObject(json.getString("calculatedPrices"))
                    .toMap().entrySet().stream().map(entry -> Map.entry(UUID.fromString(entry.getKey()), (Double) entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            return data;
        }
    }
}
