package net.swofty.type.ravengardgeneric.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;

public class GUIRavengardMenu extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("󏀈§f󏿳Main MenuMain Menu", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.slot(0, ItemStackCreator.getStack(
                Component.translatable("item.minecraft.stick"),
                Material.STICK,
                1
        ));
        layout.slot(3, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x642100)).append(Component.text("Join the Fight!").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Jump into the action and fight"),
                Component.text("§7against other players and monsters!"),
                Component.text("§7"),
                Component.text("§7§eClick to join!")
        ));
        layout.slot(4, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x642100)).append(Component.text("Join the Fight!").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Jump into the action and fight"),
                Component.text("§7against other players and monsters!"),
                Component.text("§7"),
                Component.text("§7§eClick to join!")
        ));
        layout.slot(5, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x642100)).append(Component.text("Join the Fight!").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Jump into the action and fight"),
                Component.text("§7against other players and monsters!"),
                Component.text("§7"),
                Component.text("§7§eClick to join!")
        ));
        layout.slot(18, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x2A4900)).append(Component.text("Profiles - Assassin").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text(""),
                Component.text("§7A stealth-focused class adept at"),
                Component.text("§7sneaking."),
                Component.text(""),
                Component.text("§7Primary weapon: §fDaggers"),
                Component.text(""),
                Component.text("§7Stats:"),
                Component.text("§7◦ Health §c160.0 ❤"),
                Component.text("§7◦ Protection §b0.0 ⛊"),
                Component.text("§7◦ Damage §420.0 ⚔"),
                Component.text(""),
                Component.text("§eClick to change profile!")
        ));
        layout.slot(20, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x504800)).append(Component.text("Lockbox").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Safely store your items here!"),
                Component.text("§7"),
                Component.text("§7§eClick to open!")
        ));
        layout.slot(21, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x504800)).append(Component.text("Lockbox").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Safely store your items here!"),
                Component.text("§7"),
                Component.text("§7§eClick to open!")
        ));
        layout.slot(22, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x7B4400)).append(Component.text("Abcdefgh").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Lorem ipsum dolor sit amet!"),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(23, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x7B4400)).append(Component.text("Abcdefgh").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Lorem ipsum dolor sit amet!"),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(24, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x964700)).append(Component.text("Ijklmno").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Consectetur adipiscing elit."),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(25, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x964700)).append(Component.text("Ijklmno").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Consectetur adipiscing elit."),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(26, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0xB73C00)).append(Component.text("Ability 1 - Shadows").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7§7Become invisible for §e25 seconds§7, but"),
                Component.text("§7movement speed is reduced."),
                Component.text("§7Attacking or being attacked cancels"),
                Component.text("§7the invisibility."),
                Component.text(""),
                Component.text("§eClick to change!")
        ));
        layout.slot(27, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x2A4900)).append(Component.text("Profiles - Assassin").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text(""),
                Component.text("§7A stealth-focused class adept at"),
                Component.text("§7sneaking."),
                Component.text(""),
                Component.text("§7Primary weapon: §fDaggers"),
                Component.text(""),
                Component.text("§7Stats:"),
                Component.text("§7◦ Health §c160.0 ❤"),
                Component.text("§7◦ Protection §b0.0 ⛊"),
                Component.text("§7◦ Damage §420.0 ⚔"),
                Component.text(""),
                Component.text("§eClick to change profile!")
        ));
        layout.slot(29, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x504800)).append(Component.text("Lockbox").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Safely store your items here!"),
                Component.text("§7"),
                Component.text("§7§eClick to open!")
        ));
        layout.slot(30, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x504800)).append(Component.text("Lockbox").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Safely store your items here!"),
                Component.text("§7"),
                Component.text("§7§eClick to open!")
        ));
        layout.slot(31, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x7B4400)).append(Component.text("Abcdefgh").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Lorem ipsum dolor sit amet!"),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(32, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x7B4400)).append(Component.text("Abcdefgh").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Lorem ipsum dolor sit amet!"),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(33, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x964700)).append(Component.text("Ijklmno").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Consectetur adipiscing elit."),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(34, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x964700)).append(Component.text("Ijklmno").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Consectetur adipiscing elit."),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(36, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x2A4900)).append(Component.text("Profiles - Assassin").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text(""),
                Component.text("§7A stealth-focused class adept at"),
                Component.text("§7sneaking."),
                Component.text(""),
                Component.text("§7Primary weapon: §fDaggers"),
                Component.text(""),
                Component.text("§7Stats:"),
                Component.text("§7◦ Health §c160.0 ❤"),
                Component.text("§7◦ Protection §b0.0 ⛊"),
                Component.text("§7◦ Damage §420.0 ⚔"),
                Component.text(""),
                Component.text("§eClick to change profile!")
        ));
        layout.slot(38, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x546C00)).append(Component.text("Pqrstuv").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Sed do eiusmod tempor incididunt!"),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(39, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x697500)).append(Component.text("Pqrstu").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Sed do eiusmod tempor incididunt!"),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(40, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x697500)).append(Component.text("Pqrstu").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Sed do eiusmod tempor incididunt!"),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(41, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x697500)).append(Component.text("Pqrstu").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Sed do eiusmod tempor incididunt!"),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(42, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x9E6F00)).append(Component.text("Vwxyz").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Ut labore et dolore magna aliqua!"),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(44, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0xB76000)).append(Component.text("Ability 2 - Heal Wounds").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7§7Heals §c+35 HP §7over §e10 seconds§7."),
                Component.text(""),
                Component.text("§eClick to change!")
        ));
        layout.slot(47, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x546C00)).append(Component.text("Pqrstuv").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Sed do eiusmod tempor incididunt!"),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(48, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x697500)).append(Component.text("Pqrstu").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Sed do eiusmod tempor incididunt!"),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(49, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x697500)).append(Component.text("Pqrstu").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Sed do eiusmod tempor incididunt!"),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(50, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x697500)).append(Component.text("Pqrstu").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Sed do eiusmod tempor incididunt!"),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
        layout.slot(51, ItemStackCreator.getStack(
                Component.text("󏿿").color(TextColor.color(0x9E6F00)).append(Component.text("Vwxyz").color(NamedTextColor.WHITE)),
                Material.LEATHER_CHESTPLATE,
                1,
                Component.text("§7Ut labore et dolore magna aliqua!"),
                Component.text("§7"),
                Component.text("§7§cComing Soon!")
        ));
    }
}
