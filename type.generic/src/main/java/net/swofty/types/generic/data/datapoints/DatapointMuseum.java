package net.swofty.types.generic.data.datapoints;

import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.commons.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.museum.MuseumDisplays;
import net.swofty.types.generic.museum.MuseumableItemCategory;
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
        private Map.Entry<UUID, UUID> currentlyViewing = Map.entry(UUID.randomUUID(), UUID.randomUUID());
        private boolean hasBoughtAppraisalService = false;
        private ArrayList<SkyBlockItem> currentlyInMuseum = new ArrayList<>();
        private ArrayList<SkyBlockItem> previouslyInMuseum = new ArrayList<>();
        private Map<UUID, Map.Entry<MuseumDisplays, Integer>> museumDisplay = new HashMap<>();
        private Map<UUID, Long> insertionTimes = new HashMap<>();
        private Map<UUID, Double> calculatedPrices = new HashMap<>();

        public Map<SkyBlockItem, Integer> getInDisplay(MuseumDisplays display) {
            return museumDisplay.entrySet().stream()
                    .filter(entry -> entry.getValue() != null && entry.getValue().getKey().equals(display))
                    .collect(Collectors.toMap(
                            entry -> getFromUUID(entry.getKey()),
                            entry -> entry.getValue().getValue()
                    ));
        }

        public @Nullable SkyBlockItem getTypeInMuseum(ItemType type) {
            return currentlyInMuseum.stream()
                    .filter(item -> item.getAttributeHandler().getPotentialType().equals(type))
                    .findFirst().orElse(null);
        }

        public @Nullable SkyBlockItem getTypePreviouslyInMuseum(ItemType type) {
            return previouslyInMuseum.stream()
                    .filter(item -> item.getAttributeHandler().getPotentialType().equals(type))
                    .findFirst().orElse(null);
        }

        public List<SkyBlockItem> getInMuseumThatAreNotInDisplay() {
            return currentlyInMuseum.stream()
                    .filter(item -> !museumDisplay.containsKey(UUID.fromString(item.getAttributeHandler().getUniqueTrackedID())))
                    .collect(Collectors.toList());
        }

        public void removeFromDisplay(MuseumDisplays display, Integer slot) {
            museumDisplay.entrySet().stream()
                    .filter(entry -> entry.getValue().getKey().equals(display) && entry.getValue().getValue().equals(slot))
                    .findFirst()
                    .ifPresent(entry -> museumDisplay.remove(entry.getKey()));
        }

        public List<SkyBlockItem> getAllItems() {
            List<SkyBlockItem> items = new ArrayList<>(currentlyInMuseum);
            items.addAll(previouslyInMuseum);
            return items;
        }

        public List<SkyBlockItem> getAllItems(MuseumableItemCategory category) {
            return getAllItems().stream()
                    .filter(item -> category.getItems().contains(item.getAttributeHandler().getPotentialType()))
                    .collect(Collectors.toList());
        }

        public @Nullable SkyBlockItem getItem(MuseumableItemCategory category, ItemType type) {
            return getAllItems(category).stream()
                    .filter(item -> item.getAttributeHandler().getPotentialType().equals(type))
                    .findFirst()
                    .orElse(null);
        }

        public List<SkyBlockItem> getNotInDisplay() {
            return currentlyInMuseum.stream()
                    .filter(item -> !museumDisplay.containsKey(UUID.fromString(item.getAttributeHandler().getUniqueTrackedID())))
                    .collect(Collectors.toList());
        }

        public @NonNull SkyBlockItem getFromUUID(UUID itemUuid) {
            return currentlyInMuseum.stream()
                    .filter(item -> item.getAttributeHandler().getUniqueTrackedID().equals(itemUuid.toString()))
                    .findFirst()
                    .orElseGet(() -> previouslyInMuseum.stream()
                            .filter(item -> item.getAttributeHandler().getUniqueTrackedID().equals(itemUuid.toString()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("No item with UUID " + itemUuid + " found")));
        }

        public void addToDisplay(SkyBlockItem item, MuseumDisplays display, Integer slot) {
            UUID itemUuid = UUID.fromString(item.getAttributeHandler().getUniqueTrackedID());
            museumDisplay.put(itemUuid, Map.entry(display, slot));
        }

        public void add(SkyBlockItem item) {
            // Remove any other item with the same type
            currentlyInMuseum.removeIf(i -> i.getAttributeHandler().getPotentialType().equals(item.getAttributeHandler().getPotentialType()));
            previouslyInMuseum.removeIf(i -> i.getAttributeHandler().getPotentialType().equals(item.getAttributeHandler().getPotentialType()));

            currentlyInMuseum.add(item);
            UUID itemUuid = UUID.fromString(item.getAttributeHandler().getUniqueTrackedID());
            insertionTimes.put(itemUuid, System.currentTimeMillis());
        }

        public JSONObject serialize() {
            JSONObject json = new JSONObject();
            if (currentlyViewing != null)
                json.put("currentlyViewing", currentlyViewing.getKey() + "=" + currentlyViewing.getValue());
            else json.put("currentlyViewing", JSONObject.NULL);
            json.put("hasBoughtAppraisalService", hasBoughtAppraisalService);

            List<String> currentlyInMuseum = this.currentlyInMuseum.stream()
                    .map(item -> item.toUnderstandable().serialize())
                    .toList();
            List<String> previouslyInMuseum = this.previouslyInMuseum.stream()
                    .map(item -> item.toUnderstandable().serialize())
                    .toList();

            json.put("currentlyInMuseum", currentlyInMuseum);
            json.put("previouslyInMuseum", previouslyInMuseum);

            JSONObject museumDisplayJson = new JSONObject();
            for (Map.Entry<UUID, Map.Entry<MuseumDisplays, Integer>> entry : museumDisplay.entrySet()) {
                UUID itemUuid = entry.getKey();
                MuseumDisplays display = entry.getValue().getKey();
                int position = entry.getValue().getValue();
                museumDisplayJson.put(itemUuid.toString(), display.name() + "=" + position);
            }
            json.put("museumDisplay", museumDisplayJson.toString());

            json.put("insertionTimes", new JSONObject(insertionTimes).toString());
            json.put("calculatedPrices", new JSONObject(calculatedPrices).toString());

            return json;
        }

        public static MuseumData deserialize(JSONObject json) {
            MuseumData data = new MuseumData();
            if (json.isNull("currentlyViewing"))
                data.currentlyViewing = null;
            else
                data.currentlyViewing = Map.entry(
                        UUID.fromString(json.getString("currentlyViewing").split("=")[0]),
                        UUID.fromString(json.getString("currentlyViewing").split("=")[1])
                );
            data.hasBoughtAppraisalService = json.getBoolean("hasBoughtAppraisalService");

            data.previouslyInMuseum = json.getJSONArray("previouslyInMuseum").toList().stream()
                    .map(item -> new SkyBlockItem(UnderstandableSkyBlockItem.deserialize((String) item)))
                    .collect(Collectors.toCollection(ArrayList::new));

            data.currentlyInMuseum = json.getJSONArray("currentlyInMuseum").toList().stream()
                    .map(item -> new SkyBlockItem(UnderstandableSkyBlockItem.deserialize((String) item)))
                    .collect(Collectors.toCollection(ArrayList::new));

            JSONObject museumDisplayJson = new JSONObject(json.getString("museumDisplay"));
            data.museumDisplay = museumDisplayJson.toMap().entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> UUID.fromString(entry.getKey()),
                            entry -> {
                                String[] values = ((String) entry.getValue()).split("=");
                                MuseumDisplays display = MuseumDisplays.valueOf(values[0]);
                                int position = Integer.parseInt(values[1]);
                                return Map.entry(display, position);
                            }
                    ));

            data.insertionTimes = new JSONObject(json.getString("insertionTimes")).toMap().entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> UUID.fromString(entry.getKey()),
                            entry -> (Long) entry.getValue()
                    ));
            data.calculatedPrices = new JSONObject(json.getString("calculatedPrices")).toMap().entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> UUID.fromString(entry.getKey()),
                            entry -> (Double) entry.getValue()
                    ));

            return data;
        }
    }
}