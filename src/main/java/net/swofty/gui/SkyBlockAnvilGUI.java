package net.swofty.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.*;
import net.swofty.user.SkyBlockPlayer;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class SkyBlockAnvilGUI {
    public static Map<SkyBlockPlayer, Map.Entry<String, CompletableFuture<String>>> anvilGUIs = new HashMap<>();
    public static Map<SkyBlockPlayer, String> anvilValues = new HashMap<>();
    private final SkyBlockPlayer player;

    public SkyBlockAnvilGUI(SkyBlockPlayer player) {
        this.player = player;
    }

    public CompletableFuture<String> open(String text) {
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(0, ItemStack.builder(Material.PAPER)
                .displayName(Component.text("")).build());
        stacks.add(1, ItemStack.of(Material.AIR));
        stacks.add(2, ItemStack.builder(Material.PAPER)
                .displayName(Component.text("")).build());
        ItemStack[] stackList = player.getInventory().getItemStacks();
        List<ItemStack> stacks2 = new ArrayList<>(Arrays.asList(stackList));
        for (int i = 27; i <= 35; i++) {
            stacks.add(stacks2.get(i));
        }
        for (int i = 9; i <= 26; i++) {
            stacks.add(stacks2.get(i));
        }
        for (int i = 0; i <= 8; i++) {
            stacks.add(stacks2.get(i));
        }
        int i = 0;
        for (ItemStack stack : stacks) {
            i++;
        }

        player.sendPackets(
                new OpenWindowPacket(99, 7, Component.text("Insert Data: " + text)),
                new WindowItemsPacket((byte) 99, 1, stacks, ItemStack.of(Material.AIR)),
                new WindowPropertyPacket((byte) 99, (short) 0, (short) 0)
        );

        CompletableFuture<String> future = new CompletableFuture<>();
        anvilGUIs.put(player, Map.entry(text, future));
        anvilValues.put(player, text);
        return future;
    }
}
