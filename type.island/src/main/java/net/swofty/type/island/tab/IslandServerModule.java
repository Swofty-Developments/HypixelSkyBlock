package net.swofty.type.island.tab;

import net.swofty.commons.StringUtility;
import net.swofty.type.generic.SkyBlockConst;
import net.swofty.type.generic.data.datapoints.DatapointMinionData;
import net.swofty.type.generic.minion.IslandMinionData;
import net.swofty.type.generic.minion.MinionHandler;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.tab.TablistSkinRegistry;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;

public class IslandServerModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(HypixelPlayer player) {
        List<IslandMinionData.IslandMinion> minions = player.getSkyBlockIsland().getMinionData().getMinions();
        DatapointMinionData.ProfileMinionData data = player.getSkyBlockData().get(
                SkyBlockDataHandler.Data.MINION_DATA,
                DatapointMinionData.class
        ).getValue();

        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered("§3§lServer Info"), TablistSkinRegistry.CYAN)
        ));

        entries.add(new TablistEntry("§b§lArea: §7Private Island", TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(" Server: §8" + SkyBlockConst.getServerName(), TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(" Minions: §9" + minions.size() + "§7/§9" + data.getSlots(), TablistSkinRegistry.GRAY));

        entries.add(getGrayEntry());
        entries.add(new TablistEntry("§b§lMinions: §f(" + minions.size() + ")", TablistSkinRegistry.GRAY));

        minions.forEach(minion -> {
            String content = " " + minion.getMinion().getDisplay().replace(" Minion", "");

            // Add tier
            content = content + " " + StringUtility.getAsRomanNumeral(minion.getTier());

            MinionHandler.InternalMinionTags.State minionState = minion.getInternalMinionTags().getState();

            switch (minionState) {
                case BAD_FULL -> content = content + " §7[§cFULL§7]";
                case BAD_LOCATION -> content = content + " §7[§cBLOCKED§7]";
                default -> content = content + " §7[§aACTIVE§7]";
            }

            entries.add(new TablistEntry(content, TablistSkinRegistry.GRAY));
        });

        fillRestWithGray(entries);

        return entries;
    }
}

