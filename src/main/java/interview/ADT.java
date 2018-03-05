package interview;

public class ADT extends DT{

    public ADT(int a) {
        System.out.println(this.getClass());
        System.out.println(super.getClass());
    }

    static class A{
        public void t(){
            System.out.println(a);
        }
    }

    public static void main(String[] args) {

        Object a;
        class A{

        }
        System.out.println("fda");
        new ADT(4).t();

        new ADT(4){

        }.t();
    }

    public void t(){
        new A().t();
    }

    private static int a;

    //abstract void t();


}
