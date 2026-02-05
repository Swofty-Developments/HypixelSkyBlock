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

public class NPCRabbitBro extends HypixelNPC {
    private static final ChocolateFactoryRank RANK = ChocolateFactoryRank.UNEMPLOYED;
    private static final String NPC_NAME = "Rabbit Bro";

    public NPCRabbitBro() {
        super(new HumanConfiguration() {
            @Override
            public boolean visible(HypixelPlayer player) {
                // Rabbit Bro is always visible (unlocked by default)
                return true;
            }

            @Override
            public String[] holograms(HypixelPlayer player) {
                if (player instanceof SkyBlockPlayer skyBlockPlayer) {
                    DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(skyBlockPlayer);
                    DatapointChocolateFactory.EmployeeData employee = data.getEmployees().get(NPC_NAME);
                    if (employee != null) {
                        ChocolateFactoryRank rank = ChocolateFactoryRank.fromLevel(employee.getLevel());
                        return new String[]{rank.getHologramLine(), rank.getChatName(NPC_NAME), "§e§lCLICK"};
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
                return "ewogICJ0aW1lc3RhbXAiIDogMTcxMjU5NDI0NjM2MywKICAicHJvZmlsZUlkIiA6ICJjZjc4YzFkZjE3ZTI0Y2Q5YTIxYmU4NWQ0NDk5ZWE4ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYXR0c0FybW9yU3RhbmRzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI4NzkzNGJkZDlkZjI3MDViMjUxYmI5OTdlMDI5YjE4YzFlOTRkZjEyOTkyYjgxMDdlNzQ0OTdiMjA1Y2E3ZTgiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(8.5, 71, 19.5, 0f, 0f);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }

            @Override
            public String chatName() {
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
                                "You should hire me! I can help you boost your §6Chocolate Factory §fproduction §dtenfold §fby next quarter!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-2").lines(new String[]{
                                "Hire me, bro!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-3").lines(new String[]{
                                "Get me a job at your §6Chocolate Factory§f!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-4").lines(new String[]{
                                "§aHoppity §fthinks he's all that with his chocolate empire.",
                                "But ask him this...who can hop the highest?"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-5").lines(new String[]{
                                "Rise and grind, as I say. The early rabbit gets the cocoa beans."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-6").lines(new String[]{
                                "§aHoppity §fmay run the factory, but who do you think inspires the work ethic?"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-7").lines(new String[]{
                                "My morning routine? A quick hop around the fields, a bit of carrot juice, and then straight to work on the next big chocolate innovation."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-8").lines(new String[]{
                                "Sleep is for the weak!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-9").lines(new String[]{
                                "Working out before dawn has its perks; you get to see §aHoppity§f's 'inspirational' morning pep talks.",
                                "It's like a shot of espresso, but with more hopping and less coffee."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-10").lines(new String[]{
                                "§aHoppity's §fliving the sweet life now, but who's the one who taught him to dodge those garden gnomes? Bro knows best."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-11").lines(new String[]{
                                "It's like a shot of espresso, but with more hopping and less coffee."
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-12").lines(new String[]{
                                "§aHoppity's §fliving the sweet life now, but who's the one who taught him to dodge those garden gnomes? Bro knows best."
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
