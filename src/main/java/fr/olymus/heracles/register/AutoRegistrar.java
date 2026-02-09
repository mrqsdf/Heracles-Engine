package fr.olymus.heracles.register;

import fr.olymus.heracles.Heracles;
import fr.olymus.heracles.resources.HeraclesData;
import fr.olymus.heracles.stats.IStatistic;
import fr.olymus.heracles.stats.StatisticAnnotation;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.lang.reflect.Constructor;

/**
 * Utility class for automatically registering statistics
 * by scanning specified base packages for annotated classes.
 */
public final class AutoRegistrar {

    // Prevent instantiation
    private AutoRegistrar() {
    }


    /**
     * Registers components based on the specified type and base packages.
     *
     * @param type         The type of components to register. see {@link RegisterType}.
     * @param basePackages The base packages to scan for components.
     * @throws IllegalArgumentException if type is null or basePackages is null/empty.
     * @throws IllegalStateException    if any annotated class is invalid or cannot be instantiated.
     */
    public static void register(RegisterType type, String... basePackages) {
        if (type == null) throw new IllegalArgumentException("type cannot be null.");
        if (basePackages == null || basePackages.length == 0)
            throw new IllegalArgumentException("basePackages required.");

        HeraclesData data = Heracles.getData();

        try (ScanResult scan = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages(basePackages)
                .scan()) {

            if (type == RegisterType.ALL || type == RegisterType.STATS) {

                for (ClassInfo ci : scan.getClassesWithAnnotation(StatisticAnnotation.class.getName())) {
                    Class<?> raw = ci.loadClass();
                    if (!IStatistic.class.isAssignableFrom(raw)) {
                        throw new IllegalStateException("@StatisticAnnotation on non-Entity: " + raw.getName());
                    }
                    @SuppressWarnings("unchecked")
                    Class<? extends IStatistic> clazz = (Class<? extends IStatistic>) raw;

                    StatisticAnnotation ann = clazz.getAnnotation(StatisticAnnotation.class);

                    StatisticRegistryEntry entry = new StatisticRegistryEntry(
                            ann.id(),
                            () -> newInstance(clazz)
                    );
                    data.registerStatistic(entry);
                }
            }
        }
    }

    /**
     * Creates a new instance of the specified class using its no-argument constructor.
     *
     * @param clazz The class to instantiate.
     * @param <T>   The type of the class.
     * @return A new instance of the specified class.
     * @throws IllegalStateException if the class cannot be instantiated.
     */
    private static <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> c = clazz.getDeclaredConstructor();
            c.setAccessible(true);
            return c.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No-arg constructor required for auto-register: " + clazz.getName(), e);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot instantiate: " + clazz.getName(), e);
        }
    }
}
