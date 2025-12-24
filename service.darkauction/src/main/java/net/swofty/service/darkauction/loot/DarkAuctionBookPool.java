package net.swofty.service.darkauction.loot;

import net.swofty.commons.skyblock.item.ItemType;

import java.util.List;
import java.util.Random;

/**
 * Pool for Dark Auction round 2 (book round)
 * All books have equal chance of being selected
 *
 * In actual Hypixel, these are specific enchanted books like:
 * - Soul Eater V
 * - One For All I
 * - Bank V
 * - Chimera V
 * - Duplex I
 * - Legion V
 * - Ender Slayer VII
 * - No Pain No Gain I
 * - Mana Vampire VI
 *
 * For now, we use ENCHANTED_BOOK as a placeholder.
 * The specific enchantment type would be handled by item metadata.
 */
public class DarkAuctionBookPool {
    private static final Random random = new Random();

    // Book pool - for now just returning ENCHANTED_BOOK
    // The specific enchantment would be determined by item configuration
    private static final List<String> BOOK_TYPES = List.of(
            "SOUL_EATER_V",
            "ONE_FOR_ALL_I",
            "BANK_V",
            "CHIMERA_V",
            "DUPLEX_I",
            "LEGION_V",
            "ENDER_SLAYER_VII",
            "NO_PAIN_NO_GAIN_I",
            "MANA_VAMPIRE_VI"
    );

    /**
     * Select a random book for the auction
     * Returns ENCHANTED_BOOK - the specific type is stored separately
     */
    public static ItemType selectRandom() {
        // For now, return ENCHANTED_BOOK
        // The actual book type would be stored in item metadata
        return ItemType.ENCHANTED_BOOK;
    }

    /**
     * Get the name of a random book type (for display/tracking purposes)
     */
    public static String getRandomBookTypeName() {
        return BOOK_TYPES.get(random.nextInt(BOOK_TYPES.size()));
    }

    /**
     * Get all available book types
     */
    public static List<String> getAvailableBookTypes() {
        return BOOK_TYPES;
    }
}
