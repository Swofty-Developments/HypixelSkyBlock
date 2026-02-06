package net.swofty.type.skyblockgeneric.chocolatefactory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minestom.server.coordinate.Pos;

@Getter
@AllArgsConstructor
public enum HoppityEggLocations {
    BAZAAR_ALLEY(new Pos(-40, 70, -74), "§din the §eBazaar Alley"),
    BLACKSMITH(new Pos(-38, 70, -135), "§dbehind the §bBlacksmith §dbuilding"),
    COLOSSEUM(new Pos(160, 97, -71), "§datop the §bColosseum"),
    WHEAT_MINION(new Pos(44, 71, -137), "§dnear the Wheat Minion"),
    FARMHOUSE(new Pos(18, 70, -76), "§dnext to the §bFarmhouse"),
    FISHERMANS_HUT(new Pos(169, 72, 36), "§dat the §bFisherman's Hut"),
    LUMBER_PILES(new Pos(-153, 74, -40), "§dnear the lumber piles"),
    EMERALD_ALTAR(new Pos(-127, 73, -126), "§don the emerald altar"),
    MOUNTAIN_PATH(new Pos(-1, 144, 51), "§don the §bMountain §dpath"),
    MOUNTAIN_PEAK(new Pos(-38, 193, 35), "§don the second highest §bMountain §dpeak"),
    RUINS(new Pos(-186, 87, 81), "§dwithin the §bRuins"),
    UNINCORPORATED(new Pos(-7, 70, 188), "§din §cUnincorporated Territory"),
    CRANE(new Pos(-61, 80, -38), "§don the crane"),
    DARK_AUCTION(new Pos(125, 74, 168), "§dnear the §5Dark Auction"),
    EMERALD_TREEHOUSE(new Pos(147, 113, 117), "§dwithin the emerald treehouse"),
    WITHER_CAGE(new Pos(161, 71, 157), "§dwithin the wither cage"),
    WIZARD_TOWER(new Pos(35, 72, 79), "§dat the base of the Wizard Tower");

    private final Pos position;
    private final String locationMessage;

    public static int totalLocations() {
        return values().length;
    }
}
