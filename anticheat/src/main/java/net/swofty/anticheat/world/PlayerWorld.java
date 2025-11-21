package net.swofty.anticheat.world;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class PlayerWorld {
    private static final int CHUNK_SIZE = 16;
    private final Map<ChunkCoordinate, Chunk> chunks;

    private final ExecutorService executorService;
    private final ReentrantLock lock;
    private final BlockingQueue<Runnable> taskQueue;

    public PlayerWorld() {
        this.chunks = new HashMap<>();
        this.executorService = Executors.newSingleThreadExecutor();
        this.lock = new ReentrantLock();
        this.taskQueue = new LinkedBlockingQueue<>();

        executorService.submit(this::processTaskQueue);
    }

    private void processTaskQueue() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Runnable task = taskQueue.take();
                task.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public CompletableFuture<Void> updateBlock(int x, int y, int z, Block block) {
        return CompletableFuture.runAsync(() -> {
            lock.lock();
            try {
                ChunkCoordinate chunkCoord = new ChunkCoordinate(x / CHUNK_SIZE, z / CHUNK_SIZE);
                Chunk chunk = chunks.computeIfAbsent(chunkCoord, k -> new Chunk());
                chunk.setBlock(x % CHUNK_SIZE, y, z % CHUNK_SIZE, block);
            } finally {
                lock.unlock();
            }
        }, executorService);
    }

    public CompletableFuture<Block> getBlock(int x, int y, int z) {
        return CompletableFuture.supplyAsync(() -> {
            lock.lock();
            try {
                ChunkCoordinate chunkCoord = new ChunkCoordinate(x / CHUNK_SIZE, z / CHUNK_SIZE);
                Chunk chunk = chunks.get(chunkCoord);
                if (chunk == null) {
                    return null;
                }
                return chunk.getBlock(x % CHUNK_SIZE, y, z % CHUNK_SIZE);
            } finally {
                lock.unlock();
            }
        }, executorService);
    }

    public void shutdown() {
        executorService.shutdown();
    }

    private static class ChunkCoordinate {
        final int x;
        final int z;

        ChunkCoordinate(int x, int z) {
            this.x = x;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChunkCoordinate that = (ChunkCoordinate) o;
            return x == that.x && z == that.z;
        }

        @Override
        public int hashCode() {
            return 31 * x + z;
        }
    }

    private static class Chunk {
        private Block[][][] blocks;

        public Chunk() {
            this.blocks = new Block[CHUNK_SIZE][256][CHUNK_SIZE];
        }

        public void setBlock(int x, int y, int z, Block block) {
            blocks[x][y][z] = block;
        }

        public Block getBlock(int x, int y, int z) {
            return blocks[x][y][z];
        }
    }
}
