package net.swofty.type.island.tab;

import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointMinionData;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.MinionHandler;
import net.swofty.types.generic.tab.TablistModule;
import net.swofty.types.generic.tab.TablistSkinRegistry;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public class IslandServerModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(SkyBlockPlayer player) {
        List<IslandMinionData.IslandMinion> minions = player.getSkyBlockIsland().getMinionData().getMinions();
        DatapointMinionData.ProfileMinionData data = player.getDataHandler().get(
                DataHandler.Data.MINION_DATA,
                DatapointMinionData.class
        ).getValue();

        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                new TablistEntry(getCentered("§3§lServer Info"), TablistSkinRegistry.CYAN)
        ));

        entries.add(new TablistEntry("§b§lArea: §7Private Island", TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(" Server: §8mini" + SkyBlockConst.getServerName(), TablistSkinRegistry.GRAY));
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

