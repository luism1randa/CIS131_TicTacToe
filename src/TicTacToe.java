/**
 *  This is a Tic-Tac-Toe game played in the command line. Player 1 is X and Player 2
 *  is O. Players can play a match which contains a series of games. The game follows
 *  the classic 3-in-a-row rules.
 *
 *  @author Luis Miranda
 *  @version 1.0
 *
 */

public class TicTacToe {

    static final int NBR_OF_GAMES = 3;
    static final int ROWS = 3;
    static final int COLS = 3;

    static final int X_WIN = 1;
    static final int O_WIN = 2;
    static final int TIE = 3;

    /**
     * Entry point for the program
     * @param args - Not used in this program.
     */
    public static void main(String[] args) {

        String[][] gameBoard = new String[ROWS][COLS];
        int matchResult;

        do {
            displayWelcome();
            matchResult = playMatch(gameBoard);
            displayFinalResults(matchResult);

        } while (IR4.getYorN("Play again? y/n") );

        System.out.println("------Thanks for playing!------");
    }

    /**
     * Plays a match of Tic-Tac-Toe which involves a series of games. The winner
     * of each game gets their match score increased, or if a tie both players get
     * half a point.
     * @param board - 2D array that contains all spots on the board and player symbols
     * @return the match result
     */
    public static int playMatch(String[][] board){
        int gameResult;

        int xScore = 0;
        int oScore = 0;

        for (int gameNbr = 1; gameNbr <= NBR_OF_GAMES; gameNbr++) {
            System.out.println("----- Game number " + gameNbr + " -----");

            gameResult = playGame(board);
            displayBoard(board);

            if (gameResult == X_WIN){
                xScore++;
                System.out.println("X won this game");
            } else if (gameResult == O_WIN){
                oScore++;
                System.out.println("O won this game");
            } else {
                xScore += 0.5;
                oScore += 0.5;
                System.out.println("Nobody won this game. Tie!");
            }

            System.out.println("The score is: " + xScore + "-" + oScore);

            // If a user has won more than half of the games,
            // They can be declared the winner because the other
            // player can no longer catch up
            if (xScore > (NBR_OF_GAMES * .5)){
                return X_WIN;
            }

            if (oScore > (NBR_OF_GAMES * .5)){
                return O_WIN;
            }

        }

        // If neither player has won the match after all
        // games have been played, the match is a tie
        return TIE;
    }

    /**
     * Plays a single game of Tic-Tac-Toe for a given match.
     * @param board - 2D array that contains all spots on the board and player symbols
     * @return the result of the game - X Wins: 1, O: Wins 2, Tie: 3
     */
    public static int playGame (String[][] board){
        int move;
        int gameResult;

        int xPos;
        int yPos;

        final int WIN = 1;
        final int CONTINUE = 0;

        initializeBoard(board);

        do {
            displayBoard(board);

            // X's turn
            move = getPlayerMove("What is your move X?", board);

            // Calculate coordinates for X
            xPos = move / 10 - 1;
            yPos = move % 10 - 1;

            // Place X on their chosen spot
            board[xPos][yPos] = "X";
            gameResult = checkForWin(board, "X");

            if (gameResult == CONTINUE){
                displayBoard(board);

                move = getPlayerMove("What is your move O?", board);

                // Calculate coordinates for X
                xPos = move / 10 - 1;
                yPos = move % 10 - 1;

                // Place X on their chosen spot
                board[xPos][yPos] = "O";
                gameResult = checkForWin(board, "O");

                if (gameResult == WIN){
                    gameResult = O_WIN;
                }
            }

        } while (gameResult == CONTINUE);

        return gameResult; // 1 = X won, 2 = O won, 3 = tie
    }

    /**
     * Checks the board to see if a player has won, if there is a tie, or if the game should continue
     * @param board - 2D array that contains all spots on the board and player symbols
     * @param playerSymbol - Player symbol
     * @return WIN = 1, CONTINUE = 0, TIE = 3
     */
    public static int checkForWin(String[][] board, String playerSymbol){

        int row;
        int col;

        final int WIN = 1;
        final int CONTINUE = 0;

        // Check 3-in-a-line for each row
        for (row = 0; row < 3; row++) {
            if (board[row][0].equals(playerSymbol) &&
                board[row][1].equals(playerSymbol) &&
                board[row][2].equals(playerSymbol) ){
                return WIN; // Player wins
            }
        }

        // Check 3-in-a-line for each column
        for (col = 0; col < 3; col++) {
            if (board[0][col].equals(playerSymbol) &&
                board[1][col].equals(playerSymbol) &&
                board[2][col].equals(playerSymbol) ){
                return WIN; // Player wins
            }
        }

        // Check for left to right down diagonal 3-in-a-line
        if (board[0][0].equals(playerSymbol) &&
            board[1][1].equals(playerSymbol) &&
            board[2][2].equals(playerSymbol) ){
            return WIN; // Player wins
        }

        // Check for left to right up diagonal 3-in-a-line
        if (board[0][2].equals(playerSymbol) &&
            board[1][1].equals(playerSymbol) &&
            board[2][0].equals(playerSymbol) ){
            return WIN; // Player wins
        }

        // Check if the board is full
        if (boardIsFull(board)){
            return TIE; // Player has not won yet and the board is full so the game is a tie
        }

        return CONTINUE; // No player has won or tied yet, continue game
    }

