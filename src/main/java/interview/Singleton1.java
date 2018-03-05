package interview;

public class Singleton1 {

    private volatile Singleton1 s;

    private Singleton1(){

    }

    public Singleton1 getInstance(){
        if(s!=null){
            return s;
        }
        synchronized (Singleton1.class){
            if(s==null){
                s=new Singleton1();
            }
        }
        return s;
    }
}
