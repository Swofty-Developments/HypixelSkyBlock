package net.swofty.type.ravengardgeneric.entity.animation;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RavengardMobClip {
    public static final File MOBS_DIR = new File("./configuration/ravengard/mobs");

    private static final Gson GSON = new Gson();
    private static final Map<String, RavengardMobClip> CACHE = new ConcurrentHashMap<>();

    private String name;
    private double health;
    private double walkSpeed;
    private List<Part> parts;
    private Map<String, Animation> animations;

    public static RavengardMobClip load(String name) {
        return CACHE.computeIfAbsent(name, key -> {
            try (InputStream stream = new FileInputStream(new File(MOBS_DIR, key + ".json"))) {
                return GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),
                        RavengardMobClip.class);
            } catch (Exception exception) {
                throw new IllegalStateException("Failed to load mob clip " + key, exception);
            }
        });
    }

    public static List<String> available() {
        List<String> names = new ArrayList<>();
        File[] files = MOBS_DIR.listFiles((dir, file) -> file.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                names.add(file.getName().substring(0, file.getName().length() - 5));
            }
        }
        names.sort(String::compareTo);
        return names;
    }

    public String name() {
        return name;
    }

    public double health() {
        return health;
    }

    public double walkSpeed() {
        return walkSpeed;
    }

    public List<Part> parts() {
        return parts == null ? List.of() : parts;
    }

    public Map<String, Animation> animations() {
        return animations == null ? Map.of() : animations;
    }

    public Animation animation(String key) {
        return animations().get(key);
    }

    public List<String> attackAnimations() {
        List<String> names = new ArrayList<>();
        for (String key : animations().keySet()) {
            if (key.startsWith("attack")) names.add(key);
        }
        names.sort(String::compareTo);
        return names;
    }

    public static final class Part {
        private String model;
        private String name;
        private double[] offset;
        private float[] t;
        private float[] s;
        private float[] r;
        private float[] rr;
        private int context;
        private float viewRange;

        public String model() {
            return model;
        }

        public String name() {
            return name;
        }

        public double[] offset() {
            return offset == null ? new double[]{0, 0, 0} : offset;
        }

        public float[] translation() {
            return t;
        }

        public float[] scale() {
            return s == null ? new float[]{1, 1, 1} : s;
        }

        public float[] leftRotation() {
            return r;
        }

        public float[] rightRotation() {
            return rr == null ? new float[]{0, 0, 0, 1} : rr;
        }

        public int context() {
            return context;
        }

        public float viewRange() {
            return viewRange <= 0 ? 4096f : viewRange;
        }
    }

    public static final class Animation {
        private boolean loop;
        private List<Map<String, Keyframe>> frames;

        public boolean loop() {
            return loop;
        }

        public List<Map<String, Keyframe>> frames() {
            return frames == null ? List.of() : frames;
        }
    }

    public static final class Keyframe {
        private float[] t;
        private float[] r;
        private float[] s;
        private Integer d;

        public float[] translation() {
            return t;
        }

        public float[] leftRotation() {
            return r;
        }

        public float[] scale() {
            return s;
        }

        public Integer duration() {
            return d;
        }
    }
}
