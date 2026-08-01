package net.swofty.type.hub.npcs.rabbits;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointChocolateFactory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

@AllArgsConstructor
public class RabbitConfiguration extends HumanConfiguration {

    private final DatapointChocolateFactory.EmployeeType type;
    private final String texture;
    private final String signature;
    private final Pos pos;

    @Override
    public String texture(HypixelPlayer player) {
        return texture;
    }

    @Override
    public String signature(HypixelPlayer player) {
        return signature;
    }

    @Override
    public boolean visible(HypixelPlayer player) {
        if (player instanceof SkyBlockPlayer skyBlockPlayer) {
            DatapointChocolateFactory.ChocolateFactoryData data = skyBlockPlayer.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.CHOCOLATE_FACTORY, DatapointChocolateFactory.class).getValue();
            return data.getEmployees().containsKey(type);
        }
        return false;
    }

    @Override
    public Component[] hologramComponents(HypixelPlayer player) {
        if (player instanceof SkyBlockPlayer skyBlockPlayer) {
            DatapointChocolateFactory.ChocolateFactoryData data = skyBlockPlayer.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.CHOCOLATE_FACTORY, DatapointChocolateFactory.class).getValue();
            DatapointChocolateFactory.EmployeeData employee = data.getEmployees().get(type);
            if (employee != null) {
                ChocolateFactoryRank rank = ChocolateFactoryRank.fromLevel(employee.getLevel());
                return new Component[]{rank.getHologramLine(employee.getLevel()), Component.text(type.getName(), rank.getColor()), Component.text("CLICK", NamedTextColor.YELLOW).decorate(net.kyori.adventure.text.format.TextDecoration.BOLD)};
            }
        }
        return new Component[]{Component.text(type.getName(), NamedTextColor.AQUA), Component.text("CLICK", NamedTextColor.YELLOW).decorate(net.kyori.adventure.text.format.TextDecoration.BOLD)};
    }

    @Override
    public String[] holograms(HypixelPlayer player) {
        return java.util.Arrays.stream(hologramComponents(player))
                .map(component -> LegacyComponentSerializer.legacySection().serialize(component))
                .toArray(String[]::new);
    }

    @Override
    public Component chatNameComponent(HypixelPlayer player) {
        if (player instanceof SkyBlockPlayer skyBlockPlayer) {
            DatapointChocolateFactory.ChocolateFactoryData data = skyBlockPlayer.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.CHOCOLATE_FACTORY, DatapointChocolateFactory.class).getValue();
            DatapointChocolateFactory.EmployeeData employee = data.getEmployees().get(type);
            if (employee != null) {
                ChocolateFactoryRank rank = ChocolateFactoryRank.fromLevel(employee.getLevel());
                return Component.text(type.getName(), rank.getColor());
            }
        }
        return Component.text(type.getName());
    }

    @Override
    public Pos position(HypixelPlayer player) {
        return pos;
    }

    @Override
    public boolean looking(HypixelPlayer player) {
        return true;
    }
}
