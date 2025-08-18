package net.swofty.type.bedwarsgame.shop.upgrades;

import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.TeamUpgrade;
import net.swofty.type.bedwarsgame.shop.TeamUpgradeTier;

import java.util.List;

public class SharpenedSwordUpgrade extends TeamUpgrade {

    public SharpenedSwordUpgrade() {
        super(
                "sharpness",
                "Sharpened Swords",
                "Your team permanently gains Sharpness on all swords and axes!",
                ItemStack.of(Material.IRON_SWORD),
                List.of(
                        new TeamUpgradeTier(1, "Sharpness I", 4, Currency.DIAMOND)
                )
        );
    }

    @Override
    public void applyEffect(Game game, String teamName, int level) {
        game.getPlayers().stream()
                .filter(p -> teamName.equals(p.getTag(Tag.String("team"))))
                .forEach(player -> enchantItems(player, level));
    }

    public static void enchantItems(Player player, int level) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack stack = player.getInventory().getItemStack(i);
            if (stack.material().name().endsWith("_sword") || stack.material().name().endsWith("_axe")) {
                player.getInventory().setItemStack(i, stack.with(ItemComponent.ENCHANTMENTS, EnchantmentList.EMPTY.with(Enchantment.SHARPNESS, level)));
            }
        }
    }
}

