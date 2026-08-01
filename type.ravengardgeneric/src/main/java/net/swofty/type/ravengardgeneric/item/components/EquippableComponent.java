package net.swofty.type.ravengardgeneric.item.components;

import lombok.Getter;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.Equippable;
import net.swofty.type.ravengardgeneric.item.RavengardItemComponent;
import net.swofty.type.ravengardgeneric.item.RavengardItemType;

import java.util.Map;

/**
 * Sets the worn model. Without an asset id the pack falls back to plain leather, so every armour
 * piece needs this.
 */
@Getter
public class EquippableComponent implements RavengardItemComponent {
    private String assetId;

    @Override
    public String id() {
        return "EQUIPPABLE";
    }

    @Override
    public void configure(Map<String, Object> config) {
        Object value = config.get("asset_id");
        this.assetId = value == null ? null : value.toString();
    }

    @Override
    public ItemStack.Builder apply(ItemStack.Builder builder, RavengardItemType type) {
        StandardItemComponent standard = type.component(StandardItemComponent.class);
        EquipmentSlot slot = standard == null ? null : standard.equipmentSlot();
        if (assetId == null || slot == null) {
            return builder;
        }
        return builder.set(DataComponents.EQUIPPABLE, new Equippable(slot,
                Equippable.DEFAULT_EQUIP_SOUND, assetId, null, null,
                false, false, false, false, false,
                Equippable.DEFAULT_SHEARING_SOUND));
    }
}
