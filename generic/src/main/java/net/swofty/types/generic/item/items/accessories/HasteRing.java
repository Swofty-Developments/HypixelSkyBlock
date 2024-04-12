package net.swofty.types.generic.item.items.accessories;

import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.types.generic.enchantment.EnchantmentType;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.MiningValueUpdateEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.BlockBreakEvent;
import net.swofty.types.generic.item.impl.ConstantStatistics;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.PlayerEnchantmentHandler;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HasteRing implements Talisman, NotFinishedYet, BlockBreakEvent {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "3c26a1ec929d4b144266c56af11d9abaf93f6b274872c96d3e34cb7c7965";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Gives §aHaste I §7for §a15",
                "§7seconds when breaking any block.");
    }

    @Override
    public void onBreak(PlayerBlockBreakEvent event, SkyBlockPlayer player, SkyBlockItem item) {
        if (!player.hasTalisman(this)) return;
        player.addEffect(new Potion(PotionEffect.HASTE, (byte) 1 , 15));
    }
}
