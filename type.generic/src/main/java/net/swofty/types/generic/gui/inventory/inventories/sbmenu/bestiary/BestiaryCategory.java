package net.swofty.types.generic.gui.inventory.inventories.sbmenu.bestiary;

import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.entity.mob.BestiaryMob;
import net.swofty.types.generic.entity.mob.mobs.hub.MobGraveyardZombie;
import net.swofty.types.generic.entity.mob.mobs.hub.MobGraveyardZombieVillager;
import net.swofty.types.generic.entity.mob.mobs.hub.MobRuinsOldWolf;
import net.swofty.types.generic.entity.mob.mobs.hub.MobRuinsWolf;
import net.swofty.types.generic.entity.mob.mobs.island.MobZombie_1;
import net.swofty.types.generic.entity.mob.mobs.island.MobZombie_2;

import java.util.List;

public enum BestiaryCategory {;
    @Getter
    public enum PRIVATE_ISLAND implements BestiaryEntry {
        ZOMBIE("§aZombie", "Brains.", Material.ZOMBIE_HEAD,"", List.of(new MobZombie_1(), new MobZombie_2())),
        ;

        private final String name, description, texture;
        private final Material material;
        private final List<BestiaryMob> mobs;

        PRIVATE_ISLAND(String name, String description, Material material, String texture, List<BestiaryMob> mobs) {
            this.name = name;
            this.description = description;
            this.material = material;
            this.texture = texture;
            this.mobs = mobs;
        }
    }

    @Getter
    public enum HUB implements BestiaryEntry {
        GRAVEYARD_ZOMBIE("§aGraveyard Zombie", "Brains.", Material.ZOMBIE_HEAD, "", List.of(new MobGraveyardZombie())),
        OLD_WOLF("§aOld Wolf", "Wolves older than the island itself.", Material.PLAYER_HEAD,"d359537c15534f61c1cd886bc118774ed22280e7cdab6613870160aad4ca39", List.of(new MobRuinsOldWolf())),
        WOLF("§aWolf", "Roaming the remains of a Castle far from its best days.", Material.PLAYER_HEAD,"f4cb7a6bf6c32c49f2589147e6f0f888e9e35875dd1ea2a8af379ca710589e6b", List.of(new MobRuinsWolf())),
        ZOMBIE_VILLAGER("§aZombie Villager", "The real enemy isn't the dead - it's the living.", Material.PLAYER_HEAD,"69198f410a10f99314aa0fbe9a3db10697bbc1c011f019507d96673c64217f5a", List.of(new MobGraveyardZombieVillager())),
        ;

        private final String name, description, texture;
        private final Material material;
        private final List<BestiaryMob> mobs;

        HUB(String name, String description, Material material, String texture, List<BestiaryMob> mobs) {
            this.name = name;
            this.description = description;
            this.material = material;
            this.texture = texture;
            this.mobs = mobs;
        }
    }
}
