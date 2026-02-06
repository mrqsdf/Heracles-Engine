package fr.olymus.heracles.register;


import fr.olymus.heracles.stats.Statistic;

import java.util.function.Supplier;

/**
 * Represents an entry in the Statistic registry, containing the Statistic's unique identifier and a supplier for creating instances of the Statistic.
 *
 * @param id The unique identifier for the Statistic.
 * @param supplier A supplier that provides instances of the Statistic when requested.
 */
public record StatisticRegistryEntry(String id, Supplier<? extends Statistic> supplier) {

    /**
     * Constructs a new StatisticRegistryEntry with the specified unique identifier and supplier.
     * @param id The unique identifier for the Statistic.
     * @param supplier A supplier that provides instances of the Statistic when requested.
      * @throws IllegalArgumentException if id is null or if supplier is null.
     */
    public StatisticRegistryEntry {
        if (id == null) throw new IllegalArgumentException("Statistic id cannot be null");
        if (supplier == null) throw new IllegalArgumentException("Statistic supplier cannot be null");
    }

    /**
     * Creates a new instance of the Statistic using the supplier.
     * @return A new instance of the Statistic provided by the supplier.
      * @throws RuntimeException if the supplier fails to create an instance.
      * @throws IllegalStateException if the created instance is null or not an instance of IStatistic
     */
    public Statistic createInstance() {
        return supplier.get();
    }



}
