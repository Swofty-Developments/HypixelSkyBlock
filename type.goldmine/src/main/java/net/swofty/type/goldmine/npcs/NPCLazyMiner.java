package net.swofty.type.goldmine.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.entity.npc.NPCDialogue;
import net.swofty.type.generic.entity.npc.NPCParameters;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class NPCLazyMiner extends NPCDialogue {

    public NPCLazyMiner() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Lazy Miner", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "DEPpEYXbbm6SQmo0h40eZFei7e+qtBqzZXDcRrPI5L2Yb4AFHQtalJrqqR4YlOv9NtOuBg+MDIMna+KFBSldUA0fMklitAk3GRl7Y4mWpAQM9BuOGGuWnqf2wVci5hZSiMtL2pSC4ogln0urAdm5qsB224RfbfnpiEWrm8BTHFXfqaMkEZNah+dxeszTMl1TytWYePAFtolMw/MdyuTGqApSpgk9nWoPaUAgGVWEo0mlrflEdmX0DkR98BKpqeg1naN8C+SbleTBJe8ZYqKRNMqD5bIJWc6J6cjGEeb7TbR+ZMipk3cMRu43HrcRdPAlmxihezTlxeDi1RoeObDSZXNSGtMhDxEWU1bOBAgohT8qM2x9AChAGL2kphq3EJplMlw45a9KbZj+/R68QmcEPY+TNo0BPshFoj8sh+RxZPdrirjM76G6TyVYJNIScu0JQ74WqYh/hpjc75rOldORlS9Aat5IXsDnrjCYi4l37DH2bBB6C+gk5BgGqnce/1QF0VESfJWtmS9gHSPBNZHa9ave2rM0ou2hpEw1LgW27hGWqZfGg0sfU9deQvLYyiRFsAXww22L5xmNJnOmvn3L1lDdGNpT63qXknp9NvAABOCPpBV0zeSCmHlDYbAN19sEkthnnIGk1BwZhsaZizf09gGAlMdLgcGYQKzfJJTODWo=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "eyJ0aW1lc3RhbXAiOjE1NjAxOTM2MzQxNTQsInByb2ZpbGVJZCI6ImEyZjgzNDU5NWM4OTRhMjdhZGQzMDQ5NzE2Y2E5MTBjIiwicHJvZmlsZU5hbWUiOiJiUHVuY2giLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2Q5M2Q5NjM2YWE5NTU2YWNmNGJlMmNjNThkOTNmZjBiNTMyZTY4MDk4ZjgwYTdkYzE1MWEwZjFkMTZiOGU1ZjgifX19";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-11.50, 78.00, -337.50, 40, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.player();
        if (isInDialogue(player)) return;
        boolean hasSpokenBefore = player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_LAZY_MINER);

        if (!hasSpokenBefore) {
            setDialogue(player, "intro").thenRun(() -> {
                player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_LAZY_MINER, true);
            });
            return;
        }

        boolean hasFoundPickaxe = player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_FOUND_LAZY_MINER_PICKAXE);
        if (!hasFoundPickaxe) {
            setDialogue(player, "no-pickaxe-found");
            return;
        } else if (!player.getCollection().unlocked(ItemType.GOLD_INGOT) || !player.getCollection().unlocked(ItemType.IRON_INGOT)) {
            setDialogue(player, "found-pick-no-collection");
            return;
        }

        if (player.getSkills().getCurrentLevel(SkillCategories.MINING) < 5) {
            setDialogue(player, "not-reached-deep-caverns");
            return;
        }

        setDialogue(player, "idle");
    }

    @Override
    public DialogueSet[] getDialogueSets(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("first-interaction").lines(new String[]{
                                "Whoops! I lost my good pickaxe again in the mines! That's why I always come prepared with a backup!",
                                "It's probably down in the mine somewhere. Can you go find it?"
                        }).build(),
                DialogueSet.builder()
                        .key("no-pickaxe-found").lines(new String[]{
                                "Find my pickaxe in the Gold Mines! I'm not going back down there..."
                        }).build(),
                DialogueSet.builder()
                        .key("found-pick-no-collection").lines(new String[]{
                                "Collect iron and gold ingots with that pickaxe you found!",
                                "..Or do it the long way and smelt the ore yourself.",
                                "Smelting Touch is a really useful enchantment. It automatically smelts ores into ingots!"
                        })
                        .build(),
                DialogueSet.builder()
                        .key("not-reached-deep-caverns").lines(new String[]{
                                "The Deep Caverns are full of strange creatures and expensive treasures.",
                                "Reach Mining Level V to gain access!",
                                "Have you spelunked the Deep Caverns?",
                                "They are full of bountiful treasures, but watch out for those Lapis Zombies!"
                        }).build(),
                DialogueSet.builder()
                        .key("idle").lines(new String[]{
                                SkyBlockCalendar.getMonthName() + " " + SkyBlockCalendar.getDay() + " is my resting day!",
                                "I'd go mining but I'm too lazy.",
                                "Maybe some day I'll go back down there."
                        }).build()
        ).toArray(NPCDialogue.DialogueSet[]::new);
    }
}