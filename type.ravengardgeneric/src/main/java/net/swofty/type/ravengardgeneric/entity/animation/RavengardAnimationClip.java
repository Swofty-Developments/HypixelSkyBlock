package net.swofty.type.ravengardgeneric.entity.animation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RavengardAnimationClip {
    /** Clips live alongside the other Ravengard configs. */
    public static final java.io.File ANIMATIONS_DIR =
            new java.io.File("./configuration/ravengard/npc_animations");

    private static final Gson GSON = new GsonBuilder().create();
    private static final Map<String, RavengardAnimationClip> CACHE = new ConcurrentHashMap<>();

    private String name;
    private int loopTicks;
    private int interpolationDuration;
    private double[] position;
    private Float yaw;
    private List<Part> parts;
    private List<MoveFrame> move;
    private double walkSpeed;
    private double roamRadius;

    public double walkSpeed() {
        return walkSpeed;
    }

    public double roamRadius() {
        return roamRadius <= 0 ? 8.0 : roamRadius;
    }
    private Dialogue dialogue;
    private String world;
    private String onComplete;
    private String shop;
    private String shopLine;
    private double health;

    public double[] position() {
        return position == null ? new double[]{0, 0, 0} : position;
    }

    public List<MoveFrame> move() {
        return move;
    }

    /** Facing the NPC holds when idle; the rig's rotations are relative to it. */
    public float yaw() {
        return yaw == null ? 0f : yaw;
    }

    public String world() {
        return world;
    }

    public String onComplete() {
        return onComplete;
    }

    public String shop() {
        return shop;
    }

    /** An unnumbered line some shopkeepers say as their shop opens, captured without an index. */
    public String shopLine() {
        return shopLine;
    }

    public double health() {
        return health;
    }

    public Dialogue dialogue() {
        return dialogue;
    }

    public static final class MoveFrame {
        private double[] d;
        private float y;

        public double[] delta() {
            return d == null ? new double[]{0, 0, 0} : d;
        }

        public float yaw() {
            return y;
        }
    }

    public static final class Dialogue {
        private String speaker;
        private List<Line> lines;

        public String speaker() {
            return speaker;
        }

        public List<Line> lines() {
            return lines == null ? List.of() : lines;
        }
    }

    public static final class Line {
        private int delay;
        private String text;
        private Integer index;
        private Integer total;

        public int delay() {
            return delay;
        }

        public String text() {
            return text;
        }

        public Integer index() {
            return index;
        }

        public Integer total() {
            return total;
        }
    }

    public static RavengardAnimationClip load(String name) {
        return CACHE.computeIfAbsent(name, key -> {
            try (InputStream stream = new java.io.FileInputStream(new java.io.File(ANIMATIONS_DIR, key + ".json"))) {
                if (stream == null) {
                    throw new IllegalStateException("Missing animation clip " + key);
                }
                return GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),
                        RavengardAnimationClip.class);
            } catch (Exception exception) {
                throw new IllegalStateException("Failed to load animation clip " + key, exception);
            }
        });
    }

    public String name() {
        return name;
    }

    public int loopTicks() {
        return loopTicks;
    }

    public int interpolationDuration() {
        return interpolationDuration <= 0 ? 1 : interpolationDuration;
    }

    public List<Part> parts() {
        return parts;
    }

    public static final class Part {
        private Base base;
        private List<Frame> idle;
        private List<Frame> talk;
        private List<Frame> walk;

        public Base base() {
            return base;
        }

        public List<Frame> idle() {
            return idle;
        }

        public List<Frame> talk() {
            return talk;
        }

        public List<Frame> phase(RavengardAnimationPhase phase) {
            List<Frame> frames = switch (phase) {
                case TALK -> talk;
                case WALK -> walk;
                case IDLE -> idle;
            };
            return frames == null || frames.isEmpty() ? idle : frames;
        }
    }

    public static final class Base {
        private String model;
        private float[] translation;
        private float[] scale;
        private float[] leftRotation;
        private float[] rightRotation;
        private int itemDisplayContext;
        private float viewRange;
        private int glowColor;
        private double[] offset;

        public String model() {
            return model;
        }

        public float[] translation() {
            return translation;
        }

        public float[] scale() {
            return scale;
        }

        public float[] leftRotation() {
            return leftRotation;
        }

        public float[] rightRotation() {
            return rightRotation;
        }

        public int itemDisplayContext() {
            return itemDisplayContext;
        }

        public float viewRange() {
            return viewRange;
        }

        public int glowColor() {
            return glowColor;
        }

        public double[] offset() {
            return offset == null ? new double[]{0, 0, 0} : offset;
        }
    }

    public static final class Frame {
        private float[] t;
        private float[] r;

        public float[] translation() {
            return t;
        }

        public float[] leftRotation() {
            return r;
        }
    }
}
