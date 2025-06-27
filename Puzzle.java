import java.lang.Character;
import java.util.Random;
import java.lang.Math;

/** 
 * The objects of this class are immutable and represent valid puzzles for the Waffle Game.
 *
 * @author Bruno Faustino n59784
 * @author Vladislav Zavgorodnii n59783 
 */
public class Puzzle {

    //The arrays of horizontal words and vertical words, respectively, that will form the puzzle
    private String[] hWords;
    private String[] vWords;

    //The number of characters of the words
    private int size;

    /** 
     * Verifies if the words for the puzzle are valid by verifying if they respect 
     * the following conditions:
     * 1- the size is an odd number between 4 and 8 (not included);
     * 2- both arrays have the necessary length to be combined;
     * 3- the words are not null, have the same length as size and only have uppercase letters.
     *
     * @param size   the size that the words for the puzzle should have
     * @param hWords the array of horizontal words that will form the main rows of the puzzle
     * @param vWords the array of vertical words that will form the main columns of the puzzle
     * @return true if the words are valid or false if not
     * @requires {@code hWords != null && vWords != null}
     */
    public static boolean validWords(int size, String[] hWords, String[] vWords) {
        boolean validWords = true;
        int i = 0;

        if (size % 2 != 1 || size <= 4 || size >= 8) {
            validWords = false;
        }
        else if (hWords.length != (size + 1) / 2 || vWords.length != (size + 1) / 2) {
            validWords = false;
        }
        else{
            for (String s : hWords) {
                validWords = validWords && s != null && s.length() == size;
                while (validWords && i < s.length()) {
                    validWords = validWords && Character.isUpperCase(s.charAt(i));
                    i++;
                }
                i = 0;
            }
            for (String s : vWords) {
                validWords = validWords && s != null && s.length() == size;
                while (validWords && i < s.length()) {
                    validWords = validWords && Character.isUpperCase(s.charAt(i));
                    i++;
                }
                i = 0;
            }
        }
        return validWords;
    }

    /** 
     * Verifies if the words intersect by verifying if the letters in specific positions are the
     * same in both the horizontal word and the vertical word.
     *
     * @param size   the number of characters of each word
     * @param hWords the array of horizontal words
     * @param vWords the array of vertical words
     * @return true if the specified letters are in both the horizontal word and the vertical word
     *         or false if not
     * @requires {@code validWords(size, hWords, vWords)}
     */
    public static boolean overlappedWords(int size, String[] hWords, String[] vWords) {
        boolean overlappedWords = true;

        for (int i = 0; i <= size / 2; i++) {
            for (int j = 0; j <= size / 2; j++) {
                overlappedWords = overlappedWords && 
                                  hWords[j].charAt(2 * i) == vWords[i].charAt(2 * j);
            }
        }
        return overlappedWords;
    }

    /** 
     * Builds the puzzle with the given array of horizontal words and array of vertical words.
     *
     * @param size   the number of characters of each word
     * @param hWords the array of horizontal words
     * @param vWords the array of vertical words
     * @requires {@code validWords(size, hWords, vWords) && overlappedWords(size, hWords, vWords)}
     */
    public Puzzle(int size, String[] hWords, String[] vWords) {
        this.size = size;
        this.hWords = new String[hWords.length];
        this.vWords = new String[vWords.length];

        for (int i = 0; i < hWords.length; i++) {
            this.hWords[i] = hWords[i];
            this.vWords[i] = vWords[i];
        }
    }

    /** 
     * @return the number of characters of each word
     */
    public int size() {
        return this.size;
    }

    /** 
     * @return the total number of words
     */
    public int nrWords() {
        return this.hWords.length + this.vWords.length;
    }

    /** 
     * @return the number of swaps that needs to be made in the puzzle to create the inicial grid
     */
    public int shuffleSwaps() {
        return 5 * (this.size - 3);
    }

    /** 
     * @param i the position of the letter in the word
     * @param j the position of the word in the array of horizontal words
     * @return the character of the horizontal word j in the position i
     * @requires {@code 0 < i && i <= size() && 0 < j && j <= nrWords() / 2}
     */
    public char getLetterInHorizontalWord(int i, int j) {
        return this.hWords[j - 1].charAt(i - 1);
    }

