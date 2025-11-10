import java.util.*;

public class GameLogic implements IGameLogic {
    protected char[][] board; // protected so subclasses can access
    private Random random = new Random();

    public GameLogic() {
        board = new char[3][3];
        initializeBoard();
    }

    @Override
    public void initializeBoard() {
        for (int i = 0; i < 3; i++)
            Arrays.fill(board[i], '-');
    }

    @Override
    public void printBoard() {
        System.out.println("\nCurrent Board:");
        for (char[] row : board) {
            for (char cell : row)
                System.out.print(cell + " ");
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public boolean makeMove(int row, int col, char player) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3 || board[row][col] != '-') {
            return false;
        }
        board[row][col] = player;
        return true;
    }

    @Override
    public boolean isFull() {
        for (char[] row : board)
            for (char cell : row)
                if (cell == '-') return false;
        return true;
    }

    @Override
    public boolean checkWin(char player) {
        for (int i = 0; i < 3; i++)
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player)
                return true;
        for (int i = 0; i < 3; i++)
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player)
                return true;
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
               (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    @Override
    public void computerMove(char player) {
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (board[row][col] != '-');
        System.out.println("Computer chooses: " + row + " " + col);
        board[row][col] = player;
    }
}
