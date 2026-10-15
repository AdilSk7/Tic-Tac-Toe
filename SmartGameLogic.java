public class SmartGameLogic extends GameLogic {
    @Override
    public void computerMove(char p) {
        char o = (p=='X') ? 'O' : 'X';
        // First, try to place to win
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                if (board[i][j]=='-'){
                    board[i][j] = p;
                    if (checkWin(p)) return;
                    board[i][j] = '-';
                }
            }
        }

        // Then, try to block opponent's win
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                if (board[i][j]=='-'){
                    board[i][j] = o;
                    if (checkWin(o)){
                        board[i][j] = p; // block
                        return;
                    }
                    board[i][j] = '-';
                }
            }
        }

        // Take center if available
        if (board[1][1] == '-'){
            board[1][1] = p;
            return;
        }

        // Otherwise take first available cell (prefer corners)
        int[][] pref = {{0,0},{0,2},{2,0},{2,2},{0,1},{1,0},{1,2},{2,1}};
        for (int[] c : pref){
            int i = c[0], j = c[1];
            if (board[i][j]=='-'){
                board[i][j] = p;
                return;
            }
        }
    }
}
