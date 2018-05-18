package concurrent.tests;

/**
 * Created by guzy on 18/1/24.
 */
public class MyStack<T> {

    private Object[]data=null;

    private static final Integer INIT_SIZE=100,INC_SIZE=100;

    private void init(){
        data=new Object[INIT_SIZE];
    }
}
