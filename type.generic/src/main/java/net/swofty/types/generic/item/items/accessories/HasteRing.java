package net.swofty.types.generic.item.items.accessories;

import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.CustomBlockBreakEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HasteRing implements Talisman, SkyBlockEventClass {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "3c26a1ec929d4b144266c56af11d9abaf93f6b274872c96d3e34cb7c7965";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Gives §aHaste I §7for §a15",
                "§7seconds when breaking any block.");
    }


    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
    public void run(CustomBlockBreakEvent event) {
        SkyBlockPlayer player = event.getPlayer();

        if (!player.hasTalisman(this)) return;

        player.addEffect(new Potion(PotionEffect.HASTE, (byte) 1 , 15));
    }

}
