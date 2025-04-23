package net.swofty.service.api.http;

import org.jetbrains.annotations.Nullable;
import spark.Request;
import spark.Response;

import java.util.Map;

public abstract class SkyBlockEndpoint {
    public abstract EndpointType getType();
    public abstract String getPath();

    public abstract @Nullable APIResponse handle(Map<String, String> headers, Request req, Response res);
}
