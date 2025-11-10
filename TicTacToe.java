import java.util.*;

public class TicTacToe {
    private IGameLogic gameLogic; // Depends on abstraction, not concrete class
    private String player1, player2;
    private boolean vsComputer;

    private int player1Wins = 0;
    private int player2Wins = 0;
    private int draws = 0;

    private TicTacToe(GameBuilder builder) {
        this.player1 = builder.player1;
        this.player2 = builder.player2;
        this.vsComputer = builder.vsComputer;
        this.gameLogic = builder.gameLogic; // use polymorphism here
    }

    public void startGame() {
        Scanner sc = new Scanner(System.in);
        char choice;
        do {
            gameLogic.initializeBoard();
            char winner = playRound(sc);
            if (winner == 'X') player1Wins++;
            else if (winner == 'O') player2Wins++;
            else draws++;

            showScoreboard();
            System.out.print("Do you want to play again? (Y/N): ");
            choice = sc.next().toUpperCase().charAt(0);
        } while (choice == 'Y');

        System.out.println("\nFinal Results:");
        showScoreboard();
        System.out.println("Thanks for playing!");
        sc.close();
    }

    private char playRound(Scanner sc) {
        char currentPlayer = 'X';
        while (!gameLogic.isFull()) {
            gameLogic.printBoard();

            if (currentPlayer == 'X' || !vsComputer) {
                System.out.println("Player " + currentPlayer + " (" + (currentPlayer == 'X' ? player1 : player2) + "), enter row and column (0-2): ");
                int row = sc.nextInt(), col = sc.nextInt();
                if (!gameLogic.makeMove(row, col, currentPlayer)) {
                    System.out.println("Invalid move. Try again.");
                    continue;
                }
            } else {
                gameLogic.computerMove(currentPlayer); // polymorphism happens here
            }

            if (gameLogic.checkWin(currentPlayer)) {
                gameLogic.printBoard();
                System.out.println("Player " + currentPlayer + " (" + (currentPlayer == 'X' ? player1 : player2) + ") wins!");
                return currentPlayer;
            }
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }

        gameLogic.printBoard();
        System.out.println("It's a draw!");
        return '-';
    }

    private void showScoreboard() {
        System.out.println("\n--- Scoreboard ---");
        System.out.println(player1 + " Wins: " + player1Wins);
        System.out.println(player2 + " Wins: " + player2Wins);
        System.out.println("Draws: " + draws);
        System.out.println("------------------\n");
    }

    // Builder Pattern + Dependency Injection
    public static class GameBuilder {
        private String player1;
        private String player2;
        private boolean vsComputer;
        private IGameLogic gameLogic;

        public GameBuilder setPlayer1(String name) {
            this.player1 = name;
            return this;
        }

        public GameBuilder setPlayer2(String name) {
            this.player2 = name;
            return this;
        }

        public GameBuilder enableComputerMode(boolean vsComputer) {
            this.vsComputer = vsComputer;
            return this;
        }

        public GameBuilder setGameLogic(IGameLogic logic) { // inject dependency
            this.gameLogic = logic;
            return this;
        }

        public TicTacToe build() {
            return new TicTacToe(this);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Tic Tac Toe!");
        System.out.print("Enter Player 1 name: ");
        String p1 = sc.nextLine();

        System.out.print("Do you want to play against Smart AI? (Y/N): ");
        char mode = sc.next().toUpperCase().charAt(0);

        IGameLogic logic = (mode == 'Y') ? new SmartGameLogic() : new GameLogic();

        TicTacToe game = new GameBuilder()
                .setPlayer1(p1)
                .setPlayer2(mode == 'Y' ? "Smart AI" : "Player 2")
                .enableComputerMode(mode == 'Y')
                .setGameLogic(logic)
                .build();

        game.startGame();
    }
}
