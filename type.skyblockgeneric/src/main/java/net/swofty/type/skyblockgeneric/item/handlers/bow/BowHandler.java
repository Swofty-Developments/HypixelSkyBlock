package net.swofty.type.skyblockgeneric.item.handlers.bow;

import lombok.Builder;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

@FunctionalInterface
public interface BowHandler {
    void onShoot(SkyBlockPlayer player, SkyBlockItem item, double power);
}
