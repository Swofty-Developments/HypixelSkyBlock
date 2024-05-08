package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.bazaar.BazaarCategories;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.gui.inventory.inventories.bazaar.GUIBazaar;
import net.swofty.types.generic.levels.SkyBlockLevelRequirement;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class NPCBazaar extends NPCDialogue {
    public NPCBazaar() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"§6Bazaar", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "XEVGD9iUeMd+s6AlQRTpQmZOmV7SdO0VyVofMxiPf2eU8feyujw11Yatp6wYuvCuCig3RMGfkL30mcrfZtKA25I4hB7bzJrlV7iZQ/9Oc9nVJKhmwpBT2/HeNrw5gN6dk5nYqW5tg/tjssymrxVy8Cws88I2QNlIlTDMvdEqgHfGzewyT/c8wOv6LlCCEJIDrxzyU3NwGQiIejvmtQdOzbWvei94QWhcFKKxSUCqq0OQ6jB+vU2IGfjmaEYM5ZUfgcNLs1NY8zUHyXAVpqEeVEYnD1+iF0cifFKQ5oz+voWI/cX29GB9lVyJlPmpITrweqPJFQJsKrPK+9BQ9sFhDOPZjjCMODteM09I2IhsPo+LSQf2QuUPF5tBbKQjo738mDhoYi3N2yNOhARdddHMeDtsimkSJV60XiHtMUCkwUTmuY/ieKO5LbpRh5d0lR0HBPtxtoYNWNqGj+P6L+CKszBtbJLnlLxzewUWz/1vzkLst3b+4U8D82bpRSvtU2Riq4ZVj22Dv5daqeh8yXiFqwJ9R7IRCPPLmlhcCx1jo0jbC1lDU7ao3415mJUh9Db/8w6ukQwsBVGFowsjJBPTaWWJq7//twxow76Iw/B3yiHCDwhU48D0mNBBpdFz75DexnOjlqN+5EU8meaAJiZN0LID1jJ5JigD8nYucWM/J+o=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxMzc3OTY1NTA4MywKICAicHJvZmlsZUlkIiA6ICI3MmNiMDYyMWU1MTA0MDdjOWRlMDA1OTRmNjAxNTIyZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNb3M5OTAiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzIzMmUzODIwODk3NDI5MTU3NjE5YjBlZTA5OWZlYzA2MjhmNjAyZmZmMTJiNjk1ZGU1NGFlZjExZDkyM2FkNyIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(-32.5, 71, -76.5, -45, 0);
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
        SkyBlockLevelRequirement lvl = e.player().getSkyBlockExperience().getLevel();
        if (lvl.asInt() >= 7) {
            new GUIBazaar(BazaarCategories.FARMING).open(e.player());
            return;
        }
        setDialogue(e.player(), "hello");
    }
    @Override
    public NPCDialogue.DialogueSet[] getDialogueSets(SkyBlockPlayer player) {
        return Stream.of(
                NPCDialogue.DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "§cYou need SkyBlock Level 7 to access this feature!"
                        }).build()
        ).toArray(NPCDialogue.DialogueSet[]::new);
    }
}
