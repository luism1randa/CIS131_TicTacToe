/**
 *  This is a Tic-Tac-Toe game played in the command line. Players can play a match which
 *  contains a series of games. The game follows the classic 3-in-a-row rules. Players can
 *  navigate the program and change their symbols using a menu system.
 *
 *  @author Luis Miranda
 *  @version 2.0
 *
 */

public class TicTacToe {

    static final int NBR_OF_GAMES = 3;
    static final int ROWS = 3;
    static final int COLS = 3;

    static final int FIRST_WINS = 1;
    static final int SECOND_WINS = 2;
    static final int TIE = 3;

    static String playerOne = "X";
    static String playerTwo = "O";

    /**
     * Entry point for the program
     * @param args - Not used in this program.
     */
    public static void main(String[] args) {

        int menuSelection;
        final int SET_SYMBOLS = 1;
        final int PLAY_GAME = 2;
        final int EXIT = 3;

        do {

            displayMainMenu();
            menuSelection = IR4.getInteger("Select a menu item: ");

            switch (menuSelection){
                case SET_SYMBOLS:
                    changeSymbols();
                    break;
                case PLAY_GAME:
                    String[][] gameBoard = new String[ROWS][COLS];
                    int matchResult; // Will store the outcome of each match

                    do {
                        displayWelcome();
                        matchResult = playMatch(gameBoard);
                        displayFinalResults(matchResult);
                    } while (IR4.getYorN("Play again? y/n") );
                case EXIT:
                    break;
                default:
                    System.err.println("Invalid selection. Try again.");
            }

        } while (menuSelection != EXIT);

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

        int playerOneScore = 0;
        int playerTwoScore = 0;

        for (int gameNbr = 1; gameNbr <= NBR_OF_GAMES; gameNbr++) {
            System.out.println("----- Game number " + gameNbr + " -----");

            gameResult = playGame(board);
            displayBoard(board);

            if (gameResult == FIRST_WINS){
                playerOneScore++;
                System.out.println(playerOne + " won this game");
            } else if (gameResult == SECOND_WINS){
                playerTwoScore++;
                System.out.println(playerTwo + " won this game");
            } else {
                playerOneScore += 0.5;
                playerTwoScore += 0.5;
                System.out.println("Nobody won this game. Tie!");
            }

            System.out.println("The score is: " + playerOneScore + "-" + playerTwoScore);

            // If a user has won more than half of the games,
            // They can be declared the winner because the other
            // player can no longer catch up
            if (playerOneScore > (NBR_OF_GAMES * .5)){
                return FIRST_WINS;
            }

            if (playerTwoScore > (NBR_OF_GAMES * .5)){
                return SECOND_WINS;
            }

        }

        // If neither player has won the match after all
        // games have been played, the match is a tie
        return TIE;
    }

