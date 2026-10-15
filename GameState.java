import java.util.*;

public class GameState {
    public IGameLogic logic;
    public char currentPlayer;
    public boolean vsComputer;
    public String player1;
    public String player2;
    public char winner; // 'X','O','-' for draw, '?' for ongoing
    public boolean finished;

    public GameState(boolean vsComputer, String player1) {
        this(vsComputer, player1, null);
    }

    public GameState(boolean vsComputer, String player1, String player2) {
        this.vsComputer = vsComputer;
        this.player1 = player1 == null || player1.isEmpty() ? "Player 1" : player1;
        if (player2 != null && !player2.isEmpty()) {
            this.player2 = player2;
        } else {
            this.player2 = vsComputer ? "Computer" : "Player 2";
        }
        this.logic = vsComputer ? new SmartGameLogic() : new GameLogic();
        this.logic.initializeBoard();
        this.currentPlayer = 'X';
        this.winner = '?';
        this.finished = false;
    }

    public synchronized boolean makeMove(int row, int col) {
        if (finished) return false;
        boolean ok = logic.makeMove(row, col, currentPlayer);
        if (!ok) return false;

        

        if (logic.checkWin(currentPlayer)) {
            winner = currentPlayer;
            finished = true;
        } else if (logic.isFull()) {
            winner = '-';
            finished = true;
        } else {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }
        return true;
    }

    public synchronized void computerTurn() {
        if (!vsComputer || finished) return;
        if (currentPlayer != 'O') return;
        logic.computerMove('O');
        if (logic.checkWin('O')) {
            winner = 'O';
            finished = true;
        } else if (logic.isFull()) {
            winner = '-';
            finished = true;
        } else {
            currentPlayer = 'X';
        }
    }

    public synchronized String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"player1\":\"").append(escape(player1)).append('\"').append(',');
        sb.append("\"player2\":\"").append(escape(player2)).append('\"').append(',');
        sb.append("\"vsComputer\":").append(vsComputer).append(',');
        sb.append("\"currentPlayer\":\"").append(currentPlayer).append('\"').append(',');
        sb.append("\"finished\":").append(finished).append(',');
        sb.append("\"winner\":\"").append(winner).append('\"').append(',');

        sb.append("\"board\":[");
        // board is protected in GameLogic, but accessible via methods: we will reflect by calling print like methods are not returning board
        // Access board via reflection to avoid changing existing classes
        try {
            Class<?> cls = logic.getClass();
            java.lang.reflect.Field f = null;
            while (cls != null) {
                try {
                    f = cls.getDeclaredField("board");
                    break;
                } catch (NoSuchFieldException nsf) {
                    cls = cls.getSuperclass();
                }
            }
            if (f != null) {
                f.setAccessible(true);
                char[][] board = (char[][]) f.get(logic);
                for (int i = 0; i < 3; i++) {
                    sb.append('[');
                    for (int j = 0; j < 3; j++) {
                        sb.append('"').append(board[i][j]).append('"');
                        if (j < 2) sb.append(',');
                    }
                    sb.append(']');
                    if (i < 2) sb.append(',');
                }
            } else {
                sb.append("[\"-\",\"-\",\"-\"],[\"-\",\"-\",\"-\"],[\"-\",\"-\",\"-\"]");
            }
        } catch (Exception e) {
            sb.append("[\"-\",\"-\",\"-\"],[\"-\",\"-\",\"-\"],[\"-\",\"-\",\"-\"]");
        }
        sb.append(']');
        sb.append('}');
        return sb.toString();
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
