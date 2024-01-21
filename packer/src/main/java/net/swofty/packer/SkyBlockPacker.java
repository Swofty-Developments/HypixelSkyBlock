package net.swofty.packer;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SkyBlockPacker {

    public static void main(String[] args) {
        PackerValues values = parseArgs(args);

        if (values.vanillaPack() == null) {
            System.out.println("Please specify a vanilla pack to use with -v or --vanilla-pack");
            return;
        }

        if (values.outputDirectory() == null) {
            System.out.println("Please specify an output directory to use with -o or --output-directory");
            return;
        }

        if (values.textureCategory() == null) {
            System.out.println("Please specify a texture category to use with -t or --texture-directory");
            return;
        }

        System.out.println("Vanilla pack: " + values.vanillaPack());
        System.out.println("Output directory: " + values.outputDirectory());
        System.out.println("Texture category: " + values.textureCategory());

        System.out.println("Starting packer...");

        if (!values.skip()) {
            // Copy vanilla pack to output directory, deleting any existing files
            System.out.println("Copying vanilla pack to output directory...");
            try {
                FileUtils.deleteDirectory(values.outputDirectory());
                FileUtils.copyDirectory(values.vanillaPack(), values.outputDirectory());
            } catch (IOException e) {
                System.out.println("Failed to copy vanilla pack to output directory");
                e.printStackTrace();
                return;
            }
        }

        // Modify lang file
        System.out.println("Modifying lang file...");
        try {
            LangModifier.modifyLangFile(values.outputDirectory());
        } catch (IOException e) {
            System.out.println("Failed to modify lang file");
            e.printStackTrace();
            return;
        }

        // Move textures into custom
        System.out.println("Moving textures into custom...");
        try {
            FileUtils.copyDirectory(values.textureCategory(), values.outputDirectory() + "/assets/skyblock/textures/");
        } catch (IOException e) {
            System.out.println("Failed to move textures into custom");
            e.printStackTrace();
            return;
        }

        // Override default.json with our textures
        System.out.println("Overriding default.json with our textures...");
        try {
            JSONObject object = SkyBlockTexture.updateTextures(new String(Files.readAllBytes(new File(values.outputDirectory() + "/assets/minecraft/font/default.json").toPath())));
            Files.write(new File(values.outputDirectory() + "/assets/minecraft/font/default.json").toPath(), object.toString().getBytes());

            // Remove all double backslash from the file and replace with a single backslash
            String defaultJson = new String(Files.readAllBytes(new File(values.outputDirectory() + "/assets/minecraft/font/default.json").toPath()));
            defaultJson = defaultJson.replaceAll("\\\\\\\\", "\\\\");

            Files.write(new File(values.outputDirectory() + "/assets/minecraft/font/default.json").toPath(), defaultJson.getBytes());
        } catch (IOException e) {
            System.out.println("Failed to override default.json with our textures");
            e.printStackTrace();
            return;
        }
    }

    private static PackerValues parseArgs(String[] args) {
        String vanillaPack = null;
        String outputDirectory = null;
        String textureCategory = null;
        boolean skip = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.startsWith("-")) {
                Options option = Options.getFromOption(arg);

                switch (option) {
                    case VANILLA_PACK -> vanillaPack = args[++i];
                    case OUTPUT_DIRECTORY -> outputDirectory = args[++i];
                    case TEXTURE_DIRECTORY -> textureCategory = args[++i];
                    case SKIP_VANILLA_CREATION -> {
                        skip = true;
                        i++;
                    }
                }
            }
        }

        return new PackerValues(vanillaPack, outputDirectory, textureCategory, skip);
    }

    enum Options {
        VANILLA_PACK("-v", "--vanilla-pack"),
        OUTPUT_DIRECTORY("-o", "--output-directory"),
        TEXTURE_DIRECTORY("-t", "--texture-directory"),
        SKIP_VANILLA_CREATION("-s", "--skip-vanilla-creation")
        ;

        private final String[] options;

        Options(String... options) {
            this.options = options;
        }

        public static Options getFromOption(String option) {
            for (Options value : values()) {
                for (String s : value.options) {
                    if (s.equals(option)) {
                        return value;
                    }
                }
            }

            return null;
        }
    }

    public record PackerValues(String vanillaPack, String outputDirectory, String textureCategory, boolean skip) { }
}
