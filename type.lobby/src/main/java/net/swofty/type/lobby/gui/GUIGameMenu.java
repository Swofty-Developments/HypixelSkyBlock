package net.swofty.type.lobby.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.component.DataComponents;
import net.minestom.server.network.player.ResolvableProfile;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.lobby.ServerInfoCache;
import net.swofty.type.lobby.game.GameType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GUIGameMenu extends HypixelInventoryGUI implements RefreshingGUI {

    private static final int[] GAME_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
                    21,     23,
            28, 29, 30, 31, 32, 33, 33,
            37, 38, 39, 40, 41, 42, 43
    };

    private int cycleIndex = 0;

    public GUIGameMenu() {
        super("Game Menu", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();

        set(new GUIClickableItem(4) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {

            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack("§aMain Lobby",
                        Material.BOOKSHELF, 1,
                        "",
                        "§7Return to the Main Lobby.");
            }
        });

        // Refresh cache, then populate
        ServerInfoCache.getServers().thenAccept(servers -> {
            int i = 0;
            for (GameType game : GameType.values()) {
                if (i >= GAME_SLOTS.length) break;
                set(createGameItem(game, GAME_SLOTS[i++]));
            }
            set(createRandomGameItem(49));
            updateItemStacks(getInventory(), player);
        });

        updateItemStacks(getInventory(), player);
    }

    private GUIClickableItem createGameItem(GameType game, int slot) {
        return new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                String playerCount = StringUtility.commaify(game.getPlayerCount());
                ItemStack.Builder itemBuilder = ItemStackCreator.getFromStack(game.getItem().build());

                List<String> lore = new ArrayList<>();
                lore.add("§8" + StringUtility.toNormalCase(game.getCategory().name()));
                lore.add("");
                lore.addAll(Arrays.asList(game.getLore()));
                lore.add("");
                if (game.isImplemented()) {
                    if (cycleIndex % 2 == 0) {
                        lore.add("§a  Click to Connect!");
                    } else {
                        lore.add("§a► Click to Connect!");
                    }
                    lore.add("§7" + playerCount + " currently playing!");
                }

                return ItemStackCreator.appendLore(itemBuilder, lore).customName(
                        Component.text("§a" + game.getDisplayName())
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (!game.isImplemented()) {
                    player.sendMessage("§cThis game is not yet available!");
                    return;
                }
                player.closeInventory();
                player.sendTo(game.getLobbyType());
            }
        };
    }

    private GUIClickableItem createRandomGameItem(int slot) {
        GameType displayGame = GameType.values()[cycleIndex % GameType.values().length];
        return new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                ItemStack base = displayGame.getItem().build();
                ItemStack.Builder builder = ItemStack.builder(base.material())
                        .amount(1)
                        .customName(Component.text("§aRandom Game"))
                        .lore(
                                Component.text("§7Join a random game."),
                                Component.empty(),
                                Component.text("§eClick to Play")
                        );

                // Copy head texture if present
                ResolvableProfile profile = base.get(DataComponents.PROFILE);
                if (profile != null) {
                    builder.set(DataComponents.PROFILE, profile);
                }

                return builder;
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                List<GameType> implemented = Arrays.stream(GameType.values())
                        .filter(GameType::isImplemented)
                        .toList();
                if (implemented.isEmpty()) {
                    player.sendMessage("§cNo games available!");
                    return;
                }
                GameType random = implemented.get(
                        ThreadLocalRandom.current().nextInt(implemented.size())
                );
                player.closeInventory();
                player.sendTo(random.getLobbyType());
            }
        };
    }

    @Override
    public void refreshItems(HypixelPlayer player) {
        cycleIndex++;
        set(createRandomGameItem(49));
    }

    @Override
    public int refreshRate() {
        return 10; // 0.5 seconds
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
