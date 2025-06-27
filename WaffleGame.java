import java.lang.StringBuilder;

/** 
 * The objects of this class represent matches of the Waffle Game.
 *
 * @author Bruno Faustino n59784
 * @author Vladislav Zavgorodnii n59783
 */
public class WaffleGame {

    //The words that form the puzzle matrix
    private Puzzle puzzle;

    //The matrixes of characters that correspond to the puzzle and the grid, respectively
    private char[][] puzzleGrid;
    private char[][] grid;

    //The copy of the grid
    private char[][] gridCopy;

    /*Indicates if the letter is in the correct position, or if it is not in the correct
      position but belongs to a horizontal word or vertical word, or if it does not exist
      in the words*/
    private LetterStatus status;

    //The remaining number of swaps left to complete the game
    private int remainingSwaps;

    /** 
     * Verifies if a given grid is valid, according to the given puzzle, by verifying the following
     * conditions:
     * 1- the puzzle, the grid, and each array of the grid are not null;
     * 2- grid is a square matrix;
     * 3- the number of rows is equal to the size of the words of the puzzle;
     * 4- the rows and columns of the grid with an odd index (at the same time) have a *;
     * 5- every other position is occupied by an uppercase letter;
     * 6- every letter in the puzzle appears the same number of times in the grid.
     *
     * @param puzzle the puzzle
     * @param grid   the grid
     * @return true if the grid is valid, according to the puzzle, or false if not
     */
    public static boolean validGrid(Puzzle puzzle, char[][] grid) {
        boolean isValid = true;
        char[][] puzzleGrid = puzzle.buildGrid();
        char letter = ' ';
        int i = 0;
        int j = 0;

        if (puzzle == null || grid == null) {
            isValid = false;
        }
        else if (grid.length != puzzleGrid.length) {
            isValid = false;
        }
        else {
            while (isValid && i < grid.length) {
                if (grid[i] == null) {
                    isValid = false;
                }
                else {
                    while (isValid && j < grid[i].length) {
                        if (grid[0].length != grid[i].length || grid.length != grid[i].length) {
                            isValid = false;
                        }
                        else if (grid[i][j] != '*' && i % 2 == 1 && j % 2 == 1) {
                            isValid = false;
                        }
                        else if (!(i % 2 == 1 && j % 2 == 1) && 
                            !Character.isUpperCase(grid[i][j])) {

                            isValid = false;
                        }
                        else {
                            letter = puzzleGrid[i][j];
                            if (!sameNumberOfLetters(puzzleGrid, grid, letter)) {
                                isValid = false;
                            }
                        }
                        j++;
                    }
                    j = 0;
                }
                i++;
            }
        }
        return isValid;
    }

