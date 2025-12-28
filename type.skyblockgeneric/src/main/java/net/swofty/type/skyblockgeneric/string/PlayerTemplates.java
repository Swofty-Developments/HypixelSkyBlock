package net.swofty.type.skyblockgeneric.string;


import net.kyori.adventure.text.Component;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.function.BiFunction;

public enum PlayerTemplates {
    // Basic Information
    NAME((player, input) -> Component.text(player.getUsername())),
    UUID((player, input) -> Component.text(player.getUuid().toString())),
    DISPLAY_NAME((player, input) -> Component.text(player.getFullDisplayName())),
    SHORTENED_NAME((player, input) -> Component.text(player.getShortenedDisplayName())),

    // Currency & Resources
    COINS((player, input) -> Component.text(String.format("%.2f", player.getCoins()))),
    BITS((player, input) -> Component.text(player.getBits().toString())),
    GEMS((player, input) -> Component.text(player.getGems().toString())),

    // Statistics & Levels
    HEALTH((player, input) -> Component.text(String.format("%.1f", player.getHealth()))),
    MAX_HEALTH((player, input) -> Component.text(String.format("%.1f", player.getMaxHealth()))),
    MANA((player, input) -> Component.text(String.format("%.1f", player.getMana()))),
    MAX_MANA((player, input) -> Component.text(String.format("%.1f", player.getMaxMana()))),
    DEFENSE((player, input) -> Component.text(String.format("%.1f", player.getDefense()))),
    MINING_SPEED((player, input) -> Component.text(String.format("%.1f", player.getMiningSpeed()))),

    // Collections & Progress
    COLLECTION((player, input) -> {
        String collectionType = input.split(":")[1];
        return Component.text(player.getCollection().get(ItemType.valueOf(collectionType)).toString());
    }),
    FAIRY_SOULS((player, input) -> Component.text(player.getFairySouls().getAllFairySouls().size())),

    // Skills
    SKILL_LEVEL((player, input) -> {
        String skillName = input.split(":")[1];
        SkillCategories category = SkillCategories.valueOf(skillName);
        return Component.text(player.getSkills().getCurrentLevel(category));
    }),
    SKILL_XP((player, input) -> {
        String skillName = input.split(":")[1];
        SkillCategories category = SkillCategories.valueOf(skillName);
        return Component.text(String.format("%.2f", player.getSkills().getRaw(category)));
    }),

    // SkyBlock Experience
    SKYBLOCK_LEVEL((player, input) -> Component.text(player.getSkyBlockExperience().getLevel().asInt())),
    SKYBLOCK_XP((player, input) -> Component.text(String.format("%.2f", player.getSkyBlockExperience().getTotalXP()))),

    // Equipment & Inventory
    ARMOR_SET((player, input) -> {
        var armorSet = player.getArmorSet();
        return Component.text(armorSet != null ? armorSet.name() : "None");
    }),
    EMPTY_SLOTS((player, input) -> Component.text(player.getAmountOfEmptySlots())),

    // Region & Location
    REGION((player, input) -> {
        var region = player.getRegion();
        return Component.text(region != null ? region.getName() : "None");
    }),

    // Magical Power & Special Stats
    MAGICAL_POWER((player, input) -> Component.text(player.getMagicalPower())),
    RUNE_LEVEL((player, input) -> Component.text(player.getRuneLevel())),

    // Toggles & Settings
    TOGGLE((player, input) -> {
        String toggleName = input.split(":")[1];
        DatapointToggles.Toggles.ToggleType toggle = DatapointToggles.Toggles.ToggleType.valueOf(toggleName);
        return Component.text(player.getToggles().get(toggle));
    }),

    // Coop Status
    IS_COOP((player, input) -> Component.text(player.isCoop())),
    COOP_MEMBERS((player, input) -> {
        if (!player.isCoop()) return Component.text("0");
        return Component.text(player.getCoop().members().size());
    }),
    ;

    private final BiFunction<SkyBlockPlayer, String, Component> processor;

    PlayerTemplates(BiFunction<SkyBlockPlayer, String, Component> processor) {
        this.processor = processor;
    }

    public Component process(SkyBlockPlayer player, String input) {
        return processor.apply(player, input);
    }
}