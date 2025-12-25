package net.swofty.type.skyblockgeneric.data.datapoints;

import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.museum.MuseumDisplays;
import net.swofty.type.skyblockgeneric.museum.MuseumableItemCategory;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class DatapointMuseum extends SkyBlockDatapoint<DatapointMuseum.MuseumData> {
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

    // Records for better encapsulation
    public record ViewingInfo(UUID playerUuid, UUID profileUuid) {}
    public record DisplayPlacement(MuseumDisplays display, Integer slot) {}
    public record ItemMetadata(UUID itemUuid, Long insertionTime, Double calculatedPrice) {}

    @Getter
    @Setter
    public static class MuseumData {
        private ViewingInfo currentlyViewing = new ViewingInfo(UUID.randomUUID(), UUID.randomUUID());
        private boolean hasBoughtAppraisalService = false;
        private ArrayList<SkyBlockItem> currentlyInMuseum = new ArrayList<>();
        private ArrayList<SkyBlockItem> previouslyInMuseum = new ArrayList<>();
        private Map<UUID, DisplayPlacement> museumDisplay = new HashMap<>();
        private Map<UUID, Long> insertionTimes = new HashMap<>();
        private Map<UUID, Double> calculatedPrices = new HashMap<>();

        @Getter
        private final DisplayHandler displayHandler = new DisplayHandler();

        public List<SkyBlockItem> getItemsByCategory(MuseumableItemCategory category) {
            return getAllItems().stream()
                    .filter(item -> category.contains(item.getAttributeHandler().getPotentialType()))
                    .collect(Collectors.toList());
        }

        public List<SkyBlockItem> getCurrentItemsByCategory(MuseumableItemCategory category) {
            return currentlyInMuseum.stream()
                    .filter(item -> category.contains(item.getAttributeHandler().getPotentialType()))
                    .collect(Collectors.toList());
        }

        public List<SkyBlockItem> getNotInDisplayByCategory(MuseumableItemCategory category) {
            return currentlyInMuseum.stream()
                    .filter(item -> category.contains(item.getAttributeHandler().getPotentialType()))
                    .filter(item -> !museumDisplay.containsKey(UUID.fromString(item.getAttributeHandler().getUniqueTrackedID())))
                    .collect(Collectors.toList());
        }

        // Item retrieval methods
        public @Nullable SkyBlockItem getItemInMuseum(ItemType type) {
            return currentlyInMuseum.stream()
                    .filter(item -> item.getAttributeHandler().getPotentialType().equals(type))
                    .findFirst().orElse(null);
        }

        public @Nullable SkyBlockItem getItemPreviouslyInMuseum(ItemType type) {
            return previouslyInMuseum.stream()
                    .filter(item -> item.getAttributeHandler().getPotentialType().equals(type))
                    .findFirst().orElse(null);
        }

        public @Nullable SkyBlockItem getItem(MuseumableItemCategory category, ItemType type) {
            return getItemsByCategory(category).stream()
                    .filter(item -> item.getAttributeHandler().getPotentialType().equals(type))
                    .findFirst()
                    .orElse(null);
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

        public List<SkyBlockItem> getAllItems() {
            List<SkyBlockItem> items = new ArrayList<>(currentlyInMuseum);
            items.addAll(previouslyInMuseum);
            return items;
        }

        public Map<MuseumableItemCategory, List<SkyBlockItem>> getAllItemsByCategory() {
            return MuseumableItemCategory.sortAsMuseumItems(getAllItems());
        }

        public void add(SkyBlockItem item) {
            ItemType itemType = item.getAttributeHandler().getPotentialType();

            // Remove any existing item of the same type
            currentlyInMuseum.removeIf(i -> i.getAttributeHandler().getPotentialType().equals(itemType));
            previouslyInMuseum.removeIf(i -> i.getAttributeHandler().getPotentialType().equals(itemType));

            currentlyInMuseum.add(item);
            UUID itemUuid = UUID.fromString(item.getAttributeHandler().getUniqueTrackedID());
            insertionTimes.put(itemUuid, System.currentTimeMillis());
        }

        public void moveToRetrieved(SkyBlockItem item) {
            UUID itemUuid = UUID.fromString(item.getAttributeHandler().getUniqueTrackedID());

            if (currentlyInMuseum.remove(item)) {
                previouslyInMuseum.add(item);
                museumDisplay.remove(itemUuid);
            }
        }

        public ItemMetadata getItemMetadata(UUID itemUuid) {
            return new ItemMetadata(
                    itemUuid,
                    insertionTimes.get(itemUuid),
                    calculatedPrices.get(itemUuid)
            );
        }

        public class DisplayHandler {
            public List<SkyBlockItem> getAllItemsInDisplay(MuseumDisplays display) {
                return museumDisplay.entrySet().stream()
                        .filter(entry -> entry.getValue().display().equals(display))
                        .sorted(Map.Entry.comparingByValue(Comparator.comparing(DisplayPlacement::slot)))
                        .map(entry -> getFromUUID(entry.getKey()))
                        .collect(Collectors.toList());
            }

            public List<SkyBlockItem> getItemsAtSlot(MuseumDisplays display, Integer slot) {
                return museumDisplay.entrySet().stream()
                        .filter(entry -> entry.getValue().display().equals(display) && entry.getValue().slot().equals(slot))
                        .map(entry -> getFromUUID(entry.getKey()))
                        .collect(Collectors.toList());
            }

            public Map<Integer, List<SkyBlockItem>> getItemsBySlot(MuseumDisplays display) {
                return museumDisplay.entrySet().stream()
                        .filter(entry -> entry.getValue().display().equals(display))
                        .collect(Collectors.groupingBy(
                                entry -> entry.getValue().slot(),
                                Collectors.mapping(entry -> getFromUUID(entry.getKey()), Collectors.toList())
                        ));
            }

            public List<SkyBlockItem> getItemsInDisplayByCategory(MuseumDisplays display, MuseumableItemCategory category) {
                return getAllItemsInDisplay(display).stream()
                        .filter(item -> category.contains(item.getAttributeHandler().getPotentialType()))
                        .collect(Collectors.toList());
            }

            public List<SkyBlockItem> getItemsAtSlotByCategory(MuseumDisplays display, Integer slot, MuseumableItemCategory category) {
                return getItemsAtSlot(display, slot).stream()
                        .filter(item -> category.contains(item.getAttributeHandler().getPotentialType()))
                        .collect(Collectors.toList());
            }

            public boolean isItemInDisplay(SkyBlockItem item) {
                UUID itemUuid = UUID.fromString(item.getAttributeHandler().getUniqueTrackedID());
                return museumDisplay.containsKey(itemUuid);
            }

            public @Nullable DisplayPlacement getItemDisplayPlacement(SkyBlockItem item) {
                UUID itemUuid = UUID.fromString(item.getAttributeHandler().getUniqueTrackedID());
                return museumDisplay.get(itemUuid);
            }

            public void addToDisplay(SkyBlockItem item, MuseumDisplays display, Integer slot) {
                UUID itemUuid = UUID.fromString(item.getAttributeHandler().getUniqueTrackedID());
                museumDisplay.put(itemUuid, new DisplayPlacement(display, slot));
            }

            public void addToDisplay(List<SkyBlockItem> items, MuseumDisplays display, Integer slot) {
                for (SkyBlockItem item : items) {
                    addToDisplay(item, display, slot);
                }
            }

            public void removeFromDisplay(SkyBlockItem item) {
                UUID itemUuid = UUID.fromString(item.getAttributeHandler().getUniqueTrackedID());
                museumDisplay.remove(itemUuid);
            }

            public void removeFromDisplay(List<SkyBlockItem> items) {
                for (SkyBlockItem item : items) {
                    removeFromDisplay(item);
                }
            }

            public void removeAllFromSlot(MuseumDisplays display, Integer slot) {
                museumDisplay.entrySet().removeIf(entry ->
                        entry.getValue().display().equals(display) &&
                                entry.getValue().slot().equals(slot)
                );
            }

            public void removeAllFromDisplay(MuseumDisplays display) {
                museumDisplay.entrySet().removeIf(entry ->
                        entry.getValue().display().equals(display)
                );
            }

            public boolean hasItemsAtSlot(MuseumDisplays display, Integer slot) {
                return museumDisplay.values().stream()
                        .anyMatch(placement -> placement.display().equals(display) && placement.slot().equals(slot));
            }

            public int getItemCountAtSlot(MuseumDisplays display, Integer slot) {
                return (int) museumDisplay.values().stream()
                        .filter(placement -> placement.display().equals(display) && placement.slot().equals(slot))
                        .count();
            }

            public List<Integer> getOccupiedSlots(MuseumDisplays display) {
                return museumDisplay.values().stream()
                        .filter(placement -> placement.display().equals(display))
                        .map(DisplayPlacement::slot)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());
            }

            public List<Integer> getAvailableSlots(MuseumDisplays display) {
                Set<Integer> occupiedSlots = museumDisplay.values().stream()
                        .filter(placement -> placement.display().equals(display))
                        .map(DisplayPlacement::slot)
                        .collect(Collectors.toSet());

                List<Integer> availableSlots = new ArrayList<>();
                for (int i = 0; i < display.getPositions().size(); i++) {
                    if (!occupiedSlots.contains(i)) {
                        availableSlots.add(i);
                    }
                }
                return availableSlots;
            }

            public Map<MuseumDisplays, List<SkyBlockItem>> fetchAllDisplayedItems() {
                Map<MuseumDisplays, List<SkyBlockItem>> displayedItems = new HashMap<>();

                for (MuseumDisplays display : MuseumDisplays.values()) {
                    displayedItems.put(display, getAllItemsInDisplay(display));
                }

                return displayedItems;
            }

            public Map<MuseumDisplays, Map<Integer, List<SkyBlockItem>>> fetchAllDisplayedItemsBySlot() {
                Map<MuseumDisplays, Map<Integer, List<SkyBlockItem>>> displayedItems = new HashMap<>();

                for (MuseumDisplays display : MuseumDisplays.values()) {
                    displayedItems.put(display, getItemsBySlot(display));
                }

                return displayedItems;
            }

            public Map<MuseumDisplays, Integer> getDisplayItemCounts() {
                return Arrays.stream(MuseumDisplays.values())
                        .collect(Collectors.toMap(
                                display -> display,
                                display -> getAllItemsInDisplay(display).size()
                        ));
            }
        }

        public JSONObject serialize() {
            JSONObject json = new JSONObject();

            if (currentlyViewing != null) {
                json.put("currentlyViewing", currentlyViewing.playerUuid() + "=" + currentlyViewing.profileUuid());
            } else {
                json.put("currentlyViewing", JSONObject.NULL);
            }

            json.put("hasBoughtAppraisalService", hasBoughtAppraisalService);

            List<String> currentItems = currentlyInMuseum.stream()
                    .map(item -> item.toUnderstandable().serialize())
                    .toList();
            List<String> previousItems = previouslyInMuseum.stream()
                    .map(item -> item.toUnderstandable().serialize())
                    .toList();

            json.put("currentlyInMuseum", currentItems);
            json.put("previouslyInMuseum", previousItems);

            JSONObject displayJson = new JSONObject();
            for (Map.Entry<UUID, DisplayPlacement> entry : museumDisplay.entrySet()) {
                UUID itemUuid = entry.getKey();
                DisplayPlacement placement = entry.getValue();
                displayJson.put(itemUuid.toString(), placement.display().name() + "=" + placement.slot());
            }
            json.put("museumDisplay", displayJson.toString());

            json.put("insertionTimes", new JSONObject(insertionTimes).toString());
            json.put("calculatedPrices", new JSONObject(calculatedPrices).toString());

            return json;
        }

        public static MuseumData deserialize(JSONObject json) {
            MuseumData data = new MuseumData();

            if (json.isNull("currentlyViewing")) {
                data.currentlyViewing = null;
            } else {
                String[] viewingParts = json.getString("currentlyViewing").split("=");
                data.currentlyViewing = new ViewingInfo(
                        UUID.fromString(viewingParts[0]),
                        UUID.fromString(viewingParts[1])
                );
            }

            data.hasBoughtAppraisalService = json.getBoolean("hasBoughtAppraisalService");

            data.previouslyInMuseum = json.getJSONArray("previouslyInMuseum").toList().stream()
                    .map(item -> new SkyBlockItem(UnderstandableSkyBlockItem.deserialize((String) item)))
                    .collect(Collectors.toCollection(ArrayList::new));

            data.currentlyInMuseum = json.getJSONArray("currentlyInMuseum").toList().stream()
                    .map(item -> new SkyBlockItem(UnderstandableSkyBlockItem.deserialize((String) item)))
                    .collect(Collectors.toCollection(ArrayList::new));

            JSONObject displayJson = new JSONObject(json.getString("museumDisplay"));
            data.museumDisplay = displayJson.toMap().entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> UUID.fromString(entry.getKey()),
                            entry -> {
                                String[] values = ((String) entry.getValue()).split("=");
                                MuseumDisplays display = MuseumDisplays.valueOf(values[0]);
                                int slot = Integer.parseInt(values[1]);
                                return new DisplayPlacement(display, slot);
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