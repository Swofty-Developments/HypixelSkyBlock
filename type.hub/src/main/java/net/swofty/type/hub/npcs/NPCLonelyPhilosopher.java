package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.hub.gui.GUILonelyPhilosopher;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

import java.util.stream.Stream;

public class NPCLonelyPhilosopher extends NPCDialogue {

    public NPCLonelyPhilosopher() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"§9Lonely Philosopher", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "SGkbMOhgmZcqP0OQuKKcdNNxSjQr269u0hxroC5JEFM596JM8YMqFZCUnoIuetZVvBhdTktyKe2fVchEhFhNpOwLhSrUHWLDDGUvnBirPp9g8iDmntiRFL9D6WzI0srWQYY7jIYjIeY3j+D3nAbAiYosDS/eZbXDaa2JS0Xt30l6DqUzgxKBolzlhkwEWXVVXSmIoz8tGOlcn5STMDu/vmGY4j/Bh6dWQnXoMUGVcRBHD5P+5/ajutixuxB4ELl1LCptFcNJHsNMvV2KOmorUCT+xH9cEirrx3r38DGGCzSzBMIWQvgDYcFTpwFHyCZgL6e6atLP3DReYwpWEp0Mr9sRatVY/2IPoLcByRiJjufo931La5E5+3uagNkA8qQ8+O1mWMUNv1qEbCkPvVo9VQkJSL282DERvJPk0A5k9SK1jV+ecEkVHr6XSq6mmz8I8f+5NZNtd41xhJp6AAbr6AWMuP1+9aP60xfQK6Qh2Kephz06ya1afDnYdxDk7HDvjXFkDwC4P4EaR1KD0ouIUngG/cyjPytmhxxNLsPW9Ag+/3Hk62meVMIl0K42P+rYGg+scL/glJHgtZiR7P5pAvjc8vuW4TM30ZzZ9vAScfhTBAolYe5dx12X9IjFIiPw6gibo7Mf7FtfhVoTgYhYx6hFLhxlfhSuKCyKTGCyUYU=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "eyJ0aW1lc3RhbXAiOjE1NTUyMjk2MzE5MjgsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjAwOGRjYzg5YjdkMTQ4NzBmMzJhMjg5NjI5M2Q3NzhiMGU1MGQyNWZjNGRiNDRhMTBiYWI5ODAwMjFiYWRiNiJ9fX0=";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(-250.7, 130, 41.2, 40, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        if (isInDialogue(e.player())) return;

        Rank rank = e.player().getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue();
        if (rank.isEqualOrHigherThan(Rank.MVP_PLUS)) {
            setDialogue(e.player(), "open_shop").thenRun(() -> {
                new GUILonelyPhilosopher().open(e.player());
            });
            return;
        }

        setDialogue(e.player(), "hello");
    }

    @Override
    public NPCDialogue.DialogueSet[] getDialogueSets(SkyBlockPlayer player) {
        return Stream.of(
                NPCDialogue.DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "§fI'm sorry, I have nothing for you."
                        }).build(),
                NPCDialogue.DialogueSet.builder()
                        .key("open_shop").lines(new String[]{
                                "§fTo fast travel or not to fast travel?"
                        }).build()
        ).toArray(NPCDialogue.DialogueSet[]::new);
    }
}
