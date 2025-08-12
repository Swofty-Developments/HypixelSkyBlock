package net.swofty.type.skyblockgeneric.redis.service;

import net.swofty.commons.service.FromServiceChannels;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import SkyBlockPlayer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RedisKickFromGUI implements ServiceToClient {
    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.KICK_FROM_GUI;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        JSONArray playerUUIDs = message.getJSONArray("playerUUIDs");
        String guiType = message.getString("guiType");

        List<UUID> kickedPlayers = new ArrayList<>();

        for (int i = 0; i < playerUUIDs.length(); i++) {
            UUID playerUUID = UUID.fromString(playerUUIDs.getString(i));
            SkyBlockPlayer player = SkyBlockGenericLoader.getFromUUID(playerUUID);

            if (player != null && HypixelInventoryGUI.GUI_MAP.containsKey(playerUUID)) {
                HypixelInventoryGUI gui = net.swofty.type.generic.gui.inventory.HypixelInventoryGUI.GUI_MAP.get(playerUUID);

                // Check if GUI matches the type we want to kick from
                if (gui.getClass().getSimpleName().toLowerCase().contains(guiType.toLowerCase())) {
                    player.closeInventory();
                    player.sendMessage("§cYou have been kicked from the " + guiType + " GUI due to a data synchronization operation.");
                    kickedPlayers.add(playerUUID);
                }
            }
        }

        return new JSONObject()
                .put("success", true)
                .put("kickedPlayers", kickedPlayers)
                .put("totalKicked", kickedPlayers.size());
    }
}