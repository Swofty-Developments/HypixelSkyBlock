package net.swofty.types.generic.item.items.travelscroll;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.TravelScrollItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.warps.TravelScrollType;

import java.util.ArrayList;
import java.util.Arrays;

public class HubCastleTravelScroll implements TravelScrollItem {
    @Override
    public TravelScrollType getTravelScrollType() {
        return TravelScrollType.HUB_CASTLE;
    }
}
