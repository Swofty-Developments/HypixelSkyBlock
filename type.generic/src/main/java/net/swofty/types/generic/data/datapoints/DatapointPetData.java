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
    @JsonIgnoreProperties(ignoreUnknown = true) // Due to protocol serializing "enabledPet"
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

        public void setEnabled(ItemTypeLinker type, boolean enabled) {
            // Set all previous true pets to false
            petsMap.keySet().stream().filter(pet -> pet.getAttributeHandler().getPotentialClassLinker() == type).forEach(pet -> petsMap.put(pet, false));

            // Set the new pet to the new state
            petsMap.keySet().stream().filter(pet -> pet.getAttributeHandler().getPotentialClassLinker() == type).findFirst().ifPresent(pet -> petsMap.put(pet, enabled));

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

        public void isEnabled(ItemTypeLinker type) {
            petsMap.keySet().stream().filter(pet -> pet.getAttributeHandler().getPotentialClassLinker() == type).findFirst().ifPresent(petsMap::get);
        }

        public void deselectCurrent() {
            petsMap.keySet().forEach(pet -> petsMap.put(pet, false));
        }

        public @Nullable SkyBlockItem getEnabledPet() {
            return petsMap.keySet().stream().filter(petsMap::get).findFirst().orElse(null);
        }

        public @Nullable SkyBlockItem getPet(ItemTypeLinker type) {
            return petsMap.keySet().stream().filter(pet -> pet.getAttributeHandler().getPotentialClassLinker() == type).findFirst().orElse(null);
        }

        public void removePet(ItemTypeLinker petType) {
            petsMap.keySet().removeIf(pet -> pet.getAttributeHandler().getPotentialClassLinker() == petType);
        }
    }
}
