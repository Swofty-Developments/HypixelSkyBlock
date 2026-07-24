package net.swofty.type.ravengardgeneric.data;

import net.swofty.commons.ServerType;
import net.swofty.type.generic.data.domain.DomainKey;
import net.swofty.type.generic.data.domain.PlayerDataDomain;
import net.swofty.type.generic.data.domain.PlayerDataService;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.UUID;

public final class RavengardDomain implements PlayerDataDomain<RavengardDataHandler> {
    public static final DomainKey<RavengardDataHandler> KEY = new DomainKey<>("ravengard", RavengardDataHandler.class);

    @Override
    public DomainKey<RavengardDataHandler> key() {
        return KEY;
    }

    @Override
    public boolean appliesTo(ServerType type) {
        return true;
    }

    @Override
    public int order() {
        return 30;
    }

    @Override
    public void load(UUID uuid) {
        if (PlayerDataService.isLoaded(KEY, uuid)) return;
        RavengardDataHandler handler = RavengardDataHandler.initUserWithDefaultData(uuid);
        handler.loadBackedData();
        PlayerDataService.store(KEY, uuid, handler);
    }

    @Override
    public void applyToPlayer(HypixelPlayer player) {
        PlayerDataService.get(KEY, player.getUuid()).runOnLoad(player);
    }

    @Override
    public void save(HypixelPlayer player) {
        RavengardDataHandler handler = PlayerDataService.find(KEY, player.getUuid()).orElse(null);
        if (handler == null) return;
        handler.runOnSave(player);
        handler.saveBackedData();
    }

    @Override
    public void unload(UUID uuid) {
        PlayerDataService.evict(KEY, uuid);
    }
}
