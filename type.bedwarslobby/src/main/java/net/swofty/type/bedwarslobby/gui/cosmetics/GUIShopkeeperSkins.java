package net.swofty.type.bedwarslobby.gui.cosmetics;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

// TODO: dynamic, paginatedview
public class GUIShopkeeperSkins extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Shopkeeper Skins ", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.slot(10, ItemStackCreator.getStack(
            "§aBlacksmith",
            Material.VILLAGER_SPAWN_EGG,
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Blacksmith as your",
            "§7Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(11, ItemStackCreator.getStack(
            "§aRandom Shopkeeper Skin",
            Material.CHEST,
            1,
            "§7Use a random Shopkeeper Skin!",
            "",
            "§eClick to select!"
        ));
        layout.slot(12, ItemStackCreator.getStack(
            "§aRandom Favorite Shopkeeper Skin",
            Material.ENDER_CHEST,
            1,
            "§7Use a Random §6✯ Favorite §7Shopkeeper Skin!",
            "",
            "§eClick to select!"
        ));
        layout.slot(13, ItemStackCreator.getStack(
            "§aSkeleton",
            Material.SKELETON_SPAWN_EGG,
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Skeleton as your Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(14, ItemStackCreator.getStack(
            "§aZombie Pigman",
            Material.POLAR_BEAR_SPAWN_EGG,
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Zombie Pigman as your",
            "§7Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(15, ItemStackCreator.getStack(
            "§aZombie",
            Material.ZOMBIE_SPAWN_EGG,
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Zombie as your Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(16, ItemStackCreator.getStack(
            "§aVillager Zombie",
            Material.ZOMBIE_SPAWN_EGG,
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Villager Zombie as your",
            "§7Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(19, ItemStackCreator.getStack(
            "§aWither Skeleton",
            Material.ENDERMAN_SPAWN_EGG,
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Wither Skeleton as your",
            "§7Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(20, ItemStackCreator.getStack(
            "§aBlaze",
            Material.BLAZE_SPAWN_EGG,
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Blaze as your Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(21, ItemStackCreator.getStackHead(
            "§aBed Salesman",
            "574caa78ce6a461416407f41d9f5726fb7bd2b8a257c7472d26bededeaffd9c",
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Bed Salesman as your",
            "§7Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(22, ItemStackCreator.getStackHead(
            "§aHoliday Bartender",
            "b8337eb4847335f3996d7e3b8db93a4c18bc9e50989d11d5f53c77a024d6f",
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Holiday Bartender as your",
            "§7Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(23, ItemStackCreator.getStackHead(
            "§aMagic Vendor",
            "23387866973f6bdc99a8de99851fe20bbf6c86e7de6ccd3e442959d75d65f1e",
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Magic Vendor as your",
            "§7Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(24, ItemStackCreator.getStackHead(
            "§aChen",
            "d373dedca39648da8ed76ebc7fa64c40cf63e360eecdc63162922e73b1f5396",
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Chen as your Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(25, ItemStackCreator.getStackHead(
            "§aLi",
            "af1b285b992e276d7fcec6d001d4141ac7d96c221968cc2c4103268974e96c1",
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Li as your Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(28, ItemStackCreator.getStackHead(
            "§aXiu",
            "3a1b85fb4caf1f46a65c470304780d9b6de997ec923d81c5389559a2c2b55",
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Xiu as your Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(29, ItemStackCreator.getStackHead(
            "§aZhao",
            "2d9bc877f65f86c3a7c53152517f4ee3c2a1ccafaec74ba2b855aaca4b90dacd",
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Zhao as your Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(30, ItemStackCreator.getStackHead(
            "§aStellar",
            "4513ec7ca98d328a1c9f17797b56f841b841c2c186b2c36ced4e94c53394c078",
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Stellar as your Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(31, ItemStackCreator.getStack(
            "§aCreeper",
            Material.CREEPER_SPAWN_EGG,
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Creeper as your Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(32, ItemStackCreator.getStack(
            "§aWitch",
            Material.WITCH_SPAWN_EGG,
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Witch as your Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(33, ItemStackCreator.getStackHead(
            "§aBed Researcher",
            "1bc76f1654f4a82fb11e6f18f44cb4dc8c7137f027cbd723a5c5a797b074e1",
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select Bed Researcher as your",
            "§7Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        layout.slot(34, ItemStackCreator.getStackHead(
            "§aKing Of Beds",
            "eb74285793dd26c4a79e79e878581f0181e1aaee411954ceecdb15a93138",
            1,
            "§8Shopkeeper Skin",
            "",
            "§7Select King Of Beds as your",
            "§7Shopkeeper!",
            "",
            "§eRight-Click to preview!",
            "§eClick to select!",
            "§eShift-click to toggle favorite!"
        ));
        Components.back(layout, 48, ctx);
        layout.slot(49, ItemStackCreator.getStack(
            "§7Total Tokens: §21,924,622",
            Material.EMERALD,
            1,
            "§6https://store.hypixel.net"
        ));
        layout.slot(50, ItemStackCreator.getStack(
            "§6Sorted by: §aLowest rarity first",
            Material.HOPPER,
            1,
            "§7Sorts by rarity: Lowest rarity first",
            "",
            "§7Next sort: §aHighest rarity first",
            "§eLeft click to use!",
            "",
            "§7Owned items first: §aYes",
            "§eRight click to toggle!"
        ));
        layout.slot(53, ItemStackCreator.getStack(
            "§eLeft-click for next page!",
            Material.ARROW,
            1,
            "§bRight-click for last page!"
        ));
    }
}
