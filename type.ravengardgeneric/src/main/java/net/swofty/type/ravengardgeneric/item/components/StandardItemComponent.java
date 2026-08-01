package net.swofty.type.ravengardgeneric.item.components;

import lombok.Getter;
import net.minestom.server.entity.EquipmentSlot;
import net.swofty.type.ravengardgeneric.item.RavengardItemComponent;

import java.util.Map;

/** Marks what kind of item this is; armour types also give the slot it equips into. */
@Getter
public class StandardItemComponent implements RavengardItemComponent {
    private String standardItemType;

    @Override
    public String id() {
        return "STANDARD_ITEM";
    }

    @Override
    public void configure(Map<String, Object> config) {
        Object value = config.get("standard_item_type");
        this.standardItemType = value == null ? null : value.toString().toUpperCase();
    }

    public EquipmentSlot equipmentSlot() {
        if (standardItemType == null) {
            return null;
        }
        return switch (standardItemType) {
            case "HELMET" -> EquipmentSlot.HELMET;
            case "CHESTPLATE" -> EquipmentSlot.CHESTPLATE;
            case "LEGGINGS" -> EquipmentSlot.LEGGINGS;
            case "BOOTS" -> EquipmentSlot.BOOTS;
            default -> null;
        };
    }
}
