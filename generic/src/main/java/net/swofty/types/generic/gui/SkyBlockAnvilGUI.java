package net.swofty.types.generic.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.WindowPropertyPacket;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SkyBlockAnvilGUI {
    public static Map<SkyBlockPlayer, Map.Entry<String, CompletableFuture<String>>> anvilGUIs = new HashMap<>();
    private final SkyBlockPlayer player;

    public SkyBlockAnvilGUI(SkyBlockPlayer player) {
        this.player = player;
    }

    public CompletableFuture<String> open(String text) {
        Inventory inventory = new Inventory(InventoryType.ANVIL, Component.text("Insert Data: " + text));
        inventory.setItemStack(0, ItemStack.builder(Material.PAPER)
                .displayName(Component.text("")).build());
        inventory.setItemStack(1, ItemStack.of(Material.AIR));
        inventory.setItemStack(2, ItemStack.builder(Material.PAPER)
                .displayName(Component.text("")).build());

        player.openInventory(inventory);

        player.sendPacket(new WindowPropertyPacket(player.getOpenInventory().getWindowId(), (short) 0, (short) 0));

        CompletableFuture<String> future = new CompletableFuture<>();
        anvilGUIs.put(player, Map.entry(text, future));
        return future;
    }
}
