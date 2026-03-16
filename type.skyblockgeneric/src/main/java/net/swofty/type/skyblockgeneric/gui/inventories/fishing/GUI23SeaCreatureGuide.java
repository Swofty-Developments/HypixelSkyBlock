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

// make these actually paginated views and data-driven
public class GUI23SeaCreatureGuide extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("(2/3) Sea Creature Guide", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.close(layout, 49);

        layout.slot(4, ItemStackCreator.getStack(
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
            "§7Your Fishing: §aLevel XVIII"
        ));
        layout.slot(10, ItemStackCreator.getStackHead(
            "§7[Lvl 24] Nightmare (§9§lRare§7)",
            "578211e1b4d99d1c7bfda4838e48fc884c3eae376f58d932bc2f78b0a919f8e7",
            1,
            "§9⚓ Aquatic§7, §6☽ Spooky§7, §2༕ Undead",
            "",
            "§cDrops:",
            "§7- Lily Pad",
            "§7- §aEnchanted Rotten Flesh",
            "§7- §aGreen Candy",
            "§7- §aLucky Hoof",
            "§7- §5Purple Candy",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 14",
            "§7- §bWater",
            "§7- §6Spooky Festival"
        ));
        layout.slot(11, ItemStackCreator.getStackHead(
            "§7[Lvl 35] Agarimoo (§9§lRare§7)",
            "3d597f77cde32c9ac9b06f82fcf7c9cb500facc14bff166222b24be39962f0ef",
            1,
            "§9⚓ Aquatic§7, §a☮ Animal",
            "",
            "§cDrops:",
            "§7- Red Mushroom",
            "§7- §9Agarimoo Tongue",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 15",
            "§7- §aChumcap Bucket",
            "§7- §bWater"
        ));
        layout.slot(12, ItemStackCreator.getStackHead(
            "§7[Lvl 20] Water Worm (§9§lRare§7)",
            "811a1173af3bead305e6339f555662e990d5faadb87e07299fa68ca828a6d2fb",
            1,
            "§9⚓ Aquatic§7, §6⛏ Subterranean",
            "",
            "§cDrops:",
            "§7- Hard Stone",
            "§7- ⸕ Rough Amber Gemstone",
            "§7- §aWorm Membrane",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 15",
            "§7- §bWater",
            "§7- §6Goblin Holdout"
        ));
        layout.slot(13, ItemStackCreator.getStackHead(
            "§7[Lvl 25] Bayou Sludge (§5§lEpic§7)",
            "895aeec6b842ada8669f846d65bc49762597824ab944f22f45bf3bbb941abe6c",
            1,
            "§9⚓ Aquatic§7, §a⚂ Cubic",
            "",
            "§cDrops:",
            "§7- Clay Ball",
            "§7- Enchanted Book",
            "§7   (Respite I)",
            "§7- Lily Pad",
            "§7- Slimeball",
            "§7- §9Enchanted Book",
            "§7   (Blessing VI)",
            "§7- §aEnchanted Slimeball",
            "§7- §aPoison Sample",
            "§7- §9Enchanted Slime Block",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 15",
            "§7- §bWater",
            "§7- §2Backwater Bayou",
            "",
            "§cStats",
            "§7- Kills: §b23"
        ));
        layout.slot(14, ItemStackCreator.getStackHead(
            "§7[Lvl 30] Sea Leech (§9§lRare§7)",
            "811a1173af3bead305e6339f555662e990d5faadb87e07299fa68ca828a6d2fb",
            1,
            "§9⚓ Aquatic§7, §a☮ Animal",
            "",
            "§cDrops:",
            "§7- Lily Pad",
            "§7- Raw Cod",
            "§7- Sponge",
            "§7- Tropical Fish",
            "§7- §9Enchanted Book",
            "§7   (Spiked Hook VI)",
            "§7- §9Fishing Exp Boost",
            "§7- §5Fishing Exp Boost",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 16",
            "§7- §bWater",
            "",
            "§cStats",
            "§7- Kills: §b15"
        ));
        layout.slot(15, ItemStackCreator.getStackHead(
            "§7[Lvl 75] Fireproof Witch (§9§lRare§7)",
            "fce6604157fc4ab5591e4bcf507a749918ee9c41e357d47376e0ee7342074c90",
            1,
            "§c♆ Magmatic§7, §e✰ Humanoid§7, §5♃ Arcane",
            "",
            "§cDrops:",
            "§7- §9Enchanted Book",
            "§7   (Fire Protection VI)",
            "§7- §aEnchanted Glowstone",
            "§7- §aEnchanted Glowstone Dust",
            "§7- §aSinged Powder",
            "§7- §9Magmafish",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 16",
            "§7- §cLava",
            "§7- §dFishing Hotspot",
            "§7- §cCrimson Isle"
        ));
        layout.slot(16, ItemStackCreator.getStackHead(
            "§7[Lvl 21] Stridersurfer (§9§lRare§7)",
            "620971f4dd71592a6065944487da2adf22987d5d0cccf6085dff7a6767d1b21a",
            1,
            "§c♆ Magmatic§7, §2༕ Undead§7, §2⸙ Woodland",
            "",
            "§cDrops:",
            "§7- §aGill Membrane",
            "§7- §9Sturdy Bone",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 17",
            "§7- §cLava",
            "§7- §2Galatea",
            "",
            "§cStats",
            "§7- Kills: §b8"
        ));
        layout.slot(19, ItemStackCreator.getStackHead(
            "§7[Lvl 25] Poisoned Water Worm (§9§lRare§7)",
            "18ae7046da98dcb33f3ed42f1dc41d08ac8dfa5db3a3860de5b1b5c056804187",
            1,
            "§9⚓ Aquatic§7, §6⛏ Subterranean§7, §4Ж Arthropod",
            "",
            "§cDrops:",
            "§7- Hard Stone",
            "§7- ⸕ Rough Amber Gemstone",
            "§7- §aWorm Membrane",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 17",
            "§7- §bWater",
            "§7- §6Goblin Holdout"
        ));
        layout.slot(20, ItemStackCreator.getStackHead(
            "§7[Lvl 45] Guardian Defender (§5§lEpic§7)",
            "221025434045bda7025b3e514b316a4b770c6faa4ba9adb4be3809526db77f9d",
            1,
            "§9⚓ Aquatic§7, §5♃ Arcane",
            "",
            "§cDrops:",
            "§7- Lily Pad",
            "§7- Raw Cod",
            "§7- Sponge",
            "§7- §9Enchanted Book",
            "§7   (Lure VI)",
            "§7- §aEnchanted Prismarine Crystals",
            "§7- §aEnchanted Prismarine Shard",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 17",
            "§7- §bWater",
            "",
            "§cStats",
            "§7- Kills: §b12"
        ));
        layout.slot(21, ItemStackCreator.getStackHead(
            "§7[Lvl 50] Werewolf (§5§lEpic§7)",
            "ce4606c6d973a999aec1687c7e075f7d37db8185e88b844507f16b3e2b3eb690",
            1,
            "§9⚓ Aquatic§7, §6☽ Spooky§7, §a☮ Animal",
            "",
            "§cDrops:",
            "§7- Lily Pad",
            "§7- §aGreen Candy",
            "§7- §9Werewolf Skin",
            "§7- §5Deep Sea Orb",
            "§7- §5Purple Candy",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 17",
            "§7- §bWater",
            "§7- §6Spooky Festival"
        ));
        layout.slot(22, ItemStackCreator.getStackHead(
            "§7[Lvl 50] Tiger Shark (§5§lEpic§7)",
            "ea575977e6bd0c7add94e2d8fdcc2af77e36c44d6b4c67788862a94000be6399",
            1,
            "§9⚓ Aquatic§7, §a☮ Animal",
            "",
            "§cDrops:",
            "§7- §aCarnival Ticket",
            "§7- §9Shark Fin",
            "§7- §5Megalodon Pet",
            "§7- §5Tiger Shark Tooth",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 18",
            "§7- §bWater",
            "§7- §bFishing Festival"
        ));
        layout.slot(23, ItemStackCreator.getStackHead(
            "§7[Lvl 60] Deep Sea Protector (§5§lEpic§7)",
            "22bcaceeb4162f400d44743315932ac820d3119ac8986a0161a726161ccc93fc",
            1,
            "§9⚓ Aquatic§7, ⚙ Construct",
            "",
            "§cDrops:",
            "§7- Lily Pad",
            "§7- Sponge",
            "§7- Tropical Fish",
            "§7- §9Enchanted Book",
            "§7   (Angler VI)",
            "§7- §aEnchanted Iron Ingot",
            "§7- §5Fishing Exp Boost",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 18",
            "§7- §bWater",
            "",
            "§cStats",
            "§7- Kills: §b7"
        ));
        layout.slot(24, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§9§lRare§7)",
            "811a1173af3bead305e6339f555662e990d5faadb87e07299fa68ca828a6d2fb",
            1,
            "§c♆ Magmatic§7, §4Ж Arthropod",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 19",
            "§7- §cLava",
            "§7- §bPrecursor Remnants"
        ));
        layout.slot(25, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§6§lLegendary§7)",
            "6d6bcd3bea0dff1f45d808e7a8550f95106af41b6d8d18a0793e19c9255ae845",
            1,
            "§9⚓ Aquatic§7, §e✰ Humanoid",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 19",
            "§7- §bWater",
            "",
            "§cStats",
            "§7- Kills: §b1"
        ));
        layout.slot(28, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§5§lEpic§7)",
            "b78ef2e4cf2c41a2d14bfde9caff10219f5b1bf5b35a49eb51c6467882cb5f0",
            1,
            "§c♆ Magmatic§7, §4♨ Infernal",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 20",
            "§7- §cLava",
            "§7- §cMagma Fields"
        ));
        layout.slot(29, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§6§lLegendary§7)",
            "5cf6abfbc778b1fac0d6db161e74438a1b468323b6f93fa4d650e42cd0f5802a",
            1,
            "§9⚓ Aquatic§7, §a☮ Animal",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 20",
            "§7- §bWater",
            "§7- §2Backwater Bayou",
            "",
            "§cStats",
            "§7- Kills: §b6"
        ));
        layout.slot(30, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§6§lLegendary§7)",
            "46c15b527e4872249c91797435521cb77651b567e57518304f5f131e49ded652",
            1,
            "§9⚓ Aquatic§7, §f🦴 Skeletal§7, §5♃ Arcane",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 20",
            "§7- §bWater",
            "§7- §2Galatea"
        ));
        layout.slot(31, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§6§lLegendary§7)",
            "6c9bc01f299f98d565a27ba10a1293915ae8beeefb8a67845e2331dbe6fd6fd6",
            1,
            "§9⚓ Aquatic§7, §6☽ Spooky§7, §5♃ Arcane",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 21",
            "§7- §bWater",
            "§7- §6Spooky Festival"
        ));
        layout.slot(32, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§5§lEpic§7)",
            "74e9c6e98582ffd8ff8feb3322cd1849c43fb16b158abb11ca7b42eda7743eb",
            1,
            "§c♆ Magmatic§7, §2༕ Undead",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 22",
            "§7- §cLava",
            "§7- §cMagma Fields"
        ));
        layout.slot(33, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§6§lLegendary§7)",
            "58be05cfae2c6a7d47da2ce88b3e00c72a145cc3218f041b3dd5bd5fa5ca827",
            1,
            "§9⚓ Aquatic§7, §2༕ Undead§7, §6⛏ Subterranean",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 24",
            "§7- §bWater",
            "§7- §2Mithril Deposits",
            "§7- §bPrecursor Remnants",
            "§7- §2Jungle"
        ));
        layout.slot(34, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§6§lLegendary§7)",
            "de2e5865429dd2520bbc703e4a9f2f1abd5e1cc5d31b8a9acbf74b7a97c937aa",
            1,
            "§9⚓ Aquatic§7, §a☮ Animal",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 24",
            "§7- §bWater",
            "§7- §bFishing Festival"
        ));
        layout.slot(37, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§6§lLegendary§7)",
            "4dd2d3c6d01c276226c7b0d377122e1a647b2ffb5b9b54fa98eac37bb1d09d3a",
            1,
            "§9⚓ Aquatic§7, §f☃ Frozen§7, §e✰ Humanoid",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 25",
            "§7- §bWater",
            "§7- §cJerry's Workshop",
            "",
            "§cStats",
            "§7- Kills: §b1"
        ));
        layout.slot(38, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§6§lLegendary§7)",
            "68a6194a5b217b9f5a3dfecce5f3efe6967405039b82fa0c4e8959175f32e75a",
            1,
            "§9⚓ Aquatic§7, §6☽ Spooky§7, §5♃ Arcane",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 26",
            "§7- §bWater",
            "§7- §6Spooky Festival"
        ));
        layout.slot(39, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§a§lUncommon§7)",
            "38957d5023c937c4c41aa2412d43410bda23cf79a9f6ab36b76fef2d7c429",
            1,
            "§c♆ Magmatic§7, §a⚂ Cubic",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 27",
            "§7- §cLava",
            "§7- §cCrimson Isle",
            "",
            "§cStats",
            "§7- Kills: §b1"
        ));
        layout.slot(40, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§a§lUncommon§7)",
            "47f1bc3fa91cd86cf4ba7745586d67207b58e7cf27bdf7a717780843785bf9b5",
            1,
            "§c♆ Magmatic§7, §a☮ Animal",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 28",
            "§7- §cLava",
            "§7- §cCrimson Isle"
        ));
        layout.slot(41, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§9§lRare§7)",
            "c2407e66c81b1443c2e7dfc4d6583eb19c622fa22f34fbf99fe6c45f76a",
            1,
            "§9⚓ Aquatic§7, §f☃ Frozen§7, §e✰ Humanoid",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 28",
            "§7- §bWater",
            "§7- §cJerry's Workshop",
            "",
            "§cStats",
            "§7- Kills: §b2"
        ));
        layout.slot(42, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§9§lRare§7)",
            "18ae7046da98dcb33f3ed42f1dc41d08ac8dfa5db3a3860de5b1b5c056804187",
            1,
            "§c♆ Magmatic§7, §4Ж Arthropod",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 30",
            "§7- §cLava",
            "§7- §cCrimson Isle"
        ));
        layout.slot(43, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§d§lMythic§7)",
            "645f2c0bbfe3b8b19b7452072db69a5f59da38ff61415545156e5701e1be756d",
            1,
            "§9⚓ Aquatic§7, §a☮ Animal§7, §d❃ Elusive",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 30",
            "§7- §bWater",
            "§7- §2Backwater Bayou"
        ));
        layout.slot(45, ItemStackCreator.getStack(
            "§aPrevious Page",
            Material.ARROW,
            1,
            "§ePage 1"
        ));
        layout.slot(48, ItemStackCreator.getStack(
            "§aGo Back",
            Material.ARROW,
            1,
            "§7To Fishing Skill"
        ));
        layout.slot(50, ItemStackCreator.getStack(
            "§aSort",
            Material.HOPPER,
            1,
            "",
            "§b▶ Fishing Level Req",
            "§7  Alphabetical",
            "§7  Mob Level",
            "§7  Killed Most",
            "§7  Ascending Rarity",
            "§7  Descending Rarity",
            "",
            "§bRight-click to go backwards!",
            "§eClick to switch!"
        ));
        layout.slot(51, ItemStackCreator.getStack(
            "§aFilter",
            Material.ENDER_EYE,
            1,
            "",
            "§f▶ All Sea Creatures",
            "§7  Has Level Requirement",
            "§7  Has Never Killed",
            "",
            "§bRight-click to go backwards!",
            "§eClick to switch!"
        ));
        layout.slot(52, ItemStackCreator.getStack(
            "§aCategory",
            Material.CAULDRON,
            1,
            "",
            "§a▶ Any Category",
            "§7  Water",
            "§7  Lava",
            "§7  Winter",
            "§7  Spooky",
            "§7  Shark",
            "§7  Oasis",
            "§7  Bayou",
            "§7  Hotspot",
            "§7  Galatea",
            "",
            "§bRight-click to go backwards!",
            "§eClick to switch!"
        ));
        layout.slot(53, ItemStackCreator.getStack(
            "§aNext Page",
            Material.ARROW,
            1,
            "§ePage 3"
        ));
    }
}
