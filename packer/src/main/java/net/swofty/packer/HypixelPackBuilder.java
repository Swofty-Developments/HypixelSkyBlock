package net.swofty.packer;

import net.kyori.adventure.text.Component;
import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.metadata.pack.FormatVersion;
import team.unnamed.creative.metadata.pack.PackFormat;
import team.unnamed.creative.metadata.pack.PackMeta;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;

import java.io.File;

public class HypixelPackBuilder {
    private static final FormatVersion FORMAT_VERSION = FormatVersion.of(FormatVersion.FORMAT_26_1);

    private final PackDefinition definition;

    public HypixelPackBuilder(PackDefinition definition) {
        this.definition = definition;
    }

    public BuiltResourcePack build() {
        File packDirectory = new File(definition.getPackDirectory()).getAbsoluteFile();
        if (!packDirectory.isDirectory()) {
            throw new IllegalStateException("Pack directory does not exist: " + packDirectory.getPath());
        }

        ResourcePack pack = MinecraftResourcePackReader.minecraft()
                .readFromDirectory(packDirectory);
        pack.packMeta(PackMeta.of(PackFormat.format(FORMAT_VERSION, FORMAT_VERSION), Component.text("Hypixel")));

        return MinecraftResourcePackWriter.minecraft().build(pack);
    }

}
