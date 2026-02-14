package net.swofty.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.party.FullParty;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.event.PlayerPostJoinGameEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.party.PartyManager;

import java.util.Random;
import java.util.UUID;

public class PlayerJoinGameListener implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onPlayerJoinGame(PlayerPostJoinGameEvent event) {
        BedWarsPlayer player = (BedWarsPlayer) event.player();
        BedWarsGame game = TypeBedWarsGameLoader.getGameById(event.getGameId());

        BedWarsMapsConfig.Position waiting = game.getMapEntry().getConfiguration().getLocations().getWaiting();

        if (player.getInstance() == null || player.getInstance().getUuid() != game.getInstance().getUuid()) {
            player.setInstance(game.getInstance(), new Pos(waiting.x(), waiting.y(), waiting.z()));
        }

        player.setEnableRespawnScreen(false);
        player.setFlying(false);
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().setItemStack(8,
            TypeBedWarsGameLoader.getItemHandler().getItem("leave_game").getItemStack());

        BedWarsMapsConfig.Position spec = game.getMapEntry().getConfiguration().getLocations().getSpectator();
        player.setRespawnPoint(new Pos(spec.x(), spec.y(), spec.z()));

        player.setGameId(event.getGameId());
        String randomLetters = UUID.randomUUID().toString().replaceAll("-", "")
            .substring(0, new Random().nextInt(10) + 4);
        player.setDisplayName(Component.text(randomLetters, NamedTextColor.WHITE, TextDecoration.OBFUSCATED));

        FullParty party = PartyManager.getPartyFromPlayer(player);

        for (BedWarsPlayer p : game.getPlayers()) {
            boolean shouldObfuscate = true;
            if (party != null) {
                shouldObfuscate = !party.getParticipants().contains(p.getUuid());
            }
            if (shouldObfuscate && p.getUuid().compareTo(player.getUuid()) == 0) {
                shouldObfuscate = false;
            }
            String name = shouldObfuscate ? "§k" + randomLetters : LegacyComponentSerializer.legacySection().serialize(player.getColouredName());
            p.sendMessage(name + " §ehas joined (§b" + game.getPlayers().size() + "§e/§b" + game.getMaxPlayers() + "§e)!");
        }
    }

}
