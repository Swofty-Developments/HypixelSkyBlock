package net.swofty.type.bedwarsgame;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.entity.PlayerSkin;
import net.swofty.commons.party.FullParty;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.party.PartyManager;
import net.swofty.type.generic.tab.CustomTablistSkin;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.tab.TablistSkin;
import net.swofty.type.generic.tab.TablistSkinRegistry;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class BedWarsGameTabListModule extends TablistModule {

    @Override
    public List<TablistEntry> getEntries(HypixelPlayer p) {
        BedWarsPlayer player = (BedWarsPlayer) p;
        BedWarsGame game = player.getGame();
        if (game == null) {
            return List.of();
        }

        Collection<BedWarsPlayer> players = game.getPlayers();
        if (game.getState().isWaiting()) {
            TablistEntry[] entries = new TablistEntry[players.size()];
            int index = 0;
            for (BedWarsPlayer bedWarsPlayer : players) {
                boolean shouldObfuscate = bedWarsPlayer.getUuid().compareTo(player.getUuid()) != 0;
                FullParty party = PartyManager.getPartyFromPlayer(player);
                if (party != null && !shouldObfuscate) {
                    shouldObfuscate = !party.getParticipants().contains(p.getUuid());
                }

                TablistSkin skin;
                PlayerSkin playerSkin = bedWarsPlayer.getSkin();

                if (shouldObfuscate) {
                    skin = TablistSkinRegistry.GRAY;
                } else if (playerSkin == null) {
                    skin = TablistSkinRegistry.GRAY;
                } else {
                    skin = new CustomTablistSkin(playerSkin);
                }

                String displayName = shouldObfuscate
                    ? "§k" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, new Random().nextInt(10) + 4)
                    : LegacyComponentSerializer.legacySection().serialize(bedWarsPlayer.getColouredName());
                entries[index++] = new TablistEntry(displayName, skin);
            }
            return List.of(entries);
        } else if (game.getState().isInProgress()) {
            TablistEntry[] entries = new TablistEntry[players.size()];
            int index = 0;
            for (BedWarsPlayer bedWarsPlayer : players) {
                Component displayName = bedWarsPlayer.getDisplayName();
                if (displayName == null) {
                    displayName = Component.text(bedWarsPlayer.getUsername());
                }
                String name = LegacyComponentSerializer.legacySection().serialize(displayName);
                TablistSkin skin;
                PlayerSkin playerSkin = bedWarsPlayer.getSkin();
                if (playerSkin == null) {
                    skin = TablistSkinRegistry.GRAY;
                } else {
                    skin = new CustomTablistSkin(playerSkin);
                }
                entries[index++] = new TablistEntry(name, skin);
            }
            return List.of(entries);
        }

        return List.of();
    }
}
