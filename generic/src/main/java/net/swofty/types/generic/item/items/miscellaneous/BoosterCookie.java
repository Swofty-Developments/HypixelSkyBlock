package net.swofty.types.generic.item.items.miscellaneous;

import net.swofty.types.generic.gui.inventory.inventories.GUIBoosterCookie;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.Arrays;
import java.util.List;

public class BoosterCookie implements CustomSkyBlockItem, Interactable, Enchanted, Unstackable, NotFinishedYet {

    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public List<String> getAbsoluteLore(SkyBlockPlayer player, SkyBlockItem item) {
        return Arrays.asList(
                "§7Consume to gain the §dCookie",
                "§dBuff §7for §b4 §7days:",
                "§7▸ Ability to gain §bBits§7!",
                "§7▸ §3+25☯ §7on all §3Wisdom stats",
                "§7▸ §b+15✯ §7Magic Find",
                "§7▸ §7Keep §6coins §7on death",
                "§7▸ §ePermafly on private islands",
                "§7▸ §7Quick access to some menus using their respective commands:",
                "§6/ah§7, §6/bazaar§7, §a/bank§7, §f/anvil§7, §d/etable §7and §e/quiver",
                "§7▸ Sell items directly to the trades and cookie menu",
                "§7▸ AFK §aimmunity §7on your island",
                "§7▸ Toggle specific §dpotion effects"
        );
    }

    @Override
    public String getAbsoluteName(SkyBlockPlayer player, SkyBlockItem item) {
        return "§6Booster Cookie";
    }

    @Override
    public void onRightInteract(SkyBlockPlayer player, SkyBlockItem item) {
        Thread.startVirtualThread(() -> {
            new GUIBoosterCookie().open(player);
        });
    }
}