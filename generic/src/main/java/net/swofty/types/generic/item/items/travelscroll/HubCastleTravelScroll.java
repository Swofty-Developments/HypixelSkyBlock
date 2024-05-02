package net.swofty.types.generic.item.items.travelscroll;

import net.swofty.types.generic.item.impl.TravelScrollItem;
import net.swofty.types.generic.warps.TravelScrollType;

public class HubCastleTravelScroll implements TravelScrollItem {
    @Override
    public TravelScrollType getTravelScrollType() {
        return TravelScrollType.HUB_CASTLE;
    }
}
