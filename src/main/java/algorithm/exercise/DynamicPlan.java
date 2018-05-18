package algorithm.exercise;

import org.apache.commons.collections.CollectionUtils;

import java.util.*;

public class DynamicPlan {


    public static void main(String[] args) {
        Integer[] weights=new Integer[]{3, 5, 7, 7, 8, 8, 23 ,3, 5, 7, 7, 8, 8, 23,3, 5, 7, 7, 8, 8, 23,3, 5, 7, 7, 8, 8, 23};
        Integer[] values=new Integer[] {4, 6, 8, 7, 9, 10, 24,4, 6, 8, 7, 9, 10, 24,4, 6, 8, 7, 9, 10, 24,4, 6, 8, 7, 9, 10, 24};
        List<Integer> chosen=new ArrayList<Integer>();
        System.out.println(new DynamicPlan().transfer01(weights,values,chosen,127));
        System.out.println(chosen);
    }



    public int transfer01(Integer[] weights,Integer[] values,List<Integer> chosen,int leftWeight){
        return transfer01Cur(weights,values,chosen,new ArrayList<Integer>(),leftWeight);
    }

    /**
     * 由动态规划问题可见：平时的最优化选择其实就是多元素 重量、性价比、价值的一个综合估计
     * @param weights 重量数组
     * @param values 价值数组
     * @param chosen 已选择的点
     * @param excludes 已排除的点
     * @param leftWeight 剩余重量
     * @return
     */
    private int transfer01Cur(Integer[] weights,Integer[] values,List<Integer> chosen,List<Integer> excludes,int leftWeight){
        List<Integer> lefts=new ArrayList<Integer>();
        for(int i=0;i<weights.length;i++){
            if(chosen.contains(i) || excludes.contains(i)){
                continue;
            }
            if(weights[i]>leftWeight){
                excludes.add(i);
                continue;
            }
            lefts.add(i);
        }


        if(lefts.size()==0){
            return 0;
        }
        if(lefts.size()==1){
            int left=lefts.get(0);
            chosen.add(left);
            return values[left];
        }

        //减小计算量
        double maxRate=0d;
        Set<Integer> maxRateIndexes=new HashSet<Integer>();
        Integer maxValue=0;
        Set<Integer> maxValueIndexes=new HashSet<Integer>();
        for(Integer i:lefts){
            double rate=values[i].doubleValue()/weights[i];
            if(maxRate<rate){
                maxRate=rate;
                maxRateIndexes.clear();
                maxRateIndexes.add(i);
            }else if(maxRate==rate){
                maxRateIndexes.add(i);
            }

            if(maxValue<values[i]){
                maxValueIndexes.clear();
                maxValueIndexes.add(i);
            }else if(maxValue==values[i]){
                maxValueIndexes.add(i);
            }
        }
       Collection<Integer> intersects= CollectionUtils.intersection(maxRateIndexes,maxValueIndexes);
        if(intersects.size()>0){
            Integer i=intersects.iterator().next();
            chosen.add(i);
            return values[i]+transfer01Cur(weights,values,chosen,excludes,leftWeight-weights[i]);
        }

        List<Integer> newChosen=null, newExcludes=null;
        maxValue=0;
        for(Integer i:lefts){
            List<Integer> copiedChosen=new ArrayList<Integer>(chosen);
            List<Integer> copiedExcludes=new ArrayList<Integer>(excludes);
//            int chosenSize=chosen.size();
//            int excludeSize=excludes.size();

            copiedChosen.add(i);
            int curValue=values[i]+transfer01Cur(weights,values,copiedChosen,copiedExcludes,leftWeight-weights[i]);
            if(maxValue<curValue){
                maxValue=curValue;
                newChosen=copiedChosen;
                newExcludes=copiedExcludes;
            }
        }

        if(newChosen!=null){
            chosen.clear();
            chosen.addAll(newChosen);
            excludes.clear();
            excludes.addAll(newExcludes);
        }
        return maxValue;
    }
}
