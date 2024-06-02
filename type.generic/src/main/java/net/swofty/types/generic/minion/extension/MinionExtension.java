package net.swofty.types.generic.minion.extension;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.minion.IslandMinionData;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public abstract class MinionExtension {
    private ItemType itemTypePassedIn;
    private Object data;

    public MinionExtension(@Nullable ItemType itemType, @Nullable Object data) {
        this.itemTypePassedIn = itemType;
        this.data = data;
    }

    public abstract @NonNull GUIClickableItem getDisplayItem(IslandMinionData.IslandMinion minion, int slot);
    public abstract String toString();
    public abstract void fromString(String string);
}
