package net.swofty.commons;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class TestFlow {
    @Getter
    @Setter
    private static String currentTestFlow;

    @Getter
    @Setter
    private static List<String> currentTestPlayers;

    /**
     * Checks if any test flow is currently active on this server.
     */
    public static boolean isTestFlowActive() {
        return currentTestFlow != null;
    }

    /**
     * Checks if a specific test flow is active.
     */
    public static boolean isTestFlowActive(String testFlowName) {
        return currentTestFlow != null && currentTestFlow.equals(testFlowName);
    }

    /**
     * Gets the current test flow instance, or {@code null} if no test flow is active.
     */
    public static TestFlowInstance getTestFlowInstance() {
        if (currentTestFlow == null) return null;
        return new TestFlowInstance(currentTestFlow, currentTestPlayers);
    }

    /**
     * Sets the current test flow for this server.
     */
    public static void setCurrentTestFlow(String testFlowName, List<String> players) {
        currentTestFlow = testFlowName;
        currentTestPlayers = players;
    }

    /**
     * Clears the current test flow.
     */
    public static void clearTestFlow() {
        currentTestFlow = null;
        currentTestPlayers = null;
    }

    public record TestFlowInstance(String name, List<String> players) {
        public boolean hasPlayer(String playerName) {
            return players != null && players.contains(playerName);
        }
    }
}
