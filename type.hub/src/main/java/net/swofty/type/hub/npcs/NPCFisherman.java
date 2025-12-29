package net.swofty.type.hub.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCFisherman extends HypixelNPC {

    public NPCFisherman() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§9Fisherman", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "hpEaxJJ2efngrs7TlipgMRqtWmjF0zURt/rhM7rZTpDSXgKE/1RUQ+w5xgPvJqa1IrjBgzqPPAUcFkFnalkdvK9Ni6UGicbcYEnWER2GcuEYJW6dRh89lwZLmWRTx4useP2WJjlPlVO76PIdfKS1GZVhSlXcKm1rbY0BHFUai8rsL25KqZh18eoGkcFkBZ2DUK0XzejT6o2NV8hXm+GgIJEZ5VXnQaMJrXHJXPy9x+YV4AFI4ChFciys8pngWFCwoBpamWgvKaFkAyFBuEyi/9+Nq2IN0Y3quVa0JVe488dNwwvuoPHvBl3Pqlkl42RD85gYqG3RwxpPgFF7dHyr3/U/DZOIQ7hL3pqDaWSBzPoe16UpC8Spb6AgF4wIWbGikkD0zCfA2BEckUoaoZ+ja4FNlHECwXK5zVEmRQfT8Buhf2oQrQA+wqdd6UxZ5exlkzllM3Wqq5W7GFy7AEt7e9hIRQCdtXrNwAwb3TsalOv1jMREet4LgBzNp2sz488lAYmApbK2Y1lMO2f0hTbrXMMcCXDlxhBqt16Sze6O4K89/Xn36B9oekTBLKN+SPO0l2cZqjyai8eTNgLZFFeTm8gjNjFWkwq/hGIlNAKzcQZl3QRcflboOQo2YBb+G9d8l5eI5qvWFpz5/nM29Zg3ZPOUuUoPHmCjUFb63yc6azg=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "eyJ0aW1lc3RhbXAiOjE1ODU2MzUwMzQyNzEsInByb2ZpbGVJZCI6ImUzYjQ0NWM4NDdmNTQ4ZmI4YzhmYTNmMWY3ZWZiYThlIiwicHJvZmlsZU5hbWUiOiJNaW5pRGlnZ2VyVGVzdCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWFkMmJjM2Y1ZGUxM2JkMjViOWEyMzg5MjEzMTFkZDk5ZGQ1NTc1MmI3MGE1Yjc2ZDA5ZjQ0ZjVlY2UwY2RiNCJ9fX0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(155.5, 68, 47.5, 120, 0);
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
