import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by guzy on 16/6/25.
 */
public class Preloader {

    private final FutureTask<ProductInfo> future=new FutureTask<ProductInfo>(new Callable<ProductInfo>() {
        @Override
        public ProductInfo call() throws Exception {
            return new ProductInfo();
        }
    });

    private final Thread t=new Thread(future);

    public void start(){
        t.start();
    }

    public ProductInfo get() throws Throwable {
        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return handleExecException(e);
        }
    }

    ProductInfo handleExecException(ExecutionException e) throws Throwable {
        Throwable throwable=e.getCause();
        if(throwable instanceof RuntimeException){
            throw throwable;
        }else if(throwable instanceof Error){
            throw new RuntimeException(throwable.getMessage(),throwable);
        }else {
            throw new IllegalStateException(e.getMessage(),e.getCause());
        }
    }
}
