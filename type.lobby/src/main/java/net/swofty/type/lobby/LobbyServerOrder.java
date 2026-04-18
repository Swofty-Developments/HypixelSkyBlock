package net.swofty.type.lobby;

import lombok.NoArgsConstructor;
import net.swofty.commons.UnderstandableProxyServer;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
public final class LobbyServerOrder {

    public static List<UnderstandableProxyServer> sortBySelectorOrder(List<UnderstandableProxyServer> servers) {
        return servers.stream()
            .sorted(Comparator
                .comparingInt((UnderstandableProxyServer server) -> extractLobbyNumber(server.shortName()))
                .thenComparing(UnderstandableProxyServer::shortName))
            .toList();
    }

    public static @Nullable UnderstandableProxyServer getBySelectorIndex(List<UnderstandableProxyServer> orderedServers, int index) {
        if (index < 1 || index > orderedServers.size()) {
            return null;
        }
        return orderedServers.get(index - 1);
    }

    public static int extractLobbyNumber(String shortName) {
        try {
            String digits = shortName.replaceAll("[^0-9]", "");
            return digits.isEmpty() ? 1 : Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
