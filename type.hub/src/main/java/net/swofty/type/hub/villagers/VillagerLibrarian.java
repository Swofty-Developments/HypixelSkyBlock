package net.swofty.type.hub.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.swofty.type.hub.gui.GUIShopLibrarian;
import net.swofty.type.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.type.generic.entity.villager.NPCVillagerParameters;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class VillagerLibrarian extends NPCVillagerDialogue {
    public VillagerLibrarian() {
        super(new NPCVillagerParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"&fLibrarian", "&e&lCLICK"};
            }

            @Override
            public Pos position() {
                return new Pos(-35, 69, -112, -45, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public VillagerProfession profession() {
                return VillagerProfession.LIBRARIAN;
            }
        });
    }

    @Override
    public void onClick(PlayerClickVillagerNPCEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        if (isInDialogue(player)) return;

        MissionData data = player.getMissionData();

        if (data.isCurrentlyActive("speak_to_librarian")) {
            setDialogue(player, "quest-hello").thenRun(() -> {
                data.endMission("speak_to_librarian");
            });
            return;
        }

        new GUIShopLibrarian().open(player);
    }

    @Override
    public DialogueSet[] getDialogueSets() {
        return Stream.of(
                DialogueSet.builder()
                        .key("quest-hello").lines(new String[]{
                                "&e[NPC] Librarian§f: Greetings! Welcome to the §bLibrary§f!",
                                "&e[NPC] Librarian§f: The §bLibrary §fis your one-stop shop for all things enchanting. Enchant items, purchase §aEnchanted Books§f, and more!",
                                "&e[NPC] Librarian§f: You can enchant items by clicking any §aEnchanting Table§f. Enchanting costs §3experience levels §f- the more levels you spend, the better enchantments you will receive.",
                                "§e[NPC] Librarian§f: Use the §aEnchanting Table §fto enchant an item!"
                        }).build()
        ).toArray(NPCVillagerDialogue.DialogueSet[]::new);
    }
}
