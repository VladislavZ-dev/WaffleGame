/**
 *
 * Esta classe e fornecida para auxilio no projeto da disciplina de 
 * Introducao a Programacao da Faculdade de Ciencias, ULisboa
 * 2022/23
 * @author Andreia Mordido
 * @author Pedro Pais
 *
 */
public class WaffleTest {

    // palavras e puzzle para testes
    private static final String[] hWords = new String[] {"CHORE", "MINUS", "THREE"};
    private static final String[] vWords = new String[] {"COMET", "OWNER", "ENSUE"};
    private static final int size = 5;
    private static final int nrWords = 6;

    // puzzle #313 de wafflegame.net
    private static final char[][] puzzleGrid = new char[][] {
        {'C', 'H', 'O', 'R', 'E'},
        {'O', '*', 'W', '*', 'N'},
        {'M', 'I', 'N', 'U', 'S'},
        {'E', '*', 'E', '*', 'U'},
        {'T', 'H', 'R', 'E', 'E'}
    };

    // quadricula inicial #313 de wafflegame.net
    private static char[][] initialGrid = new char[][] {
        {'C', 'O', 'N', 'I', 'E'},
        {'W', '*', 'E', '*', 'R'},
        {'M', 'U', 'N', 'R', 'U'},
        {'H', '*', 'O', '*', 'E'},
        {'T', 'H', 'E', 'S', 'E'}
    };

    public static void main(String[] args) {
        System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println(">>>>>>>>>>>>>>> Starting tests <<<<<<<<<<<<<<<");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<");
        testLetterStatus();
        testPuzzle();
        testWaffleGame();
        System.out.println("\n>>>>>>>>>>>>>>> Tests finished <<<<<<<<<<<<<<<\n");
        System.out.println("                Important notes: \n");
        System.out.println("* These are just a couple of simple tests, just ");
        System.out.println("  covering puzzles of size 5. \n");
        System.out.println("* Test your code with additional tests and implement");
		System.out.println("  your own test suite for puzzles of size 7.\n");
    }

    /**
	 * Testa os estados possiveis das letras no enum LetterStatus:
	 */
    private static void testLetterStatus() {
        System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println(">>>>>>>>> Testing enum LetterStatus <<<<<<<<<");
		LetterStatus[] status = LetterStatus.values();

		System.out.print("> LetterStatus size: ");
        String msgTest = status.length == 3 ? "OK"
				: "\n\n ERROR: incorrect size. Expected: 3 - Found: " + status.length + " \n";
		System.out.println(msgTest);

        System.out.print("> LetterStatus elements: ");
		LetterStatus[] expectedStatus = { LetterStatus.CORRECT_POS, 
                                          LetterStatus.WRONG_POS, 
                                          LetterStatus.INEXISTENT };
		msgTest = containsAllStatus(status, expectedStatus) ? "OK"
				: "\n\n ERROR: LetterStatus does not contain the correct elements ";
		System.out.println(msgTest);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<");
    }

    /**
	 * Verifica se os valores em LetterStatus estao corretos
	 *
	 * @param userStatus valores definidos para LetterStatus
	 * @param expectedStatus valores esperados para LetterStatus
	 * @return true se userStatus e expectedStatus coincidem, false caso contrario
	 */
	private static boolean containsAllStatus(LetterStatus[] userStatus, LetterStatus[] expectedStatus) {
		boolean ret = true;
		boolean found = false;
		for (LetterStatus expSt : expectedStatus) {
			for (LetterStatus userSt : userStatus) {
				if (expSt.equals(userSt))
					found = true;
			}
			ret &= found;
			found = false;
		}
		return ret;
	}

