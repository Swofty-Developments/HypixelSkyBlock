package net.swofty.types.generic.user;

import net.swofty.types.generic.data.mongodb.FairySoulDatabase;
import net.swofty.types.generic.user.fairysouls.FairySoul;
import net.swofty.types.generic.user.fairysouls.FairySoulZone;

public record FairySoulHandler(SkyBlockPlayer player) {

    public int getFound(FairySoulZone zone) {
        int amount = 0;
        for (Integer id : player.getFairySouls().getAllFairySouls()) {
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
        return player.getFairySouls().getAllFairySouls().size();
    }
}
