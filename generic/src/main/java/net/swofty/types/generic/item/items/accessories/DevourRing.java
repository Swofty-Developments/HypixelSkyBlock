package net.swofty.types.generic.item.items.accessories;

import net.minestom.server.event.Event;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.PlayerKilledSkyBlockMobEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@EventParameters(description = "heal 5 hp when player kills mob",
        node = EventNodes.CUSTOM,
        requireDataLoaded = false)
public class DevourRing extends SkyBlockEvent implements Talisman {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Heal §c5❤ §7when killing a monster.",
                "§8Cooldown: §a0.5s");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8a7d1a52e8c8ddaaf0c428e1fe4901ef33eff251f289de2716395cad20bf309b";
    }

    @Override
    public void run(Event tempEvent) {
        PlayerKilledSkyBlockMobEvent event = (PlayerKilledSkyBlockMobEvent) tempEvent;
        SkyBlockPlayer player = event.getPlayer();
        player.sendMessage("a");

        if (!player.hasTalisman(this)) return;

        player.setHealth(player.getHealth() + 5 > player.getMaxHealth() ? player.getMaxHealth() : player.getHealth() + 5);
        player.sendMessage("b");
    }

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerKilledSkyBlockMobEvent.class;
    }
}