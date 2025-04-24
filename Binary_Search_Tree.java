import java.io.*;
import java.util.Scanner;

//Implementation using Binary Search Tree for the Map ADT
public class AHSAPA2 {
    //Map interface
    private static class BSTMap<K extends Comparable<K>, V> implements Map<K, V> {
        private class Node {
            K key;
            V value;
            Node left, right;
            int height;
            
            Node(K key, V value) {
                this.key = key;
                this.value = value;
                this.height = 1;
            }
        }
        
        private Node root;
        private int size;
        
        public BSTMap() {
            root = null;
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
            Node node = findNode(root, key);
            return node == null ? null : node.value;
        }
        
        private Node findNode(Node node, K key) {
            if (node == null) return null;
            int cmp = key.compareTo(node.key);
            if (cmp < 0) return findNode(node.left, key);
            if (cmp > 0) return findNode(node.right, key);
            return node;
        }
        
        @Override
        public V insert(K key, V value) {
            root = insert(root, key, value);
            return value;
        }
        
        private Node insert(Node node, K key, V value) {
            if (node == null) {
                size++;
                return new Node(key, value);
            }
            
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                node.left = insert(node.left, key, value);
            } else if (cmp > 0) {
                node.right = insert(node.right, key, value);
            } else {
                node.value = value;
                return node;
            }
            
            // Update height and balance
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
            return balance(node);
        }
        
        @Override
        public void remove(K key) {
            root = remove(root, key);
        }
        
        private Node remove(Node node, K key) {
            if (node == null) throw new IllegalArgumentException("Key not found");
            
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                node.left = remove(node.left, key);
            } else if (cmp > 0) {
                node.right = remove(node.right, key);
            } else {
                size--;
                if (node.left == null) return node.right;
                if (node.right == null) return node.left;
                
                Node successor = findMin(node.right);
                node.key = successor.key;
                node.value = successor.value;
                node.right = remove(node.right, successor.key);
            }
            
            // Update height and balance
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
            return balance(node);
        }
        
        private Node findMin(Node node) {
            while (node.left != null) node = node.left;
            return node;
        }
        
        // AVL balancing method
        private int getHeight(Node node) {
            return node == null ? 0 : node.height;
        }
        
        private int getBalance(Node node) {
            return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
        }
        
        private Node rotateRight(Node y) {
            Node x = y.left;
            Node T2 = x.right;
            x.right = y;
            y.left = T2;
            y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
            x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
            return x;
        }
        
        private Node rotateLeft(Node x) {
            Node y = x.right;
            Node T2 = y.left;
            y.left = x;
            x.right = T2;
            x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
            y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
            return y;
        }
        
        private Node balance(Node node) {
            if (node == null) return null;
            
            int balance = getBalance(node);
            
            // Left heavy
            if (balance > 1) {
                if (getBalance(node.left) < 0) {
                    node.left = rotateLeft(node.left);
                }
                return rotateRight(node);
            }
            
            // Right heavy
            if (balance < -1) {
                if (getBalance(node.right) > 0) {
                    node.right = rotateRight(node.right);
                }
                return rotateLeft(node);
            }
            
            return node;
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
            Map<String, CharDistribution> distributions = new BSTMap<>();
            
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
            writeFile("output_bst.txt", output.toString());
            System.out.println("Output written to output_bst.txt");
            
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