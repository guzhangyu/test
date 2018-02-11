package jvm;

/**
 * 多线程情况下测试栈溢出
 * Created by guzy on 16/10/8.
 */
public class JavaVMStackOpMulti {

    private void dontStop(){
        while(true);
    }

    public void test(){
        while (true){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dontStop();
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        new JavaVMStackOpMulti().test();
    }
}
