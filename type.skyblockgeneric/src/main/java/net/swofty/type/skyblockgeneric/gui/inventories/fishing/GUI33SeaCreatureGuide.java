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

// make these actually paginated views
public class GUI33SeaCreatureGuide extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("(3/3) Sea Creature Guide", InventoryType.CHEST_6_ROW);
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
            "§7[Lvl §k?] §k????? (§9§lRare§7)",
            "38957d5023c937c4c41aa2412d43410bda23cf79a9f6ab36b76fef2d7c429",
            1,
            "§c♆ Magmatic§7, §a⚂ Cubic",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 31",
            "§7- §cLava",
            "§7- §cCrimson Isle"
        ));
        layout.slot(11, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§9§lRare§7)",
            "b78ef2e4cf2c41a2d14bfde9caff10219f5b1bf5b35a49eb51c6467882cb5f0",
            1,
            "§c♆ Magmatic§7, §4♨ Infernal",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 33",
            "§7- §cLava",
            "§7- §cCrimson Isle"
        ));
        layout.slot(12, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§9§lRare§7)",
            "c63704a7fc7d437f7b923e23e9a08ae3bbe28937df4dafa9e3e8725b2ce4afa5",
            1,
            "§c♆ Magmatic§7, §a☮ Animal",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 34",
            "§7- §cLava",
            "§7- §cCrimson Isle"
        ));
        layout.slot(13, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§5§lEpic§7)",
            "1642a06cd75ef307c1913ba7a224fb2082d8a2c5254fd1bf006125a087a9a868",
            1,
            "§c♆ Magmatic§7, §e✰ Humanoid§7, §a☮ Animal",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 35",
            "§7- §cLava",
            "§7- §cCrimson Isle"
        ));
        layout.slot(14, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§6§lLegendary§7)",
            "b2b6074d0c9d6b89a494cf4f74158282a64ee23ba8a0725633ad70932ada1a8f",
            1,
            "§9⚓ Aquatic§7, §a☮ Animal",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 35",
            "§7- §bWater",
            "§7- §dFishing Hotspot",
            "",
            "§cStats",
            "§7- Kills: §b1"
        ));
        layout.slot(15, ItemStackCreator.getStack(
            "§7[Lvl §k?] §k????? (§d§lMythic§7)",
            Material.DRAGON_EGG,
            1,
            "§f☃ Frozen§7, §e⛨ Shielded§7, §d❃ Elusive",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 35",
            "§7- §bWater",
            "§7- §cJerry's Workshop"
        ));
        layout.slot(16, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§d§lMythic§7)",
            "32581d564f01d712255125e1f101e534217f76e3599dab7f4ae0ffe328f729eb",
            1,
            "§c♆ Magmatic§7, §d❃ Elusive§7, §a☮ Animal",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 36",
            "§7- §cLava",
            "§7- §cCrimson Isle"
        ));
        layout.slot(19, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§d§lMythic§7)",
            "a3f925d274ec65e002028c898e11aa9b76d6d67aa305ad9c7c69fe61cec5e664",
            1,
            "§c♆ Magmatic§7, §5♃ Arcane§7, §d❃ Elusive",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 36",
            "§7- §cLava",
            "§7- §cCrimson Isle",
            "§7- §cNovice Trophy Fisher"
        ));
        layout.slot(20, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§6§lLegendary§7)",
            "55b194025806687642e2bc239895d646a6d8c193d9253b61bfce908f6ce1b84a",
            1,
            "§c♆ Magmatic§7, §a☮ Animal",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 37",
            "§7- §cLava",
            "§7- §dFishing Hotspot",
            "§7- §cCrimson Isle"
        ));
        layout.slot(21, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§d§lMythic§7)",
            "f3c802e580bfefc18c4af94cceb82968b5b4aeab0d832346a633a7473a41dfac",
            1,
            "§9⚓ Aquatic§7, ⚙ Construct, §d❃ Elusive",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 40",
            "§7- §bWater",
            "§7- §dFishing Hotspot"
        ));
        layout.slot(22, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§d§lMythic§7)",
            "22bcaceeb4162f400d44743315932ac820d3119ac8986a0161a726161ccc93fc",
            1,
            "§c♆ Magmatic§7, ⚙ Construct, §d❃ Elusive",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 45",
            "§7- §cLava",
            "§7- §cCrimson Isle",
            "§7- §cAdept Trophy Fisher"
        ));
        layout.slot(23, ItemStackCreator.getStackHead(
            "§7[Lvl §k?] §k????? (§d§lMythic§7)",
            "a8e1fe214b71f6ea69c541a861c64bafda7bf9b85de5dd17ab2b6ccd1d32b039",
            1,
            "§c♆ Magmatic§7, §f🦴 Skeletal§7, §d❃ Elusive",
            "",
            "§cDrops:",
            "§7- §k???????",
            "",
            "§cRequirements:",
            "§7- §aFishing Skill 47",
            "§7- §cLava",
            "§7- §dFishing Hotspot",
            "§7- §cCrimson Isle"
        ));
        layout.slot(45, ItemStackCreator.getStack(
            "§aPrevious Page",
            Material.ARROW,
            1,
            "§ePage 2"
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
    }
}
