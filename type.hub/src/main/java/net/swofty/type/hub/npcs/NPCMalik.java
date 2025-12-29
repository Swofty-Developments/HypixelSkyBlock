package net.swofty.type.hub.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCMalik extends HypixelNPC {

    public NPCMalik() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§9Malik", "§e§lBLACKSMITH"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "apqCV7ME+n/tSQMWMXIrkAyQoDL3nJrvnXtwNIRiIpc5o1QRzsLK4fIhaDbtrTEt/qtwx+93pMRgFqp+CwkutYvc76u6gYybVTcfmDfIJyhacppqUgQiqUCPSCvg+3BvG1bHaRCM8Lrc6wRsFRqqDP22mzFqeY1o3O0+0RqClwKUYntnx1dS58l93rnm3dKKo5NOH0bnM+uZEKxCTTI5/IQwL4CAe1ntyqXPRy7QO5LEWBlA4vDwR60WesDCVdx4QV6mK8IO8DM5BqThszsLm3PMuMP61pnE12+7WleHOlEqC6xY+kDUgsfXZQgfJGqV67J1U+Hi3RSCj3EkokMh45+ZalXWaP+BlSjzzTvhZUP2nM/XLgpuC7k4F2hxOcPz5X2vmVqcjuaGVDa40J2qJ+vRwvMEFs+O9rlSTPxa6oVJiPD/aFFpMCJtqmoTkfbJKNVHQXhtJBYHkdxqAvAfUNnkd024CQNc8WYRnmovRxgx+i6pDaF8ADLFPIMaLZnUOJ2M8wwocuSpLT3qXTqOBHjA8IdWB+ekzyLDkqwWomMUoR06Ws1U6qHiKxJvnJz6MQ1Qu9mHB9tKCgL8wB4SK/2v/KvklGXVyeuim3P0sW5buuMH4SJNWKUdUYK8sZFN1grydmnZo2dILaXAXAcwisUROEUG/vT/WudTD+rV53A=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxMjAzNjg4MDAyNCwKICAicHJvZmlsZUlkIiA6ICI5MzZmMTA3MTEzOGM0YjMyYTg0OGY2NmE5Nzc2NDJhMiIsCiAgInByb2ZpbGVOYW1lIiA6ICIwMDAwMDAwMDAwMDAwMDB4IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2FkMjY1YjAwNGI0MTAwNTJmZjE1YmMwOWFjOGM0OGU4ZWYwY2E5YWE2NmYzN2I0NDY0NDE3NTY2N2YxNGY2MDYiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-80.5, 56, -119.5, 180, 0);
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
