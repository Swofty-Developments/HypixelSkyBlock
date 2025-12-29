package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.hub.gui.GUIGeorge;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCGeorge extends HypixelNPC {

    public NPCGeorge() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§fGeorge", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "mJcNsdFtv5d2NTLPG5rdx9RcHmoEUzb10WT5sCeg43M7KX6FcicawmwzhLiDdqaCRZZ2uLRFVnQmXrewogMsuOQXDywQ5iugAJ1SqehRGTqqIXw1micRgk+7MElFuvQzU6NW8GrEBkzhS4MoJafrl3Gy8+6JSmvZFt7wHP1x1Ww5cBoDVHml7RWHmowHqbOOsCWILduWFJTU8+5RKmUD6tlR82lPKPBVjq/EgrYSmUH+weFYP/08mATalEEKUKBqgTh6dzhhTtUcxgH/k/wYPcTeXHYP3YNW4eRjL7rAsPbOY6lyDEEcJ3xEkRzrJJ0dIZ14NsN4lppPCP5v4FGP7tq5z9rKxqCrpm4vowzCfReVysZDlVZPbC/jRGjYNKfAUB4z5liy+S5mqTuvm1KY6s6X/l6+VtXg7GDfz/hthr8ElVevIdDzHNhsKzp+PWsA/UKLYUnmVEWmbl3Mk6CvLxkopXp3ro0QM9yZiAbo6fYoMSilfLZlvuwJq6NhdpmooXN/3F6LzVaYT+rj3yzHxYT+76zntTB/9VBAUYlKnw+4K9hotkFNKxCixUjVuHglMXu78mzC+hUKbNd5A0veytOHndbWSD9NebvApT385RohA9/S63qC3el2bgVeA+gAwnq222LMTq9RDR7+eUNm8hQFGMfmnW3nQzW2fw68OUo=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxNDYzNzIzOTg2MiwKICAicHJvZmlsZUlkIiA6ICJlYjc0ZDU5MjcxZjg0YjFlYTFiMWJhNzZhMTJkODg1OCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTZWVwZWQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTliYjEzNzM3OTc5Zjk4YmEyMzZlMzdhZTQ4ZmQ1ZGRkZWRiYTcxOWVjNjIwNjhlOGFjOTYyYWJmN2UwN2Q4MyIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(39.5, 71, -100.5, 25, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        new GUIGeorge().open(e.player());
    }

}
