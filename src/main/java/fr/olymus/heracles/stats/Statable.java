package fr.olymus.heracles.stats;

import java.util.UUID;

/**
 * An interface for Objects that can be tracked and have statistics associated with them.
 * This interface provides a method to retrieve the unique identifier (UUID) of the object.
 * Implementing classes can use this UUID to store and retrieve statistics related to the object.
 */
public interface Statable {

    /**
     * Returns the unique identifier (UUID) of the object.
     * @return the UUID of the object
     */
    UUID uuid();

}
