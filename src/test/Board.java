package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Board { // https://en.wikibooks.org/wiki/Scrabble/Rules
    /**
     * Logic behind building the Board: <p>
     * 1. Initiate 15X15 array <p>
     * 2. Implement each square with its bonuses (switch case?):<pre>
     * Star = word_value*2
     * DL = Double Letter = Azure = letter_value*2
     * TL = Triple Letter = Blue = letter_value*3
     * DW = Double Word = Yellow = word_value*2
     * TW = Triple Word = Red = word_value*3
     * Green = no bonus</pre>
     */
    public static class Cell {

        private Tile tile;
        private String bonus;
        private boolean bonusUsed;

        public Cell() {
            tile = null;
            bonus = "None";
            bonusUsed = false;
        }

        public Tile getTile() {
            return tile;
        }

        protected void setTile(Tile tile) {
            this.tile = tile;
        }

        public String getBonus() {
            return bonus;
        }

        private void setBonus(String bonus) {
            this.bonus = bonus;
        }

        public boolean isBonusUsed() {
            return bonusUsed;
        }

        protected void setBonusToUsed() {
            this.bonusUsed = true;
        }
    }

    private final static int ROW_SIZE = 15;
    private final static int COL_SIZE = 15;
    Cell[][] boardMatrix; // Represents the squares of the Scrabble Board (like board bonuses)
    Tile[][] tilesMatrix; // Represents all the tiles that are on the board.
    ArrayList<Word> wordsOnBoard;  // List of all the words currently on board OR has been used (like FARM -> FARMS, both will be in the list)

    private Board() {
        super();
        buildBoard();
    }

    private static Board gameBoard = null;

    // 'synchronized' so the code can only be executed by one thread at a time.
    public static synchronized Board getBoard() {
        if (gameBoard == null) gameBoard = new Board();
        return gameBoard;
    }

    private void buildBoard() {
        this.boardMatrix = new Cell[ROW_SIZE][COL_SIZE];
        this.tilesMatrix = new Tile[ROW_SIZE][ROW_SIZE];
        for (int row = 0; row < ROW_SIZE; row++) {
            for (int col = 0; col < COL_SIZE; col++) {
                this.boardMatrix[row][col] = new Cell();
                this.tilesMatrix[row][col] = new Tile();
            }
        }
        this.wordsOnBoard = new ArrayList<>();
        createBonus();
    }

    // Fills ths game board with the correct bonuses for each cell.
    private void createBonus() {
        // Star:
        boardMatrix[7][7].setBonus("Star");
        // Double Letters:
        boardMatrix[3][0].setBonus("DL");
        boardMatrix[0][3].setBonus("DL");
        boardMatrix[11][0].setBonus("DL");
        boardMatrix[0][11].setBonus("DL");
        boardMatrix[6][2].setBonus("DL");
        boardMatrix[2][6].setBonus("DL");
        boardMatrix[7][3].setBonus("DL");
        boardMatrix[3][7].setBonus("DL");
        boardMatrix[8][2].setBonus("DL");
        boardMatrix[2][8].setBonus("DL");
        boardMatrix[8][6].setBonus("DL");
        boardMatrix[6][8].setBonus("DL");
        boardMatrix[6][6].setBonus("DL");
        boardMatrix[8][8].setBonus("DL");
        boardMatrix[14][3].setBonus("DL");
        boardMatrix[3][14].setBonus("DL");
        boardMatrix[12][6].setBonus("DL");
        boardMatrix[6][12].setBonus("DL");
        boardMatrix[11][7].setBonus("DL");
        boardMatrix[7][11].setBonus("DL");
        boardMatrix[12][8].setBonus("DL");
        boardMatrix[8][12].setBonus("DL");
        boardMatrix[14][11].setBonus("DL");
        boardMatrix[11][14].setBonus("DL");
        // Triple Letter:
        boardMatrix[5][1].setBonus("TL");
        boardMatrix[1][5].setBonus("TL");
        boardMatrix[5][5].setBonus("TL");
        boardMatrix[9][1].setBonus("TL");
        boardMatrix[1][9].setBonus("TL");
        boardMatrix[9][5].setBonus("TL");
        boardMatrix[5][9].setBonus("TL");
        boardMatrix[9][9].setBonus("TL");
        boardMatrix[13][5].setBonus("TL");
        boardMatrix[5][13].setBonus("TL");
        boardMatrix[13][9].setBonus("TL");
        boardMatrix[9][13].setBonus("TL");
        // Double Word:
        boardMatrix[1][1].setBonus("DW");
        boardMatrix[2][2].setBonus("DW");
        boardMatrix[3][3].setBonus("DW");
        boardMatrix[4][4].setBonus("DW");
        boardMatrix[4][10].setBonus("DW");
        boardMatrix[10][4].setBonus("DW");
        boardMatrix[3][11].setBonus("DW");
        boardMatrix[11][3].setBonus("DW");
        boardMatrix[2][12].setBonus("DW");
        boardMatrix[12][2].setBonus("DW");
        boardMatrix[1][13].setBonus("DW");
        boardMatrix[13][1].setBonus("DW");
        boardMatrix[10][10].setBonus("DW");
        boardMatrix[11][11].setBonus("DW");
        boardMatrix[12][12].setBonus("DW");
        boardMatrix[13][13].setBonus("DW");
        // Triple Word:
        boardMatrix[0][0].setBonus("TW");
        boardMatrix[7][0].setBonus("TW");
        boardMatrix[0][7].setBonus("TW");
        boardMatrix[14][0].setBonus("TW");
        boardMatrix[0][14].setBonus("TW");
        boardMatrix[7][14].setBonus("TW");
        boardMatrix[14][7].setBonus("TW");
        boardMatrix[14][14].setBonus("TW");
    }

    public Tile[][] getTiles() {
        // No tile = null
        // Returns copied value of table - contains only pointers.
        return Arrays.copyOf(tilesMatrix, tilesMatrix.length);
    }

    public boolean dictionaryLegal(Word word) {
        return true;
    }

    private boolean isWordInRange(Word word) {
        return (word.getRow() >= 0 && word.getRow() < 15 && word.getCol() >= 0 && word.getCol() < 15);
    }

    public boolean boardLegal(Word word) { //board is legal if: 1.the word fits in the board, 2.there is at least one existing tile already on board, 3.

        if (!isWordInRange(word)) return false;
        Tile[] wordTiles = word.getTiles();
        boolean flag = false;
        final int row = word.getRow();
        final int col = word.getCol();
        int i = 0;
        int j = 0;
        if (this.boardMatrix[7][7].getTile() == null) { // Checks if first turn has been done.
            if (word.isVertical() && col == 7 && row >= 0 && row <= 7 ||
                    !word.isVertical() && row == 7 && col >= 0 && col <= 7) {
                flag = (row + wordTiles.length >= 7 && row + wordTiles.length <= 14) ||
                        (col + wordTiles.length >= 7 && col + wordTiles.length <= 14);
            }
        } else {
            // Determines if the index should be for rows or cols.
            if (word.isVertical()) i++; else j++;
            boolean isOutOfRange = (word.isVertical() ? (row + wordTiles.length) : (col + wordTiles.length)) > 14; // True if the word given is out of the board range
            for (Tile tile : wordTiles) {
                if (isOutOfRange || (tile == null) != (this.boardMatrix[row + i][col + j].getTile() == null)) {     // If both (isOutOfRange) AND (tile is null) false AND the cell on board is empty(is null) we get (false!=true) -> if statement gets 'true'
                    flag = !isOutOfRange && (tile != null);                                                         // flag gets true if both (isOutOfRange) AND (tile is null) are false. (is NOT out of range and tile is NOT null)
                }
                if (word.isVertical()) i++; else j++;
            }
        }
        return flag;
    } //DONE

    private Word getWord(ArrayList<Tile> tileArrayList, int row, int col, boolean isVer) {
        Tile[] tempTiles = new Tile[tileArrayList.size()];
        int i = 0;
        for (Tile tile : tileArrayList) {
            tempTiles[i] = tile;
            i++;
        }
        return new Word(tempTiles, row, col, isVer);
    }

    private Word getNewWord(int row, int col, boolean isVertical) {// Vertical = true, Horizontal = false
        while ((isVertical ? row : col) > 0 &&
                this.boardMatrix[isVertical ? row - 1 : row][isVertical ? col : col - 1].getTile() != null) {
            if (isVertical) row--; else col--;
        }
        int wordFinalIndex = (isVertical ? row : col);
        while (wordFinalIndex < 15 &&
                this.boardMatrix[isVertical ? wordFinalIndex + 1 : row][isVertical ? col : wordFinalIndex + 1].getTile() != null) {
            wordFinalIndex++;
        }
        Tile[] tempTiles = new Tile[wordFinalIndex - (isVertical ? row : col) + 1];
        for (int i = (isVertical ? row : col), j = 0; j < tempTiles.length; i++, j++) {
            tempTiles[j] = this.boardMatrix[isVertical ? i : row][isVertical ? col : i].getTile();
        }
        return new Word(tempTiles, row, col, isVertical);
    }

    public ArrayList<Word> getWords(Word word) {
        /*
         1. create list.
         2. insert received word to list.
         3. find the existing tiles.
        */
        int row = word.getRow();
        int col = word.getCol();
        boolean isVertical = word.isVertical();
        int wordFinalIndex = (word.isVertical() ? row : col) + word.getTiles().length;
        ArrayList<Word> newWords = new ArrayList<>();
        ArrayList<Tile> tempTiles = new ArrayList<>();
        for (int k = (isVertical ? row : col); k < wordFinalIndex; k++) {
            tempTiles.add(this.boardMatrix[isVertical ? k : row][isVertical ? col : k].getTile());
        }
        newWords.add(getWord(tempTiles, row, col, isVertical));
        for (int i = (isVertical ? row : col); i < (isVertical ? row : col) + word.getTiles().length; i++) {
            if (this.boardMatrix[isVertical ? i : row - 1][isVertical ? col - 1 : i].getTile() == null &&
                    this.boardMatrix[isVertical ? i : row + 1][isVertical ? col + 1 : i].getTile() == null) {// Checks if there are adjacent cells, if not continue.
                continue;
            }
            if (isVertical) { // Horizontal check
                Word horWord = getNewWord(i, col, false);
                newWords.add(horWord);
            } else { // Vertical check
                Word verWord = getNewWord(row, i, true);
                newWords.add(verWord);
            }
        }
        return newWords;
    }

    private void setAllBonusesToUsed(ArrayList<Word> newWordsList){
        for (Word w:newWordsList){
            int wordFinalIndex = (w.isVertical() ? w.getRow() : w.getCol()) + w.getTiles().length;
            for (int i = (w.isVertical() ? w.getRow() : w.getCol()), index = 0; i < wordFinalIndex && index < w.getTiles().length; i++, index++) {
                this.boardMatrix[w.isVertical() ? i : w.getRow()][w.isVertical() ? w.getCol() : i].setBonusToUsed();
            }
        }
    }

    public int getScore(Word word) {

        boolean isVertical = word.isVertical();
        int row = word.getRow();
        int col = word.getCol();
        int wordFinalIndex = (isVertical ? row : col) + word.getTiles().length;
        int letterScore;
        int score = 0;
        int wordMultiplier = 1;

        for (int i = (isVertical ? row : col); i < wordFinalIndex; i++) {
            letterScore = this.boardMatrix[isVertical ? i : row][isVertical ? col : i].getTile().score;
            String bonus = this.boardMatrix[isVertical ? i : row][isVertical ? col : i].getBonus();
            if (Objects.equals(bonus, "None") || this.boardMatrix[isVertical ? i : row][isVertical ? col : i].isBonusUsed()) {
                score += letterScore;
                continue;
            }
            switch (bonus) {
                case "Star", "DW" -> {
                    score += letterScore;
                    wordMultiplier *= 2;
                }
                case "DL" -> score += letterScore * 2;
                case "TL" -> score += letterScore * 3;
                case "TW" -> {
                    score += letterScore;
                    wordMultiplier *= 3;
                }
            }
        }
        return score * wordMultiplier;
    } //DONE

    public int tryPlaceWord(Word word) {
        /*
           1. Is the word boardLegal?
           2. getWords for all the new words.
           3. Are the words dictionaryLegal?
              if All True -> place word and return the score achieved, else return 0 score.
         */
        int row = word.getRow();
        int col = word.getCol();
        boolean isVertical = word.isVertical();
        int totalScore = 0;
        if (boardLegal(word) && dictionaryLegal(word)) {
            int wordFinalIndex = (isVertical ? row : col) + word.getTiles().length;
            for (int i = (isVertical ? row : col), index = 0; i < wordFinalIndex && index < word.getTiles().length; i++, index++) {
                if (this.boardMatrix[isVertical ? i : row][isVertical ? col : i].getTile() == null) {
                    this.boardMatrix[isVertical ? i : row][isVertical ? col : i].setTile(word.getTiles()[index]);
                    this.tilesMatrix[isVertical ? i : row][isVertical ? col : i] = word.getTiles()[index];
                }
            }
            ArrayList<Word> newWordsList = getWords(word);
            for (Word w : newWordsList) {
                if (dictionaryLegal(w) && !wordsOnBoard.contains(w)) {
                    totalScore += getScore(w);
                    wordsOnBoard.add(w);
                }
            }
            setAllBonusesToUsed(newWordsList);
            System.out.println("Word Score:" + totalScore);
            return totalScore;
        }
        return -1;
    }
}