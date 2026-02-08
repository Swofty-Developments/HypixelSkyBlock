package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.skyblockgeneric.chocolatefactory.ChocolateFactoryHelper;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointChocolateFactory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class NPCRabbitDaddy extends HypixelNPC {
    private static final ChocolateFactoryRank RANK = ChocolateFactoryRank.UNEMPLOYED;
    private static final String NPC_NAME = "Rabbit Daddy";

    public NPCRabbitDaddy() {
        super(new HumanConfiguration() {
            @Override
            public boolean visible(HypixelPlayer player) {
                if (player instanceof SkyBlockPlayer skyBlockPlayer) {
                    DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(skyBlockPlayer);
                    return data.getEmployees().containsKey(NPC_NAME);
                }
                return false;
            }

            @Override
            public String[] holograms(HypixelPlayer player) {
                if (player instanceof SkyBlockPlayer skyBlockPlayer) {
                    DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(skyBlockPlayer);
                    DatapointChocolateFactory.EmployeeData employee = data.getEmployees().get(NPC_NAME);
                    if (employee != null) {
                        ChocolateFactoryRank rank = ChocolateFactoryRank.fromLevel(employee.getLevel());
                        return new String[]{rank.getHologramLine(employee.getLevel()), rank.getChatName(NPC_NAME), "§e§lCLICK"};
                    }
                }
                return new String[]{RANK.getHologramLine(), RANK.getChatName(NPC_NAME), "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTcxMjg0NzA0NzAwNSwKICAicHJvZmlsZUlkIiA6ICIzOThiZGM3NWVhYzQ0ZjMzYWEyMDBiMTYyNTRmMDhlOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJJa2h3YW4wNTEwIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzU3Y2FiMGMzNGQ3ZGRjZjcyZGI1NmZmMzZmMjg4M2Y1NTRjZmY3NmViNWQzYjNlMDU2MjMzODAzNmM5NzYwNDMiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(14.5, 69, 23.5, -25.3f, 0f);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }

            @Override
            public String chatName() {
                return RANK.getChatName(NPC_NAME);
            }

            @Override
            public String chatName(HypixelPlayer player) {
                if (player instanceof SkyBlockPlayer skyBlockPlayer) {
                    DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(skyBlockPlayer);
                    DatapointChocolateFactory.EmployeeData employee = data.getEmployees().get(NPC_NAME);
                    if (employee != null) {
                        ChocolateFactoryRank rank = ChocolateFactoryRank.fromLevel(employee.getLevel());
                        return rank.getChatName(NPC_NAME);
                    }
                }
                return RANK.getChatName(NPC_NAME);
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        if (isInDialogue(e.player())) return;
        setDialogue(e.player(), "dialogue-" + (int) (Math.random() * 12 + 1));
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("dialogue-1").lines(new String[]{
                                "In life there are §csharks§f and there are §asheep§f.",
                                "I'm a §cshark§f.",
                                "Well, I'm a rabbit. But you get the idea."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-2").lines(new String[]{
                                "I am the heir to the §6chocolate throne§f.",
                                "The only thing in my way? You. Please hire me, boss."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-3").lines(new String[]{
                                "Heir to the §6chocolate throne§f, they say.",
                                "More like heir to a bunch of headaches, courtesy of my own daughter's protests."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-4").lines(new String[]{
                                "Every day, it's a new challenge. If it's not the market, it's Sis with her picket signs.",
                                "Still, we're making the world sweeter, one chocolate bar at a time."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-5").lines(new String[]{
                                "Every family has its ups and downs, but ours?",
                                "We've got a whole soap opera thanks to Sis."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-6").lines(new String[]{
                                "Between Bro's grindset, Cousin's relaxed approach, and Granny's wisdom, we've got all the ingredients for success.",
                                "Now, if only Sis would see that."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-7").lines(new String[]{
                                "Bro's dedication is what this company needs more of. And Cousin, well, he brings...§dcreativity§f.",
                                "Granny? She's the glue holding us all together."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-8").lines(new String[]{
                                "§aHoppity §fwants us to 'think outside the box.' Last time I did that, we ended up with chocolate-covered grass.",
                                "Sold out in a week. Shows what I know."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-9").lines(new String[]{
                                "In life there are §csharks§f and there are §asheep§f.",
                                "I'm a §cshark§f.",
                                "Well, I'm a rabbit. But you get the idea."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-10").lines(new String[]{
                                "I am the heir to the §6chocolate throne§f.",
                                "The only thing in my way? You. Please hire me, boss."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-11").lines(new String[]{
                                "Heir to the §6chocolate throne§f, they say.",
                                "More like heir to a bunch of headaches, courtesy of my own daughter's protests."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-12").lines(new String[]{
                                "Every day, it's a new challenge. If it's not the market, it's Sis with her picket signs.",
                                "Still, we're making the world sweeter, one chocolate bar at a time."
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
