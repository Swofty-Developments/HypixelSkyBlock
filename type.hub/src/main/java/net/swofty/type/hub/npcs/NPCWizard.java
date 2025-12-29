package net.swofty.type.hub.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCWizard extends HypixelNPC {

    public NPCWizard() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§9Wizard", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "g0olsK9Lb7jcCz90QZbmdSRYIqhFpLH7m0dr986QcsenqZT6eu5vVdWp8DjV+vvPJaMdhRDm6YL565r785bqBDL5nlKiEcP91Z/Y4RlP4mDjv5T17LC0YBF12wirr7kVdYsYsD95xqTiXnn9+nZjdZBi9kIwFIoDUz3bdod8EE4x1ntO+IWXTJu5I94tp3a1YxzAespCbIcQYbXNQLYKyvlh/YhHiyIMQ1AaDM+8wHIRmzD25J3ypsBz5FxdcuOeMmDPXCkWc79ScU+aHjcOHtnu7WdaQWoFrLyZfK7Zab6dxNeyw0VC215vurUXzOl1Su/AAg+OMp6G1Udj/Cn7jVxbOfofVpvG5DkdQW2q/8y88aZpxBoBpjJlBAqe+y1McFL0+OFhaVZszdoi4hND1uzScJ0nnn/zWE2ma2eGlbjU4FLUEQoeDnSJ569Dh8AXur5fwxcR6TULfw0E5Ds1qSg/Obo3X9lIejeA2XAjOC/msaD1X2setJLDk3yVL5iNDfStQ4Xxxu0xl0yxkD3Ryw32AEwt3nMhuq2cMIzQxDXlRmDRh2Dp6OGMlNICILOpxCuiuHAUpZM/wJGLgS7Ml2iwmsbriiAtGQKREIHI34UJIhCGxWMD299kAzSPR+XCaf9cnpEDdEj69IR/MH3O3JeuIqNawSovX3F6ketL6QU=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU5MDY5NTcyMzk2OCwKICAicHJvZmlsZUlkIiA6ICI5MThhMDI5NTU5ZGQ0Y2U2YjE2ZjdhNWQ1M2VmYjQxMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCZWV2ZWxvcGVyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2YwMTNmOWNlNmEzMTc5MTFmMjE5YmRhNWM1NjdjZDVjZTMzNDJmMTQxYWQ2Y2YzMDU1YjNhMmVmMWVmMGNkYSIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(46.5, 122, 75.5, 130, 0);
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
