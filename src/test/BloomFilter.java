package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;


public class BloomFilter {

    private final BitSet bitSet;
    private final List<MessageDigest> hashFunctions;

    public BloomFilter(int bitSetSize, String... algorithms) {
        this.bitSet = new BitSet(bitSetSize);
        this.hashFunctions = new ArrayList<>();
        for (String alg : algorithms) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance(alg);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            hashFunctions.add(md);
        }
    }

    public void add(String word) {         // activates the hash functions from constructor on 'word' and the corresponding Bits in bitSet.
        for (MessageDigest md : hashFunctions) {
            byte[] digest = md.digest(word.getBytes());
            BigInteger bi = new BigInteger(1, digest);
            int index = bi.intValue() % bitSet.size();
            bitSet.set(index < 0 ? index * -1 : index, true);
        }
    }

    public boolean contains(String word) { // returns true if BloomFilter has word.
        for (MessageDigest md : hashFunctions) {
            byte[] digest = md.digest(word.getBytes());
            BigInteger bi = new BigInteger(1, digest);
            int index = bi.intValue() % bitSet.size();
            if (!bitSet.get(index < 0 ? index * -1 : index))  //if bit is 1 (aka->true) then word can't be in book.
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitSet.size(); i++)
            sb.append(bitSet.get(i) ? "1" : "0");
        return sb.toString();
    }
}
