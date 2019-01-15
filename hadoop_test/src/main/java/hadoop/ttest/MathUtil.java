package hadoop.ttest;

public class MathUtil {

    public static double ttest(double v1[],double[] v2){
        if(v1.length==0 || v2.length==0){
            throw new IllegalArgumentException();
        }
        double avg1=0,avg2=0;
        for(double a:v1){
            avg1+=a;
        }
        for(double a:v2){
            avg2+=a;
        }
        avg1/=v1.length;
        avg2/=v2.length;

        double viriance1 = getViriance(v1, avg1),viriance2=getViriance(v2,avg2);
        return (avg1-avg2)/Math.sqrt(viriance1/v1.length+viriance2/v2.length);
    }

    private static double getViriance(double[] v1, double avg1) {
        double viriance1=0;
        for(double a:v1){
            viriance1+=(a-avg1)*(a-avg1);
        }
        return viriance1/v1.length;
    }
}
