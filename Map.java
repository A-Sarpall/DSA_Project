/**
 * Map ADT interface defining the required operations for both BST and Hash Table implementations
 * @param <K> The key type
 * @param <V> The value type
 */
public interface Map<K, V> {
    /**
     * Returns the number of elements in the map
     * @return Number of elements
     */
    int size();
    
    /**
     * Checks if the map is empty
     * @return true if empty, false otherwise
     */
    boolean empty();
    
    /**
     * Finds and returns the value associated with the given key
     * @param key The key to search for
     * @return The value associated with the key, or null if not found
     */
    V find(K key);
    
    /**
     * Inserts or updates a key-value pair in the map
     * @param key The key to insert/update
     * @param value The value to associate with the key
     * @return The value that was inserted
     */
    V insert(K key, V value);
    
    /**
     * Removes the entry with the given key from the map
     * @param key The key to remove
     * @throws IllegalArgumentException if the key is not found
     */
    void remove(K key);
} 