package net.swofty.types.generic.item.items.communitycenter;

import net.minestom.server.event.player.PlayerItemAnimationEvent;
import net.swofty.types.generic.gui.inventory.inventories.GUIBoosterCookie;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.List;

public class BoosterCookie implements CustomSkyBlockItem, Interactable, Enchanted, TrackedUniqueItem, DisableAnimationImpl ,  NotFinishedYet {

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(List.of(
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
                "§7▸ Toggle specific §dpotion effects",
                "§8‣ §7Link your items in chat using §e/show",
                "§8‣ §7Insta-sell your Material stash to the §6Bazaar"
        ));
    }

    @Override
    public void onRightInteract(SkyBlockPlayer player, SkyBlockItem item) {
        new GUIBoosterCookie().open(player);
    }

    @Override
    public List<PlayerItemAnimationEvent.ItemAnimationType> getDisabledAnimations() {
        return List.of(PlayerItemAnimationEvent.ItemAnimationType.EAT);
    }
}