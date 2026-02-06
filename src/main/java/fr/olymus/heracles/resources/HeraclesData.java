package fr.olymus.heracles.resources;

import fr.olymus.heracles.register.StatisticRegistryEntry;
import fr.olymus.heracles.stats.Statable;
import fr.olymus.heracles.stats.Statistic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class representing the main data structure for managing statistics in the Heracles system.
 * It contains a registry for statistics and a list of currently loaded statistics.
 */
public class HeraclesData {

    /**
     * Registry of statistics by their unique identifiers.
     */
    private Map<String, StatisticRegistryEntry> statistics;

    /**
     * Map of currently loaded statistics, where the key is the Statable instance and the value is a list of associated Statistic instances.
     */
    private Map<Statable, List<Statistic>> loadedStatistics;

    /**
     * Constructs a HeraclesData instance with an empty statistics registry and an empty list of loaded statistics.
     */
    public HeraclesData() {
        this.statistics = new ConcurrentHashMap<>();
        this.loadedStatistics = new ConcurrentHashMap<>();
    }

    /**
     * Retrieves the statistics registry.
     *
     * @return A map containing the registered statistics, where the key is the statistic's unique identifier and the value is the corresponding StatisticRegistryEntry.
     */
    public Map<String, StatisticRegistryEntry> getStatistics() {
        return statistics;
    }

    /**
     * Registers a new statistic in the statistics registry.
     *
     * @param entry The StatisticRegistryEntry containing the statistic's unique identifier and supplier.
     * @throws IllegalArgumentException if entry is null or if the entry's id is null.
     * @throws IllegalStateException    if a statistic with the same id is already registered.
     */
    public void registerStatistic(StatisticRegistryEntry entry) {
        statistics.put(entry.id(), entry);
    }

    /**
     * Creates a new instance of a statistic based on the provided unique identifier and registers it in the loaded statistics.
     *
     * @param id       The unique identifier of the statistic to create.
     * @param statable The Statable instance to which the statistic will be associated.
     * @throws IllegalArgumentException if no statistic is found with the provided id.
     * @throws RuntimeException         if the statistic instance cannot be created.
     * @throws IllegalStateException    if the created statistic instance is null or not an instance of Statistic
     * @throws IllegalStateException    if a statistic with the same uuid is already loaded for the same Statable
     * @throws IllegalStateException    if the created statistic instance cannot be registered with the provided uuid and id
     */
    public void createStatistic(String id, Statable statable) {
        StatisticRegistryEntry entry = statistics.get(id);
        if (entry == null) {
            throw new IllegalArgumentException("No statistic found with id: " + id);
        }
        Statistic statistic = entry.createInstance();
        statistic.registerMeta(statable.uuid(), id);
        loadedStatistics.computeIfAbsent(statable, k -> new ArrayList<>()).add(statistic);
    }

    /**
     * Retrieves the list of statistics associated with a given Statable instance.
     *
     * @param statable The Statable instance for which to retrieve the associated statistics.
     * @return A list of Statistic instances associated with the provided Statable instance. If no statistics are associated, an empty list is returned.
     * @throws IllegalArgumentException if statable is null.
     */
    public List<Statistic> getStatisticsForStatable(Statable statable) {
        return loadedStatistics.getOrDefault(statable, new ArrayList<>());
    }

    /**
     * Retrieves the list of statistics associated with a given Statable instance by its UUID.
     * <p>
     * prefers using Statable instance for better performance, but this method can be used when only the UUID is available.
     *
     * @param statableUuid The UUID of the Statable instance for which to retrieve the associated statistics.
     * @return A list of Statistic instances associated with the provided Statable UUID. If no statistics are associated, an empty list is returned.
     * @throws IllegalArgumentException if statableUuid is null.
     */
    public List<Statistic> getStatisticsForStatable(UUID statableUuid) {
        for (Map.Entry<Statable, List<Statistic>> entry : loadedStatistics.entrySet()) {
            if (entry.getKey().uuid().equals(statableUuid)) {
                return entry.getValue();
            }
        }
        return new ArrayList<>();
    }

    /**
     * Retrieves the map of currently loaded statistics.
     *
     * @return A map where the key is a Statable instance and the value is a list of Statistic instances associated with that Statable. If no statistics are loaded, an empty map is returned.
     * @throws IllegalStateException if loadedStatistics is null (should never happen as it's initialized in the constructor).
     */
    public Map<Statable, List<Statistic>> getLoadedStatistics() {
        return loadedStatistics;
    }

}
