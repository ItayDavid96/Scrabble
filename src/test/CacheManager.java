package test;

import java.util.HashSet;

public class CacheManager {

    HashSet<String> cache;
    CacheReplacementPolicy crpAlgo;
    int capacity;
    int numberOfElements;

    public CacheManager(int cacheMaxSize, CacheReplacementPolicy crp) {
        cache = new HashSet<>(cacheMaxSize);
        crpAlgo = crp;
        capacity = cacheMaxSize;
        numberOfElements = 0;
    }


    public boolean query(String word) {   // returns true if word in cache, false if not
        return cache.contains(word);
    }

    public void add(String word) {
        // updates the crp and adds the word to cache.
        // if as a result the cache accedes its max value -> remove the word crp returned.
        if (cache.contains(word))
            this.crpAlgo.add(word);
        else if (numberOfElements + 1 > capacity)
            cache.remove(crpAlgo.remove());
        else
            numberOfElements++;
        cache.add(word);
        crpAlgo.add(word);
    }
}
