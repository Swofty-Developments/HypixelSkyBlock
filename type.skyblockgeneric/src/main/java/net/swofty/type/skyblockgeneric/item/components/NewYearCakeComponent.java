package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.commons.StringUtility;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.item.handlers.lore.LoreConfig;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public class NewYearCakeComponent extends SkyBlockItemComponent {

    public NewYearCakeComponent() {
        addInheritedComponent(new CustomDisplayNameComponent((item) -> {
            int year = item.getAttributeHandler().getNewYearCakeYear();
            return "§cNew Year Cake (Year " + year + ")";
        }));
        addInheritedComponent(new LoreUpdateComponent(
                new LoreConfig(this::lore, null),
                false
        ));
        addInheritedComponent(new TrackedUniqueComponent());
    }

    private List<String> lore(SkyBlockItem item, SkyBlockPlayer player) {
        int year = item.getAttributeHandler().getNewYearCakeYear();
        return List.of(
                "§7Given to every player as a",
                "§7celebration for the " + StringUtility.ntify(year) + " Skyblock",
                "§7year!"
        );
    }

}