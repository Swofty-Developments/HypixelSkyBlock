package net.swofty.type.bedwarslobby;

import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record OrchestratorConnector(HypixelPlayer player) {
    private static final ProxyService PROXY_SERVICE = new ProxyService(ServiceType.ORCHESTRATOR);
    private static final List<UUID> PLAYERS_SEARCHING = new ArrayList<>();
}
