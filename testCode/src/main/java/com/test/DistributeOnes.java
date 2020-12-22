package com.test;

/**
 * @Author zhangyugu
 * @Date 2020/10/8 1:19 下午
 * @Version 1.0
 */
public class DistributeOnes {
    public int findMaxForm(String[] strs, int m, int n) {
        int[][] elementCounts = new int[strs.length][2];
        for(int i=0; i<strs.length; i++) {
            for(int j=0; j<strs[i].length(); j++) {
                elementCounts[i][strs[i].charAt(j) - '0'] ++;
            }
        }
        return findMaxForm(elementCounts, m, n, 0);
    }

    public int findMaxForm(int[][] elementCounts, int m, int n, int index) {
        if(index > elementCounts.length - 1) {
            return 0;
        }
        if(elementCounts[index][0] > m || elementCounts[index][1] > n) {
            return findMaxForm(elementCounts, m, n, index + 1);
        }
        int result1 = 1 + findMaxForm(elementCounts, m - elementCounts[index][0], n - elementCounts[index][1], index + 1);
        int result2 = findMaxForm(elementCounts, m, n, index + 1);
        return result1 > result2 ? result1: result2;
    }

    public static void main(String[] args) {
        int result = new DistributeOnes().findMaxForm(new String[]{"11","11","0","0","10","1","1","0","11","1","0","111","11111000","0","11","000","1","1","0","00","1","101","001","000","0","00","0011","0","10000"},
        90,
        66);
        System.out.println(result);
    }
}
