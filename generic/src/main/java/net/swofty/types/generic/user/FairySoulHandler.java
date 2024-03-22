package net.swofty.types.generic.user;

import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointIntegerList;
import net.swofty.types.generic.data.mongodb.FairySoulDatabase;
import net.swofty.types.generic.user.fairysouls.FairySoul;
import net.swofty.types.generic.user.fairysouls.FairySoulZone;

public record FairySoulHandler(SkyBlockPlayer player) {

    public int getFound(FairySoulZone zone) {
        int amount = 0;
        for (Integer id : player.getDataHandler().get(DataHandler.Data.FAIRY_SOULS, DatapointIntegerList.class).getValue()) {
            if (FairySoulDatabase.getAllSouls().get(id).getZone() == zone) {
                amount++;
            }
        }
        return amount;
    }

    public int getMax(FairySoulZone zone) {
        int amount = 0;
        for (FairySoul soul : FairySoulDatabase.getAllSouls()) {
            if (soul.getZone() == zone) {
                amount++;
            }
        }
        return amount;
    }

    public int getTotalFoundFairySouls() {
        return player.getDataHandler().get(DataHandler.Data.FAIRY_SOULS, DatapointIntegerList.class).getValue().size();
    }
}
