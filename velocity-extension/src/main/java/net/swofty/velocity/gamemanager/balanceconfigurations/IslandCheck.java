package net.swofty.velocity.gamemanager.balanceconfigurations;

import com.velocitypowered.api.proxy.Player;
import net.swofty.commons.ServerType;
import net.swofty.velocity.data.ProfilesDatabase;
import net.swofty.velocity.data.UserDatabase;
import net.swofty.velocity.gamemanager.BalanceConfiguration;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.RedisMessage;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class IslandCheck extends BalanceConfiguration {
    @Override
    public GameManager.GameServer getServer(Player player, List<GameManager.GameServer> servers) {
        Document userDatabase = new UserDatabase(player.getUniqueId()).getDocument();
        if (userDatabase == null) {
            return null;
        }
        UUID activeProfile = UUID.fromString(userDatabase.getString("selected"));
        Document document = new ProfilesDatabase(activeProfile.toString()).getDocument();
        if (document == null) {
            return null;
        }
        UUID islandUUID = UUID.fromString(document.getString("island_uuid").replace("\"", ""));

        AtomicReference<GameManager.GameServer> toSendTo = getGameServerAtomicReference(islandUUID);

        return toSendTo.get();
    }

    @NotNull
    private static AtomicReference<GameManager.GameServer> getGameServerAtomicReference(UUID islandUUID) {
        AtomicReference<GameManager.GameServer> toSendTo = new AtomicReference<>(null);

        for (Map.Entry<ServerType, ArrayList<GameManager.GameServer>> entry : GameManager.getServers().entrySet()) {
            ServerType serverType = entry.getKey();
            if (serverType == ServerType.ISLAND) {
                ArrayList<GameManager.GameServer> gameServers = entry.getValue();

                gameServers.forEach(gameServer -> {
                    String hasIsland = RedisMessage.sendMessageToServer(
                            gameServer.internalID(), "has-island", islandUUID.toString())
                            .join();

                    if (hasIsland.equals("true")) {
                        toSendTo.set(gameServer);
                    }
                });
            }
        }
        return toSendTo;
    }
}
