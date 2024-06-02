package net.swofty.types.generic.minion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.SharedInstance;
import net.swofty.types.generic.entity.MinionEntityImpl;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeMinionData;
import net.swofty.types.generic.item.impl.MinionFuelItem;
import net.swofty.types.generic.item.impl.recipes.MinionUpgradeSpeedItem;
import net.swofty.types.generic.minion.extension.MinionExtensionData;
import net.swofty.types.generic.minion.extension.extensions.MinionFuelExtension;
import net.swofty.types.generic.user.SkyBlockIsland;

import java.util.*;

@Getter
public class IslandMinionData {
    private final SkyBlockIsland island;
    private final List<IslandMinion> minions = new ArrayList<>();

    public IslandMinionData(SkyBlockIsland island) {
        this.island = island;
    }

    public IslandMinion initializeMinion(
            Pos position, MinionRegistry minion, ItemAttributeMinionData.MinionData previousData, boolean mithrilInfusion) {
        IslandMinion islandMinion = new IslandMinion(
                UUID.randomUUID(), position, minion,
                previousData.tier(), new ArrayList<>(),
                previousData.generatedResources(),
                System.currentTimeMillis(), null,
                new MinionHandler.InternalMinionTags(),
                new MinionExtensionData(),
                mithrilInfusion);
        minions.add(islandMinion);
        return islandMinion;
    }

