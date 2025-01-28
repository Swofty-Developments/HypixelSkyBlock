package net.swofty.types.generic.redis;

import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBankData;
import net.swofty.types.generic.gui.inventory.inventories.banker.GUIBanker;
import net.swofty.types.generic.gui.inventory.inventories.banker.GUIBankerDeposit;
import net.swofty.types.generic.gui.inventory.inventories.banker.GUIBankerWithdraw;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.json.JSONObject;

import java.util.UUID;

public class RedisBankHash implements ProxyToClient {
    @Override
    public FromProxyChannels getChannel() {
        return FromProxyChannels.GET_BANK_HASH;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        UUID uuid = UUID.fromString(message.getString("uuid"));
        SkyBlockPlayer player = SkyBlockGenericLoader.getFromUUID(uuid);

        if (SkyBlockInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
            SkyBlockInventoryGUI gui = SkyBlockInventoryGUI.GUI_MAP.get(player.getUuid());
            if (gui.getClass() == GUIBankerDeposit.class ||
                    gui.getClass() == GUIBanker.class ||
                    gui.getClass() == GUIBankerWithdraw.class) {
                player.closeInventory();
                player.sendMessage("§cYou have been kicked from the banker GUI as your bank session has been invalidated by a coop member.");
            }
        }

        return new JSONObject().put("hash", player.getDataHandler().get(DataHandler.Data.BANK_DATA, DatapointBankData.class).getValue().getSessionHash());
    }
}
