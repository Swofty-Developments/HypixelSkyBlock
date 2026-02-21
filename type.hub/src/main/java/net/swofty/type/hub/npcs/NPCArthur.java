package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.stream.Stream;

public class NPCArthur extends HypixelNPC {

    public NPCArthur() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Arthur", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "ph8zOGN/cLhHpRWWG6YOFEC4SyDqgCi5WjutZUXWdP1bBSUZLhYBMp8oMOS/wlnt5MIc8RW7Gy3feu26BpiFthpwYXEytK7pqRbszY+TvjlhA496oiEtWliu+MepMQPe4fODyNaRwfis5N2VvbzoKQU2DJ0PZvijrQufe6z7Rai2LesFQeZj3LC4UMqQkyJWkZ8VAMSvvldbMdvZ7osZSGgOVBYi97KyRvgAixxADBNlKpdGmJCqJ3GFEA02enI6VisrsT4Qe1lcSURUu9gUt4PwCGZBN3o/1AJV4R8/QUHIWz+3cx1oWy2MEmLBWIPO6SLqWdyXb+g3IPlQpZGaEINNXHhVsubGscB5fpQzqJCYgX/dHAGffCZLgihjB1eodSvQHV4l+ByQRzp1pf8hHMNBwPXwNleAPYaULeHrEsgpcc4KRzsYWxDn49foTDsUnwmjinZrU7w9G1a/lpMb2HrMDX0gu5P8hYHzfOLgMgm7lNOw9KLqoWVH32vK7aBrlCrEVBXHo2Iv5KR5esvjdNE5prdXj+JoGtLPod2KM5LFsUWLu9D3eRVN3FkxmKWAL7YVVxZNcpZmPRzZllx4H6rNAy9dUrwNkclkyJcACKrs0AOehRjWNfSfhd376MaTC/HegeTPvPQ7tppj/VOb0EbmNcXqL0+Oqp+9SoROZ8U=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "eyJ0aW1lc3RhbXAiOjE1MzY3NTkzMzY3NjQsInByb2ZpbGVJZCI6IjQ2N2NmOTRkY2UyYjQ1ZTY4YmRhNTJlNTUwMmU3M2U4IiwicHJvZmlsZU5hbWUiOiJMaWxpeWFfIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jYzJlODE5OTA2ZWIxNzk0MzliOGRkNTU1MTEzMmU1NGViNDcxNzNlMGY0NTg4NjFhZDJhOGM5Mzc5MTgzODkxIn19fQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(53.5, 72, -111.5, 55, 0);
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

        setDialogue(e.player(), "dialogue-" + (int) (Math.random() * 7 + 1));
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("dialogue-1").lines(new String[]{
                                "Fuel makes your Minion work harder for a limited time",
                                "There are multiple types of Fuel, like Coal, Enchanted Bread and many more!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-2").lines(new String[]{
                                "If you place your Minions poorly, they will complain!",
                                "Open their Menu and look for their Perfect Layout!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-3").lines(new String[]{
                                "Once you unlock a Minion, you also unlock all their Level up recipes!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-4").lines(new String[]{
                                "Use a §aBudget Hopper§f to make your Minions automatically sell their work once full!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-5").lines(new String[]{
                                "My favorite Minion Upgrade is the §aCompactor§f, sooooo useful!",
                                "I have one in my Minion, look!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-6").lines(new String[]{
                                "People always empty the storage of my Minion when I'm not looking!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-7").lines(new String[]{
                                "Minions always have 4 Upgrade Slots where you can place Fuel or else"
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