    /**
     * Checks if the board is full by looking for remaining blank spaces
     * @param board - 2D array that contains all spots on the board and player symbols
     * @return True - board is full, False - board is not full
     */
    public static boolean boardIsFull(String[][] board){
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col].equals(" ")){
                    return false; // There are still blank spots on the board
                }
            }
        }

        return true; // There are no blank spots left on the board
    }

    /**
     * Prompts the user to input a spot on the board where they want to put
     * their next symbol.
     * @param prompt - message prompting the user for input
     * @param board - 2D array that contains all spots on the board and player symbols
     * @return the position input by the player
     */
    public static int getPlayerMove(String prompt, String[][] board){
        int playerMove = IR4.getInteger(prompt);

        while (isInvalid(playerMove, board)){
            playerMove = IR4.getInteger(prompt);
        }

        return playerMove;
    }

    /**
     * Checks if a player's move is in the correct format and if the spot
     * on the board has already been taken.
     * @param playerMove - spot on the board entered by the user
     * @param board - 2D array that contains all spots on the board and player symbols
     * @return True - move is invalid, False - move is valid
     */
    public static boolean isInvalid(int playerMove, String[][] board){

        final int MIN = 11;
        final int MAX = 33;

        // Check that the input is between 11 and 33. Also checks that the
        // first digit is between 1 and 3.
        if (playerMove < MIN || playerMove > MAX){
            System.out.println("Your move must be in 11 through 33 format");
            return true; // Input is outside acceptable range
        }

        // Check that the second digit is between 1 and 3. Dividing by 10
        // and getting the remainder gets the second digit
        if (playerMove % 10 != 1 && playerMove % 10 != 2 && playerMove % 10 != 3){
            System.out.println("Column values must be 1, 2, or 3.");
            return true; // Second digit is invalid
        }

        // Subtract one from each digit to get the corresponding position on the board
        int xPos = playerMove / 10 - 1;
        int yPos = playerMove % 10 - 1;

        // Check if the selected spot on the board has already been taken
        if (!board[xPos][yPos].equals(" ")){
            System.out.println("That space is already taken!");
            return true; // Invalid spot
        }

        return false; // Format and spot are valid
    }

    /**
     * Sets all spaces on a game board to blank
     * @param board - 2D array that contains all spots on the board and player symbols
     */
    public static void initializeBoard(String[][] board){
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col] = " ";
            }
        }
    }

    /**
     * Displays all spots on the board and their game state
     * @param board - 2D array that contains all spots on the board and player symbols
     */
    public static void displayBoard(String[][] board){
        System.out.println("    1   2   3");
        System.out.println("1:  " + board[0][0] + " | " + board[0][1] + " | " + board[0][2]);
        System.out.println("   ---+---+---");
        System.out.println("2:  " + board[1][0] + " | " + board[1][1] + " | " + board[1][2]);
        System.out.println("   ---+---+---");
        System.out.println("3:  " + board[2][0] + " | " + board[2][1] + " | " + board[2][2]);
    }

    /**
     * Displays the match results
     * @param result - the match result
     */
    public static void displayFinalResults(int result){

        if (result == X_WIN){
            System.out.println("X won the match");
        } else if (result == O_WIN){
            System.out.println("O won the match");
        } else {
            System.out.println("The match is a tie!");
        }

    }

    /**
     * Displays an introduction when the program is run which includes the rules
     * of the game.
     */
    public static void displayWelcome(){
        System.out.println("*************************************************************");
        System.out.println("Welcome to the Amazing game of Best-of-Three Tic-Tac-Toe!");
        System.out.println();
        System.out.println("Each player will take turns putting a mark on the board.");
        System.out.println("Players will enter row and column like this: 12 or 23.");
        System.out.println("A player will win when they get 3 of their marks in a row.");
        System.out.println("If the board is filled without 3 in a row, the game is a tie.");
        System.out.println();
        System.out.println("The best of 3 games is the winner! Good luck!");
        System.out.println("*************************************************************");
    }
}
