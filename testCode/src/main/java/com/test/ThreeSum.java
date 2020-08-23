package com.test;

import java.util.*;

public class ThreeSum {
    public List<List<Integer>> threeSum(int[] nums) {
        int[] sortedIndexs = sortedIndex(nums);
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        Set<String> resultSet = new HashSet<String>();
        for(int i=0; i<nums.length-2; i++){
            for(int j=i+1; j<nums.length-1; j++){
                if(nums[i] + nums[j] + nums[nums.length-1]<0 || resultSet.contains(nums[i] + "," + nums[j])){
                    continue;
                }
                int bottom = j + 1;
                int top = nums.length - 1;
                while(top>=bottom){
                    int k = (top + bottom) / 2;
                    if(nums[i] + nums[j] + nums[k] == 0){
                        List<Integer> list = new ArrayList<Integer>();
                        list.add(nums[i]);
                        list.add(nums[j]);
                        list.add(nums[k]);
                        result.add(list);
                        resultSet.add(nums[i] + "," + nums[j]);
                        break;
                    }else if(nums[i] + nums[j] + nums[k] > 0){
                        top = k - 1;
                    }else{
                        bottom = k + 1;
                    }
                }
            }
        }
        return result;
    }

    public int[] sortedIndex(int[] nums){
        int[] result = new int[nums.length];
        for(int i=0; i<nums.length-1; i++){
            int index = i;
            for(int j=i+1; j<nums.length; j++){
                if(nums[index] > nums[j]){
                    index = j;
                }
            }
            if(index != i){
                int tmp = nums[i];
                nums[i] = nums[index];
                nums[index] = tmp;
            }
            result[i] = index;
        }
        return result;
    }

    public static void main(String[] args) {
        ThreeSum threeSum = new ThreeSum();
        int[] nums = new int[]{0,-4,-1,-4,-2,-3,2};
        List<List<Integer>> result = threeSum.threeSum(nums);
        System.out.println(result);
    }
}