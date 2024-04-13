package net.swofty.types.generic.item.items.accessories;

import net.minestom.server.entity.EntityType;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.CustomBlockBreakEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.PlayerDamagedByMobValueUpdateEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@EventParameters(description = "break block get haste effect",
        node = EventNodes.CUSTOM,
        requireDataLoaded = true)
public class HasteRing extends SkyBlockEvent implements Talisman {
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
    public void run(Event tempEvent) {
        CustomBlockBreakEvent event = (CustomBlockBreakEvent) tempEvent;
        SkyBlockPlayer player = event.getPlayer();

        if (!player.hasTalisman(this)) return;

        player.addEffect(new Potion(PotionEffect.HASTE, (byte) 1 , 15));
    }

    @Override
    public Class<? extends Event> getEvent() {
        return CustomBlockBreakEvent.class;
    }

}
