package net.swofty.type.hub.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCAnita extends HypixelNPC {

    public NPCAnita() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§9Anita", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "ATQXzIxoDp9u5MJssz6Xaz7rvMTjtRwQZMt8JQG1O+YDEN7eeLNX+3heu1gvjYbeGGnLKoUwhgkG+IiYURB27mBccEITUJCE2E4s5svbPsy82tYvcGEBnqur5oUoHkVxLhEGk4/U5UekMVoseCOsfia+iEiVzyhTqZ+FqDZT30nay7krLkznWJN7M0v2AWj41922eMlPsa9v58tDhCJCAAC4EdB5XfPVXN5V3rTfwA4SuVL3rm1z1mHcx8jwCxRKzYWzFhH3FzdUd65TXsFv4P7Cn1y3b2Y2elWQuosS2bZPHh1jefQdVzH9URjRmWlmZqtTVDXKDvnulAqI/QSC+JRNG4k3AbvJPlzhFJ84fhyvSwlBs77/MzH+LL33G818hEtee9ypQStK8KGSZSkg5bdoVOj4jFaJfoDkAWHnfwdL/QvT8KhiEj5182ATLQi5eQDSy1jiUpl7KBfwd9yvD7gHKCZHVSEjN1CXbVcRnP4CZYkLtjTCI3zjyHbNdSzzfRHQcganeaDS75QpCo/4hKmPxZTL043CN4RbrIP7lHLttiWL7M7gFTKRQcY4qf7/QbnLF2/knK77wdgOepqyNbn+VdEVsm8Ev0EQA4aYMRdkOlPxr90EpyDUCH5k2rUYXCnGhz8SogRfdFacUuEeT2yPzQd2TkzDX7gNBsk2EjE=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTY1NzIxNTMwMTU5NCwKICAicHJvZmlsZUlkIiA6ICIzYTdhMDVjMDc0MTI0N2Q2YWVmMDMzMDNkOWNlMjMzNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJzcXJ0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk2YWNlOGNkYTA5ODQwMjE2Y2NkYmM2MDFlODRiZTk5ZTU5NmNjZWJjZDc3M2E4YTljZjRkNzhiYWNhNjQxNjMiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(23, 77, -69.5, 0, 0);
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
