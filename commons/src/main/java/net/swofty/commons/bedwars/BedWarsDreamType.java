package net.swofty.commons.bedwars;

import lombok.Getter;

import java.util.List;

@Getter
public enum BedWarsDreamType {
    SWAPPAGE("Swappage", "Use the Swap to change ownership", "between beds! Chaos ensues."),
    ONE_BLOCK("One Block"),
    RUSH("Rush V2"),
    ULTIMATE("Ultimate V2"),
    CASTLE("40v40 Castle V2"),
    VOIDLESS("Voidless"),
    ARMED("Armed");

    private final String displayName;
    private final List<String> desc;

    BedWarsDreamType(String name, String... desc) {
        this.displayName = name;
        this.desc = List.of(desc);
    }

}
