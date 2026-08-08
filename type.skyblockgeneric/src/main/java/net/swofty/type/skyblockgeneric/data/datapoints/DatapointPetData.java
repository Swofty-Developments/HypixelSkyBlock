package net.swofty.type.skyblockgeneric.data.datapoints;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.protocol.serializers.UnderstandableSkyBlockItemSerializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.entity.PetEntityImpl;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.PetComponent;
import net.swofty.type.skyblockgeneric.item.components.SkullHeadComponent;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.AbilityRuntime;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEvent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatapointPetData extends SkyBlockDatapoint<DatapointPetData.UserPetData> {
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
        private transient List<PetAbility> cachedAbilities;
        private final transient Map<PetAbility, AbilityRuntime> abilityRuntimes = new HashMap<>();

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
            petsMap.keySet().forEach(pet -> petsMap.put(pet, false));

            // Set the new pet to the new state
            petsMap.keySet().stream().filter(pet -> pet.getAttributeHandler().getPotentialType() == type).findFirst().ifPresent(pet -> petsMap.put(pet, enabled));

            if (enabledPetEntityImpl != null)
                enabledPetEntityImpl.remove();

            refreshCachedAbilities();
        }

        public void updatePetEntityImpl(SkyBlockPlayer player) {
            if (enabledPetEntityImpl != null) {
                enabledPetEntityImpl.kill();
                enabledPetEntityImpl.remove();
            }
            if (player == null) return;

            SkyBlockItem enabledPet = getEnabledPet();
            if (enabledPet != null) {
                enabledPetEntityImpl = new PetEntityImpl(player, enabledPet.getComponent(SkullHeadComponent.class).getSkullTexture(enabledPet), enabledPet);
                enabledPetEntityImpl.setInstance(player.getInstance(), player.getPosition());
            }
        }

        public void isEnabled(ItemType type) {
            petsMap.keySet().stream().filter(pet -> pet.getAttributeHandler().getPotentialType() == type).findFirst().ifPresent(petsMap::get);
        }

        public void deselectCurrent() {
            petsMap.keySet().forEach(pet -> petsMap.put(pet, false));
            this.cachedAbilities = null;
        }

        public @Nullable SkyBlockItem getEnabledPet() {
            return petsMap.keySet().stream().filter(petsMap::get).findFirst().orElse(null);
        }

        public List<PetAbility> getCachedAbilities(SkyBlockItem pet) {
            if (cachedAbilities == null) {
                PetComponent component = pet.getComponent(PetComponent.class);
                cachedAbilities = PetHandler.valueOf(component.getHandlerId().toUpperCase()).getAbilities(pet);
            }
            return cachedAbilities;
        }

        public AbilityRuntime getAbilityRuntime(PetAbility ability) {
            return abilityRuntimes.computeIfAbsent(ability, _ -> new AbilityRuntime());
        }

        public <E extends PetEvent> E dispatch(E event) {
            SkyBlockItem pet = getEnabledPet();
            if (pet == null) return event;
            for (PetAbility ability : getCachedAbilities(pet)) {
                ability.onEvent(event);
            }
            return event;
        }

        // TODO: need to be called by Tier Boost
        public void refreshCachedAbilities() {
            SkyBlockItem activePet = getEnabledPet();
            if (activePet != null) {
                PetComponent component = activePet.getComponent(PetComponent.class);
                this.cachedAbilities = PetHandler.valueOf(component.getHandlerId().toUpperCase()).getAbilities(activePet);
            } else {
                this.cachedAbilities = null;
            }
        }

        public @Nullable SkyBlockItem getPet(ItemType type) {
            return petsMap.keySet().stream().filter(pet -> pet.getAttributeHandler().getPotentialType() == type).findFirst().orElse(null);
        }

        public void removePet(ItemType petType) {
            petsMap.keySet().removeIf(pet -> pet.getAttributeHandler().getPotentialType() == petType);
        }
    }
}
