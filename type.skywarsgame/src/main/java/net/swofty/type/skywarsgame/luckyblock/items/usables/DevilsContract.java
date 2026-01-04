package net.swofty.type.skywarsgame.luckyblock.items.usables;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockConsumable;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;
import java.util.Map;

public class DevilsContract implements LuckyBlockConsumable {

    @Override
    public String getId() {
        return "devils_contract";
    }

    @Override
    public String getDisplayName() {
        return "Devil's Contract";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.PAPER)
                .customName(Component.text(getDisplayName(), NamedTextColor.DARK_RED)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("Sign a contract with the devil...", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("Receive powerful items, but you", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("will die in 60 seconds!", NamedTextColor.RED)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Right-click to sign!", NamedTextColor.YELLOW)
                                .decoration(TextDecoration.ITALIC, false)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, getId())
                .build();
    }

    @Override
    public void onConsume(SkywarsPlayer player) {
        ItemStack sword = ItemStack.builder(Material.DIAMOND_SWORD)
                .customName(Component.text("Devil's Blade", NamedTextColor.DARK_RED)
                        .decoration(TextDecoration.ITALIC, false))
                .set(DataComponents.ENCHANTMENTS, new EnchantmentList(Map.of(Enchantment.SHARPNESS, 1)))
                .build();

        player.getInventory().addItemStack(sword);
        player.getInventory().addItemStack(ItemStack.of(Material.ENDER_PEARL, 16));

        player.sendMessage(Component.text("You signed the Devil's Contract!", NamedTextColor.DARK_RED));
        player.sendMessage(Component.text("You will die in 60 seconds...", NamedTextColor.RED));

        player.scheduler().buildTask(() -> {
            if (!player.isOnline() || player.isEliminated()) return;
            player.sendMessage(Component.text("30 seconds remaining!", NamedTextColor.RED));
        }).delay(TaskSchedule.seconds(30)).schedule();

        player.scheduler().buildTask(() -> {
            if (!player.isOnline() || player.isEliminated()) return;
            player.sendMessage(Component.text("10 seconds remaining!", NamedTextColor.DARK_RED));
        }).delay(TaskSchedule.seconds(50)).schedule();

        player.scheduler().buildTask(() -> {
            if (!player.isOnline() || player.isEliminated()) return;
            player.sendMessage(Component.text("5 seconds remaining!", NamedTextColor.DARK_RED));
        }).delay(TaskSchedule.seconds(55)).schedule();

        player.scheduler().buildTask(() -> {
            if (!player.isOnline() || player.isEliminated()) return;
            player.sendMessage(Component.text("The Devil collects his due...", NamedTextColor.DARK_RED));
            player.damage(Damage.fromPlayer(player, 1000));
        }).delay(TaskSchedule.seconds(60)).schedule();
    }
}
