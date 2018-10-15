package interview;

public class InterviewTest {

    public static void main(String[] args) {
        String a = "Programming";
        String b = new String("Programming");
        String c = "Program" + "ming";
        StringBuffer p=new StringBuffer("Program");

        if(System.currentTimeMillis()%2==0){
            p.append("ming");
        }

        String d=p.toString();
        System.out.println(d);

        System.out.println(a == b);
        System.out.println(a == c);
        System.out.println(a.equals(b));
        System.out.println(a.equals(c));
        System.out.println(a.intern() == b.intern());
        System.out.println(a.intern()==d.intern());
    }
}
