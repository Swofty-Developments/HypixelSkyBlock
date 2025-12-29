package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;

import java.util.stream.Stream;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCNicole extends HypixelNPC {

    public NPCNicole() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§9Nicole", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "KDd6cdjHjGul2t11SIwkeeSpYNhI0rSuyqlvb1Go2IpWze5G9hwWu1uE3Vh+g0+Isu41Ng0VdKk65W7R+qlBbwCf4CdUwi5f01MCdTTgXTmhBShHlu47UQ7Pw4Dkz0I5o6lsbRnTqEclxO7zxJJZAzEaAQYBHdnEVqP7vXsBsgGfkNYHm8v0lSNDUHrnIuypMu1lvkWEQp0PPjHgLohe8uw5Yji8yoeJyxuNPMpu2fnq50dtLm+cY1F5Ucf33kpwVNj1h2h7wbN1rUd5+cK6bGipM2FSPu95IJtduN5iNEi0SNmefop8lkvgeliEb9oPC/xkzr+h9Vnj2o2oHDmGL3sFot8o7fy0jgTsA3wunLArE3kocCOFiId+nKH2v7lK2/fIVMz5M1XxAcRwJGo36fg+aHbhMKlvVrDa1mU2BxfqGxXJICNjAwbUzWt5je49zluxEJscLLEgFHsZ62qowg1J0NsSPBqZpH8vfv7vnEzhUJhcjbTY8/zVKJdgVZLCOMJkHuutZgYk38Ypmtpz13uZjzY3b7D/chIyLDIeLdiBmLAZu3H2xwZDibpHY9DnEclKneBSW8gvXxnDu0oyUaIGfK9y8AHMoYNsgdY87txGYA++VPQ+IJUVlY1y26X1ND/AApR9BoLu9eo53vKY8m9mVvrHy5IT7AvgAAbzTGI=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYwOTE5OTI3ODQxOSwKICAicHJvZmlsZUlkIiA6ICI3MzgyZGRmYmU0ODU0NTVjODI1ZjkwMGY4OGZkMzJmOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJJb3lhbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80OGEzZDQzMjgzZjEyYWIxZmE0ZDFiOTAxZGY4OTc1ODkxNDBmNGNjMWY2NzQ4MjIyZTU3NGNkYzcwY2U4M2E0IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(39.5, 90, 72.5, 90, 0);
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
        ItemType itemTypeLinker = new SkyBlockItem(e.player().getItemInMainHand()).getAttributeHandler().getPotentialType();
        // check if the item is cheap coffee ItemType
        if (itemTypeLinker == ItemType.CHEAP_COFFEE || itemTypeLinker == ItemType.DECENT_COFFEE || itemTypeLinker == ItemType.BLACK_COFFEE) {
            setDialogue(e.player(), "coffee-hello");
        } else {
            setDialogue(e.player(), "hello");
        }
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "Books are mesmerizing.",
                                "Their words offer permanence.",
                                "Permanence is knowledge.",
                                "Knowledge transcends time and space.",
                                "Space is only a construct...",
                                "§cWhew! §fI need some coffee!"
                        }).build(),
                DialogueSet.builder()
                        .key("coffee-hello").lines(new String[]{
                                "Thank you!",
                                "§dElise §freally wanted this but she is shy.",
                                "I'm selecting books for her.",
                                "Anything to do with the §4Calamity §for the §dRiftway §fat the top of the mountain."
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
