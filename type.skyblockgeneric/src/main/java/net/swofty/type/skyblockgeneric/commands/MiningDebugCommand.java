package net.swofty.type.skyblockgeneric.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.item.components.AxeComponent;
import net.swofty.type.skyblockgeneric.item.components.DrillComponent;
import net.swofty.type.skyblockgeneric.item.components.HoeComponent;
import net.swofty.type.skyblockgeneric.item.components.PickaxeComponent;
import net.swofty.type.skyblockgeneric.region.mining.MineableBlock;
import net.swofty.type.skyblockgeneric.region.mining.handler.SkyBlockMiningHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CommandParameters(aliases = "mininginfo",
        description = "Debug command to display mining handler relationships",
        usage = "/miningdebug",
        permission = Rank.STAFF,
        allowsConsole = false)
public class MiningDebugCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            sender.sendMessage("§6§l=== Mining Handler Debug ===");
            sender.sendMessage("");

            // Group blocks by handler type
            Map<String, List<MineableBlock>> blocksByHandler = new HashMap<>();

            for (MineableBlock block : MineableBlock.values()) {
                SkyBlockMiningHandler handler = block.getMiningHandler();
                String handlerName = handler.getHandlerName();
                blocksByHandler.computeIfAbsent(handlerName, k -> new ArrayList<>()).add(block);
            }

            // Print blocks grouped by handler
            sender.sendMessage("§e§lBlocks by Handler Type:");
            for (Map.Entry<String, List<MineableBlock>> entry : blocksByHandler.entrySet()) {
                sender.sendMessage("");
                sender.sendMessage("§b" + entry.getKey() + " Handler §7(" + entry.getValue().size() + " blocks):");
                for (MineableBlock block : entry.getValue()) {
                    SkyBlockMiningHandler handler = block.getMiningHandler();
                    String strengthInfo = handler.breaksInstantly() ? "§aInstant" : "§7Strength: " + handler.getStrength();
                    String powerInfo = handler.getMiningPowerRequirement() > 0 ? " §7Power: " + handler.getMiningPowerRequirement() : "";
                    sender.sendMessage("  §7- §f" + block.name() + " §7(" + strengthInfo + powerInfo + "§7)");
                }
            }

            sender.sendMessage("");
            sender.sendMessage("§e§lTool Component Mappings:");

            // Print tool -> blocks relationships
            printToolBlocks(sender, "Pickaxe/Drill", PickaxeComponent.class, DrillComponent.class);
            printToolBlocks(sender, "Axe", AxeComponent.class);
            printToolBlocks(sender, "Hoe", HoeComponent.class);

            sender.sendMessage("");
            sender.sendMessage("§6§l=========================");
        });
    }

    @SafeVarargs
    private void printToolBlocks(Object sender, String toolName, Class<? extends SkyBlockItemComponent>... componentClasses) {
        List<String> breakableBlocks = new ArrayList<>();

        for (MineableBlock block : MineableBlock.values()) {
            SkyBlockMiningHandler handler = block.getMiningHandler();
            List<Class<? extends SkyBlockItemComponent>> validComponents = handler.getValidToolComponents();

            for (Class<? extends SkyBlockItemComponent> componentClass : componentClasses) {
                if (validComponents.contains(componentClass)) {
                    breakableBlocks.add(block.name());
                    break;
                }
            }
        }

        if (!breakableBlocks.isEmpty()) {
            ((net.minestom.server.command.CommandSender) sender).sendMessage("§b" + toolName + " §7can break " + breakableBlocks.size() + " blocks:");
            ((net.minestom.server.command.CommandSender) sender).sendMessage("  §7" + String.join(", ", breakableBlocks));
        } else {
            ((net.minestom.server.command.CommandSender) sender).sendMessage("§b" + toolName + " §7has no breakable blocks configured");
        }
    }
}