    /**
	 * Testa as funcoes e metodos da classe Puzzle
	 * - static validWords(...)
     * - static overlappedWords(...)
     * - Puzzle (...)
	 * - size()
	 * - nrWords()
	 * - shuffleSwaps()
	 * - getLetterInHorizontalWord(...)
	 * - getLetterInVerticalWord(...)
	 * - getShuffledGrid()
	 */
	private static void testPuzzle() {
        String msgTest = "";
        System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println(">>>>>>>>>>>> Testing class Puzzle <<<<<<<<<<<<");

        testPuzzleValidationFunctions();
		System.out.print("> Testing the constructor: ");
        try {
            Puzzle puzzle = new Puzzle(size, hWords, vWords);
            msgTest = "OK";
			System.out.println(msgTest);

            System.out.print("> Testing method size: ");
			msgTest = size == puzzle.size() ? "OK" : "\n\n ERROR: incorrect size \n";
			System.out.println(msgTest);

            System.out.print("> Testing method nrWords: ");
			msgTest = nrWords == puzzle.nrWords() ? "OK" : "\n\n ERROR: incorrect number of words \n";
			System.out.println(msgTest);

            System.out.print("> Testing method shuffleSwaps: ");
			msgTest = 5*(size-3) == puzzle.shuffleSwaps() ? "OK" : 
                      "\n\n ERROR: incorrect number of shuffle swaps \n";
			System.out.println(msgTest);

            System.out.print("> Testing method getLetterInHorizontalWord: ");
            char c = puzzle.getLetterInHorizontalWord(1,3);
			msgTest = 'T' == c ? "OK" : 
                      "\n\n ERROR: incorrect letter. Expected: T - Found: " + c + " \n";
			System.out.println(msgTest);

            System.out.print("> Testing method getLetterInVerticalWord: ");
            c = puzzle.getLetterInVerticalWord(2,1);
			msgTest = 'O' == c ? "OK" : 
                      "\n\n ERROR: incorrect letter. Expected: O - Found: " + c + " \n";
			System.out.println(msgTest);

            System.out.println("> Testing method getShuffledGrid: ");
            char[][] shuffledGrid = puzzle.getShuffledGrid();
            testShuffledGrid(shuffledGrid);
        } catch (Exception e) {
			msgTest = "\n\n ERROR: Failed because of " + e.toString() + "\n";
			System.out.println(msgTest);
		}
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<");
    }

    /**
	 * Funcao auxiliar que testa as funcoes de validacao da classe Puzzle:
	 * - static validWords(...)
     * - static overlappedWords(...)
	 */
    private static void testPuzzleValidationFunctions() {
        String msgTest;
        System.out.println("> Testing function validWords");

        System.out.print("  >> on valid words: ");
        msgTest = Puzzle.validWords(size, hWords, vWords) ? "OK" : 
                  "\n\n ERROR: the function did not validate valid words \n";
		System.out.println(msgTest);

        System.out.print("  >> on invalid words: ");
        msgTest = !Puzzle.validWords(size, hWords, new String[] {"COMET", "OWNER", "ENSU"}) ? "OK" : 
                  "\n\n ERROR: the function did validate invalid words \n";
			System.out.println(msgTest);

        System.out.println("> Testing function overlappedWords");

        System.out.print("  >> on overlapping words: ");
        msgTest = Puzzle.overlappedWords(size, hWords, vWords) ? "OK" : 
                  "\n\n ERROR: the function did not validate valid words \n";
		System.out.println(msgTest);

        System.out.print("  >> on non-overlapping words: ");
        msgTest = !Puzzle.overlappedWords(size, hWords, new String[] {"COAST", "OWNER", "ENSUE"}) ? "OK" : 
                  "\n\n ERROR: the function did validate non-overlapping words \n";
			System.out.println(msgTest);
    }