    /** 
     * @param i the position of the letter in the word
     * @param j the position of the word in the array of vertical words
     * @return the character of the vertical word j in the position i
     * @requires {@code 0 < i && i <= size() && 0 < j && j <= nrWords() / 2}
     */
    public char getLetterInVerticalWord(int i, int j) {
        return this.vWords[j - 1].charAt(i - 1);
    }

    /** 
     * Iniciallizes the puzzle matrix then swaps one letter in each even row and column with
     * an adjacent and swaps a number of letters in each row with their correspondents
     * in the respective columns to create a shuffled grid.
     *
     * @return the shuffled puzzle matrix
     * @ensures {@code maximum ammount of swaps made: shuffleSwaps(),
     *           \result only has letters in odd indexes of rows and/or columns,
     *           the letters in the diagonals of the matrix do not get shuffled,
     *           \result has * in even indexes of rows and columns (at the same time)}
     */
    public char[][] getShuffledGrid() {
        char[][] grid = buildGrid();
        Random r = new Random();

        grid = shuffleWithAdjacent(grid, r);
        grid = switchRowColOfLetters(grid, r);
        return grid;
    }

    /** 
     * Creates the puzzle matrix.
     *
     * @return the puzzle matrix
     * @requires {@code validWords(this.size, this.hWords, this.vWords) &&
     *            overlappedWords(this.size, this.hWords, this.vWords)}
     * @ensures {@code \result only has letters in odd indexes of rows and/or columns,
     *           \result has * in even indexes of rows and columns (at the same time)}
     */
    public char[][] buildGrid() {
        char[][] grid = new char[size()][size()];

        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (i % 2 == 0 && j % 2 != 0) {
                    grid[i][j] = getLetterInHorizontalWord(j + 1, i - (i / 2) + 1);
                }
                else if (j % 2 == 0) {
                    grid[i][j] = getLetterInVerticalWord(i + 1, j - (j / 2) + 1);
                }
                else if (i % 2 != 0 && j % 2 != 0) {
                    grid[i][j] = '*';
                }
            }
        }
        return grid;
    }

    /** 
     * Swaps one letter in each even row and column of a grid with an adjacent.
     *
     * @param grid the grid
     * @param r    random used to choose the letter in each row and column with an even index
     *             that will be swapped and with which letter it will be swapped
     * @return the shuffled grid
     * @requires {@code grid != null}
     * @ensures {@code the letters in the diagonals of the matrix and the * do not get shuffled}
     */
    private char[][] shuffleWithAdjacent(char[][] grid, Random r) {
        char toSwap = ' ';
        int randomPos = 0;
        int swapsTo = 0;
        int chooseDirection = 0;

        int[] swapLeftOrRight = new int[2];
        int[] swapUpOrDown = new int[2];

        for (int i = 0; i < nrWords(); i += 2) {
            do {
                randomPos = r.nextInt(size());
            } while (isInDiagonal(i, randomPos));

            toSwap = grid[i][randomPos];

            if (randomPos == 0) {
                swapLeftOrRight[0] = 1;
                swapLeftOrRight[1] = size() - 1;
                chooseDirection = r.nextInt(swapLeftOrRight.length);
                swapsTo = swapLeftOrRight[chooseDirection];

                if (isInDiagonal(i, randomPos + swapsTo)) {
                    chooseDirection = Math.abs(chooseDirection - 1); //the other direction
                    swapsTo = swapLeftOrRight[chooseDirection];
                }
                if (!isInDiagonal(i, randomPos + swapsTo)) {
                    grid[i][randomPos] = grid[i][randomPos + swapsTo];
                    grid[i][randomPos + swapsTo] = toSwap;
                }
            }
            else if (randomPos == size() - 1) {
                swapLeftOrRight[0] = -(size() - 1);
                swapLeftOrRight[1] = -1;
                chooseDirection = r.nextInt(swapLeftOrRight.length);
                swapsTo = swapLeftOrRight[chooseDirection];
                
                if (isInDiagonal(i, randomPos + swapsTo)) {
                    chooseDirection = Math.abs(chooseDirection - 1); //the other direction
                    swapsTo = swapLeftOrRight[chooseDirection];
                }
                if (!isInDiagonal(i, randomPos + swapsTo)) {
                    grid[i][randomPos] = grid[i][randomPos + swapsTo];
                    grid[i][randomPos + swapsTo] = toSwap;
                }
            }
            else {
                swapLeftOrRight[0] = -1;
                swapLeftOrRight[1] = 1;
                chooseDirection = r.nextInt(swapLeftOrRight.length);
                swapsTo = swapLeftOrRight[chooseDirection];
                
                if (isInDiagonal(i, randomPos + swapsTo)) {
                    chooseDirection = Math.abs(chooseDirection - 1); //the other direction
                    swapsTo = swapLeftOrRight[chooseDirection];
                }
                if (!isInDiagonal(i, randomPos + swapsTo)) {
                    grid[i][randomPos] = grid[i][randomPos + swapsTo];
                    grid[i][randomPos + swapsTo] = toSwap;
                }
            }
        }

        for (int j = 0; j < nrWords(); j += 2) {
            do {
                randomPos = r.nextInt(size());
            } while (j == randomPos || j == size() - 1 - randomPos);

            toSwap = grid[randomPos][j];

            if (randomPos == 0) {
                swapUpOrDown[0] = 1;
                swapUpOrDown[1] = size() - 1;
                chooseDirection = r.nextInt(swapUpOrDown.length);
                swapsTo = swapUpOrDown[chooseDirection];
                
                if (isInDiagonal(j, randomPos + swapsTo)) {
                    chooseDirection = Math.abs(chooseDirection - 1); //the other direction
                    swapsTo = swapLeftOrRight[chooseDirection];
                }
                if (!isInDiagonal(j, randomPos + swapsTo)) {
                    grid[randomPos][j] = grid[randomPos + swapsTo][j];
                    grid[randomPos + swapsTo][j] = toSwap;
                }
            }
            else if (randomPos == size() - 1) {
                swapUpOrDown[0] = -(size() - 1);
                swapUpOrDown[1] = -1;
                chooseDirection = r.nextInt(swapUpOrDown.length);
                swapsTo = swapUpOrDown[chooseDirection];
                
                if (isInDiagonal(j, randomPos + swapsTo)) {
                    chooseDirection = Math.abs(chooseDirection - 1); //the other direction
                    swapsTo = swapLeftOrRight[chooseDirection];
                }
                if (!isInDiagonal(j, randomPos + swapsTo)) {
                    grid[randomPos][j] = grid[randomPos + swapsTo][j];
                    grid[randomPos + swapsTo][j] = toSwap;
                }
            }
            else {
                swapUpOrDown[0] = -1;
                swapUpOrDown[1] = 1;
                chooseDirection = r.nextInt(swapUpOrDown.length);
                swapsTo = swapUpOrDown[chooseDirection];
                
                if (isInDiagonal(j, randomPos + swapsTo)) {
                    chooseDirection = Math.abs(chooseDirection - 1); //the other direction
                    swapsTo = swapLeftOrRight[chooseDirection];
                }
                if (!isInDiagonal(j, randomPos + swapsTo)) {
                    grid[randomPos][j] = grid[randomPos + swapsTo][j];
                    grid[randomPos + swapsTo][j] = toSwap;
                }
            }
        }
        return grid;
    }

    /** 
     * Swaps a number of letters in each row of a grid with their correspondents in the
     * respective columns.
     *
     * @param grid the grid
     * @param r    random used to choose the letter in each row of the grid with an even index
     *             that will be swapped
     * @return the shuffled grid
     * @requires {@code grid != null}
     * @ensures {@code the letters in the diagonals of the matrix and the * do not get shuffled}
     */
    private char[][] switchRowColOfLetters(char[][] grid, Random r) {
        char toSwap = ' ';
        int randomPos = 0;

        for (int i = 0; i < nrWords(); i += 2) {
            for (int nrLetters = 0; nrLetters < (size() - 3) / 2; nrLetters++) {
                do {
                    randomPos = r.nextInt(size());
                } while (isInDiagonal(i, randomPos));

                toSwap = grid[i][randomPos];
                grid[i][randomPos] = grid[randomPos][i];
                grid[randomPos][i] = toSwap;
            }
        }
        return grid;
    }

    /** 
     * Verifies if the given positions correspond to a character in a diagonal of a grid.
     *
     * @param i the row of the character
     * @param j the column of the character
     * @return true if the character belongs to a diagonal or false if not
     */
    private boolean isInDiagonal(int i, int j) {
        boolean isInDiagonal = false;

        if (i == j || i == size() - 1 - j) {
            isInDiagonal = true;
        }
        return isInDiagonal;
    }
}




