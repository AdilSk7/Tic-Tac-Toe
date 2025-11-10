public interface IGameLogic {
    void initializeBoard();
    void printBoard();
    boolean makeMove(int row, int col, char player);
    boolean isFull();
    boolean checkWin(char player);
    void computerMove(char player);
}