    /** 
     * Verifies if a given letter appears the same number of times in a given puzzle
     * and in a given grid.
     *
     * @param puzzle the puzzle matrix of characters
     * @param grid   the grid matrix of characters
     * @param letter the letter
     * @return true if the letter appears the same number of times in the puzzle and in the grid
     *         or false if not
     * @requires {@code puzzle != null && grid != null}
     */
    private static boolean sameNumberOfLetters(char[][] puzzle, char[][] grid, char letter) {
        boolean sameNumberOfLetters = true;
        int counterPuzzle = 0;
        int counterGrid = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (letter == puzzle[i][j]) {
                    counterPuzzle++;
                }
                if (letter == grid[i][j]) {
                    counterGrid++;
                }
            }
        }
        if (counterPuzzle != counterGrid) {
            sameNumberOfLetters = false;
        }
        return sameNumberOfLetters;
    }

    /** 
     * Builds a match of the Waffle Game with the given puzzle and grid matrix.
     *
     * @param puzzle the puzzle that contains the horizontal and vertical words
     * @param grid   the matrix of characters
     * @requires {@code validGrid(puzzle, grid)}
     */
    public WaffleGame(Puzzle puzzle, char[][] grid) {
        this.puzzle = puzzle;
        this.puzzleGrid = puzzle.buildGrid();
        this.grid = grid;
        this.remainingSwaps = maxSwaps();
        
        //gridCopy is used if the player wants to restart the same grid
        this.gridCopy = new char[grid.length][grid.length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                this.gridCopy[i][j] = grid[i][j];
            }
        }
    }

    /** 
     * Verifies if a given position is valid by confirming if the row and column are values
     * between 1 and the size of the puzzle words (included) and if they are not both even.
     *
     * @param row the row
     * @param col the column
     * @return true if the position is valid or false if not
     */
    public boolean validPosition(int row, int col) {
        return (1 <= row && row <= this.puzzleGrid.length && 
                1 <= col && col <= this.puzzleGrid.length &&
                !(row % 2 == 0 && col % 2 == 0));
    }

    /** 
     * @return the status of the letter in the given position of the grid. 
     *         In other words: if the letter is in the correct position, according to the puzzle,
     *         or if it is not in the correct position but belongs to a horizontal word or a
     *         vertical word (multiple occurences of the same letter in wrong positions are
     *         validated from left to right (or top to bottom)), or if the letter does not belong
     *         to the words it intersets
     * @requires {@code validPosition(row, col)}
     */
    public LetterStatus clue(int row, int col) {

        if (matchingLetters()[row - 1][col - 1] && this.grid[row - 1][col - 1] != '*') {
            status = LetterStatus.CORRECT_POS;
        }
        else if (((row % 2 == 1 && letterInRowWrongPos(row, col)) ||
                  (col % 2 == 1 && letterInColWrongPos(row, col))) && 
                   this.grid[row - 1][col - 1] != '*') {
            
            status = LetterStatus.WRONG_POS;
        }
        else if (this.grid[row - 1][col - 1] != '*') {
            status = LetterStatus.INEXISTENT;
        }
        return status;
    }

    /**
     * Returns a matrix with the dimensions of grid that indicates for each letter in grid if
     * it is in the right position, according to the puzzle.
     *
     * @return a matrix with true if the letter in grid is in the right position and/or false
     *         if the letter is in the wrong position, according to the puzzle
     */
    private boolean[][] matchingLetters() {
        boolean [][] matchingLetters = new boolean[this.grid.length][this.grid.length];
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if (this.puzzleGrid[i][j] == this.grid[i][j]) {
                    matchingLetters[i][j] = true;
                }
                else {
                    matchingLetters[i][j] = false;
                }
            }
        }
        return matchingLetters;
    }

    /**
     * Verifies if a letter of a grid is valid in its column but is in the wrong position,
     * according to the puzzle.
     *
     * @param row    the row of the letter
     * @param col    the column of the letter
     * @return true if the letter is in the right column, according to the puzzle, but
     *         is in the wrong position (if there are more than one occurrence of the letter
     *         in the column of the grid, the letters will be validated from top to bottom)
     *         or false if not
     * @requires {@code validPosition(row, col) && col % 2 != 0}
     */
    private boolean letterInColWrongPos(int row, int col) {
        boolean letterInColWrongPos = false;
        char letter = this.grid[row - 1][col - 1];

        String puzzleWord = " ";
        String gridWord = " ";
        String incorrectLetterPos = " ";

        StringBuilder sbPuzzle = new StringBuilder();
        StringBuilder sbGrid = new StringBuilder();
        StringBuilder sbIncorrectPos = new StringBuilder();

        int rowCounter = 0;
        
        int letterAppearencesInPuzzle = 0;
            /*the number of times the specified letter (or an equal one) appears in
              the specified column of the puzzle*/
        int letterAppearencesInGrid = 0;
            /*the number of times the specified letter (or an equal one) appears in
              the specified column of the grid*/
        int correctLetterAppearences = 0;
            /*the number of times the specified letter (or an equal one) is correct,
              in the specified column of the grid*/
        int remainingLetterAppearences = 0;

        /*inicializing the strings that correspond to the specified column 
          in the puzzle and in the grid*/
        for (int i = 0; i < this.puzzleGrid.length; i++) {
            sbPuzzle = sbPuzzle.append(this.puzzleGrid[i][col - 1]);
            sbGrid = sbGrid.append(this.grid[i][col - 1]);
        }
        puzzleWord = sbPuzzle.toString();
        gridWord = sbGrid.toString();

        /*Will only enter here if the specified letter is not already in the correct position*/
        if (!matchingLetters()[row - 1][col - 1]) {
            while (!letterInColWrongPos && rowCounter < this.puzzleGrid.length) {
                letterInColWrongPos = letterInColWrongPos || 
                    letter == puzzleWord.charAt(rowCounter);
                    
                rowCounter++;
            }
            if (letterInColWrongPos) {

                for (int i = 0; i < this.grid.length; i++) {

                    if (puzzleWord.charAt(i) == letter) {
                        letterAppearencesInPuzzle++;
                    }
                    if (gridWord.charAt(i) == letter) {
                        letterAppearencesInGrid++;
                        if (!matchingLetters()[i][col - 1]) {
                            sbIncorrectPos = sbIncorrectPos.append(i);
                        }
                    }
                    if (gridWord.charAt(i) == letter &&
                        matchingLetters()[i][col - 1]) {
                        
                        correctLetterAppearences++;
                    }

                }
                incorrectLetterPos = sbIncorrectPos.toString();

                remainingLetterAppearences = letterAppearencesInPuzzle - 
                        correctLetterAppearences;
                
                /*If the number of correct letters equal to the specified letter in the
                 col of the grid is already the same as the number of letters equal to the
                 specified letter in the col of the puzzle, then if there are still more letters
                 in the grid, those will change the boolean value to false.*/
                if (remainingLetterAppearences == 0 && incorrectLetterPos.length() > 0) {
                    letterInColWrongPos = false;
                }
                /*If there are more letters equal to the specified letter in the col of the grid
                 than there are in the col of the puzzle then some letters of the col of the
                 grid are true and others are false. This will be evaluated from top to bottom.
                 From the top until we reach the required ammount of missing letters
                 (remainingLetterAppearences): if the specified letter is in that interval,
                 the boolean value remains true, if not then it is false.*/
                else if (letterAppearencesInGrid > letterAppearencesInPuzzle) {
                    for (int i = 0; i < incorrectLetterPos.length(); i++) {
                        if (i + 1 > remainingLetterAppearences &&
                            Integer.parseInt(String.valueOf(incorrectLetterPos.charAt(i)))
                                 == row - 1) {
                                    
                            letterInColWrongPos = false;
                        }
                    }
                }
            }
        }
        return letterInColWrongPos;    
    }

    /**
     * Verifies if a letter of a grid is valid in its row but is in the wrong position,
     * according to the puzzle.
     *
     * @param row    the row of the letter
     * @param col    the column of the letter
     * @return true if the letter is in the right row, according to the puzzle, but
     *         is in the wrong position (if there are more than one occurrence of the letter
     *         in the row of the grid, the letters will be validated from left to right)
     *         or false if not
     * @requires {@code validPosition(row, col) && row % 2 != 0}
     */
    public boolean letterInRowWrongPos(int row, int col) {
        boolean letterInRowWrongPos = false;
        char letter = grid[row - 1][col - 1];

        String puzzleWord = " ";
        String gridWord = " ";
        String incorrectLetterPos = " ";

        StringBuilder sbIncorrectPos = new StringBuilder();

        /*inicializing the strings that correspond to the specified row 
          in the puzzle and in the grid*/
        puzzleWord = String.copyValueOf(this.puzzleGrid[row - 1]);
        gridWord = String.copyValueOf(this.grid[row - 1]);

        int colCounter = 0;

        int correctLetterAppearences = 0;
            /*the number of times the specified letter (or an equal one) appears in
              the specified row of the puzzle*/
        int letterAppearencesInPuzzle = 0;
            /*the number of times the specified letter (or an equal one) appears in
              the specified row of the grid*/
        int letterAppearencesInGrid = 0;
            /*the number of times the specified letter (or an equal one) is correct,
              in the specified row of the grid*/
        int remainingLetterAppearences = 0;

        /*Will only enter here if the specified letter is not already in the correct position*/
        if (!matchingLetters()[row - 1][col - 1]) {
            while (!letterInRowWrongPos && colCounter < this.puzzleGrid.length) {
                letterInRowWrongPos = letterInRowWrongPos || 
                    letter == puzzleWord.charAt(colCounter);
                    
                colCounter++;
            }
            if (letterInRowWrongPos) {

                for (int j = 0; j < this.grid.length; j++) {

                    if (puzzleWord.charAt(j) == letter) {
                        letterAppearencesInPuzzle++;
                    }
                    if (gridWord.charAt(j) == letter) {
                        letterAppearencesInGrid++;
                        if (!matchingLetters()[row - 1][j]) {
                            sbIncorrectPos = sbIncorrectPos.append(j);
                        }
                    }
                    if (gridWord.charAt(j) == letter &&
                        matchingLetters()[row - 1][j]) {
                        
                        correctLetterAppearences++;
                    }

                }
                incorrectLetterPos = sbIncorrectPos.toString();

                remainingLetterAppearences = letterAppearencesInPuzzle - 
                        correctLetterAppearences;
                
                /*If the number of correct letters equal to the specified letter in the
                 row of the grid is already the same as the number of letters equal to the
                 specified letter in the row of the puzzle, then if there are still more letters
                 in the grid, those will change the boolean value to false.*/
                if (remainingLetterAppearences == 0 && incorrectLetterPos.length() > 0) {
                    letterInRowWrongPos = false;
                }
                /*If there are more letters equal to the specified letter in the row of the grid
                 than there are in the row of the puzzle then some letters of the row of the
                 grid are true and others are false. This will be evaluated from left to right.
                 From the left until we reach the required ammount of missing letters
                 (remainingLetterAppearences): if the specified letter is in that interval,
                 the boolean value remains true, if not then it is false.*/
                else if (letterAppearencesInGrid > letterAppearencesInPuzzle) {
                    for (int j = 0; j < incorrectLetterPos.length(); j++) {
                        if (j + 1 > remainingLetterAppearences &&
                            Integer.parseInt(String.valueOf(incorrectLetterPos.charAt(j)))
                                 == col - 1) {
                                    
                            letterInRowWrongPos = false;
                        }
                    }
                }
            }
        }
        return letterInRowWrongPos;
    }

    /**
     * Verifies if the specified letter in the grid can be swappable by verifing if its
     * row and column:
     * 1- are values between 1 and the length of the grid;
     * 2- are not both even;
     * 3- do not correspond to a letter already in the right position, according to the puzzle.
     *
     * @param row    the row of the letter
     * @param col    the column of the letter
     * @return true if the letter follows all conditions to be swapped and false if not
     */
    public boolean swappablePosition(int row, int col) {
        boolean swappablePosition = true;

        if (!validPosition(row, col)) {
            swappablePosition = false;
        }
        else if (matchingLetters()[row - 1][col - 1]) {
            swappablePosition = false;
        }
        return swappablePosition;
    }

    /**
     * Swaps 2 letters of a grid and, if those letters are different, updates the remaining
     * number of swaps.
     * 
     * @param row1 the row of the first letter to be swapped
     * @param col1 the column of the first letter to be swapped
     * @param row2 the row of the second letter to be swapped
     * @param col2 the column of the second letter to be swapped
     * @requires {@code swappablePosition(row1, col1) &&
     *            swappablePosition(row2, col2) &&
     *            (row1 != row2 || col1 != col2)}
     */
    public void swap(int row1, int col1, int row2, int col2) {
        char toSwap = this.grid[row1 - 1][col1 - 1];
        this.grid[row1 - 1][col1 - 1] = this.grid[row2 - 1][col2 - 1];
        this.grid[row2 - 1][col2 - 1] = toSwap;

        if (this.grid[row1 - 1][col1 - 1] != this.grid[row2 - 1][col2 - 1]) {
            this.remainingSwaps--;
        }
    }

    /** 
     * @return the maximum ammount of swaps that can be done in the grid which is the number
     *         of swaps that takes to create the grid + 5
     */
    public int maxSwaps() {
        return this.puzzle.shuffleSwaps() + 5;
    }

    /** 
     * @return the remaining number of swaps available for the player to complete the game
     */
    public int remainingSwaps() {
        return this.remainingSwaps;
    }

    /** 
     * @return the current grid
     */
    public char[][] getCurrentGrid() {
        return this.grid;
    }

    /**
     * Verifies if the elements in the grid are in the same position as the elements in the puzzle
     * 
     * @return true if all elements in the grid are in the same position as the elements in
     *         the puzzle or false if not
     */
    public boolean puzzleFound() {
        boolean puzzleFound = true;
        int i = 0;
        int j = 0;
        while (puzzleFound && i < this.grid.length) {
            while (puzzleFound && j < this.grid[i].length) {
                if (this.puzzleGrid[i][j] != this.grid[i][j]) {
                    puzzleFound = false;
                }
                j++;
            }
            j = 0;
            i++;
        } 
        return puzzleFound;
    }

    /** 
     * Verifies if the match is over.
     *
     * @return true if either all the letters in the puzzle are in the same position as the letters
     *         in the grid or the player reached the maximum ammount of swaps allowed,
     *         or false if not
     */
    public boolean isOver() {
        return (puzzleFound() || this.remainingSwaps == 0);
    }

    /** 
     * Restarts the match by setting the grid back to its inicial state and resetting
     * the number of swaps allowed.
     */
    public void restart() {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                this.grid[i][j] = this.gridCopy[i][j];
            }
        }
        this.remainingSwaps = maxSwaps();
    }

    /** 
     * Prints a textual representation of the current grid and the remaining number
     * of swaps allowed.
     */
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("      ");
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if (clue(i + 1, j + 1) == LetterStatus.CORRECT_POS) {
                    sb.append(
                        StringColouring.toGreenString(String.valueOf(this.grid[i][j])) + " ");
                }
                else if (clue(i + 1, j + 1) == LetterStatus.WRONG_POS) {
                    sb.append(
                        StringColouring.toYellowString(String.valueOf(this.grid[i][j])) + " ");
                }
                else if (clue(i + 1, j + 1) == LetterStatus.INEXISTENT) {
                    sb.append(this.grid[i][j] + " ");
                }

                if (j == this.grid[i].length - 1 && i != this.grid.length - 1) {
                    sb.append("\n      ");
                }
            }
        }
        sb.append("\n> " + maxSwaps() + " swaps remaining <");
        return sb.toString().replace("*"," ");
    }
}