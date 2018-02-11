
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by guzy on 16/6/25.
 */
public class CellularAutomata {

    final Board mainBoard;

    final CyclicBarrier barrier;

    final Worker[] workers;

    public CellularAutomata(Board board){
        this.mainBoard=board;
        int count=Runtime.getRuntime().availableProcessors();
        this.barrier=new CyclicBarrier(count, new Runnable() {
            @Override
            public void run() {
                mainBoard.commitNewValues();
            }
        });
        this.workers=new Worker[count];
        for(int i=0;i<count;i++){
            workers[i]=new Worker(mainBoard.getSubBoard(count,i));
        }
    }

    class Worker implements Runnable{
        final Board board;

        public Worker(Board board){
            this.board=board;
        }

        @Override
        public void run() {
            while(!board.hasConverged()){
                for(int x=0;x<board.getMaxX();x++){
                    for(int y=0;y<board.getMaxY();y++){
                        board.setNewValue(x,y,computeValue(x,y));
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }

        private String computeValue(int x, int y) {
            return null;
        }
    }

    public void start(){
        for(int i=0;i<workers.length;i++){
            new Thread(workers[i]).start();
        }
        mainBoard.waitForConvergence();
    }

}
