package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.commons.StringUtility;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

import java.util.List;

public class NewYearCakeComponent extends SkyBlockItemComponent {

    public NewYearCakeComponent(int year) {
        addInheritedComponent(new CustomDisplayNameComponent((item) -> {
            return "§cNew Year Cake (Year " + year + ")";
        }));
        addInheritedComponent(new LoreUpdateComponent(
                List.of(
                        "§7Given to every player as a",
                        "§7celebration for the " + StringUtility.ntify(year) + " Skyblock",
                        "§7year!"
                ),
                false
        ));
        addInheritedComponent(new TrackedUniqueComponent());
    }

}