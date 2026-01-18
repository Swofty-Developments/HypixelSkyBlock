package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.hub.gui.GUIShopAdventurer;
import net.swofty.type.generic.data.datapoints.DatapointToggles;

import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.hub.gui.GUIShopLibrarian;

public class NPCAdventurer extends HypixelNPC {
    public NPCAdventurer() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Adventurer", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "axkpnuCr04Z7Pq4o15BsBhinft3CuUs2zU6moSYFAYf5bNlcvXjOwl82QBdkAEURGjJ3ps6jIUpu9JCd8FOnKRrF0VUV82PM/cI/uqySXoBj7tYj2qoTCQpVjtUM8CqXl8pPLYW9YjItUepqCQSoraDKeIpUx6j91laF7narlQPgPofdEjten/KbQUqPnIZDokja50S9eoU24aSLV8Ld7UvFDTQdIRJxHMyMHEjgAI1Ven2omHrsMFNdGN1VfuH7fKYFjDaCFjBAnwgvBkfKkLSG6hPz1hpBbBa1GzodvdwLH9fN10knHGmxUWENnd7ex+Mlmy8G1Wu8Mu/SFrtkbLAOWMnyDenUD92XhbQFSUlABrKRxOppOf7ctWlCk/pO9HjKnmf6N07hcI0Hvrl2ZKqC64bN91m7j9crj6jxDlb1xJGyBqXIzQZEcF1TdjI7FFoe4f6xxRDHqfsIgkmhAnTz+Oc8U73k0XZ1JdKMIVL8RCxQU1nqtNt9LUPgZ7XUrcrLjrzkS4odKZKuhyD/EDwfIKJOqjv9FDIuN91xEwb/xvOBmqoqOOROQKqpb/If9me62V/vYecN++R16uW/XELFijVe5bjqFOXxNBvBFFlbRlPLP2rQ//bT/0vUztUn2l87lM34Wc3yktarToUEtiSxubTPz9cg7uLUgI/76mA=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYzODA4ODEwNTc1NywKICAicHJvZmlsZUlkIiA6ICIyMjI3ZDM4ZmRkYTA0OGJmODA1MTFlNzlmNTBlOWVhYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJtYXBkYWRkeSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mMzA3YjAyMmE4ZDQzY2JlMDU4MjEwMzQwZGMyZDVhYTVkZDRhNGQyOTlhM2E5YjUyMzhmYWJjZDFiYzMwZjc2IgogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-41.5, 70, -64.5, 180, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        HypixelPlayer player = e.player();
        if (isInDialogue(player)) return;
        boolean hasSpokenBefore = player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_ADVENTURER);

        if (!hasSpokenBefore) {
            setDialogue(player, "hello").thenRun(() -> {
                player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_ADVENTURER, true);
            });
            return;
        }

        player.openView(new GUIShopAdventurer());
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return new DialogueSet[] {
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "I've seen it all - every island from here to the edge of the world!",
                                "Over the years I've acquired a variety of Talismans and Artifact.",
                                "For a price, you can have it all!",
                                "Click me again to open the Adventurer Shop!"
                        }).build(),
        };
    }
}
