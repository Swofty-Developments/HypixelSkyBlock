package net.swofty.types.generic.utility;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.instance.block.Block;
import org.json.JSONObject;

import java.util.Base64;

public class BlockUtility {


    public static Block applyTexture(Block block , String texture){
        JSONObject json = new JSONObject();
        json.put("isPublic", true);
        json.put("signatureRequired", false);
        json.put("textures", new JSONObject().put("SKIN",
                new JSONObject().put("url", "http://textures.minecraft.net/texture/" + texture).put("metadata", new JSONObject().put("model", "slim"))));

        String texturesEncoded = Base64.getEncoder().encodeToString(json.toString().getBytes());
        block = block.withTag(ExtraItemTags.SKULL_OWNER , new ExtraItemTags.SkullOwner(null , "25" ,
                new PlayerSkin(texturesEncoded , null)));
        return block;
    }

}
