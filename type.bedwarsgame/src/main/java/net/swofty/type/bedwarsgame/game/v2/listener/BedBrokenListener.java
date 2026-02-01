package net.swofty.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.events.custom.BedDestroyedEvent;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class BedBrokenListener implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onBedDestroyed(BedDestroyedEvent event) {
        BedWarsGame game = TypeBedWarsGameLoader.getGameById(event.gameId());
        BedWarsMapsConfig.TeamKey teamKey = event.teamKey();
        if (game == null) return;
        if (game.getReplayManager().isRecording()) {
            game.getReplayManager().recordBedDestroyed(teamKey, event.destroyer());
        }

        game.broadcastMessage(Component.text(teamKey.chatColor() + "Team " + teamKey.getName() + "'s §cbed has been destroyed!"));

        for (BedWarsPlayer player : game.getPlayers()) {
            player.playSound(Sound.sound(Key.key("minecraft:entity.wither.death"),
                Sound.Source.MASTER, 1f, 1f), Sound.Emitter.self());
        }

        game.checkWinConditions();
    }

}
