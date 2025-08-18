package net.swofty.type.bedwarsgame.shop.traps;

import net.kyori.adventure.key.Key;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.attribute.AttributeModifier;
import net.minestom.server.entity.attribute.AttributeOperation;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.Trap;

public class MinerFatigueTrap extends Trap {

    public MinerFatigueTrap() {
        super(
                "miner_fatigue_trap",
                "Miner Fatigue Trap",
                ItemStack.of(Material.IRON_PICKAXE),
                "Inflicts Mining Fatigue II for 10s.",
                Currency.DIAMOND
        );
    }

    @Override
    public void triggered(Game game, String teamName, Player triggerer) {
        triggerer.getAttribute(Attribute.MINING_EFFICIENCY).addModifier(new AttributeModifier(Key.key("bw:miner_fatigue"), -0.6, AttributeOperation.MULTIPLY_TOTAL));
    }
}

