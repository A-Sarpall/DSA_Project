# CS 3345  
## Programming Assignments #2 & #3  
**University of Texas at Dallas**  
**Date Assigned:** February 26, 2025  
**Due:** March 12, 2025  

---

## Introduction

The laws of probability indicate that if you left some monkeys in a room with some typewriters for long enough, eventually they would reproduce Shakespeare. This assignment asks you to write a program that tries to speed up the process. Instead of producing an output text in which each character is chosen independently and totally at random, the program tries to "learn" from an input text about which sequences of characters are likely to occur in English.

This assignment is intended to give you experience with implementing the **Map ADT** (entries are (key, value) pairs, keys must be unique, unordered) with both a binary search tree and a hash table.

---

## Simple Case

First, suppose you want to implement a simple version of this program that just mimics the distribution of the 26 lower-case letters of the English alphabet and the space character in the input text.

- Scan through the input text, keeping track of how many times each character occurs. During this process, you will update a `CharDistribution` object using a function called `occurs(c)` that will increment a counter for character `c` every time `c` occurs. So a `CharDistribution` object is implemented with a vector of 27 counters.

- After the distribution has been initialized in this way, you can obtain characters from the `CharDistribution` object using a function `getRandomChar()`. This function will return a character chosen randomly with the appropriate frequency. For example, if the input text has 100 characters and the letter `"a"` appears 20 times, then the probability that the function returns `"a"` should be 0.2 (i.e., 20%).

- **Implementation Tip:** One way to implement `getRandomChar()`:
  1. Choose a number at random between 1 and the size of the input text, such that each number has equal probability of being chosen.
  2. Then scan through the vector of counters, keeping a cumulative sum of character occurrences.
  3. When the sum exceeds the random number, return the associated character. (You may use a different method if you prefer.)

- To generate the output text for the simple case, call `getRandomChar()` the required number of times, after initializing the `CharDistribution` object on the input text.

---

## General Case

The simple case described above generates a series of characters in which each individual character occurs with the same frequency as it does in the input text. However, the combinations of characters will not reflect the input text (i.e., will not be plausible English). To capture the frequency with which different character combinations occur, we need to alter this approach.

- When scanning the input text, you must create several different `CharDistribution` objects. The number of `CharDistribution` objects depends on the **window size `w`** provided as input to your program.

- Suppose `w = 1`. Then we will need **27** different character distributions: one for characters following `"a"`, one for `"b"`, and so on.

- In general, when the window size is `w`, potentially `27^w` different character distributions are needed. For example:
  - One distribution for characters following `"aaa...a"`, one for `"aaa...b"`, etc.

- Although `27^w` is large, **most of these distributions will not be needed**. If a window never occurs in the input, you don’t need a distribution for it.

- We will store the different distribution objects in a **map** where:
  - The **key** is the string window (e.g., `"aaa"`)
  - The **value** is the corresponding `CharDistribution` object

### Output Text Generation

1. The first `w` characters of the output are the first `w` characters of the input text.  
2. While the output has not reached the desired length:  
    - Let the `window` be the last `w` characters that have been generated  
    - Use the `window` as the key to search the map and obtain the corresponding character distribution  
    - Call `getRandomChar()` from that distribution to get the next character to generate  

---

## Map ADT

The **Map ADT** usually has the following operations:

- `Size()`: Return the number of elements in the map.  
- `Empty()`: Return `true` if the map is empty, otherwise `false`.  
- `Find(k)`: Return a pointer/reference to the element with key `k`, or null if it doesn't exist.  
- `Insert(k, v)`: Add element `(k, v)` to the map. If `k` already exists, replace the value. Return a reference to the inserted/updated element.  
- `Remove(k)`: Remove the element with key `k`. Throw an error if it doesn't exist.  

### Implementations

You will create **two implementations** of the Map ADT:

1. **Binary Search Tree (BST)**
   - Compare keys lexicographically.
   - **Bonus:** If implemented as a balanced BST, **+20 extra points**.

2. **Hash Table with Separate Chaining**
   - Experiment with hash functions and table sizes for optimal results.


> You may base your code on that in the textbook, but otherwise all code should be your own.  
> **Do not use premade data structures!**  
>  
> *Adapted with permission from Dr. Nemec and Dr. Jennifer Welch, Texas A&M University.*
