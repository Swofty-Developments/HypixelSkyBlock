package net.swofty.packer;

import net.swofty.packer.packs.TestingPackDefinition;
import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.server.ResourcePackServer;

import java.io.IOException;
import java.util.concurrent.Executors;

public class HypixelPackServer {
    private static final String DEFAULT_HOST = "0.0.0.0";
    private static final int DEFAULT_PORT = 7270;

    public static void main(String[] args) throws IOException {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        for (int i = 0; i < args.length - 1; i++) {
            switch (args[i]) {
                case "-h", "--host" -> host = args[++i];
                case "-p", "--port" -> port = Integer.parseInt(args[++i]);
            }
        }

        PackDefinition definition = TestingPackDefinition.INSTANCE;
        System.out.println("Building resource pack '" + definition.getPackName() + "'...");
        System.out.println("Pack directory: " + definition.getPackDirectory());
        System.out.println("Textures directory: " + definition.getTexturesDirectory());

        HypixelPackBuilder builder = new HypixelPackBuilder(definition);
        BuiltResourcePack built = builder.build();

        System.out.println("Resource pack built. Hash: " + built.hash());

        ResourcePackServer server = ResourcePackServer.server()
                .address(host, port)
                .pack(built)
                .executor(Executors.newFixedThreadPool(4))
                .build();
        server.start();

        System.out.println("Resource pack server started on " + host + ":" + port);
        System.out.println("Pack URL: http://" + host + ":" + port + "/" + built.hash() + ".zip");
        System.out.println("Press Ctrl+C to stop.");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down pack server...");
            server.stop(5);
        }));

        try {
            Thread.currentThread().join();
        } catch (InterruptedException ignored) {}
    }
}
