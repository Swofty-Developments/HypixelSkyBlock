package net.swofty.types.generic.data.datapoints;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.entity.PetEntityImpl;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.serializer.UserPetDataSerializer;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class DatapointPetData extends Datapoint<DatapointPetData.UserPetData> {
    private static final UserPetDataSerializer serializer = new UserPetDataSerializer();

    public DatapointPetData(String key, UserPetData value) {
        super(key, value, serializer);
    }

    public DatapointPetData(String key) {
        super(key, new UserPetData(new HashMap<>()), serializer);
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true) // Due to serializer serializing "enabledPet"
    public static class UserPetData {
        private HashMap<SkyBlockItem, Boolean> petsMap;
        private PetEntityImpl enabledPetEntityImpl = null;

        public UserPetData() {
            this.petsMap = new HashMap<>();
        }

        public UserPetData(HashMap<SkyBlockItem, Boolean> pets) {
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

            if (enabledPetEntityImpl != null)
                enabledPetEntityImpl.remove();
        }

        public void updatePetEntityImpl(SkyBlockPlayer player) {
            if (enabledPetEntityImpl != null) {
                enabledPetEntityImpl.kill();
                enabledPetEntityImpl.remove();
            }
            if (player == null) return;

            SkyBlockItem enabledPet = getEnabledPet();
            if (enabledPet != null) {
                enabledPetEntityImpl = new PetEntityImpl(player, ((SkullHead) enabledPet.getGenericInstance()).getSkullTexture(
                        player, enabledPet
                ), enabledPet);
                enabledPetEntityImpl.setInstance(player.getInstance(), player.getPosition());
            }
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
