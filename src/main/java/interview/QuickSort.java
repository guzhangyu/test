package interview;

public class QuickSort {

    public void quickSort(int arr[]){
        quickSort(arr,0,arr.length-1);
    }

    private void quickSort(int arr[],int start,int end){
        if(end-start<=0){
            return;
        }

//        if(end-start==1){
//            if(arr[start]>arr[end]){
//                exchange(arr,start,end);
//            }
//            return;
//        }

        int preStart=start;
        int preEnd=end;

        //根据arr[preStart]的大小，左右分隔
        while(start<end){
            while(start<end && arr[start]<=arr[preStart]){
                start++;
            }
            while(start<end && arr[end]>=arr[preStart]){
                end--;
            }
            exchange(arr, start, end);
        }

        //保证start位置的元素 <= preStart位置的元素
        if(arr[start]>arr[preStart]){
            start--;
        }
        exchange(arr,preStart,start);

        quickSort(arr,preStart,start-1);
        quickSort(arr,start+1,preEnd);
    }

    private void exchange(int[] arr, int start, int end) {
        if(start==end){
            return;
        }
        int temp=arr[start];
        arr[start]=arr[end];
        arr[end]=temp;
    }

    public static void main(String[] args) {
        int arr[]=new int[]{1,4,10,4,3,9,5,2,6,4,2,8,5,7};
        QuickSort qs=new QuickSort();
        qs.quickSort(arr);
        for(int a : arr){
            System.out.println(a);
        }
    }
}
