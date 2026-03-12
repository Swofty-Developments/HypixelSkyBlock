package net.swofty.type.garden.npc;

import net.swofty.type.garden.gui.GUIJacobSFarmingContests;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class NPCJacob extends AbstractGardenNpc {
    private static final String TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTYwOTYwOTQwMjU0MywKICAicHJvZmlsZUlkIiA6ICJmMTA0NzMxZjljYTU0NmI0OTkzNjM4NTlkZWY5N2NjNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJ6aWFkODciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjVmMzFjMWMyMTVhNTdlOTM3ZmQ3NWFiMzU3ODJmODVlYzI0MmExYjFmOTUwYTI2YTQyYmI1ZTBhYTVjYmVkYSIKICAgIH0KICB9Cn0=";
    private static final String SIGNATURE = "q+WCbao7pfdeqzeoM228bAO5L1zhmYrlwxTz/7pY+Y+YsOPhKrKYX3pDO2XLsWqg2Zzh55wXsa2fHRWCd8p0GnYfUkVWWHBQWHy600iVH48RnSZLECnCure/bbn7auNbs7fjdtkXk0lmyn5z2lxnQFpFHy4gOODPgkpXNsLSJAnaNnOnebS1UBu4p/HVlS0jK/DLngtCK9NV/4bo4BpDXVDvZQOShSJoWSvnzVPriTla1zpVq86WKGHioFdUeCM/tTSSw5jtM32/HiQaC388Rvt1yuvKFoI5VoDAbjY5R0Kh8q8PsYKrKwUsN8SFREsCqXE1kjyG3zTkyrz8tXO5vBh9J5fPavQBdvGr9H6Vs1JgAngS5vuNl3sqfqYgTd5p2JE1JSxOJX/v4v7iTj6TKtrGA+jZ7xCDX1pexi110iEnZMm0bvJRgkwlSxXYKpzO5dZjyHqRNvl0sbQ5O7Pa0H0fSPrM1Fo/2WxY76GtYHeRucIKHgX8H264TlLtQ3QrALP9gS68Xcm4/mfYrT/YZAossLbUP2PhWvWL4xnGjRWPsu9LVHyY4oI6Z0aRBQPFBTc/v7IaucUcGmSAh4MKV4V62hMQ74sxK94vOJYYCWsI52GLyz6EWAQj3TQR8IIgGfIhKnn7UeIDbOntyisFkdsJU0iOHF1uXGYkmpayzSc=";

    public NPCJacob() {
        super("jacob", "Jacob", TEXTURE, SIGNATURE);
    }

    @Override
    public void onClick(NPCInteractEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.player();
        if (isInDialogue(player)) {
            return;
        }
        if (!hasSpoken(player)) {
            setDialogue(player, "hello").thenRun(() -> markSpoken(player));
            return;
        }
        player.openView(new GUIJacobSFarmingContests());
    }

    @Override
    protected DialogueSet[] dialogues(net.swofty.type.generic.user.HypixelPlayer player) {
        return new DialogueSet[]{
            DialogueSet.builder()
                .key("hello")
                .lines(new String[]{
                    "Every 3 SkyBlock days, I host a contest for 3 crops.",
                    "Collect at least 100 of a contest crop to earn rewards."
                })
                .build()
        };
    }
}
