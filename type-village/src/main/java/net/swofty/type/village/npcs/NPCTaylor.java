package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

import java.util.stream.Stream;

public class NPCTaylor extends NPCDialogue {

    public NPCTaylor() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"Taylor", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "sOmYjVJq7bD9nZubtvTDSrZ+y/HahVdH4Uf/I28EOdxzlsErr2gMj2g9VAQZcaL1efnNtibYzrcpwC1dAEiE5Epeur1xJBolZLJ69xgF1V29BDkUOPhZXENiV2d7AxRn88p2/uQ5s7Bjp0DFLctKRRif7eU4gVmE+Qzgh+bamrA2Gh9nhYBSQpbs8Oy3k7I851S++ImWZccHqQ7ntwD+Fbi8tWBV3tD/FBxcw5vp1dGGA/edL7HjOtOLx+SQbk6s44MAkRCmvgaGw5JFkVSffpvSCJv7vcLdyI0VWdGoetGM4ESa1IgN/6DVXCRtB11yWW0yS4cYuvkee0iGLs/wGcwRa4KDxsfpvXRc5/FVfH7VO0lOl046m+bjmwhXDZ8P9ovwL/PLo0HOoEcUlzlMX1ABvqx3DUdxaSLLfgZthwGXHZztupKbkI41MFtCom81PgHmfDY1vCCbrGQGfdtRHUlTwcTtB+xMe0egU7ASvxe+yzINY/X8yzl75UrOiQ0DTGhYhoSX2Upmm7BpOhWQ4n2lNuHEBD9B2obTuCT5EcrlRUGAzEwX0hBCHz2d4TnWy9ppP78beXamMIjvyiJt2TFAUZVv1hnjscrL25909M8ZHgKk/l8lZkKwoEhPN+NtURj11XMVthUUtvUtFtVAOl1HO/bE6ts2CR55zFTsoCQ=";
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxNDc2NTAyMjM1NCwKICAicHJvZmlsZUlkIiA6ICIwNWQ0NTNiZWE0N2Y0MThiOWI2ZDUzODg0MWQxMDY2MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJFY2hvcnJhIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzE4Y2EwYzg5YmVmOGM0ZTdlNTIxYjExMzgyMWZmZGYwMGU3YjVlYjU1MmEzNzhmOTAzN2NiZWM2NTVkZDI3YzgiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position() {
                return new Pos(22, 71, -45.5, 0, 0);
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
                                "Hello!",
                                "You look dashing today!"
                        }).build()
        ).toArray(NPCDialogue.DialogueSet[]::new);
    }
}
