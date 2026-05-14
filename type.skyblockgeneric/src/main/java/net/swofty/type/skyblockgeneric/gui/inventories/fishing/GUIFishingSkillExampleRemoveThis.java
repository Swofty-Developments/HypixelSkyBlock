package net.swofty.type.skyblockgeneric.gui.inventories.fishing;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public class GUIFishingSkillExampleRemoveThis extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Fishing Skill", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.close(layout, 49);

        layout.slot(0, ItemStackCreator.getStack(
            "§aFishing Skill",
            Material.FISHING_ROD,
            1,
            "§7Visit your local pond to fish and",
            "§7earn Fishing XP!",
            "",
            "§7Progress to Level XIX: §e33.1%",
            "§2§l§m         §f§l§m                 §e33,082§6/§e100k",
            "",
            "§eTreasure Hunter XVIII",
            "  §fGrants §a+1.8§f §6⛃ Treasure Chance§f.",
            "",
            "§8Increase your Fishing Level to",
            "§8unlock Perks, statistic bonuses, and",
            "§8more!"
        ));
        layout.slot(2, ItemStackCreator.getStack(
            "§aFishing Level VIII",
            Material.LIME_STAINED_GLASS_PANE,
            8,
            "§7Rewards:",
            "  §eTreasure Hunter VIII",
            "    §fGrants §a+§80.7➜§a0.8§f §6⛃ Treasure Chance§f.",
            "  §8+§a2 §c❤ Health",
            "  §8+§64,000 §7Coins",
            "  §8+§b5 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(3, ItemStackCreator.getStack(
            "§aFishing Level IX",
            Material.LIME_STAINED_GLASS_PANE,
            9,
            "§7Rewards:",
            "  §fScarecrow§3 Sea Creature",
            "  §9Tadgang§3 Sea Creature",
            "  §aSea Archer§3 Sea Creature",
            "  §eTreasure Hunter IX",
            "    §fGrants §a+§80.8➜§a0.9§f §6⛃ Treasure Chance§f.",
            "  §8+§a2 §c❤ Health",
            "  §8+§65,000 §7Coins",
            "  §8+§b5 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(4, ItemStackCreator.getStack(
            "§aFishing Level X",
            Material.PRISMARINE,
            10,
            "§7Rewards:",
            "  §9Banshee§3 Sea Creature",
            "  §aOasis Sheep§3 Sea Creature",
            "  §9Blue Shark§3 Sea Creature",
            "  §9Snapping Turtle§3 Sea Creature",
            "  §aOasis Rabbit§3 Sea Creature",
            "  §eTreasure Hunter X",
            "    §fGrants §a+§80.9➜§a1§f §6⛃ Treasure Chance§f.",
            "  §8+§a2 §c❤ Health",
            "  §8+§67,500 §7Coins",
            "  §8+§b5 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(6, ItemStackCreator.getStack(
            "§aFishing Level XVIII",
            Material.LIME_STAINED_GLASS_PANE,
            18,
            "§7Rewards:",
            "  §5Tiger Shark§3 Sea Creature",
            "  §5Deep Sea Protector§3 Sea Creature",
            "  §eTreasure Hunter XVIII",
            "    §fGrants §a+§81.7➜§a1.8§f §6⛃ Treasure Chance§f.",
            "  §8+§a3 §c❤ Health",
            "  §8+§665,000 §7Coins",
            "  §8+§b10 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(7, ItemStackCreator.getStack(
            "§eFishing Level XIX",
            Material.YELLOW_STAINED_GLASS_PANE,
            19,
            "§7Rewards:",
            "  §6???§3 Sea Creature",
            "  §9???§3 Sea Creature",
            "  §eTreasure Hunter XIX",
            "    §fGrants §a+§81.8➜§a1.9§f §6⛃ Treasure Chance§f.",
            "  §8+§a3 §c❤ Health",
            "  §8+§680,000 §7Coins",
            "  §8+§b10 SkyBlock XP",
            "",
            "§7Progress: §e33.1%",
            "§2§l§m         §f§l§m                 §e33,082§6/§e100k"
        ));
        layout.slot(8, ItemStackCreator.getStack(
            "§cFishing Level XX",
            Material.RED_STAINED_GLASS_PANE,
            20,
            "§7Rewards:",
            "  §6???§3 Sea Creature",
            "  §5???§3 Sea Creature",
            "  §6???§3 Sea Creature",
            "  §eTreasure Hunter XX",
            "    §fGrants §a+§81.9➜§a2§f §6⛃ Treasure Chance§f.",
            "  §8+§a4 §c❤ Health",
            "  §8+§6100,000 §7Coins",
            "  §8+§b10 SkyBlock XP"
        ));
        layout.slot(9, ItemStackCreator.getStack(
            "§aFishing Level I",
            Material.LIME_STAINED_GLASS_PANE,
            1,
            "§7Rewards:",
            "  §fSquid§3 Sea Creature",
            "  §fSea Walker§3 Sea Creature",
            "  §eTreasure Hunter I",
            "    §fGrants §a+0.1§f §6⛃ Treasure Chance§f.",
            "  §8+§a2 §c❤ Health",
            "  §8+§6100 §7Coins",
            "  §8+§b5 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(11, ItemStackCreator.getStack(
            "§aFishing Level VII",
            Material.LIME_STAINED_GLASS_PANE,
            7,
            "§7Rewards:",
            "  §aWetwing§3 Sea Creature",
            "  §aSea Witch§3 Sea Creature",
            "  §eTreasure Hunter VII",
            "    §fGrants §a+§80.6➜§a0.7§f §6⛃ Treasure Chance§f.",
            "  §8+§a2 §c❤ Health",
            "  §8+§63,000 §7Coins",
            "  §8+§b5 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(13, ItemStackCreator.getStack(
            "§aFishing Level XI",
            Material.LIME_STAINED_GLASS_PANE,
            11,
            "§7Rewards:",
            "  §aRider of the Deep§3 Sea Creature",
            "  §eTreasure Hunter XI",
            "    §fGrants §a+§81➜§a1.1§f §6⛃ Treasure Chance§f.",
            "  §8+§a2 §c❤ Health",
            "  §8+§610,000 §7Coins",
            "  §8+§b10 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(15, ItemStackCreator.getStack(
            "§aFishing Level XVII",
            Material.LIME_STAINED_GLASS_PANE,
            17,
            "§7Rewards:",
            "  §5Guardian Defender§3 Sea Creature",
            "  §5Werewolf§3 Sea Creature",
            "  §9Stridersurfer§3 Sea Creature",
            "  §9Poisoned Water Worm§3 Sea Creature",
            "  §eTreasure Hunter XVII",
            "    §fGrants §a+§81.6➜§a1.7§f §6⛃ Treasure Chance§f.",
            "  §8+§a3 §c❤ Health",
            "  §8+§650,000 §7Coins",
            "  §8+§b10 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(17, ItemStackCreator.getStack(
            "§cFishing Level XXI",
            Material.RED_STAINED_GLASS_PANE,
            21,
            "§7Rewards:",
            "  §6???§3 Sea Creature",
            "  §eTreasure Hunter XXI",
            "    §fGrants §a+§82➜§a2.1§f §6⛃ Treasure Chance§f.",
            "  §8+§a4 §c❤ Health",
            "  §8+§6125,000 §7Coins",
            "  §8+§b10 SkyBlock XP"
        ));
        layout.slot(18, ItemStackCreator.getStack(
            "§aFishing Level II",
            Material.LIME_STAINED_GLASS_PANE,
            2,
            "§7Rewards:",
            "  §eTreasure Hunter II",
            "    §fGrants §a+§80.1➜§a0.2§f §6⛃ Treasure Chance§f.",
            "  §8+§a2 §c❤ Health",
            "  §8+§6250 §7Coins",
            "  §8+§b5 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(20, ItemStackCreator.getStack(
            "§aFishing Level VI",
            Material.LIME_STAINED_GLASS_PANE,
            6,
            "§7Rewards:",
            "  §aMedium Mithril Grubber§3 Sea Creature",
            "  §aMithril Grubber§3 Sea Creature",
            "  §aBloated Mithril Grubber§3 Sea Creature",
            "  §aLarge Mithril Grubber§3 Sea Creature",
            "  §fFrosty§3 Sea Creature",
            "  §eTreasure Hunter VI",
            "    §fGrants §a+§80.5➜§a0.6§f §6⛃ Treasure Chance§f.",
            "  §8+§a2 §c❤ Health",
            "  §8+§62,000 §7Coins",
            "  §8+§b5 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(22, ItemStackCreator.getStack(
            "§aFishing Level XII",
            Material.LIME_STAINED_GLASS_PANE,
            12,
            "§7Rewards:",
            "  §5Ent§3 Sea Creature",
            "  §eTreasure Hunter XII",
            "    §fGrants §a+§81.1➜§a1.2§f §6⛃ Treasure Chance§f.",
            "  §8+§a2 §c❤ Health",
            "  §8+§615,000 §7Coins",
            "  §8+§b10 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(24, ItemStackCreator.getStack(
            "§aFishing Level XVI",
            Material.LIME_STAINED_GLASS_PANE,
            16,
            "§7Rewards:",
            "  §9Sea Leech§3 Sea Creature",
            "  §9Fireproof Witch§3 Sea Creature",
            "  §eTreasure Hunter XVI",
            "    §fGrants §a+§81.5➜§a1.6§f §6⛃ Treasure Chance§f.",
            "  §8+§a3 §c❤ Health",
            "  §8+§640,000 §7Coins",
            "  §8+§b10 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(26, ItemStackCreator.getStack(
            "§cFishing Level XXII",
            Material.RED_STAINED_GLASS_PANE,
            22,
            "§7Rewards:",
            "  §5???§3 Sea Creature",
            "  §eTreasure Hunter XXII",
            "    §fGrants §a+§82.1➜§a2.2§f §6⛃ Treasure Chance§f.",
            "  §8+§a4 §c❤ Health",
            "  §8+§6150,000 §7Coins",
            "  §8+§b10 SkyBlock XP"
        ));
        layout.slot(27, ItemStackCreator.getStack(
            "§aFishing Level III",
            Material.LIME_STAINED_GLASS_PANE,
            3,
            "§7Rewards:",
            "  §fNight Squid§3 Sea Creature",
            "  §eTreasure Hunter III",
            "    §fGrants §a+§80.2➜§a0.3§f §6⛃ Treasure Chance§f.",
            "  §8+§a2 §c❤ Health",
            "  §8+§6500 §7Coins",
            "  §8+§b5 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(28, ItemStackCreator.getStack(
            "§aFishing Level IV",
            Material.LIME_STAINED_GLASS_PANE,
            4,
            "§7Rewards:",
            "  §eTreasure Hunter IV",
            "    §fGrants §a+§80.3➜§a0.4§f §6⛃ Treasure Chance§f.",
            "  §8+§a2 §c❤ Health",
            "  §8+§6750 §7Coins",
            "  §8+§b5 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(29, ItemStackCreator.getStack(
            "§aFishing Level V",
            Material.PRISMARINE,
            5,
            "§7Rewards:",
            "  §fSea Guardian§3 Sea Creature",
            "  §aDumpster Diver§3 Sea Creature",
            "  §fFrog Man§3 Sea Creature",
            "  §fTrash Gobbler§3 Sea Creature",
            "  §fBogged§3 Sea Creature",
            "  §aNurse Shark§3 Sea Creature",
            "  §fFrozen Steve§3 Sea Creature",
            "  §eTreasure Hunter V",
            "    §fGrants §a+§80.4➜§a0.5§f §6⛃ Treasure Chance§f.",
            "  §8+§a2 §c❤ Health",
            "  §8+§61,000 §7Coins",
            "  §8+§b5 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(31, ItemStackCreator.getStack(
            "§aFishing Level XIII",
            Material.LIME_STAINED_GLASS_PANE,
            13,
            "§7Rewards:",
            "  §9Catfish§3 Sea Creature",
            "  §aGrinch§3 Sea Creature",
            "  §eTreasure Hunter XIII",
            "    §fGrants §a+§81.2➜§a1.3§f §6⛃ Treasure Chance§f.",
            "  §8+§a2 §c❤ Health",
            "  §8+§620,000 §7Coins",
            "  §8+§b10 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(32, ItemStackCreator.getStack(
            "§aFishing Level XIV",
            Material.LIME_STAINED_GLASS_PANE,
            14,
            "§7Rewards:",
            "  §fFried Chicken§3 Sea Creature",
            "  §9Carrot King§3 Sea Creature",
            "  §9Nightmare§3 Sea Creature",
            "  §eTreasure Hunter XIV",
            "    §fGrants §a+§81.3➜§a1.4§f §6⛃ Treasure Chance§f.",
            "  §8+§a2 §c❤ Health",
            "  §8+§625,000 §7Coins",
            "  §8+§b10 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(33, ItemStackCreator.getStack(
            "§aFishing Level XV",
            Material.PRISMARINE,
            15,
            "§7Rewards:",
            "  §9Agarimoo§3 Sea Creature",
            "  §9Water Worm§3 Sea Creature",
            "  §5Bayou Sludge§3 Sea Creature",
            "  §eTreasure Hunter XV",
            "    §fGrants §a+§81.4➜§a1.5§f §6⛃ Treasure Chance§f.",
            "  §8+§a3 §c❤ Health",
            "  §8+§630,000 §7Coins",
            "  §8+§b10 SkyBlock XP",
            "",
            "§a§lUNLOCKED"
        ));
        layout.slot(35, ItemStackCreator.getStack(
            "§cFishing Level XXIII",
            Material.RED_STAINED_GLASS_PANE,
            23,
            "§7Rewards:",
            "  §eTreasure Hunter XXIII",
            "    §fGrants §a+§82.2➜§a2.3§f §6⛃ Treasure Chance§f.",
            "  §8+§a4 §c❤ Health",
            "  §8+§6175,000 §7Coins",
            "  §8+§b10 SkyBlock XP"
        ));
        layout.slot(39, ItemStackCreator.getStack(
            "§6Bait Guide",
            Material.BOOK,
            1,
            "§7View §6Baits§7, their uses, and how to",
            "§7obtain them! Can also be accessed",
            "§7with §a/bait§7!",
            "",
            "§eClick to view!"
        ));
        layout.slot(40, ItemStackCreator.getStack(
            "§aSea Creature Guide",
            Material.BOOK,
            1,
            "§7Your guide to the creatures of the",
            "§7deep! Can also be accessed with",
            "§a/scg§7!",
            "",
            "§7Beware, Sea Creatures spawn much",
            "§7less often on your private island.",
            "",
            "§7Your Fishing: §aLevel XVIII",
            "",
            "§eClick to view!"
        ));
        layout.slot(41, ItemStackCreator.getStack(
            "§9Rod Part Guide",
            Material.BOOK,
            1,
            "§7View all of the §9Rod Parts §7that can be",
            "§7applied to §aFishing Rods§7! Can also be",
            "§7accessed with §a/rodparts§7!",
            "",
            "§eClick to view!"
        ));
        layout.slot(44, ItemStackCreator.getStack(
            "§cFishing Level XXIV",
            Material.RED_STAINED_GLASS_PANE,
            24,
            "§7Rewards:",
            "  §6???§3 Sea Creature",
            "  §6???§3 Sea Creature",
            "  §eTreasure Hunter XXIV",
            "    §fGrants §a+§82.3➜§a2.4§f §6⛃ Treasure Chance§f.",
            "  §8+§a4 §c❤ Health",
            "  §8+§6200,000 §7Coins",
            "  §8+§b10 SkyBlock XP"
        ));
        layout.slot(48, ItemStackCreator.getStack(
            "§aGo Back",
            Material.ARROW,
            1,
            "§7To Your Skills"
        ));
        layout.slot(53, ItemStackCreator.getStack(
            "§cFishing Level XXV",
            Material.RED_STAINED_GLASS_PANE,
            25,
            "§7Rewards:",
            "  §6???§3 Sea Creature",
            "  §eTreasure Hunter XXV",
            "    §fGrants §a+§82.4➜§a2.5§f §6⛃ Treasure Chance§f.",
            "  §8+§a4 §c❤ Health",
            "  §8+§6225,000 §7Coins",
            "  §8+§b10 SkyBlock XP"
        ));
    }
}
