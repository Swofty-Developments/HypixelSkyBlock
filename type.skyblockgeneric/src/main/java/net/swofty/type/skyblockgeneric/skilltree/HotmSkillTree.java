package net.swofty.type.skyblockgeneric.skilltree;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static net.swofty.type.skyblockgeneric.skilltree.TreeNodeDefinition.NodeRenderContext;
import static net.swofty.type.skyblockgeneric.skilltree.TreeNodeDefinition.NodeValue;

public final class HotmSkillTree {
    private static final String HEAD_TEXTURE = "86f06eaa3004aeed09b3d5b45d976de584e691c0e9cade133635de93d23b9edb";

    private HotmSkillTree() {
    }

    public static SkillTreeDefinition create() {
        return new SkillTreeDefinition(
                SkillTreeType.HOTM,
                "Heart of the Mountain",
                HEAD_TEXTURE,
                9,
                List.of(
                        miningSpeed(), miningSpeedBoost(), precisionMining(), miningFortune(), titaniumInsanium(), pickobulus(),
                        luckOfTheCave(), efficientMiner(), quickForge(), skyMall(), oldSchool(), professional(), mole(), gemLover(),
                        seasonedMineman(), frontLoaded(), dailyGrind(), coreOfTheMountain(), dailyPowder(), anomalousDesire(), blockhead(),
                        subterraneanFisher(), keepItCool(), lonesomeMiner(), greatExplorer(), maniacMiner(), powderBuff(), speedyMineman(),
                        fortunateMineman(), warmHeart(), minersBlessing(), noStoneUnturned(), strongArm(), steadyHand(), mineshaftMayhem(),
                        surveyor(), metalHead(), ragsToRiches(), eagerAdventurer(), gemstoneInfusion(), crystalline(), giftsFromTheDeparted(),
                        miningMaster(), deadMansChest(), vanguardSeeker(), sheerForce()
                ),
                tiers()
        );
    }

    private static TreeNodeDefinition miningSpeed() {
        return node("mining_speed", "Mining Speed", 3, 9, 50)
                .powder(TreePowder.MITHRIL).cost(3).value("stat", level(20))
                .lore("§7Grants §a+§a{stat} §6⸕ Mining Speed§7.").build();
    }

    private static TreeNodeDefinition miningSpeedBoost() {
        return node("mining_speed_boost", "Mining Speed Boost", 1, 8, 1).ability()
                .powder(TreePowder.MITHRIL)
                .value("statBoost", coreValue(200, 250))
                .value("statDuration", coreValue(10, 15))
                .lore("", "§6Pickaxe Ability: Mining Speed Boost", "§7Grants §a+§a{statBoost}% §6⸕ Mining Speed §7for", "§7§a{statDuration}s§7.", "§8Cooldown: §a120s")
                .build();
    }

    private static TreeNodeDefinition precisionMining() {
        return node("precision_mining", "Precision Mining", 2, 8, 1)
                .powder(TreePowder.MITHRIL)
                .lore("§7When Mining §6Ores§7 or §8Dwarven Metals§7,", "§7a particle target appears on the", "§7block that increases your §6⸕ Mining", "§6Speed §7by §a30% §7when aiming at it.")
                .build();
    }

    private static TreeNodeDefinition miningFortune() {
        return node("mining_fortune", "Mining Fortune", 3, 8, 50)
                .powder(TreePowder.MITHRIL).cost(3.05).value("stat", level(2))
                .lore("§7Grants §a+§a{stat} §6☘ Mining Fortune§7.").build();
    }

    private static TreeNodeDefinition titaniumInsanium() {
        return node("titanium_insanium", "Titanium Insanium", 4, 8, 50)
                .powder(TreePowder.MITHRIL).cost(3.1).value("stat", (context) -> number(2 + context.level() * 0.1))
                .lore("§7When mining §2Mithril Ore§7, you have a", "§7§a{stat}% §7chance to convert the block", "§7into §fTitanium Ore§7.")
                .build();
    }

    private static TreeNodeDefinition pickobulus() {
        return node("pickobulus", "Pickobulus", 5, 8, 1).ability()
                .powder(TreePowder.MITHRIL).value("stat", coreValue(60, 50))
                .lore("", "§6Pickaxe Ability: Pickobulus", "§7Throw your pickaxe to create an", "§7explosion on impact, mining all ores", "§7within a §a2 §7block radius.", "§8Cooldown: §a{stat}s")
                .build();
    }

