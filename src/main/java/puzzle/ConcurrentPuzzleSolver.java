package puzzle;

import puzzle.MoveNode;
import puzzle.Puzzle;

import java.util.List;
import java.util.concurrent.*;

/**
 * 并发的迷宫解决方案
 * Created by guzy on 16/7/11.
 */
public class ConcurrentPuzzleSolver<P,M> {

    private final Puzzle<P,M> puzzle;

    private final ConcurrentMap<P,Boolean> seen=new ConcurrentHashMap<P,Boolean>();

    private List<M> result;

    //private ReentrantLock lock=new ReentrantLock();
    CountDownLatch latch=new CountDownLatch(1);

    public ConcurrentPuzzleSolver(Puzzle<P,M> puzzle){
        this.puzzle=puzzle;
    }

    public List<M> solve(){
        P pos=puzzle.initialPosition();
        ExecutorService executorService=  new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),new RejectedExecutionHandler(){

            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

            }
        });

        search(new MoveNode<P, M>(pos, null, null), executorService);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void search(final MoveNode<P,M> node,final ExecutorService executor){
        //如果已经有结果或者放入已经到过的点,直接返回
        if(this.result!=null || seen.putIfAbsent(node.pos,true)!=null){
            return;
        }

        if(puzzle.isGoal(node.pos)){//这一块代码不会重复进去，因为在putIfAbsent就已经拦截了
            executor.shutdown();
            result=node.asMoveList();
            latch.countDown();
            return;
        }
        for(final M m:puzzle.legalMoves(node.pos)){
           // System.out.println("dd");
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    MoveNode<P,M> next=new MoveNode<P, M>(puzzle.move(node.pos,m),m,node);
                    search(next,executor);
                }
            });
        }
    }
}
