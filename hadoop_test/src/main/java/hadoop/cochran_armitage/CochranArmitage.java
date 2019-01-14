package hadoop.cochran_armitage;

import org.apache.commons.math3.distribution.NormalDistribution;

public class CochranArmitage {

    final static int[] WEIGHTS=new int[]{0,1,2};
    static NormalDistribution normalDistribution=new NormalDistribution();

    private double stat;

    private double standardStatistics;

    double variance;

    double pValue=-1;

    public double calc(int[][]countTable){
        int rowSum[]=new int[countTable.length];
        int colSum[]=new int[3];
        int totalSum=0;
        for(int i=0;i<countTable.length;i++){
            for(int j=0;j<3;j++){
                rowSum[i]+=countTable[i][j];
                colSum[j]+=countTable[i][j];
                totalSum+=countTable[i][j];;
            }
        }

        stat=0;
        standardStatistics=0;
        variance=0;
        for(int j=0;j<3;j++){
            stat+=WEIGHTS[j]*(rowSum[1]*countTable[0][j]-rowSum[0]*countTable[1][j]);
            variance+=WEIGHTS[j]*WEIGHTS[j]*colSum[j]*(totalSum-colSum[j]);

            if(j<colSum.length-1){
                for(int k=j+1;k<colSum.length;k++){
                    variance-=2*WEIGHTS[j]*WEIGHTS[k]*colSum[j]*colSum[k];
                }
            }
        }

        variance*=rowSum[0]*rowSum[1]/totalSum;

        standardStatistics=stat/Math.sqrt(variance);

        pValue=2*normalDistribution.cumulativeProbability(-Math.abs(standardStatistics));

        return pValue;
    }

    public double getStat() {
        return stat;
    }

    public double getStandardStatistics() {
        return standardStatistics;
    }

    public double getVariance() {
        return variance;
    }

    public double getpValue() {
        return pValue;
    }

    public static void main(String[] args) {
        int[][]countTable=new int[][]{
                {1386,1565,401},
                {1342,1579,434}
        };
        System.out.println(new CochranArmitage().calc(countTable));
    }
}
