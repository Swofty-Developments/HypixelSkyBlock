package net.swofty.type.skyblockgeneric.gems;

import lombok.Getter;
import net.swofty.commons.skyblock.item.Rarity;

import java.util.List;

@Getter
public enum GemRarity {
    ROUGH("Rough", Rarity.COMMON, 1, "§f", "§7Taken right from the heart of a", "§7crystal vein in the §5Crystal", "§5Hollows§7."),
    FLAWED("Flawed", Rarity.UNCOMMON, 100, "§a", "§7A slightly better version of", "{GEM}§7, but it could still use some work."),
    FINE("Fine", Rarity.RARE, 10000, "§b", "§7A type of {GEM} §7that has", "§7clearly been treated with care."),
    FLAWLESS("Flawless", Rarity.EPIC, 100000, "§5", "§7A new perfect version of", "{GEM}§7."),
    PERFECT("Perfect", Rarity.LEGENDARY, 500000, "§6", "§7A perfectly refined {GEM}§7."),
    ;

    public final String name;
    public final Rarity rarity;
    public final Integer costToRemove;
    public final List<String> description;
    public final String bracketColor;

    GemRarity(String name, Rarity rarity, Integer costToRemove, String bracketColor, String... description) {
        this.name = name;
        this.rarity = rarity;
        this.costToRemove = costToRemove;
        this.bracketColor = bracketColor;
        this.description = List.of(description);
    }
}
