package gui;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by guzy on 16/7/12.
 */
public class GuiExecutor extends AbstractExecutorService {

    private static final GuiExecutor instance=new GuiExecutor();

    public static GuiExecutor instance(){
        return instance;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void execute(Runnable command) {
        if(SwingUtilities.isEventDispatchThread()){
            command.run();
        }else{
            SwingUtilities.invokeLater(command);
        }
    }
}
