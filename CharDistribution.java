import java.util.Random;

/**
 * CharDistribution class handles tracking character frequencies and generating random characters
 * based on their distribution in the input text.
 */
public class CharDistribution {
    private int[] counters;  // Array to store counts for each character (a-z and space)
    private int totalChars;  // Total number of characters counted
    private static final Random random = new Random();
    
    /**
     * Constructor initializes the counters array for 27 characters (a-z and space)
     */
    public CharDistribution() {
        counters = new int[27];  // 26 letters + space
        totalChars = 0;
    }
    
    /**
     * Increments the counter for the specified character
     * @param c The character to count
     */
    public void occurs(char c) {
        int index = charToIndex(c);
        if (index != -1) {
            counters[index]++;
            totalChars++;
        }
    }
    
    /**
     * Converts a character to its corresponding index in the counters array
     * @param c The character to convert
     * @return The index (0-26) or -1 if invalid character
     */
    private int charToIndex(char c) {
        if (c == ' ') return 26;
        if (c >= 'a' && c <= 'z') return c - 'a';
        return -1;
    }
    
    /**
     * Converts an index to its corresponding character
     * @param index The index to convert
     * @return The corresponding character
     */
    private char indexToChar(int index) {
        if (index == 26) return ' ';
        return (char)('a' + index);
    }
    
    /**
     * Returns a random character based on the frequency distribution
     * @return A randomly selected character
     */
    public char getRandomChar() {
        if (totalChars == 0) return ' ';
        
        int target = random.nextInt(totalChars) + 1;
        int sum = 0;
        
        for (int i = 0; i < counters.length; i++) {
            sum += counters[i];
            if (sum >= target) {
                return indexToChar(i);
            }
        }
        
        return ' ';  // Should never reach here if totalChars > 0
    }
    
    /**
     * Gets the total number of characters processed
     * @return Total character count
     */
    public int getTotalChars() {
        return totalChars;
    }
} 