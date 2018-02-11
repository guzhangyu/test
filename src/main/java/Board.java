/**
 * Created by guzy on 16/6/25.
 */
public class Board {

    private Board[][] boards;

    public Board(int rows,int cols){
        boards=new Board[rows][cols];
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                boards[i][j]=new Board();
            }
        }
    }

    public Board(){
    }

    public void commitNewValues() {
    }

    public Board getSubBoard(int x,int y) {
        return boards[x][y];
    }

    public boolean hasConverged() {
        return false;
    }

    public int getMaxX() {
        return 0;
    }

    public int getMaxY() {
        return 0;
    }

    public void setNewValue(int x, int y, String s) {

    }

    public void waitForConvergence() {
    }
}
