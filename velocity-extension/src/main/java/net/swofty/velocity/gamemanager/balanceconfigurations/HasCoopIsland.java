package net.swofty.velocity.gamemanager.balanceconfigurations;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import net.swofty.commons.ServerType;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.data.CoopDatabase;
import net.swofty.velocity.data.ProfilesDatabase;
import net.swofty.velocity.data.UserDatabase;
import net.swofty.velocity.gamemanager.BalanceConfiguration;
import net.swofty.velocity.gamemanager.GameManager;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HasCoopIsland extends BalanceConfiguration {
    @Override
    public GameManager.GameServer getServer(Player player, List<GameManager.GameServer> servers) {
        UUID activeProfile = UUID.fromString(new UserDatabase(player.getUniqueId()).getDocument().getString("selected"));
        Document document = new ProfilesDatabase(activeProfile.toString()).getDocument();

        if (!document.getString("is_coop").equals("true")) {
            return null;
        }

        Document coop = CoopDatabase.getFromMemberProfile(activeProfile);

        List<UUID> members = ((List<String>) coop.get("members")).stream().map(UUID::fromString).toList();
        List<Optional<Player>> potentiallyOnlineMembers = members.stream().map(SkyBlockVelocity.getServer()::getPlayer).toList();
        List<Player> onlineMembers = new ArrayList<>(potentiallyOnlineMembers
                .stream().filter(Optional::isPresent).map(Optional::get).toList());

        onlineMembers.remove(player);

        if (onlineMembers.isEmpty()) {
            return null;
        }

        GameManager.GameServer toSendTo = null;
        for (Player member : onlineMembers) {
            Optional<ServerConnection> potentialServer = member.getCurrentServer();
            if (potentialServer.isEmpty()) {
                continue;
            }
            ServerConnection serverConnection = potentialServer.get();
            UUID serverUUID = UUID.fromString(serverConnection.getServer().getServerInfo().getName());

            if (!GameManager.getTypeFromUUID(serverUUID).equals(ServerType.ISLAND)) {
                continue;
            }

            UUID memberProfile = UUID.fromString(new UserDatabase(member.getUniqueId()).getDocument().getString("selected"));
            Document memberDocument = new ProfilesDatabase(memberProfile.toString()).getDocument();

            if (!memberDocument.getString("is_coop").equals("true")) {
                continue;
            }

            toSendTo = GameManager.getFromUUID(serverUUID);
        }

        return toSendTo;
    }
}
