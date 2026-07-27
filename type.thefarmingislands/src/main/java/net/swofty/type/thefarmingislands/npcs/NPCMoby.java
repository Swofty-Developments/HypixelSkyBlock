package net.swofty.type.thefarmingislands.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ChatColor;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.stream.Stream;

public class NPCMoby extends HypixelNPC {
    public NPCMoby() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Moby", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "O3vkTjT7tKfVpyQwg7q7m0PIqkfl1uvaQoeHMN9pwpOeMZ9kJH45rr4vJV6eb8qjWoOpYl01p8uLHyW3+TSFPuCDBsQSKIqAu7+h6LpZn3dGIfvS+7kml7keIR+L3JPiMz+qT52E2BWzRg78MDIb0OPF5sAB/3XvpiKq4TIQhyrw0m3ovi+7DF4Kk+DK2tTflgpsMb35kiR+hqyIq87vENv8iN9QnE8iI1xMQ3sirpDBhz+dtjMVKZkG9J3lKiiA4LUspphiM8L2NrkF0ru9WET9BngpUKArmBzldfMw0t8SXDUPkiCH2p3R7LkI1bVEaF+Cw/WVlSVMxJrBs9dkRkdDbpMDNRkiEkFfd39NPCaKi5nWzhaLmHTo4nIzYnZ76E4LpaYQflCZdH8ECewbmU/zFXUVcp+iYOkfgAIAFq901KQ/z0aDsC+181hMCfwmaPyz4rWX6efqeZge2qw9bV8SOz7bom0jz2KAoCU1xyi0Ql9A2OXu/YU5pvVUlqBI5pyp+y/EwHeJ3imRzdAH+UHRxdb4nTDl0txFISiZCAG/wvGW/cisdfaFn3PblmbFWtKTN9NrmM/MCnb1w5hnKPhvNFZt1ch8Xi1Xpa5zghSiy7TT2gANlkhvpjYWETmMfwcZ71sf1gM3SngYH6IpJzCeGWN+E0N+SoyMviq9Kec=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYyNjEwNzI5NjMzNCwKICAicHJvZmlsZUlkIiA6ICJiMWMyNWQ0YjMwZDU0N2Y4YTk3NmZlYTllOGU1YzBjMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJvd29FbmRlciIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85YjY4ODU0NWUzMjZlNGYxZmQxYzQwNTg3M2Q5YzViOGJiZTY5ZGQyNjg4OWNlM2RlYTEwYjRkOWUzZDdiNGRiIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(206, 43, -500, -130, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        e.player().notImplemented();
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("first-interaction").lines(new String[]{
                                "So listen.",
                                "The other day I was chumming with my chums.",
                                "One of them yaps and I say, \"ever tried my tonic\"?",
                                "So yeah, I come back to my crib, right?",
                                "And listen, I've never seen anybody down here, but you seem like a nice fellow.",
                                "So just bring me " + ChatColor.GREEN + "8 Glowing Mushrooms" + ChatColor.WHITE + " and I'll show you how to chum, alright buddy?"
                        }).build(),
                DialogueSet.builder()
                        .key("after-bringing-8-glowing-mushrooms").lines(new String[]{
                                "Ok listen now, I practice a secret chumming technique.",
                                "It's reverse-chumming!",
                                "Instead of chumming, it's the sea creatures giving you " + ChatColor.GREEN + "Chums" + ChatColor.WHITE + ".",
                                "Listen, it's very easy.",
                                "First we trade a Chum Bucket, ok?",
                                "Just click me!"
                        }).build(),
                DialogueSet.builder()
                        .key("before-buying-an-empty-chum-bucket").lines(new String[]{
                                "Just click me!"
                        }).build(),
                DialogueSet.builder()
                        .key("after-buying-an-empty-chum-bucket").lines(new String[]{
                                "First you place down the bucket near water, right?",
                                "Then use a fishing rod and throw a line in said water.",
                                "Ok and you wait now. That part is easier with YOUR chums.",
                                "Listen, time is shorter with friends.",
                                "After waiting you kill a sea creature and get a " + ChatColor.GREEN + "Chums" + ChatColor.WHITE + ".",
                                "You put it in the bucket and someday it's filled.",
                                "Then listen you bring me the filled bucket, ok?"
                        }).build(),
                DialogueSet.builder()
                        .key("bringing-a-full-chum-bucket").lines(new String[]{
                                "Wow ok listen that's good.",
                                "You are the chumming expert ok?",
                                "Listen. I also have some good items for your chumming.",
                                "And I have this really good tonic. It's a really good tonic, ok?",
                                "So get mushrooms, fill chum buckets, trade tonics! Easy chums!"
                        }).build(),
                DialogueSet.builder()
                        .key("not-bringing-the-correct-items").lines(new String[]{
                                "Come back with the right amount of items!"
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
