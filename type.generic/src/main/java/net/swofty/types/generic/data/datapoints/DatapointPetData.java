package net.swofty.types.generic.data.datapoints;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.protocol.serializers.UnderstandableSkyBlockItemSerializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.entity.PetEntityImpl;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;

public class DatapointPetData extends Datapoint<DatapointPetData.UserPetData> {
    private static final Serializer serializer = new Serializer<UserPetData>() {
        @Override
        public String serialize(UserPetData value) {
            JSONObject jsonObject = new JSONObject();
            HashMap<SkyBlockItem, Boolean> petsMap = value.getPetsMap();

            for (SkyBlockItem item : petsMap.keySet()) {
                boolean enabled = petsMap.get(item);
                jsonObject.put(new UnderstandableSkyBlockItemSerializer().serialize(item.toUnderstandable()), enabled);
            }

            return jsonObject.toString();
        }

        @Override
        public UserPetData deserialize(String json) {
            JSONObject jsonObject = new JSONObject(json);
            HashMap<SkyBlockItem, Boolean> petsMap = new HashMap<>();

            jsonObject.keySet().forEach(key -> {
                UnderstandableSkyBlockItem item = new UnderstandableSkyBlockItemSerializer().deserialize(key);
                boolean enabled = jsonObject.getBoolean(key);
                petsMap.put(new SkyBlockItem(item), enabled);
            });

            return new UserPetData(petsMap);
        }

        @Override
        public UserPetData clone(UserPetData value) {
            return new UserPetData(value.petsMap);
        }
    };

    public DatapointPetData(String key, UserPetData value) {
        super(key, value, serializer);
    }

    public DatapointPetData(String key) {
        super(key, new UserPetData(new HashMap<>()), serializer);
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true) // Due to protocols serializing "enabledPet"
    public static class UserPetData {
        private HashMap<SkyBlockItem, Boolean> petsMap;
        private PetEntityImpl enabledPetEntityImpl = null;

        public UserPetData() {
            this.petsMap = new HashMap<>();
        }

        public UserPetData(HashMap<SkyBlockItem, Boolean> pets) {
            this.petsMap = pets;
        }

        /**
         * Adds a new pet to the pet menu.
         *
         * @param pet the {@link SkyBlockItem} representing the pet to be added
         */
        public void addPet(SkyBlockItem pet) {
            petsMap.put(pet, false);
        }

        /**
         * Sets the enabled state for a pet of a specific type.
         * It disables all previous pets of that type before enabling the new one.
         *
         * @param type the {@link ItemTypeLinker} representing the type of pet to enable or disable
         * @param enabled true to enable the pet, false to disable it
         */
        public void setEnabled(ItemTypeLinker type, boolean enabled) {
            // Set all previous true pets to false
            petsMap.keySet().stream()
                    .filter(pet -> pet.getAttributeHandler().getPotentialClassLinker() == type)
                    .forEach(pet -> petsMap.put(pet, false));

            // Set the new pet to the new state
            petsMap.keySet().stream()
                    .filter(pet -> pet.getAttributeHandler().getPotentialClassLinker() == type)
                    .findFirst()
                    .ifPresent(pet -> petsMap.put(pet, enabled));

            if (enabledPetEntityImpl != null) {
                enabledPetEntityImpl.remove();
            }
        }

        /**
         * Updates the pet entity implementation for the given player.
         * If an enabled pet exists, it will be removed and replaced with the newly enabled pet.
         *
         * @param player the {@link SkyBlockPlayer} to update the pet entity for
         */
        public void updatePetEntityImpl(SkyBlockPlayer player) {
            if (enabledPetEntityImpl != null) {
                enabledPetEntityImpl.kill();
                enabledPetEntityImpl.remove();
            }
            if (player == null) return;

            SkyBlockItem enabledPet = getEnabledPet();
            if (enabledPet != null) {
                enabledPetEntityImpl = new PetEntityImpl(player,
                        ((SkullHead) enabledPet.getGenericInstance()).getSkullTexture(player, enabledPet),
                        enabledPet);
                enabledPetEntityImpl.setInstance(player.getInstance(), player.getPosition());
            }
        }

        /**
         * Checks if a specific pet type is enabled.
         *
         * @param type the {@link ItemTypeLinker} representing the pet type to check
         * @return true if the pet type is enabled, false otherwise
         */
        public boolean isEnabled(ItemTypeLinker type) {
            return petsMap.keySet().stream()
                    .filter(pet -> pet.getAttributeHandler().getPotentialClassLinker() == type)
                    .findFirst()
                    .map(petsMap::get)
                    .orElse(false);
        }

        /**
         * Deselects all currently enabled pets.
         */
        public void deselectCurrent() {
            petsMap.keySet().forEach(pet -> petsMap.put(pet, false));
        }

        /**
         * Retrieves the currently enabled pet.
         *
         * @return the enabled {@link SkyBlockItem} representing the pet, or null if none is enabled
         */
        public @Nullable SkyBlockItem getEnabledPet() {
            return petsMap.keySet().stream()
                    .filter(petsMap::get)
                    .findFirst()
                    .orElse(null);
        }

        /**
         * Retrieves a pet of a specific type.
         *
         * @param type the {@link ItemTypeLinker} representing the pet type to retrieve
         * @return the {@link SkyBlockItem} representing the pet, or null if it does not exist
         */
        public @Nullable SkyBlockItem getPet(ItemTypeLinker type) {
            return petsMap.keySet().stream()
                    .filter(pet -> pet.getAttributeHandler().getPotentialClassLinker() == type)
                    .findFirst()
                    .orElse(null);
        }

        /**
         * Removes a pet of a specific type from the pet menu.
         *
         * @param petType the {@link ItemTypeLinker} representing the type of pet to remove
         */
        public void removePet(ItemTypeLinker petType) {
            petsMap.keySet().removeIf(pet -> pet.getAttributeHandler().getPotentialClassLinker() == petType);
        }
    }
}
