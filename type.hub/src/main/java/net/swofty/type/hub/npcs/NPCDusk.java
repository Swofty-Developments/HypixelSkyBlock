package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

import java.util.stream.Stream;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCDusk extends HypixelNPC {

    public NPCDusk() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"§dDusk", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "Sw9y01Pmd5tbRn0PIqitlPIBE/tEW0dtlIhGaE/tfQXDU/FnHhuv6qtU4zVMtsDJmP+50DebsemPCy2/+MMJc5u1/IFpbLXPWPc20hyqcOwlkLTiiNipKL+1UUJxNtKC2AJd3qex/z/F9+SIR5UTddVbp4wYIfmSOjhFJh6WzmTPK2WJIOf8a9d+JoY3fjvFmIdRJaqOeV6LBAlPZtfeN63uQzM9nRxZpe9oP/M4mWks+U5fS99Qg23US+GsZczUWjkjrEtxOOQFbWLYzX1PuC1TKdqVXsfSaGw/r/AyZt2LjVdv1TdAA+OGWlUGEq0CuFLsMyS5Ft3zDku6vkStnVSKv8bO51jFy/NSLm9D+VSy6n07Pob1Dk1wyQZaw5aqv1lgc9FUuYwMU9xPYd7n0X4jcd3FYFFA3D66uu/hiw7Wt9+FCJhuAaUFLukCoqBs03GJv4dUgTVEw9E+6TCeWVXoW1SE/vp+yJ0KkOay6tcAtNap0oKu4ZL4absdK8x6X7mpOJjPhC2KRBrrhKqeeAlP3jDsrI0brIA6wuvasKAWAljR4F65d5muhrra8dAc53MdzzRfOoAFbT7PuXMlseE7s47tW6yshzNgf+lgqoJ5WhK53o0LRA9fxsd4n2dWB4hHI82N2O1Z18Z+7uyXKzJ6NMk2c2O86eP7IS5eRuE=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "eyJ0aW1lc3RhbXAiOjE1NzY3OTAxMTUwMjAsInByb2ZpbGVJZCI6ImVkNTNkZDgxNGY5ZDRhM2NiNGViNjUxZGNiYTc3ZTY2IiwicHJvZmlsZU5hbWUiOiJGb3J5eExPTCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTkyZTNmZTdkYWZlMDJjMGJlMDFjMTdjYmU2NGRjMGQ5YTI1MDVjMWNmMjM4MzdjNDllMmFlNzk0YWMxMjE4NCJ9fX0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-37.5, 68, -125.5, -90, 0);
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
        setDialogue(e.player(), "hello");
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "You can apply §drunes §fto weapons and armor with the §dRune Pedestal §fbehind me.",
                                "You can also combine two runes for a chance to create a higher level rune with a better effect!"
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
