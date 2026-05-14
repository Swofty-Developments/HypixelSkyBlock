package net.swofty.service.bazaar;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;
import org.tinylog.Logger;

public class OrderDatabase {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> ordersCollection;

    public static void connect(String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(cs)
                .build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        ordersCollection = database.getCollection("bazaarOrders");

        Logger.info("Connected to MongoDB for bazaar orders");
    }

    public static void disconnect() {
        if (client != null) {
            client.close();
        }
    }
}