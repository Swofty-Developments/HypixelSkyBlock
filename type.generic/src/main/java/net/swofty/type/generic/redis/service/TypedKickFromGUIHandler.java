package net.swofty.type.generic.redis.service;

import net.swofty.commons.protocol.ServicePushProtocol;
import net.swofty.commons.protocol.objects.gui.KickFromGUIPushProtocol;
import net.swofty.commons.protocol.objects.gui.KickFromGUIPushProtocol.Request;
import net.swofty.commons.protocol.objects.gui.KickFromGUIPushProtocol.Response;
import net.swofty.proxyapi.redis.TypedServiceHandler;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TypedKickFromGUIHandler implements TypedServiceHandler<Request, Response> {

    private static final KickFromGUIPushProtocol PROTOCOL = new KickFromGUIPushProtocol();

    @Override
    public ServicePushProtocol<Request, Response> getProtocol() {
        return PROTOCOL;
    }

    @Override
    public Response onMessage(Request message) {
        List<UUID> kickedPlayers = new ArrayList<>();

        for (UUID playerUUID : message.playerUUIDs()) {
            HypixelPlayer player = HypixelGenericLoader.getFromUUID(playerUUID);

            if (player != null && HypixelInventoryGUI.GUI_MAP.containsKey(playerUUID)) {
                HypixelInventoryGUI gui = HypixelInventoryGUI.GUI_MAP.get(playerUUID);

                if (gui.getClass().getSimpleName().toLowerCase().contains(message.guiType().toLowerCase())) {
                    player.closeInventory();
                    player.sendMessage("§cYou have been kicked from the " + message.guiType() + " GUI due to a data synchronization operation.");
                    kickedPlayers.add(playerUUID);
                }
            }
        }

        return new Response(true, kickedPlayers, kickedPlayers.size());
    }
}
