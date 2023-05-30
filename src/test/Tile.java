package test;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Tile {

    final public char letter;
    final int score;

    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    protected Tile() {
        this.letter = 0; // <-null
        this.score = -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return letter == tile.letter && score == tile.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }

    public static class Bag extends Tile {
        int totalTiles = 98;
        int[] letterQuantity = new int[]{9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        int[] maxLetterQuantity = new int[]{9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        Tile[] letterSorted = new Tile[26];

        private Bag() {
            super();
            letterSorted[0] = new Tile('A', 1);
            letterSorted[1] = new Tile('B', 3);
            letterSorted[2] = new Tile('C', 3);
            letterSorted[3] = new Tile('D', 2);
            letterSorted[4] = new Tile('E', 1);
            letterSorted[5] = new Tile('F', 4);
            letterSorted[6] = new Tile('G', 2);
            letterSorted[7] = new Tile('H', 4);
            letterSorted[8] = new Tile('I', 1);
            letterSorted[9] = new Tile('J', 8);
            letterSorted[10] = new Tile('K', 5);
            letterSorted[11] = new Tile('L', 1);
            letterSorted[12] = new Tile('M', 3);
            letterSorted[13] = new Tile('N', 1);
            letterSorted[14] = new Tile('O', 1);
            letterSorted[15] = new Tile('P', 3);
            letterSorted[16] = new Tile('Q', 10);
            letterSorted[17] = new Tile('R', 1);
            letterSorted[18] = new Tile('S', 1);
            letterSorted[19] = new Tile('T', 1);
            letterSorted[20] = new Tile('U', 1);
            letterSorted[21] = new Tile('V', 4);
            letterSorted[22] = new Tile('W', 4);
            letterSorted[23] = new Tile('X', 8);
            letterSorted[24] = new Tile('Y', 4);
            letterSorted[25] = new Tile('Z', 10);
        }

        private static Bag tilesBag = null;

        // 'synchronized' so the code can only be executed by one thread at a time.
        public static synchronized Bag getBag() {
            if (tilesBag == null) tilesBag = new Bag();
            return tilesBag;
        }

        public Tile getRand() {
            if (totalTiles == 0) return null;
            ArrayList<Integer> checkedIndex = new ArrayList<>();
            for (int i = 0; i < 26; i++) {
                checkedIndex.add(i, 0);
            }
            int randomNum = ThreadLocalRandom.current().nextInt(0, 25 + 1);
            while (letterQuantity[randomNum] == 0 && checkedIndex.get(randomNum) == 0) {
                checkedIndex.set(randomNum, 1);
                randomNum = ThreadLocalRandom.current().nextInt(0, 25 + 1);
            }
            letterQuantity[randomNum]--;
            totalTiles--;
            return letterSorted[randomNum];
        }

        private boolean validLetter(char letter) {
            return (letter > 64 && letter < 91);
        }

        public Tile getTile(char letter) {
            if (totalTiles == 0 || !validLetter(letter)) return null;
            int index = letter - 65;
            if (letterQuantity[index] > 0) {
                letterQuantity[index]--;
                totalTiles--;
                return letterSorted[index];
            } else return null;
        }

        public void put(Tile tile) {
            if (totalTiles == 0 || !validLetter(tile.letter)) return;
            int index = tile.letter - 65;
            if (letterQuantity[index] < maxLetterQuantity[index]) letterQuantity[index]++;
        }

        public int size() {
            return totalTiles;
        }

        public int[] getQuantities() {
            int[] copyQuantities = new int[this.letterQuantity.length];
            System.arraycopy(letterQuantity, 0, copyQuantities, 0, this.letterQuantity.length);
            return copyQuantities;
        }
    }
}