    /**
	 * Funcao auxiliar que valida algumas propriedades de uma matriz baralhada
     * @param shuffledGrid matriz dada
	 */
    private static void testShuffledGrid(char[][] shuffledGrid){
        String msgTest;
        System.out.print("  >> same size: ");
        msgTest = shuffledGrid!= null && size == shuffledGrid.length &&  
                  size == shuffledGrid[0].length && size == shuffledGrid[1].length &&
                  size == shuffledGrid[2].length && size == shuffledGrid[3].length &&
                  size == shuffledGrid[4].length ? "OK" : 
                      "\n\n ERROR: incorrect size \n";
		System.out.println(msgTest);

        System.out.print("  >> rows and cols with even index have *: ");
        msgTest = shuffledGrid[1][1] == '*' && shuffledGrid[1][3] == '*' &&
                  shuffledGrid[3][1] == '*' && shuffledGrid[3][3] == '*' ? "OK" : 
                      "\n\n ERROR: positions with even index for row and column do not contain * \n";
		System.out.println(msgTest);

        System.out.print("  >> diagonals are preserved: ");
        msgTest = testShuffleDiagonals(puzzleGrid, shuffledGrid) ? "OK" : 
                      "\n\n ERROR: elements in the diagonals are not preserved \n";
		System.out.println(msgTest);
    }

    /**
     * Verifica que as diagonais do puzzle sao preservadas com o shuffle
     * @param puzzleGrid matriz original
     * @param shuffledGrid matriz baralhada
     * @return true se as diagonais sao preservadas e false caso contrario
     */
    private static boolean testShuffleDiagonals(char[][] puzzleGrid, char[][] shuffledGrid) {
        boolean diagonalsOk = true;
        for (int i = 0; i < puzzleGrid.length; i++){
            diagonalsOk &= puzzleGrid[i][i] == shuffledGrid[i][i];
        }
        // hard coded de proposito 
        // mas, como sabemos, em geral e' uma implementacao muito indesejavel
        diagonalsOk &= puzzleGrid[0][4] == shuffledGrid[0][4];
        diagonalsOk &= puzzleGrid[1][3] == shuffledGrid[1][3];
        diagonalsOk &= puzzleGrid[3][1] == shuffledGrid[3][1];
        diagonalsOk &= puzzleGrid[4][0] == shuffledGrid[4][0];
        return diagonalsOk;
    }