    /**
     * Plays a single game of Tic-Tac-Toe for a given match.
     * @param board - 2D array that contains all spots on the board and player symbols
     * @return the result of the game - PlayerOne Wins: 1, PlayerTwo: Wins 2, Tie: 3
     */
    public static int playGame (String[][] board){
        int move;
        int gameResult;

        int xPos;
        int yPos;

        final int WIN = 1;
        final int KEEP_PLAYING = 0;

        initializeBoard(board);

        do {
            displayBoard(board);

            // playerOne's turn
            move = getPlayerMove("What is your move " + playerOne + "?", board);

            // Covert two-digit number to coordinates playerOne
            // Subtract 1 from digits to make suitable for array use
            xPos = move / 10 - 1; // First digit
            yPos = move % 10 - 1; // Second digit

            // Place playerOne on their chosen spot
            board[xPos][yPos] = playerOne;
            gameResult = checkForWin(board, playerOne);

            if (gameResult == KEEP_PLAYING){
                displayBoard(board);

                move = getPlayerMove("What is your move " + playerTwo + "?", board);

                // Covert two-digit number to coordinates playerTwo
                // Subtract 1 from digits to make suitable for array use
                xPos = move / 10 - 1; // First digit
                yPos = move % 10 - 1; // Second digit

                // Place playerTwo on their chosen spot
                board[xPos][yPos] = playerTwo;
                gameResult = checkForWin(board, playerTwo);

                if (gameResult == WIN){
                    gameResult = SECOND_WINS;
                }
            }

        } while (gameResult == KEEP_PLAYING);

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

        while (moveIsInvalid(playerMove, board)){
            // Error messages are provided by moveIsInvalid()
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
    public static boolean moveIsInvalid(int playerMove, String[][] board){

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

        if (result == FIRST_WINS){
            System.out.println(playerOne + " won the match");
        } else if (result == SECOND_WINS){
            System.out.println(playerTwo + " won the match");
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

    /**
     * Displays menu with options to play, change symbols, or exit program
     */
    public static void displayMainMenu(){
        System.out.println("------ Tic-Tac-Toe -----");
        System.out.println(" 1. Set User Symbols (" + playerOne + ", " + playerTwo +")");
        System.out.println(" 2. Play Tic-Tac-Toe");
        System.out.println(" 3. Exit");
    }

    /**
     * Displays menu with options to change symbol for playerOne, player Two, or return
     * to the main menu
     */
    public static void displaySymbolMenu(){
        System.out.println("------ Set User Symbols -----");
        System.out.println(" 1. Set Symbol for First Player: " + playerOne);
        System.out.println(" 2. Set Symbol for Second Player: " + playerTwo);
        System.out.println(" 3. Return to Main Menu");
    }

    /**
     * Presents a menu that allows players to change their symbols or
     * return to the main menu
     */
    public static void changeSymbols(){
        int menuSelection;
        final int CHANGE_PLAYER_ONE = 1;
        final int CHANGE_PLAYER_TWO = 2;
        final int EXIT = 3;

        do {
            displaySymbolMenu();
            menuSelection = IR4.getInteger("Select a menu Item: ");
            switch (menuSelection){
                case CHANGE_PLAYER_ONE:
                    playerOne = getSymbol(CHANGE_PLAYER_ONE, "What is your new symbol player 1?");
                    break;
                case CHANGE_PLAYER_TWO:
                    playerTwo = getSymbol(CHANGE_PLAYER_TWO, "What is your new symbol player 2?");
                    break;
                case EXIT:
                    break;
                default:
                    System.err.println("Invalid selection, Try again");
            }

        } while (menuSelection != EXIT);
    }

    /**
     * Asks user to input a valid new symbol
     * @param player - the player to be changed
     * @param prompt message prompting the user to input a symbol
     * @return a valid new symbol
     */
    public static String getSymbol(int player, String prompt){
        String newSymbol = IR4.getString(prompt);

        // symbolIsInvalid(newSymbol, symbolComparison)
        while (symbolIsInvalid(newSymbol, player)){
            // Error messages provided by symbolIsInvalid()
            newSymbol = IR4.getString(prompt);
        }

        return newSymbol;
    }


    /**
     * Checks if a new symbol is valid.
     * @param newPlayerSymbol new symbol input by user
     * @param secondPlayerSymbol symbol of other player to compare against
     * @return true = symbol is invalid, false = symbol is valid
     */
    public static boolean symbolIsInvalid(String newPlayerSymbol, String secondPlayerSymbol){
        final int MAX_LENGTH = 1;
        if (newPlayerSymbol.length() > MAX_LENGTH){
            System.err.println("Symbol must be 1 character. Try again.");
            return true; // Invalid
        }

        if (newPlayerSymbol.equals(secondPlayerSymbol)){
            System.err.println("Your symbol can't be the same value as the other player. Try again");
            return true; // Invalid
        }

        if (newPlayerSymbol.toLowerCase().equals(secondPlayerSymbol.toLowerCase() )){
            System.err.println("You can't enter the an uppercase or lowercase version of the other player's symbol. Try again.");
            return true; // Invalid
        }

        return false; // Symbol is valid

    }


    /**
     * Checks if a new symbol is valid.
     * @param symbol - new symbol input by the user
     * @param player - player to be changed
     * @return True - symbol is invalid, False - symbol is not invalid
     */
    public static boolean symbolIsInvalid(String symbol, int player){
        final int MAX_LENGTH = 1;
        if (symbol.length() > MAX_LENGTH){
            System.err.println("Symbol must be 1 character. Try again.");
            return true; // Invalid
        }

        final int PLAYER_ONE = 1;
        final int PLAYER_TWO = 2;

        switch (player){
            case PLAYER_ONE:
                if (symbol.equals(playerTwo)){
                    System.err.println("The first player can't enter the same value as the second. Try again.");
                    return true; // Invalid
                }

                if (symbol.toLowerCase().equals(playerTwo.toLowerCase()) ){
                    System.err.println("The first player can't enter an uppercase or lowercase version " +
                            "of the second player's value. Try again.");
                    return true; // Invalid
                }
                break;

            case PLAYER_TWO:
                if (symbol.equals(playerOne)){
                    System.err.println("The second player can't enter the same value as the first. Try again.");
                    return true; // Invalid
                }

                if (symbol.toLowerCase().equals(playerOne.toLowerCase()) ){
                    System.err.println("The second player can't enter an uppercase or lowercase version " +
                            "of the first player's value. Try again.");
                    return true; // Invalid
                }
                break;
        }

        return false; // Symbols is not invalid

    }
}
