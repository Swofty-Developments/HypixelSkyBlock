package net.swofty.types.generic.minion.extension;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.minion.IslandMinionData;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public abstract class MinionExtension {
    private ItemTypeLinker itemTypeLinkerPassedIn;
    private Object data;

    public MinionExtension(@Nullable ItemTypeLinker itemTypeLinker, @Nullable Object data) {
        this.itemTypeLinkerPassedIn = itemTypeLinker;
        this.data = data;
    }

    public abstract @NonNull GUIClickableItem getDisplayItem(IslandMinionData.IslandMinion minion, int slot);
    public abstract String toString();
    public abstract void fromString(String string);
}
