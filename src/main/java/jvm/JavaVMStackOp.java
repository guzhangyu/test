package jvm;

/**
 * 单线程情况下测试栈溢出
 * Created by guzy on 16/10/8.
 */
public class JavaVMStackOp {

    int stackLength=1;

    public void stackLeak(){
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) throws Exception {
        JavaVMStackOp op=new JavaVMStackOp();
        try{
            op.stackLeak();
        }catch (Error e){
            System.out.println("stack length:"+op.stackLength);
            throw e;
        }
    }
}
