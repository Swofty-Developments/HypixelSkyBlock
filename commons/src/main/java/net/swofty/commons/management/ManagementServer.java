package net.swofty.commons.management;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import net.swofty.commons.config.Settings;
import org.tinylog.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public final class ManagementServer {
    private static final AtomicLong NEXT_INSTANCE_ID = new AtomicLong();

    private final long startedAtMillis = System.currentTimeMillis();
    private final long instanceId = NEXT_INSTANCE_ID.incrementAndGet();

    private ManagementServer() {
    }

    public static ManagementServer start(String componentName,
                                         Settings.ManagementSettings settings,
                                         BooleanSupplier liveSupplier,
                                         BooleanSupplier readySupplier,
                                         Supplier<String> metricsSupplier) {
        if (!settings.isEnabled()) {
            Logger.info("Management endpoints disabled for {}", componentName);
            return null;
        }

        try {
            HttpServer server = HttpServer.create(
                new InetSocketAddress(settings.getHostName(), settings.getPort()),
                0
            );
            ManagementServer managementServer = new ManagementServer();
            server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

            server.createContext("/healthz", exchange ->
                managementServer.writeState(exchange, liveSupplier.getAsBoolean(), "live"));
            server.createContext("/livez", exchange ->
                managementServer.writeState(exchange, liveSupplier.getAsBoolean(), "live"));
            server.createContext("/readyz", exchange ->
                managementServer.writeState(exchange, readySupplier.getAsBoolean(), "ready"));
            server.createContext("/metrics", exchange -> {
                boolean ready = readySupplier.getAsBoolean();
                boolean live = liveSupplier.getAsBoolean();
                String body = metricLine("hypixel_process_uptime_seconds",
                    (System.currentTimeMillis() - managementServer.startedAtMillis) / 1000.0,
                    Map.of("component", componentName)) +
                    metricLine("hypixel_process_live",
                        live ? 1 : 0,
                        Map.of("component", componentName)) +
                    metricLine("hypixel_process_ready",
                        ready ? 1 : 0,
                        Map.of("component", componentName)) +
                    metricLine("hypixel_jvm_heap_used_bytes",
                        Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(),
                        Map.of("component", componentName)) +
                    metricLine("hypixel_jvm_heap_max_bytes",
                        Runtime.getRuntime().maxMemory(),
                        Map.of("component", componentName)) +
                    metricLine("hypixel_jvm_available_processors",
                        Runtime.getRuntime().availableProcessors(),
                        Map.of("component", componentName)) +
                    metricLine("hypixel_process_info",
                        1,
                        Map.of("component", componentName, "instance", Long.toString(managementServer.instanceId))) +
                    metricsSupplier.get();
                managementServer.write(exchange, 200, body, "text/plain; version=0.0.4");
            });
            server.start();
            Logger.info("Management endpoints for {} listening on {}:{}",
                componentName, settings.getHostName(), settings.getPort());
            return managementServer;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to start management server for " + componentName, e);
        }
    }

    private void writeState(HttpExchange exchange, boolean ok, String state) throws IOException {
        write(exchange, ok ? 200 : 503, state + "=" + ok, "text/plain; charset=utf-8");
    }

    private void write(HttpExchange exchange, int status, String body, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(bytes);
        }
    }

    public static String metricLine(String name, double value, Map<String, String> labels) {
        StringBuilder builder = new StringBuilder(name);
        if (!labels.isEmpty()) {
            StringJoiner joiner = new StringJoiner(",", "{", "}");
            labels.forEach((key, rawValue) -> joiner.add(key + "=\"" + escapeLabelValue(rawValue) + "\""));
            builder.append(joiner);
        }
        builder.append(' ').append(value).append('\n');
        return builder.toString();
    }

    public static String metricLine(String name, long value, Map<String, String> labels) {
        return metricLine(name, (double) value, labels);
    }

    private static String escapeLabelValue(String value) {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"");
    }
}
