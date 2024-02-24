package net.swofty.commons.bazaar;

import java.util.HashMap;
import java.util.Map;

public record BazaarInitializationRequest(Map<String, Map.Entry<Double, Double>> itemsToInitialize) { }
