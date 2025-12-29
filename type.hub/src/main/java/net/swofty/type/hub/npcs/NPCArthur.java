package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

import java.util.stream.Stream;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCArthur extends HypixelNPC {

    public NPCArthur() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Arthur", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "IztNkj/HFzhYz9bLCKMw/8V09bK9v6tOZ22S046oq/4g4HWD5V57oSLWcyxji7GQrV5067zrBR7DsjDHz8oF0ZEgRkyuQdb4iRKw1pyeyrd5P6iRRNeSE0hkuFOk0/6aRvcDYibGht1CEsUB64aoo7aaw6qt7e/BiMqV6pAoiEDzpGP8Am0MtvxvXm7zNtTyDjwBEZ9yTeRWAAh+Wyfad7DcY99JMggqmsj1F0+Je08ZwF9fHAtSEI3/gg/2z5DNh8posDjWhRR6zA3NZaFN+miexAmKKGtZydgb8CvQDm7sC2MNxZQlAVRSbee9YBmAQiGDZa+kguJj9DK+W8riyVXjJLc59XLDyk/HoqNWjL0MdUIhfWMkxft1DVaOBacRnZ3Iy9DfgZUsbkHKrOPZ4pJkssDwonpjBd4OoXU6yQxXq1yebvxPSlCzcqohQG1YhEwBaWxmJRKJUyloRulpfbqsTkE5nj7McA1c0wqAQbTMuMTGtOk0sEAz6+IAMTBwafX1QKiVe3CNpxpmHoVTNnTokEGn+CzENDiy03Cm9EKWK9tAxBtEUY2kFMtHGZwT6yZH+I5phZ70OIrh0hQra4lnTMrE/xWZy6Z8tqVcKGDmOEcf0hF7m8ZhZnTteayd4bXtYTvo4dWBC5lqka+PLJ3RAkyCuCJd2+rwuLZLruc=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxMzg2MTM5MjcxNywKICAicHJvZmlsZUlkIiA6ICI0ZGI2MWRkOTM0Mzk0M2M0YjhhOTZiNDQwMWM3MDM1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJiZWVyYmVsbHltYW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2MyZTgxOTkwNmViMTc5NDM5YjhkZDU1NTExMzJlNTRlYjQ3MTczZTBmNDU4ODYxYWQyYThjOTM3OTE4Mzg5MSIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(51.5, 71, -136.5, 0, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        if (isInDialogue(e.player())) return;

        setDialogue(e.player(), "dialogue-" + (int) (Math.random() * 7 + 1));
    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("dialogue-1").lines(new String[]{
                                "Fuel makes your Minion work harder for a limited time",
                                "There are multiple types of Fuel, like Coal, Enchanted Bread and many more!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-2").lines(new String[]{
                                "If you place your Minions poorly, they will complain!",
                                "Open their Menu and look for their Perfect Layout!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-3").lines(new String[]{
                                "Once you unlock a Minion, you also unlock all their Level up recipes!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-4").lines(new String[]{
                                "Use a §aBudget Hopper§f to make your Minions automatically sell their work once full!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-5").lines(new String[]{
                                "My favorite Minion Upgrade is the §aCompactor§f, sooooo useful!",
                                "I have one in my Minion, look!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-6").lines(new String[]{
                                "People always empty the storage of my Minion when I'm not looking!"
                        }).build(),
                DialogueSet.builder()
                        .key("dialogue-7").lines(new String[]{
                                "Minions always have 4 Upgrade Slots where you can place Fuel or else"
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}
