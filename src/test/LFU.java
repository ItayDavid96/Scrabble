package test;

import java.util.HashMap;

// LFU = Least Frequently Used - for all non-existing words in books
// uses HashMap data structure.
public class LFU implements CacheReplacementPolicy {

    private static class Node {           // A private inner class to represent a node in the cache.
        String word;
        int count;

        public Node(String word, int count) {
            this.word = word;
            this.count = count;
        }
    }

    private final HashMap<String, Node> lfu;  // A private HashMap to store the words and their counts.

    public LFU() {
        lfu = new HashMap<>();
    }

    public void add(String word) {
        if (lfu.containsKey(word))
            lfu.get(word).count++;
        else
            lfu.put(word, new Node(word, 1));
    }

    public String remove() {
        Node leastFrequent = leastFrequent();
        lfu.remove(leastFrequent.word);
        return leastFrequent.word;
    }

    private Node leastFrequent() {        // Get the node with the lowest count.
        Node leastFrequent = null;
        for (Node node : lfu.values())    // If the current node has a lower count than the least frequent node, update the least frequent node.
            if (leastFrequent == null || node.count < leastFrequent.count)  // If leastFrequent is equal returns earliest.
                leastFrequent = node;
        return leastFrequent;
    }
}