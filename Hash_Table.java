import java.io.*;
import java.util.Scanner;

//Implementation using Hash Table for the Map ADT
public class AHSAPA3 {
    //Implementation of Map interface using separate chaining
    
    private static class HashTableMap<K, V> implements Map<K, V> {
        private static class Node<K, V> {
            K key;
            V value;
            Node<K, V> next;
            
            Node(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }
        
        private Node<K, V>[] table;
        private int size;
        private static final int INITIAL_CAPACITY = 101;
        private static final double LOAD_FACTOR_THRESHOLD = 0.75;
        
        @SuppressWarnings("unchecked")
        public HashTableMap() {
            table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
            size = 0;
        }
        
        @Override
        public int size() {
            return size;
        }
        
        @Override
        public boolean empty() {
            return size == 0;
        }
        
        @Override
        public V find(K key) {
            int index = hash(key);
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.key.equals(key)) {
                    return current.value;
                }
                current = current.next;
            }
            return null;
        }
        
        @Override
        public V insert(K key, V value) {
            // Check if resize is needed
            if ((double) size / table.length >= LOAD_FACTOR_THRESHOLD) {
                resize();
            }
            
            int index = hash(key);
            Node<K, V> current = table[index];
            
            // Check if key already exists
            while (current != null) {
                if (current.key.equals(key)) {
                    current.value = value;
                    return value;
                }
                current = current.next;
            }
            
            // Insert new node at beginning of chain
            Node<K, V> newNode = new Node<>(key, value);
            newNode.next = table[index];
            table[index] = newNode;
            size++;
            return value;
        }
        
        @Override
        public void remove(K key) {
            int index = hash(key);
            Node<K, V> current = table[index];
            Node<K, V> prev = null;
            
            while (current != null) {
                if (current.key.equals(key)) {
                    if (prev == null) {
                        table[index] = current.next;
                    } else {
                        prev.next = current.next;
                    }
                    size--;
                    return;
                }
                prev = current;
                current = current.next;
            }
            
            throw new IllegalArgumentException("Key not found");
        }
        
        private int hash(K key) {
            // Use modulo to ensure index is within bounds
            return Math.abs(key.hashCode() % table.length);
        }
        
        @SuppressWarnings("unchecked")
        private void resize() {
            Node<K, V>[] oldTable = table;
            table = (Node<K, V>[]) new Node[nextPrime(2 * oldTable.length)];
            size = 0;
            
            // Rehash all entries
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    insert(node.key, node.value);
                    node = node.next;
                }
            }
        }
        
        private int nextPrime(int n) {
            while (!isPrime(n)) n++;
            return n;
        }
        
        private boolean isPrime(int n) {
            if (n <= 1) return false;
            if (n <= 3) return true;
            if (n % 2 == 0 || n % 3 == 0) return false;
            
            for (int i = 5; i * i <= n; i += 6) {
                if (n % i == 0 || n % (i + 2) == 0) return false;
            }
            return true;
        }
    }
    
    public static void main(String[] args) {
        try {
            // Create Scanner for user input
            Scanner scanner = new Scanner(System.in);
            
            // Prompt for window size
            System.out.print("Enter window size: ");
            int windowSize = scanner.nextInt();
            if (windowSize < 1) {
                System.out.println("Window size must be positive");
                return;
            }
            
            // Prompt for output length
            System.out.print("Enter output length: ");
            int outputLength = scanner.nextInt();
            if (outputLength < 1) {
                System.out.println("Output length must be positive");
                return;
            }
            
            // Read input file
            String inputText = readFile("merchant.txt");
            if (inputText == null) return;
            
            // Create map for character distributions
            Map<String, CharDistribution> distributions = new HashTableMap<>();
            
            // Process input text
            for (int i = 0; i <= inputText.length() - windowSize; i++) {
                String window = inputText.substring(i, i + windowSize);
                CharDistribution dist = distributions.find(window);
                if (dist == null) {
                    dist = new CharDistribution();
                    distributions.insert(window, dist);
                }
                if (i + windowSize < inputText.length()) {
                    dist.occurs(inputText.charAt(i + windowSize));
                }
            }
            
            // Generate output
            StringBuilder output = new StringBuilder();
            
            // First w characters from input
            output.append(inputText.substring(0, windowSize));
            
            // Generate remaining characters
            while (output.length() < outputLength) {
                String window = output.substring(output.length() - windowSize);
                CharDistribution dist = distributions.find(window);
                if (dist == null) {
                    // If window not found, use a random character
                    output.append('a');
                } else {
                    output.append(dist.getRandomChar());
                }
            }
            
            // Write output to file
            writeFile("output_hash.txt", output.toString());
            System.out.println("Output written to output_hash.txt");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String readFile(String filename) {
        try {
            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line.toLowerCase());
            }
            reader.close();
            return content.toString();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }
    }
    
    private static void writeFile(String filename, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
} 