package net.swofty.velocity.testflow.handlers;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.testflow.ProxyTestFlowHandler;
import net.swofty.velocity.testflow.TestFlowManager;
import org.tinylog.Logger;

import java.util.Optional;

public class SendToHubTestFlowHandler extends ProxyTestFlowHandler {

    @Override
    public void onTestFlowStart(TestFlowManager.ProxyTestFlowInstance instance) {

    }

    @Override
    public void onAllServersReady(TestFlowManager.ProxyTestFlowInstance instance) {

    }

    @Override
    public void onPlayerJoin(String playerName, TestFlowManager.ProxyTestFlowInstance instance) {

    }

    @Override
    public void onPlayerLeave(String playerName, TestFlowManager.ProxyTestFlowInstance instance) {

    }

    @Override
    public void onTestFlowEnd(TestFlowManager.ProxyTestFlowInstance instance) {
        System.out.println("Example test flow ended: " + instance.getName());
    }

    @Override
    public void onPeriodicUpdate(TestFlowManager.ProxyTestFlowInstance instance) {
        if (instance.getUptime() % 300000 == 0) {
            System.out.println("Test flow " + instance.getName() + " status update:");
            System.out.println("  Runtime: " + (instance.getUptime() / 1000) + " seconds");
            System.out.println("  Active servers: " + instance.getGameServers().size());
            System.out.println("  Online test players: " +
                    instance.getPlayers().stream()
                            .mapToLong(name -> SkyBlockVelocity.getServer().getPlayer(name).isPresent() ? 1 : 0)
                            .sum());
        }
    }
}