    private static TreeNodeDefinition luckOfTheCave() {
        return node("luck_of_the_cave", "Luck of the Cave", 1, 7, 45)
                .powder(TreePowder.MITHRIL).cost(3.07).value("stat", (context) -> number(context.level() + 5))
                .require("mining_speed_boost")
                .lore("§7Increases the chance for you to", "§7trigger rare occurrences in the", "§7§2Dwarven Mines §7by §a{stat}%§7.", "", "§7Rare occurrences include:", "§7 • §6Golden Goblins", "§7 • §5Fallen Stars", "§7 • §6Powder Ghasts")
                .build();
    }

    private static TreeNodeDefinition efficientMiner() {
        return node("efficient_miner", "Efficient Miner", 3, 7, 100)
                .powder(TreePowder.MITHRIL).cost(2.6).value("stat", level(3))
                .lore("§7Grants §e+{stat}▚ Mining Spread§7.").build();
    }

    private static TreeNodeDefinition quickForge() {
        return node("quick_forge", "Quick Forge", 5, 7, 20)
                .powder(TreePowder.MITHRIL).cost(4)
                .value("stat", context -> number(context.level() < 20 ? 10 + context.level() * 0.5 : 30))
                .lore("§7Decreases the time it takes to forge", "§7by §a{stat}%§7.").build();
    }

    private static TreeNodeDefinition skyMall() {
        return node("sky_mall", "Sky Mall", 0, 6, 1)
                .powder(TreePowder.MITHRIL)
                .require("old_school")
                .lore("§7Every SkyBlock day, you receive a", "§7random buff while on any §bMining", "§bIsland§7.", "", "§7Possible Buffs", "§8 ■ §7Gain §6+100⸕ Mining Speed§7.", "§8 ■ §7Gain §6+50☘ Mining Fortune§7.", "§8 ■ §7Gain §a+15% §7more Powder while mining.", "§8 ■ §7§a-20%§7 Pickaxe Ability cooldowns.", "§8 ■ §7§a10x §7chance to find Golden and", "    §7Diamond Goblins.", "§8 ■ §7Gain §a5x §9Titanium §7drops.")
                .build();
    }

    private static TreeNodeDefinition oldSchool() {
        return node("old_school", "Old-School", 1, 6, 20)
                .powder(TreePowder.GEMSTONE).cost(4).value("stat", level(5)).require("professional")
                .lore("§7Grants §6+{stat}☘ Ore Fortune§7.").build();
    }

    private static TreeNodeDefinition professional() {
        return node("professional", "Professional", 2, 6, 140)
                .powder(TreePowder.GEMSTONE).cost(2.3).value("stat", context -> number(context.level() * 5 + 50))
                .lore("§7Gain §a+§a{stat} §6⸕ Mining Speed §7when mining", "§7Gemstones.").build();
    }

    private static TreeNodeDefinition mole() {
        return node("mole", "Mole", 3, 6, 200)
                .powder(TreePowder.GEMSTONE).cost(2.2)
                .value("stat", context -> number(50 + ((context.level() - 1) * 350.0 / 199)))
                .lore("§7Grants §e+{stat}▚ Mining Spread§7 when", "§7mining Hard Stone.").build();
    }

    private static TreeNodeDefinition gemLover() {
        return node("gem_lover", "Gem Lover", 4, 6, 20)
                .powder(TreePowder.GEMSTONE).cost(4).value("stat", context -> number(context.level() * 4 + 20))
                .lore("§7Grants §6+{stat}☘ Gemstone Fortune§7.").build();
    }

    private static TreeNodeDefinition seasonedMineman() {
        return node("seasoned_mineman", "Seasoned Mineman", 5, 6, 100)
                .powder(TreePowder.GEMSTONE).cost(2.3).value("stat", context -> number(5 + context.level() * 0.1))
                .lore("§7Grants §3+{stat}☯ Mining Wisdom§7.").build();
    }

    private static TreeNodeDefinition frontLoaded() {
        return node("front_loaded", "Front Loaded", 6, 6, 1)
                .powder(TreePowder.GEMSTONE)
                .lore("§7§7Grants the following buffs for the", "§7first §a2,500 §dGemstones§7 you mine each", "§7day.", " ", " §8■ §d3x Gemstone Powder", " §8■ §6+150☘ Gemstone Fortune", " §8■ §6+250⸕ Mining Speed").build();
    }

