package net.swofty.type.hub.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCGuy extends HypixelNPC {

    public NPCGuy() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§9Guy", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "lu9SuKpv/U8XqaZTkleKzPDg8S1pcqA7LSWiWimR9x0BnkpK5CkyLwkWA1AMKCibQZSMPjoFFySNMVRcIhylv3yN0V6/Y6moJi1/SmRIeJJL/FovCUykzTSvbWsqJXfRyoi+5mUt6REj6bvJQruNtCedIHQD5a0Mrw3d8LbvZ0OlGPUbaAv1O7dW1O2uxmxCDSWMOL8PN+6fb/zYgA/XeJvSj97LafK4YAeb1YV362CeMkhmMP0uE5wj11+BnexEN+WaBzbRIUlBuSMB+Pw+7RoS4Nk7kxxKSNAR/pzlSqFHLkTlL88ljrLeyEooccpETSuqLh55/wsWSdesEDpSNjmfRYVX9EXOk783VRz3Btb+MItjiqmos5Mgmjelnx34utIPkAFbLyn/AUvWaNImxhWw/iDFYod+C/QNbUqR/H9ahIHzZXun4+6tKhVBgaCfLqaqF+V9Js8miapUpW16EEnElTNJ843+/HFgqex18q2vCTUX0tixtzHrFmwhhbBnT02DSvbvIxm9ucyNMwTpYhJ33I433pB67i1iQxiNBxaTTVSn2bGs4AKLgOjkTg3TsixEix02fCOzFl8bau/JlZMDmk7/2SAI74VRnreBVTEHjIAb7SRRXNy+zOxQJLzyMB+TwpGBBIUbNpCgjKu0aqu+Ld/FOO37dvBke8bv7Uw=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU5OTQ1NzU3MjI1NCwKICAicHJvZmlsZUlkIiA6ICI2MTI4MTA4MjU5M2Q0OGQ2OWIzMmI3YjlkMzIxMGUxMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJuaWNyb25pYzcyMTk2IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U5ZTIzYmU3ZjA0NTU2ZmEzMzM1MWE0Yzc3MWEzZjA1ZjRhNmQyN2RlNDEzYTM2ZDAyMzBjNjFmNzE2OTg3OTkiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(51.5, 79, -13.5, 180, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        e.player().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
                        .clickEvent(ClickEvent.openUrl("https://github.com/Swofty-Developments/HypixelSkyBlock")));
    }

}
