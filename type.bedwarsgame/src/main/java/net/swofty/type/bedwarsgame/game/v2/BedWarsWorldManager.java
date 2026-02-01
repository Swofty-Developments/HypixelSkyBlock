package net.swofty.type.bedwarsgame.game.v2;

import lombok.RequiredArgsConstructor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.MapTeam;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.gui.GUIItemShop;
import net.swofty.type.bedwarsgame.gui.GUITeamShop;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.VillagerConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;

import java.util.Map;

@RequiredArgsConstructor
public class BedWarsWorldManager {
    private final BedWarsGame game;

    public void clearExistingBeds() {
        InstanceContainer instance = game.getInstance();
        Map<TeamKey, MapTeam> teams = game.getMapEntry().getConfiguration().getTeams();

        for (MapTeam team : teams.values()) {
            BedWarsMapsConfig.TwoBlockPosition bedPos = team.getBed();
            if (bedPos != null) {
                clearBedBlocks(instance, bedPos);
            }
        }
    }

    private void clearBedBlocks(InstanceContainer instance, BedWarsMapsConfig.TwoBlockPosition bedPos) {
        if (bedPos.feet() != null) {
            instance.setBlock(
                (int) bedPos.feet().x(),
                (int) bedPos.feet().y(),
                (int) bedPos.feet().z(),
                Block.AIR
            );
        }
        if (bedPos.head() != null) {
            instance.setBlock(
                (int) bedPos.head().x(),
                (int) bedPos.head().y(),
                (int) bedPos.head().z(),
                Block.AIR
            );
        }
    }

    public void placeBeds(Map<TeamKey, MapTeam> activeTeams) {
        activeTeams.forEach(this::placeBedForTeam);
    }

    /**
     * Places a bed for a specific team.
     */
    public void placeBedForTeam(TeamKey teamKey, MapTeam team) {
        BedWarsMapsConfig.TwoBlockPosition bedPos = team.getBed();
        if (bedPos == null || bedPos.feet() == null || bedPos.head() == null) {
            Logger.warn("Bed position not defined for team: {}", teamKey.getName());
            return;
        }

        InstanceContainer instance = game.getInstance();
        BedWarsMapsConfig.Position feetPos = bedPos.feet();
        BedWarsMapsConfig.Position headPos = bedPos.head();

        try {
            Material bedMaterial = getBedMaterial(teamKey);
            String facing = calculateBedFacing(feetPos, headPos);

            Block footBlock = bedMaterial.block()
                .withProperty("part", "foot")
                .withProperty("facing", facing);
            Block headBlock = bedMaterial.block()
                .withProperty("part", "head")
                .withProperty("facing", facing);

            instance.setBlock((int) feetPos.x(), (int) feetPos.y(), (int) feetPos.z(), footBlock);
            instance.setBlock((int) headPos.x(), (int) headPos.y(), (int) headPos.z(), headBlock);

            // Mark bed as alive in team
            game.getTeam(teamKey.name()).ifPresent(t -> t.setBedAlive(true));

        } catch (Exception e) {
            Logger.error("Error placing bed for team {}: {}", teamKey.getName(), e.getMessage());
        }
    }

    private Material getBedMaterial(TeamKey teamKey) {
        return switch (teamKey) {
            case RED -> Material.RED_BED;
            case BLUE -> Material.BLUE_BED;
            case GREEN -> Material.LIME_BED;
            case YELLOW -> Material.YELLOW_BED;
            case AQUA -> Material.CYAN_BED;
            case WHITE -> Material.WHITE_BED;
            case PINK -> Material.PINK_BED;
            case GRAY -> Material.GRAY_BED;
        };
    }

    private String calculateBedFacing(BedWarsMapsConfig.Position feet, BedWarsMapsConfig.Position head) {
        double dx = head.x() - feet.x();
        double dz = head.z() - feet.z();

        if (Math.abs(dx) > Math.abs(dz)) {
            return dx > 0 ? "east" : "west";
        } else {
            return dz > 0 ? "south" : "north";
        }
    }

    public void spawnShopNPCs(Map<TeamKey, MapTeam> activeTeams) {
        activeTeams.forEach((teamKey, team) -> {
            if (team.getShop() == null) return;

            BedWarsMapsConfig.PitchYawPosition itemShopPos = team.getShop().item();
            BedWarsMapsConfig.PitchYawPosition teamShopPos = team.getShop().team();

            if (itemShopPos != null) {
                HypixelNPC shopNpc = new HypixelNPC(
                    new VillagerConfiguration() {
                        @Override
                        public String[] holograms(HypixelPlayer player) {
                            return new String[]{"§bITEM SHOP", "§e§lRIGHT CLICK"};
                        }

                        @Override
                        public Pos position(HypixelPlayer player) {
                            return new Pos(itemShopPos.x(), itemShopPos.y(), itemShopPos.z(),
                                itemShopPos.yaw(), itemShopPos.pitch());
                        }

                        @Override
                        public VillagerProfession profession() {
                            return VillagerProfession.BUTCHER;
                        }

                        @Override
                        public boolean looking(HypixelPlayer player) {
                            return true;
                        }

                        @Override
                        public Instance instance() {
                            return game.getInstance();
                        }
                    }
                ) {
                    @Override
                    public void onClick(NPCInteractEvent event) {
                        BedWarsPlayer bwPlayer = (BedWarsPlayer) event.player();
                        new GUIItemShop(bwPlayer.getGame()).open(event.player());
                    }
                };
                shopNpc.register();
            }

            if (teamShopPos != null) {
                HypixelNPC teamNpc = new HypixelNPC(
                    new VillagerConfiguration() {
                        @Override
                        public String[] holograms(HypixelPlayer player) {
                            return new String[]{"§bTEAM", "§bUPGRADES", "§e§lRIGHT CLICK"};
                        }

                        @Override
                        public Pos position(HypixelPlayer player) {
                            return new Pos(teamShopPos.x(), teamShopPos.y(), teamShopPos.z(),
                                teamShopPos.yaw(), teamShopPos.pitch());
                        }

                        @Override
                        public VillagerProfession profession() {
                            return VillagerProfession.BUTCHER;
                        }

                        @Override
                        public boolean looking(HypixelPlayer player) {
                            return true;
                        }

                        @Override
                        public Instance instance() {
                            return game.getInstance();
                        }
                    }
                ) {
                    @Override
                    public void onClick(NPCInteractEvent event) {
                        BedWarsPlayer bwPlayer = (BedWarsPlayer) event.player();
                        new GUITeamShop().open(bwPlayer);
                    }
                };
                teamNpc.register();
            }
        });
    }
}