    private static TreeNodeDefinition dailyGrind() {
        return node("daily_grind", "Daily Grind", 1, 5, 1)
                .powder(TreePowder.GLACITE).value("stat", context -> String.valueOf(context.hotmTier() * 500)).require("old_school")
                .lore("§7§7Your first daily commission on each", "§7§bMining Island§7 grants §9+500 Powder§7,", "§7multiplied by your §5HOTM§7 level.", "", "§2Dwarven Mines§7: §a+{stat} §2Mithril Powder", "§5Crystal Hollows§7: §a+{stat} §dGemstone Powder", "§bGlacite Tunnels§7: §a+{stat} §bGlacite Powder").build();
    }

    private static TreeNodeDefinition coreOfTheMountain() {
        return node("core_of_the_mountain", "Core of the Mountain", 3, 5, 10)
                .powder(level -> level < 3 ? TreePowder.MITHRIL : level < 7 ? TreePowder.GEMSTONE : TreePowder.GLACITE)
                .costs(0, 50_000, 100_000, 200_000, 300_000, 400_000, 600_000, 750_000, 1_000_000, 1_250_000, 0)
                .customLore(HotmSkillTree::coreLore)
                .build();
    }

    private static TreeNodeDefinition dailyPowder() {
        return node("daily_powder", "Daily Powder", 5, 5, 1)
                .powder(TreePowder.GEMSTONE).value("stat", context -> String.valueOf(context.hotmTier() * 500))
                .lore("§7The first ore you mine each day", "§7grants §9+500 Powder§7, multiplied by", "§7your §5HOTM §7level.", "", "§2Mithril§7: §a+{stat} §2Mithril Powder", "§dGemstone§7: §a+{stat} §dGemstone Powder", "§bGlacite§7: §a+{stat} §bGlacite Powder").build();
    }

    private static TreeNodeDefinition anomalousDesire() {
        return node("anomalous_desire", "Tunnel Vision", 0, 4, 1).ability()
                .powder(TreePowder.GEMSTONE).value("stat", coreValue(30, 40)).value("statCooldown", coreValue(120, 110))
                .require("blockhead")
                .lore("§6Pickaxe Ability: Tunnel Vision", "§7Increases the chances of triggering", "§7rare occurrences by §a§a{stat}%§7 for §a30s§7.", "", "§7Rare occurrences include:", "§7§8 ■ §6Golden Goblins", "§7§8 ■ §5Fallen Stars", "§7§8 ■ §6Powder Ghasts", "§7§8 ■ §6Worms", "§7§8 ■ §bGlacite Mineshafts", "§8Cooldown: §a{statCooldown}s")
                .build();
    }

    private static TreeNodeDefinition blockhead() {
        return node("blockhead", "Blockhead", 1, 4, 20)
                .powder(TreePowder.GEMSTONE).cost(4).value("stat", level(5)).require("daily_grind")
                .lore("§7Grants §6+{stat}☘ Block Fortune§7.").build();
    }

    private static TreeNodeDefinition subterraneanFisher() {
        return node("subterranean_fisher", "Subterranean Fisher", 2, 4, 40)
                .powder(TreePowder.GEMSTONE).cost(3.7)
                .value("statFishingSpeed", context -> number(context.level() * 0.5 + 5))
                .value("statSeaCreatureChance", context -> number(context.level() * 0.1 + 1))
                .require("keep_it_cool")
                .lore("§7§7Grants §b+{statFishingSpeed}☂ Fishing Speed§7 and §3+{statSeaCreatureChance}α", "§7§3Sea Creature Chance§7 while on §bMining", "§7§bIslands§7.").build();
    }

    private static TreeNodeDefinition keepItCool() {
        return node("keep_it_cool", "Keep It Cool", 3, 4, 50)
                .powder(TreePowder.GEMSTONE).cost(3.07).value("stat", level(0.4))
                .lore("§7Grants §c+{stat}♨ Heat Resistance§7.").build();
    }

    private static TreeNodeDefinition lonesomeMiner() {
        return node("lonesome_miner", "Lonesome Miner", 4, 4, 45)
                .powder(TreePowder.GEMSTONE).cost(3.07).value("stat", context -> number(context.level() * 0.5 + 4.5))
                .lore("§7Increases §c❁ Strength§7, §9☣ Crit", "§9Chance§7, §9☠ Crit Damage§7, §a❈ Defense§7,", "§7and §c❤ Health §7statistics gain by §a{stat}%", "§a§7while on §bMining Islands§7.").build();
    }

