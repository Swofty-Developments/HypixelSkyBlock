package net.swofty.types.generic.item.items.travelscroll;

import net.swofty.types.generic.item.impl.TravelScrollItem;
import net.swofty.types.generic.warps.TravelScrollType;

public class HubMuseumTravelScroll implements TravelScrollItem {
    @Override
    public TravelScrollType getTravelScrollType() {
        return TravelScrollType.HUB_MUSEUM;
    }
}
