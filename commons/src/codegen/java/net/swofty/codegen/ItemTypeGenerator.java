package net.swofty.codegen;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import javax.lang.model.element.Modifier;

public class ItemTypeGenerator {
    static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("Expected args: <inputDir> <outputDir>");
        }

        Path inputDir = Path.of(args[0]);
        Path outputDir = Path.of(args[1]);

        Map<String, ItemYaml> items = new TreeMap<>();

        for (Path file : findYamlFiles(inputDir)) {
            for (ItemYaml item : loadItems(file)) {
                if (item.id() == null || item.id().isBlank()) continue;
                items.put(item.id(), item);
            }
        }

        generateEnum(items.values(), outputDir);
    }

    static List<Path> findYamlFiles(Path root) throws IOException {
        if (!Files.exists(root)) return List.of();
        try (Stream<Path> stream = Files.walk(root)) {
            return stream
                    .filter(p -> p.toString().endsWith(".yml") || p.toString().endsWith(".yaml"))
                    .toList();
        }
    }

    static List<ItemYaml> loadItems(Path file) throws IOException {
        Yaml yaml = new Yaml();
        Map<String, Object> root = yaml.load(Files.newInputStream(file));
        if (root == null) return List.of();

        Object rawItems = root.get("items");
        if (!(rawItems instanceof List<?> list)) return List.of();

        List<ItemYaml> result = new ArrayList<>();
        for (Object entry : list) {
            if (!(entry instanceof Map<?, ?> item)) continue;
            Object id = item.get("id");
            Object rarity = item.get("rarity");
            Object material = item.get("material");
            if (!(id instanceof String)) continue;
            result.add(new ItemYaml(
                    (String) id,
                    material instanceof String ? (String) material : "BARRIER",
                    rarity instanceof String ? (String) rarity : null
            ));
        }
        return result;
    }

    static void generateEnum(
            Collection<ItemYaml> items,
            Path outputDir
    ) throws IOException {

        ClassName material = ClassName.get("net.minestom.server.item", "Material");
        ClassName rarity = ClassName.get("net.swofty.commons.skyblock.item", "Rarity");
        ClassName nullable = ClassName.get("org.jetbrains.annotations", "Nullable");

        TypeSpec.Builder enumBuilder = TypeSpec.enumBuilder("ItemType")
                .addModifiers(Modifier.PUBLIC);

        for (ItemYaml item : items) {
            String enumName = toEnumConstantName(item.id());
            String rarityExpr = resolveRarity(item);

            enumBuilder.addEnumConstant(
                    enumName,
                    TypeSpec.anonymousClassBuilder(
                            "$T.$L, $T.$L",
                            material,
                            item.material().toUpperCase(),
                            rarity,
                            rarityExpr
                    ).build()
            );
        }

        // Fields
        enumBuilder.addField(material, "material",
                Modifier.PUBLIC, Modifier.FINAL);
        enumBuilder.addField(rarity, "rarity",
                Modifier.PUBLIC, Modifier.FINAL);

        // Constructor
        enumBuilder.addMethod(MethodSpec.constructorBuilder()
                .addParameter(material, "material")
                .addParameter(rarity, "rarity")
                .addStatement("this.material = material")
                .addStatement("this.rarity = rarity")
                .build()
        );

        // get(String)
        enumBuilder.addMethod(MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addAnnotation(nullable)
                .returns(ClassName.bestGuess("ItemType"))
                .addParameter(String.class, "name")
                .beginControlFlow("try")
                .addStatement(
                        "return ItemType.valueOf(name.replace($S, $S).toUpperCase())",
                        "minecraft:", ""
                )
                .nextControlFlow("catch ($T e)", Exception.class)
                .addStatement("return null")
                .endControlFlow()
                .build()
        );

        enumBuilder.addMethod(MethodSpec.methodBuilder("isVanillaReplaced")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(boolean.class)
                .addParameter(String.class, "item")
                .addStatement("return get(item) != null")
                .build()
        );

        enumBuilder.addMethod(
                MethodSpec.methodBuilder("fromMaterial")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addAnnotation(nullable)
                        .returns(ClassName.bestGuess("ItemType"))
                        .addParameter(material, "material")
                        .addStatement(
                                "String materialName = material.key().value()"
                        )
                        .addStatement(
                                "String formattedItemName = $T.toNormalCase(materialName)",
                                ClassName.get("net.swofty.commons", "StringUtility")
                        )
                        .beginControlFlow("for (ItemType itemType : ItemType.values())")
                        .beginControlFlow(
                                "if (itemType.material == material && formattedItemName.equals($T.toNormalCase(itemType.getDisplayName())))",
                                ClassName.get("net.swofty.commons", "StringUtility")
                        )
                        .addStatement("return itemType")
                        .endControlFlow()
                        .endControlFlow()
                        .addStatement("return null")
                        .build()
        );

        enumBuilder.addMethod(
                MethodSpec.methodBuilder("getDisplayName")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement(
                                "return $T.toNormalCase(this.name())",
                                ClassName.get("net.swofty.commons", "StringUtility")
                        )
                        .build()
        );

        Files.createDirectories(outputDir);
        JavaFile.builder("net.swofty.commons.skyblock.item", enumBuilder.build())
                .addFileComment("AUTO-GENERATED FILE. DO NOT EDIT.")
                .build()
                .writeTo(outputDir);
    }

    private static String resolveRarity(ItemYaml item) {
        String rarityString = item.rarity();
        if (rarityString == null || rarityString.isBlank()) return "COMMON";
        return rarityString.trim().toUpperCase(Locale.ROOT);
    }

    private static String toEnumConstantName(String id) {
        if (id == null) return "UNKNOWN";
        String name = id;
        int idx = name.indexOf(':');
        if (idx >= 0) name = name.substring(idx + 1);
        name = name.trim().toUpperCase(Locale.ROOT);
        name = name.replace('-', '_');
        name = name.replaceAll("[^A-Z0-9_]", "_");
        if (name.isEmpty()) name = "UNKNOWN";
        if (Character.isDigit(name.charAt(0))) name = "_" + name;
        return name;
    }

    private record ItemYaml(String id, String material, String rarity) {
    }
}
