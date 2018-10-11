package jvm;

public class DeadWhile {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    System.out.println("4");
                }
            }
        }).start();
    }
}
