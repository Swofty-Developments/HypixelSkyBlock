package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.NPCParameters;

import java.util.stream.Stream;

public class NPCDcRubik extends NPCDialogue {

    public NPCDcRubik() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§cDcRubik", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTcxMzUzODM4ODA1OSwKICAicHJvZmlsZUlkIiA6ICI2NzIyZjAxYTA1ODg0MWU5YTdkNzcyN2Q2NzE0ZTdiOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJEY1J1YmlrIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2Y4NmRlMzg1MGI1NmYyNzc5NmVlZDdhNDExNDU3MjkyZmE4NjViYzU4ODI2MjhlZDEyYWEzYmQ0MGNmODEwZDIiCiAgICB9CiAgfQp9";
            }

            @Override
            public String texture() {
                return "hl8cTU4AwlOs8oXP6HD1O+7J5EWMoS1QOJnWBm35izolt/eOFZZK6votAkx/KvgUoGjQdm28cCqVFvrQ8kaIgifH8DNap/nHUIv0h9a80qOdi/mRVOilZQwiB6I3BLJY9G1bpWYZjtRSlkq7qivz8Ca0MRNQp8URwONrcXfa1ZGSvvg3VwOhf8UsCK7bhkngIAJ2QHue8tRvd6i+BppzgfNpyFWVoFF3zdQb30b2ZupSk6jxMj6Lvn58czxYu8BjEplRwj3VlFxDqM4LyVJ93M6n+Gl/KWmv706tS0GygBuRaiP1Jz0L+gxXbhP8jsMsYfiKCgQDqptundOFOUxTTKrmYe+uu5YetVP4O3JCZhQ7Swosfo4ib3rPNusPGRWwuRIWoMgNKsWbLwKzbQ3ZvWXFqsyVZM8HXpVZNG7maHEDieOo+xmM5hl6XJLmnqTWhSQbzzDj2tf2AjdBnahMhVI1t3jpnHXuy9AeaBp2Jyhv78laUo5+eEiK780nWdmUAHdeMJf8BCg2xTUrtf8fUpEM+4Sc8CTedb823HAyIpMdN7KaQhNK8w7kaI9yVnEOUMiDDkrhXup3Rey1v6VSE6UMmKuMDb/iTsn3CnzdQm8hvLlGkkRcKyax8WVKnQaxGH/HAV+mffskvRq90z2ele8SMmzK+sleZyG1d1Qnnhs=";
            }

            @Override
            public Pos position() {
                return new Pos(101.5, 72, -104.5, 0, 0);
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
        setDialogue(e.player(), "hello");
    }

    @Override
    public NPCDialogue.DialogueSet[] getDialogueSets() {
        return Stream.of(
                NPCDialogue.DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "Hello there!",
                                "Thanks for visiting me =)",
                                "Have a nice day!"
                        }).build()
        ).toArray(NPCDialogue.DialogueSet[]::new);
    }
}