    /**
	 * Testa as funcoes e metodos da classe WaffleGame
	 * - static validGrid(...)
     * - WaffleGame (...)
	 * - validPosition(...)
	 * - clue(...)
	 * - swappablePosition(...)
	 * - swap(...)
	 * - maxSwaps()
     * - remainingSwaps()
     * - getCurrentGrid()
     * - puzzleFound()
     * - puzzleFound()
     * - restart()
	 */
    private static void testWaffleGame() {
        String msgTest = "";
        System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println(">>>>>>>>>> Testing class WaffleGame <<<<<<<<<<");
        try {
            testWaffleGameValidationFunctions();
		    System.out.print("> Testing the constructor: ");
            Puzzle puzzle = new Puzzle(size, hWords, vWords);
            WaffleGame game = new WaffleGame (puzzle, initialGrid);
            msgTest = "OK";
			System.out.println(msgTest);

            System.out.println("> Testing method validPosition");

            System.out.print("  >> on a valid position: ");
            msgTest = game.validPosition(1,4) ? "OK" : 
                  "\n\n ERROR: the method did not validate a valid position \n";
		    System.out.println(msgTest);

            System.out.print("  >> on invalid positions: ");
            boolean test1 = game.validPosition(2,4);
            boolean test2 = game.validPosition(0,2);
            msgTest = !test1 && !test2 ? "OK" : 
                  "\n\n ERROR: the method did validate invalid positions \n";
		    System.out.println(msgTest);
            
            System.out.println("> Testing method swappablePosition");

            System.out.print("  >> on a swappable position: ");
            msgTest = game.swappablePosition(1,2) ? "OK" : 
                     "\n\n ERROR: the method did not validate a swappable position \n";
		    System.out.println(msgTest);

            System.out.print("  >> on a non-swappable position: ");
            msgTest = !game.swappablePosition(1,1) ? "OK" : 
                      "\n\n ERROR: the method did validate a non-swappable position \n";
		    System.out.println(msgTest);

            System.out.print("> Testing method maxSwaps: ");
            msgTest = game.maxSwaps() == 5*(size-3)+5 ? "OK" : 
                      "\n\n ERROR: incorrect remaining swaps. Expected: "+ (5*(size-3)) + 
                      " - Found: " + game.maxSwaps() + " \n";
		    System.out.println(msgTest);

            System.out.print("> Testing method getCurrentGrid: ");
            char[][] userGrid = game.getCurrentGrid();
            boolean theSame = areEqual(userGrid, initialGrid);
            msgTest = theSame ? "OK" : "\n\n ERROR: incorrect current grid \n";
		    System.out.println(msgTest);

            System.out.print("> Testing method swap: ");
            game.swap(1,3,1,4);
            char[][] auxiliaryGrid = new char[][] {{'C', 'O', 'I', 'N', 'E'},
                                                   {'W', '*', 'E', '*', 'R'},
                                                   {'M', 'U', 'N', 'R', 'U'},
                                                   {'H', '*', 'O', '*', 'E'},
                                                   {'T', 'H', 'E', 'S', 'E'}
                                                  };
            boolean correctSwap = areEqual(auxiliaryGrid, game.getCurrentGrid());
            msgTest = correctSwap ? "OK" : "\n\n ERROR: swap changed more cells than it should \n";
		    System.out.println(msgTest);

            testClue(game);

            System.out.print("> Testing method remainingSwaps: ");
            int remainingSwaps = game.remainingSwaps();
            msgTest = remainingSwaps == 5*(size-3)+5-2 ? "OK" : 
                      "\n\n ERROR: incorrect remaining swaps. Expected: "+ (5*(size-3)-2) + 
                      " - Found: " + remainingSwaps + " \n";
		    System.out.println(msgTest);

            System.out.println("> Testing method puzzleFound");

            System.out.print("  >> on a found puzzle: ");
            WaffleGame gameFound = new WaffleGame (puzzle, puzzleGrid);
            msgTest = gameFound.puzzleFound() ? "OK" : 
                     "\n\n ERROR: the method did not identify a found puzzle \n";
		    System.out.println(msgTest);

            System.out.print("  >> on a puzzle not yet found: ");
            msgTest = !game.puzzleFound() ? "OK" : 
                     "\n\n ERROR: the method incorrectly identified a (yet hidden) puzzle as found \n";
		    System.out.println(msgTest);

            System.out.println("> Testing method isOver");

            System.out.print("  >> on games that are over: ");
            // fazemos swaps ate esgotar as trocas em game
            for (int i = 0; i < (5*(size-3)+5-2); i++){
                game.swap(1,4,1,3);
            }
            msgTest = gameFound.isOver() && game.isOver() ? "OK" : 
                     "\n\n ERROR: the method did not identify the terminated games as being over \n";
		    System.out.println(msgTest);

            game = new WaffleGame (puzzle, initialGrid);
            game.swap(1,2,1,3);
            System.out.print("  >> on a game that is not over yet: ");
            msgTest = !game.isOver() ? "OK" : 
                     "\n\n ERROR: the method incorrectly identified a (yet terminated) game as being over \n";
		    System.out.println(msgTest);

            System.out.print("> Testing method restart: ");
            game.restart();
            char[][] currentGrid = game.getCurrentGrid();
            boolean coincidesWithInitial = areEqual(initialGrid, currentGrid);
            msgTest = coincidesWithInitial && game.maxSwaps() == 5*(size-3)+5 ? "OK" : 
                     "\n\n ERROR: the method have not restarted the game correctly \n";
		    System.out.println(msgTest);
        } catch (Exception e) {
			msgTest = "\n\n ERROR: Failed because of " + e.toString() + "\n";
			System.out.println(msgTest);
		}
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<");
    }

