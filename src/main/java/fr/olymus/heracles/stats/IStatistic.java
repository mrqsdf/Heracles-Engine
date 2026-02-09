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

    /**
     * Retrieves the value of this statistic based on the provided class type.
     * @param clazz A Class object representing the type of statistic to retrieve.
     * @return An instance of the specified statistic type containing the value of this statistic.
     * @param <T> The type of statistic to retrieve, which must extend IStatistic.
     */
    <T extends IStatistic> T getValue(Class<T> clazz);

}
