import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Created by guzy on 16/6/28.
 */
interface CancellableTask<T> extends Callable<T>{
    void cancel();
    RunnableFuture<T> newTask();
}
public class CancellingExecutor extends ThreadPoolExecutor {
    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable){
        if(callable instanceof CancellableTask){
            return ((CancellableTask)callable).newTask();
        }else{
            return super.newTaskFor(callable);
        }
    }
}

abstract class SocketUsingTask<T> implements CancellableTask<T>{
    Socket socket;
    protected synchronized void setSocket(Socket s){
        socket=s;
    }

    public synchronized void cancel(){
        if(socket!=null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public RunnableFuture<T> newTask(){
        return new FutureTask<T>(this){
            public boolean cancel(boolean mayInterruptIfRunning){
                try{
                    SocketUsingTask.this.cancel();
                }finally{
                    return super.cancel(mayInterruptIfRunning);
                }
            }
        };
    }
}
