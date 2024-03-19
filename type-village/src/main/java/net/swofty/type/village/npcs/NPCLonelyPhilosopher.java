package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCLonelyPhilosopher extends SkyBlockNPC {

    public NPCLonelyPhilosopher() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Lonely Philosopher", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "SGkbMOhgmZcqP0OQuKKcdNNxSjQr269u0hxroC5JEFM596JM8YMqFZCUnoIuetZVvBhdTktyKe2fVchEhFhNpOwLhSrUHWLDDGUvnBirPp9g8iDmntiRFL9D6WzI0srWQYY7jIYjIeY3j+D3nAbAiYosDS/eZbXDaa2JS0Xt30l6DqUzgxKBolzlhkwEWXVVXSmIoz8tGOlcn5STMDu/vmGY4j/Bh6dWQnXoMUGVcRBHD5P+5/ajutixuxB4ELl1LCptFcNJHsNMvV2KOmorUCT+xH9cEirrx3r38DGGCzSzBMIWQvgDYcFTpwFHyCZgL6e6atLP3DReYwpWEp0Mr9sRatVY/2IPoLcByRiJjufo931La5E5+3uagNkA8qQ8+O1mWMUNv1qEbCkPvVo9VQkJSL282DERvJPk0A5k9SK1jV+ecEkVHr6XSq6mmz8I8f+5NZNtd41xhJp6AAbr6AWMuP1+9aP60xfQK6Qh2Kephz06ya1afDnYdxDk7HDvjXFkDwC4P4EaR1KD0ouIUngG/cyjPytmhxxNLsPW9Ag+/3Hk62meVMIl0K42P+rYGg+scL/glJHgtZiR7P5pAvjc8vuW4TM30ZzZ9vAScfhTBAolYe5dx12X9IjFIiPw6gibo7Mf7FtfhVoTgYhYx6hFLhxlfhSuKCyKTGCyUYU=";
            }

            @Override
            public String texture() {
                return "eyJ0aW1lc3RhbXAiOjE1NTUyMjk2MzE5MjgsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjAwOGRjYzg5YjdkMTQ4NzBmMzJhMjg5NjI5M2Q3NzhiMGU1MGQyNWZjNGRiNDRhMTBiYWI5ODAwMjFiYWRiNiJ9fX0=";
            }

            @Override
            public Pos position() {
                return new Pos(-250, 130, 41, 90, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        e.player().sendMessage("§cThis Feature is not there yet. §aOpen a Pull request at https://github.com/Swofty-Developments/HypixelSkyBlock to get it done quickly!");
    }

}
