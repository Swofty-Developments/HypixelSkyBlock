package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.hub.gui.GUIBiblio;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCBiblio extends HypixelNPC {

    public NPCBiblio() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Biblio", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "WzvBfN4RiZeuHYY5XTfJxMgMvQ8owbEv0V7ON2YO76Wyv2aCwnKuGr6vm58ndmY9/4ZcC7oSrdZYHU9UlMy//0B3d2aLdX1mN953lR3llumMK4DuiWK6eLevkRGnR/hgm0WfDd4/fIN6y7Exo9w0kWG4LlwlRmijXODWVcZFHBUMJS9pbtKlGmstzutUzcnyk38SlhvSOs9rk3Now8rl1u55VZ0wtaIeKdZRkcAIzY0LrjKWTB+IGD40PIw1ZAzae/nqryErVxzKzdSTZYy2Lym8Fe6Aw13abqKQpJyn9xK1upuFhGXsby/mWMm5qVn5ScUNTvoyVuYMuSTx4WtKbdUHLq9DOK+ocv2y6dwXgH3ybeAs/yQxMLRFGaRbz6cQSaIqanrLPFi1ruLzhSter+weTdZlW40zN8QeLuQysZF9IgPCFy5ILs97VJkglyRkbrno6JsWq+jeBFGae3U5a3MjnmUTa1Vm79sqWIhofRfcCDwYHlh3U5BohtsdnwysTZoY2iiWQTxXI1NtBmOzm69QitE40QFHd0OCvHQdw/f5qvjZ/Po7j6qAM6IYDyB5WlIMYvsOPuGj4ZxeEdWaVxbVsKdI4FOBfxBy6/53hWoEH1x+pyIhhC5E6khJK6zBrwX8RkA+iG31I07nrAOmL7kiZoeIP6ItnqxdM8kBkyY=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxMjEwMzI3NDA3MCwKICAicHJvZmlsZUlkIiA6ICIwZjczMDA3NjEyNGU0NGM3YWYxMTE1NDY5YzQ5OTY3OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPcmVfTWluZXIxMjMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzAxOTI4OTA1NTg5MGMxMjExYWNkMzcyNWE3MGYwNTE1NWU2ZWMxNmVhODMyNmMzN2Y0MWIyMTYxMjY2MWRiOCIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(14, 72, -106, 50, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        new GUIBiblio().open(e.player());
    }

}
