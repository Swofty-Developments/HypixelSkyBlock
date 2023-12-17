package net.swofty.event.actions.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.Groups;

@EventParameters(
        description = "For mining",
        node = EventNodes.PLAYER,
        requireDataLoaded = true
)
public class ActionPlayerMove extends SkyBlockEvent
{
      @Override
      public Class<? extends Event> getEvent() {
            return PlayerMoveEvent.class;
      }

      @Override
      public void run(Event event) {
            PlayerMoveEvent e = (PlayerMoveEvent) event;
            SkyBlockPlayer player = (SkyBlockPlayer) e.getPlayer();

            if (player.getRegion() != null && Groups.MINING_REGIONS.contains(player.getRegion().getType())) {
                  if (!player.getActiveEffects().stream().map(f -> f.getPotion().effect()).toList().contains(PotionEffect.MINING_FATIGUE))
                        player.addEffect(new Potion(PotionEffect.MINING_FATIGUE, (byte) 255, 9999999));
            } else {
                  if (player.getActiveEffects().stream().map(f -> f.getPotion().effect()).toList().contains(PotionEffect.MINING_FATIGUE))
                        player.removeEffect(PotionEffect.MINING_FATIGUE);
            }
      }
}