    public void spawn(IslandMinion minion) {
        minion.spawnMinion(getIsland().getSharedInstance().join());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class IslandMinion {
        private final UUID minionUUID;
        private final Pos position;
        private final MinionRegistry minion;
        private final int tier;
        private List<MaterialQuantifiable> itemsInMinion;
        private int generatedItems;
        private long lastAction;
        private MinionEntityImpl minionEntity;
        private MinionHandler.InternalMinionTags internalMinionTags;
        private final MinionExtensionData extensionData;
        private boolean mithrilInfusion;

        public void spawnMinion(SharedInstance instance) {
            minionEntity = new MinionEntityImpl(this, minion.asSkyBlockMinion());
            minionEntity.setInstance(instance, position.add(0.5, 0, 0.5));
        }

        public void removeMinion() {
            internalMinionTags.onMinionDespawn(this);
            minionEntity.remove();
        }

        public boolean addItem(SkyBlockItem item) {
            int slots = minion.asSkyBlockMinion().getTiers().get(getTier() - 1).getSlots();
            // Calculate the total number of items in minion as if they were in stacks of 64.
            int totalItemsInStacks = itemsInMinion.stream().mapToInt(materialQuantifiable -> materialQuantifiable.getAmount()).sum();

            // Check if adding the new item would exceed the total slot capacity (slots * 64).
            if ((totalItemsInStacks + item.getAmount()) > (slots * 64)) {
                return false; // Do not add the item if it exceeds capacity.
            }

            // Increment the generatedItems counter by the amount of the new item.
            setGeneratedItems(getGeneratedItems() + item.getAmount());

            // Check if the item already exists in the inventory.
            Optional<MaterialQuantifiable> existingItem = itemsInMinion.stream()
                    .filter(materialQuantifiable -> materialQuantifiable.getMaterial() == item.getAttributeHandler().getItemTypeAsType())
                    .findFirst();

            if (existingItem.isPresent()) {
                // If the item exists, increase its amount.
                existingItem.get().setAmount(existingItem.get().getAmount() + item.getAmount());
            } else {
                // If the item does not exist, add it as a new entry.
                itemsInMinion.add(new MaterialQuantifiable(item.getAttributeHandler().getItemTypeAsType(), item.getAmount()));
            }
            return true;
        }

        public SkyBlockItem asSkyBlockItem() {
            SkyBlockItem toReturn = new SkyBlockItem(getMinion().getItemType());
            toReturn.getAttributeHandler().setMinionData(new ItemAttributeMinionData.MinionData(
                    getTier(),
                    getGeneratedItems()
            ));
            toReturn.getAttributeHandler().setMithrilInfused(mithrilInfusion);
            return toReturn;
        }

        public int getSpeedPercentage(){//Handle percentage speed increase from both fuels and minion upgrades
            int percentageSpeedIncrease = 0;

            //Handle Mithril Infusion
            if(isMithrilInfusion())
                percentageSpeedIncrease += 10;

            //Handle Minion Fuel
            ItemType minionFuel = extensionData.getOfType(MinionFuelExtension.class).getItemTypePassedIn();
            if (minionFuel != null) {
                percentageSpeedIncrease += ((MinionFuelItem) new SkyBlockItem(minionFuel).getGenericInstance()).getMinionFuelPercentage();
            }

            //Handle speed increases from minion upgrades
            for(SkyBlockItem item : extensionData.getMinionUpgrades()) {
                if (item != null && item.getGenericInstance() instanceof MinionUpgradeSpeedItem) {
                    percentageSpeedIncrease += (((MinionUpgradeSpeedItem) item.getGenericInstance()).getPercentageSpeedIncrease());
                }
            }

            return percentageSpeedIncrease;
        }

        public int getBonusRange(){
            int range = 0;
            range += extensionData.getMinionUpgradeCount(ItemType.MINION_EXPANDER);
            return range;
        }

        public Map<String, Object> serialize() {
            List<String> itemsInMinionAsString = new ArrayList<>();
            itemsInMinion.forEach(item -> {
                itemsInMinionAsString.add(item.getMaterial().name() + "," + item.getAmount());
            });

            Map<String, Object> data = new HashMap<>();
            data.put("position", position.blockX() + "," + position.blockY() + "," + position.blockZ());
            data.put("itemsInMinion", itemsInMinionAsString);
            data.put("minion", minion.name());
            data.put("lastAction", lastAction);
            data.put("tier", tier);
            data.put("generatedItems", generatedItems);
            data.put("minionUUID", minionUUID.toString());
            data.put("extensionData", extensionData.toString());
            data.put("mithrilInfusion", mithrilInfusion);
            return data;
        }

        public static IslandMinion deserialize(Map<String, Object> data) {
            List<MaterialQuantifiable> itemsInMinion = new ArrayList<>();
            ((List<String>) data.get("itemsInMinion")).forEach(item -> {
                itemsInMinion.add(new MaterialQuantifiable(
                        ItemType.valueOf(item.split(",")[0]),
                        Integer.parseInt(item.split(",")[1])
                ));
            });
            MinionExtensionData extensionData;
            if (data.containsKey("extensionData"))
                extensionData = MinionExtensionData.fromString(data.get("extensionData").toString());
            else
                extensionData = new MinionExtensionData();

            return new IslandMinion(
                    UUID.fromString(data.get("minionUUID").toString()),
                    new Pos(
                            Integer.parseInt(data.get("position").toString().split(",")[0]),
                            Integer.parseInt(data.get("position").toString().split(",")[1]),
                            Integer.parseInt(data.get("position").toString().split(",")[2])
                    ),
                    MinionRegistry.valueOf(data.get("minion").toString()),
                    (int) data.get("tier"),
                    itemsInMinion,
                    (int) data.get("generatedItems"),
                    System.currentTimeMillis(),
                    null,
                    new MinionHandler.InternalMinionTags(),
                    extensionData,
                    data.containsKey("mithrilInfusion") && (boolean) data.get("mithrilInfusion")
            );
        }
    }

    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();

        minions.forEach(minion -> {
            data.put(minion.minionUUID.toString(), minion.serialize());
        });

        return data;
    }

    public static IslandMinionData deserialize(Map<String, Object> data, SkyBlockIsland island) {
        IslandMinionData islandMinionData = new IslandMinionData(island);
        data.forEach((uuid, minionData) -> {
            islandMinionData.minions.add(IslandMinion.deserialize((Map<String, Object>) minionData));
        });
        return islandMinionData;
    }
}
