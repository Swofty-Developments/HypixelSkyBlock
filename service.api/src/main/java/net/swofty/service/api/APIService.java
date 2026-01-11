package net.swofty.service.api;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import net.swofty.commons.ServiceType;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.service.api.http.APIResponse;
import net.swofty.service.api.http.SkyBlockEndpoint;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import org.jetbrains.annotations.Nullable;
import spark.Spark;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIService implements SkyBlockService {

    public static void main(String[] args) {
        int port = 0;
        for (String arg : args) {
            if (arg.startsWith("--port=")) {
                port = Integer.parseInt(arg.substring(7));
            }
        }

        if (port == 0) {
            System.out.println("No port specified, defaulting to 8080");
            port = 8080;
        }

        APIService service = new APIService();
        Thread.startVirtualThread(() -> {
            SkyBlockService.init(service);
        });

        System.out.println("Connecting to databases...");

        ConnectionString cs = new ConnectionString(ConfigProvider.settings().getMongodb());
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        MongoClient mongoClient = MongoClients.create(settings);

        UserDatabase.connect(mongoClient);
        ProfilesDatabase.connect(mongoClient);
        new APIAdminDatabase().connect(ConfigProvider.settings().getMongodb());
        new APIKeyDatabase("_placeHolder").connect(ConfigProvider.settings().getMongodb());

        System.out.println("API Service initializing...");
        Spark.port(port);

        service.loopThroughPackage("net.swofty.service.api.http.endpoints", SkyBlockEndpoint.class).forEach(endpoint -> {
            Spark.get("/api" + endpoint.getPath(), (req, res) -> {
                res.type("application/json");

                Map<String, String> headers = new HashMap<>();
                for (String header : req.headers()) {
                    headers.put(header, req.headers(header));
                }

                @Nullable APIResponse response = endpoint.handle(headers, req, res);
                if (response == null) {
                    return APIResponse.serverError().toJson();
                }
                res.body(response.toJson());
                return response.toJson();
            });
        });

        Spark.get("/", (req, res) -> {
            String sessionId = req.cookie("sessionId");

            if (sessionId != null && !sessionId.isEmpty()) {
                APIAdminDatabase adminDatabase = new APIAdminDatabase();
                APIAdminDatabaseObject document = adminDatabase.getFromSessionId(sessionId);

                if (document != null && document.isAuthenticated()) {
                    res.redirect("/panel/authenticated");
                    return "";
                }
            }

            res.type("text/html");
            try (InputStream is = APIService.class.getResourceAsStream("/unauthenticated.html")) {
                if (is != null) {
                    return new String(is.readAllBytes(), StandardCharsets.UTF_8);
                } else {
                    res.status(404);
                    return APIResponse.serverError().toJson();
                }
            } catch (IOException e) {
                res.status(500);
                return APIResponse.serverError().toJson();
            }
        });

        Spark.get("/panel/authenticated", (req, res) -> {
            String sessionId = req.cookie("sessionId");

            if (sessionId == null || sessionId.isEmpty()) {
                res.redirect("/");
                return "";
            }

            APIAdminDatabase adminDatabase = new APIAdminDatabase();
            APIAdminDatabaseObject document = adminDatabase.getFromSessionId(sessionId);

            if (document == null) {
                res.redirect("/");
                return "";
            }

            res.type("text/html");
            try (InputStream is = APIService.class.getResourceAsStream("/authenticated.html")) {
                if (is != null) {
                    return new String(is.readAllBytes(), StandardCharsets.UTF_8);
                } else {
                    res.status(404);
                    return APIResponse.serverError().toJson();
                }
            } catch (IOException e) {
                res.status(500);
                return APIResponse.serverError().toJson();
            }
        });

        System.out.println("API Service started on port " + port);
    }

    @Override
    public ServiceType getType() {
        return ServiceType.API;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage("net.swofty.service.api.endpoints", ServiceEndpoint.class).toList();
    }
}

