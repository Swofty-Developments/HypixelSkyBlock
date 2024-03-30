package net.swofty.type.village.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.village.gui.GUITiaTheFairy;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;

public class NPCTiaTheFairy extends SkyBlockNPC {
    public NPCTiaTheFairy() {
        super(new NPCParameters() {
            @Override
            public String[] holograms() {
                return new String[]{"§9Tia The Fairy", "§e§lCLICK"};
            }

            @Override
            public String signature() {
                return "Jrf7HqW8Dj956V2RFd/8BPaiYICriEJBNJvz4bISRLMWPov15IhtVQlBj8aOF4ka5Q1oeI8Vj+Bm0BU+4JCNJ72Lr/pQR3ozU7uKxn6o+0N4+kyrYHbL9EfjYzyjHNmSJbAZPpNWl91EJvUb7u6LSrgdhfSHpED5uaXavi30TN7HHPdwo3NIx5A8ZdJyoCDB7k1bnO7z0IsrAv0+tLNgD/No47KVfw6kfWHwklz8RkkD7cbdjKojX3nqCvyYbrWqZbMbTNI0BqReQLB1mYBjDTQKBiGy09yJECJsn6jlszEcVY/c2VOmoCgi95aKNlLwUH864NDISnjyLgX/Z+VKgXPdzhf7IseplUr9B+2njvQucjm4rT5IT6GKHlfjTkLEnfaVrwvGI4N1NEyzG9ULSJaRdXnt5qO5UyiNBF9tXAeJ6+yPpmurbr/ZhcPPVErFogda8UKpZ0X9Q+3l+Ij6S+Z9mSNJ1kwROIQsmWDKNy3AhTytibtDzhY3WhsKO9SHseaZaaH2EnVYdwgSGOMG1DcE3u79gMfEzHYT9JZFDZPALrosXmCUC5MpbJz7L44+OSgQx4YHfZl/8iGdxOolXf0n8XmaVEPQ/NVpTm3n2QeYiK4OBOSdfa6BPtO/aZiFbOG5kqvJ+JEg222UZgEDtVdYKdFdc06d6KZ04WC0LRA=";
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTY4MzgyNjIyOTU3MywKICAicHJvZmlsZUlkIiA6ICI0YjBhOTI4NDY2NDY0NmRlYjY5NDJiZTIzZTlhMWQyZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJJc2FhY0FsdCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83M2YwNzI5NDBhOTQyYjk1M2EzZjlkMTM5MWZmMzQyYjdkMTkwNzZmN2ExYTNlOTQ0MDQwZDY0MjBjN2Q0OTgwIgogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position() {
                return new Pos(129.5, 66, 137.5, 125, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        new GUITiaTheFairy().open(e.player());
    }
}
