package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Dictionary {

    CacheManager existingWords; // For Existing Words
    LRU lru;
    CacheManager nonExistingWords; // For Not-Existing Words
    LFU lfu;
    ArrayList<String> books;
    BloomFilter BF;

    public Dictionary(String... fileNames) {
        lru = new LRU();
        existingWords = new CacheManager(400, lru);
        lfu = new LFU();
        nonExistingWords = new CacheManager(100, lfu);
        books = new ArrayList<>();
        books.addAll(Arrays.asList(fileNames));
        BF = new BloomFilter(256, "MD5", "SHA1");
        fillBloomFilter();
    }

    private void fillBloomFilter(){
        for (String book : this.books) {
            try (BufferedReader reader = new BufferedReader(new FileReader(book))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] words = line.split(" "); // Split by space
                    for (String word : words)
                        BF.add(word);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean query(String word) {
        // TODO: use the corresponded Algo with the action - if using lru use the Algo "MD5"(?) and for lfu "SHA1"(?)
        if (existingWords.query(word))  // return true if word in existing words
            return true;
        else if (nonExistingWords.query(word)) // else return false if word in non-existing words.
            return false;
        else if (BF.contains(word)) {
            BF.add(word);
            existingWords.add(word);
            return true;
        } else {
            nonExistingWords.add(word);
            return false;
        }
    }

    public boolean challenge(String word) {
        try {
            if (IOSearcher.search(word, books.toString().split(","))) { //Can use the 'replace()' func instead of 'startWith()' and 'substring()'.
                existingWords.add(word);
                return true;
            } else {
                nonExistingWords.add(word);
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}