package net.swofty.type.thefarmingislands.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class NPCHungryHiker extends SkyBlockNPC {
    public NPCHungryHiker() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"Hungry Hiker", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "cIr8X0Mtz1Ok7wybAmWuIpersxILt4FbKNsIyDaQ3g90dd1Uq5qlvUT4CvRJigKBZZhMAeJOrRVWe+U+f4BqAXwo5V7lhLxhvBhVNYzE6+AsAh7V8kTJtMiymLtVVeRsuVWaeH2C5lMPQUHl9bzEgCPnMdLGEF4CRogVSXMoXcSj+ev6q7Vk7eNPEIPD2ws4p+N+MZJQ9lQHvh1GuCMqomW4TQ805fUJtY1d23xQ7fSVZr4M81kBEQSQWdVLoq38+LPYUr5RYxUfXYPyb10bET2mIS27blnwrEdpv+h5nFrpibCXHvxEQzfL7r8jf7jmn08pzdptbOebrnktzbsa+LQw5yHfkAgK0Z7NJMBADpeG3ek/mv5TGbIerNQo4KVx/OZNfR+jCr1gxnSKDKUB5ye59BuSqKVzi2XICvNynxnl6UfN3CQXK3iZPVXA7laFDmULftKXx5FFCQCFUzitxdOEcDO6GBhp0ZIC2FyIMGfA9attcmu6QDx0L0H6O54sDrOQRVBKfyBR5DW0isQQ6OpBVxsEQtL0ny+wZ8VAlQNuVaiYTQMp7O9J0sjJfjtgjyfGvRFHF0G+MFCH3NuY/ch8jt02CKEUDEaSSaCtbvPM/b9DtEPSUkEMrXWiVyvhLfiFuolQnfX8KXS6l6Dbq5wpTS/DQIPtUTLTDO8zLnI=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxNzAzNjc3MTUyOSwKICAicHJvZmlsZUlkIiA6ICJiMGQ3MzJmZTAwZjc0MDdlOWU3Zjc0NjMwMWNkOThjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPUHBscyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kNmY5YjliNjI0MDRjNWNmOTRjMzg1YzdmMWM4NmFkZGUwZDlhZWY5YTljZDI5ZTJhYjU4M2ZhMWRlNjNmNDI2IgogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(269, 48, -480, -130, 0);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        e.player().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Swofty-Developments/HypixelSkyBlock")));
    }
}
