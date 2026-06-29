import { Db, MongoClient } from "mongodb";

const globalForMongo = globalThis as typeof globalThis & {
  hypixelStoreMongoClient?: Promise<MongoClient>;
};

function mongoUri() {
  return process.env.MONGODB_URI ?? process.env.HYPIXEL_MONGODB ?? "mongodb://localhost:27017";
}

export async function mongoClient(): Promise<MongoClient> {
  if (!globalForMongo.hypixelStoreMongoClient) {
    globalForMongo.hypixelStoreMongoClient = new MongoClient(mongoUri(), {
      maxPoolSize: Number(process.env.STORE_MONGO_MAX_POOL_SIZE ?? 30),
      minPoolSize: Number(process.env.STORE_MONGO_MIN_POOL_SIZE ?? 2),
    }).connect();
  }

  return globalForMongo.hypixelStoreMongoClient;
}

export async function minestomDb(): Promise<Db> {
  const client = await mongoClient();
  return client.db(process.env.MONGODB_DATABASE ?? "Minestom");
}

export async function findPlayerByUsername(username: string) {
  const normalized = username.trim().toLowerCase();
  const db = await minestomDb();

  return db.collection("profiles").findOne<{ _id: string; ign?: string; ignLowercase?: string }>(
    {
      ignLowercase: { $in: [normalized, JSON.stringify(normalized)] },
    },
    {
      projection: { _id: 1, ign: 1, ignLowercase: 1 },
    },
  );
}
