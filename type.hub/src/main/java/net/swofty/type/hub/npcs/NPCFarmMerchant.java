package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.hub.gui.GUIShopFarmMerchant;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCFarmMerchant extends HypixelNPC {

    public NPCFarmMerchant() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Farm Merchant", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "qRdj9NhBftgViWv6Ihdr48J0G9FDZIPh4s2X0dSBmnyvHujXKdgJL031wH0VaCerI9hbSuNVVqOuDuMWoW1LpYOMVO99fUx8ksXAUGo85gQQsRaYF5XN92gwYOuxLwhkLlDMj8h2EbyL7+3/rFysqC6b4ppV1N4uEgvkLRF7KzTGoB1geMw4uv4hZ9YpnhsY7adRT3d/ZY6KwygzqcncvZPdq2tW7f41vwYtdJ4IcGVk7+dnosd7+xkiYDYjWx0H6MGhsSdsqoIuMY4TwihsUDVlByteeEKyN3OAc39VyJsOvMUMEytTPwsYiWsVJ7D4YPV+xdnebMiGTJF9vLmRyIInhJpciBUEqMJz3k7AnkBrtF9oZ65mI0uvXM5PeHdQz7uMt4qbnXJaqy3u7VkC7ajrskHvWmKaEy0mHk1iBJcN/eq0gwVu21Hu3zFAGpeIJI92FnGLdHHcncNHri+TKSbpoBxeoM1oB22mPYW2v5iBX8Zb+YjWrkZX7BFkRLvjhNHy6uAGK5UsNIrXw8TqiBQsvcJ1NnNmkboUmo+iIQlL5bLMAa24ekQ3FVK7AjuqZDcrjPQwK4b6a82iLUK/dGxkXJRjO3/AzAtLwxM3pgG+P9aXpubk4pxjzYE1op5CpHA2ERrCaDqaiORptYUzDn5+FgTe3YRU4OPoK8WobZ4=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYzODA3ODcyNTAyMSwKICAicHJvZmlsZUlkIiA6ICIyMjI3ZDM4ZmRkYTA0OGJmODA1MTFlNzlmNTBlOWVhYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJtYXBkYWRkeSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xM2Y0ZGMzZTY1YjY3OTI2MDMzYjBhZjgzYThkYWU2YzYzOGQzNzcwNzc4OWYzYTYxZDc4YjZmZGJkZjkyOWJmIgogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(15.5, 70.0, -71.8, 90, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        if (isInDialogue(player)) return;
        boolean hasSpokenBefore = player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_FARM_MERCHANT);

        if (!hasSpokenBefore) {
            setDialogue(player, "hello").thenRun(() -> {
                player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_FARM_MERCHANT, true);
            });
            return;
        }

        player.openView(new GUIShopFarmMerchant());
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return new DialogueSet[] {
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "You can buy and sell harvested crops with me!",
                                "Wheat, carrots, potatoes, and melon are my specialties!",
                                "Click me again to open the Farmer Shop!"
                        }).build(),
        };
    }

}
