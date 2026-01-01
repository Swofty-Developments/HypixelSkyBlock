package net.swofty.type.lobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.lobby.ServerInfoCache;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Dynamic lobby selector that shows only available lobbies.
 * Displays current/max players and highlights the current server.
 */
public class GUILobbySelector extends HypixelInventoryGUI {

    private final ServerType lobbyType;
    private final String lobbyName;
    private List<UnderstandableProxyServer> lobbies = new ArrayList<>();
    private boolean loaded = false;

    public GUILobbySelector(ServerType lobbyType, String lobbyName) {
        super(lobbyName + " Selector", InventoryType.CHEST_2_ROW);
        this.lobbyType = lobbyType;
        this.lobbyName = lobbyName;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();

        if (!loaded) {
            // Show loading state
            set(new GUIClickableItem(4) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    return ItemStackCreator.getStack("§eLoading lobbies...",
                            Material.CLOCK, 1,
                            "§7Please wait...");
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    // No action while loading
                }
            });
            loadLobbies(player);
        } else {
            populateLobbies(player);
        }

        updateItemStacks(getInventory(), player);
    }

    private void loadLobbies(HypixelPlayer player) {
        ServerInfoCache.getServersByType(lobbyType).thenAccept(servers -> {
            lobbies = servers.stream()
                    .sorted(Comparator.comparing(s -> extractLobbyNumber(s.shortName())))
                    .toList();
            loaded = true;
            populateLobbies(player);
            updateItemStacks(getInventory(), player);
        }).exceptionally(throwable -> {
            player.sendMessage("§cFailed to load lobbies: " + throwable.getMessage());
            player.closeInventory();
            return null;
        });
    }

    private void populateLobbies(HypixelPlayer player) {
        items.clear();
        for (int i = 0; i < 18; i++) {
            getInventory().setItemStack(i, ItemStack.AIR);
        }

        UUID currentServer = HypixelConst.getServerUUID();

        if (lobbies.isEmpty()) {
            set(new GUIClickableItem(4) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    return ItemStackCreator.getStack("§cNo lobbies available",
                            Material.BARRIER, 1,
                            "§7No lobbies are currently online.");
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    // No action
                }
            });
            return;
        }

        for (int i = 0; i < lobbies.size() && i < 18; i++) {
            UnderstandableProxyServer lobby = lobbies.get(i);
            boolean isCurrentServer = lobby.uuid().equals(currentServer);
            int lobbyNumber = i + 1;

            set(new GUIClickableItem(i) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    int players = lobby.players().size();
                    int max = lobby.maxPlayers();

                    Material material = isCurrentServer ? Material.RED_TERRACOTTA : Material.QUARTZ_BLOCK;
                    String titleColor = isCurrentServer ? "§c" : "§a";
                    String statusLine = isCurrentServer ? "§cAlready connected!" : "§eClick to connect!";

                    return ItemStackCreator.getStack(
                            titleColor + lobbyName + " #" + lobbyNumber,
                            material,
                            lobbyNumber,
                            "§7Players: " + players + "/" + max,
                            "",
                            statusLine
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    if (isCurrentServer) {
                        p.sendMessage("§cYou are already connected to this lobby!");
                        return;
                    }
                    p.closeInventory();
                    p.sendMessage("§aSending you to " + lobbyName + " #" + lobbyNumber + "...");
                    p.asProxyPlayer().transferToWithIndication(lobby.uuid());
                }
            });
        }
    }

    private int extractLobbyNumber(String shortName) {
        try {
            String digits = shortName.replaceAll("[^0-9]", "");
            return digits.isEmpty() ? 1 : Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
