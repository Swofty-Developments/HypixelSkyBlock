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

public final class RavengardReviewClip {
    public static final File REVIEW_DIR = new File("./configuration/ravengard/anim_review");

    private static final Gson GSON = new Gson();

    private String name;
    private String mob;
    private String source;
    private List<RavengardMobClip.Part> parts;
    private List<Map<String, RavengardMobClip.Keyframe>> frames;
    private List<double[]> root;
    private List<Integer> flashes;

    public static RavengardReviewClip load(String name) {
        try (InputStream stream = new FileInputStream(new File(REVIEW_DIR, name + ".json"))) {
            return GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),
                    RavengardReviewClip.class);
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to load review clip " + name, exception);
        }
    }

    public static List<String> available() {
        List<String> names = new ArrayList<>();
        File[] files = REVIEW_DIR.listFiles((dir, file) -> file.endsWith(".json"));
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

    public String mob() {
        return mob;
    }

    public String source() {
        return source;
    }

    public List<RavengardMobClip.Part> parts() {
        return parts == null ? List.of() : parts;
    }

    public List<Map<String, RavengardMobClip.Keyframe>> frames() {
        return frames == null ? List.of() : frames;
    }

    public List<double[]> root() {
        return root == null ? List.of() : root;
    }

    public List<Integer> flashes() {
        return flashes == null ? List.of() : flashes;
    }
}