    private static TreeNodeDefinition greatExplorer() {
        return node("great_explorer", "Great Explorer", 5, 4, 20)
                .powder(TreePowder.GEMSTONE).cost(4)
                .value("statChance", context -> number(context.level() * 4 + 16))
                .value("statLocks", context -> String.valueOf(Math.round(context.level() / 5.0 + 1)))
                .require("daily_powder")
                .lore("§7Boosts the chance to find treasure", "§7chests while mining in the §5Crystal", "§5Hollows §7by §a+§a{statChance}% §7and reduces the", "§7amount of locks on the chests by §a{statLocks}§7.").build();
    }

    private static TreeNodeDefinition maniacMiner() {
        return node("maniac_miner", "Maniac Miner", 6, 4, 1).ability()
                .powder(TreePowder.GEMSTONE).value("stat", coreValue(5, 10)).value("statDuration", coreValue(25, 30))
                .require("core_of_the_mountain")
                .lore("", "§6Pickaxe Ability: Maniac Miner", "§7Grants §2+1Ⓟ Breaking Power§7 and a", "§7stack of §6+{stat}☘ Mining Fortune §8(caps", "§8at 1000) §7per block broken for §a{statDuration}s§7.", "§7Each block broken consumes §b20 Mana§7.", "§8Cooldown: §a120s").build();
    }

    private static TreeNodeDefinition powderBuff() {
        return node("powder_buff", "Powder Buff", 3, 3, 50)
                .powder(TreePowder.GEMSTONE).cost(3.2).value("stat", context -> String.valueOf(context.level()))
                .lore("§7Gain §a+§a{stat}% §7more Powder from any", "§7source.").build();
    }

    private static TreeNodeDefinition speedyMineman() {
        return node("speedy_mineman", "Speedy Mineman", 1, 3, 50)
                .powder(TreePowder.GEMSTONE).cost(3.2).value("stat", level(40))
                .lore("§7Grants §a+§a{stat} §6⸕ Mining Speed§7.").build();
    }

    private static TreeNodeDefinition fortunateMineman() {
        return node("fortunate_mineman", "Fortunate Mineman", 5, 3, 50)
                .powder(TreePowder.GEMSTONE).cost(3.2).value("stat", level(3))
                .lore("§7Grants §a+§a{stat} §6☘ Mining Fortune§7.").build();
    }

    private static TreeNodeDefinition warmHeart() {
        return node("warm_heart", "Warm Heart", 4, 2, 50)
                .powder(TreePowder.GLACITE).cost(3.1).value("stat", level(0.4))
                .lore("§7Grants §a+§a{stat} §b❄ Cold Resistance§7.").build();
    }

    private static TreeNodeDefinition minersBlessing() {
        return node("miners_blessing", "Miner's Blessing", 0, 2, 1)
                .powder(TreePowder.GLACITE)
                .lore("§7Grants §b+30✯ Magic Find§7 on all §bMining", "§bIslands§7.").build();
    }

    private static TreeNodeDefinition noStoneUnturned() {
        return node("no_stone_unturned", "No Stone Unturned", 1, 2, 50)
                .powder(TreePowder.GLACITE).cost(3.05).value("stat", level(0.5))
                .lore("§7Increases your chances of finding a", "§7§9Suspicious Scrap §7when mining in a", "§7§bGlacite Mineshaft §7by §a§a{stat}%§7.").build();
    }

    private static TreeNodeDefinition strongArm() {
        return node("strong_arm", "Strong Arm", 2, 2, 100)
                .powder(TreePowder.GLACITE).cost(2.3).value("stat", level(5))
                .lore("§7Gain §6+{stat}⸕ Mining Speed§7 when mining", "§7§6Dwarven Metals§7.").build();
    }

    private static TreeNodeDefinition steadyHand() {
        return node("steady_hand", "Steady Hand", 3, 2, 100)
                .powder(TreePowder.GLACITE).cost(2.6).value("stat", level(0.1))
                .lore("§7Grants §e+{stat}▚ Gemstone Spread§7 while", "§7in the §bGlacite Mineshafts§7.").build();
    }

