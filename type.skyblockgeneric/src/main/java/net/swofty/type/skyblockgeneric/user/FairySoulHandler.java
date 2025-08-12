package net.swofty.type.skyblockgeneric.user;

import net.swofty.type.generic.data.monogdb.FairySoulDatabase;
import net.swofty.type.generic.user.fairysouls.FairySoul;
import net.swofty.type.generic.user.fairysouls.FairySoulZone;

public record FairySoulHandler(HypixelPlayer player) {

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
