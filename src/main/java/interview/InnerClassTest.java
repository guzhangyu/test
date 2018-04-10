package interview;

/**
 * Created by guzy on 2018-04-01.
 */
public class InnerClassTest {

    private static int c=4;

    static class StaticInnerClass{
        static  int b;
        int a=c;
    }

    class InnerClass{
        int d=c;
    }

    public static void main(String[] args) {
        System.out.println(StaticInnerClass.b);

        System.out.println(new InnerClassTest().new InnerClass().d);
    }
}
