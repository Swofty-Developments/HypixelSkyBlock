package net.swofty.type.skyblockgeneric.entity.mob;

import lombok.Getter;

@Getter
public enum MobType {
    AIRBORNE("✈", "§7", "Airborne", "Mobs that can fly."),
    ANIMAL("☮", "§a", "Animal", "Mobs that are animals or have animalistic characteristics. They can be typically found at The Barn."),
    AQUATIC("⚓", "§1", "Aquatic", "Consists mostly of Water Sea Creatures or mobs that reside in water."),
    ARCANE("♃", "§5", "Arcane", "Mobs that specialize in magic or have high magical density."),
    ARTHROPOD("Ж", "§4", "Arthropod", "Mainly consists of spiders and other invertebrate-adjacent mobs."),
    CONSTRUCT("⚙", "§7", "Construct", "Mobs that are summoned by other enemies or are artificially created."),
    CUBIC("⚂", "§a", "Cubic", "Mobs that have cube-like or blocky appearances."),
    ELUSIVE("♣", "§d", "Elusive", "Mobs that are rare and hard to find."),
    ENDER("⊙", "§5", "Ender", "Mobs related to the End dimension."),
    FROZEN("☃", "§f", "Frozen", "Mobs that reside in Jerry's Workshop."),
    GLACIAL("❄", "§b", "Glacial", "Mobs that reside in the Glacite Mineshaft."),
    HUMANOID("✰", "§e", "Humanoid", "Enemies that are found in the Crystal Hollows."),
    INFERNAL("♨", "§4", "Infernal", "Extremely dangerous mobs native to the Crimson Isle."),
    MAGMATIC("♆", "§c", "Magmatic", "Mobs that spawn in lava or fiery environments."),
    MYTHOLOGICAL("✿", "§2", "Mythological", "Mobs that appear during the Mythological Ritual Event."),
    PEST("ൠ", "§2", "Pest", "Mobs considered nuisances, often found in the Garden."),
    SHIELDED("⛨", "§e", "Shielded", "Mobs that take only one point of damage per hit."),
    SKELETAL("🦴", "§f", "Skeletal", "Skeleton-based mobs or those with skeletal traits."),
    SPOOKY("☽", "§6", "Spooky", "Mobs that appear during the Spooky Festival and Great Spook."),
    SUBTERRANEAN("⛏", "§6", "Subterranean", "Mobs found in Dwarven Mines."),
    UNDEAD("༕", "§2", "Undead", "Mobs that have risen from their graves."),
    WITHER("☠", "§8", "Wither", "Mobs related to the Wither or found in the Catacombs."),
    WOODLAND("⸙", "§2", "Woodland", "Mobs that reside in Galatea.");

    private final String symbol;
    private final String color;
    private final String displayName;
    private final String description;

    MobType(String symbol, String color, String displayName, String description) {
        this.symbol = symbol;
        this.color = color;
        this.displayName = displayName;
        this.description = description;
    }

    public String getFullDisplayName() {
        return color + symbol + " " + displayName + "§r";
    }

    @Override
    public String toString() {
        return symbol + " " + displayName;
    }
}