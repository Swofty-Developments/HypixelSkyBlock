package net.swofty.types.generic.redis;

import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBankData;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.banker.GUIBanker;
import net.swofty.types.generic.gui.inventory.inventories.banker.GUIBankerDeposit;
import net.swofty.types.generic.gui.inventory.inventories.banker.GUIBankerWithdraw;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.UUID;

public class RedisBankHash implements ProxyToClient {
    @Override
    public String onMessage(String message) {
        SkyBlockPlayer player = SkyBlockGenericLoader.getFromUUID(UUID.fromString(message));
        UUID uuid = player.getDataHandler()
                .get(DataHandler.Data.BANK_DATA, DatapointBankData.class)
                .getValue()
                .getSessionHash();

        if (SkyBlockInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
            SkyBlockInventoryGUI gui = SkyBlockInventoryGUI.GUI_MAP.get(player.getUuid());
            if (gui.getClass() == GUIBankerDeposit.class ||
                    gui.getClass() == GUIBanker.class ||
                    gui.getClass() == GUIBankerWithdraw.class) {
                player.closeInventory();
                player.sendMessage("§cYou have been kicked from the banker GUI as your bank session has been invalidated by a coop member.");
            }
        }

        return uuid.toString();
    }
}
