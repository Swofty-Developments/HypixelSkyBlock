package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCMinikloon extends SkyBlockNPC {

    public NPCMinikloon() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Minikloon", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "b1LoQqiHDCmpZDZD1WnNeK93IT5Svy2Zz1qZHP06mLo+yqe7452jTwRgPQue2Rb2E8nc+bvAIJKhmXx9JCcSNuoCL3NNmI5ujj5mfYfDF25AZo6ShwTk+O6yIUXuumAruOgGoxJnXNvrgXYCEU3AhdcNDoUsx01zcVnC3keZdUIA6gDV0Och8clNtQtO2pRHZDXmWSOHAeRETN+Jd++7oITYkLJCp4MXqw0dpbLMP2YMq4KsfQoM6QSZg1gHyizEPVD1q57jn6wfwyidN8N8oaOvTZ+dRoXP2TkrRtx9FfDnuVlOMu14gUx9FRhIY2biEA8pb4cPis9odRk0yUx6rIgySeZuiaV3HZh2OGAd1FhIqg+GijqbyO5u8NcPbTZAQ99eeqqNGVMRLbJW/IYl2iNWj6mtN4LXgforTo2fc/rNwgigve2PnO1avGnymPfq516fQRDrl6AyDI+QIa2lbKWNNiWMXqpl/ad/PnFKpbYh6Mmpu5lOVwYKScb1EpLEve/TFVpG/y/wNlWQqdVIJFnKn8XCmlMvz1jobuj9Adwxn1C60AGL++BFgfsv+hlcmVk7igJ1MHnP3Qujn+rKfQzER37Qi14iZNqEWNH142fTG8raJ0bjzo5cP7n6KEzJDGnQcsAdsZnr8+pbJwPSDygaL19Mwzq1LT95m3OwLrA=";
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxMjU4Mjk3MDEwOCwKICAicHJvZmlsZUlkIiA6ICIyMDkzNGVmOTQ4OGM0NjUxODBhNzhmODYxNTg2YjRjZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNaW5pa2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJiZjk1ZDIyZTE1MmQ4YTNhMDQ3NjQ0NGM0MTE0MDQ0OTAzYjkzN2E2NzlhMDBiMTJiMmYxNzRhMTA3NTQ5OSIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position() {
                return new Pos(83.5, 72, -117.5, 0, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        e.player().sendMessage("§cThis Feature is not there yet. §aOpen a Pull request at https://github.com/Swofty-Developments/HypixelSkyBlock to get it added quickly!");
    }

}
