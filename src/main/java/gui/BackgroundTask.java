package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.*;

/**
 * Created by guzy on 16/7/11.
 */
public abstract class BackgroundTask<V> implements Runnable,Future<V> {

    private final FutureTask<V> computation=new Computation();

    private class Computation extends FutureTask<V>{
        public Computation(){
            super(new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return BackgroundTask.this.compute();
                }
            });
        }
    }

    protected final void done(){
        GuiExecutor.instance().execute(new Runnable(){
            public void run(){
                V value=null;
                Throwable thrown=null;
                boolean cancelled=false;

                try {
                    value =get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    thrown=e.getCause();
                    e.printStackTrace();
                }catch (CancellationException e){
                    cancelled=true;
                }finally {
                    onCompletion(value,thrown,cancelled);
                }
            }
        });
    }

    protected void setProgress(final int current,final int max){
        GuiExecutor.instance().execute(new Runnable(){
            @Override
            public void run() {
                onProgress(current,max);
            }
        });
    }

    protected abstract V compute() throws Exception;

    protected void onCompletion(V result,Throwable exception,boolean cancelled){

    }

    protected void onProgress(int current,int max){

    }

    public static void main(String[] args) {
        JButton startButton=null;
        final JButton cancelButton=null;
        final JLabel label=null;
        final ExecutorService backgroundExec=Executors.newCachedThreadPool();
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                class CancelListener implements ActionListener{
                    BackgroundTask<?> task;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(task!=null){
                            task.cancel(true);
                        }
                    }
                }
                final CancelListener listener=new CancelListener();
                listener.task=new BackgroundTask<Void>() {
                    @Override
                    protected Void compute() throws Exception {
                        while(moreWork() && !isCancelled()){
                            doSomeWork();
                        }
                        return null;
                    }

                    public boolean moreWork(){
                        return true;
                    }

                    public void doSomeWork(){

                    }

                    @Override
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        return false;
                    }

                    @Override
                    public boolean isCancelled() {
                        return false;
                    }

                    @Override
                    public boolean isDone() {
                        return false;
                    }

                    @Override
                    public Void get() throws InterruptedException, ExecutionException {
                        return null;
                    }

                    @Override
                    public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                        return null;
                    }

                    @Override
                    public void run() {

                    }

                    public void onCompletion(boolean cancelled,String s,Throwable exception){
                        cancelButton.removeActionListener(listener);
                        label.setText("done");
                    }
                };
                cancelButton.addActionListener(listener);
                backgroundExec.execute(listener.task);
            }
        });
    }
}
