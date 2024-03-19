package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCArthur extends SkyBlockNPC {

    public NPCArthur() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Arthur", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "IztNkj/HFzhYz9bLCKMw/8V09bK9v6tOZ22S046oq/4g4HWD5V57oSLWcyxji7GQrV5067zrBR7DsjDHz8oF0ZEgRkyuQdb4iRKw1pyeyrd5P6iRRNeSE0hkuFOk0/6aRvcDYibGht1CEsUB64aoo7aaw6qt7e/BiMqV6pAoiEDzpGP8Am0MtvxvXm7zNtTyDjwBEZ9yTeRWAAh+Wyfad7DcY99JMggqmsj1F0+Je08ZwF9fHAtSEI3/gg/2z5DNh8posDjWhRR6zA3NZaFN+miexAmKKGtZydgb8CvQDm7sC2MNxZQlAVRSbee9YBmAQiGDZa+kguJj9DK+W8riyVXjJLc59XLDyk/HoqNWjL0MdUIhfWMkxft1DVaOBacRnZ3Iy9DfgZUsbkHKrOPZ4pJkssDwonpjBd4OoXU6yQxXq1yebvxPSlCzcqohQG1YhEwBaWxmJRKJUyloRulpfbqsTkE5nj7McA1c0wqAQbTMuMTGtOk0sEAz6+IAMTBwafX1QKiVe3CNpxpmHoVTNnTokEGn+CzENDiy03Cm9EKWK9tAxBtEUY2kFMtHGZwT6yZH+I5phZ70OIrh0hQra4lnTMrE/xWZy6Z8tqVcKGDmOEcf0hF7m8ZhZnTteayd4bXtYTvo4dWBC5lqka+PLJ3RAkyCuCJd2+rwuLZLruc=";
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxMzg2MTM5MjcxNywKICAicHJvZmlsZUlkIiA6ICI0ZGI2MWRkOTM0Mzk0M2M0YjhhOTZiNDQwMWM3MDM1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJiZWVyYmVsbHltYW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2MyZTgxOTkwNmViMTc5NDM5YjhkZDU1NTExMzJlNTRlYjQ3MTczZTBmNDU4ODYxYWQyYThjOTM3OTE4Mzg5MSIKICAgIH0KICB9Cn0=";
            }

            @Override
            public Pos position() {
                return new Pos(51.5, 71, -136.5, 90, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        e.player().sendMessage("§cThis Feature is not there yet. §aOpen a Pull request at https://github.com/Swofty-Developments/HypixelSkyBlock to get it done quickly!");
    }

}
