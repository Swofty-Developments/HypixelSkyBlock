package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.hub.gui.GUIShopFishMerchant;
import net.swofty.types.generic.data.datapoints.DatapointToggles;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class NPCFishMerchant extends NPCDialogue {
    public NPCFishMerchant() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"§9Fish Merchant", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "piUjhfqpIwGQ/JbUxauA4SOGkH/2Li6Sb5y4ErzIob6Nv1Ai/BFyKac5aEroxh53M9a9Qdqz13tXnS03nlykCGXtlhFhJ/H/pEgIKGEFRvRmgF3L9R7u4ZYqZT7R89m6yruGCr+37ZUUk1rSF7idx7TtC0IFJt9k+QFOyQqtk2vjka8POPGQ2nXPqLnDaKi8TlAvlCIEWR3z0hSN4pLOupXFMF99a49GX0CWj7ewB6vJl+sEinNSYyviYPK7ncVYfJcn4gSN9EtsI1PQzAT2Tb3/EntzoBk88RLxpJNKjcySTzs2yALlRyUAtsn4QkOyoIBOkSEhixDY7BHd46ALllo5HwdzKjExc5JK6HUL/KdsRXIfK3ZEJ8QodhJUiwn7JkiMtMmLTpebIZwVWDQIB+1fz9KQ7bUq01OkQx4bGeIfg6H0nMRY/gPAVWSvAZbJwZkeJ811RbNbgHozfuvo2/Bq74KTiobxkD+M4MbfXfTi1XjawDZg+W/Jzb313cDL1EbgyU+DIDQUA4aPtoVcGFu3l9BoM8Yb+mD0XiMxwxw9cJGLS6f+xg5sG4IgK5RMpYjMuq9Mrt2ebu7nxx5JqjygJcxqy1/YQt4k4BL/D0tfZbmSjsQ45RZ7uMjP0FtALhFusLfykALCP78hYm8q4to8pDmIhvdrjssUVu0C9Uk=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "eyJ0aW1lc3RhbXAiOjE1NTkxMzU3NjQ4NDMsInByb2ZpbGVJZCI6IjkxZjA0ZmU5MGYzNjQzYjU4ZjIwZTMzNzVmODZkMzllIiwicHJvZmlsZU5hbWUiOiJTdG9ybVN0b3JteSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTJjNGVlMDQ3OWI5MDFmMTc3YzI4Njg5NWRhMzEwYTgwNmRmZTg5N2M5YTg2NzhiOWRlODhhYmJiMWJmZjRiMSJ9fX0=";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(52.5, 68, -82.5, 90, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        SkyBlockPlayer player = e.player();
        if (isInDialogue(player)) return;
        boolean hasSpokenBefore = player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_FISH_MERCHANT);

        if (!hasSpokenBefore) {
            setDialogue(player, "hello").thenRun(() -> {
                player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_FISH_MERCHANT, true);
            });
            return;
        }

        new GUIShopFishMerchant().open(player);
    }

    @Override
    public NPCDialogue.DialogueSet[] getDialogueSets(SkyBlockPlayer player) {
        return new NPCDialogue.DialogueSet[] {
                NPCDialogue.DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "Fishing is my trade.",
                                "I buy and sell any fish, rod, or treasure you can find!",
                                "Click me again to open the Fisherman Shop!"
                        }).build(),
        };
    }
}
