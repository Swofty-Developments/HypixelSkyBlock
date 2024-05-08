package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.missions.lumber.MissionBreakOaklog;
import net.swofty.types.generic.mission.missions.lumber.MissionTalkToLumberjack;
import net.swofty.types.generic.mission.missions.lumber.MissionTalkToLumberjackAgain;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.List;

public class NPCLumberJack extends NPCDialogue {

    public NPCLumberJack() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"Lumber Jack", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "kyxxya3FqBJo6onG3bC1BjsjrF4uWTDd7Qitz14iNFwvZOn6FsW1A7dkNiZmiR1nVBfex7b4XHYAb6f3jXx8wIEvoDzSjzVLkCkzbQ9aMTTlDSAvkZ/fqtgMJbbXnppUSETmbWm7fdPjr4P5J30+Mz5vb66kNYu8QXsWqQ36YxI6sS2P77+vLdq+n1l389Npw1uMBpMLPjXaTsjAMrq1U6bDbTj7YwYqtURh0hxJm6v3q9d+oXD989fvYu04DYtEiW6H3VDjtvoEAb+m3H9tlDt74SNVXlIJ0lGa6RNpidBhKgSS38F0P5nMo1XbHJ/FcrWP+UZ6D4rT5TuW0T1J7n+5q+/LMMOR2hofFHgdqmTD85tTOmTmKKtIBPW9yreEKNZg7Ah/s3jStnVosp6A9qkpTcmdneRJwL+ZvHcpCbZpJq9Ii/NV+cBNrL49ylVCDZnRN4I7xENYfAq/Xe241cs2bEErrpu9uDH7dXSnhwQn4PdtMN6ZZrr4IJS2sjAyPjcuN/A006gs3Cu+9Cb4MViRCdIgOZDsIV/C0yDqr3+/SgKa+GRp7qIiphIMgRXC03GDSd/btYea1g8qJhhnkAL5MStubm/rdPaEf8wBc3y8Kc4pdEemo5kHQ8SPGvrk+xI8sGWKmlf/bZkyXKw9Wdhg/npeptxjNb2rUuaIDvk=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU5MTQ2MzM5MzU5MywKICAicHJvZmlsZUlkIiA6ICJmMjljOTIyMmVjNmY0NjExOTc3YWNkMmFjYzExNDAxOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJJSVJleWRlbklJIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2NkMGFiZGQ5ZjZlZTdkODM3MGQyNjIxMDljYjg1M2JhY2QzZjQzZjhjZTQyNDExNjRkMDhlM2QyYTU2ODNlMzIiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(-112.5, 74, -36.5, -90, 0);
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
        MissionData data = e.player().getMissionData();

        if (data.isCurrentlyActive(MissionTalkToLumberjack.class)) {
            setDialogue(e.player(), "initial-hello").thenRun(() -> {
                data.endMission(MissionTalkToLumberjack.class);
            });
            return;
        }
        if (!data.hasCompleted(MissionBreakOaklog.class)) {
            e.player().sendMessage("§e[NPC] Lumber Jack§f: Bring me some Logs. You can chop them down in this Forest!");
            return;
        }
        if (!data.hasCompleted(MissionTalkToLumberjackAgain.class)) {
            setDialogue(e.player(), "spoke-again").thenRun(() -> {
                data.endMission(MissionTalkToLumberjackAgain.class);
            });
            return;
        }

    }

    @Override
    public DialogueSet[] getDialogueSets(SkyBlockPlayer player) {
        return List.of(
                DialogueSet.builder()
                        .key("initial-hello").lines(new String[]{
                                "Timber!",
                                "My woodcutting assistant has fallen quite ill! Do you think you could take over for him?",
                                "I just need you to chop down some Logs. If you do, I'll even give you his old axe as a reward!",
                                "I just need you to chop down some Logs. If you do, I'll even give you his old axe as a reward!"
                        }).build(),
                DialogueSet.builder()
                        .key("spoke-again").lines(new String[]{
                                "Thank you! Take this §aSweet Axe§f, it's so sweet that it drops apples from logs sometimes!",
                                "You've got the knack for wood. Could you get some Birch Planks from §aBirch Park§f?",
                                "My associate will be there waiting for you. He will reward you in §6Coins if you're up to the task!",
                                "However, this time I will reforge any item for the low price of Coal §8x10!"
                        }).build()
        ).stream().toArray(DialogueSet[]::new);
    }

}
