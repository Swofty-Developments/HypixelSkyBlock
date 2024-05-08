package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.hub.gui.GUIBuilder;
import net.swofty.types.generic.data.datapoints.DatapointToggles;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class NPCBuilder extends NPCDialogue {

    public NPCBuilder() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"Builder", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "LmyRtvfjp3d+s97W51oEwXTTanW2mVMh2RJO25h8hhm7E52oIj8RbJxqA7liIzcCo0oNzrvp0W8HZ+Yxt0SbtlmsxAtFckWVfODYWkBv3seW2nJpZgybrzeEif2sevU8sfZGqqfS7Y81aB/Ah2dfR9/jkYUqwRb2tUSewKkGQtIxZOs/H7tn8KjyKhhaSF6qzcalSQW+TA2I+c70Tk4+QMcKfIHo5EiXoZ3P6X+KVnR6VoUHbn52DKEUafFAXKMl7gvv3e8WIEbsgrOtFblscsqvUmQgR4joB476av2qDKw9IXxcu0hzhiBJ4upBXc7+RS8xBlQOQwH1xC+mEW59RvwcjhJZ1Wuqj2dlU9ctzih4/jporEiPZhsI/zmw0u8ubFd07g5eq3YpDtctCFf6CN1OF+ifVzJq5INEIJbvlk8OB2/iMbq4im+SJwYc+NCbJJEk297xbx6nDdELYT6T0RNHM3KoYrQscDEbJQ/YTVlaadUOB4Hi/YGXIqhzrnBjEioaEuIlUt45cjX2mkRVer+DY/159UkJMjG/lqEYKlHJxNCZLzEDX7EQQjYwTGwdFMa0uJXx2VN4ExKKHqrE4Vj7WiTYvNKQ+puowPG1D3yjEkwa/wHENrmd838I2e/miPMaaZI8t0J4WfkRhOS/GzeC0w+/k2gQQUH3rW9z4e8=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU5MDMzOTk0OTU3MywKICAicHJvZmlsZUlkIiA6ICJkNjBmMzQ3MzZhMTI0N2EyOWI4MmNjNzE1YjAwNDhkYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCSl9EYW5pZWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTIzMWU4ZGEzMjkxOTg3MDk1Y2YyYjhiYzFkMjY5NjE1NmMwOTBiNWUxZmFhMWEzMGYyYzRlOGEyNmZhZDhkYyIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(-51, 71, -27.5, 180, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        SkyBlockPlayer player = e.player();
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
    public NPCDialogue.DialogueSet[] getDialogueSets(SkyBlockPlayer player) {
        return new NPCDialogue.DialogueSet[] {
                NPCDialogue.DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "If you build, they will come!",
                                "Click me again to open the Builder Shop!"
                        }).build(),
        };
    }

}
