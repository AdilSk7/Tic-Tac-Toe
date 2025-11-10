public class SmartGameLogic extends GameLogic {
    @Override
    public void computerMove(char player) {
        System.out.println("Smart AI thinking...");
        // simple "smarter" logic — picks first empty cell
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    board[i][j] = player;
                    System.out.println("Smart AI chose: " + i + " " + j);
                    return;
                }
            }
        }
    }
}
