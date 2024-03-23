package net.swofty.types.generic.data.datapoints;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.serializer.MissionDataSerializer;
import net.swofty.types.generic.serializer.UserPetDataSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatapointPetData extends Datapoint<DatapointPetData.UserPetData> {
    private static final UserPetDataSerializer serializer = new UserPetDataSerializer();

    public DatapointPetData(String key, UserPetData value) {
        super(key, value, serializer);
    }

    public DatapointPetData(String key) {
        super(key, new UserPetData(new HashMap<>()), serializer);
    }

    @JsonIgnoreProperties(ignoreUnknown = true) // Due to serializer serializing "enabledPet"
    public static class UserPetData {
        @Getter
        private final Map<SkyBlockItem, Boolean> petsMap;

        public UserPetData() {
            this.petsMap = new HashMap<>();
        }

        public UserPetData(Map<SkyBlockItem, Boolean> pets) {
            this.petsMap = pets;
        }

        public void addPet(SkyBlockItem pet) {
            petsMap.put(pet, false);
        }

        public void setEnabled(ItemType type, boolean enabled) {
            // Set all previous true pets to false
            petsMap.keySet().stream().filter(pet -> pet.getAttributeHandler().getItemTypeAsType() == type).forEach(pet -> petsMap.put(pet, false));

            // Set the new pet to the new state
            petsMap.keySet().stream().filter(pet -> pet.getAttributeHandler().getItemTypeAsType() == type).findFirst().ifPresent(pet -> petsMap.put(pet, enabled));
        }

        public void isEnabled(ItemType type) {
            petsMap.keySet().stream().filter(pet -> pet.getAttributeHandler().getItemTypeAsType() == type).findFirst().ifPresent(petsMap::get);
        }

        public void deselectCurrent() {
            petsMap.keySet().forEach(pet -> petsMap.put(pet, false));
        }

        public @Nullable SkyBlockItem getEnabledPet() {
            return petsMap.keySet().stream().filter(petsMap::get).findFirst().orElse(null);
        }

        public @Nullable SkyBlockItem getPet(ItemType type) {
            return petsMap.keySet().stream().filter(pet -> pet.getAttributeHandler().getItemTypeAsType() == type).findFirst().orElse(null);
        }

        public void removePet(ItemType petType) {
            petsMap.keySet().removeIf(pet -> pet.getAttributeHandler().getItemTypeAsType() == petType);
        }
    }
}
