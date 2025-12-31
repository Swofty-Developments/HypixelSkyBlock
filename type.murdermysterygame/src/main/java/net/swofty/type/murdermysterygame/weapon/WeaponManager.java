package net.swofty.type.murdermysterygame.weapon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;

public class WeaponManager {
    private final Game game;

    public WeaponManager(Game game) {
        this.game = game;
    }

    public void giveMurdererKnife(MurderMysteryPlayer player) {
        ItemStack knife = ItemStack.builder(Material.IRON_SWORD)
                .customName(Component.text("Murderer's Knife", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                .lore(
                        Component.text("Right-click to throw", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("Left-click for melee attack", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                )
                .build();
        player.getInventory().addItemStack(knife);
    }

    public void giveDetectiveBow(MurderMysteryPlayer player) {
        ItemStack bow = ItemStack.builder(Material.BOW)
                .customName(Component.text("Detective's Bow", NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false))
                .lore(
                        Component.text("One shot kill", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("Use it wisely!", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                )
                .build();
        ItemStack arrow = ItemStack.of(Material.ARROW, 1);

        player.getInventory().addItemStack(bow);
        player.getInventory().addItemStack(arrow);
    }

    public void giveInnocentBow(MurderMysteryPlayer player) {
        ItemStack bow = ItemStack.builder(Material.BOW)
                .customName(Component.text("Bow", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                .lore(
                        Component.text("Collected enough gold!", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("One shot - make it count!", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                )
                .build();
        ItemStack arrow = ItemStack.of(Material.ARROW, 1);

        player.getInventory().addItemStack(bow);
        player.getInventory().addItemStack(arrow);

        player.sendMessage(Component.text("You collected enough gold for a bow!", NamedTextColor.GREEN));
    }
}
