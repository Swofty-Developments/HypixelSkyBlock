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

public class NPCRabbitGranny extends HypixelNPC {
    private static final ChocolateFactoryRank RANK = ChocolateFactoryRank.UNEMPLOYED;
    private static final String NPC_NAME = "Rabbit Granny";

    public NPCRabbitGranny() {
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
                return "ewogICJ0aW1lc3RhbXAiIDogMTcxMjU5NDIyNDA2NCwKICAicHJvZmlsZUlkIiA6ICI2OGVmMmM5NTc5NjM0MjE4YjYwNTM5YWVlOTU3NWJiNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVNdWx0aUFjb3VudCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kNmViMmQ4NWVlOGUzYWYxYzJlYzkzNGJlYjcwYTM5YzVlNzY2YjIzYmRhYjYzMjEwYmQyYWFjZDczY2JiZmM4IgogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(9.5, 69, 23.5, -85.8f, 0f);
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
        setDialogue(e.player(), "dialogue-" + (int) (Math.random() * 14 + 1));
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("dialogue-1").lines(new String[]{
                                "I'm looking to come out of retirement for §done last job§f.",
                                "Plus, I decided I don't think I could ever give up chocolate."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-2").lines(new String[]{
                                "The §6Chocolate Factory §fused to be different before §aHoppity§f arrived.",
                                "You never know that you're in the glory days until it's too late."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-3").lines(new String[]{
                                "In my day, we made sweets from whatever was in the garden. §aHoppity§f just added cocoa.",
                                "Kids these days think they've invented sugar."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-4").lines(new String[]{
                                "Every time §aHoppity§f talks about 'expanding the business', I remind him: 'Don't forget to expand your heart too.'",
                                "He's a good boy, he listens."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-5").lines(new String[]{
                                "§aHoppity§f wants to automate the chocolate wrapping. I told him, 'Nothing beats the personal touch.'",
                                "He gave me a computerized knitting machine."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-6").lines(new String[]{
                                "I've watched this family business grow from a single carrot patch to rows of cocoa trees.",
                                "Each bar we produce carries that legacy. Makes an old bunny proud."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-7").lines(new String[]{
                                "They say you can't choose your family. But if I could, I'd choose this chocolate-crazed bunch every time.",
                                "From Dust Bowl to chocolate empire, we've come a long way."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-8").lines(new String[]{
                                "In my days, we settled disputes over a hot cup of cocoa.",
                                "Maybe that's what Sis and my son need - a reminder of what binds us."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-9").lines(new String[]{
                                "I'm looking to come out of retirement for §done last job§f.",
                                "Plus, I decided I don't think I could ever give up chocolate."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-10").lines(new String[]{
                                "The §6Chocolate Factory §fused to be different before §aHoppity§f arrived.",
                                "You never know that you're in the glory days until it's too late."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-11").lines(new String[]{
                                "In my day, we made sweets from whatever was in the garden. §aHoppity§f just added cocoa.",
                                "Kids these days think they've invented sugar."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-12").lines(new String[]{
                                "Every time §aHoppity§f talks about 'expanding the business', I remind him: 'Don't forget to expand your heart too.'",
                                "He's a good boy, he listens."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-13").lines(new String[]{
                                "§aHoppity§f wants to automate the chocolate wrapping. I told him, 'Nothing beats the personal touch.'",
                                "He gave me a computerized knitting machine."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-14").lines(new String[]{
                                "I've watched this family business grow from a single carrot patch to rows of cocoa trees.",
                                "Each bar we produce carries that legacy. Makes an old bunny proud."
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
