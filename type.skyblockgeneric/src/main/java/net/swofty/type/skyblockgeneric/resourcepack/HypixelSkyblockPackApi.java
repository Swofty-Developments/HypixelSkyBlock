package net.swofty.type.skyblockgeneric.resourcepack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

final class HypixelSkyblockPackApi {
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(REQUEST_TIMEOUT)
            .build();

    private HypixelSkyblockPackApi() {
    }

    static Catalog fetch(String apiUrl) throws IOException, InterruptedException {
        URI uri = URI.create(apiUrl);
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(REQUEST_TIMEOUT)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Hypixel API returned HTTP " + response.statusCode());
        }

        return parse(response.body());
    }

    static Catalog parse(String responseBody) {
        JSONObject response = new JSONObject(responseBody);
        if (!response.optBoolean("success")) {
            throw new IllegalStateException("Hypixel API returned an unsuccessful response");
        }

        JSONArray packs = response.optJSONArray("packs");
        if (packs == null) {
            throw new IllegalStateException("Hypixel API response did not contain packs");
        }

        for (int i = 0; i < packs.length(); i++) {
            JSONObject pack = packs.optJSONObject(i);
            if (pack != null && "SkyBlock".equals(pack.optString("id"))) {
                return parseCatalog(pack);
            }
        }

        throw new IllegalStateException("Hypixel API response did not contain the SkyBlock pack");
    }

    private static Catalog parseCatalog(JSONObject pack) {
        JSONArray versions = pack.optJSONArray("versions");
        if (versions == null) {
            throw new IllegalStateException("Hypixel API response did not contain SkyBlock pack versions");
        }

        NavigableMap<Integer, Version> byPackFormat = new TreeMap<>();
        for (int i = 0; i < versions.length(); i++) {
            JSONObject version = versions.optJSONObject(i);
            if (version == null) {
                continue;
            }

            String url = version.optString("url").trim();
            String hash = version.optString("hash").trim();
            if (!version.has("packFormat") || url.isEmpty() || hash.isEmpty()) {
                continue;
            }

            int packFormat = version.getInt("packFormat");
            URI.create(url);
            byPackFormat.put(packFormat, new Version(packFormat, url, hash));
        }

        if (byPackFormat.isEmpty()) {
            throw new IllegalStateException("Hypixel API response did not contain usable SkyBlock pack versions");
        }

        return new Catalog(byPackFormat);
    }

    record Version(int packFormat, String url, String hash) {
        Version {
            Objects.requireNonNull(url, "url");
            Objects.requireNonNull(hash, "hash");
        }
    }

    static final class Catalog {
        private final NavigableMap<Integer, Version> versions;

        private Catalog(NavigableMap<Integer, Version> versions) {
            this.versions = Collections.unmodifiableNavigableMap(new TreeMap<>(versions));
        }

        Version latest() {
            return versions.lastEntry().getValue();
        }

        Version forProtocol(int protocolVersion) {
            int packFormat = HypixelPackFormatResolver.packFormatForProtocol(protocolVersion);
            if (packFormat == Integer.MAX_VALUE) {
                return latest();
            }
            if (packFormat <= 0) {
                return null;
            }
            return versions.get(packFormat);
        }

    }
}
