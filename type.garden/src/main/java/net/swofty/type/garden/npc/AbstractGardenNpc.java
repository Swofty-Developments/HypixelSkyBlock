package net.swofty.type.garden.npc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.swofty.type.garden.user.SkyBlockGarden;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public abstract class AbstractGardenNpc extends HypixelNPC {
    private final String npcId;

    protected AbstractGardenNpc(String npcId, String displayName, String texture, String signature) {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{displayName, "§e§lCLICK"};
            }

            @Override
            public Pos position(HypixelPlayer player) {
                if (!(player instanceof SkyBlockPlayer skyBlockPlayer)) {
                    return new Pos(0, -1000, 0);
                }
                return GardenNpcAnchorRegistry.getNpcAnchor(skyBlockPlayer, npcId)
                    .map(GardenNpcAnchorRegistry.NpcAnchor::position)
                    .orElse(new Pos(0, -1000, 0));
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }

            @Override
            public boolean visible(HypixelPlayer player) {
                return player instanceof SkyBlockPlayer skyBlockPlayer
                    && skyBlockPlayer.isOnGarden()
                    && GardenNpcAnchorRegistry.getNpcAnchor(skyBlockPlayer, npcId).isPresent();
            }

            @Override
            public String texture(HypixelPlayer player) {
                return texture;
            }

            @Override
            public String signature(HypixelPlayer player) {
                return signature;
            }

            @Override
            public Instance instance(HypixelPlayer player) {
                if (player instanceof SkyBlockPlayer skyBlockPlayer
                    && skyBlockPlayer.getSkyBlockGarden() instanceof SkyBlockGarden garden
                    && garden.getGardenInstance() != null) {
                    return garden.getGardenInstance();
                }
                return HypixelConst.getInstanceContainer();
            }
        });
        this.npcId = npcId;
    }

    protected boolean hasSpoken(SkyBlockPlayer player) {
        return net.swofty.type.garden.gui.GardenGuiSupport.personal(player).getSpokenNpcFlags().contains(npcId);
    }

    protected void markSpoken(SkyBlockPlayer player) {
        net.swofty.type.garden.gui.GardenGuiSupport.personal(player).getSpokenNpcFlags().add(npcId);
    }
}
