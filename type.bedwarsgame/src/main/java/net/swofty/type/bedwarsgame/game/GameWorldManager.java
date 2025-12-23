package net.swofty.type.bedwarsgame.game;

import net.kyori.adventure.key.Key;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.gui.GUIItemShop;
import net.swofty.type.bedwarsgame.gui.GUITeamShop;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgeneric.game.BedWarsMapsConfig;
import net.swofty.type.bedwarsgeneric.game.BedWarsMapsConfig.MapTeam;
import net.swofty.type.bedwarsgeneric.game.BedWarsMapsConfig.TeamKey;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.VillagerConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import org.intellij.lang.annotations.Subst;
import org.tinylog.Logger;

import java.util.Map;

public final class GameWorldManager {
    private final Game game;
    private final InstanceContainer instanceContainer;

    public GameWorldManager(Game game) {
        this.game = game;
        this.instanceContainer = game.getInstanceContainer();
    }

    public void clearExistingBeds() {
        Map<TeamKey, MapTeam> teams = game.getMapEntry().getConfiguration().getTeams();

        for (MapTeam team : teams.values()) {
            BedWarsMapsConfig.TwoBlockPosition bedPositions = team.getBed();
            if (bedPositions != null) {
                clearBedBlocks(bedPositions);
            }
        }
    }

    private void clearBedBlocks(BedWarsMapsConfig.TwoBlockPosition bedPositions) {
        if (bedPositions.feet() != null) {
            instanceContainer.setBlock(
                    (int) bedPositions.feet().x(),
                    (int) bedPositions.feet().y(),
                    (int) bedPositions.feet().z(),
                    Block.AIR
            );
        }

        if (bedPositions.head() != null) {
            instanceContainer.setBlock(
                    (int) bedPositions.head().x(),
                    (int) bedPositions.head().y(),
                    (int) bedPositions.head().z(),
                    Block.AIR
            );
        }
    }

    public void placeBeds(Map<TeamKey, MapTeam> activeTeams) {
        activeTeams.forEach(this::placeBedForTeam);
    }

    private void placeBedForTeam(TeamKey teamKey, MapTeam team) {
        BedWarsMapsConfig.TwoBlockPosition bedPositions = team.getBed();
        if (bedPositions == null || bedPositions.feet() == null || bedPositions.head() == null) {
            Logger.warn("Bed position not fully defined for team: {}. Skipping bed placement.", teamKey.getName());
            return;
        }

        BedWarsMapsConfig.Position feetPos = bedPositions.feet();
        BedWarsMapsConfig.Position headPos = bedPositions.head();

        try {
            Material bedMaterial = getBedMaterialForTeam(teamKey);
            String facing = calculateBedFacing(feetPos, headPos);

            Block footBlock = bedMaterial.block()
                    .withProperty("part", "foot")
                    .withProperty("facing", facing);
            Block headBlock = bedMaterial.block()
                    .withProperty("part", "head")
                    .withProperty("facing", facing);

            instanceContainer.setBlock((int) feetPos.x(), (int) feetPos.y(), (int) feetPos.z(), footBlock);
            instanceContainer.setBlock((int) headPos.x(), (int) headPos.y(), (int) headPos.z(), headBlock);

            game.getTeamManager().setBedStatus(teamKey, true);

            Logger.debug("Placed {} bed for team {} (foot: {}, head: {}, facing: {})",
                    teamKey.getName().toLowerCase(), teamKey.getName(), feetPos, headPos, facing);

        } catch (IllegalArgumentException e) {
            Logger.error("Error placing bed for team {}: {}", teamKey.getName(), e.getMessage());
            instanceContainer.setBlock((int) feetPos.x(), (int) feetPos.y(), (int) feetPos.z(), Block.STONE);
            instanceContainer.setBlock((int) headPos.x(), (int) headPos.y(), (int) headPos.z(), Block.STONE);
        }
    }

    private Material getBedMaterialForTeam(TeamKey teamKey) {
        @Subst("red_bed") String bedBlockName = teamKey.getName().toLowerCase() + "_bed";
        Material bedMaterial = Material.fromKey(Key.key(Key.MINECRAFT_NAMESPACE, bedBlockName));

        if (bedMaterial == null) {
            Logger.warn("Could not find bed material for team: {}. Defaulting to RED_BED.", teamKey.getName());
            return Material.RED_BED;
        }

        return bedMaterial;
    }

    private String calculateBedFacing(BedWarsMapsConfig.Position feetPos, BedWarsMapsConfig.Position headPos) {
        if (headPos.x() > feetPos.x()) return "east";
        if (headPos.x() < feetPos.x()) return "west";
        if (headPos.z() > feetPos.z()) return "south";
        return "north";
    }

    public void spawnShopNPCs(Map<TeamKey, MapTeam> activeTeams) {
        activeTeams.forEach(this::spawnShopNPCsForTeam);
    }

    private void spawnShopNPCsForTeam(TeamKey teamKey, MapTeam team) {
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
                        public boolean looking() {
                            return true;
                        }

                        @Override
                        public Instance instance() {
                            return game.getInstanceContainer();
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
                        public boolean looking() {
                            return true;
                        }

                        @Override
                        public Instance instance() {
                            return game.getInstanceContainer();
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
    }

    public void respawnAllBeds() {
        Map<TeamKey, MapTeam> teams = game.getMapEntry().getConfiguration().getTeams();

        teams.forEach((teamKey, team) -> {
            if (game.getTeamManager().isBedAlive(teamKey)) {
                placeBedForTeam(teamKey, team);
            }
        });
    }
}
