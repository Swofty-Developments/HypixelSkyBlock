package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointMuseum;
import net.swofty.type.skyblockgeneric.gui.inventories.museum.GUIYourMuseum;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.UUID;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCMuseumDisplay extends HypixelNPC {
    public NPCMuseumDisplay() {
        super(new MuseumHumanConfiguration());
    }

    private static class MuseumHumanConfiguration extends HumanConfiguration {
        private SkyBlockDataHandler skyblockHandler = null;
        private HypixelDataHandler dataHandler = null;

        @Override
        public String[] holograms(HypixelPlayer p) {
			SkyBlockPlayer player = (SkyBlockPlayer) p;
			DatapointMuseum.MuseumData data = player.getMuseumData();

            UUID currentlyViewing = data.getCurrentlyViewing().playerUuid();
            UUID profileUUID = data.getCurrentlyViewing().profileUuid();
			if (skyblockHandler == null) {
				skyblockHandler = SkyBlockDataHandler.getProfileOfOfflinePlayer(currentlyViewing, profileUUID);
			}
            if (dataHandler == null) {
                dataHandler = HypixelDataHandler.getOfOfflinePlayer(currentlyViewing);
            }
			String username = HypixelDataHandler.getPotentialIGNFromUUID(profileUUID);
			String profileName = skyblockHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();

			return new String[]{
					"§b" + username,
					"§a" + profileName
			};
		}

        @Override
        public String signature(HypixelPlayer p) {
            SkyBlockPlayer player = (SkyBlockPlayer) p;
            if (dataHandler == null) {
                DatapointMuseum.MuseumData data = player.getMuseumData();
                UUID currentlyViewing = data.getCurrentlyViewing().playerUuid();

                dataHandler = HypixelDataHandler.getOfOfflinePlayer(currentlyViewing);
            }
            return dataHandler.get(HypixelDataHandler.Data.SKIN_SIGNATURE, DatapointString.class).getValue();
        }

        @Override
        public String texture(HypixelPlayer p) {
            SkyBlockPlayer player = (SkyBlockPlayer) p;
            if (dataHandler == null) {
                DatapointMuseum.MuseumData data = player.getMuseumData();
                UUID currentlyViewing = data.getCurrentlyViewing().playerUuid();

                dataHandler = HypixelDataHandler.getOfOfflinePlayer(currentlyViewing);
            }
            return dataHandler.get(HypixelDataHandler.Data.SKIN_TEXTURE, DatapointString.class).getValue();
        }

        @Override
        public Pos position(HypixelPlayer player) {
            return new Pos(-22.5, 67, 80.5, 90, 0);
        }

        @Override
        public boolean looking(HypixelPlayer player) {
            return false;
        }
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        new GUIYourMuseum().open(e.player());
    }
}