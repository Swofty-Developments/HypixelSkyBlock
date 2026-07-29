package net.swofty.type.skyblockgeneric.data.datapoints;

import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.user.SkyBlockInventory;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.Map;

public class DatapointInventory extends SkyBlockDatapoint<SkyBlockInventory> {
    private static final Serializer<SkyBlockInventory> serializer = new Serializer<>() {
        @Override
        public String serialize(SkyBlockInventory value) {
            JSONObject jsonObject = new JSONObject();
            String helmet = value.getHelmet().serialize();
            String chestplate = value.getChestplate().serialize();
            String leggings = value.getLeggings().serialize();
            String boots = value.getBoots().serialize();

            jsonObject.put("helmet", helmet);
            jsonObject.put("chestplate", chestplate);
            jsonObject.put("leggings", leggings);
            jsonObject.put("boots", boots);
            jsonObject.put("necklace", value.getNecklace().serialize());
            jsonObject.put("cloak", value.getCloak().serialize());
            jsonObject.put("belt", value.getBelt().serialize());
            jsonObject.put("gloves", value.getGloves().serialize());

            Map<Integer, String> inventory = new HashMap<>();
            value.getItems().forEach((slot, item) -> {
                inventory.put(slot, item.serialize());
            });

            jsonObject.put("inventory", inventory);

            return jsonObject.toString();
        }

        @Override
        public SkyBlockInventory deserialize(String json) {
            JSONObject jsonObject = new JSONObject(json);
            SkyBlockInventory inventory = new SkyBlockInventory();

            UnderstandableSkyBlockItem helmet;
            UnderstandableSkyBlockItem chestplate;
            UnderstandableSkyBlockItem leggings;
            UnderstandableSkyBlockItem boots;

            try {
                helmet = UnderstandableSkyBlockItem.deserialize(jsonObject.get("helmet").toString());
                chestplate = UnderstandableSkyBlockItem.deserialize(jsonObject.get("chestplate").toString());
                leggings = UnderstandableSkyBlockItem.deserialize(jsonObject.get("leggings").toString());
                boots = UnderstandableSkyBlockItem.deserialize(jsonObject.get("boots").toString());
            } catch (Exception e) {
                helmet = UnderstandableSkyBlockItem.deserialize(jsonObject.getString("helmet"));
                chestplate = UnderstandableSkyBlockItem.deserialize(jsonObject.getString("chestplate"));
                leggings = UnderstandableSkyBlockItem.deserialize(jsonObject.getString("leggings"));
                boots = UnderstandableSkyBlockItem.deserialize(jsonObject.getString("boots"));

                Logger.error(e, "Failed to deserialize inventory datapoint");
            }

            inventory.setHelmet(helmet);
            inventory.setChestplate(chestplate);
            inventory.setLeggings(leggings);
            inventory.setBoots(boots);
            inventory.setNecklace(deserializeOptionalItem(jsonObject, "necklace"));
            inventory.setCloak(deserializeOptionalItem(jsonObject, "cloak"));
            inventory.setBelt(deserializeOptionalItem(jsonObject, "belt"));
            inventory.setGloves(deserializeOptionalItem(jsonObject, "gloves"));

            Map<Integer, UnderstandableSkyBlockItem> items = new HashMap<>();
            jsonObject.getJSONObject("inventory").toMap().forEach((slot, item) -> {
                items.put(Integer.parseInt(slot), UnderstandableSkyBlockItem.deserialize((String) item));
            });

            inventory.setItems(items);

            return inventory;
        }

        @Override
        public SkyBlockInventory clone(SkyBlockInventory value) {
            SkyBlockInventory inventory = new SkyBlockInventory();
            inventory.setHelmet(value.getHelmet());
            inventory.setChestplate(value.getChestplate());
            inventory.setLeggings(value.getLeggings());
            inventory.setBoots(value.getBoots());
            inventory.setNecklace(value.getNecklace());
            inventory.setCloak(value.getCloak());
            inventory.setBelt(value.getBelt());
            inventory.setGloves(value.getGloves());
            inventory.setItems(value.getItems());
            return inventory;
        }

        private UnderstandableSkyBlockItem deserializeOptionalItem(JSONObject object, String key) {
            if (!object.has(key) || object.isNull(key)) {
                return new net.swofty.type.skyblockgeneric.item.SkyBlockItem(
                    net.minestom.server.item.Material.AIR
                ).toUnderstandable();
            }

            Object value = object.get(key);
            return UnderstandableSkyBlockItem.deserialize(value.toString());
        }
    };

    public DatapointInventory(String key, SkyBlockInventory value) {
        super(key, value, serializer);
    }

    public DatapointInventory(String key) {
        super(key, null, serializer);
    }
}
