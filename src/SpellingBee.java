import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        findWords("", letters);
    }

    public void findWords(String word, String letters) {
        // Adds current combination of letters to words list
        words.add(word);
        // Base Case: if full length word has been created it returns
        if (letters.length() == 0) {
            return;
        }
        for (int i = 0; i < letters.length(); i ++) {
            // Recursively calls this method
            // Takes away a letter from letters and adds it to the word
            findWords(word + letters.charAt(i), letters.substring(0,i) + letters.substring(i + 1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        words = mergeSort(words, 0, words.size() - 1);

    }

    public ArrayList<String> mergeSort(ArrayList<String> words, int low, int high) {
        // Base Case: if ArrayList words is 1 individual word
        if (high - low == 0) {
            ArrayList<String> newArr = new ArrayList<>();
            newArr.add(words.get(low));
            return newArr;
        }
        int med = (high + low) / 2;
        // Recursively divides the array into two halves and then merges those arrays when the base case returns a single element
        ArrayList<String> arr1 = mergeSort(words, low, med);
        ArrayList<String> arr2 = mergeSort(words, med + 1, high);
        return merge(arr1, arr2);
    }

    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        ArrayList<String> merged = new ArrayList<>();
        int i = 0, j = 0;
        while (i < arr1.size() && j < arr2.size()) {
            // Compares elements from both arrays and indexes forward once that element has been sorted
            if (arr1.get(i).compareTo(arr2.get(j)) < 0) {
                merged.add(arr1.get(i));
                i++;
            }
            else {
                merged.add(arr2.get(j));
                j++;
            }
        }
        // Adds excess elements because they have been sorted already
        while (i < arr1.size()) {
            merged.add(arr1.get(i));
            i++;
        }
        while (j < arr2.size()) {
            merged.add(arr2.get(j));
            j++;
        }
        // Returns sorted ArrayList
        return merged;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        int i = 0;
        while (i < words.size()) {
            // Removes the word from the ArrayList if it is not in the dictionary
            if (!binarySearch(words.get(i), DICTIONARY, 0, DICTIONARY_SIZE -1)){
                words.remove(i);
                i--;
            }
            i ++;
        }
    }

    public boolean binarySearch(String word, String[] dictionary, int low, int high) {
        // Base Case: returns false if the word has not been found and the whole dictionary has been searched
        if (low > high) {
            return false;
        }
        int med = (high + low) / 2;
        // Base Case: returns true if the word has been found
        if (dictionary[med].equals(word)) {
            return true;
        }
        // Sets new dictionary to the first half if the middle word is greater than the target
        if (dictionary[med].compareTo(word) > 0) {
            high = med - 1;
        }
        // Sets new dictionary to the second half if the middle word is less than the target
        else {
            low = med + 1;
        }
        // Recursively searches the new half of the dictionary
        return binarySearch(word, dictionary, low, high);
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
