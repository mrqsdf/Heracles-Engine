package fr.olymus.heracles.stats;

import java.util.UUID;

/**
 * Interface representing a statistic that can be registered and tracked.
 */
public interface IStatistic {

    /**
     * Gets the unique identifier for this statistic.
     * @return A string representing the unique identifier for this statistic.
     */
    String registerId();

    /**
     * Gets the value of this statistic.
     * @return An object representing the value of this statistic.
     */
    Object value();

    /**
     * Gets the UUID associated with this statistic.
     * This is Linked to the Statable Object that is registered with this statistic.
     * @return A UUID representing the unique identifier associated with this statistic.
     */
    UUID uuid();

    /**
     * Registers the metadata for this statistic based on the provided UUID and register ID.
     * @param uuid A UUID representing the unique identifier associated with this statistic.
     * @param registerId A string representing the unique identifier for this statistic.
     */
    void registerMeta(UUID uuid, String registerId);

}
