package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bags;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minestom.server.inventory.InventoryType;

import java.util.List;

@AllArgsConstructor
@Getter
public class SackSize {
    private Integer amountItems;
    private InventoryType inventoryType;
    private List<Integer> slots;
}
