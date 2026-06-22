package net.swofty.type.skyblockgeneric.user.statistics;

import lombok.Getter;
import net.minestom.server.item.Material;

import java.util.List;

@Getter
public enum StatisticModifierType {
    BASIC("Basic", Material.GRASS_BLOCK, List.of("§8Stats which are built into each item.")),
    ENCHANTMENT("Enchantment", Material.ENCHANTING_TABLE, List.of("§8Stats coming from enchantments on", "§8the item.")),
    REFORGE("Reforge", Material.ANVIL, List.of("§8Stats from the item's reforge.")),
    GEMSTONE("Gemstone", Material.AMETHYST_SHARD, List.of("§8Stats from gemstones on the item.")),
    HOT_POTATO_BOOK("Hot Potato Book", Material.BAKED_POTATO, List.of("§8Stats from Hot Potato Books on the item.")),
    ABILITY_PASSIVE("Ability/Passive", Material.BLAZE_POWDER, List.of("§8Stats from an ability or passive on", "§8the item.")),
    ATTRIBUTE("Attribute", Material.DIAMOND, List.of("§8Stats from your equipped Attributes.")),
    SKILL("Skill", Material.CRAFTING_TABLE, List.of("§8Level up your skills to gain", "§8permanent stats.")),
    INNATE("Innate", Material.NETHER_STAR, List.of("§8This is the base value of this stat", "§8which everybody starts with.")),
    PROGRESSION("Progression", Material.EXPERIENCE_BOTTLE, List.of("§8Stats from permanent progression.")),
    BUFF("Buff", Material.POTION, List.of("§8Stats from an active buff.")),
    OTHER("Other", Material.PAPER, List.of("§8Another modifier of this statistic."));

    private final String displayName;
    private final Material material;
    private final List<String> description;

    StatisticModifierType(String displayName, Material material, List<String> description) {
        this.displayName = displayName;
        this.material = material;
        this.description = description;
    }
}
