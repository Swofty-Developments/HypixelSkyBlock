package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointMuseum;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointString;
import net.swofty.type.skyblockgeneric.entity.npc.NPCParameters;
import net.swofty.type.skyblockgeneric.entity.npc.SkyBlockNPC;
import net.swofty.type.skyblockgeneric.gui.inventories.museum.GUIYourMuseum;
import SkyBlockPlayer;

import java.util.UUID;

public class NPCMuseumDisplay extends SkyBlockNPC {
    public NPCMuseumDisplay() {
        super(new MuseumNPCParameters());
    }

    private static class MuseumNPCParameters extends NPCParameters {
        private DataHandler handler = null;
        @Override
        public String[] holograms(SkyBlockPlayer player) {
            if (handler == null) {
                DatapointMuseum.MuseumData data = player.getMuseumData();
                UUID currentlyViewing = data.getCurrentlyViewing().playerUuid();
                UUID profileUUID = data.getCurrentlyViewing().profileUuid();

                handler = DataHandler.getProfileOfOfflinePlayer(currentlyViewing, profileUUID);
            }
            String username = handler.get(DataHandler.Data.IGN, DatapointString.class).getValue();
            String profileName = handler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();

            return new String[] {
                    "§b" + username,
                    "§a" + profileName
            };
        }

        @Override
        public String signature(SkyBlockPlayer player) {
            if (handler == null) {
                DatapointMuseum.MuseumData data = player.getMuseumData();
                UUID currentlyViewing = data.getCurrentlyViewing().playerUuid();
                UUID profileUUID = data.getCurrentlyViewing().profileUuid();

                handler = DataHandler.getProfileOfOfflinePlayer(currentlyViewing, profileUUID);
            }
            return handler.get(DataHandler.Data.SKIN_SIGNATURE, DatapointString.class).getValue();
        }

        @Override
        public String texture(SkyBlockPlayer player) {
            if (handler == null) {
                DatapointMuseum.MuseumData data = player.getMuseumData();
                UUID currentlyViewing = data.getCurrentlyViewing().playerUuid();
                UUID profileUUID = data.getCurrentlyViewing().profileUuid();

                handler = DataHandler.getProfileOfOfflinePlayer(currentlyViewing, profileUUID);
            }
            return handler.get(DataHandler.Data.SKIN_TEXTURE, DatapointString.class).getValue();
        }

        @Override
        public Pos position(SkyBlockPlayer player) {
            return new Pos(-22.5, 67, 80.5, 90, 0);
        }

        @Override
        public boolean looking() {
            return false;
        }
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        new GUIYourMuseum().open(e.player());
    }
}