    private static TreeNodeDefinition mineshaftMayhem() {
        return node("mineshaft_mayhem", "Mineshaft Mayhem", 6, 2, 1)
                .powder(TreePowder.GLACITE)
                .lore("§7Every time you enter a §bGlacite", "§bMineshaft§7, you receive a random buff.", "", "§7Possible Buffs", "§8 ■ §a+5% §7chance to find a §9Suspicious Scrap§7.", "§8 ■ §7Gain §a+100 §6☘ Mining Fortune§7", "§8 ■ §7Gain §a+200 §6⸕ Mining Speed§7", "§8 ■ §7Gain §a+10 §b❄ Cold Resistance§7", "§8 ■ §7Reduce Pickaxe Ability cooldown by §a25%§7.").build();
    }

    private static TreeNodeDefinition surveyor() {
        return node("surveyor", "Surveyor", 5, 2, 20)
                .powder(TreePowder.GLACITE).cost(4).value("stat", level(0.75))
                .lore("§7Increases your chances of finding a", "§7§bGlacite Mineshaft §7when mining in the", "§7§bGlacite Tunnels §7by §a§a{stat}%§7.").build();
    }

    private static TreeNodeDefinition metalHead() {
        return node("metal_head", "Metal Head", 1, 1, 20)
                .powder(TreePowder.GLACITE).cost(4).value("stat", level(5))
                .lore("§7Grants §6+{stat}☘ Dwarven Metal Fortune§7.").build();
    }

    private static TreeNodeDefinition ragsToRiches() {
        return node("rags_to_riches", "Rags to Riches", 3, 1, 50)
                .powder(TreePowder.GLACITE).cost(3.05).value("stat", level(4))
                .lore("§7Grants §a+§a{stat} §6☘ Mining Fortune §7while", "§7inside a §bGlacite Mineshaft§7.").build();
    }

    private static TreeNodeDefinition eagerAdventurer() {
        return node("eager_adventurer", "Eager Adventurer", 5, 1, 100)
                .powder(TreePowder.GLACITE).cost(2.3).value("stat", level(4))
                .lore("§7Grants §a+§a{stat}§7 §6⸕ Mining Speed §7while", "§7inside the §bGlacite Mineshafts§7.").build();
    }

    private static TreeNodeDefinition gemstoneInfusion() {
        return node("gemstone_infusion", "Gemstone Infusion", 0, 0, 1).ability()
                .powder(TreePowder.GLACITE).value("statDuration", coreValue(20, 25))
                .lore("", "§6Pickaxe Ability: Gemstone Infusion", "§7Increases the effectiveness of", "§7§6every Gemstone §7in your pick's", "§7Gemstone Slots by §a100% §7for §a{statDuration}s§7.", "§8Cooldown: §a140s").build();
    }

    private static TreeNodeDefinition crystalline() {
        return node("crystalline", "Crystalline", 1, 0, 50)
                .powder(TreePowder.GLACITE).cost(3.3).value("stat", level(0.5))
                .lore("§7Increases your chances of finding a", "§7§bGlacite Mineshaft§7 containing a", "§7§dGemstone Crystal§7 by §a§a{stat}%§7.").build();
    }

    private static TreeNodeDefinition giftsFromTheDeparted() {
        return node("gifts_from_the_departed", "Gifts from the Departed", 2, 0, 100)
                .powder(TreePowder.GLACITE).cost(2.45).value("stat", level(0.2))
                .lore("§7Gain a §a§a{stat}% §7chance to get an extra", "§7item when looting a §bFrozen Corpse§7.").build();
    }

    private static TreeNodeDefinition miningMaster() {
        return node("mining_master", "Mining Master", 3, 0, 10)
                .powder(TreePowder.GLACITE).cost(5).value("stat", level(0.1))
                .lore("§7Grants §5+{stat}✧ Pristine§7.").build();
    }

    private static TreeNodeDefinition deadMansChest() {
        return node("dead_mans_chest", "Dead Man's Chest", 4, 0, 50)
                .powder(TreePowder.GLACITE).cost(3.2).value("stat", level(0.5))
                .lore("§7Gain a §a§a{stat}% §7chance to spawn §a1", "§a§7additional §bFrozen Corpse §7when you", "§7enter a §bGlacite Mineshaft§7.").build();
    }

    private static TreeNodeDefinition vanguardSeeker() {
        return node("vanguard_seeker", "Vanguard Seeker", 5, 0, 50)
                .powder(TreePowder.GLACITE).cost(3.1).value("stat", level(1))
                .lore("§7Increases your chances of finding a", "§7§bGlacite Mineshaft§7 containing a", "§7§fVanguard Corpse§7 by §a§a{stat}%§7.").build();
    }

