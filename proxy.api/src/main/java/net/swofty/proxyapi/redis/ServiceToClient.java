package net.swofty.proxyapi.redis;

import net.swofty.commons.service.FromServiceChannels;
import org.json.JSONObject;

public interface ServiceToClient {
    FromServiceChannels getChannel();
    JSONObject onMessage(JSONObject message);
}