    /**
	 * Funcao auxiliar que testa a funcao de validacao da classe WaffleGame:
	 * - static validGrid(...)
	 */
    private static void testWaffleGameValidationFunctions() {
        String msgTest;

        System.out.println("> Testing function validGrid");
        Puzzle puzzle = new Puzzle(size, hWords, vWords);
        System.out.print("  >> on a valid grid: ");
        msgTest = WaffleGame.validGrid(puzzle, initialGrid) ? "OK" : 
                  "\n\n ERROR: the function did not validate a valid grid \n";
		System.out.println(msgTest);

        System.out.print("  >> on invalid grids: ");
        char[][] invalidGrid1 = new char[][] {{'C', 'O', 'N', 'I', 'E'},
                                              {'W', '*', 'E', '*', 'R'},
                                              {'M', 'U', 'N', 'R', 'U'},
                                              {'H', '*', 'O', '*', 'E'},
                                              {'T', 'H', 'W', 'S', 'E'}
                                              };
        boolean test1 = WaffleGame.validGrid(puzzle, invalidGrid1);
        char[][] invalidGrid2 = new char[][] {{'C', 'O', 'N', 'I', 'E'},
                                              {'o', '*', 'E', '*', 'R'},
                                              {'M', 'U', 'N', 'R', 'U'},
                                              {'H', '*', 'O', '*', 'E'},
                                              {'T', 'H', 'E', 'S', 'E'}
                                              };
        boolean test2 = WaffleGame.validGrid(puzzle, invalidGrid2);
        msgTest = !test1 && !test2 ? "OK" : 
                  "\n\n ERROR: the function did validate an invalid grid \n";
		System.out.println(msgTest);
    }

    /**
	 * Funcao auxiliar que testa o metodo clue
	 * @param game jogo Waffle dado
	 */
    private static void testClue(WaffleGame game){
        String msgTest;
        System.out.println("> Testing method clue");
        boolean greenCluesAreCorrect = true;
        boolean yellowCluesAreCorrect = true;
        boolean uncolouredCluesAreCorrect = true;
        // comecamos com um teste com a quadricula atual
        // Es verdes
        greenCluesAreCorrect &= game.clue(1,5) == LetterStatus.CORRECT_POS &&
                                game.clue(5,5) == LetterStatus.CORRECT_POS;
        // Es amarelos
        yellowCluesAreCorrect &= game.clue(2,3) == LetterStatus.WRONG_POS &&
                                 game.clue(5,3) == LetterStatus.WRONG_POS;
        // E por colorir
        uncolouredCluesAreCorrect &= game.clue(4,5) == LetterStatus.INEXISTENT;

        // Us da terceira linha
        yellowCluesAreCorrect &= game.clue(3,2) == LetterStatus.WRONG_POS &&
                                 game.clue(3,5) == LetterStatus.WRONG_POS;

        // Os da primeira linha e terceira coluna
        yellowCluesAreCorrect &= game.clue(1,2) == LetterStatus.WRONG_POS &&
                                 game.clue(4,3) == LetterStatus.WRONG_POS;
        
        // troca do O da primeira linha para a posicao correta
        game.swap(1,2,1,3);
        // verificar que os Os ficaram bem marcados
        greenCluesAreCorrect &= game.clue(1,3) == LetterStatus.CORRECT_POS;
        yellowCluesAreCorrect &= game.clue(4,3) == LetterStatus.INEXISTENT;
        
        System.out.print("  >> letters in correct position: ");
        msgTest = greenCluesAreCorrect ? "OK" : "\n\nERROR\n";
		System.out.println(msgTest);

        System.out.print("  >> existing letters in wrong position: ");
        msgTest = yellowCluesAreCorrect ? "OK" : "\n\nERROR\n";
		System.out.println(msgTest);

        System.out.print("  >> inexistent letters: ");
        msgTest = uncolouredCluesAreCorrect ? "OK" : "\n\nERROR\n";
		System.out.println(msgTest);
    }

     /**
	 * Verifica se as matrizes dadas sao iguais
	 * @param expected matriz dada
	 * @param current matriz dada
     * @return true se expected e current sao iguais, false caso contrario
	 */
    private static boolean areEqual (char[][] expected, char[][] current){
        boolean areEq = true;
        for (int i = 0; i < expected.length; i++){
            for (int j = 0; j < expected[0].length; j++){
                areEq &= expected[i][j] == current[i][j];
            }
        }
        return areEq;
    }

}
