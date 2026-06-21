package net.swofty.type.generic.collectibles.bedwars;

import net.minestom.server.item.Material;
import net.swofty.type.generic.collectibles.CollectibleCategory;
import net.swofty.type.generic.collectibles.CollectibleDefinition;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Optional;

public final class BedWarsWoodSkinRuntimeService {

    private BedWarsWoodSkinRuntimeService() {
    }

    public static Material resolveSelectedMaterial(HypixelPlayer player) {
        Optional<CollectibleDefinition> selected = BedWarsCollectibleStateService.reconcileSelected(
            player,
            CollectibleCategory.WOOD_SKINS
        );
        if (selected.isEmpty()) {
            return Material.OAK_PLANKS;
        }

        Material selectedMaterial = BedWarsCollectibleCatalog.parseMaterial(selected.get().effectiveSelectionValue());
        if (selectedMaterial != null) {
            return selectedMaterial;
        }

        Material iconMaterial = selected.get().iconMaterial();
        return iconMaterial != null ? iconMaterial : Material.OAK_PLANKS;
    }
}
