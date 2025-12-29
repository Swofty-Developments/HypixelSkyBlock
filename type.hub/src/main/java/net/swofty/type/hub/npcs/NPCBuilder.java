package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.trait.NPCAbiphoneTrait;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.gui.inventories.builder.GUIBuilder;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCBuilder extends HypixelNPC implements NPCAbiphoneTrait {

    public NPCBuilder() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Builder", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "LmyRtvfjp3d+s97W51oEwXTTanW2mVMh2RJO25h8hhm7E52oIj8RbJxqA7liIzcCo0oNzrvp0W8HZ+Yxt0SbtlmsxAtFckWVfODYWkBv3seW2nJpZgybrzeEif2sevU8sfZGqqfS7Y81aB/Ah2dfR9/jkYUqwRb2tUSewKkGQtIxZOs/H7tn8KjyKhhaSF6qzcalSQW+TA2I+c70Tk4+QMcKfIHo5EiXoZ3P6X+KVnR6VoUHbn52DKEUafFAXKMl7gvv3e8WIEbsgrOtFblscsqvUmQgR4joB476av2qDKw9IXxcu0hzhiBJ4upBXc7+RS8xBlQOQwH1xC+mEW59RvwcjhJZ1Wuqj2dlU9ctzih4/jporEiPZhsI/zmw0u8ubFd07g5eq3YpDtctCFf6CN1OF+ifVzJq5INEIJbvlk8OB2/iMbq4im+SJwYc+NCbJJEk297xbx6nDdELYT6T0RNHM3KoYrQscDEbJQ/YTVlaadUOB4Hi/YGXIqhzrnBjEioaEuIlUt45cjX2mkRVer+DY/159UkJMjG/lqEYKlHJxNCZLzEDX7EQQjYwTGwdFMa0uJXx2VN4ExKKHqrE4Vj7WiTYvNKQ+puowPG1D3yjEkwa/wHENrmd838I2e/miPMaaZI8t0J4WfkRhOS/GzeC0w+/k2gQQUH3rW9z4e8=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU5MDMzOTk0OTU3MywKICAicHJvZmlsZUlkIiA6ICJkNjBmMzQ3MzZhMTI0N2EyOWI4MmNjNzE1YjAwNDhkYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCSl9EYW5pZWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTIzMWU4ZGEzMjkxOTg3MDk1Y2YyYjhiYzFkMjY5NjE1NmMwOTBiNWUxZmFhMWEzMGYyYzRlOGEyNmZhZDhkYyIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-51, 71, -27.5, 180, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        if (isInDialogue(player)) return;
        boolean hasSpokenBefore = player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_BUILDER);

        if (!hasSpokenBefore) {
            setDialogue(player, "hello").thenRun(() -> {
                player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_BUILDER, true);
            });
            return;
        }

        new GUIBuilder().open(player);
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return new DialogueSet[] {
                DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "If you build, they will come!",
                                "Click me again to open the Builder Shop!"
                        }).build(),
        };
    }

    @Override
    public String getAbiphoneKey() {
        return "builder";
    }
}
