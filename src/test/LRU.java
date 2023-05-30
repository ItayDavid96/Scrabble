package test;

import java.util.HashMap;
import java.util.LinkedList;

// LRU = Least Recently Used - for all existing words in books
// uses Queue data structure. default
public class LRU implements CacheReplacementPolicy {

    private static class Node {           // A class to represent a node in the linked list.
        String word;
        Node next;
        Node prev;

        public Node(String word) {
            this.word = word;
            next = null;
            prev = null;
        }
    }

    private final LinkedList<String> lru;       // A linked list to store the elements in the cache.
    private final HashMap<String, Node> map;    // A hash map to store the elements in the cache.

    public LRU() {
        lru = new LinkedList<>();
        map = new HashMap<>();
    }

    public void add(String word) {        // If the element is already in the cache, move it to the start of the list.
        if (map.containsKey(word)) {
            Node node = map.get(word);
            lru.remove(node.word);
            lru.addFirst(node.word);
        } else {                          // If the element is not in the cache, add it to the start of the list and the map.
            Node node = new Node(word);
            lru.addFirst(node.word);
            map.put(word, node);
        }
    }

    public String remove() {              // A method to remove the least recently used element from the cache.
        String word = lru.removeLast();   // Removes the least recently used element from the list, and store in 'word'.
        Node node = map.get(word);
        map.remove(node.word);
        return node.word;
    }
}
