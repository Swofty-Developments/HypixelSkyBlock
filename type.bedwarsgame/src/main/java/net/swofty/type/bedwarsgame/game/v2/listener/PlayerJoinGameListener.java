package net.swofty.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.tag.Tag;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.event.PlayerJoinGameEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

import java.util.Random;
import java.util.UUID;

public class PlayerJoinGameListener implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onPlayerJoinGame(PlayerJoinGameEvent event) {
        BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
        BedWarsGame game = TypeBedWarsGameLoader.getGameById(event.getGameId());

        BedWarsMapsConfig.Position waiting = game.getMapEntry().getConfiguration().getLocations().getWaiting();

        if (player.getInstance() == null || player.getInstance().getUuid() != game.getInstance().getUuid()) {
            player.setInstance(game.getInstance(), new Pos(waiting.x(), waiting.y(), waiting.z()));
        }

        player.setEnableRespawnScreen(true);
        player.setFlying(false);
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().setItemStack(8,
            TypeBedWarsGameLoader.getItemHandler().getItem("leave_game").getItemStack());

        // Set game ID tag
        player.setTag(Tag.String("gameId"), event.getGameId());

        String randomLetters = UUID.randomUUID().toString().replaceAll("-", "")
            .substring(0, new Random().nextInt(10) + 4);
        player.setDisplayName(Component.text(randomLetters, NamedTextColor.WHITE, TextDecoration.OBFUSCATED));

        for (BedWarsPlayer p : game.getPlayers()) {
            String name = p.getUuid().compareTo(player.getUuid()) == 0
                ? LegacyComponentSerializer.legacySection().serialize(player.getColouredName())
                : "§k" + randomLetters;
            p.sendMessage(name + " §ehas joined (§b" + game.getPlayers().size() + "§e/§b" + game.getMaxPlayers() + "§e)!");
        }
    }

}
