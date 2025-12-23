package net.swofty.type.dwarvenmines.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUIGuide extends HypixelInventoryGUI {

    public GUIGuide() {
        super("Guide", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(new GUIItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aMithril §8and §9᠅ Powder",
                        Material.PRISMARINE_CRYSTALS,
                        1,
                        "§8 ■ §7All of the veins on this island are",
                        "    §7mineable.",
                        "§8 ■ §7The center of §fMithril §7veins are more",
                        "    §7pure and thus drop more Mithril,",
                        "    §7while the outer edges of the vein",
                        "    §7drop the least!",
                        "§8 ■ §7Mining Mithril is the main source of",
                        "    §7gaining §2Mithril Powder§7, which is useful",
                        "    §7in the §5Heart of the Mountain§7."
                );
            }
        });
        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Commissions",
                        Material.WRITABLE_BOOK,
                        1,
                        "§8 ■ §7The §6§lKing §7can be found in the §bRoyal",
                        "    §bPalace§7. He's pretty famous around",
                        "    §7here.",
                        "§8 ■ §7If you do §9Commissions §7for the King",
                        "    §7you will be rewarded with §5HOTM Exp§7!",
                        "§8 ■ §7You can always see your active",
                        "    §7Commissions in the §atab list§7.",
                        "§8 ■ §7When you are done with a Commission,",
                        "    §7talk to the King to get new ones!",
                        "§8 ■ §7Your first §a4 §7Commissions of the day",
                        "    §7reward a lot more HOTM Exp!"
                );
            }
        });
        set(new GUIItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStackHead(
                        "§5Heart of the Mountain",
                        "86f06eaa3004aeed09b3d5b45d976de584e691c0e9cade133635de93d23b9edb",
                        1,
                        "§8 ■ §7You can level up your §5Heart of the",
                        "    §5Mountain §7by doing §9Commissions§7.",
                        "§8 ■ §7Spend your §5Token of the Mountain",
                        "    §7wisely to unlock §aPerks §7and §aPickaxe",
                        "    §aAbilities§7!",
                        "§8 ■ §7Most of these Perks can be",
                        "    §7upgraded using §9Powder§7! Some are",
                        "    §7only one-time unlocks.",
                        "§8 ■ §7If you are not happy with your",
                        "    §7setup, you can reset your HOTM for",
                        "    §7a price."
                );
            }
        });
        set(new GUIItem(28) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aMining",
                        Material.DIAMOND_PICKAXE,
                        1,
                        "§8 ■ §7Your §6⸕ Mining Speed §7stat increases",
                        "    §7the speed at which you can mine.",
                        "    §7Increase your §6⸕ Mining Speed §7by",
                        "    §7using better pickaxes or §aDrills§7.",
                        "§8 ■ §7Certain ores are tougher, requiring",
                        "    §7pickaxes with a higher §aBreaking",
                        "    §aPower §7to mine!",
                        "§8 ■ §7Talk to §5Bubu §7at the nearby shop to",
                        "    §7buy your first §aMithril Pickaxe§7!",
                        "§8 ■ §7If you want better pickaxes with",
                        "    §7higher §6⸕ Mining Speed§7, you will need",
                        "    §7to unlock the §6Forge §7down the road."
                );
            }
        });
        set(new GUIItem(30) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6The Forge",
                        Material.FURNACE,
                        1,
                        "§8 ■ §7The §6Forge §7allows you to §asmelt §7and",
                        "    §acast §7higher quality items.",
                        "§8 ■ §7You start with §a2 §7Forge slots and can",
                        "    §7currently unlock up to §a7§7!",
                        "§8 ■ §7Forging takes time. You'll need to",
                        "    §7plan things out to get the most",
                        "    §7efficiency out of it.",
                        "§8 ■ §7You can unlock new Forging options",
                        "    §7- including more slots and faster",
                        "    §7speeds - by leveling up your §5HOTM."
                );
            }
        });
        set(new GUIItem(32) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§bDwarven Rail Co.",
                        Material.MINECART,
                        1,
                        "§8 ■ §7The brand new fast travel system",
                        "    §7that spans the whole of the §6Dwarven",
                        "    §6Mines§7.",
                        "§8 ■ §7Many §bTicket Masters §7now dot the",
                        "    §7cave and will offer to take you to",
                        "    §7different §astations §7in return for",
                        "    §6coins§7.",
                        "§8 ■ §7The cost of a §2journey §7will vary",
                        "    §7depending on how far away you are",
                        "    §7from the §astation§7.",
                        "§8 ■ §7Once you have paid a §8Minecart §7will",
                        "    §7appear on the tracks to take you to",
                        "    §7your destination's §astation§7.",
                        "§8 ■ §7But be careful, the §6Dwarves §7run a",
                        "    §7tight schedule and if you miss your",
                        "    §8Minecart §7it will leave without you. Oh",
                        "    §7and §cno refunds§7!"
                );
            }
        });
        set(new GUIItem(34) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStackHead(
                        "§5Crystal Hollows",
                        "d159b03243be18a14f3eae763c4565c78f1f339a8742d26fde541be59b7de07",
                        1,
                        "§8 ■ §7The entrance to the §5Crystal Hollows",
                        "    §7is a newly excavated area at the",
                        "    §7back of the §bDwarven Village",
                        "    §7accessible only via §8Minecart§7.",
                        "§8 ■ §7Speak to §5Gwendolyn §7next to the",
                        "    §7entrance to gain access to the cave",
                        "    §7system!",
                        "§8 ■ §dGemstones §7are a precious resource",
                        "    §7in the §5Crystal Hollows §7so be sure to",
                        "    §7be on the lookout for them whilst",
                        "    §7visiting to §bupgrade §7your gear at",
                        "    §aGeo's Shop§7!",
                        "§8 ■ §7Make sure to take lots of §6Torches",
                        "    §7and watch out for §9cave-ins §7- they",
                        "    §7are incredibly §cdangerous §7and tend",
                        "    §7to happen every few hours."
                );
            }
        });
        set(GUIClickableItem.getCloseItem(49));
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
