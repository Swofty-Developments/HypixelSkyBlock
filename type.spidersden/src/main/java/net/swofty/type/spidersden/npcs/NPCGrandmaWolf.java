package net.swofty.type.spidersden.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ChatColor;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.stream.Stream;

public class NPCGrandmaWolf extends HypixelNPC {
    public NPCGrandmaWolf() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{
                        ChatColor.RED + "Grandma Wolf",
                        ChatColor.YELLOW + "" + ChatColor.BOLD + "CLICK"
                };
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "PX74SU2IXWdqcl0HOqT7dsD78wvGTZQxfirgsAw1lF2iN5XhEosX+1QlZL2OimwV7WsQIs3Slnwo6aG7a07OdnkLSnrkQSA6payjVMexS3aSk5QBqIVrVyxDXbcSp7ck4SOPwmFuzOTqtr0+jPmjiJlcrHUa4MnYd+taIyweiE1Vwu/j65Skx1roTbEAf1l3zXdmemVXUVKfXYbT96+do4dp1ycGA2pYGC5xWgFPzxdY+WU2OYVl7KPCnArfGyKvfoA9gpWNlmo/tDQcieHkSADuOZrKWjRNw97w3FOys2BGWgF95Qsf9GJqBBQsLJMIfSdHh5nlvvBiQnhnCaacZ5l5ySm1VQ6beAllMWs0W+UaTfNtwsXYsBnC65JzhC01w3ALt7GjO5sfSlRPjbDkfp14z6U4/hebL0yQrQT3K+ghQ6iEU7acpP+0pclwhT6GGNjqssO+wvIvYOfkgDQ+KJFIPOdEsnYG7DPFgJVmJWG+qMHC3inkCkT9Afb4fLprjDtZVbMgVYo2V8AWsjGE2IDtQ4sjOa0OAEBwqlPB62n8irts6JgNMWddE69f0LLkmwLkcLHiLalygSyxxuNmyGlAyOMkMS7AANvsYalqFbjHn3EyPhRR+Mzz3q0UK048ezFMs8BgStPq4OgPnCSIJggpemXThSdAIULn6BqTnRY=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxNDg3MTAxNDQ5OCwKICAicHJvZmlsZUlkIiA6ICJmMTA0NzMxZjljYTU0NmI0OTkzNjM4NTlkZWY5N2NjNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJ6aWFkODciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODM5MTkzYzA5YmNhZDlkMWY5MDFhZmVmNGUyYzBjYWQ4M2FhZTFhNTcwMzM1YzNlY2JjN2QwNWZkZTE5ODc5NSIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-281.5, 122, -190.5, 0, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent event) {
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("first-interaction").lines(new String[]{
                                "Oh, " + player.getFullDisplayName() + ChatColor.WHITE + "! I'm having a conundrum! There's so much to kill, yet so little time.",
                                "I've added the " + ChatColor.GREEN + "Grandma Wolf Pet" + ChatColor.WHITE + " to your " + ChatColor.GREEN + "Pets Menu" + ChatColor.WHITE + ".",
                                "The Grandma Wolf is particularly adept at combos. The more mobs you kill in quick succession, the better!",
                                "If you reach high enough " + ChatColor.GOLD + "Bestiary Milestones" + ChatColor.WHITE + ", I will increase the rarity of this pet!",
                                "The pet does not need to be spawned for combos to work! Happy hunting!"
                        }).build(),
                DialogueSet.builder()
                        .key("before-bestiary-10").lines(new String[]{
                                "There you go! I've upgraded your " + ChatColor.GREEN + "Grandma Wolf Pet" + ChatColor.WHITE + " to " + ChatColor.GREEN + "Uncommon" + ChatColor.WHITE + "!",
                                "Come back when you've reached " + ChatColor.GOLD + "Bestiary Milestone X" + ChatColor.WHITE + " to upgrade it to " + ChatColor.BLUE + "Rare" + ChatColor.WHITE + "!"
                        }).build(),
                DialogueSet.builder()
                        .key("before-bestiary-15").lines(new String[]{
                                "There you go! I've upgraded your " + ChatColor.BLUE + "Grandma Wolf Pet" + ChatColor.WHITE + " to " + ChatColor.BLUE + "Rare" + ChatColor.WHITE + "!",
                                "Come back when you've reached " + ChatColor.GOLD + "Bestiary Milestone XV" + ChatColor.WHITE + " to upgrade it to " + ChatColor.DARK_PURPLE + "Epic" + ChatColor.WHITE + "!"
                        }).build(),
                DialogueSet.builder()
                        .key("before-bestiary-20").lines(new String[]{
                                "There you go! I've upgraded your " + ChatColor.DARK_PURPLE + "Grandma Wolf Pet" + ChatColor.WHITE + " to " + ChatColor.DARK_PURPLE + "Epic" + ChatColor.WHITE + "!",
                                "Come back when you've reached " + ChatColor.GOLD + "Bestiary Milestone XX" + ChatColor.WHITE + " to upgrade it to " + ChatColor.GOLD + "Legendary" + ChatColor.WHITE + "!"
                        }).build(),
                DialogueSet.builder()
                        .key("after-bestiary-20").lines(new String[]{
                                "There you go! I've upgraded your " + ChatColor.GOLD + "Grandma Wolf Pet" + ChatColor.WHITE + " to " + ChatColor.GOLD + "Legendary" + ChatColor.WHITE + "!",
                                "I'm afraid that's all I can do for you. Thank you for helping this little old Grandma!"
                        }).build(),
                DialogueSet.builder()
                        .key("pet-is-already-legendary").lines(new String[]{
                                "I've already upgraded your pet to the maximum tier! Leave this little old Grandma alone!"
                        }).build(),
                DialogueSet.builder()
                        .key("losing-pet").lines(new String[]{
                                "I'm afraid you don't have a " + ChatColor.GOLD + "Grandma Wolf Pet" + ChatColor.WHITE + "! Leave this little old Grandma alone!"
                        }).build(),
                DialogueSet.builder()
                        .key("without-bestiary-requirement").lines(new String[]{
                                "I will upgrade your Grandma Wolf Pet to [rarity] when you reach Bestiary Milestone #." // TODO: Replace [rarity] and # with the actual values when calling this dialogue.
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
