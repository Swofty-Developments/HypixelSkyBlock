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
     * Checks if any test flow is currently active on this server
     * @return true if a test flow is active
     */
    public static boolean isTestFlowActive() {
        return currentTestFlow != null;
    }

    /**
     * Checks if a specific test flow is active
     * @param testFlowName the name of the test flow
     * @return true if the specified test flow is active
     */
    public static boolean isTestFlowActive(String testFlowName) {
        return currentTestFlow != null && currentTestFlow.equals(testFlowName);
    }

    /**
     * Gets the current test flow instance
     * @return a simple TestFlowInstance or null if no test flow is active
     */
    public static TestFlowInstance getTestFlowInstance() {
        if (currentTestFlow == null) return null;
        return new TestFlowInstance(currentTestFlow, currentTestPlayers);
    }

    /**
     * Sets the current test flow for this server
     * @param testFlowName the name of the test flow
     * @param players the list of test flow players
     */
    public static void setCurrentTestFlow(String testFlowName, List<String> players) {
        currentTestFlow = testFlowName;
        currentTestPlayers = players;
    }

    /**
     * Clears the current test flow
     */
    public static void clearTestFlow() {
        currentTestFlow = null;
        currentTestPlayers = null;
    }

    /**
     * Simple test flow instance for server-side usage
     */
    @Getter
    public static class TestFlowInstance {
        private final String name;
        private final List<String> players;

        public TestFlowInstance(String name, List<String> players) {
            this.name = name;
            this.players = players;
        }

        /**
         * Checks if a player is part of this test flow
         * @param playerName the player name to check
         * @return true if the player is in this test flow
         */
        public boolean hasPlayer(String playerName) {
            return players != null && players.contains(playerName);
        }
    }
}