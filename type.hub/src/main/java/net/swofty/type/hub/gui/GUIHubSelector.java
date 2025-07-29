package net.swofty.type.hub.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.proxyapi.ProxyInformation;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockPaginatedGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class GUIHubSelector extends SkyBlockPaginatedGUI<UnderstandableProxyServer> implements RefreshingGUI {
    private final ProxyInformation information;
    private List<UnderstandableProxyServer> servers;
    private int counter = 0;
    private boolean sending = false;

    public GUIHubSelector() {
        super(InventoryType.CHEST_6_ROW);

        information = new ProxyInformation();
    }

    @Override
    protected int[] getPaginatedSlots() {
        return new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };
    }

    @Override
    protected PaginationList<UnderstandableProxyServer> fillPaged(SkyBlockPlayer player, PaginationList<UnderstandableProxyServer> paged) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        servers = information.getServerInformation(ServerType.HUB).join();
        paged.addAll(servers);

        set(GUIClickableItem.getCloseItem(49));
        set(new GUIClickableItem(50) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                ClickType clickType = e.getClickType();

                if (sending) {
                    player.sendMessage("§cWe are currently trying to queue you into another server!");
                    return;
                }

                // Exclude current server
                List<UnderstandableProxyServer> serversToUse = servers.stream()
                        .filter(server -> !server.name().equals(SkyBlockConst.getServerName()))
                        .toList();

                if (serversToUse.isEmpty()) {
                    player.sendMessage("§cNo servers with enough players found!");
                    return;
                }

                sending = true;

                if (clickType == ClickType.RIGHT_CLICK) {
                    UnderstandableProxyServer smallestServer = serversToUse.stream()
                            .min(Comparator.comparingInt((UnderstandableProxyServer server) -> server.players().size()))
                           .orElseThrow();
                    player.sendMessage("§7Request join for Hub mega" + smallestServer.name() + "...");
                    ProxyPlayer proxyPlayer = new ProxyPlayer(player.getUuid());
                    proxyPlayer.transferToWithIndication(smallestServer.uuid())
                            .orTimeout(3, TimeUnit.SECONDS)
                            .exceptionally(throwable -> {
                                if (throwable instanceof TimeoutException) {
                                    player.sendMessage("§cYour transfer failed! The server took too long to respond.");
                                } else {
                                    player.sendMessage("§cYour transfer failed! An error occurred.");
                                }
                                sending = false;
                                return null; // Return value for the CompletableFuture
                            });
                    return;
                }

                int randomIndex = (int) (Math.random() * serversToUse.size());
                UnderstandableProxyServer randomServer = serversToUse.get(randomIndex);
                player.sendMessage("§7Request join for Hub mega" + randomServer.name() + "...");
                ProxyPlayer proxyPlayer = new ProxyPlayer(player.getUuid());
                proxyPlayer.transferToWithIndication(randomServer.uuid())
                        .orTimeout(3, TimeUnit.SECONDS)
                        .exceptionally(throwable -> {
                            if (throwable instanceof TimeoutException) {
                                player.sendMessage("§cYour transfer failed! The server took too long to respond.");
                            } else {
                                player.sendMessage("§cYour transfer failed! An error occurred.");
                            }
                            sending = false;
                            return null; // Return value for the CompletableFuture
                        });
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack(
                        "§aRandom Hub",
                        Material.COMPASS, 1,
                        "§7Have no strong feelings one way",
                        "§7or the other?",
                        " ",
                        "§7Hub Servers: §a" + servers.size(),
                        "§7Current: §3mega" + SkyBlockConst.getServerName() + " §7(" + SkyBlockGenericLoader.getLoadedPlayers().size() + ")",
                        " ",
                        "§bRight-Click for a small server!",
                        "§eClick to join a random hub!"
                );
            }
        });

        return paged;
    }

    @Override
    protected boolean shouldFilterFromSearch(String query, UnderstandableProxyServer server) {
        return false;
    }

    @Override
    protected void performSearch(SkyBlockPlayer player, String query, int page, int maxPage) {
    }

    @Override
    protected String getTitle(SkyBlockPlayer player, String query, int page, PaginationList<UnderstandableProxyServer> paged) {
        return "SkyBlock Hub Selector";
    }

    @Override
    protected GUIClickableItem createItemFor(UnderstandableProxyServer server, int slot, SkyBlockPlayer player) {
        boolean isThisServer = server.port() == SkyBlockConst.getPort();
        return new GUIClickableItem(slot) {

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                counter++;
                return ItemStackCreator.getStack(
                        (isThisServer ? "§c"  : "§a") + "SkyBlock Hub #" + counter,
                        (isThisServer ? Material.RED_CONCRETE  : Material.QUARTZ_BLOCK), 1,
                        "§7Players: " + server.players().size(),
                        "§8Server: mega" + server.name(),
                        " ",
                        (isThisServer ? "§cAlready connected!" : "§eClick to connect!")
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                if (isThisServer) {
                    player.sendMessage("§cYou are already on this server!");
                    player.closeInventory();
                    return;
                }

                if (sending) {
                    player.sendMessage("§cWe are currently trying to queue you into another server!");
                    return;
                }

                sending = true;
                player.sendMessage("§7Request join for Hub #" + counter + " (mega" + server.name() + ")...");
                ProxyPlayer proxyPlayer = new ProxyPlayer(player.getUuid());
                proxyPlayer.transferToWithIndication(server.uuid())
                        .orTimeout(3, TimeUnit.SECONDS)
                        .exceptionally(throwable -> {
                            if (throwable instanceof TimeoutException) {
                                player.sendMessage("§cYour transfer failed! The server took too long to respond.");
                            } else {
                                player.sendMessage("§cYour transfer failed! An error occurred.");
                            }
                            sending = false;
                            return null; // Return value for the CompletableFuture
                        });
            }
        };
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {
        servers = information.getServerInformation(ServerType.HUB).join();
        PaginationList<UnderstandableProxyServer> paged = fillPaged(player, new PaginationList<>(getPaginatedSlots().length));
        int page = 1;
        counter = 0;

        updatePagedItems(paged, page, player);
    }

    @Override
    public int refreshRate() {
        return 20;
    }
}
