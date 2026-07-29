package net.swofty.proxyapi;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class ProxyPlayerSet {
    private final List<UUID> players;

    public ProxyPlayerSet(List<UUID> players) {
        this.players = players;
    }

    public List<ProxyPlayer> asProxyPlayers() {
        return players.stream().map(ProxyPlayer::new).toList();
    }
}
