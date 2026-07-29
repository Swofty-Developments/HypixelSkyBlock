package net.swofty.type.skyblockgeneric.fishing.tag;

/**
 * Pure data tag — rarity is informational, never gates rolls.
 */
public record RarityTag(String id, int weight) implements FishingTag {

    public static final RarityTag COMMON = new RarityTag("COMMON", 0);
    public static final RarityTag UNCOMMON = new RarityTag("UNCOMMON", 1);
    public static final RarityTag RARE = new RarityTag("RARE", 2);
    public static final RarityTag EPIC = new RarityTag("EPIC", 3);
    public static final RarityTag LEGENDARY = new RarityTag("LEGENDARY", 4);
    public static final RarityTag MYTHIC = new RarityTag("MYTHIC", 5);
}
