package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.gui.inventory.inventories.auction.GUIAuctionHouse;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.missions.MissionTalkToAuctionMaster;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class NPCAuctionMaster extends NPCDialogue {
    public NPCAuctionMaster() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"§6Auction Master", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "h+/xwDioXMFcAfROZ7cvvicfppF8sAXY1TVcn+3pjXnUghFfeAXxEsJGGjOMfCXkcZ4suO+ysr4XBHMy21p00cWqeYQi/fi6BRY4+ydhihDiB10VuouC9/tpOa8ph9sCI+stp38uLGsLWRXTrk+86L12cR/HjNoW9StyyFmQuWj5O5LM8cHP7aXrBuUOwnLWt/tndhD4Wa8V+erzZYqBUnGX1SX5NcjeAoHaG9WWag2gKfA2FnRiRjQBvpH77EEhM4LapZLCFz+SXdAmhYulxHAMPWmTjyTfhDg5IoXnKB6n7z3xglRqai0ptNQQpkb+io9EcFj7gu5qIYvI0mbBNOXeKxZTJ/S374H1fRKvUW8AJOyfygpz+m88kkHoP4qFpFbvErAnL/spVHd5IyuwsdtZ/GKN3Re5tXbgqTTFjMBHNNuE70X10WxKBbXCIHJklr0FWR4K58uNQ0qTrAeEd5cX2Sfz/BQVpwcrSi4BkTE3g0XAKL2yoPPceQkB0/i+vB7IAvFqJGKpfKobL0vYFcoQvJcivTH6+17H+Gs6rU7Re3KILrXZ8whfEUdJ39bY50bQKuLyWnvheDzQX9IKCy+64RkE4bx+EDNJBmUgMElT0T+RB1YoaEDpDU2D5m6S/nGgxxMyUiM9UU0N3WAbsXfK/x5q7lypAHEy+IWjgyM=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU5OTk5NzU1MDE4NCwKICAicHJvZmlsZUlkIiA6ICI5NGMzZGM3YTdiMmQ0NzQ1YmVlYjQzZDc2ZjRjNDVkYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJEb25Db3JsZW9uZTE5NzIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzE5NzFkNzk2ZGM0ZGM5YWYzOGNjOTcwNjc5NjNkZTRlZDJiZjk5OTIyNDI1NzQxNmRmYjg0MTQ4OGZkOWUwZCIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(-46.5, 73, -90.5, -90, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public DialogueSet[] getDialogueSets(SkyBlockPlayer player) {
        return Stream.of(
                NPCDialogue.DialogueSet.builder()
                        .key("quest-hello").lines(new String[]{
                                "Hey there, I'm the Auction Master.",
                                "In the §6Auction House§f, you can put your valuable items up for auction!",
                                "You may also want to check back here to see what items other players are selling to see if you can get a good deal.",
                                "Talk to me or one of the §6Auction Agents §fif you would like to start your first auction or if you want to see the items currently being sold."
                        }).build()
        ).toArray(NPCDialogue.DialogueSet[]::new);
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        if (isInDialogue(e.player())) return;
        MissionData missionData = e.player().getMissionData();

        if (missionData.isCurrentlyActive(MissionTalkToAuctionMaster.class)) {
            setDialogue(e.player(), "quest-hello").thenRun(() -> {
                missionData.endMission(MissionTalkToAuctionMaster.class);
            });
            return;
        }

        new GUIAuctionHouse().open(e.player());
    }
}
