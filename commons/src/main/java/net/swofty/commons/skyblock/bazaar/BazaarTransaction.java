package net.swofty.commons.skyblock.bazaar;

import org.json.JSONObject;

public interface BazaarTransaction {
    JSONObject toJSON();
    BazaarTransaction fromJSON(JSONObject json);
}