    private static TreeNodeDefinition sheerForce() {
        return node("sheer_force", "Sheer Force", 6, 0, 1).ability()
                .powder(TreePowder.GLACITE)
                .lore("", "§6Pickaxe Ability: Sheer Force", "§7Grants §e+200▚ Mining Spread§7 for §a§a25s§7.", "§8Cooldown: §a120s").build();
    }

    private static TreeNodeDefinition.Builder node(String id, String name, int x, int y, int maxLevel) {
        return TreeNodeDefinition.builder(id, name, x, y, maxLevel);
    }

    private static NodeValue level(double multiplier) {
        return context -> number(context.level() * multiplier);
    }

    private static NodeValue coreValue(String beforeCoreTwo, String afterCoreTwo) {
        return context -> context.coreLevel() < 2 ? beforeCoreTwo : afterCoreTwo;
    }

    private static NodeValue coreValue(double beforeCoreTwo, double afterCoreTwo) {
        return context -> number(context.coreLevel() < 2 ? beforeCoreTwo : afterCoreTwo);
    }

    private static String number(double value) {
        double rounded = Math.round(value * 100.0) / 100.0;
        if (rounded == Math.rint(rounded)) return String.format(Locale.ROOT, "%.0f", rounded);
        return String.format(Locale.ROOT, "%.2f", rounded).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    private static List<String> coreLore(NodeRenderContext context) {
        int level = context.level();
        List<String> lines = new ArrayList<>();
        if (level > 0) lines.add("§7§8+§c1 Pickaxe Ability Level");
        if (level > 1) lines.add("§7§8+§a1 Forge Slot");
        if (level > 2) lines.add("§7§8+§a1 Commission Slot");
        if (level > 3) lines.add("§7§8+§21 Base Mithril Powder §7when mining §2Mithril§7.");
        if (level > 4) lines.add("§8+§51 Token of the Mountain");
        if (level > 5) lines.add("§7§8+§d2 Base Gemstone Powder §7when mining §dGemstones§7.");
        if (level > 6) lines.add("§8+§51 Token of the Mountain");
        if (level > 7) lines.add("§7§8+§b3 Base Glacite Powder §7when mining §bGlacite§7.");
        if (level > 8) lines.add("§7§8+§a10% chance §7for §bGlacite Mineshafts §7to spawn.");
        if (level > 9) lines.add("§8+§52 Token of the Mountain");
        return lines;
    }

    private static List<TreeTierDefinition> tiers() {
        return List.of(
                new TreeTierDefinition(1, 0, 1, 0, 35, List.of("+1 Token of the Mountain", "+35 SkyBlock XP")),
                new TreeTierDefinition(2, 3_000, 2, 0, 45, List.of("+2 Token of the Mountain", "Access to the Forge", "+45 SkyBlock XP")),
                new TreeTierDefinition(3, 12_000, 2, 1, 60, List.of("+2 Token of the Mountain", "+1 Forge Slot", "+60 SkyBlock XP")),
                new TreeTierDefinition(4, 37_000, 2, 1, 75, List.of("+2 Token of the Mountain", "+1 Forge Slot", "+75 SkyBlock XP")),
                new TreeTierDefinition(5, 97_000, 2, 1, 90, List.of("+2 Token of the Mountain", "+1 Forge Slot", "Mining Helix Emblem", "+90 SkyBlock XP")),
                new TreeTierDefinition(6, 197_000, 2, 1, 110, List.of("+2 Token of the Mountain", "+1 Forge Slot", "New Forgeable Items", "+110 SkyBlock XP")),
                new TreeTierDefinition(7, 347_000, 3, 1, 130, List.of("+3 Token of the Mountain", "+1 Forge Slot", "+130 SkyBlock XP")),
                new TreeTierDefinition(8, 557_000, 2, 0, 180, List.of("+2 Token of the Mountain", "New Forgeable Items", "+180 SkyBlock XP")),
                new TreeTierDefinition(9, 847_000, 2, 0, 210, List.of("+2 Token of the Mountain", "+210 SkyBlock XP")),
                new TreeTierDefinition(10, 1_247_000, 2, 0, 240, List.of("+2 Token of the Mountain", "+240 SkyBlock XP"))
        );
    }
}
