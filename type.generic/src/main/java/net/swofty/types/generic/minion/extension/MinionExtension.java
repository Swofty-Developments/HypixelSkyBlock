package net.swofty.types.generic.minion.extension;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.minion.IslandMinionData;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public abstract class MinionExtension {
    private ItemType itemTypePassedIn;
    private Object data;

    public MinionExtension(@Nullable ItemType itemTypeLinker, @Nullable Object data) {
        this.itemTypePassedIn = itemTypeLinker;
        this.data = data;
    }

    public abstract @NonNull GUIItem getDisplayItem(IslandMinionData.IslandMinion minion, int slot, SkyBlockAbstractInventory inventory);
    public abstract String toString();
    public abstract void fromString(String string);
}
