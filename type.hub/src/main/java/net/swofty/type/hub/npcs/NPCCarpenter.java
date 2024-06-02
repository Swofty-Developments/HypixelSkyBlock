package net.swofty.type.hub.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class NPCCarpenter extends SkyBlockNPC {

    public NPCCarpenter() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"§9Carpenter", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "b7Z5Yu3WTa01ELIE8FfLvVIu4kyeKFscKRhtjbKlaIUJyaM1hcClDCmgjXcEUmn/47L57jMBYJAspmBhu1402XQkm+Au+6pUBNp1j9uSTAcec5IcpxqkRgOkXMx8HAoH5WTvoLFYwpC8Ff/PSK835WAKHYW4K3o13QcJrlAKenMHBcwXewNy6CNHoXbJwmCzoPTWDjWyE1f2DfKff5hp1WZUFwODOXKMJEFY1+DzWKAwvQSRse+O/89HdB4CMRKbfQnfDYu8m/VRx+86JopCu9PZZk5J5vGdfhrGxChY7H9Lp9I8QADgjU3rdAfbE7AXj95vU6MfyEyOuAxnvTXPHa+vFwTj//1eTJ0+vxfPibg6gqW5FktR8daSjxoOvLqGST1FDn/O3hC3rBm1If2Y9KJgGzDiw+7Loc+oNMprtPriajydwJlcKBrhKAQINDrw3v1xKhphuqSq/+cjoz4OAGPxSqlJ1rN6OvU7HluATXbPhRvJS8PeRaBSLYMqnfqH7o1M2gt5f5D1Wjf9WMVMjqVMBRGjWDcoEvTECW9HHdb8w7vG0uoH3yzdFvHu96Zs4ADDS5eIYXTUjglZDICS06vGm++5cVRq2z2lHmllFsP/KIZY93JhWdfjDm4cuASohQmN+TLznX8C1PInlB4kYnQKyoJiiRss0EoqGZUyMxc=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxMzU4MDg3MDAwNiwKICAicHJvZmlsZUlkIiA6ICI0ZTMwZjUwZTdiYWU0M2YzYWZkMmE3NDUyY2ViZTI5YyIsCiAgInByb2ZpbGVOYW1lIiA6ICJfdG9tYXRvel8iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M4ZDJhNWFmOTc1YzUzY2YwODFiNTMxNTY2MTQ4YmFlMzY2MTQxMzE0YjM1YWE1NzI2ZTk3Y2M5N2VmMTE4YiIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(15.5, 72, -21.5, 90, 0);
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
