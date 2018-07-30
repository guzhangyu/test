package interview;

public class Singleton {

    private Singleton(){

    }

    private static Singleton s=new Singleton();

    public static Singleton getInstance(){
        return s;
    }
}
