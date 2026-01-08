package net.swofty.type.murdermysterylobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.murdermystery.MurderMysteryGameType;
import net.swofty.commons.ServerType;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.lobby.LobbyOrchestratorConnector;
import net.swofty.type.generic.utility.GameCountCache;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.party.PartyManager;
import net.swofty.commons.party.FullParty;

public class GUIPlayMurderMystery extends HypixelInventoryGUI {

    public GUIPlayMurderMystery() {
        super("Play Murder Mystery", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        set(new GUIClickableItem(12) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                int playersInGame = GameCountCache.getPlayerCount(
                        ServerType.MURDER_MYSTERY_GAME,
                        MurderMysteryGameType.CLASSIC.name()
                );
                String playersPlaying = StringUtility.commaify(playersInGame);

                return ItemStackCreator.getStack(
                        "§aMurder Mystery (Classic)",
                        Material.CLOCK,
                        1,
                        "§8Solo",
                        "§8" + MurderMysteryGameType.CLASSIC.getMaxPlayers() + " Total Players",
                        "",
                        "§7The Classic Murder Mystery",
                        "§7experience - take on the role of",
                        "§cMurderer§7, §9Detective §7or §aInnocent§7. The",
                        "§7Murderer must try and kill without",
                        "§7getting caught, while the others must",
                        "§7try to figure out who they are!",
                        "",
                        "§c1 Murderer",
                        "§91 Detective",
                        "§a10 Innocents",
                        "",
                        "§a" + playersPlaying + " currently playing!",
                        "",
                        "§eClick to play!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.closeInventory();

                if (LobbyOrchestratorConnector.isSearching(player.getUuid())) {
                    player.sendMessage("§cYou are already searching for a game!");
                    return;
                }

                // Party check - non-leaders cannot queue
                if (PartyManager.isInParty(player)) {
                    FullParty party = PartyManager.getPartyFromPlayer(player);
                    if (party != null && !party.getLeader().getUuid().equals(player.getUuid())) {
                        player.sendMessage("§cYou are in a party! Ask your leader to start the game, or /p leave");
                        return;
                    }
                }

                LobbyOrchestratorConnector connector = new LobbyOrchestratorConnector(player);
                connector.sendToGame(ServerType.MURDER_MYSTERY_GAME, MurderMysteryGameType.CLASSIC.toString());
            }
        });
        set(new GUIClickableItem(14) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                int playersInGame = GameCountCache.getPlayerCount(
                        ServerType.MURDER_MYSTERY_GAME,
                        MurderMysteryGameType.DOUBLE_UP.name()
                );
                String playersPlaying = StringUtility.commaify(playersInGame);

                return ItemStackCreator.getStack(
                        "§aMurder Mystery (Double Up!)",
                        Material.CLOCK,
                        1,
                        "§8Teams",
                        "§8" + MurderMysteryGameType.DOUBLE_UP.getMaxPlayers() + " Total Players",
                        "",
                        "§7The Classic Murder Mystery",
                        "§7experience - take on the role of",
                        "§cMurderer§7, §9Detective §7or §aInnocent§7. The",
                        "§7Murderers must try and kill without",
                        "§7getting caught, while the others must",
                        "§7try to figure out who they are!",
                        "",
                        "§c2 Murderers",
                        "§92 Detectives",
                        "§a12 Innocents",
                        "",
                        "§a" + playersPlaying + " currently playing!",
                        "",
                        "§eClick to play!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.closeInventory();

                if (LobbyOrchestratorConnector.isSearching(player.getUuid())) {
                    player.sendMessage("§cYou are already searching for a game!");
                    return;
                }

                // Party check - non-leaders cannot queue
                if (PartyManager.isInParty(player)) {
                    FullParty party = PartyManager.getPartyFromPlayer(player);
                    if (party != null && !party.getLeader().getUuid().equals(player.getUuid())) {
                        player.sendMessage("§cYou are in a party! Ask your leader to start the game, or /p leave");
                        return;
                    }
                }

                LobbyOrchestratorConnector connector = new LobbyOrchestratorConnector(player);
                connector.sendToGame(ServerType.MURDER_MYSTERY_GAME, MurderMysteryGameType.DOUBLE_UP.toString());
            }
        });
        set(new GUIClickableItem(21) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aMap Selector (Classic)",
                        Material.OAK_SIGN,
                        1,
                        "§7Pick which map you want to play from",
                        "§7a list of available games.",
                        "",
                        "§eClick to browse!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIMapSelection(MurderMysteryGameType.CLASSIC).open(player);
            }
        });
        set(new GUIClickableItem(23) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aMap Selector (Double Up!)",
                        Material.OAK_SIGN,
                        1,
                        "§7Pick which map you want to play from",
                        "§7a list of available games.",
                        "",
                        "§eClick to browse!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIMapSelection(MurderMysteryGameType.DOUBLE_UP).open(player);
            }
        });
        set(GUIClickableItem.getCloseItem(40));
        updateItemStacks(getInventory(), getPlayer());
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
