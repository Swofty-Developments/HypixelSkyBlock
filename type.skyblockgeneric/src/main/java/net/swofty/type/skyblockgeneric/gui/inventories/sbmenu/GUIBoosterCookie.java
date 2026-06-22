package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.datapoints.DatapointInteger;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIBoosterCookie extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Booster Cookie", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 48, ctx);
        Components.close(layout, 49);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        SkyBlockDataHandler handler = player.getSkyblockDataHandler();
        int gems = handler.get(SkyBlockDataHandler.Data.GEMS, DatapointInteger.class).getValue();

        layout.slot(11, ItemStackCreator.getStack(
            "§bBits",
            Material.DIAMOND,
            1,
            "§7Bits are earned from booster",
            "§7cookies and spent in the community",
            "§7shop for unique items.",
            "",
            "§7Bits Purse: §b0",
            "",
            "§7Bits Available: §b0§3/0",
            "§8Eligible to farm while you have",
            "§8the cookie buff.",
            "",
            "§7Bits Multiplier: §b1x",
            "§7Per §6Cookie§7: §b+4,800 bits §7available",
            "",
            "§8Your Fame Rank's bit multiplier",
            "§8applies on the bits from every",
            "§8cookie you've ever eaten!"
        ));
        layout.slot(13, ItemStackCreator.getStack(
            "§6Booster Cookie",
            Material.COOKIE,
            1,
            "§7Acquire Booster Cookies from the",
            "§bCommunity Center §7in the §aHub§7.",
            "",
            "§dCookie Buff:",
            "§8‣ §7Ability to gain §bBits§7!",
            "§8‣ §3+25☯ §7on all §3Wisdom §7stats",
            "§8‣ §b+15✯ §7Magic Find",
            "§8‣ §7Keep §6coins §7on death",
            "§8‣ §ePermafly §7on private islands and gardens",
            "§8‣ §7Quick access to some menus using their",
            "§7respective commands:",
            "  §6/ah§7, §6/bazaar§7, §a/bank§7, §6/accessorybag§7,",
            "  §b/fishingbag§7, §d/timepocket§7, §f/anvil§7, §d/hex§7,",
            "  §b/etable§7, §d/potionbag§7, §d/rngmeter§7, §d/pity§7,",
            "  §7and §e/quiver",
            "§8‣ §7Sell items directly to the trades and cookie menu",
            "§8‣ §7AFK §aimmunity §7on your island and garden",
            "§8‣ §7Toggle specific §dpotion effects",
            "§8‣ §7Link your items in chat using §e/show",
            "§8‣ §7Insta-sell your Material stash to the §6Bazaar",
            "§8‣ §7Increases §6Chocolate Factory §7production by §6+0.25x",
            "§8‣ §7Allows consuming §9Mixins §7directly from your inventory",
            "§8‣ §7Call §3Abiphone Contacts §7with §b/call",
            "",
            "§cYou do not currently have a",
            "§cBooster Cookie active!",
            "",
            "§7Cost",
            "§a325 SkyBlock Gems",
            "",
            "§7You have: §a" + gems + " Gems",
            "",
            "§cYou cannot afford this!",
            "§eClick to get store info!"
        ));
        layout.slot(15, ItemStackCreator.getStack(
            "§eFame Rank",
            Material.GOLDEN_HELMET,
            1,
            "§7Earn fame by §6contributing to city",
            "§6projects §7and spending §bBits §7& §aGems",
            "§7with Elizabeth.",
            "",
            "§7Your rank: §eNew player",
            "§7Bits Multiplier: §b1x",
            "§7Election Votes: §a1",
            "§8The election is run every SkyBlock",
            "§8year.",
            "",
            "§7Your total: §e10,850 Fame",
            "",
            "§7Next rank: §bSettler",
            "§6§l§m           §f§l§m         §r §e10,850§6/§e20k",
            "",
            "§8You earn 1 fame per bit and",
            "§8200 per gem spent in the",
            "§8community shop."
        ));
        layout.slot(28, ItemStackCreator.getStackHead(
            "§dThe Hex",
            "3b11fb90db7f57beb435954013b1c7ef776c6bd96cbf3308aa8ebac29591ebbd",
            1,
            "§7Access §dThe Hex §7from anywhere in",
            "§7SkyBlock!",
            "",
            "§8Also accessible via /hex",
            "",
            "§cRequires Cookie Buff!"
        ));
        layout.slot(29, ItemStackCreator.getStack(
            "§6Enchantment Table",
            Material.ENCHANTING_TABLE,
            1,
            "§7Access an Enchantment Table from",
            "§7anywhere in SkyBlock!",
            "",
            "§7This portable table remembers the",
            "§7highest §dbookshelf power §7you've seen!",
            "",
            "§8Also accessible via /enchantingtable",
            "",
            "§cRequires Cookie Buff!"
        ));
        layout.slot(30, ItemStackCreator.getStack(
            "§6Anvil",
            Material.ANVIL,
            1,
            "§7Access an Anvil from anywhere in",
            "§7SkyBlock!",
            "",
            "§8Also accessible via /anvil",
            "",
            "§cRequires Cookie Buff!"
        ));
        layout.slot(32, ItemStackCreator.getStack(
            "§aToggle Potion Effects",
            Material.POTION,
            1,
            "§7Choose which of your potion effects",
            "§7are applied to you.",
            "",
            "§cRequires Cookie Buff!"
        ));
        layout.slot(33, ItemStackCreator.getStack(
            "§6Auction House",
            Material.GOLDEN_HORSE_ARMOR,
            1,
            "§7Access the Auction House menu from",
            "§7anywhere in SkyBlock!",
            "",
            "§8Also accessible via /auctions",
            "",
            "§cRequires Cookie Buff!"
        ));
        layout.slot(34, ItemStackCreator.getStackHead(
            "§6Bazaar",
            "c232e3820897429157619b0ee099fec0628f602fff12b695de54aef11d923ad7",
            1,
            "§7Access the Bazaar from anywhere in",
            "§7SkyBlock!",
            "",
            "§8Also accessible via /bazaar",
            "",
            "§cRequires Cookie Buff!"
        ));

        layout.slot(50, ItemStackCreator.getStack(
            "§aSkyBlock Gems",
            Material.EMERALD,
            1,
            "§7Use SkyBlock Gems to purchase:",
            "§7 - §6Booster Cookies",
            "§7 - §cFire Sales",
            "§7 - §bTaylor's Cosmetics",
            "§7 - §aSkyMart Barn & Greenhouse Skins",
            "§7 - §dAccount & Profile Upgrades",
            "",
            "§7You have: §a" + gems + " Gems",
            "§8Gems can be purchased from our",
            "§8webstore at §bstore.hypixel.net§8!",
            "",
            "§eClick to get link!"
        ));
    }
}
