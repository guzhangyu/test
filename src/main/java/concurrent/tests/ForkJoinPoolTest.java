package concurrent.tests;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ForkJoinPoolTest {

    public static void main(String[] args) {
        ForkJoinPool pool=new ForkJoinPool(4);
        int arr[]=new int[10];
        Random random=new Random();

        for(int i=0;i<10;i++){
            arr[i]=random.nextInt(20);
        }
        for(int a:arr) {
            System.out.print(a+"  ");
        }
        System.out.println();
        ForkJoinTask task=pool.submit(new SortForkJoinTask(arr,0,arr.length-1));
        try {
            task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        for(int a:arr){
            System.out.print(a+"  ");
        }
    }
}

class SortForkJoinTask extends RecursiveAction{

    int arr[];
    int start;
    int end;

    public SortForkJoinTask(int[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
//        System.out.println(String.format("%d,%d",start,end));
        if(end-start==1){
            if(arr[start]>arr[end]){
                swap(arr,start,end);
            }
        }else if(end-start>1){
            int mid=(end-start)/2;
            ForkJoinTask left=new SortForkJoinTask(arr,start,start+mid).fork();
            ForkJoinTask right=new SortForkJoinTask(arr,start+mid+1,end).fork();

            left.join();
            right.join();

            int pre=start;
            int after=start+mid+1;

            int[] cur=new int[end-start+1];
            int i=0;
            while(pre<=start+mid && after<=end){
                if(arr[pre]>arr[after]){
                    cur[i++]=arr[after++];
                }else{
                    cur[i++]=arr[pre++];
                }
            }

            while(pre<=start+mid){
                cur[i++]=arr[pre++];
            }
            while(after<=end){
                cur[i++]=arr[after++];
            }

            i=0;
            for(int j=start;j<=end;j++){
                arr[j]=cur[i++];
            }
        }
    }

    private void swap(int arr[],int start,int end) {
        int mid=arr[start];
        arr[start]=arr[end];
        arr[end]=mid;
    }
}

//class SortForkJoinTask extends RecursiveTask<int[]> {
//
//    int arr[];
//    int start;
//    int end;
//
//
//    public SortForkJoinTask(int[] arr,int start,int end){
//        this.arr=arr;
//        this.start=start;
//        this.end=end;
//    }
//
//    @Override
//    protected int[] compute() {
//        if()
//        if(end-start>1){
//
//        }
//        return new int[0];
//    }
//}
