package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.village.gui.GUIShopWoolWeaverVibrant;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCWoolWeaver extends SkyBlockNPC {

    public NPCWoolWeaver() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Wool Weaver", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "XSS0gW3Fl9ReFpL9ZQ1q2urV9ZtaZzMAF2QIFkP7Tk382ZxzAYY6c03YjbpGsSqksMjVrHquV16toXJ8XrrTSVn97+MsvKsP8O1hi13y6Xhv4lGwfM18PwC4dhKv3jLJLbL+49n4Ee+v9OfWJbitupdUm/b4CszmMp1y5lfW2NaJw8CkDIQTPdu3f7EGcUGL4g3SI8CjQ1TgEavWOE57SKPMt70xuLNZ4ZKCd34pUDH7C451BOST51MAfGJcLoFcw68lkBMl2zzFYj8kKLP96T0l3BCD/McmH+Rxy0edV4r+hP7sLXNlKvV+hY6iOpM9RfLU4wj/xVkfGj30d00F4RhVUSK1G1XXNq25IABKYBJKu45cgwbM/KxUZNTv1kchGUr3iNh2MMsgk7DdH3ZjnefBqgwH187VVV7hisFDx5mDBYseI508hDAGHO2aHgDrWbJnsBjX0XqrYTsmFjok1Oe/igCo11wfh1S8MzTsB5VXDX6B3/FhBp1mL5CBDSro/+MKKGh0U9NZi/F8CNclLGjD2kPaVtiQqcwH+UCYcSSPS+A/BJZFjwpVdXE/xonoau0mwJpu2VX9caoJKWJjnACdAxaqcDU8xG2P5g9nETd5cwpGUJAhlCVx7cCWMbykjOWPReAQuEinsahzJullTPdarLS42Fev3WYYw3c3i64=";
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU5NTI1NjExOTkzOSwKICAicHJvZmlsZUlkIiA6ICIwNjEzY2I1Y2QxYjg0M2JjYjI4OTk1NWU4N2QzMGEyYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJicmVhZGxvYWZzcyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82NGU1ODdjMDhiMzQwM2Q3NjFjZGFmZTU0ZjRkNTMwY2JlMTRiNGM1YzJhMDFlODdlYTE4YWYxMWQwZTA0YzIwIgogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position() {
                return new Pos(-47, 74, -30, 90, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
            new GUIShopWoolWeaverVibrant().open(e.player());
    }
}
