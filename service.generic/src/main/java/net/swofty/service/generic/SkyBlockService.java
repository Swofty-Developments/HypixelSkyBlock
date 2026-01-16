package net.swofty.service.generic;

import net.swofty.commons.ServiceType;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public interface SkyBlockService {
    ServiceType getType();

    List<ServiceEndpoint> getEndpoints();

    static void init(SkyBlockService service) {
        new ServiceInitializer(service).init();
    }

    default <T> Stream<T> loopThroughPackage(String packageName, Class<T> clazz) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(clazz);

        return subTypes.stream()
                .map(subClass -> {
                    try {
                        return clazz.cast(subClass.getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull);
    }
}
