package net.swofty.types.generic.item.handlers.place;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.Block;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointMinionData;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.item.components.MinionComponent;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;

public class PlaceEventRegistry {
    private static final Map<String, PlaceEventHandler> REGISTERED_HANDLERS = new HashMap<>();

    static {
        register("minion", new PlaceEventHandler() {
            @Override
            public void onPlace(PlayerBlockPlaceEvent event, SkyBlockPlayer player, SkyBlockItem item) {
                if (!SkyBlockConst.isIslandServer()) {
                    player.sendMessage("§cYou can only place minions on your island!");
                    event.setCancelled(true);
                    return;
                }

                DatapointMinionData.ProfileMinionData playerData = player.getDataHandler().get(
                        DataHandler.Data.MINION_DATA, DatapointMinionData.class
                ).getValue();

                int slots = playerData.getSlots();

                IslandMinionData minionData = player.getSkyBlockIsland().getMinionData();

                if (minionData.getMinions().size() >= slots) {
                    player.sendMessage("§cYou have reached the maximum amount of minions you can place!");
                    event.setCancelled(true);
                    return;
                }

                MinionComponent component = item.getConfig().getComponent(MinionComponent.class);

                IslandMinionData.IslandMinion minion = minionData.initializeMinion(Pos.fromPoint(event.getBlockPosition()),
                        component.getMinionRegistry(),
                        item.getAttributeHandler().getMinionData(),
                        item.getAttributeHandler().isMithrilInfused());
                minionData.spawn(minion);

                event.setBlock(Block.AIR);

                player.sendMessage(
                        "§bYou placed a minion! (" + minionData.getMinions().size() + "/" + playerData.getSlots() + ")"
                );
            }
        });
        register("CARROT_CRYSTAL_PLACE", new PlaceEventHandler() {
            @Override
            public void onPlace(PlayerBlockPlaceEvent event, SkyBlockPlayer player, SkyBlockItem item) {

            }
        });
    }

    public static void register(String id, PlaceEventHandler handler) {
        REGISTERED_HANDLERS.put(id, handler);
    }

    public static PlaceEventHandler getHandler(String id) {
        return REGISTERED_HANDLERS.get(id);
    }
}