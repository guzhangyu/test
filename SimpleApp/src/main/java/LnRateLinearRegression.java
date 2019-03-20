import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class LnRateLinearRegression {

    public static double getY(double x){
        return 1d/(Math.exp(-x)+1d);
    }

    public static void main(String[] args) {

//        System.out.println(getY(7));
//        System.out.println(getY(10));
//        System.out.println(getY(16));

        double x[][]=new double[][]{
//                {
//                    1,2,3,1
//                },{
//                    2,3,4,1
//                },{
//                    4,5,6,1
//                }
                {0.697,0.46,1},
                {0.774,0.376,1},
                {0.634,0.264,1},
                {0.608,0.318,1},
                {0.556,0.215,1},
                {0.403,0.237,1},
                {0.481,0.149,1},
                {0.437,0.211,1},
                {0.666,0.091,1},
                {0.243,0.267,1},
                {0.245,0.057,1},
                {0.343,0.099,1},
                {0.639,0.161,1},
                {0.657,0.198,1},
                {0.36,0.37,1},
                {0.593,0.042,1}


        };
        double[] y=new double[]{
                1,1,1,1,1,1,1,1,
                0,0,0,0,0,0,0,0
//                1,1,1



//                0.9990889488055994,
//                0.9999546021312976,
//                0.9999998874648379

//            0.999088948805599,0.9999546021312976,0.9999998874648379
        };

        double[] beta = calcBeta(x, y);


        System.out.println(Arrays.toString(beta));
    }

    public static double simulate(double[]x,double[]beta){
        return 1.0/(1.0+Math.exp(-multiply(x,beta)));
    }

    public static double[] calcBeta(double[][] x, double[] y) {
        double beta[]=new double[x[0].length];
        double pre_dis=0d; //两次beta之间的差距 进步的速度
        double step_speed=0d;//三次中，这两次的差距 - 上两次的差距 进步的加速度

        Deque<Double> betaChange=new ArrayDeque<>();
        int i=0;
        //假设牛顿法是以最快的速度逼近真实值
        // 当最近5次的变化平均值 小于 之前50次的变化平均值的1/7，则停止
        while(true){
            double[] d1=new double[x[0].length];
            double d2=0;

            int jj=0;
            for(double[] xj : x){
                double p1=p1(beta,xj);
                d1=add(multiply(xj,p1-y[jj++]),d1);

                d2+=multiply(xj,xj)*p1*(1-p1);
            }
            if(d2==0d){
                break;
                //throw new IllegalArgumentException(i+"");
            }
            double beta_pre[]=beta;

            beta=add(beta,multiply(d1,-1d/d2));

            double dis=absDistance(beta,beta_pre);
            double c=0.01*beta.length;
//            System.out.println(String.format("第%d次迭代    beta参数距离变化:%f beta参数变化速度:%f   beta参数变化加速度:%f  两次beta参数变化速度比值:%f",i++,dis,dis-pre_dis,(dis-pre_dis-step_speed),(dis-pre_dis)/step_speed));
            System.out.println(String.format("第%d次 beta参数变化:%s",i++, Arrays.toString(add(beta,multiply(beta_pre,-1)))));
            System.out.println(Arrays.toString(beta));
//            if(dis<c){
//                break;
//            }

            step_speed=dis-pre_dis;
            betaChange.add(step_speed);
            if(betaChange.size()>50){
                betaChange.poll();

                double avg5=0d;
                Iterator<Double> itr=betaChange.descendingIterator();
                for(int j=0;j<5;j++){
                    avg5+=itr.next();
                }
                avg5/=5;

                double avg50=0d;
                itr=betaChange.iterator();
                while(itr.hasNext()){
                    avg50+=itr.next();
                }
                avg50/=50;
                System.out.println(String.format("avg compare(%s,%s),%f",avg5,avg50,avg5/avg50));
                if(avg5/avg50<1.0/7){
                    break;
                }
            }

            pre_dis=dis;
        }
        return beta;
    }


    public static double absDistance(double[]x,double[]y){
        double distance=0d;
        for(int i=0;i<x.length;i++){
            distance+=Math.abs(x[i]-y[i]);
        }
        return distance;
    }



    public static double p1(double[]w,double[]x){
        double e=Math.exp(multiply(w,x));
        return e/(1+e);
    }

    public static double[] multiply(double []x,double y){
        double[] newA=new double[x.length];
        for(int i=0;i<x.length;i++){
            newA[i]=x[i]*y;
        }
        return newA;
    }

    public static double[] add(double []x,double y[]){
        double[] newA=new double[x.length];
        for(int i=0;i<x.length;i++){
            newA[i]=x[i]+y[i];
        }
        return newA;
    }

    public static double multiply(double []x,double []y){
        double ret=0d;
        for(int i=0;i<x.length;i++){
            ret+=x[i]*y[i];
        }
        return ret;
    }
}
