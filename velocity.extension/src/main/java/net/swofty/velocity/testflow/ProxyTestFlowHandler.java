package net.swofty.velocity.testflow;

/**
 * Abstract base class for proxy-side test flow handlers.
 * This runs on the proxy and can coordinate across all servers in the test flow.
 */
public abstract class ProxyTestFlowHandler {

    /**
     * Called when the test flow is first registered on the proxy
     * @param instance the test flow instance
     */
    public abstract void onTestFlowStart(TestFlowManager.ProxyTestFlowInstance instance);

    /**
     * Called when all expected servers for the test flow are ready and registered
     * @param instance the test flow instance
     */
    public abstract void onAllServersReady(TestFlowManager.ProxyTestFlowInstance instance);

    /**
     * Called when a test flow player joins any server
     * @param playerName the name of the player
     * @param instance the test flow instance
     */
    public abstract void onPlayerJoin(String playerName, TestFlowManager.ProxyTestFlowInstance instance);

    /**
     * Called when a test flow player leaves any server
     * @param playerName the name of the player
     * @param instance the test flow instance
     */
    public abstract void onPlayerLeave(String playerName, TestFlowManager.ProxyTestFlowInstance instance);

    /**
     * Called when the test flow is ended/cleaned up
     * @param instance the test flow instance
     */
    public abstract void onTestFlowEnd(TestFlowManager.ProxyTestFlowInstance instance);

    /**
     * Called periodically while the test flow is active (every 30 seconds by default)
     * Override this method if you need periodic checks or updates
     * @param instance the test flow instance
     */
    public void onPeriodicUpdate(TestFlowManager.ProxyTestFlowInstance instance) {
        // Default implementation does nothing
    }
}