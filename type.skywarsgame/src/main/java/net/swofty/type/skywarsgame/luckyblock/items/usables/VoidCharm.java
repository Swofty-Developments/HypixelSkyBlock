package net.swofty.type.skywarsgame.luckyblock.items.usables;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockConsumable;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class VoidCharm implements LuckyBlockConsumable {

    public static final Tag<Boolean> VOID_CHARM_TAG = Tag.Boolean("has_void_charm");

    @Override
    public String getId() {
        return "void_charm";
    }

    @Override
    public String getDisplayName() {
        return "Void Charm";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.GHAST_TEAR)
                .customName(Component.text(getDisplayName(), NamedTextColor.LIGHT_PURPLE)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("Activates protection from", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("falling into the void!", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("(One-time use)", NamedTextColor.DARK_GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Right-click to activate!", NamedTextColor.YELLOW)
                                .decoration(TextDecoration.ITALIC, false)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, getId())
                .build();
    }

    @Override
    public void onConsume(SkywarsPlayer player) {
        player.setTag(VOID_CHARM_TAG, true);
        player.sendMessage(Component.text("Void Charm activated!", NamedTextColor.LIGHT_PURPLE));
        player.sendMessage(Component.text("You will be saved from the void once!", NamedTextColor.GRAY));
    }

    public static boolean hasVoidCharm(SkywarsPlayer player) {
        Boolean hasCharm = player.getTag(VOID_CHARM_TAG);
        return hasCharm != null && hasCharm;
    }

    public static void consumeVoidCharm(SkywarsPlayer player) {
        player.removeTag(VOID_CHARM_TAG);
    }
}
