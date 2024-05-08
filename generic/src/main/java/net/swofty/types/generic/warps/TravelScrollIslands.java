package net.swofty.types.generic.warps;

import lombok.Getter;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public enum TravelScrollIslands {
    PRIVATE_ISLAND("home", "§bPrivate Island",
            "f151cffdaf303673531a7651b36637cad912ba485643158e548d59b2ead5011",
            (unused) -> "Your very own chunk of SkyBlock. Nice housing for your minions.", ServerType.ISLAND),
    SKYBLOCK_HUB("hub", "§bSkyBlock Hub", "9c465a5d348c53d473f8115ed8923be416f35149f73ebaf5f2b05e13401e814f", (unused) -> "Where everything happens and anything is possible.",
            ServerType.HUB, List.of(TravelScrollType.HUB_CASTLE, TravelScrollType.HUB_MUSEUM, TravelScrollType.HUB_CRYPTS)),
    ;

    private final String internalName;
    private final String descriptiveName;
    private final String texture;
    private final Function<Boolean, String> description;
    private final ServerType serverType;
    private final List<TravelScrollType> associatedScrolls = new ArrayList<>();

    private final SkillCategories associatedSkill;
    private final Integer islandTier;

    TravelScrollIslands(String internalName, String descriptiveName, String texture, Function<Boolean, String> description, ServerType serverType, SkillCategories associatedSkill, Integer islandTier) {
        this.internalName = internalName;
        this.descriptiveName = descriptiveName;
        this.texture = texture;
        this.description = description;
        this.serverType = serverType;
        this.associatedSkill = associatedSkill;
        this.islandTier = islandTier;
    }

    TravelScrollIslands(String internalName, String descriptiveName, String texture, Function<Boolean, String> description, ServerType serverType) {
        this.internalName = internalName;
        this.descriptiveName = descriptiveName;
        this.texture = texture;
        this.description = description;
        this.serverType = serverType;
        this.associatedSkill = null;
        this.islandTier = null;
    }

    TravelScrollIslands(String internalName, String descriptiveName, String texture, Function<Boolean, String> description, ServerType serverType, List<TravelScrollType> associatedScrolls) {
        this.internalName = internalName;
        this.description = description;
        this.serverType = serverType;
        this.texture = texture;
        this.descriptiveName = descriptiveName;
        this.associatedScrolls.addAll(associatedScrolls);
        this.associatedSkill = null;
        this.islandTier = null;
    }

    public void sendPlayer(SkyBlockPlayer player) {

    }

    public static @Nullable TravelScrollIslands getFromType(ServerType type) {
        for (TravelScrollIslands island : values()) {
            if (island.getServerType().equals(type)) {
                return island;
            }
        }
        return null;
    }

    public static @Nullable TravelScrollIslands getFromInternalName(String internalName) {
        for (TravelScrollIslands island : values()) {
            if (island.getInternalName().equalsIgnoreCase(internalName)) {
                return island;
            }
        }
        return null;
    }

    public static @Nullable TravelScrollIslands getFromTravelScroll(TravelScrollType type) {
        for (TravelScrollIslands island : values()) {
            if (island.getAssociatedScrolls().contains(type)) {
                return island;
            }
        }
        return null;
    }
}
