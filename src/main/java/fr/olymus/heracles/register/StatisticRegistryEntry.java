package fr.olymus.heracles.register;


import fr.olymus.heracles.stats.IStatistic;

import java.util.function.Supplier;

/**
 * Represents an entry in the IStatistic registry, containing the IStatistic's unique identifier and a supplier for creating instances of the IStatistic.
 *
 * @param id The unique identifier for the IStatistic.
 * @param supplier A supplier that provides instances of the IStatistic when requested.
 */
public record StatisticRegistryEntry(String id, Supplier<? extends IStatistic> supplier) {

    /**
     * Constructs a new StatisticRegistryEntry with the specified unique identifier and supplier.
     * @param id The unique identifier for the IStatistic.
     * @param supplier A supplier that provides instances of the IStatistic when requested.
      * @throws IllegalArgumentException if id is null or if supplier is null.
     */
    public StatisticRegistryEntry {
        if (id == null) throw new IllegalArgumentException("IStatistic id cannot be null");
        if (supplier == null) throw new IllegalArgumentException("IStatistic supplier cannot be null");
    }

    /**
     * Creates a new instance of the IStatistic using the supplier.
     * @return A new instance of the IStatistic provided by the supplier.
      * @throws RuntimeException if the supplier fails to create an instance.
      * @throws IllegalStateException if the created instance is null or not an instance of IStatistic
     */
    public IStatistic createInstance() {
        return supplier.get();
    }



}
