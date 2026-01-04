package net.swofty.type.skywarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.orchestrator.GetMapsProtocolObject;
import net.swofty.commons.skywars.SkywarsGameType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.lobby.GameQueueValidator;
import net.swofty.type.lobby.LobbyOrchestratorConnector;

import java.util.ArrayList;
import java.util.List;

public class GUIMapSelectionSkywars extends HypixelInventoryGUI {
    private final SkywarsGameType gameType;
    private List<String> maps = new ArrayList<>();
    private boolean mapsLoaded = false;

    public GUIMapSelectionSkywars(SkywarsGameType gameType) {
        super("Map Selection - " + gameType.getDisplayName(), InventoryType.CHEST_4_ROW);
        this.gameType = gameType;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();

        if (!mapsLoaded) {
            set(new GUIClickableItem(13) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(
                            "§eLoading maps...",
                            Material.CLOCK, 1,
                            "§7Please wait while we fetch",
                            "§7available maps for " + gameType.getDisplayName()
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                }
            });

            loadMaps(player);
        } else {
            populateMaps(player);
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    private void loadMaps(HypixelPlayer player) {
        ProxyService orchestratorService = new ProxyService(ServiceType.ORCHESTRATOR);

        GetMapsProtocolObject.GetMapsMessage message =
                new GetMapsProtocolObject.GetMapsMessage(ServerType.SKYWARS_GAME, gameType.name());

        orchestratorService.handleRequest(message)
                .thenAccept(response -> {
                    if (response instanceof GetMapsProtocolObject.GetMapsResponse mapsResponse) {
                        maps = mapsResponse.maps();
                        mapsLoaded = true;
                        populateMaps(player);
                        updateItemStacks(getInventory(), player);
                    }
                })
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    player.sendMessage("§cFailed to load maps: " + throwable.getMessage());
                    player.closeInventory();
                    return null;
                });
    }

    private void populateMaps(HypixelPlayer player) {
        if (maps.isEmpty()) {
            set(new GUIClickableItem(13) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(
                            "§cNo maps available",
                            Material.BARRIER, 1,
                            "§7No maps are currently available",
                            "§7for " + gameType.getDisplayName(),
                            "",
                            "§eClick to go back"
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    new GUIPlaySkywars(gameType, false).open(player);
                }
            });
            return;
        }

        // Back button
        set(new GUIClickableItem(31) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§cBack",
                        Material.ARROW, 1,
                        "§7Go back to game selection"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIPlaySkywars(gameType, false).open(player);
            }
        });

        // Add map options
        int slot = 10;
        for (String map : maps) {
            if (slot > 25) break;

            final String mapName = map;

            set(new GUIClickableItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(
                            "§a" + mapName,
                            Material.FIREWORK_STAR, 1,
                            "§7" + gameType.getDisplayName(),
                            "",
                            "§7Available Games: §aUnknown",
                            "",
                            "§aClick to Play"
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    player.closeInventory();

                    if (!GameQueueValidator.canPlayerQueue(player)) {
                        return;
                    }

                    LobbyOrchestratorConnector connector = new LobbyOrchestratorConnector(player);
                    connector.sendToGame(ServerType.SKYWARS_GAME, gameType.name(), mapName);
                }
            });

            slot++;
            if (slot == 17) slot = 19; // Skip to next row
        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }
}
