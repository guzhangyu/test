import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 错误率对比
 */
public class WrongRateCompare {

    public static void main(String[] args) throws IOException {
//        BigDecimal bd = new BigDecimal("-2e-05");
//        System.out.println(bd.toPlainString());
//        System.exit(0);

        Data data = readData();
        List<double[]> x=data.x;
        List<Double> y=data.y;

       List<double[]> xx0=new ArrayList<>();
        List<Double> yy0=new ArrayList<>();
        List<double[]> xx1=new ArrayList<>();
        List<Double> yy1=new ArrayList<>();
        for(int j=0;j<y.size();j++){
            if(y.get(j)==0){
                yy0.add(y.get(j));
                xx0.add(x.get(j));
            }else{
                yy1.add(y.get(j));
                xx1.add(x.get(j));
            }
        }

//        int end=(int)(x.size()*0.9);
        double rate=0.9;
        int end0=(int)(xx0.size()*rate);
        int end1=(int)(xx1.size()*rate);

        //从xx0与xx1中分别取出前0.9，作为训练组
        List<double[]> xcList=new ArrayList(xx0.subList(0,end0));
        xcList.addAll(xx1.subList(0,end1));
        double[][] xc1=list2Array(xcList);
        //从xx0与xx1中分别取出后0.1，作为测试组
        xcList=new ArrayList(xx0.subList(end0,xx0.size()));
        xcList.addAll(xx1.subList(end1,xx1.size()));
        double[][] xc2=list2Array(xcList);

        List<Double> ycList=new ArrayList(yy0.subList(0,end0));
        ycList.addAll(yy1.subList(0,end1));
        double[] yc1=list2Array2(ycList);
        ycList=new ArrayList(yy0.subList(end0,yy0.size()));
        ycList.addAll(yy1.subList(end1,yy1.size()));
        double[] yc2=list2Array2(ycList);

        crossValidate(xc1,xc2,yc1,yc2);
    }

    private static Data readData() throws IOException {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream("/Users/zhangyugu/Downloads/EMG_data_for_gestures-master/01/1_raw_data_13-12_22.03.16.txt")));

        List<double[]> x=new ArrayList<>();
        List<Double> y=new ArrayList<>();
        String line = null;
        int i=0;
        while((line=bufferedReader.readLine())!=null){
            if(i<2){
                i++;
            }else{
                String lines[]=line.split("\\s+");

                double[] xi=new double[lines.length];
                for(int j=0;j<lines.length-1;j++){
                    xi[j]=new BigDecimal(lines[j]).doubleValue();
                }
                xi[lines.length-1]=1;
                x.add(xi);

                y.add(new BigDecimal(lines[lines.length-1]).doubleValue());
            }
        }
        return new Data(x,y);
    }

    private static double[] list2Array2(List<Double> y) {
        double[] yy=new double[y.size()];
        for(int ii=0;ii<y.size();ii++){
            yy[ii]=y.get(ii);
        }
        return yy;
    }

    private static double[][] list2Array(List<double[]> x) {
        return x.toArray(new double[x.size()][x.get(0).length]);
    }


//    public static void crossValidate10(double[][]x,double[]y){
//        int end=(int)(x.length*0.9);
//        double[][]xx1= Arrays.copyOfRange(x,0,end);
//        double[][]xx2=Arrays.copyOfRange(x,end+1,y.length-1);
//        double[] y1=Arrays.copyOfRange(y,0,end);
//        double[] y2=Arrays.copyOfRange(y,end+1,y.length-1);
//
//        crossValidate(xx1, xx2, y1, y2);
//
//    }

    private static void crossValidate(double[][] xx1, double[][] xx2, double[] y1, double[] y2) {
        double[] beta=LnRateLinearRegression.calcBeta(xx1,y1);
        int j=0;
        for(double[]xx2i:xx2){
            double yp=LnRateLinearRegression.simulate(xx2i,beta);
            if(yp!=y2[j]){
                System.out.println(String.format("%f,%f",yp,y2[j]));
            }
            j++;
        }
    }
}
