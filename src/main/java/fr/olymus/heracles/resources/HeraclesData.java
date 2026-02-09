package fr.olymus.heracles.resources;

import fr.olymus.heracles.register.StatisticRegistryEntry;
import fr.olymus.heracles.stats.Statable;
import fr.olymus.heracles.stats.IStatistic;

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
     * Map of currently loaded statistics, where the key is the Statable instance and the value is a list of associated IStatistic instances.
     */
    private Map<UUID, List<IStatistic>> loadedStatistics;

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
     * @throws IllegalStateException    if the created statistic instance is null or not an instance of IStatistic
     * @throws IllegalStateException    if a statistic with the same uuid is already loaded for the same Statable
     * @throws IllegalStateException    if the created statistic instance cannot be registered with the provided uuid and id
     */
    public void createStatistic(String id, Statable statable) {
        StatisticRegistryEntry entry = statistics.get(id);
        if (entry == null) {
            throw new IllegalArgumentException("No IStatistic found with id: " + id);
        }
        IStatistic IStatistic = entry.createInstance();
        IStatistic.registerMeta(statable.uuid(), id);
        loadedStatistics.computeIfAbsent(statable.uuid(), k -> new ArrayList<>()).add(IStatistic);
    }

    /**
     * Retrieves the list of statistics associated with a given Statable instance.
     *
     * @param statable The Statable instance for which to retrieve the associated statistics.
     * @return A list of IStatistic instances associated with the provided Statable instance. If no statistics are associated, an empty list is returned.
     * @throws IllegalArgumentException if statable is null.
     */
    public List<IStatistic> getStatisticsForStatable(Statable statable) {
        return loadedStatistics.getOrDefault(statable.uuid(), new ArrayList<>());
    }

    /**
     * Retrieves a specific statistic associated with a given Statable instance, identified by its unique identifier.
     *
     * @param uuid The UUID of the Statable instance for which to retrieve the specific statistic.
     * @throws IllegalArgumentException if statable is null or if statisticId is null or blank.
     */
    public List<IStatistic> getStatisticForStatable(UUID uuid) {
        return loadedStatistics.getOrDefault(uuid, new ArrayList<>());
    }

    /**
     * Retrieves the list of statistics associated with a given Statable instance by its UUID.
     * <p>
     * prefers using Statable instance for better performance, but this method can be used when only the UUID is available.
     *
     * @param statableUuid The UUID of the Statable instance for which to retrieve the associated statistics.
     * @return A list of IStatistic instances associated with the provided Statable UUID. If no statistics are associated, an empty list is returned.
     * @throws IllegalArgumentException if statableUuid is null.
     */
    public List<IStatistic> getStatisticsForStatable(UUID statableUuid) {
        for (Map.Entry<UUID, List<IStatistic>> entry : loadedStatistics.entrySet()) {
            if (entry.getKey().equals(statableUuid)) {
                return entry.getValue();
            }
        }
        return new ArrayList<>();
    }

    /**
     * Retrieves the map of currently loaded statistics.
     *
     * @return A map where the key is a Statable instance and the value is a list of IStatistic instances associated with that Statable. If no statistics are loaded, an empty map is returned.
     * @throws IllegalStateException if loadedStatistics is null (should never happen as it's initialized in the constructor).
     */
    public Map<UUID, List<IStatistic>> getLoadedStatistics() {
        return loadedStatistics;
    }

    /**
     * Destroys all statistics associated with a given Statable instance, removing them from the loaded statistics.
     *
     * @param statable The Statable instance for which to destroy all associated statistics.
     * @throws IllegalArgumentException if statable is null.
     */
    public void destroyStatistics(Statable statable) {
        loadedStatistics.remove(statable.uuid());
    }

    /**
     * Destroys all statistics associated with a given Statable instance, identified by its unique identifier, removing them from the loaded statistics.
     *
     * @param statableUuid The UUID of the Statable instance for which to destroy all associated statistics.
     * @throws IllegalArgumentException if statableUuid is null.
     */
    public void destroyStatistics(UUID statableUuid) {
        loadedStatistics.remove(statableUuid);
    }

    /**
     * Destroys a specific statistic associated with a given Statable instance, identified by its unique identifier, removing it from the loaded statistics.
     *
     * @param statable    The Statable instance for which to destroy the specific statistic.
     * @param statisticId The unique identifier of the statistic to destroy.
     * @throws IllegalArgumentException if statable is null or if statisticId is null or blank.
     */
    public void destroyStatistic(Statable statable, String statisticId) {
        List<IStatistic> stats = loadedStatistics.get(statable.uuid());
        if (stats != null) {
            stats.removeIf(stat -> stat.registerId().equals(statisticId));
            if (stats.isEmpty()) {
                loadedStatistics.remove(statable.uuid());
            }
        }
    }

    /**
     * Destroys a specific statistic associated with a given Statable instance, identified by its unique identifier, removing it from the loaded statistics.
     *
     * @param statableUuid The UUID of the Statable instance for which to destroy the specific statistic.
     * @param statisticId  The unique identifier of the statistic to destroy.
     * @throws IllegalArgumentException if statableUuid is null or if statisticId is null or blank.
     */
    public void destroyStatistic(UUID statableUuid, String statisticId) {
        List<IStatistic> stats = loadedStatistics.get(statableUuid);
        if (stats != null) {
            stats.removeIf(stat -> stat.registerId().equals(statisticId));
            if (stats.isEmpty()) {
                loadedStatistics.remove(statableUuid);
            }
        }
    }

}
