package jvm;

public class QuickSort {


    public static void quickSort(int[]arr,int preStart,int preEnd){
        if(preStart>=preEnd){
            return;
        }
        int start=preStart,end=preEnd;

        int pivot=start;
        while(true){
            while(pivot<end && arr[pivot]<=arr[end]){
                end--;
            }
            swap(arr,pivot,end);
            if(start>=end){
                break;
            }
            start=pivot+1;
            pivot=end;

            while(start<pivot && arr[start]<=arr[pivot]){
                start++;
            }
            swap(arr,pivot,start);
            if(start>=end){
                break;
            }
            end=pivot-1;
            pivot=start;
        }
        quickSort(arr,preStart,pivot-1);
        quickSort(arr,pivot+1,preEnd);
    }

    private static void swap(int[]arr,int i,int j){
        if(i==j){
            return;
        }
        int tmp=arr[i];
        arr[i]=arr[j];
        arr[j]=tmp;
    }

    public static void main(String[] args) {
        int[] arr=new int[]{1,5,6,2,3,11,9,7,1};

        quickSort(arr,0,arr.length-1);


        for(int k : arr){
            System.out.print(k+" ");
        }
    }
}
