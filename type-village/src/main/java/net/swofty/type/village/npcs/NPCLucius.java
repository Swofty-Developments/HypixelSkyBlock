package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCLucius extends SkyBlockNPC {

    public NPCLucius() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Lucius", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "qIfu78CDQ8MWv7TQESfQK2JXWa5+r9QlbDSLSAE4V1U1uJBtrC9HKobg5iKneNai1D38kA4dVZXvEkWE6MC7+EqJ8Lp2RsSi54uqf7xsQcMSWZFA8PkrxP43tpQ6VI4qA0TBdLkK1xYUT/Hd3szBl6frFcIZi6jYqk3wMyjjBLnOW+E57JT/udd4xUBSFuFVH/vVCz/Pu5O3U0xUnwFbF1Hyeac2jZmXbGCvT1GxS8jfIWgeWjkMUdovbpYuoPmmyhykInomv5eVxCg6x3XgW9Ncv98+zkGW/R+wCIw4iY45WK53j6qzm3HFeyHaH8Md171taZo4muPAAL41Txm+f/xzz6hDqpguLC8konuw5oN2YLKXr5j7416jNie27JiOTHrGC42a9DV+IVOWtQYDGd0tus74NoKN7QFrZ8FIpuTqXPCr211b7PhyfADneHdXKls/yA0YUfnmztrk/TWiHcrze3RzuL9d7GHCsyBivuUTK/LqhTDH0N0mQSNEbScc/d10e6hzQpBYygzxEuidLGJRY/xHby7VN6CwPmZ+2UWcy/XMh49B5On8I5Y5QgpB56Kkv/xkP8AKuSLHxRjy4xWohOOaxXekduM3S9afRt5pc5uqPnX8sbUMVw5qeot2yueDdS9jUDgHMuR9usymnQEdTenIFGcHJpuKAMwVd5o=";
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxMzY2MDY1NTkzNCwKICAicHJvZmlsZUlkIiA6ICI5NGMzZGM3YTdiMmQ0NzQ1YmVlYjQzZDc2ZjRjNDVkYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVRdWFzb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRkODk1MWNkMjA5NzY4ZGI1NzE1YmRjNDQzNjIxYTU0NTJkMjVmNGViNWI2YTFjMGU3Mjg2YjE4NDI5NDFmOSIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position() {
                return new Pos(124, 73, 165, 90, 0);
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
