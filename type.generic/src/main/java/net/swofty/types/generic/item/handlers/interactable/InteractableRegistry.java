package net.swofty.types.generic.item.handlers.interactable;

import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.entity.hologram.ServerHolograms;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockIsland;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.JerryInformation;
import java.util.HashMap;
import java.util.Map;

public class InteractableRegistry {
    private static final Map<String, InteractableItemConfig> REGISTERED_HANDLERS = new HashMap<>();

    static {
        register("SKYBLOCK_MENU_INTERACT", InteractableItemConfig.builder().rightClickHandler(
                ((skyBlockPlayer, skyBlockItem) -> new GUISkyBlockMenu().open(skyBlockPlayer))
        ).leftClickHandler(
                ((skyBlockPlayer, skyBlockItem) -> new GUISkyBlockMenu().open(skyBlockPlayer))
        ).inventoryInteractHandler(
                ((skyBlockPlayer, skyBlockItem) -> {
                    new GUISkyBlockMenu().open(skyBlockPlayer);
                    return false;
                })
        ).build());
        register("MOVE_JERRY_INTERACT", InteractableItemConfig.builder().rightClickHandler(
                ((player, skyBlockItem) -> {
                    if (!SkyBlockConst.isIslandServer()) {
                        player.sendMessage("§cYou can't move Jerry here! He doesn't belong here!");
                        return;
                    }

                    Point position = player.getTargetBlockPosition(5);
                    if (position == null) {
                        return;
                    }

                    // Move up one block and center jerry
                    position = position.add(0.5, 1, 0.5);

                    SkyBlockIsland island = player.getSkyBlockIsland();
                    JerryInformation jerryInformation = island.getJerryInformation();

                    ServerHolograms.ExternalHologram hologram = jerryInformation.getHologram();
                    ServerHolograms.removeExternalHologram(hologram);

                    jerryInformation.setJerryPosition(new Pos(position).withLookAt(player.getPosition()));
                    jerryInformation.getJerry().teleport(jerryInformation.getJerryPosition());
                    jerryInformation.getJerry().lookAt(player.getPosition().add(0, 1.4, 0));

                    hologram = ServerHolograms.ExternalHologram.builder()
                            .text(hologram.getText())
                            .instance(hologram.getInstance())
                            .pos(jerryInformation.getJerryPosition().add(0, 1, 0))
                            .build();

                    ServerHolograms.addExternalHologram(hologram);
                    jerryInformation.setHologram(hologram);
                })
        ).build());
    }

    public static void register(String id, InteractableItemConfig handler) {
        REGISTERED_HANDLERS.put(id, handler);
    }

    public static InteractableItemConfig getHandler(String id) {
        return REGISTERED_HANDLERS.get(id);
    }
}