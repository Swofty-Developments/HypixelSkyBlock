package net.swofty.type.hub.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class NPCJax extends SkyBlockNPC {

    public NPCJax() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"§9Jax", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "WkNbkv4F5IKaJsWVscT7vxmBlYqJZvsyobQvQKRbgsCxNbW+nN0B3lWLwnk2CNnlMkUvYZfqjCuiOKH/IRbP9IKTRxw2yU8eel8a7IWuQtB4UlNHLVfgjmbC5EPdBRKKF0QGJcBYLIeKOXKAoVS12RA9CsNl0k1PLRZ0+vHA+M208djgySS8K+DN/JluZchF5jx0io9KxyygLZToAsD5/DLB1pkiPhaqYX6COrBg3malhHBg2xwP3KSXHAmVWx2HUTByZoJOnsGy9GbGVBd7bFOKO3Pp8+3PqxBVg7vL2Hy69ZjqiwGtvzrkGc1P+RI92ZwGkXpe+vi8fmF+E2ZleOt0PpoYbGYRI0GehCVRiPMJo206Axsh385AHCvjNClGhO1vyBLqDonYkkQJQKvH7BbjVeQ0U5T8d6f8PibHwt0UWum8s/IG0w8Lglj5usMBNS0RdKPDSeBRIt2f0cMaLRvyI6KORMARQ8dsPs/JL8qkzvOjIBIzYWkLB6IQkW2f1IdVOCbb7rjk3lE1GdY1VKEnt5scznBXVIm40hWJvly4e1kQSV8mXlqPavSpHR7nsrhaMd/g2TZplwUPdRIE97ZqM0fHdpDds7rfL0Z+LoleKUGTxib3VBwILQ9///rfljL4f5fl94fUdFjLuMAm1x7jwxuOvq3jX76dAMiJlB0=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYyNTcyODU3NTQyNiwKICAicHJvZmlsZUlkIiA6ICI0NWY3YTJlNjE3ODE0YjJjODAwODM5MmRmN2IzNWY0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJfSnVzdERvSXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWRiODUwMzIxNDYxMDZiMTlmMGJlZDA2Y2FhMDNhYjk5OWYwODA3OTY3NjgwNGU2ZTBkZjJmZjFkMjhlMmNiMSIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(5.5, 61, -134, 90, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        e.player().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Swofty-Developments/HypixelSkyBlock")));
    }

}
