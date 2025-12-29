package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointRank;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.hub.gui.GUILonelyPhilosopher;

import java.util.stream.Stream;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCLonelyPhilosopher extends HypixelNPC {

    public NPCLonelyPhilosopher() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§9Lonely Philosopher", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "SGkbMOhgmZcqP0OQuKKcdNNxSjQr269u0hxroC5JEFM596JM8YMqFZCUnoIuetZVvBhdTktyKe2fVchEhFhNpOwLhSrUHWLDDGUvnBirPp9g8iDmntiRFL9D6WzI0srWQYY7jIYjIeY3j+D3nAbAiYosDS/eZbXDaa2JS0Xt30l6DqUzgxKBolzlhkwEWXVVXSmIoz8tGOlcn5STMDu/vmGY4j/Bh6dWQnXoMUGVcRBHD5P+5/ajutixuxB4ELl1LCptFcNJHsNMvV2KOmorUCT+xH9cEirrx3r38DGGCzSzBMIWQvgDYcFTpwFHyCZgL6e6atLP3DReYwpWEp0Mr9sRatVY/2IPoLcByRiJjufo931La5E5+3uagNkA8qQ8+O1mWMUNv1qEbCkPvVo9VQkJSL282DERvJPk0A5k9SK1jV+ecEkVHr6XSq6mmz8I8f+5NZNtd41xhJp6AAbr6AWMuP1+9aP60xfQK6Qh2Kephz06ya1afDnYdxDk7HDvjXFkDwC4P4EaR1KD0ouIUngG/cyjPytmhxxNLsPW9Ag+/3Hk62meVMIl0K42P+rYGg+scL/glJHgtZiR7P5pAvjc8vuW4TM30ZzZ9vAScfhTBAolYe5dx12X9IjFIiPw6gibo7Mf7FtfhVoTgYhYx6hFLhxlfhSuKCyKTGCyUYU=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "eyJ0aW1lc3RhbXAiOjE1NTUyMjk2MzE5MjgsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjAwOGRjYzg5YjdkMTQ4NzBmMzJhMjg5NjI5M2Q3NzhiMGU1MGQyNWZjNGRiNDRhMTBiYWI5ODAwMjFiYWRiNiJ9fX0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-250.7, 130, 41.2, 40, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        if (isInDialogue(e.player())) return;

        Rank rank = e.player().getDataHandler().get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue();
        if (rank.isEqualOrHigherThan(Rank.MVP_PLUS)) {
            setDialogue(e.player(), "open_shop").thenRun(() -> {
                new GUILonelyPhilosopher().open(e.player());
            });
            return;
        }

        setDialogue(e.player(), "hello");
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "§fI'm sorry, I have nothing for you."
                        }).build(),
                DialogueSet.builder()
                        .key("open_shop").lines(new String[]{
                                "§fTo fast travel or not to fast travel?"
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
