package net.swofty.type.hub.hoppity;

import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.player.ResolvableProfile;
import net.swofty.type.skyblockgeneric.chocolatefactory.HoppityEggLocations;
import net.swofty.type.skyblockgeneric.chocolatefactory.HoppityEggType;
import org.json.JSONObject;

import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

public class HoppityEggEntity extends EntityCreature {

    private final HoppityEggLocations location;
    private final HoppityEggType eggType;

    public HoppityEggEntity(HoppityEggLocations location, HoppityEggType eggType) {
        super(EntityType.ARMOR_STAND);
        this.location = location;
        this.eggType = eggType;

        setInvisible(true);
        ArmorStandMeta meta = (ArmorStandMeta) getEntityMeta();
        meta.setCustomNameVisible(false);
        meta.setHasNoGravity(true);
        meta.setSmall(true);
        meta.setHasNoBasePlate(true);
        meta.setNotifyAboutChanges(false);

        String texturesEncoded = encodeTexture(eggType.getTextureHash());
        setHelmet(ItemStack.builder(Material.PLAYER_HEAD)
                .set(DataComponents.PROFILE, new ResolvableProfile(new PlayerSkin(texturesEncoded, null)))
                .build());
    }

    public void spawn(Instance instance) {
        float randomYaw = ThreadLocalRandom.current().nextFloat() * 360f;
        setInstance(instance, location.getPosition().sub(0, 1.46875, 0).withYaw(randomYaw));
    }

    private static String encodeTexture(String textureHash) {
        JSONObject json = new JSONObject();
        json.put("textures", new JSONObject().put("SKIN",
                new JSONObject().put("url", "http://textures.minecraft.net/texture/" + textureHash)));
        return Base64.getEncoder().encodeToString(json.toString().getBytes());
    }
}
